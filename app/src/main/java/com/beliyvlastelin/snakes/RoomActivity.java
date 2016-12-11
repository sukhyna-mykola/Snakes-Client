package com.beliyvlastelin.snakes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME_KEY;

public class RoomActivity extends AppCompatActivity {

    private User mUser;
    private RecyclerView listPlayer;
    private List<User> mUsers = new ArrayList<>();
    private Button play;
    private Context mContext;
    private String nameRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        mContext = this;

        mUser = new User(MenuActivity.USER_NAME, false, 0, false);
        listPlayer = (RecyclerView) findViewById(R.id.list_of_players);
        play = (Button) findViewById(R.id.room_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (User user : mUsers) {
                    if (user.getName().equals(MenuActivity.USER_NAME))
                        if (user.isAdmin()) {
                             new StartGameRequest().execute();
                        }
                }

            }
        });

        nameRoom = getIntent().getStringExtra(ROOM_NAME_KEY);
        //getActionBar().setTitle(nameRoom);

        listPlayer.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listPlayer.setLayoutManager(llm);
        getAllPlayers();



    }


    void getAllPlayers() {
        new ListUsersRequest().execute(nameRoom);

    }

    private class ListUsersRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> map = new HashMap<>();

            map.put(ROOM_NAME, params[0]);

            ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_USER_LIST, map);
            String responce = ManagerRequests.get(Constants.ip, Constants.port).getResponce();

            String result = ManagerRequests.getSimpleResult(responce);
            if (result.equals(RESULT_SUCCESSFUL)) {
                mUsers.addAll(ManagerRequests.getUsersInRoom(responce));
            } else {
            }

            return ManagerRequests.getSimpleResult(responce);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {
                ListPlayerAdapter mListPlayerAdapter = new ListPlayerAdapter(mUsers);
                listPlayer.setAdapter(mListPlayerAdapter);
                for (User user : mUsers) {
                    if (user.getName().equals(MenuActivity.USER_NAME))
                        if (!user.isAdmin()) {
                            new StartGame().execute();
                        }
                }

            } else {

            }

        }
    }

    private class StartGameRequest extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<>();
            ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_START_GAME, map);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new StartGame().execute();
        }
    }

    private class StartGame extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String responce = ManagerRequests.get(Constants.ip, Constants.port).getResponce();
            return ManagerRequests.getSimpleResult(responce);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {
                Intent intent = new Intent(RoomActivity.this, GameActivity.class);
                startActivity(intent);
                Log.d("Tag", "start RoomActivity");
            } else {

            }

        }
    }


}
