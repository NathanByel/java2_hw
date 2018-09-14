package com.java2.lesson_7_8.Messages;

import com.java2.lesson_7_8.CmdRsp;

public class ResponseMessage extends Message {
    public ResponseMessage(CmdRsp rsp) {
        super(MessageType.RESPONSE_MESSAGE, rsp);
    }
}
