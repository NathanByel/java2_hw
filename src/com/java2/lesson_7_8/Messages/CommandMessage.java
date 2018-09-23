package com.java2.lesson_7_8.Messages;

public class CommandMessage extends Message {
    public CommandMessage() {
        super(MessageType.COMMAND_MESSAGE, null);
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public boolean deserialize(String data) {
        return false;
    }
}
