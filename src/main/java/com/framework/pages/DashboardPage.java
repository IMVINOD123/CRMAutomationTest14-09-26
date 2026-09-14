package com.framework.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

//import com.framework.test.DashboardTest;

public class DashboardPage {
	protected static final Logger log = LogManager.getLogger(DashboardPage.class);
	private WebDriver driver;

	// Locator for the modal close button ('X')
	@FindBy(xpath = "//button[contains(@class, 'close')] | //button[@aria-label='Close'] | //div[contains(@class,'formkit-close')]")
	private WebElement popUpCloseButton;

	@FindBy(xpath = "//h1[contains(., 'Hello')]")
	private WebElement helloHeader;
	// private WebElement HelloText;

	@FindBy(xpath = "//img[@alt='Practice Test Automation']")
	WebElement practiceTestAutomationText;

	@FindBy(xpath = "//a[contains(text(),'Practice')]")
	WebElement practiceTabl;

	public DashboardPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Step("Checking Image of Practice Test Automation ")
	public boolean isDisplayedPracticeTestAutomation() {
		return practiceTestAutomationText.isDisplayed();
	}

	@Step("Checking Pop Based on the availablity it taking actions")
	public void closePopUpIfPresent() {
		try {
			// Short 3-second wait so tests aren't delayed if the pop-up doesn't appear
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

			// Xpaths targeting Kit (ConvertKit) modal close buttons
			By closeBtnLocator = By.xpath(
					"//button[@data-element='close'] | //button[contains(@class,'formkit-close')] | //*[text()='✕' or text()='×']");

			WebElement closeButton = shortWait.until(ExpectedConditions.elementToBeClickable(closeBtnLocator));
			closeButton.click();
			log.info(">>> Pop-up closed successfully.");
		} catch (Exception e) {
			// If pop-up didn't appear, swallow the exception and continue execution
			log.info(">>> Pop-up not displayed, continuing execution.");
		}
	}

	@Step("Fecthing the Dash Board Text to Validate")
	public String isPracticeTestAutomationImageDisplayed() {

		// Explicitly wait up to 10 seconds for the element to be visible
		// WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		String str = helloHeader.getText();
		return str;

	}

	@Step("Click on Practice Link")
	public void clickOnPractice() {
		practiceTabl.click();
	}
}