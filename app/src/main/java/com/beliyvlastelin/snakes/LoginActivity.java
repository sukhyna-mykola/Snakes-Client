package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.USER_NAME_KEY;
import static com.beliyvlastelin.snakes.Constants.WAIT_DIALOG_TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DialogRegFragment.Callbacks {
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
                getSupportFragmentManager().beginTransaction().add(new DialogRegFragment(), "RegDialog").commit();
                break;
            }

            case R.id.login_enter: {
                nameStr = name.getText().toString();
                passwordStr = password.getText().toString();
                new EnterRequest().execute(nameStr, passwordStr);
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

        new EnterRequest().execute(nameStr, passwordStr);
    }


    private class EnterRequest extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String responce = ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(params[0],params[1]);
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
            }else {
                Toast.makeText(LoginActivity.this, "Помилка", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void dismissDialog() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(WAIT_DIALOG_TAG);
        if (prev != null) {
            DialogWaitFragment df = (DialogWaitFragment) prev;
            df.dismiss();
        }
    }
}
