package com.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private int id;

    public ClientHandler(Socket socket,int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            out=new PrintWriter(socket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("welcome,"+id);
            username = in.readLine();

            String welcome=String.format("[%s] %s(%d) have joined the chatroom",getCurrentTime(),username,id);
            Server.broadcast(welcome);

            String message;
            while((message=in.readLine())!=null){
                String send=String.format("[%s] %s (%d) %s",getCurrentTime(),username,id,message);
                Server.broadcast(send);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void sendmsg (String msg) {
        out.println(msg);
    }

}
