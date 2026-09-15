package com.framework.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.pages.DashboardPage;
import com.framework.pages.PracticePage;

public class PracticeTest  extends BaseTest
{

	protected static final Logger log = LogManager.getLogger(PracticeTest.class);
	private PracticePage practicePage;

	@BeforeClass(alwaysRun = true)
    public void initDashboardTest() {
        log.info(">>> Binding PracticePage instance in PracticePageTest");
        practicePage = getPracticePage();
        //callPopupCloser();
    }

	
	@Test(groups ="regression",dependsOnMethods = {"ValidationOfDashBoardPage"})
	public void verifyPracticePageHeaderDetails()
	{
		String str=practicePage.verifyTitleOfPage();
		Assert.assertEquals(str,"Practice");
	}
}
