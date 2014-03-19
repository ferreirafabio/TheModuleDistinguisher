package com.mbtech_group.moduleDistinguisher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.*;

/**
 * 
 * @author Fabio
 * 
 */
public class DiffModuleChanges {

	private String file1;
	private String file2;
	private String basis = " ";
	private String sonderstand = " ";
	private Map<String, String> modulesChanged = new HashMap<String, String>();

	/**
	 * Creates a new DiffModuleChanges with both file contents as sequential
	 * strings. basis and sonderstand contain the extracted names of those two
	 * files. All modules which have experienced changes are listed in
	 * modulesChanged.
	 * 
	 * @param path1
	 *            Path to first file in ANSI format
	 * @param path2
	 *            Path to second file in ANSI format.
	 */
	public DiffModuleChanges(String path1, String path2) {
		this.file1 = getFileContent(path1);
		this.file2 = getFileContent(path2);
		this.basis = getPKName(file1);
		this.sonderstand = getPKName(file2);

		Map<String, String> modules1 = getModules(this.file1);
		Map<String, String> modules2 = getModules(this.file2);
		this.modulesChanged = compareModules(modules1, modules2);
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

	/**
	 * Reads the file content and saves it as a sequential arrangement.
	 * 
	 * @param path
	 *            Path for reading the file.
	 * @return File content as sequential string.
	 */
	public String getFileContent(String path) {

		String fileContent = "none";

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append('\n');
					line = br.readLine();
				}
				fileContent = sb.toString();
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
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return fileContent;
	}

	/**
	 * Extracts the PK name from a string, containing sequential arranged module
	 * information.
	 * 
	 * @param file
	 *            String containing module information.
	 * @return the PK name as a string.
	 */
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

	/**
	 * Extracts all the modules of a String, containing sequential arranged
	 * module information. The row-wise extraction involves the module name and
	 * its version number.
	 * 
	 * @param file
	 *            String containing module information.
	 * @return a Map(both key and value are type of String) containing all the
	 *         modules, the module name representing the Maps' key and the
	 *         version number representing the value.
	 */
	public Map<String, String> getModules(String file) {

		Map<String, String> moduleMap = new HashMap<String, String>();

		java.util.regex.Pattern p = java.util.regex.Pattern
		// .compile("\\s%(\\w+)\\s+(\\w+\\.[0-9]+)");
				.compile("\\s%(\\w+)\\s+(\\d{0,3})_\\d{0,2}[a-zA-Z]_(\\w+\\.[0-9]+)");

		Matcher m = p.matcher(file);
		while (m.find()) {
			moduleMap.put(m.group(1), m.group(2) + "." + m.group(3));
		}

		return moduleMap;
	}

	/**
	 * Compares two modules and detects differences. If there is a difference
	 * between two equal modules (that is, module name), the greater version
	 * number of the comparative modules is added to a new, final map, then
	 * containing module name and its version number. A module which is enlisted
	 * in one but not both files is also defined as a change and will be added
	 * to the returned map.
	 * 
	 * @param modules1
	 *            First map consisting of modules name and version numbers.
	 * @param modules2
	 *            Second map consisting of modules name and version numbers.
	 * @return Map containing all modules which have previously been determined
	 *         being different. Each entry consisting of module name and (the
	 *         greater) version number.
	 */
	public Map<String, String> compareModules(Map<String, String> modules1,
			Map<String, String> modules2) {

		// changes first module
		Set<Entry<String, String>> changedEntries1 = new HashSet<Entry<String, String>>(
				modules1.entrySet());

		changedEntries1.removeAll(modules2.entrySet());

		Set<Entry<String, String>> changedEntries2 = new HashSet<Entry<String, String>>(
				modules2.entrySet());

		// changes second module
		changedEntries2.removeAll(modules1.entrySet());

		changedEntries1.addAll(changedEntries2);

		Map<String, String> modulesChangedMap = new HashMap<String, String>();

		for (Entry<String, String> entry : changedEntries1) {
			if (!(modulesChangedMap.containsKey(entry.getKey()))) {
				modulesChangedMap.put(entry.getKey(), entry.getValue());
			} else {
				String value1 = modulesChangedMap.get(entry.getKey());
				String value2 = entry.getValue();
				if (value1.compareTo(value2) < 0) {
					modulesChangedMap.put(entry.getKey(), value2);
				}
			}
		}

		return modulesChangedMap;
	}
}
