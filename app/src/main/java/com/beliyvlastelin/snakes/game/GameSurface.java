package com.beliyvlastelin.snakes.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.beliyvlastelin.snakes.DialogEndGameFragment;
import com.beliyvlastelin.snakes.GameActivity;
import com.beliyvlastelin.snakes.RoomActivity;

import static com.beliyvlastelin.snakes.Constants.ROOM_NAME_KEY;

/**
 * Created by mikola on 01.12.2016.
 */

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private Game mGame;
    private Paint p;
    private GameActivity gameActivity;

    public GameSurface(Context context, Game game) {
        super(context);
        getHolder().addCallback(this);
        this.mGame = game;
        this.gameActivity = (GameActivity) context;
        p = new Paint();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        new Update().execute();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public void draw() {

        Canvas c = null;
        try {
            c = getHolder().lockCanvas(null);
            synchronized (getHolder()) {
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                drawGame(c);
            }

        } finally {
            // do this in a finally so that if an exception is thrown
            // during the above, we don't leave the Surface in an
            // inconsistent state
            if (c != null) {
                getHolder().unlockCanvasAndPost(c);
            }
        }
    }

    private void drawGame(Canvas canvas) {

        for (GameCell[] rowCell : mGame.getGamePlace().getGameCells()) {
            for (GameCell cell : rowCell) {
                switch (cell.getTypeCell()) {
                    case BONUS_CELL: {
                        p.setColor(Color.BLUE);
                        canvas.drawRect(createRect(cell), p);
                        break;
                    }
                    case SNAKE_HEAD_CELL: {
                        p.setColor(Color.YELLOW);
                        canvas.drawRect(createRect(cell), p);
                        break;
                    }
                    case SNAKE_PART_CELL: {
                        p.setColor(Color.RED);
                        canvas.drawRect(createRect(cell), p);
                        break;
                    }
                    case GAME_CELL: {
                        p.setColor(Color.GREEN);
                        canvas.drawRect(createRect(cell), p);
                        break;
                    }
                }
            }
        }

    }

    private RectF createRect(GameCell cell) {
        return new RectF(cell.getX() * mGame.getWidhtCell(), cell.getY() * mGame.getHeihgtCell(),
                cell.getX() * mGame.getWidhtCell() + mGame.getWidhtCell(), cell.getY() * mGame.getHeihgtCell() + mGame.getHeihgtCell());
    }


    public class Update extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mGame.update();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mGame.getGamePlace().setDead(true);
            if (!mGame.getGamePlace().isGameOver()) {
                if(mGame.getGamePlace().isDead()){
                    if(DialogEndGameFragment.showFirst){
                        gameActivity.getSupportFragmentManager().beginTransaction()
                                .add(new DialogEndGameFragment(),"DialogEndGameFragment").commit();
                    }
                }
                draw();
                new Update().execute();
            } else {
                Intent intent = new Intent(gameActivity, RoomActivity.class);
                intent.putExtra(ROOM_NAME_KEY, RoomActivity.nameRoom);
                gameActivity.startActivity(intent);
            }

        }
    }
}

