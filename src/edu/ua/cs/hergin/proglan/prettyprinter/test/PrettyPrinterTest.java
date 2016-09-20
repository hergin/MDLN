/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.prettyprinter.test;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.parser.Parser;
import edu.ua.cs.hergin.proglan.prettyprinter.PrettyPrinter;

public class PrettyPrinterTest {

	@Test
	public void test() {
		Parser p = new Parser("testFiles/FullCode.mdln");
		PrettyPrinter pp = new PrettyPrinter(p.parse());
		pp.print();
	}

}
