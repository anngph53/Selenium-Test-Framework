package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {
	
	@Test
	//@Test(retryAnalyzer = RetryAnalyzer.class) -- moved RetryAnalyzer to implement in Listener
	public void verifyGetUserAPI() {
		
		SoftAssert softAssert = new SoftAssert();
		
		//Step 1: define API end point
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API endPoint: " + endPoint);
		
		//Step 2: Send GET request
		ExtentManager.logStep("Sending GET request to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);
		
		//Step 3: validate status code
		ExtentManager.logStep("Validating API response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		
		//Assert.assertTrue(isStatusCodeValid, "Status code is not as expected");
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as expected");
		
		if(isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code validation Passed!");
			
		} else {
			ExtentManager.logFailureAPI("Status code validation Failed!");
		}
		
		//Step 4: Validate user name
		ExtentManager.logStep("Validate response body for username");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		//Assert.assertTrue(isUserNameValid, "Username is not valid");
		softAssert.assertTrue(isUserNameValid, "Username is not valid");
		if(isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Username validation Failed!");
		}
		
		//Step 5: Validate email
		ExtentManager.logStep("Validate response body for email");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		//Assert.assertTrue(isEmailValid, "Email is not valid");
		softAssert.assertTrue(isEmailValid, "Email is not valid");
		if(isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Email validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Email validation Failed!");
		}
		
		softAssert.assertAll();
	}
}
