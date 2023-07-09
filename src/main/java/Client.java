import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static final String EXITCHAT = "/exit";
    public static String filePath = "client_settings.txt";

    public static void main(String[] args) {
        ClientSettings clientSettings = new ClientSettings();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            clientSettings.loadSettingsFromFile(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Socket clientSocket = new Socket(clientSettings.getHost(), clientSettings.getPort());
            Thread threadWriter = createMessageWriter(clientSocket);
            threadWriter.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Thread createMessageWriter(Socket clientSocket) {
        return new Thread(() -> {
            try(PrintWriter messageWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                Scanner reader = new Scanner(System.in);) {
                System.out.println("Введите имя");
                String name = reader.nextLine();
                messageWriter.println(name);

                Thread threadReader = createMessageReader(clientSocket, name);
                threadReader.start();
                while (true) {
                    String message = reader.nextLine();
                    if (message.equalsIgnoreCase(EXITCHAT)) {
                        threadReader.interrupt();
                        clientSocket.close();
                        break;
                    } else {
                        writeMessage(messageWriter, message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void writeMessage(PrintWriter messageWriter, String message) throws IOException {
        messageWriter.println(message);
    }

    public static Thread createMessageReader(Socket clientSocket, String name) {
        return new Thread(() -> {
            try(BufferedReader messageReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Writer logger = new FileWriter(name + ".log", true)) {
                while (!Thread.interrupted()) {
                    if (messageReader.ready()) {
                        String message = messageReader.readLine();
                        readMessage(message, logger, new Date());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void readMessage(String message,  Writer logger, Date date) throws IOException {

            logger.write(String.format("[%1$td.%1$tm.%1$tY %1$tT] %2$s\n", date, message));
            logger.flush();
            System.out.println(message);
    }
}
