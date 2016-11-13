package com.beliyvlastelin.snakes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences userPref;

    public static String USER_NAME;
    public static final int REQUEST_CODE = 1;

    TextView userName;
    public  static String VERSION_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        readVerionCode();
        userName = (TextView) findViewById(R.id.menu_user_name);
        loadUserInfo();
        if (USER_NAME == null) {
            startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE);
        } else
            userName.setText(USER_NAME);

    }
    private void readVerionCode(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(this.getAssets().open("version.txt")));

           VERSION_CODE = reader.readLine();

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }


        }
    }

    void loadUserInfo() {
        userPref = getSharedPreferences(Constants.USER_PREFERENCES, MODE_PRIVATE);
        USER_NAME = userPref.getString(Constants.USER_NAME_KEY, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            USER_NAME = data.getStringExtra(Constants.USER_NAME_KEY);
            userName.setText(USER_NAME);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_play: {
                startActivity(new Intent(this, SelectRoomActivity.class));
                break;
            }
            case R.id.menu_exit: {
                finish();
                break;
            }
            case R.id.menu_info: {
                createDialogInfo();
            }
        }
    }

    private void createDialogInfo() {



            new AlertDialog.Builder(this)
                    .setMessage("Version: " + VERSION_CODE)
                    .setTitle("Info").show();


    }
}