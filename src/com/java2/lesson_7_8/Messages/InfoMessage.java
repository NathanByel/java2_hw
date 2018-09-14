package com.java2.lesson_7_8.Messages;

public class InfoMessage extends Message {
    public InfoMessage(String text) {
        super(MessageType.INFO_MESSAGE, text);
    }
}
