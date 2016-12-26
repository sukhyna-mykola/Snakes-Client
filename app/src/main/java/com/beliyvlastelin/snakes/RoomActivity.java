package com.beliyvlastelin.snakes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Random;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME_KEY;

public class RoomActivity extends Activity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView listPlayer;
    private List<User> mUsers = new ArrayList<>();
    private String nameRoom;
    private CustomFontTextView nameRoomTextView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        listPlayer = (RecyclerView) findViewById(R.id.list_of_players);
        nameRoom = getIntent().getStringExtra(ROOM_NAME_KEY);
        nameRoom = "ROOM";
        nameRoomTextView = (CustomFontTextView) findViewById(R.id.name_room);
        nameRoomTextView.setText(nameRoom);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_romm);
        refreshLayout.setColorSchemeColors(Color.GREEN,Color.RED,Color.YELLOW,Color.BLUE);
        refreshLayout.setOnRefreshListener(this);

        listPlayer.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listPlayer.setLayoutManager(llm);

    }

    @Override
    public void onBackPressed() {
        new ExitFromRoom().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllPlayers();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_room:
                for (User user : mUsers) {
                    if (user.getName().equals(MenuActivity.nameStr))
                        if (user.isAdmin()) {
                            new StartGameRequest().execute();
                        }
                }
                break;
            case R.id.back_room:
                new ExitFromRoom().execute();
                break;

            case R.id.synchron_room:
                getAllPlayers();
                break;
        }
    }

    private void getAllPlayers() {
        new ListUsersRequest().execute(nameRoom);

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        getAllPlayers();
        refreshLayout.setRefreshing(false);
    }

    /**
     * Отримати всіх граваців з кімнати
     */
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
            }return ManagerRequests.getSimpleResult(responce);


        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {
                ListPlayerAdapter mListPlayerAdapter = new ListPlayerAdapter(mUsers,RoomActivity.this);
                listPlayer.setAdapter(mListPlayerAdapter);
                for (User user : mUsers) {
                    if (user.getName().equals(MenuActivity.nameStr))
                        if (!user.isAdmin()) {
                            new StartGame().execute();
                        }
                }

            } else {

            }

        }
    }

    /**
     * Запит на початок гри(тільки від адміна)
     */
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

    /**
     * Очікування на початок гри
     */
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

    /**
     * Вихід з кімнати
     */
    private class ExitFromRoom extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<>();
            ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_EXITFROMROOM, map);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }

}
