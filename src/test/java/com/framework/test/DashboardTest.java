package com.framework.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.pages.DashboardPage;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class DashboardTest extends BaseTest {

	protected static final Logger log = LogManager.getLogger(DashboardTest.class);
	private DashboardPage dashboardPage;

	@BeforeClass(alwaysRun = true)
    public void initDashboardTest() {
        log.info(">>> Binding DashboardPage instance in DashboardTest");
        dashboardPage = getDashboardPage();
        //callPopupCloser();
    }

	@Severity(SeverityLevel.CRITICAL)
	@Feature("DashBoard Module")
	@Description("Verify header text on the dashboard")
	@Test(groups = { "sanity","regression"})
	public void ValidationOfDashBoardPage() {
		log.info(">>> Closing the Popup if present");
		dashboardPage.closePopUpIfPresent();
		String hellotext = dashboardPage.isPracticeTestAutomationImageDisplayed();
		log.info(">>> Fetching the DashBoard Text: " + hellotext);
		Assert.assertEquals(hellotext, "Hello");

		boolean isDisplayed = dashboardPage.isDisplayedPracticeTestAutomation();
		Assert.assertTrue(isDisplayed, "Image should be displayed");
		log.info("Fetching Image Is Present or not !: " + isDisplayed);
		
		
		log.info("Navigate to the Practice page after click on it ");
		dashboardPage.clickOnPractice();
		
		
	}
}