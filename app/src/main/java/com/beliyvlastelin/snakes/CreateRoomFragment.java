package com.beliyvlastelin.snakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME;

/**
 * Created by mikola on 05.12.2016.
 */

public class CreateRoomFragment extends DialogFragment implements View.OnClickListener {
    private EditText nameRoom;
    private TextView statusReg;
    private Button reg;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_room, null);
        nameRoom = (EditText) v.findViewById(R.id.input_room_name);

        statusReg = (TextView) v.findViewById(R.id.status_reg);
        reg = (Button) v.findViewById(R.id.login_reg);
        reg.setOnClickListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }

    @Override
    public void onClick(View v) {
      new CreateRoomRequest().execute(nameRoom.getText().toString());
    }

    private class CreateRoomRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> map = new HashMap<>();
            map.put(ROOM_NAME, params[0]);

            String responce = ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_CREATE_ROOM, map);
            return ManagerRequests.getSimpleResult(responce);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {

            } else {

            }

        }
    }


}
