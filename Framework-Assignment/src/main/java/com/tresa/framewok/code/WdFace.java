package com.tresa.framewok.code;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

public class WdFace {

	private WebDriver driver;

	public WebDriver driver(String browserName) {
		if (driver == null) {
			driver = DriverFactory.getInstance(browserName);
			driver.manage().timeouts().implicitlyWait(PropertiesManager.getImplicitWaitTimeout(), TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(PropertiesManager.getPageLoadTimeout(), TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(PropertiesManager.getScriptTimeout(), TimeUnit.SECONDS);
			driver.manage().window().maximize();
		}
		return driver;
	}

	public void clieanUp() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

}
