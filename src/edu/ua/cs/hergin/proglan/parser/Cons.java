/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.parser;

import edu.ua.cs.hergin.proglan.environment.Environment;
import edu.ua.cs.hergin.proglan.lexer.Lexeme;

public class Cons
{

	String type;

	Cons left;

	Cons right;

	Lexeme value;

	Environment definitionEnvironment;
	
	Environment selfEnvironment;

	public interface ConsTypes
	{
		public static final String EXPRESSION = "CONS_EXPRESSION";
		public static final String JOIN = "CONS_JOIN";
		public static final String PRIMARY = "CONS_PRIMARY";
		public static final String PROGRAM = "CONS_PROGRAM";
		public static final String STRUCTLIST = "CONS_STRUCTLIST";
		public static final String CLASSDECLARATION = "CONS_CLASSDECLARATION";
		public static final String EXTENDS = "CONS_EXTENDS";
		public static final String DECLARATIONBLOCK = "CONS_DECLARATIONBLOCK";
		public static final String FUNCTIONDECLARATION = "CONS_FUNCTIONDECLARATION";
		public static final String STATEMENTLIST = "CONS_STATEMENTLIST";
		public static final String IFSTATEMENT = "CONS_IFSTATEMENT";
		public static final String REGULARBLOCK = "CONS_REGULARBLOCK";
		public static final String ELSE = "CONS_ELSE";
		public static final String FORSTATEMENT = "CONS_FORSTATEMENT";
		public static final String REGULARSTATEMENT = "CONS_REGULARSTATEMENT";
		public static final String VARIABLEDECLARATION = "CONS_VARIABLEDECLARATION";
		public static final String ATTRIBUTELIST = "CONS_ATTRIBUTELIST";
		public static final String ATTRIBUTE = "CONS_ATTRIBUTE";
		public static final String FUNCTIONCALL = "CONS_FUNCTIONCALL";
		public static final String ARGUMENTLIST = "CONS_ARGUMENTLIST";
		public static final String WHILESTATEMENT = "CONS_WHILESTATEMENT";
		public static final String PARAMETERLIST = "CONS_PARAMLIST";
		public static final String MAIN = "CONS_MAIN";
		public static final String PROTOTYPE = "CONS_PROTOTYPE";
		public static final String DUMMY = "CONS_DUMMY";
		public static final String VARIABLEASSIGNMENT = "VARIABLEASSIGNMENT";
	}

	public Cons()
	{
		type = ConsTypes.JOIN;
		left = null;
		right = null;
		value = null;
	}

	public Cons(String t)
	{
		type = t;
		left = null;
		right = null;
		value = null;
	}

	public Cons(String t, Lexeme l)
	{
		type = t;
		left = null;
		right = null;
		value = l;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CONS( type=" + type);
		if (value != null)
			sb.append(", value=" + value.getPureVal());
		sb.append(" )");
		return sb.toString();
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Cons getLeft()
	{
		return left;
	}

	public void setLeft(Cons left)
	{
		this.left = left;
	}

	public Cons getRight()
	{
		return right;
	}

	public void setRight(Cons right)
	{
		this.right = right;
	}

	public Lexeme getValue()
	{
		return value;
	}

	public void setValue(Lexeme value)
	{
		this.value = value;
	}

	public Environment getDefinitionEnvironment()
	{
		return definitionEnvironment;
	}

	public void setDefinitionEnvironment(Environment definitionEnvironment)
	{
		this.definitionEnvironment = definitionEnvironment;
	}

	public Environment getSelfEnvironment()
	{
		return selfEnvironment;
	}

	public void setSelfEnvironment(Environment selfEnvironment)
	{
		this.selfEnvironment = selfEnvironment;
	}

}
