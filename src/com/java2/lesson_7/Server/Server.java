package com.java2.lesson_7.Server;

import com.java2.lesson_7.Log;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.java2.lesson_7.Log.ANSI_BLUE;
import static com.java2.lesson_7.Log.ANSI_GREEN;
import static com.java2.lesson_7.Log.ANSI_RED;

public class Server {
    private static final String TAG = "SERVER";
    private static final int PORT = 8189;
    private static final int clientTimeout = 10000;
    private AuthService authService;
    private List<ClientHandler> clients = new ArrayList<>();

    // *****************************************************************************************************************
    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try ( ServerSocket serverSocket = new ServerSocket(PORT);
              AuthService authService = new BaseAuthService() ) {

            this.authService = authService;
            authService.start();
            Log.i(TAG, "Сервер запущен.");

            while(true) {
                Socket socket = serverSocket.accept();
                clients.add( new ClientHandler(this, socket) );
                Log.i(TAG, "Клиент подключился. Всего - " + clients.size());
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка сервера! " + e.toString());
        }
    }

    public void dropClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        Log.i(TAG, "Клиент отключен. Всего - " + clients.size());
    }

    public boolean sendBroadcastMessage(ClientHandler clientHandler, String msg) {
        Log.i(TAG, "User " + ANSI_BLUE + clientHandler.getUser().getNickName() + ANSI_GREEN + " send msg -> ALL");
        for(ClientHandler client: clients) {
            if(client != clientHandler) {
                client.sendMessage(msg);
            }
        }
        return true;
    }

    public boolean sendPrivateMessage(ClientHandler clientHandler, String nickName, String msg) {
        Log.i(TAG, "User " + ANSI_BLUE + clientHandler.getUser().getNickName() + ANSI_GREEN + " send msg -> " + nickName);
        for(ClientHandler client: clients) {
            if( clientHandler.getUser().getNickName().equals(nickName) ) {
                client.sendMessage(msg);
                return true;
            }
        }
        Log.i(TAG, "User " + ANSI_BLUE + nickName + ANSI_GREEN + " not found");
        return false;
    }

    public void sendUserList(ClientHandler clientHandler) {
        StringBuilder users = new StringBuilder();
        users.append("/users_list");
        for(ClientHandler client: clients) {
            users.append( client.getUser().getNickName());
            users.append(" ");
        }

        clientHandler.sendMessage(users.toString());
    }
}
