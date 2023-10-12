package client;

public class ClientMain {
    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 12345, "User1");
        client.start();
    }
}
