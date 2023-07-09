import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class TestServerSettings {
    @Test
    public void testLoadSettingsFromFile() {
        //arrange
        ServerSettings serverSettings = new ServerSettings();
        String settings = "port 8181\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(settings.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        ServerSettings expect = new ServerSettings();
        expect.setPort(8181);
        //act
        serverSettings.loadSettingsFromFile(reader);
        //expect
        Assertions.assertEquals(expect, serverSettings);
    }
}
