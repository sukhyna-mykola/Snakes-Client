package com.beliyvlastelin.snakes;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.REQUEST_ACTION;
import static com.beliyvlastelin.snakes.Constants.REQUEST_PARAM;

/**
 * Created by mikola on 05.12.2016.
 */

public class ManagerRequests {

    private static Socket client;
    private static ManagerRequests manager;



    private ManagerRequests(String ip, int port) {
        try {
            client = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Tag", "error");
        }
    }


    public static ManagerRequests get(String ip, int port) {
        if (manager == null && client==null)
            return manager = new ManagerRequests(ip, port);
        else return manager;
    }

    public String sendRequest(String type, HashMap<String, String> param) {
        InputStream sin = null;
        OutputStream sout = null;
        try {

            sin = client.getInputStream();
            sout = client.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

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
            String responce = in.readUTF();

            in.close();
            out.close();

            return responce;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "null";
    }

    public String getResponceWithCordinats(){
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



}

