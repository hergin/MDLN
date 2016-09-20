/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.environment;

import java.util.HashMap;
import java.util.Map;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.parser.Cons;
import edu.ua.cs.hergin.proglan.parser.UndeclaredPrototypeException;
import edu.ua.cs.hergin.proglan.parser.UndefinedVariableException;

public class Environment
{

	String name;
	Map<String, Lexeme> variables;
	Environment prevEnv;

	public Environment(String n)
	{
		name = new String(n);
		variables = new HashMap<String, Lexeme>();
		prevEnv = null;
	}
	
	public Lexeme setVar(String name, Lexeme l)
	{
		Environment temp;
		for (temp = this; temp != null; temp = temp.getPrevEnv())
		{
			if (temp.get(name) != null)
			{
				temp.add(name, l);
				break;
			}
		}
		return l;
	}
	
	public void undeclaredPrototypes() throws UndeclaredPrototypeException
	{
		for (Map.Entry<String, Lexeme> entry : this.getVariables().entrySet())
		{
			if (entry.getValue().getType().equals(LexemeTypes.PROTOTYPE))
			{
				throw new UndeclaredPrototypeException(entry.getKey());
			}
		}
	}

	public static Environment extend(Environment original, StringLexemePair... p)
	{
		Environment newEnv = new Environment(original.getName() + "_extended");
		for (StringLexemePair s : p)
		{
			newEnv.add(s.getName(), s.getValue());
		}
		newEnv.setPrevEnv(original);
		return newEnv;
	}

	public Lexeme add(String n, Lexeme l)
	{
		variables.put(n, l);
		return l;
	}

	public Lexeme add(String n, Lexeme l, Cons c)
	{
		l.setCons(c);
		variables.put(n, l);
		return l;
	}

	public Lexeme add(String n, String v)
	{
		Lexeme l = new Lexeme(LexemeTypes.STRING, v, 0);
		variables.put(n, l);
		return l;
	}

	public Lexeme add(String n, int v)
	{
		Lexeme l = new Lexeme(LexemeTypes.STRING, v, 0);
		variables.put(n, l);
		return l;
	}

	public Lexeme get(String n)
	{
		return variables.get(n);
	}

	public Lexeme getCascading(Lexeme name) throws UndefinedVariableException
	{
		Environment temp;
		for (temp = this; temp != null; temp = temp.getPrevEnv())
		{
			if (temp.get(name.getStrVal()) != null)
			{
				return temp.get(name.getStrVal());
			}
		}
		throw new UndefinedVariableException(name);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Environment: " + name + "\n");
		sb.append("========================\n");

		for (Map.Entry<String, Lexeme> entry : variables.entrySet())
		{
			sb.append(entry.getKey() + ": " + entry.getValue().getPureVal() + "\n");
		}

		sb.append("========================");

		return sb.toString();
	}

	public void display()
	{
		System.out.println(toString());
	}

	public Environment getPrevEnv()
	{
		return prevEnv;
	}

	public void setPrevEnv(Environment prevEnv)
	{
		this.prevEnv = prevEnv;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Map<String, Lexeme> getVariables()
	{
		return variables;
	}

	public void setVariables(Map<String, Lexeme> variables)
	{
		this.variables = variables;
	}

}
