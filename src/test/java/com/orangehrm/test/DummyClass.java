package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass{
	
	@Test
	public void dummyTest() {
		
		//ExtentManager.startTest("Dummy Test1 test"); --This has been implemented in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verify the title");
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching";
		ExtentManager.logStep("Validation successful");
		
		System.out.println("Test Passed - Title is matching");
		//ExtentManager.logSkip("This test case is skipped"); --This has been implemented in TestListener
		throw new SkipException("Skipping the test as part of testing");
	}
	

}
