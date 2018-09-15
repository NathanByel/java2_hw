package com.java2.lesson_7_8.Channel;

import com.java2.lesson_7_8.Messages.Message;

import java.io.Closeable;

public interface DataChannel extends Closeable {
    void open();
    boolean sendMessage(Message msg);
    Message getMessage();
}
