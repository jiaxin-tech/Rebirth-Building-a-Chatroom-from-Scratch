package com.chatroom;

import java.io.*;
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
                String userlist = in.readLine();
                System.out.println("persons on line: "+userlist);
            }

            new Thread(this::receive).start();
            String userinput;
            while ((userinput = sysin.readLine()) != null) {
                if (userinput.equals("#chatlog")) {
                    String logreader;
                    try (BufferedReader reader = new BufferedReader(new FileReader("chatlog.txt"))) {
                        while ((logreader = reader.readLine()) != null) {
                            System.out.println(logreader);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (userinput.startsWith("#search")) {
                    String keymsg = userinput.substring(7);
                    chatsearch(keymsg);
                } else if (userinput.startsWith("[print-receivers]")) {
                   read();
                }
                else {
                    out.println(userinput);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            out.close();
            in.close();
        }
    }

    /// basic function:receive message
    public void receive() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /// advanced function:search for history chatlog
    private void chatsearch(String keymsg) {
        try (BufferedReader chatsearch = new BufferedReader(new FileReader("chatlog.txt"))) {
            String line;
            while ((line = chatsearch.readLine()) != null) {
                if (line.contains(keymsg)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /// advanced function:watch who had read last message,no matter if the use had left.
    public void read(){
        try (BufferedReader readreceivers = new BufferedReader(new FileReader("receivers.txt"))) {
            String line;
            while ((line = readreceivers.readLine()) != null) {
                System.out.println("[receivers]: " + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}