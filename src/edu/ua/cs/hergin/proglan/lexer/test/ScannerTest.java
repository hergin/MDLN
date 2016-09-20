package edu.ua.cs.hergin.proglan.lexer.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.lexer.Scanner;

public class ScannerTest {

	@Test
	public void test() {
		new Scanner("testFiles/lexerTest-newCommentMode").scanner();
	}

}
