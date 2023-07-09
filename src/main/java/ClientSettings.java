import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ClientSettings {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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
                if (nameSetting.equals("host")) {
                    host = settingArray[1];
                } else if (nameSetting.equals("port")) {
                    port = Integer.parseInt(settingArray[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return port + 31 * host.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        ClientSettings settings = (ClientSettings) obj;
        return this.port == settings.port
                && this.host.equals(settings.host);
    }
}
