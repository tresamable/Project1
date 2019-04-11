package com.dp;

import java.io.IOException;
import java.util.Properties;

import javax.management.RuntimeErrorException;

import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import com.google.common.io.Resources;
import com.tresa.framewok.code.PropertiesManager;

public class GHRegistrationDataProviders {
	public static final String GIT_HUB_REG_DATA = "github_reg_data";
	public static final String TEST_DATA_FILE="git_hub_reg_data.properties";
	@DataProvider(name = GIT_HUB_REG_DATA,parallel=PropertiesManager.RUN_DP_PARALLEL)
	public static Object[][] getRegData() {
		
		Properties props = new Properties();
		try {
			props.load(Resources.getResource(TEST_DATA_FILE).openStream());
			return new Object[][]{
				{	props.get("username"),
					props.get("password"),
					props.get("gmail_id"),
					props.get("gmail_password"),
				}
			};
		} catch (IOException e) {
			throw new RuntimeException("Error while readin input file : "+TEST_DATA_FILE,e );
		}
		
	}
	
}
