package com.java2.lesson_7;

public class User {
    private final String nickName;
    private final String pass;

    public User(String nickName, String pass) {
        this.nickName = nickName;
        this.pass = pass;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPass() {
        return pass;
    }
}
