package com.beliyvlastelin.snakes;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.beliyvlastelin.snakes.game.GameCell;
import com.beliyvlastelin.snakes.game.Snake;
import com.beliyvlastelin.snakes.game.TypeCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static com.beliyvlastelin.snakes.Constants.GAME_INFO;
import static com.beliyvlastelin.snakes.Constants.IS_ADMIN;
import static com.beliyvlastelin.snakes.Constants.JSON_RESULT;
import static com.beliyvlastelin.snakes.Constants.MAX_NUMBER_USERS;
import static com.beliyvlastelin.snakes.Constants.NUMBER_USERS;
import static com.beliyvlastelin.snakes.Constants.OTHER_SNAKE;
import static com.beliyvlastelin.snakes.Constants.RESULT_ERROR;
import static com.beliyvlastelin.snakes.Constants.ROOM_LIST;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME;
import static com.beliyvlastelin.snakes.Constants.SNAKE;
import static com.beliyvlastelin.snakes.Constants.USERS;
import static com.beliyvlastelin.snakes.Constants.X;
import static com.beliyvlastelin.snakes.Constants.Y;
import static com.beliyvlastelin.snakes.Constants.YOUR_SNAKE;

/**
 * Created by mikola on 05.12.2016.
 */

public class ManagerRequests {

    private static Socket client;
    private static ManagerRequests manager;

    InputStream sin = null;
    OutputStream sout = null;

    DataInputStream in;
    DataOutputStream out;


    private ManagerRequests(String ip, int port) {
        try {
            //   Log.d("Tag", "pre");
            client = new Socket();
            client.setTcpNoDelay(true);
            client.connect(new InetSocketAddress(ip, port));

            sin = client.getInputStream();
            sout = client.getOutputStream();
            in = new DataInputStream(new BufferedInputStream(sin));
            out = new DataOutputStream(new BufferedOutputStream(sout));

            //  Log.d("Tag", "after");


        } catch (IOException e) {
            e.printStackTrace();
            // Log.d("Tag", "error");
        }
    }


    public static ManagerRequests get(String ip, int port) {
        if (manager == null && client == null)
            return ManagerRequests.manager = new ManagerRequests(ip, port);
        else return ManagerRequests.manager;
    }

    public ManagerRequests(){

    }

    public void sendRequest(String type, HashMap<String, String> param) {
        in = new DataInputStream(new BufferedInputStream(sin));
        out = new DataOutputStream(new BufferedOutputStream(sout));
        try {
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
            writeMessage(out, jsonObject.toString().getBytes(), jsonObject.toString().length());

            // отсылаем введенную строку текста серверу.
            out.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String getResponce() {

        try {
            //Отримуємо відпровідь від сервера
            Log.d("Tag", "getResponce()");
            String responce = new String(readMessage(in));

            Log.d("Tag", "responce = " + responce);
            return responce;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "null";
    }

    public static void writeMessage(DataOutputStream dout, byte[] msg, int msgLen) throws IOException {
        dout.write(msg, 0, msgLen);
        dout.flush();
    }

    public static byte[] readMessage(DataInputStream din) throws IOException {
        int msgLen = din.readInt();
        byte[] msg = new byte[msgLen];
        din.readFully(msg);
        return msg;
    }

    public static String getSimpleResult(String JSON) {
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            String result = jsonObject.getString(JSON_RESULT);
            Log.d("Tag", "result = " + result);
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
            JSONArray rooms = jsonObject.getJSONArray(ROOM_LIST);
            for (int i = 0; i < rooms.length(); i++) {
                JSONObject roomObj = rooms.getJSONObject(i);
                Room room = new Room();
                String nameRoom = roomObj.getString(ROOM_NAME);
                String numberUsers = roomObj.getString(NUMBER_USERS);
                String maxNumberUsers = roomObj.getString(MAX_NUMBER_USERS);
                room.setNameRoom(nameRoom);
                room.setMaxUserCount(Integer.parseInt(maxNumberUsers));
                room.setUserCount(Integer.parseInt(numberUsers));
                list.add(room);
            }
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
            for (int i = 0; i < users.length(); i++) {
                JSONObject userObj = users.getJSONObject(i);
                User user = new User();
                String nameUser = userObj.getString(ROOM_NAME);
                Boolean admin = userObj.getBoolean(IS_ADMIN);
                user.setName(nameUser);
                user.setAdmin(admin);

                list.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Collection<? extends Snake> getSnakesCoordinates(String JSON) {
        ArrayList<Snake> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            String info = jsonObject.getString(GAME_INFO);

            JSONArray yourSnake = jsonObject.getJSONArray(YOUR_SNAKE);
            Snake mSnake = new Snake();
            ArrayList<GameCell> mSnakeCells = new ArrayList<>();
            for (int i = 0; i < yourSnake.length(); i++) {
                JSONObject xy = yourSnake.getJSONObject(i);

                int x = xy.getInt(X);
                int y = xy.getInt(Y);
                GameCell cell;
                if (i == 0) {
                    cell = new GameCell(x, y, TypeCell.SNAKE_HEAD_CELL);
                    mSnake.setHead(cell);
                } else {
                    cell = new GameCell(x, y, TypeCell.SNAKE_PART_CELL);
                    mSnakeCells.add(cell);
                }

            }
            mSnake.setBody(mSnakeCells);
            list.add(mSnake);

            JSONArray otherSnakes = jsonObject.getJSONArray(OTHER_SNAKE);
            for (int i = 0; i < otherSnakes.length(); i++) {
                JSONObject otherSnake = otherSnakes.getJSONObject(i);
                JSONArray otherSnakeC = otherSnake.getJSONArray(SNAKE);
                mSnake = new Snake();
                mSnakeCells = new ArrayList<>();
                for (int j = 0; j < otherSnakeC.length(); j++) {
                    JSONObject xy = otherSnakeC.getJSONObject(j);

                    int x = xy.getInt(X);
                    int y = xy.getInt(Y);
                    GameCell cell;
                    if (j == 0) {
                        cell = new GameCell(x, y, TypeCell.SNAKE_HEAD_CELL);
                        mSnake.setHead(cell);
                    } else {
                        cell = new GameCell(x, y, TypeCell.SNAKE_PART_CELL);
                        mSnakeCells.add(cell);
                    }
                }
                mSnake.setBody(mSnakeCells);
                list.add(mSnake);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}

