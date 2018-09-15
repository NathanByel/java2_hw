package com.java2.lesson_7_8.Messages;

import com.java2.lesson_7_8.CmdRsp;

public class ResponseMessage extends Message {
    public ResponseMessage(CmdRsp rsp) {
        super(MessageType.RESPONSE_MESSAGE, rsp);
    }

    public CmdRsp getRsp() {
        return (CmdRsp) super.data;
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
