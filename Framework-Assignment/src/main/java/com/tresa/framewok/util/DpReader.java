package com.tresa.framewok.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DpReader {
	/**
	 * Method to get CSV data Headers will not be included
	 * 
	 * @return
	 */
	public Object[][] read();

	/**
	 * Method to get list of key value mapping of data
	 */
	public Object[][] getDataMap();
}
