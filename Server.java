package com.chatroom;

import java.io.IOException;
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
            int id = newID.getAndIncrement();
            ClientHandler client = new ClientHandler(socket, id);
            clients.add(client);
            new Thread(client).start();
            System.out.println("welcome " + id);
        }
    }

}

