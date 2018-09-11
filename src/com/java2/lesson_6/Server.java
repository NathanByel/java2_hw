package com.java2.lesson_6;

import java.io.PrintWriter;
import java.net.ServerSocket;
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

public class Server {
    private boolean connected = false;
    private Socket socket;
    private Scanner netIn;
    private PrintWriter netOut;
    private long clientLastPingTime = 0;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен.");
            msgSenderThreadStart();
            msgReceiverThreadStart();

            while(true) {
                if(connected) {
                    if( (System.currentTimeMillis() - clientLastPingTime) > 10000 ) {
                        connected = false;
                        System.out.println("Таймаут. Соединение потеряно.");
                    }
                    Thread.sleep(1000);
                } else {
                    closeAllResources();
                    System.out.println("Ожидаем клиента...");
                    socket = serverSocket.accept();
                    clientLastPingTime = System.currentTimeMillis();

                    netIn = new Scanner(socket.getInputStream());
                    netOut = new PrintWriter(socket.getOutputStream());

                    connected = true;
                    System.out.println("Клиент подключился");
                    System.out.println("Можно отправлять текст.");
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка сервера");
        }
    }

    private void msgSenderThreadStart() {
        new Thread(() -> {
            Scanner consoleIn = new Scanner(System.in);
            while(true) {
                String sendText = consoleIn.nextLine().trim();
                if (sendText.length() > 0) {
                    if (connected) {
                        if( !netSend(sendText) ) {
                            System.out.println("Ошибка сети!");
                        }
                    } else {
                        System.out.println("Нет подключенного клиента!");
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
                        if( inText.startsWith("<") && inText.endsWith(">") ) {
                            parseCmd(inText);
                        } else {
                            System.out.println("Клиент: " + inText.trim());
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

    private void closeAllResources() {
        try {
            socket.close();
        } catch (Exception e) {
            socket = null;
        }
        if(netIn != null) netIn.close();
        if(netOut != null) netOut.close();
    }

    private void parseCmd(String cmd) {
        if(cmd.equals("<ping>")) {
            clientLastPingTime = System.currentTimeMillis();
        }
    }
}
