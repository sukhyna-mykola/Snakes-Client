package com.beliyvlastelin.snakes;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

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

import static com.beliyvlastelin.snakes.Constants.COURSE;
import static com.beliyvlastelin.snakes.Constants.GAME_INFO;
import static com.beliyvlastelin.snakes.Constants.IS_ADMIN;
import static com.beliyvlastelin.snakes.Constants.JSON_RESULT;
import static com.beliyvlastelin.snakes.Constants.MAX_NUMBER_USERS;
import static com.beliyvlastelin.snakes.Constants.MAX_PERIOD;
import static com.beliyvlastelin.snakes.Constants.MIN_PERION;
import static com.beliyvlastelin.snakes.Constants.NUMBER_USERS;
import static com.beliyvlastelin.snakes.Constants.OTHER_SNAKE;
import static com.beliyvlastelin.snakes.Constants.RESULT_ERROR;
import static com.beliyvlastelin.snakes.Constants.RESULT_SUCCESSFUL;
import static com.beliyvlastelin.snakes.Constants.ROOM_LIST;
import static com.beliyvlastelin.snakes.Constants.ROOM_NAME;
import static com.beliyvlastelin.snakes.Constants.SNAKE;
import static com.beliyvlastelin.snakes.Constants.SYSTEM_ERROR;
import static com.beliyvlastelin.snakes.Constants.USERS;
import static com.beliyvlastelin.snakes.Constants.USER_NAME;
import static com.beliyvlastelin.snakes.Constants.USER_PASSWORD;
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

    private static boolean connect;


    private ManagerRequests(String ip, int port) {
        try {
            Log.d("Tag", "pre");
            client = new Socket();
            client.setTcpNoDelay(true);

            client.connect(new InetSocketAddress(ip, port), 1000);

            sin = client.getInputStream();
            sout = client.getOutputStream();
            in = new DataInputStream(new BufferedInputStream(sin));
            out = new DataOutputStream(new BufferedOutputStream(sout));

            connect = true;
            Log.d("Tag", "after");


        } catch (Exception e) {
            e.printStackTrace();
            manager = new ManagerRequests();
            Log.d("Tag", "constructor error");
        }
    }


    public static ManagerRequests checkConnect(String ip, int port) {
        if (!connect) {
            Log.d("Tag", "connection = false");
            return ManagerRequests.manager = new ManagerRequests(ip, port);
        } else {
            Log.d("Tag", "connection = true");
            return ManagerRequests.manager;
        }
    }

    public ManagerRequests() {
        Log.d("Tag", "public constructor");
    }

    public String sendRequest(String type, HashMap<String, String> param) {
        try {
            in = new DataInputStream(new BufferedInputStream(sin));
            out = new DataOutputStream(new BufferedOutputStream(sout));

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

            writeMessage(out, jsonObject.toString().getBytes(), jsonObject.toString().length());

            // отсылаем введенную строку текста серверу.
            out.flush();

            return RESULT_SUCCESSFUL;
        } catch (Exception e) {
            e.printStackTrace();
            connect = false;
            Log.d("Tag", "send error");
            return SYSTEM_ERROR;
        }


    }

    public String getResponce() {

        try {
            //Отримуємо відпровідь від сервера
            Log.d("Tag", "getResponce()");
            String responce = new String(readMessage(in));

            Log.d("Tag", "responce = " + responce);
            return responce;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Tag", "get error");
            connect = false;
            return SYSTEM_ERROR;
        }

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
            return SYSTEM_ERROR;
        }
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

    public String enterRequest(String name, String pass) {

        HashMap<String, String> map = new HashMap<>();
        map.put(USER_NAME, name);
        map.put(USER_PASSWORD, pass);

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;

            res = sendRequest(Constants.POST_REQUEST_SIGNIN, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String regRequest(String name, String pass) {

        HashMap<String, String> map = new HashMap<>();
        map.put(USER_NAME, name);
        map.put(USER_PASSWORD, pass);

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;

            res = sendRequest(Constants.POST_REQUEST_CREATEUSER, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String listRoomsRequest(final String name, final String pass) {

        HashMap<String, String> map = new HashMap<>();


        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = sendRequest(Constants.POST_REQUEST_ROOM_LIST, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String joinRoomRequest(String roomName, final String name, final String pass) {

        HashMap<String, String> map = new HashMap<>();
        map.put(ROOM_NAME, roomName);

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = sendRequest(Constants.POST_REQUEST_JOIN_TO_ROOM, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String createRoomRequest(String roomName, final String name, final String pass) {

        HashMap<String, String> map = new HashMap<>();
        map.put(ROOM_NAME, roomName);

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = sendRequest(Constants.POST_REQUEST_CREATE_ROOM, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String exitRoomRequest(final String roomName, final String name, final String pass) {

        HashMap<String, String> map = new HashMap<>();

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = sendRequest(Constants.POST_REQUEST_EXITFROMROOM, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).joinRoomRequest(roomName, name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String startGameRequest(final String roomName, final String name, final String pass) {

        HashMap<String, String> map = new HashMap<>();

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = sendRequest(Constants.POST_REQUEST_START_GAME, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).joinRoomRequest(roomName, name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String listUsersRequest(final String roomName, final String name, final String pass) {

        HashMap<String, String> map = new HashMap<>();
        map.put(ROOM_NAME, roomName);

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = sendRequest(Constants.POST_REQUEST_USER_LIST, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).joinRoomRequest(roomName, name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String startGame(final String roomName, final String name, final String pass) {


        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).joinRoomRequest(roomName, name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }
    public String update() {


        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

    public String changeCourseRequest(String course, final String roomName, final String name, final String pass) {


        HashMap<String, String> map = new HashMap<>();
        map.put(COURSE, course);

        boolean exec = true;
        int time = 0;
        int nextTime = MIN_PERION;
        String res = new String();

        while (exec && time < MAX_PERIOD) {

            time = nextTime;
            nextTime = time * 2;


            res = sendRequest(Constants.POST_REQUEST_CHANGECOURSE, map);
            if (!res.equals(SYSTEM_ERROR))
                res = getResponce();

            if (res.equals(SYSTEM_ERROR)) {
                final int finalTime = time;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        Log.d("Tag", "Повтор : " + finalTime + " ms");
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).enterRequest(name, pass);
                        ManagerRequests.checkConnect(Constants.ip, Constants.port).joinRoomRequest(roomName, name, pass);
                    }
                }, finalTime * 1000);

            } else {
                exec = false;
                return res;
            }
        }
        return res;
    }

}