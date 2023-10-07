package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageHandler implements Runnable {
    private Socket clientSocket;
    private ChatServer server;
    private PrintWriter out;
    private BufferedReader in;

    private String receivedMessage;
    private String sentMessage;

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public String getSentMessage() {
        return sentMessage;
    }

    public MessageHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String userName = in.readLine();
            server.broadcastMessage(userName + " has joined the chat.");

            String message;
            while ((message = in.readLine()) != null) {
                server.broadcastMessage(userName + ": " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
