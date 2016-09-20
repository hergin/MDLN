package edu.ua.cs.hergin.proglan.parser;

public class UndeclaredPrototypeException extends Exception
{

	String var;

	public UndeclaredPrototypeException(String varName)
	{
		super();
		var = varName;
	}

	public void print()
	{
		System.out.println("UNDECLARED_PROTOTYPE:\n    Prototype: " + var);
	}

}
