package com.java2.lesson_7.Server;

import com.java2.lesson_7.CmdRsp;
import com.java2.lesson_7.Log;
import com.java2.lesson_7.User;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {
    private static final String TAG = "CLIENT HANDLER";

    private User user;
    private Server server;
    private Socket socket;

    private Scanner netIn;
    private PrintWriter netOut;
    private long clientLastAliveTime = 0; // Не используется пока

    private boolean run = true;
    private boolean subscribed = false;

    // *****************************************************************************************************************
    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            netIn = new Scanner(socket.getInputStream());
            netOut = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            Log.e(TAG, "stream error. " + e.toString());
            dropClient();
            return;
        }

        new Thread(() -> {
            String netData = netReceive();
            String authRes = checkAuth(netData);
            if( !authRes.equals(CmdRsp.RSP_OK) ) {
                Log.e(TAG, "Клиент " + this.hashCode() + " " + authRes + " Необходима регистрация.");
                sendMessage(authRes);
                sendMessage(CmdRsp.RSP_NEED_AUTH);
                dropClient();
                return;
            }

            sendMessage(CmdRsp.RSP_OK_AUTH);
            server.subscribe(this);
            subscribed = true;

            sendMessage("Добро пожаловать!");
            sendMessage("Для отправки личных сообщений используйте формат:");
            sendMessage("/имя сообщение");
            while(run) {
                netData = netReceive();
                if(netData != null) {
                    if(netData.startsWith("/")) {
                        parseCmd(netData);
                    } else {
                        if(server.sendBroadcastMessage(this, netData)) {
                            sendMessage(CmdRsp.RSP_OK);
                        } else {
                            sendMessage(CmdRsp.RSP_ERR);
                        }
                    }
                } else {
                    dropClient();
                }
            }
        }).start();
    }

    private String checkAuth(String netData) {
        if( (netData == null) || !netData.startsWith(CmdRsp.CMD_AUTH) ) return CmdRsp.RSP_WRONG_CMD;

        String[] cmdParts = netData.split(" ", 3);
        if( cmdParts.length != 3 ) return CmdRsp.RSP_WRONG_PARAM;
        if( server.isNickNameBusy(cmdParts[1]) ) return CmdRsp.RSP_NICK_BUSY;
        if ( !server.getAuthService().checkUser(cmdParts[1], cmdParts[2]) ) return CmdRsp.RSP_WRONG_AUTH;

        user = new User(cmdParts[1], cmdParts[2]);
        return CmdRsp.RSP_OK;
    }

    private void parseCmd(String cmd) {
        if(cmd.equals(CmdRsp.CMD_ALIVE)) {
            clientLastAliveTime = System.currentTimeMillis();
            sendMessage(CmdRsp.RSP_OK);
            return;
        } else if(cmd.equals(CmdRsp.CMD_END)) {
            Log.i(TAG, "Клиент " + user.getNickName() + " конец сессии.");
            dropClient();
            return;
        } else if(cmd.equals(CmdRsp.CMD_GET_USERS)) {
            server.sendUserList(this);
            //sendMessage(RSP_OK);
            return;
        } else if(cmd.startsWith(CmdRsp.CMD_TO_USER)) {
            String[] cmdParts = cmd.split(" ", 3);
            if(cmdParts.length == 3) {
                if( server.sendPrivateMessage(this, cmdParts[1], cmdParts[2]) ) {
                    sendMessage(CmdRsp.RSP_OK);
                } else {
                    sendMessage(CmdRsp.RSP_USER_NOT_FOUND);
                }
                return;
            }
            sendMessage(CmdRsp.RSP_WRONG_PARAM);
            return;
        }
        sendMessage(CmdRsp.RSP_WRONG_CMD);
    }

    private boolean netSend(String str) {
        try {
            netOut.println(str);
            netOut.flush();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "netSend error. " + e.toString());
            return false;
        }
    }

    private String netReceive() {
        try {
            return netIn.nextLine();
        } catch (Exception e) {
            Log.e(TAG, "netReceive error. " + e.toString());
            return null;
        }
    }

    private void closeAllResources() {
        try {
            socket.close();
        } catch (Exception e) {
            socket = null;
        }
        if(netIn != null) netIn.close();
        if(netOut != null) netOut.close();
    }

    private void dropClient() {
        Log.i(TAG, "drop client");
        server.unsubscribe(this);
        closeAllResources();
        run = false;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public User getUser() {
        return user;
    }

    public void sendMessage(String msg) {
        if( !netSend(msg) ) dropClient();
    }

}
