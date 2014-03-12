package com.mbtech_group.moduleDistinguisher;

import java.io.IOException;

/**
 * 
 * @author Fabio
 *
 */
public class TheModuleDistinguisherController {

	private static String basisPath;
	private static String sonderstandPath;
	private static String xlsPath;
	private String log = null;

	// public static void main(String[] args) {
	// // for testing
	// DiffModuleChanges diff = new DiffModuleChanges(basisPath,
	// sonderstandPath);
	// ProcessXLS xls = new ProcessXLS(xlsPath);
	//
	// String basis = diff.getBasis();
	// String sonderstand = diff.getSonderstand();
	//
	// System.out.println(diff.getModulesChanged());
	// System.out.println(xls.getExistingModules());
	//
	// if (xls.findComplementaryModules(diff.getModulesChanged(),
	// diff.getModules(diff.getFile1()),
	// diff.getModules(diff.getFile2()))) {
	// System.out.println(xls.getComplementaryModules());
	// xls.writeComplementaryModules();
	// }
	//
	// xls.writeData(1, basis, sonderstand, diff.getModulesChanged());
	//
	// System.out.println("\nnext free column: " + xls.getNextFreeColumn());
	// System.out.println("next free row: " + xls.getNextFreeRow());
	//
	// }

	public TheModuleDistinguisherController(String bPath,
			String sPath, String xPath) throws IOException {

		basisPath = bPath;
		sonderstandPath = sPath;
		xlsPath = xPath;

		DiffModuleChanges diff = new DiffModuleChanges(bPath,
				sPath);
		ProcessXLS xls = new ProcessXLS(xPath);

		try {
			log = "Module parameters\nModules with changes: "
					+ diff.getModulesChanged() + "\nExisting modules: "
					+ xls.getExistingModules();

			if (xls.findComplementaryModules(diff.getModulesChanged(),
					diff.getModules(diff.getFile1()),
					diff.getModules(diff.getFile2()))) {
				xls.writeComplementaryModules(); // throws exception
			}

			log += "\n" + "Complementary modules: "
					+ xls.getComplementaryModules();

			String basis = diff.getBasis();
			String sonderstand = diff.getSonderstand();
			xls.writeData(1, basis, sonderstand, diff.getModulesChanged()); // throws
																			// exception
			log += "\n\nExcel sheet parameters\nSheet written: 1\nNext free column: "
					+ xls.getNextFreeColumn()
					+ "\nNext free row: "
					+ xls.getNextFreeRow() + "\n\n";
		} catch (IOException e) {
			throw new IOException(e);
		}

	}

	public String getLog() {
		return this.log;
	}
}
