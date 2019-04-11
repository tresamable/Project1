package com.dp;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.testng.annotations.DataProvider;

import com.tresa.framewok.code.PropertiesManager;
import com.tresa.framewok.util.ExcelReader;

public class ApRegistrationDataProviders {

	public static final String VALID_REG_TEST_DATA = "valid_reg_test_data";

	@DataProvider(name = VALID_REG_TEST_DATA,parallel=PropertiesManager.RUN_DP_PARALLEL)
	public static Object[][] getRegData() {
		Object[][] data = new ExcelReader("registration_test_data.xlsx").getDataMap();
		// filer / change data according to test case needs
		for (int i = 0; i < data.length; i++) {
			Map<String,String> map = (Map<String,String>)data[i][0];
			Set<String> keys = map.keySet();
			for (String key : keys) {
				String value = map.get(key);
				if(value!=null && !value.isEmpty() && value.contains("<RANDOM>")){
					String newValue = map.get(key).replace("<RANDOM>", UUID.randomUUID().toString().split("-")[0]);
					map.put(key,newValue);
				}
			}
			
		}
		return data;
	}
}
