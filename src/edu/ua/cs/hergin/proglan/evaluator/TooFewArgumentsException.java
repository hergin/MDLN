package edu.ua.cs.hergin.proglan.evaluator;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;

public class TooFewArgumentsException extends Exception
{

	Lexeme call;
	String fname;

	public TooFewArgumentsException(Lexeme op, String n)
	{
		super();
		call = op;
		fname = n;
	}

	public void print()
	{
		System.out.println("TOO_FEW_ARGUMENTS:\n    Line: " + call.getLineNo() + "\n    FunctionName: " + fname);
	}
}
