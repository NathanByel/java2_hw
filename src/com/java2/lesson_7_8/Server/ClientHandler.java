package com.java2.lesson_7_8.Server;

import com.java2.lesson_7_8.Channel.NetDataChannel;
import com.java2.lesson_7_8.CmdRsp;
import com.java2.lesson_7_8.Log;
import com.java2.lesson_7_8.Messages.*;
import com.java2.lesson_7_8.User;
import com.java2.lesson_7_8.UserInfo;

import java.net.Socket;

public class ClientHandler {
    private static final String TAG = "CLIENT HANDLER";

    private UserInfo userInfo;
    private Server server;

    private long clientLastAliveTime = 0; // Не используется пока
    private long authStartTime = 0;

    private boolean run = true;
    private boolean subscribed = false;
    private boolean authorised = false;

    private NetDataChannel net;

    // *****************************************************************************************************************
    public ClientHandler(Server server, Socket socket) {
        this.server = server;

        net = new NetDataChannel(socket);

        authStartTime = System.currentTimeMillis();
        new Thread(() -> {
            while(true) {
                Message msg = net.getMessage();
                if(msg == null) {
                    dropClient();
                    return;
                }

                if(msg.getType() == MessageType.AUTH_MESSAGE) {

                    CmdRsp cmdRsp = checkAuth( ((AuthMessage)msg).getUser() );
                    if (cmdRsp == CmdRsp.RSP_OK) {
                        authorised = true;
                        break;
                    } else {
                        Log.e(TAG, "Клиент " + this.hashCode() + " " + cmdRsp + " Необходима регистрация.");
                        net.sendMessage( new ResponseMessage(cmdRsp) );
                        net.sendMessage( new ResponseMessage(CmdRsp.RSP_NEED_AUTH) );
                        //dropClient();
                        //return;
                    }
                }
            }

            net.sendMessage( new ResponseMessage(CmdRsp.RSP_OK_AUTH) );
            server.subscribe(this);
            subscribed = true;

            net.sendMessage( new InfoMessage("Добро пожаловать!\r\n" +
                    "Для отправки личных сообщений используйте формат:\r\n" +
                    "/имя сообщение\r\n") );

            while(run) {
                Message msg = net.getMessage();
                if(msg != null) {
                    parseMessage(msg);
                } else {
                    dropClient();
                }
            }
        }).start();

        new Thread(() -> {
            while(run) {
                long time = System.currentTimeMillis();

                if(!authorised) {
                    if( (time - authStartTime) > 12000) {
                        Log.e(TAG, "Клиент " + this.hashCode() + " отключен. Таймаут авторизации.");
                        dropClient();
                        break;
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private CmdRsp checkAuth(User user) {
        if(     (user == null)
                || user.getNickName().isEmpty()
                || user.getPass().isEmpty() ) return CmdRsp.RSP_WRONG_PARAM;

        if( server.isNickNameBusy(user) ) return CmdRsp.RSP_NICK_BUSY;
        if ( !server.getAuthService().checkUser(user) ) return CmdRsp.RSP_WRONG_AUTH;
        userInfo = server.getAuthService().getUserInfo(user);
        return CmdRsp.RSP_OK;
    }

    private void parseMessage(Message msg) {
        switch (msg.getType()) {
            case ALIVE_MESSAGE:
                clientLastAliveTime = System.currentTimeMillis();
                net.sendMessage(new ResponseMessage(CmdRsp.RSP_OK));
                break;

            case BROADCAST_MESSAGE:
                if(server.sendBroadcastMessage(this, msg)) {
                    net.sendMessage(new ResponseMessage(CmdRsp.RSP_OK));
                } else {
                    net.sendMessage(new ResponseMessage(CmdRsp.RSP_ERR));
                }
                break;

            case PRIVATE_MESSAGE:
                String[] cmdParts = cmd.split(" ", 3);
                if(cmdParts.length == 3) {
                    if( server.sendPrivateMessage(this, cmdParts[1], cmdParts[2]) ) {
                        net.sendMessage(new ResponseMessage(CmdRsp.RSP_OK));
                    } else {
                        net.sendMessage(new ResponseMessage(CmdRsp.RSP_USER_NOT_FOUND));
                    }
                    return;
                }
                net.sendMessage(new ResponseMessage(CmdRsp.RSP_WRONG_PARAM));
                break;

            case INFO_MESSAGE:
                break;

            case END_CMD:
                //Log.i(TAG, "Клиент " + user.getNickName() + " конец сессии.");
                //dropClient();
                break;
            case AUTH_MESSAGE:
                break;
            case GET_USERS_LIST_CMD:
                //else if(cmd.equals(CmdRsp.CMD_GET_USERS)) {
                //server.sendUserList(this);
                break;
            case RESPONSE_MESSAGE:
                break;
            case RSP_OK:
                break;
            case RSP_ERR:
                break;
            case RSP_WRONG_CMD:
                break;
            case RSP_WRONG_PARAM:
                break;
            case RSP_NEED_AUTH:
                break;
            case RSP_OK_AUTH:
                break;
            case RSP_WRONG_AUTH:
                break;
            case RSP_NICK_BUSY:
                break;
            case RSP_USER_NOT_FOUND:
                break;
            case RSP_USERS_LIST:
                break;

            default:
                net.sendMessage(new ResponseMessage(CmdRsp.RSP_WRONG_CMD));
        }
    }

    private void dropClient() {
        Log.i(TAG, "drop client");
        server.unsubscribe(this);
        net.close();
        run = false;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public User getUser() {
        return userInfo;
    }
}
