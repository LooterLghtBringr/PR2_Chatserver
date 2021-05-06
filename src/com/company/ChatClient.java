package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatClient implements Runnable{

    private BufferedReader reader;
    private PrintWriter printwriter;
    private ArrayList<ChatClient> clients;
    private Socket client;
    private String name;
    private Logger logger;
    HashMap<String,ChatClient> connectedChatClients;

    public ChatClient(ArrayList<ChatClient> clients, Socket client, Logger logger) {
        try
        {
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            printwriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            this.clients = clients;
            this.client = client;
            this.logger = logger;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message)
    {
        printwriter.println(message);
        printwriter.flush();
        logger.writeLogEntry(message);
    }

    public void close()
    {
        try {
            reader.close();
            printwriter.close();
            clients.remove(this);
            logger.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            String line = "";
            while((line = reader.readLine()) != null){
                try {
                    String[] lineSplit = line.split(":");
                    String command = lineSplit[0];

                    if (command.equals("<bye>")) {
                        ChatClient toRemove = this;
                        this.close();
                        //clients.remove(this);
                        for (ChatClient c : clients) {
                            c.sendMessage(toRemove.name + " logged out");
                        }
                        return;
                    }
                    else if(command.equals("<list>"))
                    {
                        for (ChatClient c: clients) {
                            c.sendMessage("Connected Client: " + c.name);
                        }
                    }

                    if (lineSplit.length == 1) {
                        this.sendMessage("Wrong Command");
                        continue;
                    }

                    String action = lineSplit[1];

                    if (command.equals("<name>")) {
                        this.name = action;
                        this.sendMessage("Name: " + action + " set");
                    } else if (command.equals("<msg>")) {
                        for (ChatClient c : clients) {
                            c.sendMessage(action);
                        }
                    } else if (command.equals("<msgto>")) {
                        String message = line.split(":")[2];
                        for (ChatClient c : clients) {
                            if (c.name.equals(action)) {
                                c.sendMessage(message);
                            }
                        }
                    } else {
                        this.sendMessage("Wrong Command");
                    }
                }
                catch(ArrayIndexOutOfBoundsException e){
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
