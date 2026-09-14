package com.framework.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.framework.utils.ConfigReader;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver initDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless=new");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1920,1080");
            
            driver.set(new ChromeDriver(chromeOptions));
            
        } else if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty(ConfigReader.get("webdriverGecko"), ConfigReader.get("webdriverLocalPath"));
            
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.setAcceptInsecureCerts(true);
            options.setBinary(ConfigReader.get("firefoxBinarypath"));
            
            driver.set(new FirefoxDriver(options));
            
        } else if (browser.equalsIgnoreCase("edge")) {
            System.setProperty(ConfigReader.get("webdriverEdge"), ConfigReader.get("webdriverLocpath"));
            
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--headless=new");
            edgeOptions.addArguments("--disable-gpu");
            edgeOptions.addArguments("--window-size=1920,1080");
            edgeOptions.addArguments("--remote-allow-origins=*");
            
            driver.set(new EdgeDriver(edgeOptions));
        }
        
        driver.get().manage().window().maximize();
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