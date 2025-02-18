package com.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static String username;
    private static int id;
    private Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }

    public void start() throws IOException {
        try {
            socket = new Socket(InetAddress.getByName("localhost"), 9494);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            String idline = in.readLine();
            System.out.println(idline);
            if (idline != null) {
                id = Integer.parseInt(idline.substring(8));
                System.out.println("please input your username:");
                username = sysin.readLine();
                out.println(username);
            }
            new Thread(this::receive).start();
             String userinput;
             while ((userinput = sysin.readLine()) != null) {
                 if (userinput.equals("logout")) {
                     String outmsg="has been logged out";
                     out.println(outmsg);
                     break;
                 }
                 else {
                     out.println(userinput);
                 }
             }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
            out.close();
            in.close();
        }
    }

        public void receive ()  {
            String msg;
            try{
                while ((msg = in.readLine()) != null) {
                    System.out.println(msg);
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }



}
