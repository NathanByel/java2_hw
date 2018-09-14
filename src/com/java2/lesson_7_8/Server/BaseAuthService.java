package com.java2.lesson_7_8.Server;

import java.io.IOException;

public class BaseAuthService implements AuthService {
    @Override
    public void start() {

    }

    @Override
    public boolean checkUser(String nickName, String pass) {
        return true;
    }

    @Override
    public void close() throws IOException {

    }
}
