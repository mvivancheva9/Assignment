package com.example.admin.youlocal;

public class User {
    private String avatar;
    private String fullname;
    private String info;

    public User(String avatar, String fullname, String info) {
        this.avatar = avatar;
        this.fullname = fullname;
        this.info = info;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
