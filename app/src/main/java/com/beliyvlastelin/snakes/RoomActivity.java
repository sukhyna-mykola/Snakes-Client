package com.beliyvlastelin.snakes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    Player mPlayer;
    RecyclerView listPlayer;
    List<Player> players = new ArrayList<>();
    Button play;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        mContext = this;
        mPlayer = new Player(MenuActivity.USER_NAME, false, 0, false);
        listPlayer = (RecyclerView) findViewById(R.id.list_of_players);
        play = (Button) findViewById(R.id.room_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, GameActivity.class));
            }
        });
        listPlayer.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listPlayer.setLayoutManager(llm);
        getAllPlayers();

        ListPlayerAdapter mListPlayerAdapter = new ListPlayerAdapter(players);
        listPlayer.setAdapter(mListPlayerAdapter);
    }


    void getAllPlayers() {
        players.add(mPlayer);
       // players.add(new Player("Player 1", false, 0, false));
        //players.add(new Player("Player 2", false, 0, true));
      //  players.add(new Player("Player 3", true, 0, false));

    }
}
