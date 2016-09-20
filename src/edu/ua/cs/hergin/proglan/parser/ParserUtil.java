/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.parser;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.lexer.Lexer;
import edu.ua.cs.hergin.proglan.recognizer.EndOfInputException;
import edu.ua.cs.hergin.proglan.recognizer.RecognizerException;

public class ParserUtil {

	Lexer lexer;
	Lexeme currentLexeme;

	public ParserUtil(String fileName) throws EndOfInputException {
		lexer = new Lexer(fileName);
		advance();
	}

	public boolean check(String type) {
		return currentLexeme.getType().equals(type);
	}

	public void advance() throws EndOfInputException {
		currentLexeme = lexer.lex();
		if (currentLexeme.getType().equals(LexemeTypes.ENDofINPUT))
			throw new EndOfInputException();
	}

	public Cons match(String type) throws RecognizerException {
		matchNoAdvance(type);
		Cons cons = new Cons(type, currentLexeme);
		try {
			advance();
		} catch (EndOfInputException e) {
			//System.out.println("EoF!");
		}
		return cons;
	}

	public void matchNoAdvance(String type) throws RecognizerException {
		if (!check(type))
			throw new RecognizerException(currentLexeme.getLineNo(), type,
					currentLexeme.getType());
	}

}
