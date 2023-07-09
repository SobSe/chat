import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class TestClientSettings {
    @Test
    public void testLoadSettingsFromFile() {
        //arrange
        ClientSettings clientSettings = new ClientSettings();
        String settings = "host localhost\nport 8181\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(settings.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        ClientSettings expect = new ClientSettings();
        expect.setHost("localhost");
        expect.setPort(8181);
        //act
        clientSettings.loadSettingsFromFile(reader);
        //expect
        Assertions.assertEquals(expect, clientSettings);
    }
}
