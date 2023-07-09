import java.io.PrintWriter;
import java.net.Socket;

public class User {
    private String name;
    private Socket clientSocket;
    private PrintWriter messageWriter;

    public User(String name, Socket clientSocket, PrintWriter messageWriter) {
        this.clientSocket = clientSocket;
        this.messageWriter = messageWriter;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sendMessage(String message) {
        messageWriter.println(message);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || this.getClass() != obj.getClass()) return false;

        User user = (User) obj;
        return user.name.equals(name);
    }
}
