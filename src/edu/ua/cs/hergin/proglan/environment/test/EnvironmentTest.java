/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.environment.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.environment.Environment;
import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;

public class EnvironmentTest {

	@Test
	public void test() {
		Environment env1 = new Environment("E0");
		env1.add("x", "ali");
		System.out.println(env1.toString());
		assertEquals(env1.get("x").getStrVal(), "ali");
		assertEquals(env1.get("y"), null);
	}

}
