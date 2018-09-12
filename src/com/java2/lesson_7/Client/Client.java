package com.java2.lesson_7.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements ClientController {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private boolean connected = false;
    private Socket socket;
    private Scanner netIn;
    private PrintWriter netOut;

    private ClientUI clientUI;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        clientUI = new MainWindow(this);
        connectThreadStart();
        msgReceiverThreadStart();
    }

    private void connectThreadStart() {
        new Thread(() -> {
            while(true) {
                if (connected) {
                    //netSend("<ping>");
                } else {
                    connectToServer();
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void msgReceiverThreadStart() {
        new Thread(() -> {
            while(true) {
                if (connected) {
                    String netData = netReceive();
                    if(netData != null) {
                        if(netData.startsWith("/")) {

                        } else {
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
        }).start();
    }

    private boolean netSend(String str) {
        try {
            netOut.println(str);
            netOut.flush();
            return true;
        } catch (Exception e) {
            connected = false;
            return false;
        }
    }

    private String netReceive() {
        try {
            return netIn.nextLine();
        } catch (Exception e) {
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
            clientUI.addMessage("Подключено!");
            netSend("/auth aaa sss");
            connected = true;
        } catch (IOException e) {
            System.out.println("Ошибка сети...");
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

    @Override
    public void disconnect() {

    }

    @Override
    public void sendMessage(String msg) {
        if (connected) {
            if( !netSend(msg) ) {
                System.out.println("Ошибка сети!");
            }
        } else {
            clientUI.addMessage("Нет подключения к серверу!");
            System.out.println("Нет подключения к серверу!");
        }
    }
}
