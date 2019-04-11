package com.tresa.framewok.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.io.Resources;

/**
 * Assumptions 
 * :data starts from Row 0, Col 0.
 * :first Row is heading 
 * : Sheet is not empty
 * : no column should be empty in headers
 * : Date format dd/MM/yyyy
 * : Data is correct in csv / excel.
 * @author tresaJohn
 *
 */
public class ExcelReader implements DpReader{

	Workbook workbook;
	Sheet sheet;
	private Object[][] dataIncludingHeaders;
	
	public ExcelReader(String filePath,String sheetName) {
		try {
			workbook = new XSSFWorkbook(Resources.getResource(filePath).openStream());
			this.sheet = workbook.getSheet(sheetName);
			load();
		} catch (IOException e) {
			throw new RuntimeException("Error while reading workbook", e);
		}
	}
	/**
	 * Default sheet is first sheet from workbook
	 * @param filePath
	 */
	public ExcelReader(String filePath) {
		try {
			workbook = new XSSFWorkbook(Resources.getResource(filePath).openStream());
			this.sheet = workbook.getSheetAt(0);
			load();
		} catch (IOException e) {
			throw new RuntimeException("Error while reading workbook", e);
		}
	}


	public Object getValue( int rowIndex, int colIndex) {
		Object value = null;
		Cell c = sheet.getRow(rowIndex).getCell(colIndex);
		if (c==null){
			return null;
		}
		switch (c.getCellTypeEnum()) {
		case STRING:
			value = c.getStringCellValue();
			break;
		case NUMERIC:
			value = c.getNumericCellValue();
			if(HSSFDateUtil.isCellDateFormatted(c)){
				value = c.getDateCellValue();
			}
			break;
		case _NONE:
		case BLANK:
			value = "";
			break;
		case ERROR:
			value = "#ERR#";
			break;
		case BOOLEAN:
			value = c.getBooleanCellValue();
			break;
		case FORMULA:
			value = c.getCellFormula();
			break;
		default:
			break;
		}
		return value;
	}
	private Row getRow(int rowIndex) {
		return sheet.getRow(rowIndex);
	}
	void closeReading() {
		try {
			workbook.close();
		} catch (IOException e) {
			// XXX: ignore  for now
			e.printStackTrace();
		}
	}
	public void load(){
		int lastRowNumber = sheet.getLastRowNum() ;
		int colCount = getRow(0).getLastCellNum();
		dataIncludingHeaders = new Object[lastRowNumber+1][colCount];
		for (int i = 0; i < lastRowNumber+1; i++) {
			for (int j = 0; j < colCount; j++) {
				dataIncludingHeaders[i][j] =getValue(i, j);
			}
		}
	}
	@Override
	public Object[][] read() {
		Object[][] data = new Object[dataIncludingHeaders.length-1][];
		if(dataIncludingHeaders.length<2){
			throw new RuntimeException("No Data fround in excel file");
		}
		// Remove headers
		for (int i = 0; i < dataIncludingHeaders.length-1; i++) {
			data[i]=dataIncludingHeaders[i+1];
		}
		return data;
	}
	@Override
	public Object[][] getDataMap() {
		//List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		Object[][] dataMap = new Object[dataIncludingHeaders.length-1][1];
		if(dataIncludingHeaders.length<2){
			throw new RuntimeException("No Data fround in excel file");
		}
		Object[] headers = dataIncludingHeaders[0];
		for (int i = 1; i < dataIncludingHeaders.length; i++) {
			Map<String, String> mapData = new LinkedHashMap<String,String>();
			for (int j = 0; j < dataIncludingHeaders[i].length; j++) {
				mapData.put(headers[j].toString(), toStringValue(dataIncludingHeaders[i][j]));
			}
			dataMap[i-1][0] = mapData;
		}
		return dataMap;
	}
	private String toStringValue(Object object) {
		// handle NPE
		if(object==null){
			return "";
		}
		if(object instanceof Date){
			Date d = (Date) object;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			return format.format(d);
		}
		if(object instanceof Double){
			double dblValue= (double) object;
			if(dblValue%1==0){
				return (int)dblValue+"";
			}
		}
		return object.toString();
	}
}
