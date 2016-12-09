package com.beliyvlastelin.snakes;

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

public class ListPlayerAdapter extends RecyclerView.Adapter<ListPlayerAdapter.PlayerViewHolder> {
    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_player_item, parent, false);
        PlayerViewHolder pvh = new PlayerViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        holder.playerName.setText(mUsers.get(position).getName());
        holder.playerState.setText(String.valueOf(mUsers.get(position).isState()));
        holder.plaerPhoto.setImageResource(R.drawable.public_type);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    List<User> mUsers;

    ListPlayerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView playerName;
        TextView playerState;
        ImageView plaerPhoto;

        PlayerViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            playerName = (TextView) itemView.findViewById(R.id.player_name);
            playerState = (TextView) itemView.findViewById(R.id.player_state);
            plaerPhoto = (ImageView) itemView.findViewById(R.id.player_photo);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
