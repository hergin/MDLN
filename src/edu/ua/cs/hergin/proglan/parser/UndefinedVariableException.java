package edu.ua.cs.hergin.proglan.parser;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;

public class UndefinedVariableException extends Exception
{

	Lexeme var;

	public UndefinedVariableException(Lexeme varName)
	{
		super();
		var = varName;
	}

	public void print()
	{
		System.out.println("UNDEFINED_VARIABLE:\n    Line: " + var.getLineNo() + "\n    VarName: " + var.getPureVal());
	}

}
