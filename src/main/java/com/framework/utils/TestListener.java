package com.framework.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.framework.base.BaseTest;


import io.qameta.allure.Attachment;

public class TestListener extends BaseTest implements ITestListener,IExecutionListener {
	// Capture screenshot and attach directly to Allure report
	protected static final Logger log = LogManager.getLogger(TestListener.class);
	
    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    @Attachment(value = "Test Execution Video", type = "video/avi")
    public byte[] attachVideoToAllure(File videoFile) {
        try (FileInputStream fis = new FileInputStream(videoFile)) {
            byte[] bytes = new byte[(int) videoFile.length()];
            fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    // Helper method to automatically delete temporary recording file
    private void deleteVideo(File videoFile) {
        if (videoFile.exists()) {
            videoFile.delete();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
    	log.error("Test Failed: " + result.getName());
        
    	WebDriver driver = BaseTest.getDriver();
        if (driver != null) {
        	saveScreenshotPNG(driver);
        }

        // Stop recording and attach video
        File videoFile = VideoRecorderUtils.stopRecording();
        if (videoFile != null) {
            attachVideoToAllure(videoFile);
            deleteVideo(videoFile);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
    	log.info("Starting Test: " + result.getName());
    	VideoRecorderUtils.startRecording(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    	log.warn("Test Skipped: " + result.getName());
    	File videoFile = VideoRecorderUtils.stopRecording();
        if (videoFile != null) {
            deleteVideo(videoFile); // Clean up skipped test video
        }
    }

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}
    
    @Override
    public void onExecutionStart() {
        // Leave empty or add start logs
    }

    @Override
    public void onExecutionFinish() {
        // Fires ONCE after all tests complete, right before allure:serve runs
        writeAllureMetadata();
    }

    private void writeAllureMetadata() {
        String[] targets = {
            System.getProperty("user.dir") + "/target/allure-results",
            System.getProperty("user.dir") + "/allure-results"
        };

        for (String targetDir : targets) {
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Write environment.properties
            Properties props = new Properties();
            props.setProperty("Browser", ConfigReader.get("Browser"));
            props.setProperty("Environment", ConfigReader.get("Environment"));
            props.setProperty("URL", ConfigReader.get("URL"));
            props.setProperty("OS", ConfigReader.get("OS"));
            props.setProperty("Executor", ConfigReader.get("Executor"));

            try (FileOutputStream fos = new FileOutputStream(new File(dir, "environment.properties"))) {
                props.store(fos, "Allure Environment Properties");
            } catch (IOException e) {
                // handle error
            }

            // Write categories.json
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

            try (FileWriter writer = new FileWriter(new File(dir, "categories.json"))) {
                writer.write(categoriesJson);
            } catch (IOException e) {
                // handle error
            }

            // Write executor.json
            String executorJson = "{\n" +
                    "  \"name\": \"Eclipse / Local Maven Execution\",\n" +
                    "  \"type\": \"maven\",\n" +
                    "  \"buildName\": \"CRM Automation Framework\",\n" +
                    "  \"reportName\": \"CRM Hybrid Execution Report\"\n" +
                    "}";

            try (FileWriter writer = new FileWriter(new File(dir, "executor.json"))) {
                writer.write(executorJson);
            } catch (IOException e) {
                // handle error
            }
        }
    }

}
