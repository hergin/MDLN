package edu.ua.cs.hergin.proglan.environment;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;

public class StringLexemePair {

	String name;
	Lexeme value;

	public StringLexemePair(String n, Lexeme l) {
		name = n;
		value = l;
	}

	public StringLexemePair(String n, String v) {
		name = n;
		value = new Lexeme(LexemeTypes.STRING, v, 0);
	}
	
	public StringLexemePair(String n, int v) {
		name = n;
		value = new Lexeme(LexemeTypes.INTEGER, v, 0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Lexeme getValue() {
		return value;
	}

	public void setValue(Lexeme value) {
		this.value = value;
	}

}
