package juhlamokka;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * A simple configuration manager for creating, reading and editing program
 * configuration file
 */
public class ConfigManager {

    /**
     * The configuration file path
     */
    protected static String configFile = "config.properties";
    
    /**
     * The configuration properties object, stored across instances
     */
    public static final Properties DATA = new Properties(); 
    
    /**
     * Read the configuration file
     * @throws IOException
     */
    public static synchronized void readConfig() throws IOException {
        try (InputStream fd = new FileInputStream(configFile)) {
            DATA.load(fd);
        }
    }
    
    /**
     * Save properties to configuration file
     * @throws IOException
     */
    public static synchronized void updateConfig() throws IOException {
        try (OutputStream fd = new FileOutputStream(configFile)) {
            DATA.store(fd, null);
        }
    }
    
    public static void readDefaultConfig() {
        try {
            // Read if the configuration already happens to exist
            readConfig();
        } catch (IOException ex) {
            // Ok, let's just assign defaults
            DATA.setProperty("program-name", "Juhlamokka");
            DATA.setProperty("version", "v0.1");
            DATA.setProperty("password-minimum-length", "10");
            DATA.setProperty("check-password-pwnage", "true");
            DATA.setProperty("pwnage-check-api", "https://haveibeenpwned.com/api/v2/range/%s");
            DATA.setProperty("db.hostname", "localhost");
            DATA.setProperty("db.database", "juhlamokka");
            DATA.setProperty("db.username", "juhlamokka");
            DATA.setProperty("db.password", "akkomalhuj");
        }
    }
    
    /**
     * Set the configuration file path
     * @param configFile
     */
    public static void setConfigFile(String configFile) {
        ConfigManager.configFile = configFile;
    }
}
