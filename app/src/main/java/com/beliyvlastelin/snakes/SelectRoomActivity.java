package com.beliyvlastelin.snakes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectRoomActivity extends AppCompatActivity {
    RecyclerView listRoom;
    List<Room> roomItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);
        listRoom = (RecyclerView) findViewById(R.id.list_of_room);
        listRoom.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listRoom.setLayoutManager(llm);
        getAllRoom();

        ListRoomAdapter mListRoomAdapter = new ListRoomAdapter(roomItem);
        listRoom.setAdapter(mListRoomAdapter);
    }

    void getAllRoom() {
        // отримати всі кімнати з сервера
        roomItem.add(new Room("room 1", "Player 1", 1, true));
        roomItem.add(new Room("room 2", "Player 2", 2, false));
        roomItem.add(new Room("room 3", "Player 3", 3, true));
        roomItem.add(new Room("room 4", "Player 4", 4, false));
        roomItem.add(new Room("room 5", "Player 5", 5, true));

    }
}
