package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.MAX_PERIOD;
import static com.beliyvlastelin.snakes.Constants.MIN_PERION;
import static com.beliyvlastelin.snakes.Constants.RESULT_ERROR;
import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.SYSTEM_ERROR;
import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_NAME_KEY;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;
import static com.beliyvlastelin.snakes.Constants.WAIT_DIALOG_TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RegFragment.Callbacks {
    private EditText name;
    private EditText password;


    private String nameStr;
    private String passwordStr;

    SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText) findViewById(R.id.login_user_name);
        password = (EditText) findViewById(R.id.login_password);


    }

    private void saveUserInfo(String nameStr, String passwordStr) {
        userPref = getSharedPreferences(Constants.USER_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = userPref.edit();
        ed.putString(USER_NAME_KEY, nameStr);
        ed.putString(Constants.USER_PASSWORD_KEY, passwordStr);
        ed.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_reg: {
                getSupportFragmentManager().beginTransaction().add(new RegFragment(), "RegDialog").commit();
                break;
            }

            case R.id.login_enter: {
                nameStr = name.getText().toString();
                passwordStr = password.getText().toString();
                new EnterRequest().execute(nameStr, passwordStr, MIN_PERION);
                break;
            }
        }
    }

    @Override
    public void setName(String nameReg, String passwordReg) {

        nameStr = nameReg;
        passwordStr = passwordReg;

        this.name.setText(nameStr);
        this.password.setText(passwordStr);

        new EnterRequest().execute(nameStr, passwordStr, MIN_PERION);
    }


    private class EnterRequest extends AsyncTask<String, Void, String> {
        private int curTime;
        private int nextTime;

        @Override
        protected String doInBackground(String... params) {

            curTime = Integer.parseInt(params[2]);
            nextTime *= 2;


            HashMap<String, String> map = new HashMap<>();

            map.put(USER_NAME, params[0]);
            map.put(USER_PASSWORD, params[1]);
            try {
                ManagerRequests.get(Constants.ip, Constants.port)
                        .sendRequest(Constants.POST_REQUEST_SIGNIN, map);
            } catch (Exception e) {
                return SYSTEM_ERROR;
            }
            String responce = ManagerRequests.get(Constants.ip, Constants.port).getResponce();
            return ManagerRequests.getSimpleResult(responce);
        }

        @Override
        protected void onPostExecute(String s) {
            dismissDialog();
            if (s.equals(RESULT_SUCCESSFUL)) {
                //saveUserInfo(nameStr, passwordStr);
                Intent intent = new Intent();
                intent.putExtra(USER_NAME_KEY, nameStr);
                setResult(RESULT_OK, intent);
                finish();
            } else if (s.equals(RESULT_ERROR)) {
                Toast.makeText(LoginActivity.this, RESULT_ERROR, Toast.LENGTH_SHORT).show();
            } else {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (curTime < MAX_PERIOD) {
                            new EnterRequest().execute(nameStr, passwordStr, String.valueOf(nextTime));
                            Toast.makeText(LoginActivity.this, "період = " + curTime, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, curTime);
            }

        }
    }

    public void dismissDialog() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(WAIT_DIALOG_TAG);
        if (prev != null) {
            WaitFragment df = (WaitFragment) prev;
            df.dismiss();
        }
    }
}
