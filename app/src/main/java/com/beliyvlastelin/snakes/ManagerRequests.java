package com.beliyvlastelin.snakes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.JSON_RESULT;
import static com.beliyvlastelin.snakes.Constants.RESULT_ERROR;
import static com.beliyvlastelin.snakes.Constants.USERS;

/**
 * Created by mikola on 05.12.2016.
 */

public class ManagerRequests {

    private static Socket client;
    private static ManagerRequests manager;


    private ManagerRequests(String ip, int port) {
        try {
            Log.d("Tag", "pre");
            client = new Socket();
            client.setTcpNoDelay(true);
            client.setSoTimeout(100000);
            client.connect(new InetSocketAddress(ip,port));
            Log.d("Tag", "after");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Tag", "error");
        }
    }


    public static ManagerRequests get(String ip, int port) {
        if (manager == null && client == null)
            return manager = new ManagerRequests(ip, port);
        else return manager;
    }

    public String sendRequest(String type, HashMap<String, String> param) {
        InputStream sin = null;
        OutputStream sout = null;
        try {

            sin = client.getInputStream();
            sout = client.getOutputStream();

            DataInputStream in = new DataInputStream(new BufferedInputStream(sin));
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sout));

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObjectParam = new JSONObject();

            for (HashMap.Entry entry : param.entrySet()) {
                try {
                    jsonObjectParam.put((String) entry.getKey(), entry.getValue());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                jsonObject.put(type, jsonObjectParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Tag", jsonObject.toString());
            out.writeUTF(jsonObject.toString());
            // отсылаем введенную строку текста серверу.
            out.flush();
            //Отримуємо відпровідь від сервера
            Log.d("Tag", "pre responce");
            String responce = in.readUTF();
            Log.d("Tag", "after responce");
            in.close();
            out.close();

            return responce;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "null";
    }

    public String getResponceWithCordinats() {
        InputStream sin = null;

        try {

            sin = client.getInputStream();
            DataInputStream in = new DataInputStream(sin);

            //Отримуємо відпровідь від сервера
            String responce = in.readUTF();

            in.close();
            return responce;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "null";
    }

    public static String getSimpleResult(String JSON) {
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            Log.d("Tag", "obj");
            String result = jsonObject.getString(JSON_RESULT);
            Log.d("Tag", "result");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RESULT_ERROR;
    }

    public static Collection<? extends Room> getListRoom(String JSON) {
        ArrayList<Room> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            String result = jsonObject.getString(JSON_RESULT);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Collection<? extends User> getUsersInRoom(String JSON) {
        ArrayList<User> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            JSONArray users = jsonObject.getJSONArray(USERS);
            for (int i = 0; i <users.length() ; i++) {
                JSONObject userObj = users.getJSONObject(i);
                User user = new User();

                //*****************

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return list;
    }
}

