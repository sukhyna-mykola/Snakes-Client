package com.beliyvlastelin.snakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME_KEY;
import static com.beliyvlastelin.snakes.R.string.reg;

/**
 * Created by mikola on 05.12.2016.
 */

public class DialogEndGameFragment extends DialogFragment implements View.OnClickListener {

    private Button seeGame;
    private Button exitToRoom;
    public static boolean showFirst = true;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_end, null);

        seeGame = (Button) v.findViewById(R.id.see_end);
        exitToRoom = (Button) v.findViewById(R.id.exit_end);

        seeGame.setOnClickListener(this);
        exitToRoom.setOnClickListener(this);
        showFirst = false;

        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_end: {
                dismiss();
                break;

            }
            case R.id.exit_end: {
                Intent intent = (new Intent(getContext(), RoomActivity.class));
                intent.putExtra(ROOM_NAME_KEY, RoomActivity.nameRoom);
                startActivity(intent);
                break;
            }
        }

    }


}
