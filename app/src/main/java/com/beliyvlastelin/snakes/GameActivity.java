package com.beliyvlastelin.snakes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.beliyvlastelin.snakes.game.Game;
import com.beliyvlastelin.snakes.game.GameSurface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class GameActivity extends AppCompatActivity {
    private FrameLayout frame;
    private GameSurface mGameSurface;
    private Game mGame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGame = new Game(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels);
        mGameSurface = new GameSurface(this, mGame);
        mGameSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGame.update();
                mGameSurface.draw();
            }
        });
        frame = (FrameLayout) findViewById(R.id.game_layout);
        frame.addView(mGameSurface);

    }
}
