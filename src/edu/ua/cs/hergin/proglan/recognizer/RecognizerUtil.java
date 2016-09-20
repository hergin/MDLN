/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.recognizer;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.lexer.Lexer;

public class RecognizerUtil {

	Lexer lexer;
	Lexeme currentLexeme;

	public RecognizerUtil(String fileName) throws EndOfInputException {
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

	public void match(String type) throws RecognizerException,
			EndOfInputException {
		matchNoAdvance(type);
		advance();
	}

	public void matchNoAdvance(String type) throws RecognizerException {
		if (!check(type))
			throw new RecognizerException(currentLexeme.getLineNo(), type,
					currentLexeme.getType());
	}

}
