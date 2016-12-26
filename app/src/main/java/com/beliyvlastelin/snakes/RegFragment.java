package com.beliyvlastelin.snakes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;

/**
 * Created by mikola on 05.12.2016.
 */

public class RegFragment extends DialogFragment implements View.OnClickListener {
    private EditText name;
    private EditText password;
    private CustomFontTextView statusReg;
    private Button reg;

    private Callbacks mCallbacks;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reg, null);
        name = (EditText) v.findViewById(R.id.login_user_name);
        password = (EditText) v.findViewById(R.id.login_password);
        statusReg = (CustomFontTextView) v.findViewById(R.id.status_reg);
        reg = (Button) v.findViewById(R.id.login_reg);
        reg.setOnClickListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }

    @Override
    public void onClick(View v) {

        new SendRequest().execute(name.getText().toString(), password.getText().toString());
    }


    public interface Callbacks {
        void setName(String name, String password);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (LoginActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class SendRequest extends AsyncTask<String, Void, String> {
        private String nameStr;
        private String passwordStr;

        @Override
        protected String doInBackground(String... params) {
            nameStr = params[0];
            passwordStr = params[1];

            HashMap<String, String> map = new HashMap<>();

            map.put(USER_NAME,nameStr );
            map.put(USER_PASSWORD, passwordStr);

            ManagerRequests.get(Constants.ip, Constants.port).sendRequest(Constants.POST_REQUEST_CREATEUSER, map);
            String responce =  ManagerRequests.get(Constants.ip, Constants.port).getResponce();
            return ManagerRequests.getSimpleResult(responce);

        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {
                Log.d("Tag", s);
                dismiss();
                mCallbacks.setName(nameStr,passwordStr);
            } else {

            }
            statusReg.setText(s);
        }
    }


}
