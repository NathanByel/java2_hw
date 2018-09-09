package com.java2.lesson_6;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*
1. Написать консольный вариант клиент\серверного приложения, в котором пользователь может писать сообщения,
   как на клиентской стороне, так и на серверной. Т.е. если на клиентской стороне написать «Привет», нажать Enter,
   то сообщение должно передаться на сервер и там отпечататься в консоли. Если сделать то же самое на серверной стороне,
   то сообщение передается клиенту и печатается у него в консоли. Есть одна особенность, которую нужно учитывать:
   клиент или сервер может написать несколько сообщений подряд. Такую ситуацию необходимо корректно обработать.
   Разобраться с кодом с занятия: он является фундаментом проекта-чата

   *ВАЖНО! * Сервер общается только с одним клиентом, т.е. не нужно запускать цикл,
   который будет ожидать второго/третьего/n-го клиентов.
*/

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
                    netSend("<ping>");
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
