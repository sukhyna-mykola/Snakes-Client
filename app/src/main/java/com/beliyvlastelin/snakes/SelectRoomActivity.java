package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
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

public class SelectRoomActivity extends AppCompatActivity implements CreateRoomFragment.Callbacks {
    RecyclerView listRoom;
    List<Room> roomItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);
        listRoom = (RecyclerView) findViewById(R.id.list_of_room);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_room_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_room:
                getSupportFragmentManager().beginTransaction().add(new CreateRoomFragment(), "CreateRoomDialog").commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setUpdateListRoom() {
        getAllRoom();
    }


    private class ListRoomsRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> map = new HashMap<>();
            ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_ROOM_LIST, map);
            String responce = ManagerRequests.get(Constants.ip, Constants.port).getResponce();
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



