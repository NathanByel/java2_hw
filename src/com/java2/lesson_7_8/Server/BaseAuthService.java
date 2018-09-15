package com.java2.lesson_7_8.Server;

import com.java2.lesson_7_8.User;
import com.java2.lesson_7_8.UserInfo;

import java.io.IOException;

public class BaseAuthService implements AuthService {
    @Override
    public void start() {

    }

    @Override
    public UserInfo getUserInfo(User user) {
        return new UserInfo(user.getNickName(), user.getPass()) ;
    }

    @Override
    public boolean checkUser(User user) {
        return true;
    }

    @Override
    public void close() throws IOException {

    }
}
