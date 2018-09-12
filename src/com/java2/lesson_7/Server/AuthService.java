package com.java2.lesson_7.Server;

import java.io.Closeable;

public interface AuthService extends Closeable {
    void start();
    String getNickByLoginPass(String login, String pass);
}
