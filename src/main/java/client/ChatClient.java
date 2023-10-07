package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {
    private String serverAddress;
    private int serverPort;
    private String userName;
    private Socket socket;
    private List<String> sentMessages;
    private List<String> receivedMessages; // Добавлен список для полученных сообщений

    public ChatClient(String serverAddress, int serverPort, String userName) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.userName = userName;
        this.sentMessages = new ArrayList<>();
        this.receivedMessages = new ArrayList<>(); // Инициализация списка для полученных сообщений
    }

    public void start() {
        try {
            socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            out.println(userName);

            Thread inputThread = new Thread(() -> {
                String message;
                try {
                    while ((message = consoleIn.readLine()) != null) {
                        out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            inputThread.start();

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                receivedMessages.add(serverMessage); // Добавляем полученное сообщение в список
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSentMessages() {
        return sentMessages;
    }

    public String getSentMessage() {
        // Вернуть последнее отправленное сообщение из коллекции sentMessages
        if (sentMessages.isEmpty()) {
            return null;
        }
        return sentMessages.get(sentMessages.size() - 1);
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }

    public String getReceivedMessage() {
        // Вернуть последнее полученное сообщение из коллекции receivedMessages
        if (receivedMessages.isEmpty()) {
            return null;
        }
        return receivedMessages.get(receivedMessages.size() - 1);
    }

    public void sendMessage(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            sentMessages.add(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}