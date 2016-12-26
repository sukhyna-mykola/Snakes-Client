package com.beliyvlastelin.snakes;

/**
 * Created by mikola on 16.10.2016.
 */

public class User {
    private String name;
    private int photo;
    private boolean admin;

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


    public User(String name, int photo, boolean admin) {
        this.name = name;
        this.photo = photo;
        this.admin = admin;
    }
}
