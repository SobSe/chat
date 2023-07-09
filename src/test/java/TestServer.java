import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.*;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class TestServer {
    public Socket clientSocket;
    public ServerSocket serverSocket;
    public Server server;
    public PrintWriter messageWriter;

    @BeforeEach
    public void beforeEach() {
        clientSocket = Mockito.mock(Socket.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        messageWriter = new PrintWriter(byteArrayOutputStream);

        ServerSocket serverSocket = Mockito.mock(ServerSocket.class);

        Logger serverLogger = Mockito.mock(Logger.class);
        try (MockedStatic<ServerLogger> serverLoggerMock = mockStatic(ServerLogger.class)) {
            serverLoggerMock.when(ServerLogger::getInstance).thenReturn(serverLogger);
            server = new Server(serverSocket);

            try {
                Mockito.when(clientSocket.getOutputStream()).thenReturn(byteArrayOutputStream);
                Mockito.when(clientSocket.getPort()).thenReturn(8181);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testAddUser() {
        //arrange
        Map<Integer, User> expected = new HashMap<>();
        expected.put(8181, new User("Sergey", clientSocket, messageWriter));

        StringBuilder messages = new StringBuilder();
        messages.append("Sergey\n");
        messages.append("/exit\n");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(messages.toString().getBytes());
        try {
            Mockito.when(clientSocket.getInputStream()).thenReturn(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //act
        server.addNewUser(clientSocket, messageWriter);
        //assert
        Assertions.assertEquals(expected, server.getClients());
    }

    @Test
    public void testWaitMessageAndSend() {
        //arrange
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter messageWriter = new PrintWriter(byteArrayOutputStream, true);

        Map<Integer, User> clients = new HashMap<>();
        clients.put(8181, new User("Sergey", clientSocket, messageWriter));
        try {
            Field clientsField = server.getClass().getDeclaredField("clients");
            clientsField.setAccessible(true);
            clientsField.set(server, clients);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String expected = "Hi all\n";
        //act
        server.sendMessageAll("Hi all");
        //assert
        Assertions.assertEquals(expected, byteArrayOutputStream.toString());
    }
}