package com.framework.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import com.framework.base.*;
import com.framework.pages.DashboardPage;
import com.framework.pages.LoginPage;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class LoginTest extends BaseTest {
	protected static final Logger log = LogManager.getLogger(LoginTest.class);
	private LoginPage loginPage;

	/**
	 * Rigorous Test :-)
	 */
	@BeforeClass(alwaysRun = true)
	public void initLoginTest() {
		log.info(">>> Binding LoginPage instance in LoginTest");

		// Safely fetch ThreadLocal page object instantiated by BaseTest
		loginPage = getLoginPage();
	}

	@Test(groups = "regression")
	public void shouldAnswerWithTrue() {
		log.info(">>> Test Page Launcher:");
		Assert.assertTrue(true);
	}
}
