package com.orangehrm.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass{
	
	//initialise the Page objects
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	//Create the Page objects and passing the WebDriver instance
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String emp_id, String emp_name) {
		
		SoftAssert softAssert = getSoftAssert();
		
		ExtentManager.logStep("Login with Admin Credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		
		ExtentManager.logStep("Click on PIM tab");
		homePage.clickOnPIMTab();
		
		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch(emp_name);
		
		ExtentManager.logStep("Get the employee name from DB");
		String employee_id = emp_id;
		
		//Fetch the data into a map
		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		
		String emplFirstName = employeeDetails.get("firstName");
		String emplMiddiletName = employeeDetails.get("middleName");
		String emplLastName = employeeDetails.get("lastName");
		
		String emplFirstAndMiddleName = (emplFirstName + " " + emplMiddiletName).trim();
		
		//Validation for first and middle names
		ExtentManager.logStep("Verify employee first and middle name");
		//Assert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName), "First and Middle name are not Matching");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName), "First and Middle name are not Matching");
		
		//Validation for last name
		ExtentManager.logStep("Verify employee last name");
		//Assert.assertTrue(homePage.verifyEmployeeLastame(emplLastName), "Last name is not Matching");
		softAssert.assertTrue(homePage.verifyEmployeeLastame(emplLastName), "Last name is not Matching");
		
		ExtentManager.logStep("DB Employee validation completed");
		
		softAssert.assertAll();
		
	}

}
