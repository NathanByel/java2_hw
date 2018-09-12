package com.java2.lesson_7.Server;

import com.java2.lesson_7.Log;
import com.java2.lesson_7.User;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {
    private static final String TAG = "CLIENT HANDLER";

    private static final String CMD_ALIVE           = "/alive";
    private static final String CMD_TO_USER         = "/to_user";
    private static final String CMD_GET_USERS       = "/get_users";

    private static final String RSP_OK              = "/ok";
    private static final String RSP_ERR             = "/err";
    private static final String RSP_WRONG_CMD       = "/wrong_cmd";
    private static final String RSP_WRONG_PARAM     = "/wrong_param";
    private static final String RSP_USER_NOT_FOUND  = "/not_found";


    private User user;
    private Server server;
    private Socket socket;

    private Scanner netIn;
    private PrintWriter netOut;
    private long clientLastAliveTime = 0;

    private boolean run = true;

    // *****************************************************************************************************************
    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        user = new User();

        try {
            netIn = new Scanner(socket.getInputStream());
            netOut = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            Log.e(TAG, "stream error. " + e.toString());
            dropClient();
            return;
        }

        new Thread(() -> {
            while(run) {
                String netData = netReceive();
                if(netData != null) {
                    if(netData.startsWith("/")) {
                        parseCmd(netData);
                    } else {
                        if(server.sendBroadcastMessage(this, netData)) {
                            sendMessage(RSP_OK);
                        } else {
                            sendMessage(RSP_ERR);
                        }
                    }
                } else {
                    dropClient();
                }
            }
        }).start();
    }

    public void sendMessage(String msg) {
        if( !netSend(msg) ) {
            dropClient();
        }
    }

    private void parseCmd(String cmd) {
        if(cmd.equals(CMD_ALIVE)) {
            clientLastAliveTime = System.currentTimeMillis();
            sendMessage(RSP_OK);
            return;
        } else if(cmd.equals(CMD_GET_USERS)) {
            server.sendUserList(this);
            //sendMessage(RSP_OK);
            return;
        } else if(cmd.startsWith(CMD_TO_USER)) {
            String[] cmdParts = cmd.split(" ", 3);
            if(cmdParts.length == 3) {
                if( server.sendPrivateMessage(this, cmdParts[1], cmdParts[2]) ) {
                    sendMessage(RSP_OK);
                } else {
                    sendMessage(RSP_USER_NOT_FOUND);
                }
                return;
            }
            sendMessage(RSP_WRONG_PARAM);
            return;
        }
        sendMessage(RSP_WRONG_CMD);
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
        server.dropClient(this);
        closeAllResources();
        run = false;
    }

    public User getUser() {
        return user;
    }



}
