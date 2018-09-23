package com.java2.lesson_7_8.Server;

import com.java2.lesson_7_8.User;
import com.java2.lesson_7_8.UserInfo;

import java.io.Closeable;

public interface AuthService extends Closeable {
    void start();
    boolean checkUser(User user);
    UserInfo getUserInfo(User user);
}
