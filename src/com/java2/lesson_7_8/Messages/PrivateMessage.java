package com.java2.lesson_7_8.Messages;

import com.java2.lesson_7_8.User;

public class PrivateMessage extends Message {
    private User toUser;
    public PrivateMessage(User toUser, String msg) {
        super(MessageType.PRIVATE_MESSAGE, msg);
    }

    public User getToUser() {
        return toUser;
    }
}
