package com.java2.lesson_7_8.Messages;

public abstract class Message {
    protected MessageType type;
    protected Object data;

    public Message(MessageType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    //public Object getData() {
    //    return data;
    //}
}
