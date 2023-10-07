package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatClientTest {

    private ChatClient chatClient;
    private Socket socketMock;

    @BeforeEach
    public void setUp() {
        socketMock = mock(Socket.class);
        chatClient = new ChatClient("localhost", 12345, "User1");
        chatClient.setSocket(socketMock);
    }

    @Test
    public void testStart() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Server Message\n".getBytes());
        OutputStream outputStream = mock(OutputStream.class);
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        String consoleInput = "Client Message\n/exit\n";
        InputStream consoleInputStream = new ByteArrayInputStream(consoleInput.getBytes());

        InputStream originalSystemIn = System.in;
        try {
            System.setIn(consoleInputStream);
            chatClient.start();
            assertEquals("Server Message", chatClient.getReceivedMessage());
        } finally {
            System.setIn(originalSystemIn);
        }
    }

    @Test
    public void testMessageSender() throws Exception {
        OutputStream outputStream = mock(OutputStream.class);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        PrintWriter printWriter = new PrintWriter(outputStream, true);

        String message = "Client Message";
        chatClient.sendMessage(message);

        printWriter.flush();
        assertEquals(message, chatClient.getSentMessage());
    }
}