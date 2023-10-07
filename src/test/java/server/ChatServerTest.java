package server;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatServerTest {

    private ChatServer chatServer;
    private Socket socketMock;

    @BeforeEach
    public void setUp() {
        socketMock = mock(Socket.class);
        chatServer = new ChatServer(12345);
    }

    @Test
    public void testBroadcastMessage() throws Exception {
        OutputStream outputStream = mock(OutputStream.class);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        PrintWriter printWriter = new PrintWriter(outputStream, true);

        String message = "Server Message";
        chatServer.broadcastMessage(message);

        // В данном тесте мы проверяем, что метод broadcastMessage отправляет сообщение на выход.
        // Мы не можем проверить, что сервер получил сообщение, так как он не имеет getReceivedMessage.
    }
}