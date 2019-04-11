package com.tresa.framewok.code;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class DriverFactory {

	public static WebDriver getInstance(String browserName) {
		WebDriver driver = null;
		if (browserName == null) {
			browserName = PropertiesManager.getPropertyValue(FwProperties.BROWSER_NAME);
		}
		if (!PropertiesManager.isGrid()) {
			switch (Browsers.valueOf(browserName.toUpperCase())) {
			case CHROME:
				String driverPath = PropertiesManager.getPropertyValue(FwProperties.CHROME_DRIVER);
				System.setProperty(FwProperties.CHROME_DRIVER.getName(),driverPath);
				driver = new ChromeDriver();
				break;
			case FIREFOX:
				 driverPath = PropertiesManager.getPropertyValue(FwProperties.FIREFOX_DRIVER);
				System.setProperty(FwProperties.FIREFOX_DRIVER.getName(),driverPath);
				driver = new FirefoxDriver();
				break;
			default:
				throw new RuntimeException("Not implemented yet");
			}
		}
		else{
			// Selenium GRID or cloud
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability(CapabilityType.BROWSER_NAME,browserName );
			/** XXX: VT: Only for Browser stack for now*/
			caps.setCapability("os", "Windows");
			caps.setCapability("os_version", "7");
			caps.setCapability("resolution", "1680x1050");
			
			URL hubUrl=null;
			try {
				hubUrl = new URL(PropertiesManager.getHubUrl());
				driver = new RemoteWebDriver(hubUrl,caps);
			} catch (MalformedURLException e) {
				throw new RuntimeException("invalid hub url: "+ hubUrl);
			}
		}
		EventFiringWebDriver eventFiringDriver = new EventFiringWebDriver(driver);
		eventFiringDriver.register(new FwWebDriverListener());
		driver = eventFiringDriver;
		return driver;
	}

}
