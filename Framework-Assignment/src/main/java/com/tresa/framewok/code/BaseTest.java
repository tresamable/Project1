package com.tresa.framewok.code;

import org.openqa.selenium.WebDriver;

public class BaseTest{

	private static ThreadLocal<WdFace> driver = new ThreadLocal<WdFace>();
	private static ThreadLocal<String> browserName = new ThreadLocal<String>();

	protected static WebDriver driver() {
		WdFace driverFace;
		driverFace = BaseTest.driver.get();
		if (driverFace == null) {
			driverFace = new WdFace();
			driver.set(driverFace);
		}
		return BaseTest.driver.get().driver(BaseTest.browserName.get());
	}
	public static void cleanUp(){
		WdFace driverFace = BaseTest.driver.get();
		if(driverFace!=null){
			driverFace.clieanUp();
		}
	}

	public static void setBrowserName(String browserName) {
		BaseTest.browserName.set(browserName);
	}

}
