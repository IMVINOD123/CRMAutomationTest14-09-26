package com.framework.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

public class PracticePage {
	protected static final Logger log = LogManager.getLogger(PracticePage.class);
	private WebDriver driver;

	public PracticePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//h1[contains(@class,'post-title')]")
	WebElement practiceTitle;

	@Step("Validate the Practice title in the Practice page")
	public String verifyTitleOfPage() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    return wait.until(ExpectedConditions.visibilityOf(practiceTitle)).getText();
	}
}
