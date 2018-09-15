package com.java2.lesson_7_8.Client;

import com.java2.lesson_7_8.CmdRsp;

public interface ClientUI {
    void setUsersList(String[] usersList);
    void addMessage(String msg);
    void statusCallback(CmdRsp s);
}
