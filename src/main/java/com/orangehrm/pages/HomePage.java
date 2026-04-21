package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	private ActionDriver actionDriver;
	
	//Define locators using By class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIdButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMlogo = By.xpath("//div[@class='oxd-brand-banner']/img");
	
	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By.xpath("//label[text()='Employee Name'] /parent::div/following-sibling::div/div/div/input");
	private By searchButton = By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By emplLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");
	
	//initialise ActionDriver object by passing webDriver instance
	/*
	public HomePage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
	}
	*/
	
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
		
	}
	
	//Method to verify if admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.elementIsDisplayed(adminTab);
		
	}
	
	//verify if ornageHRM logo is displayed
	public boolean verifyOrangeHRMlogo() {
		return actionDriver.elementIsDisplayed(orangeHRMlogo);
	}
	
	//Method to navigate to PIM tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}
	
	//Employee search
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch,value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(emplFirstAndMiddleName);
	}
	
	//verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(emplFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
	}
	
	//verify employee last name
	public boolean verifyEmployeeLastame(String emplLastNameFromDB) {
		return actionDriver.compareText(emplLastName, emplLastNameFromDB);
	}
	
	//Method to perform logout operation
	public void logout() {
		actionDriver.click(userIdButton);
		actionDriver.click(logoutButton);
	}
	

}
