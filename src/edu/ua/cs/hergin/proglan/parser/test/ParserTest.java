/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.parser.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.parser.Parser;

public class ParserTest {

	@Test
	public void test() {
		Parser p = new Parser("testFiles/FullCode.mdln");
		p.parse();
	}

}
