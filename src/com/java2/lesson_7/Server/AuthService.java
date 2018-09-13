package com.java2.lesson_7.Server;

import java.io.Closeable;

public interface AuthService extends Closeable {
    void start();
    boolean checkUser(String nickName, String pass);
}
