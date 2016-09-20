/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.environment;

import java.util.Map;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.parser.Cons;
import edu.ua.cs.hergin.proglan.parser.UndeclaredPrototypeException;
import edu.ua.cs.hergin.proglan.parser.UndefinedVariableException;

public class EnvironmentUtil
{

	Environment global;
	Environment mostLocal;
	int eCtr = 1;

	public EnvironmentUtil()
	{
		global = new Environment("Global");
		mostLocal = global;
	}

	public Environment createNewEnv()
	{
		Environment temp = mostLocal;
		mostLocal = new Environment("E" + eCtr++);
		mostLocal.setPrevEnv(temp);
		return mostLocal;
	}

	public Environment createNewEnv(String name)
	{
		Environment temp = mostLocal;
		mostLocal = new Environment(name);
		mostLocal.setPrevEnv(temp);
		return mostLocal;
	}

	public Environment getEnv(String name)
	{
		Environment temp;
		for (temp = mostLocal; temp != null; temp = temp.getPrevEnv())
		{
			if (temp.getName().equals(name))
			{
				return temp;
			}
		}
		return null;
	}

	public Lexeme getVar(Lexeme name) throws UndefinedVariableException
	{
		Environment temp;
		for (temp = mostLocal; temp != null; temp = temp.getPrevEnv())
		{
			if (temp.get(name.getStrVal()) != null)
			{
				return temp.get(name.getStrVal());
			}
		}
		throw new UndefinedVariableException(name);
	}

	public Lexeme getVar(String name) throws UndefinedVariableException
	{
		Environment temp;
		for (temp = mostLocal; temp != null; temp = temp.getPrevEnv())
		{
			if (temp.get(name) != null)
			{
				return temp.get(name);
			}
		}
		throw new UndefinedVariableException(new Lexeme(name, 0));
	}

	public Environment extend(String envName, StringLexemePair... pList)
	{
		Environment temp, newEnv;
		if (global == mostLocal)
		{
			newEnv = Environment.extend(global, pList);
			mostLocal = newEnv;
			return newEnv;
		}
		for (temp = mostLocal; temp != null; temp = temp.getPrevEnv())
		{
			if (temp.getPrevEnv() != null && temp.getPrevEnv().getName().equals(envName))
			{
				newEnv = Environment.extend(temp.getPrevEnv(), pList);
				temp.setPrevEnv(newEnv);
				return newEnv;
			}
		}
		return null;
	}

	public Lexeme putVar(String name, Lexeme l)
	{
		mostLocal.add(name, l);
		return l;
	}

	public Lexeme putVar(String name, String v)
	{
		return putVar(name, new Lexeme(LexemeTypes.STRING, v, 0));
	}

	public Lexeme putVar(String name, int v)
	{
		return putVar(name, new Lexeme(LexemeTypes.INTEGER, v, 0));
	}

	public Lexeme putVar(String name, Lexeme v, Cons c)
	{
		v.setCons(c);
		return putVar(name, v);
	}

	public Lexeme setVar(String name, Lexeme l)
	{
		Environment temp;
		for (temp = mostLocal; temp != null; temp = temp.getPrevEnv())
		{
			if (temp.get(name) != null)
			{
				temp.add(name, l);
				break;
			}
		}
		return l;
	}

	public Lexeme setVar(String name, String v)
	{
		return setVar(name, new Lexeme(LexemeTypes.STRING, v, 0));
	}

	public Lexeme setVar(String name, int v)
	{
		return setVar(name, new Lexeme(LexemeTypes.INTEGER, v, 0));
	}

	public Environment deleteMostLocal() throws UndeclaredPrototypeException
	{
		if (mostLocal != global)
		{
			Environment temp = mostLocal;

			for (Map.Entry<String, Lexeme> entry : mostLocal.getVariables().entrySet())
			{
				if (entry.getValue().getType().equals(LexemeTypes.PROTOTYPE))
				{
					throw new UndeclaredPrototypeException(entry.getKey());
				}
			}

			mostLocal = mostLocal.getPrevEnv();
			temp.setPrevEnv(null);
			eCtr--;
			return temp;
		}
		else
		{
			System.out.println("You can't delete Global Env!");
			return null;
		}
	}

	public void undeclaredPrototypesInGlobal() throws UndeclaredPrototypeException
	{
		for (Map.Entry<String, Lexeme> entry : global.getVariables().entrySet())
		{
			if (entry.getValue().getType().equals(LexemeTypes.PROTOTYPE))
			{
				throw new UndeclaredPrototypeException(entry.getKey());
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Environment temp = mostLocal; temp != null; temp = temp.getPrevEnv())
		{
			sb.append(temp + "\n");
		}
		return sb.toString();
	}

	public void displayLocal()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(mostLocal + "\n");
		System.out.println(sb.toString());
	}

	public void displayAll()
	{
		System.out.println(toString());
	}
}
