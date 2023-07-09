import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class TestClient {
    @Test
    public void testReadMessage() {
        //arrange
        String message = "Sergey: Hi";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer logger = new PrintWriter(byteArrayOutputStream);
        Date date = new Date();
        //act
        try {
            Client.readMessage(message, logger, date);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expect = String.format("[%1$td.%1$tm.%1$tY %1$tT] %2$s\n", date, message);
        //assert
        Assertions.assertEquals(expect, byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteMessage() {
        //arrange
        String message = "Hi all";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter messageWriter = new PrintWriter(byteArrayOutputStream, true);
        //act
        try {
            Client.writeMessage(messageWriter, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expect = "Hi all\n";
        //expect
        Assertions.assertEquals(expect, byteArrayOutputStream.toString());
    }
}
