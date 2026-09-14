package com.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class JsonReader {
    private static JsonNode jsonNode;

    static {
        String jsonPath = "com/framework/config/config.json";

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = JsonReader.class.getClassLoader().getResourceAsStream(jsonPath);

            if (inputStream == null) {
                throw new RuntimeException("Could not find file at classpath location: " + jsonPath);
            }

            jsonNode = mapper.readTree(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Failed to load JSON file from: " + jsonPath + " - " + e.getMessage());
        }
    }

    public static String get(String key) {
        return (jsonNode != null && jsonNode.has(key)) ? jsonNode.get(key).asText() : null;
    }
}