package com.beliyvlastelin.snakes;

import java.util.ArrayList;

/**
 * Created by mikola on 16.10.2016.
 */

public class Room {
    public Room() {
    }

    private String nameRoom;
    private String adminRoom;
    private int userCount;
    private int maxUserCount;

    public int getMaxUserCount() {
        return maxUserCount;
    }

    public void setMaxUserCount(int maxUserCount) {
        this.maxUserCount = maxUserCount;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public String getAdminRoom() {
        return adminRoom;
    }

    public void setAdminRoom(String adminRoom) {
        this.adminRoom = adminRoom;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public Room(String nameRoom, String adminRoom, int userCount, int maxUserCount) {
        this.nameRoom = nameRoom;
        this.adminRoom = adminRoom;
        this.userCount = userCount;
        this.maxUserCount = maxUserCount;

    }
}
