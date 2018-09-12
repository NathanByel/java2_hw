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
    //private static final int clientTimeout = 10000;
    private List<ClientHandler> clients = new ArrayList<>();
    private AuthService authService;
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
                ClientHandler client = new ClientHandler(this, socket);
                Log.i(TAG, "Клиент " + client.hashCode() + " подключился. Ожидание регистрации.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка сервера! " + e.toString());
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        sendUserList(null);
        Log.i(TAG, "Клиент " + clientHandler.getUser().getNickName() + " залогинился. Всего - " + clients.size());
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        sendUserList(null);
        Log.i(TAG, "Клиент " + clientHandler.hashCode() + " отключен. Всего - " + clients.size());
    }

    public boolean isNickNameBusy(String nickName) {
        for (ClientHandler client : clients) {
            if (client.getUser().getNickName().equals(nickName)) {
                return true;
            }
        }
        return false;
    }

    public boolean sendBroadcastMessage(ClientHandler clientHandler, String msg) {
        Log.i(TAG, "Msg send " + ANSI_BLUE + clientHandler.getUser().getNickName() + ANSI_GREEN + " -> ALL");
        for(ClientHandler client: clients) {
            if(client != clientHandler) {
                client.sendMessage(msg);
            }
        }
        return true;
    }

    public boolean sendPrivateMessage(ClientHandler clientHandler, String nickName, String msg) {
        Log.i(TAG, "Msg send " + ANSI_BLUE + clientHandler.getUser().getNickName() + ANSI_GREEN + " -> "  + ANSI_BLUE + nickName);
        for(ClientHandler client: clients) {
            if( client.getUser().getNickName().equals(nickName) ) {
                client.sendMessage(msg);
                return true;
            }
        }
        Log.i(TAG, "User " + ANSI_BLUE + nickName + ANSI_GREEN + " not found");
        return false;
    }

    public void sendUserList(ClientHandler clientHandler) {
        StringBuilder users = new StringBuilder();
        users.append("/users_list ");
        for(ClientHandler client: clients) {
            users.append( client.getUser().getNickName());
            users.append(" ");
        }

        if(clientHandler != null) {
            clientHandler.sendMessage(users.toString());
        } else {
            for(ClientHandler client: clients) {
                client.sendMessage(users.toString());
            }
        }
    }

    public AuthService getAuthService() {
        return authService;
    }
}
