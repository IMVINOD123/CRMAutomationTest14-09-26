package com.framework.utils;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        // Path relative to the root of src/test/resources
        String configPath = "com/framework/config/config.properties";

        try (InputStream fis = ConfigReader.class.getClassLoader().getResourceAsStream(configPath)) {
            if (fis == null) {
                throw new RuntimeException("Could not find file at classpath location: " + configPath);
            }
            properties.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Failed to load properties file from: " + configPath + " - " + e.getMessage());
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }
}