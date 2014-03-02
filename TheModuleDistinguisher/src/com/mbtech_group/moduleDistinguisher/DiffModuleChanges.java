package com.mbtech_group.moduleDistinguisher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class DiffModuleChanges {

	private String file1;
	private String file2;
	private String basis = " ";
	private String sonderstand = " ";
	private Map<String, String> modulesChanged = new HashMap<String, String>();

	public DiffModuleChanges(String path1, String path2) {
		this.file1 = getFileContent(path1);
		this.file2 = getFileContent(path2);
		this.basis = getPKName(file1);
		this.sonderstand = getPKName(file2);

		Map<String, String> modules1 = getModules(this.file1);
		Map<String, String> modules2 = getModules(this.file2);
		this.modulesChanged = compareModules(modules1, modules2);
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}

	public String getFile1() {
		return this.file1;
	}

	public String getFile2() {
		return this.file2;
	}

	public String getBasis() {
		return this.basis;
	}

	public String getSonderstand() {
		return this.sonderstand;
	}

	public Map<String, String> getModulesChanged() {
		return this.modulesChanged;
	}

	public String getFileContent(String path) {

		String everything = "none";

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			everything = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return everything;
	}

	public String getPKName(String file) {

		String pkName = new String("");

		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile("PK\\s(\\w+)");

		Matcher m = p.matcher(file);
		if (m.find()) {
			pkName = m.group(1);
		}

		return pkName;
	}

	public Map<String, String> getModules(String file) {

		Map<String, String> table = new HashMap<String, String>();

		java.util.regex.Pattern p = java.util.regex.Pattern
		// .compile("\\s%(\\w+)\\s+(\\w+\\.[0-9]+)");
				.compile("\\s%(\\w+)\\s+(\\d{0,3})_\\d{0,2}[a-zA-Z]_(\\w+\\.[0-9]+)");

		Matcher m = p.matcher(file);
		while (m.find()) {
			table.put(m.group(1), m.group(2) + "." + m.group(3));
		}

		return table;
	}

	public Map<String, String> compareModules(Map<String, String> modules1,
			Map<String, String> modules2) {

		//changes first module
		Set<Entry<String, String>> changedEntries1 = new HashSet<Entry<String, String>>(
				modules1.entrySet());
		//-
		changedEntries1.removeAll(modules2.entrySet());

		Set<Entry<String, String>> changedEntries2 = new HashSet<Entry<String, String>>(
				modules2.entrySet());
		changedEntries2.removeAll(modules1.entrySet());

		changedEntries1.addAll(changedEntries2);

		
		Map<String, String> modulesChangedMap = new HashMap<String, String>();


		
		for (Entry<String, String> entry : changedEntries1) {
			if(! (modulesChangedMap.containsKey(entry.getKey())) ){
				modulesChangedMap.put(entry.getKey(), entry.getValue());
			} else {
				String value1 = modulesChangedMap.get(entry.getKey());
				String value2 = entry.getValue();		
				if( value1.compareTo(value2) < 0 ) {
					modulesChangedMap.put(entry.getKey(), value2);
				}
			}
		}
		
		return modulesChangedMap;
	}
}