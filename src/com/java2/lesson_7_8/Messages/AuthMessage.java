package com.java2.lesson_7_8.Messages;

import com.java2.lesson_7_8.User;

public class AuthMessage extends Message {
    public AuthMessage(User user) {
        super(MessageType.AUTH_MESSAGE, user);
    }

    public User getUser() {
        if(super.data instanceof User) {
            return (User) super.data;
        }
        return null;
    }

    /*
    if( (user == null) || !netData.startsWith(CmdRsp.CMD_AUTH) ) return CmdRsp.RSP_WRONG_CMD;



    String[] cmdParts = netData.split(" ", 3);
        if( cmdParts.length != 3 ) return CmdRsp.RSP_WRONG_PARAM;
        if( server.isNickNameBusy(cmdParts[1]) ) return CmdRsp.RSP_NICK_BUSY;
        if ( !server.getAuthService().checkUser(cmdParts[1], cmdParts[2]) ) return CmdRsp.RSP_WRONG_AUTH;

    user = new User(cmdParts[1], cmdParts[2]);
        return CmdRsp.RSP_OK;*/
}
