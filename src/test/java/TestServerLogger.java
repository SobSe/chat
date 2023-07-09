import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Date;

public class TestServerLogger {
    @Test
    public void testLog() {
        //arrange
        Logger logger = ServerLogger.getInstance();

        ByteArrayOutputStream bausMessageWriter = new ByteArrayOutputStream();
        PrintWriter messageWriter = new PrintWriter(bausMessageWriter, true);

        ByteArrayOutputStream bausWriter = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bausWriter);

        Socket cientSocket = Mockito.mock(Socket.class);
        try {
            Field writerField = logger.getClass().getDeclaredField("writer");
            writerField.setAccessible(true);
            writerField.set(logger, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User user = new User("Sergey", cientSocket, messageWriter);

        String message = "Hi all";

        Date date = new Date();

        String expect = String.format("[%1$td.%1$tm.%1$tY %1$tT] %2$s: %3$s\n", date, user.getName(), message);
        //act
        logger.log(user, message, date);
        //assert
        Assertions.assertEquals(expect, bausWriter.toString());
    }
}
