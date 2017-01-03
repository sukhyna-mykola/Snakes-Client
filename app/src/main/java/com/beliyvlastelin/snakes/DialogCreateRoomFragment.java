package com.beliyvlastelin.snakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME_KEY;

/**
 * Created by mikola on 05.12.2016.
 */

public class DialogCreateRoomFragment extends DialogFragment implements View.OnClickListener {
    private EditText nameRoom;
    private CustomFontTextView statusReg;
    private CustomFontButton reg;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_room, null);
        nameRoom = (EditText) v.findViewById(R.id.input_room_name);

        statusReg = (CustomFontTextView) v.findViewById(R.id.status_reg);
        reg = (CustomFontButton) v.findViewById(R.id.login_reg);
        reg.setOnClickListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }

    private DialogCreateRoomFragment.Callbacks mCallbacks;

    public interface Callbacks {
        void setUpdateListRoom();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (SelectRoomActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onClick(View v) {
        new CreateRoomRequest().execute(nameRoom.getText().toString());
    }

    private class CreateRoomRequest extends AsyncTask<String, Void, String> {
        private String roomName;

        @Override
        protected String doInBackground(String... params) {

            roomName = params[0];

            String responce = ManagerRequests.checkConnect(Constants.ip, Constants.port)
                    .createRoomRequest(roomName,MenuActivity.nameStr,MenuActivity.passwordStr);

            return ManagerRequests.getSimpleResult(responce);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {
                Intent intent = new Intent(getActivity(), RoomActivity.class);
                intent.putExtra(ROOM_NAME_KEY, roomName);
                getActivity().startActivity(intent);
            } else {
                mCallbacks.setUpdateListRoom();
            }
            dismiss();
        }
    }


}
