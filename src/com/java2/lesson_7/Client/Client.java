package com.java2.lesson_7.Client;

import com.java2.lesson_7.CmdRsp;
import com.java2.lesson_7.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client implements ClientController {
    private final int CONNECT_TRY = 3;
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private boolean connected = false;
    private boolean subscribed = false;
    private int connectTry = 0;

    private User user;
    private Socket socket;
    private Scanner netIn;
    private PrintWriter netOut;

    private ClientUI clientUI;
    private ClientUI logInUI;

    private Thread receiverThread = null;
    private List<String> usersList = new ArrayList<>();



    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        logInUI = new LoginWindow(this);
    }

    private void connectThreadStart() {
        new Thread(() -> {
            while(connectTry < CONNECT_TRY) {
                if (connected) {
                    connectTry = 0;
                    //netSend("<ping>");
                    if(!subscribed) {
                        netSend(CmdRsp.CMD_AUTH + " " + user.getNickName() + " " + user.getPass());
                    }
                } else {
                    connectToServer();
                    connectTry++;
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            logInUI.statusCallback(CmdRsp.RSP_ERR);
        }).start();
    }

    private void msgReceiverThreadStart() {
        if(receiverThread == null) {
            receiverThread = new Thread(() -> {
                while (true) {
                    if (connected) {
                        String netData = netReceive();
                        if (netData != null) {
                            if (!netData.startsWith("/") || !parseCmd(netData)) {
                                clientUI.addMessage(netData);
                            }
                        } else {
                            System.out.println("Ошибка сети!");
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            receiverThread.start();
        }
    }

    private boolean netSend(String str) {
        try {
            netOut.println(str);
            netOut.flush();
            return true;
        } catch (Exception e) {
            subscribed = false;
            connected = false;
            return false;
        }
    }

    private String netReceive() {
        try {
            return netIn.nextLine();
        } catch (Exception e) {
            subscribed = false;
            connected = false;
            return null;
        }
    }

    private void connectToServer() {
        closeAllResources();
        try {
            System.out.printf("Попытка подключения к серверу - %s:%d...", SERVER_ADDR, SERVER_PORT);
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            netIn = new Scanner(socket.getInputStream());
            netOut = new PrintWriter(socket.getOutputStream());

            System.out.println("Подключено!");
            netSend(CmdRsp.CMD_AUTH + " " + user.getNickName() + " " + user.getPass());
            connected = true;
        } catch (IOException e) {
            System.out.println("Ошибка сети...");
            subscribed = false;
            connected = false;
            closeAllResources();
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

    private boolean parseCmd(String cmd) {

        if(cmd.equals(CmdRsp.RSP_OK)) {
            return true;
        } else if (cmd.equals(CmdRsp.RSP_OK_AUTH)
                || cmd.equals(CmdRsp.RSP_WRONG_AUTH)
                || cmd.equals(CmdRsp.RSP_NICK_BUSY)) {

            if (cmd.equals(CmdRsp.RSP_OK_AUTH)) {
                subscribed = true;
                if (clientUI == null) {
                    clientUI = new MainWindow(this);
                }
            } else {
                subscribed = false;
                connectTry = CONNECT_TRY;
            }

            logInUI.statusCallback(cmd);
            return true;
        } else if(cmd.startsWith(CmdRsp.RSP_USERS_LIST)) {
            // ПЕРЕДЕЛАТЬ
            usersList.clear();
            usersList.addAll(Arrays.asList( cmd.split(" ") ));
            usersList.remove(0);// (CmdRsp.RSP_USERS_LIST);

            if(clientUI != null) {
                clientUI.setUsersList( usersList.toArray(new String[0]));
            }
            return true;
        }
        return false;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public List<String> getUsersList() {
        return usersList;
    }

    @Override
    public void logIn(String nickName, String pass) {
        connectTry = 0;
        user = new User(nickName, pass);
        connectThreadStart();
        msgReceiverThreadStart();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendMessage(String msg) {
        if (connected && subscribed) {
            if( !netSend(msg) ) {
                System.out.println("Ошибка сети!");
            }
        } else {
            clientUI.addMessage("Нет подключения к серверу!");
            System.out.println("Нет подключения к серверу!");
        }
    }

    @Override
    public void sendMessage(String toUser, String msg) {
        sendMessage(CmdRsp.CMD_TO_USER + " " + toUser + " " + msg);
    }
}
