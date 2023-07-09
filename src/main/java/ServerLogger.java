import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Formatter;

public class ServerLogger implements Logger{
    private static ServerLogger LOGGER = null;
    private OutputStreamWriter writer = null;

    private ServerLogger() {
        try {
            writer = new FileWriter("server.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Logger getInstance() {
        if (LOGGER == null) {
            synchronized (ServerLogger.class) {
                if (LOGGER == null) {
                    LOGGER = new ServerLogger();
                }
            }
        }
        return LOGGER;
    }

    @Override
    public void log(User user, String message, Date date) {
        try {
            writer.write(String.format("[%1$td.%1$tm.%1$tY %1$tT] %2$s: %3$s\n", date, user.getName(), message));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            LOGGER.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
