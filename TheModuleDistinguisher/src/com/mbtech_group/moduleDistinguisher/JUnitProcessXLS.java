package com.mbtech_group.moduleDistinguisher;

import static org.junit.Assert.*;

import org.junit.Test;



public class JUnitProcessXLS {

	@Test
	public void test() {
		ProcessXLS xls = new ProcessXLS("C:\\ferrefa\\Werkstudent_3250\\trunk\\2_Arbeitspakete\\2002\\Daten\\Versionsaenderungen_Teststaende.xls");

		//int y = xls.getNextFreeColumn(1, 0);
		//System.out.println(y);
		
		int z = xls.findRow(1, "SCR_DPDVSS");
		System.out.println(z);
		
		//int x = xls.getNumberRows(1);
		//System.out.println(x);
	}
}
