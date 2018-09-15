package com.java2.lesson_7_8.Messages;

import com.java2.lesson_7_8.User;

public class PrivateMessage extends Message {
    private User toUser;
    private String from;
    public PrivateMessage(String from, User toUser, String msg) {
        super(MessageType.PRIVATE_MESSAGE, msg);
        this.from = from;
    }

    public User getToUser() {
        return toUser;
    }

    @Override
    public String serialize() {
        return "PM " + " " + from + ": " + super.data.toString();
    }

    @Override
    public boolean deserialize(String data) {
        return false;
    }
}
