package com.java2.lesson_7.Server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {
    private Server server;
    private Socket socket;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        /*if(connected) {
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
        }*/
    }
}
