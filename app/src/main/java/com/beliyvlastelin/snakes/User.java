package com.beliyvlastelin.snakes;

/**
 * Created by mikola on 16.10.2016.
 */

public class User {
    String name;
    boolean state;
    int photo;
    boolean admin;

    public User() {
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public User(String name, boolean state, int photo, boolean admin) {
        this.name = name;
        this.state = state;
        this.photo = photo;
        this.admin = admin;
    }
}
