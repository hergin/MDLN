/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class Lexer {

	PushbackReader input = null;
	int lineNo = 1;

	public Lexer(String fileName) {
		try {

			input = new PushbackReader(new FileReader(fileName));

		} catch (FileNotFoundException e) {
			System.err.println("File: " + fileName
					+ " not found in Lexer constructor!");
			e.printStackTrace();
		}
	}

	/*
	 * classic lex function
	 */
	public Lexeme lex() {

		int ch;

		try {
			skipWhiteSpace();

			ch = input.read();

			if (ch == -1)
				return new Lexeme(LexemeTypes.ENDofINPUT, lineNo);

			switch (ch) {
			case ';':
				return new Lexeme(LexemeTypes.SEMICOLON, lineNo);
			case '(':
				return new Lexeme(LexemeTypes.OPAREN, lineNo);
			case ')':
				return new Lexeme(LexemeTypes.CPAREN, lineNo);
			case '{':
				return new Lexeme(LexemeTypes.OCURLY, lineNo);
			case '}':
				return new Lexeme(LexemeTypes.CCURLY, lineNo);
			case ',':
				return new Lexeme(LexemeTypes.COMMA, lineNo);
			case '=':
				return new Lexeme(LexemeTypes.ASSIGN, lineNo);
			case '.':
				return new Lexeme(LexemeTypes.DOT, lineNo);
			case '+':
				return new Lexeme(LexemeTypes.ADD, lineNo);
			case '-':
				return new Lexeme(LexemeTypes.SUBTRACT, lineNo);
			case '*':
				return new Lexeme(LexemeTypes.MULTIPLY, lineNo);
			case '/':
				return new Lexeme(LexemeTypes.DIVIDE, lineNo);
			case '>':
				return new Lexeme(LexemeTypes.GREATERTHAN, lineNo);
			case '<':
				return new Lexeme(LexemeTypes.LESSTHAN, lineNo);
			default:

				if (Character.isDigit(ch)) {
					input.unread(ch);
					return lexNumber();
				} else if (ch == '\"') {
					return lexString();
				} else if (Character.isLetter(ch)) {
					input.unread(ch);
					return lexIdOrKeyword();
				} else {
					return new Lexeme(LexemeTypes.UNKNOWN, ch, lineNo);
				}
			}

		} catch (IOException e) {
			System.err.println("Cannot read from file!");
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * Skips whitespaces also everything after ' to newline
	 */
	public void skipWhiteSpace() throws IOException {
		int ch, ch2;
		boolean commentMode = false;
		do {
			ch = input.read();
			if (ch == '\n')
				lineNo++;
			if (!commentMode && ch == '\'') {
				commentMode = true;
			}
			if (commentMode) {
				if (ch == '\n')
					commentMode = false;
			}
		} while (Character.isWhitespace(ch) || commentMode);
		if (ch != -1) // a lack of pushbackreader, when we pushback EOF, it
						// pushes back 65535, so it needs to be passed
			input.unread(ch);
	}

	/*
	 * Lex number read digits one by one and everytime multiply with 10 and add
	 * the next digit
	 */
	public Lexeme lexNumber() throws IOException {
		int num = 0;
		int ch = input.read();

		while (Character.isDigit(ch)) {
			num = num * 10 + Character.digit(ch, 10);
			ch = input.read();
		}

		input.unread(ch);

		return new Lexeme(LexemeTypes.INTEGER, num, lineNo);
	}

	/*
	 * Lex string read until next double quotes
	 */
	public Lexeme lexString() throws IOException {

		StringBuilder str = new StringBuilder();

		int ch = input.read();

		while (ch != '\"') {
			str.append((char) ch);
			ch = input.read();
		}

		return new Lexeme(LexemeTypes.STRING, str.toString(), lineNo);
	}

	/*
	 * lex the next word
	 */
	public Lexeme lexIdOrKeyword() throws IOException {

		StringBuilder str = new StringBuilder();

		int ch = input.read();

		while (Character.isDigit(ch) || Character.isLetter(ch)) {
			str.append((char) ch);
			ch = input.read();
		}

		input.unread(ch);

		return createLexemeIdOrKeyword(str.toString());
	}

	/*
	 * distinguish between keywords and ID
	 */
	public Lexeme createLexemeIdOrKeyword(String s) {
		if (s.equals("class")) {
			return new Lexeme(LexemeTypes.CLASS, lineNo);
		} else if (s.equals("extends")) {
			return new Lexeme(LexemeTypes.EXTENDS, lineNo);
		} else if (s.equals("function")) {
			return new Lexeme(LexemeTypes.FUNCTION, lineNo);
		} else if (s.equals("return")) {
			return new Lexeme(LexemeTypes.RETURN, lineNo);
		} else if (s.equals("if")) {
			return new Lexeme(LexemeTypes.IF, lineNo);
		} else if (s.equals("else")) {
			return new Lexeme(LexemeTypes.ELSE, lineNo);
		} else if (s.equals("for")) {
			return new Lexeme(LexemeTypes.FOR, lineNo);
		} else if (s.equals("while")) {
			return new Lexeme(LexemeTypes.WHILE, lineNo);
		} else if (s.equals("main")) {
			return new Lexeme(LexemeTypes.MAIN, lineNo);
		} else if (s.equals("call")) {
			return new Lexeme(LexemeTypes.CALL, lineNo);
		} else if (s.equals("var")) {
			return new Lexeme(LexemeTypes.VAR, lineNo);
		} else if (s.equals("forward")) {
			return new Lexeme(LexemeTypes.FORWARD, lineNo);
		} else if (s.equals("set")) {
			return new Lexeme(LexemeTypes.SET, lineNo);
		} else {
			return new Lexeme(LexemeTypes.ID, s, lineNo);
		}
	}

}
