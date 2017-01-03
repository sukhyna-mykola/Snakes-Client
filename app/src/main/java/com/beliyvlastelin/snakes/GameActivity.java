package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.beliyvlastelin.snakes.game.Game;
import com.beliyvlastelin.snakes.game.GameSurface;

import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.COURSE;
import static com.beliyvlastelin.snakes.Constants.RESPONCE_DOWN;
import static com.beliyvlastelin.snakes.Constants.RESPONCE_RIGHT;
import static com.beliyvlastelin.snakes.Constants.RESPONCE_UP;
import static com.beliyvlastelin.snakes.Constants.RESPONCE_lEFT;
import static com.beliyvlastelin.snakes.Constants.RESULT_ERROR;
import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_NAME_KEY;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;

public class GameActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {


    private FrameLayout frame;
    private GameSurface mGameSurface;
    private Game mGame;
    private SimpleGestureFilter detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        frame = (FrameLayout) findViewById(R.id.game_layout);
        detector = new SimpleGestureFilter(this, this);
        frame.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        mGame = new Game(frame.getWidth(), frame.getHeight());
                        mGameSurface = new GameSurface(GameActivity.this, mGame);
                        frame.addView(mGameSurface);
                        frame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });


    }

    @Override
    public void onSwipe(int direction) {

        SendCourseRequest sendCourseRequest = new SendCourseRequest();
        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                sendCourseRequest.execute(RESPONCE_RIGHT);
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                sendCourseRequest.execute(RESPONCE_lEFT);
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                sendCourseRequest.execute(RESPONCE_DOWN);
                break;
            case SimpleGestureFilter.SWIPE_UP:
                sendCourseRequest.execute(RESPONCE_UP);
                break;
        }

    }

    @Override
    public void onDoubleTap() {
        Toast.makeText(this, "двічі", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }


    private class SendCourseRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            ManagerRequests.checkConnect(Constants.ip, Constants.port).changeCourseRequest(params[0],RoomActivity.nameRoom,MenuActivity.nameStr,MenuActivity.passwordStr);
            //String responce =  ManagerRequests.get(Constants.ip, Constants.port).getResponce();
            return RESULT_SUCCESSFUL;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals(RESULT_SUCCESSFUL)) {

            } else {

            }

        }
    }
}
