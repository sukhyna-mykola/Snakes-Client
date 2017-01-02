package com.beliyvlastelin.snakes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.beliyvlastelin.snakes.Constants.RESULT_ERROR;
import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_NAME_KEY;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences userPref;

    public static String nameStr;
    public static String passwordStr;

    public static final int REQUEST_CODE = 1;

    public static String VERSION_CODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        readVerionCode();


        loadUserInfo();
        //startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE);

        if (nameStr == null) {
             startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE);
        } else {
             //new EnterRequest().execute(nameStr, passwordStr);
        }

    }

    private void readVerionCode() {
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
        nameStr = userPref.getString(Constants.USER_NAME_KEY, null);
        passwordStr = userPref.getString(Constants.USER_PASSWORD_KEY, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            nameStr = data.getStringExtra(Constants.USER_NAME_KEY);
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
                break;
            }

            case R.id.menu_settings: {
                Snackbar.make(v, "Покищо недоступно", Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void createDialogInfo() {

        new AlertDialog.Builder(this)
                .setMessage("Version: " + VERSION_CODE)
                .setTitle("Info").show();


    }

    private class EnterRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String responce = ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(params[0],params[1]);
            return ManagerRequests.getSimpleResult(responce);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(RESULT_SUCCESSFUL)) {
                Toast.makeText(MenuActivity.this, RESULT_SUCCESSFUL, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MenuActivity.this, RESULT_ERROR, Toast.LENGTH_SHORT).show();
            }

        }
    }


}