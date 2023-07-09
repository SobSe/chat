import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private Map<Integer, User> clients = new HashMap<>();
    private ServerSocket serverSocket;
    private Logger serverLogger;
    public static final String EXITCHAT = "/exit";
    private static String settingsFilePath = "server_settings.txt";

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.serverLogger = ServerLogger.getInstance();
    }

    public static void main(String[] args) {

        ServerSettings serverSettings = new ServerSettings();
        try (BufferedReader reader = new BufferedReader(new FileReader(settingsFilePath))) {
            serverSettings.loadSettingsFromFile(reader);
        }catch (IOException e) {
            e.printStackTrace();
        }
        try(ServerSocket serverSocket = new ServerSocket(serverSettings.getPort())) {
            Server server = new Server(serverSocket);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                createClientThread(clientSocket);
            }
        } catch (IOException e) {

        } finally {
            ServerLogger.close();
        }
    }

    public void createClientThread(Socket clientSocket) {
        new Thread(() -> {
            try (PrintWriter messageWriter = new PrintWriter(clientSocket.getOutputStream(), true)){
                addNewUser(clientSocket, messageWriter);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addNewUser(Socket clientSocket, PrintWriter messageWriter) {
        try (BufferedReader messageReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String name = messageReader.readLine();
            User user = new User(name, clientSocket, messageWriter);
            clients.put(clientSocket.getPort(), user);
            waitMessageAndSend(user, messageReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitMessageAndSend(User user, BufferedReader messageReader) {
        try {
            while (true) {
                if (messageReader.ready()) {
                    String message = messageReader.readLine();
                    if (message.equalsIgnoreCase(EXITCHAT)) {
                        break;
                    }
                    sendMessageAll(user.getName() + ": " + message);
                    serverLogger.log(user, message, new Date());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessageAll(String message) {
        for (Map.Entry<Integer, User> userEntry : clients.entrySet()) {
            userEntry.getValue().sendMessage(message);
        }
    }

    public Map<Integer, User> getClients() {
        return new HashMap<>(clients);
    }
}
