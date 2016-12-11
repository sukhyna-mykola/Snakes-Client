package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME_KEY;


/**
 * Created by mikola on 16.10.2016.
 */

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.RoomViewHolder> {


    List<Room> rooms;
    SelectRoomActivity parentActivity;


    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_room_item, parent, false);
        RoomViewHolder pvh = new RoomViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {

        final Room room = rooms.get(position);
        holder.roomName.setText(room.getNameRoom());
        holder.roomAdmin.setText(room.getAdminRoom());

        holder.roomType.setImageResource(R.drawable.public_type);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JoinRoomRequest().execute(room.getNameRoom());
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


    ListRoomAdapter(List<Room> rooms, SelectRoomActivity parentActivity) {
        this.rooms = rooms;
        this.parentActivity = parentActivity;
    }


    public class RoomViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView roomName;
        TextView roomAdmin;
        ImageView roomType;

        RoomViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            roomName = (TextView) itemView.findViewById(R.id.room_name);
            roomAdmin = (TextView) itemView.findViewById(R.id.room_admin);
            roomType = (ImageView) itemView.findViewById(R.id.room_photo);


        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private class JoinRoomRequest extends AsyncTask<String, Void, String> {
        private String roomName;

        @Override
        protected String doInBackground(String... params) {
            roomName = params[0];
            HashMap<String, String> map = new HashMap<>();
            map.put(ROOM_NAME, roomName);

            ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_JOIN_TO_ROOM, map);
            String responce = ManagerRequests.get(Constants.ip, Constants.port).getResponce();

            return ManagerRequests.getSimpleResult(responce);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {
                Intent intent = new Intent(parentActivity, RoomActivity.class);
                intent.putExtra(ROOM_NAME_KEY, roomName);
                parentActivity.startActivity(intent);
                Log.d("Tag", "start RoomActivity");
            } else {

            }

        }
    }

}
