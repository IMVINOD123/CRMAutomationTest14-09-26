package com.framework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AllureReportManager {

    private static final Logger log = LogManager.getLogger(AllureReportManager.class);

    // List both common result paths so Maven & CLI catch the files regardless of configuration
    private static final String[] PATHS = {
        System.getProperty("user.dir") + "/allure-results",
        System.getProperty("user.dir") + "/target/allure-results"
    };

    public static void setupAllureMetaData() {
        for (String path : PATHS) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            generateEnvironmentProperties(path);
            generateCategoriesJson(path);
            generateExecutorJson(path);
            autoCleanAllureResults(path);
        }
    }

    private static void generateEnvironmentProperties(String basePath) {
        Properties props = new Properties();
        props.setProperty("Browser", System.getProperty("browser", "Chrome"));
        props.setProperty("Environment", "QA");
        props.setProperty("OS", System.getProperty("os.name"));
        props.setProperty("Java Version", System.getProperty("java.version"));
        props.setProperty("Executor", "QA Automation Team");

        File envFile = new File(basePath, "environment.properties");
        try (FileOutputStream fos = new FileOutputStream(envFile)) {
            props.store(fos, "Allure Environment Properties");
        } catch (IOException e) {
            log.error("Failed to generate environment.properties at " + basePath + ": " + e.getMessage());
        }
    }

    private static void generateCategoriesJson(String basePath) {
        String categoriesJson = "[\n" +
                "  {\n" +
                "    \"name\": \"Product Defects (Assertion Errors)\",\n" +
                "    \"matchedStatuses\": [\"failed\"],\n" +
                "    \"messageRegex\": \".*AssertionError.*\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Element Not Found / Locators Broken\",\n" +
                "    \"matchedStatuses\": [\"broken\"],\n" +
                "    \"messageRegex\": \".*NoSuchElementException.*|.*TimeoutException.*\"\n" +
                "  }\n" +
                "]";

        writeFile(basePath, "categories.json", categoriesJson);
    }

    private static void generateExecutorJson(String basePath) {
        String executorJson = "{\n" +
                "  \"name\": \"Eclipse / Local Maven Execution\",\n" +
                "  \"type\": \"maven\",\n" +
                "  \"buildName\": \"CRM-Automation-Suite\",\n" +
                "  \"reportName\": \"CRM Hybrid Framework Execution Report\"\n" +
                "}";

        writeFile(basePath, "executor.json", executorJson);
    }

    private static void writeFile(String basePath, String fileName, String content) {
        File file = new File(basePath, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            log.error("Failed to generate " + fileName + " at " + basePath + ": " + e.getMessage());
        }
    }
    

    public static void autoCleanAllureResults(String basePath) {
        File allureResultsDir = new File(basePath);

        if (allureResultsDir.exists() && allureResultsDir.isDirectory()) {
            File[] files = allureResultsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
            log.info(">>> Cleaned previous Allure results from directory: {}", basePath);
        }
    }
}