package com.beliyvlastelin.snakes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
                startActivity(new Intent(mContext, GameActivity.class));
            }
        });

        nameRoom = getIntent().getStringExtra(ROOM_NAME_KEY);
        getActionBar().setTitle(nameRoom);

        listPlayer.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listPlayer.setLayoutManager(llm);
        getAllPlayers();


    }


    void getAllPlayers() {
        mUsers.add(mUser);
        new ListUsersRequest().execute(nameRoom);
        //  mUsers.add(new User("User 1", false, 0, false));
        //  mUsers.add(new User("User 2", false, 0, true));
        //  mUsers.add(new User("User 3", true, 0, false));

    }

    private class ListUsersRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> map = new HashMap<>();

            map.put(ROOM_NAME, params[0]);

            String responce = ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_USER_LIST, map);

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
            } else {

            }

        }
    }
}
