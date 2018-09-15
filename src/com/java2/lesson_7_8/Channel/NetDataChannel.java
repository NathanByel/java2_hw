package com.java2.lesson_7_8.Channel;

import com.java2.lesson_7_8.Log;
import com.java2.lesson_7_8.Messages.AuthMessage;
import com.java2.lesson_7_8.Messages.Message;
import com.java2.lesson_7_8.Messages.MessageType;
import com.java2.lesson_7_8.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NetDataChannel implements DataChannel {
    private static final String TAG = "NET CHANNEL";
    private Socket socket;
    private Scanner netIn;
    private PrintWriter netOut;
//    JSONObject obj = new JSONObject();
    private boolean connected = false;

    public NetDataChannel(Socket socket) throws IOException {
        this.socket = socket;

        try {
            netIn = new Scanner(socket.getInputStream());
            netOut = new PrintWriter(socket.getOutputStream());
            connected = true;
        } catch (Exception e) {
            Log.e(TAG, "channel error. " + e.toString());
            throw new IOException("Socket error");
        }
    }

    public NetDataChannel(String host, int port) throws IOException {
        this(new Socket(host, port));
        /*try {
            this( new Socket );
        } catch (IOException e) {
            Log.e(TAG, "Connect to " + host + ":" + port + " error. " + e.toString());
            throw new IOException("Connect to " + host + ":" + port + " error.");
        }*/
    }

    @Override
    public void open() {

    }

    @Override
    public void close()/* throws IOException */{
        connected = false;
        closeAll();
    }

    @Override
    public boolean sendMessage(Message msg) {
        String data = msg.serialize();
        if(data == null) {
            return false;
        }

        return netSend(data);
    }

    @Override
    public Message getMessage() {
        String data = netReceive();
        if(data == null) {
            Log.e(TAG, "getMessage error. netReceive = null");
            return null;
        }

        String[] fields = data.split(":");
        if(fields.length < 2) {
            Log.e(TAG, "Parse error. MSG " + data);
            return null;
        }

        switch( MessageType.valueOf(fields[1]) ) {
            case BROADCAST_MESSAGE:
                break;
            case PRIVATE_MESSAGE:
                break;
            case INFO_MESSAGE:
                break;
            case ALIVE_MESSAGE:
                break;
            case COMMAND_MESSAGE:
                break;
            case RESPONSE_MESSAGE:
                break;

            case AUTH_MESSAGE:
                AuthMessage msg = new AuthMessage();
                if( msg.deserialize(data) ) {
                    return msg;
                }
                break;

            default:
                Log.e(TAG, "Wrong type. MSG " + data);
                return null;
        }

        Log.e(TAG, "Failed to deserialize. Wrong type. MSG " + data);
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
        if(netIn != null) netIn.close();
        if(netOut != null) netOut.close();

        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
