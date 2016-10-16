package com.beliyvlastelin.snakes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText name;
    EditText password;
    Button reg;
    SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText) findViewById(R.id.login_user_name);
        password = (EditText) findViewById(R.id.login_password);
        reg = (Button) findViewById(R.id.login_reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr =  name.getText().toString();
                String passwordStr = password.getText().toString();
                saveUserInfo(nameStr,passwordStr);
                Intent intent = new Intent();
                intent.putExtra(Constants.USER_NAME_KEY,nameStr);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
    void saveUserInfo(String nameStr,String passwordStr) {
        userPref = getSharedPreferences(Constants.USER_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor ed = userPref.edit();
        ed.putString(Constants.USER_NAME_KEY,nameStr);
        ed.putString(Constants.USER_PASSWORD_KEY,passwordStr);
        ed.commit();
    }
}
