package com.java2.lesson_7_8.Channel;

import com.java2.lesson_7_8.Log;
import com.java2.lesson_7_8.Messages.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetDataChannel implements DataChannel {
    private static final String TAG = "NET CHANNEL";
    private Socket socket;
    private Scanner netIn;
    private PrintWriter netOut;

    private boolean connected = false;

    public NetDataChannel(Socket socket) {
        this.socket = socket;

        try {
            netIn = new Scanner(socket.getInputStream());
            netOut = new PrintWriter(socket.getOutputStream());
            connected = true;
        } catch (Exception e) {
            Log.e(TAG, "channel error. " + e.toString());
        }
    }

    @Override
    public void open() {

    }

    @Override
    public void close()/* throws IOException */{
        closeAll();
    }

    @Override
    public void sendMessage(Message msg) {

    }

    @Override
    public Message getMessage() {
        return null;
    }

    public boolean isConnected() {
        return connected;
    }


    private boolean netSend(String str) {
        try {
            netOut.println(str);
            netOut.flush();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "netSend error. " + e.toString());
            return false;
        }
    }

    private String netReceive() {
        try {
            return netIn.nextLine();
        } catch (Exception e) {
            Log.e(TAG, "netReceive error. " + e.toString());
            return null;
        }
    }

    private void closeAll() {
        netIn.close();
        netOut.close();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
