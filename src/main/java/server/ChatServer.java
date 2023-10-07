package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatServer {
    private int port;
    private List<MessageHandler> clients = new ArrayList<>();
    private PrintWriter fileLogger;

    public ChatServer(int port) {
        this.port = port;
        try {
            fileLogger = new PrintWriter(new FileWriter("file.log", true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                MessageHandler handler = new MessageHandler(clientSocket, this);
                clients.add(handler);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message) {
        for (MessageHandler client : clients) {
            client.sendMessage(message);
        }
        logMessage(message);
    }

    private void logMessage(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        fileLogger.println(timestamp + " - " + message);
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(12345);
        server.start();
    }
}