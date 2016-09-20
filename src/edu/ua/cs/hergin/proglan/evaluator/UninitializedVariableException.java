package edu.ua.cs.hergin.proglan.evaluator;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;

public class UninitializedVariableException extends Exception
{

	Lexeme var;

	public UninitializedVariableException(Lexeme v)
	{
		super();
		var = v;
	}

	public void print()
	{
		System.out.println("UNINITIALIZED_VARIABLE:\n    Line: " + var.getLineNo() + "\n    Variable: " + var.getPureVal());
	}

	public Lexeme getVar()
	{
		return var;
	}
}
