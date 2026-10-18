package tr.abdullah.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigurationReader loads properties from config.properties file
 * and provides static access to project-level configurations.
 */
public class ConfigurationReader {

    private static final Logger logger = LogManager.getLogger(ConfigurationReader.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigurationReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                logger.error("Unable to find config.properties file in resources.");
                throw new RuntimeException("config.properties file not found in classpath");
            }

            properties.load(inputStream);
            logger.info("Configuration properties loaded successfully.");
        } catch (IOException e) {
            logger.error("Failed to load config.properties file: {}", e.getMessage());
            throw new RuntimeException("Could not load configuration file", e);
        }
    }

    /**
     * Retrieves the configuration value for a given key.
     *
     * @param key Property key name
     * @return Configured property value or null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}