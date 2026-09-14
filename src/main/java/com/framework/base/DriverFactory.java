package com.framework.base;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.framework.utils.ConfigReader;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    

    public static WebDriver initDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver.set(new ChromeDriver());
        } else if (browser.equalsIgnoreCase("firefox")) {
        	System.setProperty(ConfigReader.get("webdriverGecko"), ConfigReader.get("webdriverLocalPath"));
            
        	FirefoxOptions options = new FirefoxOptions();
            options.setAcceptInsecureCerts(true);
            // Specify the exact binary path to your firefox.exe
            options.setBinary(ConfigReader.get("firefoxBinarypath")); 
           
            driver.set(new FirefoxDriver(options));
        } else if(browser.equalsIgnoreCase("edge"))
        {
        	EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--remote-allow-origins=*");
        	System.setProperty(ConfigReader.get("webdriverEdge"), ConfigReader.get("webdriverLocpath"));
            
            driver.set(new EdgeDriver());
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