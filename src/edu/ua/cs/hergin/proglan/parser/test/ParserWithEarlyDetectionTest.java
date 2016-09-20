package edu.ua.cs.hergin.proglan.parser.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.parser.Parser;
import edu.ua.cs.hergin.proglan.parser.ParserWithEarlyDetection;
import edu.ua.cs.hergin.proglan.prettyprinter.PrettyPrinter;

public class ParserWithEarlyDetectionTest {

	@Test
	public void test() {
		List<String> l = new ArrayList<String>();
		l.add("display");
		l.add("print");
		ParserWithEarlyDetection p = new ParserWithEarlyDetection(
				"testFiles/FullCode2.mdln", l);
		PrettyPrinter pp = new PrettyPrinter(p.parse());
		pp.print();
	}

}
