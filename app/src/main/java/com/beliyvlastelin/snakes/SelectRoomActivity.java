package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class SelectRoomActivity extends AppCompatActivity {
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

        getAllRoom();


    }

    void getAllRoom() {
        // отримати всі кімнати з сервера
        new ListRoomsRequest().execute();
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


    private class ListRoomsRequest extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            HashMap<String, String> map = new HashMap<>();
            String responce = ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_ROOM_LIST, map);

            String result = ManagerRequests.getSimpleResult(responce);
            if (result.equals(RESULT_SUCCESSFUL)) {
                roomItems.addAll(ManagerRequests.getListRoom(responce));
            } else {
            }

            return null;
        }


    }
}



