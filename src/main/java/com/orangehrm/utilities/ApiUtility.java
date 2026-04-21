package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {
	
	//Method to send GET request
	public static Response sendGetRequest(String endPoint) {
		return RestAssured.get(endPoint);
	}
	
	//Method to send POST request
	public static Response sendPostRequest(String endPoint, String payLoad) {
		return RestAssured.given().header("Content-Type", "application/json")
					.body(payLoad)
					.post();
	}
	
	//Method to validate the response status
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.statusCode() == statusCode;
	}
	
	//Method to extract value from JSON response
	public static String getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);
	}

}
