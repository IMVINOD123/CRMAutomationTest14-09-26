package com.framework.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.framework.pages.DashboardPage;
import com.framework.pages.LoginPage;
import com.framework.pages.PracticePage;
import com.framework.utils.AllureReportManager;
import com.framework.utils.ConfigReader;
//import com.framework.utils.DriverFactory;
import com.framework.utils.JsonReader;

public class BaseTest {

	protected static final Logger log = LogManager.getLogger(BaseTest.class);

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();
	private static ThreadLocal<DashboardPage> dashboardPage = new ThreadLocal<>();
	private static ThreadLocal<PracticePage> practicePage = new ThreadLocal<>();

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static LoginPage getLoginPage() {
		return loginPage.get();
	}

	public static DashboardPage getDashboardPage() {
		return dashboardPage.get();
	}

	public static PracticePage getPracticePage() {
		return practicePage.get();
	}

	@BeforeSuite
	public void beforeSuiteSetup() {
		AllureReportManager.autoCleanAllureResults(System.getProperty("user.dir") + "/target/allure-results");
		AllureReportManager.autoCleanAllureResults(System.getProperty("user.dir") + "/allure-results");
		AllureReportManager.setupAllureMetaData();
	}

	// @BeforeTest runs ONCE per <test> block in testng.xml (reusing the browser
	// across all classes inside it)
	@Parameters({ "browser" })
	@BeforeTest(alwaysRun = true)
	public void baseTestSetUp(@Optional("") String xmlBrowser) {

		if (getDriver() != null) {
			log.info(">>> Driver already initialized for thread [" + Thread.currentThread().getId() + "]");
			return;
		}

		String browser = System.getProperty("browser");
		if (browser == null || browser.trim().isEmpty()) {
			browser = xmlBrowser;
		}
		if (browser == null || browser.trim().isEmpty()) {
			browser = JsonReader.get("browser");
		}
		if (browser == null || browser.trim().isEmpty()) {
			try {
				browser = ConfigReader.get("browser");
			} catch (Exception e) {
				// Ignore
			}
		}

		log.info(">>> Launching SINGLE browser instance for entire <test> suite: " + browser);

		String url = JsonReader.get("url");
		if (url == null || url.trim().isEmpty()) {
			url = ConfigReader.get("url");
		}

		WebDriver localDriver = DriverFactory.initDriver(browser);
		localDriver.get(url);
		driver.set(localDriver);

		loginPage.set(new LoginPage(getDriver()));
		dashboardPage.set(new DashboardPage(getDriver()));
		practicePage.set(new PracticePage(getDriver()));
	}

	// @AfterTest runs ONCE after ALL test classes in the <test> block complete
	@AfterTest(alwaysRun = true)
	public void baseTestTearDown() {
		if (getDriver() != null) {
			log.info(">>> Quitting browser instance after all test classes finished.");
			DriverFactory.quitDriver();
			driver.remove();
			loginPage.remove();
			dashboardPage.remove();
		}
	}
}