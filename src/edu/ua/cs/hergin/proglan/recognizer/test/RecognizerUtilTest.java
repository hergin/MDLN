package edu.ua.cs.hergin.proglan.recognizer.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.recognizer.EndOfInputException;
import edu.ua.cs.hergin.proglan.recognizer.RecognizerException;
import edu.ua.cs.hergin.proglan.recognizer.RecognizerUtil;

public class RecognizerUtilTest {

	RecognizerUtil util, util2;
	
	@Before
	public void setUp() throws Exception {
		util = new RecognizerUtil("testFiles/recognizerTest1.mdln");
		util2 = new RecognizerUtil("testFiles/recognizerTest1.mdln");
	}

	@Test
	public void checkAdvanceTests() throws EndOfInputException {
		assertTrue(util.check(LexemeTypes.CLASS));
		util.advance();
		assertTrue(util.check(LexemeTypes.ID));
		util.advance();
		assertTrue(util.check(LexemeTypes.OCURLY));
		util.advance();
		assertTrue(util.check(LexemeTypes.CCURLY));
		try {
			util.advance();
		} catch (EndOfInputException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void matchTests() throws RecognizerException, EndOfInputException {
		util2.match(LexemeTypes.CLASS);
		try {
			util2.match(LexemeTypes.ASSIGN);
		} catch (RecognizerException e) {
			assertTrue(true);
			System.out.print(e.getError());
		}
	}

}
