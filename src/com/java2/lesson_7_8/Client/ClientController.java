package com.java2.lesson_7_8.Client;

import com.java2.lesson_7_8.User;

import java.util.List;

public interface ClientController {
    void disconnect();
    void sendTextMessage(String msg);
    void sendTextMessage(String toUser, String msg);
    void logIn(String nickName, String pass);
    User getUser();
    List<String> getUsersList();
}
