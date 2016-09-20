package edu.ua.cs.hergin.proglan.evaluator;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;

public class UnsupportedOperatorException extends Exception {

	Lexeme opr;

	String typ1;

	String typ2;

	public UnsupportedOperatorException(Lexeme op, String t1, String t2) {
		super();
		opr = op;
		typ1 = t1;
		typ2 = t2;
	}

	public void print() {
		System.out.println("UNSUPPORTED_OPERATOR:\n    Line: " + opr.getLineNo()
				+ "\n    Operator: " + opr.getType() + "\n    Types: "
				+ typ1.toUpperCase() + " and " + typ2.toUpperCase());
	}
}
