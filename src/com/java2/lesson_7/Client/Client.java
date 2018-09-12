package com.java2.lesson_7.Client;
import com.java2.lesson_7.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private boolean connected = false;
    private Socket socket;
    private Scanner netIn;
    private PrintWriter netOut;


    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        connectThreadStart();
        msgSenderThreadStart();
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

    private void msgSenderThreadStart() {
        new Thread(() -> {
            Scanner consoleIn = new Scanner(System.in);
            while(true) {
                String sendText = consoleIn.nextLine()
                        .replace("<", "")
                        .replace(">", "")
                        .trim();

                if (sendText.length() > 0) {
                    if (connected) {
                        if( !netSend(sendText) ) {
                            System.out.println("Ошибка сети!");
                        }
                    } else {
                        System.out.println("Нет подключения к серверу!");
                    }
                }
            }
        }).start();
    }

    private void msgReceiverThreadStart() {
        new Thread(() -> {
            while(true) {
                if (connected) {
                    String inText = netReceive();
                    if(inText != null) {
                        System.out.println("Сервер: " + inText);
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
            System.out.println("Можно отправлять текст.");
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
}
