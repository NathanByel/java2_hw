package com.java2.lesson_7.Server;

import java.io.IOException;

public class BaseAuthService implements AuthService {
    @Override
    public void start() {

    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
