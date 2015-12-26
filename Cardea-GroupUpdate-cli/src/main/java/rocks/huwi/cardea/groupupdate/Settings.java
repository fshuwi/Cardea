package rocks.huwi.cardea.groupupdate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {

    private String host;
    private String adminDN;
    private String password;

    public Settings() {
        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("credentials.properties");

            properties.load(input);

            this.setAdminDN(properties.getProperty("adminDN"));
            this.setPassword(properties.getProperty("password"));
            this.setHost(properties.getProperty("host"));
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.INFO, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String getHost() {
        return host;
    }

    private void setHost(String host) {
        this.host = host;
    }

    public String getAdminDN() {
        return adminDN;
    }

    private void setAdminDN(String adminDN) {
        this.adminDN = adminDN;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
