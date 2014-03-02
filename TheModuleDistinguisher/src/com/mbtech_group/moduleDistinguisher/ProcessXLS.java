package com.mbtech_group.moduleDistinguisher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.record.CFRuleRecord.ComparisonOperator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public ProcessXLS(String filePath) {
		this.filepath = filePath;
		readExcelWorkbook();
	}

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

	public void writeExcelWorkBook() throws IOException {
		try {
			out = new FileOutputStream(this.filepath);
			this.wb.write(out);
			closeOutputStream();
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

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

	public CellStyle setColumnStyle() {
		Sheet sheet = wb.getSheetAt(1);

		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);

		CellStyle colStyle = wb.createCellStyle();
		colStyle.setFont(font);
		colStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		colStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		colStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		sheet.setDefaultColumnStyle(nextFreeColumn, colStyle);

		return colStyle;
	}

	public CellStyle getCellStyle(CellStyle colStyle) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.cloneStyleFrom(colStyle);

		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(IndexedColors.BLACK.getIndex());

		cellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setFont(font);
		return cellStyle;
	}
}