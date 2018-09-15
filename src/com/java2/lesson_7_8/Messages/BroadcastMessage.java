package com.java2.lesson_7_8.Messages;

import com.java2.lesson_7_8.User;

import java.util.UUID;

public class BroadcastMessage extends Message {
    private String from;
    public BroadcastMessage(String from, String msg) {
        super(MessageType.BROADCAST_MESSAGE, msg);
        this.from = from;
    }

    @Override
    public String serialize() {
        //if(getUser() != null) {
        //    return super.uuid + ":" + type + ":" + getUser().getNickName() + ":" + getUser().getPass();
        //}
        return null;
    }

    @Override
    public boolean deserialize(String data) {
        String[] fields = data.split(":");
        if (fields.length != 4) return false;
        if ( MessageType.valueOf(fields[1]) != MessageType.AUTH_MESSAGE ) return false;

        super.uuid = UUID.fromString(fields[0]);
        super.data = new User(fields[2], fields[3]);
        return true;
    }
}
