package com.chatroom;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/// basic function:implements multiple threads
public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

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

            String userlist=Server.getUserListExclude(this);
            out.println(userlist);

            /// basic function:remind other people of the new comer

            String welcome=String.format("[%s] %s(%d) have joined the chatroom",getCurrentTime(),username,id);
            Server.broadcast(welcome);

            String message;
            while((message=in.readLine())!=null){
                if(message.contains("logout")){
                    Server.broadcast("["+username+"] "+"("+id+")"+" have left the chatroom");
                    break;
                }else {
                    String send=String.format("[%s] %s (%d) %s",getCurrentTime(),username,id,message);
                    Server.broadcast(send);
                    try(FileWriter fw = new FileWriter("receivers.txt")) {
                        fw.write(Server.getUserListExclude(this));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            Server.removeclient(this);
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    /// basic function:show the time
    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void sendmsg (String msg) {
        out.println(msg);
    }

}
