package com.mbtech_group.moduleDistinguisher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 
 * @author Fabio
 *
 */

public class ProcessXLS {

	
	private static final int MY_MINIMUM_COLUMN_COUNT = 0;
	private InputStream inp;
	private OutputStream out;
	private String filepath;
	private Workbook wb;
	private ArrayList<String> existingModules = new ArrayList<String>();
	private ArrayList<String> complementaryModules = new ArrayList<String>();
	private int nextFreeColumn;
	private int nextFreeRow;

	public ProcessXLS(String filePath) {
		this.filepath = filePath;
		readExcelWorkbook();
	}

	/**
	 * FileInputStream is used to create a workbook. The next free coordinates
	 * (row, column)for writing are determined and it will be checked for the
	 * sheets' existing modules.
	 */
	public void readExcelWorkbook() {
		try {
			inp = new FileInputStream(filepath);
			this.wb = WorkbookFactory.create(inp);
			closeInputStream();
			setNextFreeColumn(1, 1);
			setNextFreeRow(1, 1);
			findExistingModules();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * FileOutputStream is created to write out data.
	 * 
	 * @throws IOException
	 *             Either no FileOutputStream could be created or no data could
	 *             be written out.
	 */
	public void writeExcelWorkBook() throws IOException {
		try {
			out = new FileOutputStream(this.filepath);
			this.wb.write(out);
			closeOutputStream();
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	/**
	 * InputStream is closed.
	 */
	public void closeInputStream() {
		if (this.inp != null) {
			try {
				inp.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * OutputStream is closed.
	 */
	public void closeOutputStream() {
		if (this.out != null) {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ArrayList<String> getExistingModules() {
		return this.existingModules;
	}

	public ArrayList<String> getComplementaryModules() {
		return this.complementaryModules;
	}

	public int getNextFreeColumn() {
		return this.nextFreeColumn;
	}

	public int getNextFreeRow() {
		return this.nextFreeRow;
	}

	/**
	 * Determines the next free cell of a given column, available to write out
	 * data. Settings are currently set to RETURN_NULL_AND_BLANK (blank and null
	 * cells will be viewed as written cells).
	 * 
	 * @param SheetIndex
	 *            Number of sheet (zero based).
	 * @param colNum
	 *            Column to which the row count will be calculated.
	 */
	public void setNextFreeRow(int SheetIndex, int colNum) {
		Sheet sheet = null;
		int rowCount = 0;

		try {
			sheet = wb.getSheetAt(SheetIndex);
			for (Row row : sheet) {
				Cell c = row.getCell(colNum, Row.RETURN_NULL_AND_BLANK);
				// blank cells will be counted
				if (c != null) {
					rowCount++;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.nextFreeRow = rowCount;
	}

	/**
	 * Determines the next free row of a given row, available to write out data.
	 * Settings are currently set to CREATE_NULL_AS_BLANK (blank cell is created
	 * for missing cell; each not null cell will be counted).
	 * 
	 * @param SheetIndex
	 *            Number of sheet (zero based).
	 * @param rowNum
	 *            Row to which the column count will be calculated.
	 */
	public void setNextFreeColumn(int SheetIndex, int rowNum) {

		Sheet sheet = null;
		int columnCount = 0;

		try {
			sheet = wb.getSheetAt(SheetIndex);

			Row r = sheet.getRow(rowNum);

			int lastColumn = Math.max(r.getLastCellNum(),
					MY_MINIMUM_COLUMN_COUNT);

			for (int cn = 0; cn < lastColumn; cn++) {
				Cell c = r.getCell(cn, Row.CREATE_NULL_AS_BLANK);
				if (c != null) {
					columnCount++;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.nextFreeColumn = columnCount; // Number of used cells, representing
											// the next free
		// cell for writing (zero based)
	}

	/**
	 * Method to find a row number of a given string which is being searched
	 * for.
	 * 
	 * @param sheet
	 *            Excel-Sheet.
	 * @param cellContent
	 *            String which will be searched for.
	 * @return Row number. Returns the row number of the first successful
	 *         comparison.
	 */
	public int findRow(int SheetIndex, String cellContent) {
		Sheet sheet = wb.getSheetAt(SheetIndex);

		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim()
							.equals(cellContent)) {
						return row.getRowNum();
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Determination of existing modules in column 1 of sheet 1. The algorithms
	 * demands the modules to be listed in a consecutive arrangement.
	 */
	public void findExistingModules() {

		Sheet sheet = wb.getSheetAt(1); // Sheet 1
		int lastRow = getNextFreeRow();
		int colNumber = 1;

		for (int rowIndex = 0; rowIndex < lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Cell cell = row.getCell(colNumber, Row.RETURN_BLANK_AS_NULL);
			if (cell != null) {
				this.existingModules.add(cell.getStringCellValue());
			}

		}
	}

	/**
	 * Modules which are not listed in the sheet but listed in one and not the
	 * other file will be added to the existing modules.
	 * 
	 * @param modulesChanged
	 *            The modules which have been tagged to contain differences in
	 *            key or value. Modules listed in one but not the other file
	 *            will also be treated as different.
	 * @param modules1
	 *            The module map of the first file containing key and value.
	 * @param modules2
	 *            The module map of the second file containing key and value.
	 * @return Returns true if complementary modules have been found and the
	 *         module list in the sheet needs to be extended.
	 */
	public boolean findComplementaryModules(Map<String, String> modulesChanged,
			Map<String, String> modules1, Map<String, String> modules2) {
		boolean complModulesFound = false;
		for (String entry : modulesChanged.keySet()) {
			if (!(existingModules.contains(entry))
					& ((modules1.containsKey(entry) & !(modules2
							.containsKey(entry))) | (!(modules1
							.containsKey(entry)) & modules2.containsKey(entry)))) {
				complementaryModules.add(entry);
				complModulesFound = true;
			}
		}
		return complModulesFound;
	}

	/**
	 * Extends the available list of modules in the sheet and sets the styles.
	 * 
	 * @throws IOException
	 *             Signals that no data could be written out.
	 */
	public void writeComplementaryModules() throws IOException {
		try {
			Sheet sheet = wb.getSheetAt(1);

			Row row;
			Cell cell;
			CellStyle style = getStyle(3);

			int j = nextFreeRow;
			for (int i = 0; i < complementaryModules.size(); i++) {
				row = sheet.createRow(j);
				cell = row.createCell(1);
				cell.setCellValue(complementaryModules.get(i));
				cell.setCellStyle(style);
				j++;
			}
			setNextFreeRow(1, 1);
			writeExcelWorkBook();
		} catch (IOException e) {
			throw new IOException(e);
		}

	}

	/**
	 * After the changes have been determined, the data is written out to the
	 * sheet. The top consisting of meta data will state basis and sonderstand.
	 * For each module that has experienced a change the (updated) version
	 * number is written out to the correct column and row, which will in turn
	 * be highlighted, indicating the change.
	 * 
	 * @param SheetIndex
	 *            Number of sheet (zero based).
	 * @param basis
	 *            The basis string.
	 * @param sonderstand
	 *            The sonderstand string.
	 * @param modulesChanged
	 *            All the modules which have experienced changes.
	 * @throws IOException
	 *             Signals that no data could be written out.
	 */
	public void writeData(int SheetIndex, String basis, String sonderstand,
			Map<String, String> modulesChanged) throws IOException {
		try {
			// declaration

			Sheet sheet = wb.getSheetAt(1);
			ArrayList<String> data = new ArrayList<String>();
			Row row;
			Cell cell;

			// prepare data
			data.add(basis);
			data.add(sonderstand);
			data.add("-");
			data.addAll(modulesChanged.keySet());

			// prepare styles
			CellStyle marked = getStyle(1);
			CellStyle head = getStyle(2);
			sheet.setDefaultColumnStyle(nextFreeColumn, getStyle(0));
			// write the data
			for (int i = 0; i < data.size(); i++) {
				if (i > 2) {
					String value = modulesChanged.get((data.get(i)));
					String key = data.get(i);
					int rowNum = findRow(1, key);
					if (rowNum >= 0) { // found (-1 = not found)
						row = sheet.getRow(rowNum);
						cell = row.createCell(nextFreeColumn);
						cell.setCellValue(value);
						cell.setCellStyle(marked);
					}
				} else {
					row = sheet.getRow(i);
					cell = row.createCell(nextFreeColumn);
					cell.setCellValue(data.get(i));
					cell.setCellStyle(head);
				}
			}
			sheet.autoSizeColumn(nextFreeColumn);
			nextFreeColumn += 1;
			writeExcelWorkBook();
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Returns a defined CellStyle type for a specific type of cell.
	 * 
	 * @param type
	 *            0:normal, 1: marked, 2: head
	 * @return A CellStyle defining font type, font height, bold(yes/no),
	 *         alignment and borders
	 */
	public CellStyle getStyle(int type) {
		Font font = wb.createFont();
		CellStyle style = wb.createCellStyle();

		switch (type) {
		case 0: // type:normal
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			style.setFont(font);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			break;

		case 1: // type:marked
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			font.setColor(IndexedColors.BLACK.getIndex());

			style.setFont(font);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			break;

		case 2: // type:head
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			font.setColor(IndexedColors.BLACK.getIndex());

			style.setFont(font);
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			break;
		case 3: // type: normal module column
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			font.setColor(IndexedColors.BLACK.getIndex());

			style.setFont(font);
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		}
		return style;
	}

}
