package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) {
        ArrayList<ChatClient> chatClients = new ArrayList<ChatClient>();
        Logger logger = new Logger("D:\\Temp\\Logging.txt");
        try(ServerSocket serverSocket = new ServerSocket(1234))
        {
            while(true)
            {
                System.out.println("Waiting for client");
                Socket client = serverSocket.accept();
                System.out.println("Create new ChatClient");
                ChatClient chatClient = new ChatClient(chatClients, client, logger);
                System.out.println("Add ChatClient to List");
                chatClients.add(chatClient);
                System.out.println("Create Thread");
                Thread clientThread = new Thread(chatClient);
                System.out.println("Run Thread");
                clientThread.start();
                //clientThread.join();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
