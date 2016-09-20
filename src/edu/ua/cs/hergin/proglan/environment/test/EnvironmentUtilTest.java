package edu.ua.cs.hergin.proglan.environment.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.environment.EnvironmentUtil;
import edu.ua.cs.hergin.proglan.environment.StringLexemePair;

public class EnvironmentUtilTest {

	@Test
	public void test() {
		EnvironmentUtil util = new EnvironmentUtil();
		util.putVar("x", "ali");
		util.putVar("z", 2);
		util.createNewEnv();
		util.putVar("y", "veli");
		util.putVar("x", "deli");
		util.createNewEnv();
		util.putVar("y", "salim");
		
		//util.deleteMostLocal();
		//util.deleteMostLocal();
		util.extend("Global", new StringLexemePair("w","deneme"));
		util.setVar("x", "newX");
		util.setVar("y", "newY");
		util.setVar("z", 3);
		util.setVar("t", "newT");
		util.extend("E1", new StringLexemePair("w","goon"));
		util.setVar("t", "newT");
		//util.deleteMostLocal();
		util.setVar("w", "newW");
		util.displayAll();
		//util.getEnv("E1_extended").display();
	}

}
