/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.lexer.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.lexer.Lexer;

public class LexerTest {

	Lexer l1;
	Lexer l2;
	Lexer l3;
	Lexer l4;
	Lexer l5;
	Lexer l6;
	Lexer l7;

	@Before
	public void setUp() throws Exception {
		l1 = new Lexer("testFiles/lexerTest1.mdln");
		l2 = new Lexer("testFiles/lexerTest2.mdln");
		l3 = new Lexer("testFiles/lexerTest-functionCode.mdln");
		l4 = new Lexer("testFiles/lexerTest-classCode.mdln");
		l5 = new Lexer("testFiles/lexerTest-mainCode.mdln");
		l6 = new Lexer("testFiles/lexerTest-onlyComments.mdln");
		l7 = new Lexer("testFiles/lexerTest-mainCodeWithComments.mdln");
	}

	@Test
	public void testLexIntro() {
		assertEquals(LexemeTypes.ADD, l1.lex().getType());
		assertEquals(1234, l2.lex().getIntVal());
		assertEquals("ali", l2.lex().getStrVal());
		assertEquals(LexemeTypes.FUNCTION, l2.lex().getType());
	}
	
	@Test
	public void testNewCommentMode() {
		Lexer l = new Lexer("testFiles/lexerTest-newCommentMode");
		assertEquals(LexemeTypes.MAIN, l.lex().getType());
		assertEquals(LexemeTypes.OCURLY, l.lex().getType());
		Lexeme temp = l.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("a", temp.getStrVal());
		assertEquals(LexemeTypes.ASSIGN, l.lex().getType());
		temp = l.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("a", temp.getStrVal());
		assertEquals(LexemeTypes.ADD, l.lex().getType());
		temp = l.lex();
		assertEquals(LexemeTypes.INTEGER, temp.getType());
		assertEquals(1, temp.getIntVal());
		assertEquals(LexemeTypes.SEMICOLON, l.lex().getType());
		assertEquals(LexemeTypes.CCURLY, l.lex().getType());

	}

	@Test
	public void testFunctionCode() {
		assertEquals(LexemeTypes.FUNCTION, l3.lex().getType());
		Lexeme temp = l3.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("ali", temp.getStrVal());
		assertEquals(LexemeTypes.OPAREN, l3.lex().getType());
		assertEquals(LexemeTypes.CPAREN, l3.lex().getType());
		assertEquals(LexemeTypes.OCURLY, l3.lex().getType());
		Lexeme temp2 = l3.lex();
		assertEquals(LexemeTypes.ID, temp2.getType());
		assertEquals("a", temp2.getStrVal());
		assertEquals(LexemeTypes.ASSIGN, l3.lex().getType());
		Lexeme temp3 = l3.lex();
		assertEquals(LexemeTypes.INTEGER, temp3.getType());
		assertEquals(32, temp3.getIntVal());
		assertEquals(LexemeTypes.SEMICOLON, l3.lex().getType());
		assertEquals(LexemeTypes.CCURLY, l3.lex().getType());

	}

	@Test
	public void testClassCode() {
		assertEquals(LexemeTypes.CLASS, l4.lex().getType());
		Lexeme temp = l4.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("Albert", temp.getStrVal());
		assertEquals(LexemeTypes.EXTENDS, l4.lex().getType());
		temp = l4.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("Einstein", temp.getStrVal());
		assertEquals(LexemeTypes.OCURLY, l4.lex().getType());
		assertEquals(LexemeTypes.FUNCTION, l4.lex().getType());
		temp = l4.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("goto", temp.getStrVal());
		assertEquals(LexemeTypes.OPAREN, l4.lex().getType());
		temp = l4.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("a", temp.getStrVal());
		assertEquals(LexemeTypes.COMMA, l4.lex().getType());
		temp = l4.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("b", temp.getStrVal());
		assertEquals(LexemeTypes.CPAREN, l4.lex().getType());
		assertEquals(LexemeTypes.SEMICOLON, l4.lex().getType());
		assertEquals(LexemeTypes.CCURLY, l4.lex().getType());
	}

	@Test
	public void testMainCode() {
		assertEquals(LexemeTypes.MAIN, l5.lex().getType());
		assertEquals(LexemeTypes.OCURLY, l5.lex().getType());
		Lexeme temp = l5.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("a", temp.getStrVal());
		assertEquals(LexemeTypes.ASSIGN, l5.lex().getType());
		temp = l5.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("a", temp.getStrVal());
		assertEquals(LexemeTypes.ADD, l5.lex().getType());
		temp = l5.lex();
		assertEquals(LexemeTypes.INTEGER, temp.getType());
		assertEquals(1, temp.getIntVal());
		assertEquals(LexemeTypes.SEMICOLON, l5.lex().getType());
		temp = l5.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("b", temp.getStrVal());
		assertEquals(LexemeTypes.ASSIGN, l5.lex().getType());
		temp = l5.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("b", temp.getStrVal());
		assertEquals(LexemeTypes.SUBTRACT, l5.lex().getType());
		temp = l5.lex();
		assertEquals(LexemeTypes.INTEGER, temp.getType());
		assertEquals(2, temp.getIntVal());
		assertEquals(LexemeTypes.SEMICOLON, l5.lex().getType());
		assertEquals(LexemeTypes.CCURLY, l5.lex().getType());
	}

	@Test
	public void testComments() {
		assertEquals(LexemeTypes.MAIN, l6.lex().getType());
	}
	
	@Test
	public void testMainCodeWithComments() {
		assertEquals(LexemeTypes.MAIN, l7.lex().getType());
		assertEquals(LexemeTypes.OCURLY, l7.lex().getType());
		Lexeme temp = l7.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("a", temp.getStrVal());
		assertEquals(LexemeTypes.ASSIGN, l7.lex().getType());
		temp = l7.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("a", temp.getStrVal());
		assertEquals(LexemeTypes.ADD, l7.lex().getType());
		temp = l7.lex();
		assertEquals(LexemeTypes.INTEGER, temp.getType());
		assertEquals(1, temp.getIntVal());
		assertEquals(LexemeTypes.SEMICOLON, l7.lex().getType());
		temp = l7.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("b", temp.getStrVal());
		assertEquals(LexemeTypes.ASSIGN, l7.lex().getType());
		temp = l7.lex();
		assertEquals(LexemeTypes.ID, temp.getType());
		assertEquals("b", temp.getStrVal());
		assertEquals(LexemeTypes.SUBTRACT, l7.lex().getType());
		temp = l7.lex();
		assertEquals(LexemeTypes.INTEGER, temp.getType());
		assertEquals(2, temp.getIntVal());
		assertEquals(LexemeTypes.SEMICOLON, l7.lex().getType());
		assertEquals(LexemeTypes.CCURLY, l7.lex().getType());
	}

}
