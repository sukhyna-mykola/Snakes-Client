package com.beliyvlastelin.snakes;

import java.util.ArrayList;

/**
 * Created by mikola on 16.10.2016.
 */

public class Room {
    String nameRoom;
    String adminRoom;
    int plaerCount;
    boolean access;

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

    public int getPlaerCount() {
        return plaerCount;
    }

    public void setPlaerCount(int plaerCount) {
        this.plaerCount = plaerCount;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public Room(String nameRoom, String adminRoom, int plaerCount, boolean access) {
        this.nameRoom = nameRoom;
        this.adminRoom = adminRoom;
        this.plaerCount = plaerCount;
        this.access = access;
    }
}
