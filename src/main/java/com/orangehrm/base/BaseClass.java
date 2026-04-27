package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {
	
	protected static Properties prop;
	//protected static WebDriver driver;
	//private static ActionDriver actionDriver;
	
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	
	//getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	

	
	@BeforeSuite
	public void loadConfig() throws IOException {		
		//Load the configuration file
		prop = new Properties();
		FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(file);
		logger.info("config.properties file loaded");
		
		//Start Extent report
		//ExtentManager.getReporter(); --This has been implemented in TestListener
		
		
	}
	
	@BeforeMethod
	public synchronized void setup() throws IOException {  //add synchronise to use with thread having tests utilise same method to run one after another
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(Integer.parseInt(prop.getProperty("waitTimeLow")));
		
		logger.info("WebDriver initialised and Browser maximised");
		logger.trace("This is a Trace message");
		logger.error("This is a Error message");
		logger.debug("This is a Debug message");
		logger.fatal("This is a Fatal message");
		logger.warn("This is a Warn message");
		
		/*
		//Initialise actionDriver object class only once
		if(actionDriver == null) {
			actionDriver = new ActionDriver(driver);
			logger.info("ActionDriver instance is created. " + Thread.currentThread().getId());
		}
		*/
		
		//Initialise ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialised for Thread: " + Thread.currentThread().getId());

	}
	
	//initialise WebDriver based on defined browser in config.properties file
	private void launchBrowser() {

		String browser = prop.getProperty("browser");
		if(browser.equalsIgnoreCase("chrome")) {
			
			//Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); //Run Chrome in headless mode
			options.addArguments("--disable-gpu"); //Disable GPU for headless mode
			options.addArguments("--start-maximized");
			//options.addArguments("--window-size=1920,1080"); //Set window size
			options.addArguments("--force-device-scale-factor=1");
			options.addArguments("--high-dpi-support=1");
			options.addArguments("--disable-notifications"); //Disable browser notification
			options.addArguments("--no-sandbox"); //Required for some CI environments like Jenkins
			options.addArguments("--disable-dev-shm-usage"); //Resolve issues in resource-limited environments

			//driver = new ChromeDriver();
			driver.set(new ChromeDriver(options)); //changes for thread usage
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created.");
		}
		else if (browser.equalsIgnoreCase("firefox")) {
			
			// Create FirefoxOptions
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments
			
			//driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options)); //changes for thread usage
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver instance is created.");
		}
		else if (browser.equalsIgnoreCase("edge")) {
			
			//Create EdgeOptions
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			
			//driver = new EdgeDriver();
			driver.set(new EdgeDriver(options)); //changes for thread usage
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver instance is created.");
		}
		else {
			throw new IllegalArgumentException("Browser not supported:" + browser);
		}
		
	}
	
	/*
	 * Configure browser, implicit wait 
	 */
	private void configureBrowser() {
		//Implicit wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		
		//maximise the driver
		getDriver().manage().window().maximize();
		
		//navigate to the url
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to URL: " + e.getMessage());
		}
		
	}
	

	
	@AfterMethod
	public synchronized void tearDown() { //add synchronise to use with thread having tests utilise same method to run one after another
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quite the driver: " + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
		driver.remove();
		actionDriver.remove();
		//driver = null;
		//actionDriver = null;
		//ExtentManager.endTest(); --This has been implemented in TestListener
	}
	
	//prop Getter method
	
	public static Properties getProp() {
		return prop;
	}
	
	/*
	//Driver getter method
	public WebDriver getDriver() {
		return driver;
	}
	*/
	
	//Driver getter method for singleton approach -- modified for running ThreadLocal
	public static WebDriver getDriver() {
		if(driver.get() == null) {
			System.out.println("WebDriver instance is not initialised.");
			throw new IllegalStateException("WebDriver instance is not initialised.");
		}
		return driver.get();

	}
	
	//ActionDriver getter method for singleton approach -- modified for running thread locally
	public static ActionDriver getActionDriver() {
		if(actionDriver.get() == null) {
			System.out.println("ActionbDriver instance is not initialised.");
			throw new IllegalStateException("ActionbDriver instance is not initialised.");
		}
		return actionDriver.get();
		
	}
	
	//Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}
	
	
	//static Wait for Pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
