package com.tresa.framewok.util;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.io.Resources;

/**
 * Assumptions 
 * :data starts from Row 0, Col 0.
 * :first Row is heading 
 * : Sheet is not empty
 * : no column should be empty in headers
 * : Date format dd/MM/yyyy
 * @author tresaJohn
 *
 */
public class CsvReader implements DpReader{
	private final String fileName;
	private final List<Map<String,String>> mapValues = new ArrayList<Map<String,String>>();
	private Object[][] data;
	public CsvReader(String csvFileName){
		this.fileName=csvFileName;
		load();
	}
	public void load(){
		final CSVFormat fmt = CSVFormat.DEFAULT.withHeader().withIgnoreEmptyLines().withIgnoreSurroundingSpaces();
		CSVParser csvParser = null;
		List<CSVRecord> records = null;
		try {
			csvParser = CSVParser.parse(Paths.get(Resources.getResource(fileName).toURI()).toFile(), StandardCharsets.UTF_8, fmt);
			records = csvParser.getRecords();
		} catch (final IOException e) {
			throw new RuntimeException("Parse CSV error", e);
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
		
		List<List<Object>> list = new ArrayList<List<Object>>();
		for(CSVRecord record : records) {
				ArrayList<Object> innerList = new ArrayList<Object>();
				for(int i = 0; i < record.size(); i++) {
					mapValues.add(record.toMap());
					innerList.add(record.get(i));
				}
				list.add(innerList);
		}
		
		//Convert list to array for TestNG
		data = new Object[list.size()][];
		for (int i = 0; i < list.size(); i++) {
		    List<Object> row = list.get(i);
		    data[i] = row.toArray(new Object[row.size()]);
		}
		
	}
	public Object[][] read() {
		return data;
	}
	@Override
	public Object[][] getDataMap() {
		Object[][] listData = new Object[mapValues.size()][1];
		int i=0;
		for (Map<String,String> value : mapValues) {
			listData[i++][0] = value;
		}
		return listData;
	}
	
}
