package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;
import static com.beliyvlastelin.snakes.Constants.WAIT_DIALOG_TAG;

public class SelectRoomActivity extends AppCompatActivity implements CreateRoomFragment.Callbacks, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView listRoom;
    private List<Room> roomItems = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);
        listRoom = (RecyclerView) findViewById(R.id.list_of_room);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_select_romm);
        refreshLayout.setColorSchemeColors(Color.GREEN,Color.RED,Color.YELLOW,Color.BLUE);
        refreshLayout.setOnRefreshListener(this);
        listRoom.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listRoom.setLayoutManager(llm);
    }

    void getAllRoom() {
        // отримати всі кімнати з сервера
        new ListRoomsRequest().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllRoom();
    }


    @Override
    public void setUpdateListRoom() {
        getAllRoom();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back_select_room:
                finish();
                break;

            case R.id.synchron_select_room:
                getAllRoom();
                break;

            case R.id.create_room_select_room:
                getSupportFragmentManager().beginTransaction().add(new CreateRoomFragment(), "CreateRoomDialog").commit();
                break;
        }


    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        getAllRoom();
        refreshLayout.setRefreshing(false);
    }


    private class ListRoomsRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {



            String responce =   ManagerRequests.checkConnect(Constants.ip, Constants.port).listRoomsRequest(MenuActivity.nameStr,MenuActivity.passwordStr);
            String result = ManagerRequests.getSimpleResult(responce);
            if (result.equals(RESULT_SUCCESSFUL)) {
                roomItems.addAll(ManagerRequests.getListRoom(responce));
            } else {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            ListRoomAdapter mListPlayerAdapter = new ListRoomAdapter(roomItems, SelectRoomActivity.this);
            listRoom.setAdapter(mListPlayerAdapter);

        }
    }

    public void dismissDialog() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(WAIT_DIALOG_TAG);
        if (prev != null) {
            WaitFragment df = (WaitFragment) prev;
            df.dismiss();
        }
    }
}



