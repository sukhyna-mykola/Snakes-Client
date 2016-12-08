package com.beliyvlastelin.snakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;

/**
 * Created by mikola on 05.12.2016.
 */

public class RegFragment extends DialogFragment implements View.OnClickListener {
    private EditText name;
    private EditText password;
    private TextView statusReg;
    private Button reg;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.reg_fragment, null);
        name = (EditText) v.findViewById(R.id.login_user_name);
        password = (EditText) v.findViewById(R.id.login_password);
        statusReg = (TextView) v.findViewById(R.id.status_reg);
        reg = (Button) v.findViewById(R.id.login_reg);
        reg.setOnClickListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }

    @Override
    public void onClick(View v) {
        createRegRequest(name.getText().toString(), password.getText().toString());
    }

    private void createRegRequest( String name,  String password) {

        new SendRequest().execute(name,password);
    }

    private class SendRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> map = new HashMap<>();

            map.put(USER_NAME, params[0] );
            map.put(USER_PASSWORD, params[1]);

            String responce = ManagerRequests.get(Constants.ip,Constants.port).sendRequest(Constants.POST_REQUEST_CREATEUSER, map);


            return responce;
        }

        @Override
        protected void onPostExecute(String s)
        {
            statusReg.setText(s);
        }
    }


}
