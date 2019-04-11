package com.tresa.framewok.code;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.common.io.Resources;

public class PropertiesManager {
	/**
	 * Store all environment level properties
	 */
	private static Map<Object, Object> envProps;
	/**
	 * Store all System level properties
	 */
	private static Map<Object, Object> systemProps;
	/**
	 * Store all file level properties
	 */
	private static Map<Object, Object> fileProps;
	/**
	 * Flag to check properties are initialized.
	 */
	/**
	 * TODO: VT: below param change to dynamic
	 */
	public static final boolean RUN_DP_PARALLEL=false;
	
	private static boolean isInitialized;

	private static void loadProperties() {
		envProps = new HashMap<Object, Object>();
		envProps.putAll(System.getenv());

		systemProps = System.getProperties();
		try {
			Properties props = new Properties();
			props.load(Resources.getResource(FwProperties.PROPERTIES_FILE.getValue()).openStream());
			fileProps = props;
			isInitialized = true;
		} catch (Exception e) {
			// just ignore
		}
		isInitialized = true;
	}
	public static String getPropertyValue(FwProperties prop) {
		return getPropertyValue(prop.getName());
	}
	/**
	 * Method to search property name and return value
	 * 
	 * @param propName
	 *            name of property.
	 * @return value of property else null if not found
	 */
	public static String getPropertyValue(String propName) {
		if (!isInitialized) {
			loadProperties();
		}
		String value = getPropValue(fileProps, propName);
		if (value != null && !value.isEmpty()) {
			return value;
		}
		value = getPropValue(systemProps, propName);
		if (value != null && !value.isEmpty()) {
			return value;
		}
		value = getPropValue(envProps, propName);
		if (value != null && !value.isEmpty()) {
			return value;
		}
		return FwProperties.getDefaultValue(propName);
	}

	/**
	 * Method to return property value from properties map
	 * @param prop
	 * @param propName
	 * @return property value else null if not found
	 */
	private static String getPropValue(Map<Object, Object> prop, String propName) {
		try {
			return prop.get(propName).toString().trim();
		} catch (Exception e) {
			return null;
		}
	}
	public static boolean isGrid() {
		return Boolean.valueOf(getPropertyValue(FwProperties.IS_GRID));
	}
	public static long getElementTimeout() {
		return Integer.parseInt(getPropertyValue(FwProperties.EXPLICIT_ELEMENT_WAIT));
	}
	public static long getExplicitTimeout() {
		return Integer.parseInt(getPropertyValue(FwProperties.EXPLICIT_TIMEOUT_WAIT));
	}
	public static long getImplicitWaitTimeout(){
		return Integer.parseInt(getPropertyValue(FwProperties.IMPLICIT_WAIT));
	}
	public static long getPageLoadTimeout() {
		return Integer.parseInt(getPropertyValue(FwProperties.MAX_PAGE_LOAD_TIMEOUT));
	}
	public static long getScriptTimeout() {
		return Integer.parseInt(getPropertyValue(FwProperties.MAX_SCRIPT_TIMEOUT));
	}
	public static String getHubUrl() {
		return getPropertyValue(FwProperties.HUB_URL);
	}
}
