/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.lexer;

import java.util.List;

import edu.ua.cs.hergin.proglan.environment.Environment;
import edu.ua.cs.hergin.proglan.parser.Cons;

public class Lexeme extends LexemeTypes
{

	String type;
	String strVal;
	int lineNo;
	int intVal;
	Cons cons;
	List<Lexeme> arrVal;

	public Lexeme(String t, int l)
	{
		type = t;
		lineNo = l;
		cons = null;
	}

	public Lexeme(String t, String val, int l)
	{
		type = t;
		strVal = val;
		lineNo = l;
		cons = null;
	}

	public Lexeme(String t, int val, int l)
	{
		type = t;
		intVal = val;
		lineNo = l;
		cons = null;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getStrVal()
	{
		return strVal;
	}

	public void setStrVal(String strVal)
	{
		this.strVal = strVal;
	}

	public int getIntVal()
	{
		return intVal;
	}

	public void setIntVal(int intVal)
	{
		this.intVal = intVal;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Line: " + lineNo + " -> " + type);
		if (type.equals(ID))
			sb.append(" " + strVal);
		else if (type.equals(STRING))
			sb.append(" \"" + strVal + "\"");
		else if (type.equals(INTEGER))
			sb.append(" " + intVal);
		return sb.toString();
	}

	public String getPureVal()
	{
		StringBuilder sb = new StringBuilder();
		if (type.equals(STRING))
			sb.append("" + strVal);
		else if (type.equals(INTEGER))
			sb.append("" + intVal);
		else if (type.equals(ID))
			sb.append("" + strVal);
		else
			sb.append(type);
		return sb.toString();
	}
	
	public String getPureVal2()
	{
		StringBuilder sb = new StringBuilder();
		if (type.equals(STRING))
			sb.append("\"" + strVal+"\"");
		else if (type.equals(INTEGER))
			sb.append("" + intVal);
		else if (type.equals(ID))
			sb.append("" + strVal);
		else
			sb.append(type);
		return sb.toString();
	}

	public int getLineNo()
	{
		return lineNo;
	}

	public void setLineNo(int lineNo)
	{
		this.lineNo = lineNo;
	}

	public Cons getCons()
	{
		return cons;
	}

	public void setCons(Cons cons)
	{
		this.cons = cons;
	}

	public List<Lexeme> getArrVal()
	{
		return arrVal;
	}

	public void setArrVal(List<Lexeme> arrVal)
	{
		this.arrVal = arrVal;
	}

}