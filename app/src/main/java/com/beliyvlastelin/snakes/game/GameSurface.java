package com.beliyvlastelin.snakes.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by mikola on 01.12.2016.
 */

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private Game mGame;
    private Paint p;
    DrawThread drawThread;

    public GameSurface(Context context, Game game) {
        super(context);
        getHolder().addCallback(this);
        this.mGame = game;
        p = new Paint();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawThread = new DrawThread(holder);
        drawThread.setRunning(true);
        //drawThread.start();
        new Update().execute();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    class DrawThread extends Thread {

        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;
                    drawGame(canvas);
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

            }
        }
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
            draw();
            new Update().execute();
        }
    }
}


