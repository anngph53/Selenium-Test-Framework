package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass{
	
	@Test
	public void dummyTest2() {
		
		//ExtentManager.startTest("Dummy Test2 test"); --This has been implemented in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verify the title" + title);
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching";
		ExtentManager.logStep("Validation successful");
		//System.out.println("Test Passed - Title is matching");
		//ExtentManager.logSkip("This test case is skipped");
		
	}
	

}
