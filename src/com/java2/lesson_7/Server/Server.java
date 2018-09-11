package com.java2.lesson_7.Server;

import com.java2.lesson_7.Log;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final String SERVER_TAG = "SERVER";

    private List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        new Server();
    }


    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            Log.i(SERVER_TAG, "Сервер запущен.");
            //msgSenderThreadStart();
            //msgReceiverThreadStart();

            while(true) {
                Socket socket = serverSocket.accept();
                clients.add( new ClientHandler(this, socket) );
                Log.i(SERVER_TAG, "Клиент подключился. Всего - " + clients.size());
            }
        } catch (Exception e) {
            Log.e(SERVER_TAG, "Ошибка сервера!");
        }
    }


}
