package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    private Socket socketMock;

    @BeforeEach
    public void setUp() {
        socketMock = mock(Socket.class);
        ChatServer server = mock(ChatServer.class);
        messageHandler = new MessageHandler(socketMock, server);
    }

    @Test
    public void testGetReceivedMessage() throws IOException {
        String message = "User1: Hello!";
        InputStream inputStream = new ByteArrayInputStream(message.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.isConnected()).thenReturn(true);

        messageHandler.run();

        assertEquals(message, messageHandler.getReceivedMessage());
    }

    @Test
    public void testGetSentMessage() {
        String message = "Hello, User2!";
        messageHandler.sendMessage(message);

        assertEquals(message, messageHandler.getSentMessage());
    }
}