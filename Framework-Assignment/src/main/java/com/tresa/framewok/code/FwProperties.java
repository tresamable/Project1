package com.tresa.framewok.code;

import org.openqa.selenium.remote.CapabilityType;

public enum FwProperties {

	BROWSER_NAME("capability_" + CapabilityType.BROWSER_NAME, "chrome"), PROPERTIES_FILE("properties_file",
			"defaults.properties"), 
	IS_GRID("fw_is_grid","true"),
	HUB_URL("fw_hub_url","http://tresa1:qzyZts6XhMVLBj5TozVt@hub-cloud.browserstack.com/wd/hub"),
	CHROME_DRIVER("webdriver.chrome.driver","/Users/tresa/Downloads/chromedriver"),
	FIREFOX_DRIVER("webdriver.gecko.driver","/Users/tresa/Downloads/geckodriver"),
	/* Timeouts & defaults */
	EXPLICIT_TIMEOUT_WAIT("fw_timeout_explicit","60"),
	EXPLICIT_ELEMENT_WAIT("fw_timeout_element","10"),
	IMPLICIT_WAIT("fw_timeout_implicit_wait","5"),
	MAX_PAGE_LOAD_TIMEOUT("fw_timeout_page_load","300"), 
	MAX_SCRIPT_TIMEOUT("fw_timeout_script","300");
	private String propName;
	private String defaultValue;

	FwProperties(String propName, String defaultValue) {
		this.propName = propName.toLowerCase();
		this.defaultValue = defaultValue;
	}

	public String getValue() {
		return defaultValue;
	}

	public String getName() {
		return propName;
	}
	/**
	 * Method to get default value of any property by it's name
	 * @param propName Property name
	 * @return default value for given property else null if property not found
	 */
	public static String getDefaultValue(String propName) {
		String value = null;
		for (FwProperties prop : FwProperties.values()) {
			if (prop.getName().equalsIgnoreCase(propName)) {
				value = prop.getValue();
				break;
			}
		}
		return value;
	}

}
