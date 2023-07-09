import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerSettings {

    private int port;

    public ServerSettings() {

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void loadSettingsFromFile(BufferedReader reader) {
        try {
            String setting;
            while ((setting = reader.readLine()) != null) {
                String[] settingArray = setting.split(" ");
                String nameSetting = settingArray[0];
                if (nameSetting.equals("port")) {
                    this.port = Integer.parseInt(settingArray[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        ServerSettings settings = (ServerSettings) obj;
        return this.port == settings.port;
    }
}
