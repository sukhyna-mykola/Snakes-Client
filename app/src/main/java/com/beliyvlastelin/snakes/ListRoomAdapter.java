package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by mikola on 16.10.2016.
 */

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.RoomViewHolder>  {


    List<Room> rooms;
    MenuActivity parentActivity;
    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_room_item, parent, false);
        RoomViewHolder pvh = new RoomViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        holder.roomName.setText(rooms.get(position).getNameRoom());
        holder.roomAdmin.setText(rooms.get(position).getAdminRoom());
        if (rooms.get(position).isAccess())
            holder.roomType.setImageResource(R.drawable.public_type);
        else
            holder.roomType.setImageResource(R.drawable.private_type);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }



    ListRoomAdapter(List<Room> rooms) {

        this.rooms = rooms;
    }



    public static class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            cv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
          v.getContext().startActivity(new Intent(v.getContext(),RoomActivity.class));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
