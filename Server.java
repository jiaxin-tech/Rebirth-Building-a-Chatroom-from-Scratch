package com.chatroom;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static AtomicInteger newID = new AtomicInteger(10000);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9494);
        System.out.println("waiting for client");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("client connected");

            /// basic function:assign new id to new user
            int id = newID.getAndIncrement();
            ClientHandler client = new ClientHandler(socket, id);
            clients.add(client);
            new Thread(client).start();
        }
    }

    public static void broadcast(String msg) {
        for (ClientHandler client : clients) {
            if (client.getUsername() != null) {
                client.sendmsg(msg);
            }
        }
        restore(msg);
    }

    public static void removeclient(ClientHandler client) {
        clients.remove(client);
    }

    public static String getUserListExclude(ClientHandler benren) {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler client : clients) {
            if (client != benren && client.getUsername() != null) {
                sb.append(client.getUsername()).append("(").append(client.getId())
                        .append(")").append(";");
            }
        }
        return sb.toString();
    }

    /// advanced function:restore the history chatlog in server side
    public static synchronized void restore(String msg) {
        try (PrintWriter restore = new PrintWriter(new FileWriter("chatlog.txt", true))) {
            restore.println(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String printRceivers() {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler client:clients){

        }
        return sb.toString();
    }
}

