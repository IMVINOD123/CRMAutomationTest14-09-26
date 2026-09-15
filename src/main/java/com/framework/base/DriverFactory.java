package com.framework.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.framework.utils.ConfigReader;

// Optional: You can remove this import if you don't use WebDriverManager elsewhere
// import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Helper method to detect if execution is running inside Jenkins
    private static boolean isJenkinsExecution() {
        return System.getenv("JENKINS_HOME") != null || System.getenv("BUILD_NUMBER") != null;
    }

    public static WebDriver initDriver(String browser) {
        boolean isHeadless = isJenkinsExecution();
        System.out.println("Execution Environment -> Jenkins: " + isHeadless + " | Running Headless: " + isHeadless);

        if (browser.equalsIgnoreCase("chrome")) {
            // REMOVED: WebDriverManager.chromedriver().setup();
            // Selenium 4.21.0 automatically handles downloading the matching ChromeDriver (v152)

            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--window-size=1920,1080"); // Set size for both local and headless
            
            if (isHeadless) {
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
            }
            
            driver.set(new ChromeDriver(chromeOptions));
            
        } else if (browser.equalsIgnoreCase("firefox")) {
            // Optional: Commenting out hardcoded path properties lets Selenium Manager handle Firefox too
            // System.setProperty(ConfigReader.get("webdriverGecko"), ConfigReader.get("webdriverLocalPath"));
            
            FirefoxOptions options = new FirefoxOptions();
            options.setAcceptInsecureCerts(true);
            
            // Only set binary if explicitly defined in config
            if (ConfigReader.get("firefoxBinarypath") != null && !ConfigReader.get("firefoxBinarypath").isEmpty()) {
                options.setBinary(ConfigReader.get("firefoxBinarypath"));
            }
            
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            
            if (isHeadless) {
                options.addArguments("--headless");
            }
            
            driver.set(new FirefoxDriver(options));
            
        } else if (browser.equalsIgnoreCase("edge")) {
            // Optional: Commenting out hardcoded path properties lets Selenium Manager handle Edge too
            // System.setProperty(ConfigReader.get("webdriverEdge"), ConfigReader.get("webdriverLocpath"));
            
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--remote-allow-origins=*");
            edgeOptions.addArguments("--window-size=1920,1080");
            
            if (isHeadless) {
                edgeOptions.addArguments("--headless=new");
                edgeOptions.addArguments("--disable-gpu");
            }
            
            driver.set(new EdgeDriver(edgeOptions));
        }
        
        // Safely maximize only for non-headless desktop runs
        if (!isHeadless && driver.get() != null) {
            driver.get().manage().window().maximize();
        }
        
        return driver.get();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}