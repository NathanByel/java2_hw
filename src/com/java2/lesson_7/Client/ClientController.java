package com.java2.lesson_7.Client;

import com.java2.lesson_7.User;

import java.util.List;

public interface ClientController {
    void disconnect();
    void sendMessage(String msg);
    void sendMessage(String toUser, String msg);
    void logIn(String nickName, String pass);
    User getUser();
    List<String> getUsersList();
}
