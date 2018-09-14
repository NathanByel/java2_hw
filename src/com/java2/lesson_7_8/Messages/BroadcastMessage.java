package com.java2.lesson_7_8.Messages;

public class BroadcastMessage extends Message {
    public BroadcastMessage(String msg) {
        super(MessageType.BROADCAST_MESSAGE, msg);
    }
}
