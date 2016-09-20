package edu.ua.cs.hergin.proglan.evaluator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import com.rits.cloning.Cloner;

import edu.ua.cs.hergin.proglan.environment.Environment;
import edu.ua.cs.hergin.proglan.environment.EnvironmentUtil;
import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.lexer.Scanner;
import edu.ua.cs.hergin.proglan.parser.Cons;
import edu.ua.cs.hergin.proglan.parser.Parser;
import edu.ua.cs.hergin.proglan.parser.ParserWithEarlyDetection;
import edu.ua.cs.hergin.proglan.parser.Cons.ConsTypes;
import edu.ua.cs.hergin.proglan.parser.UndeclaredPrototypeException;
import edu.ua.cs.hergin.proglan.parser.UndefinedVariableException;
import edu.ua.cs.hergin.proglan.prettyprinter.PrettyPrinter;

public class Evaluator
{

	ParserWithEarlyDetection parser;
	List<String> builtIns = new ArrayList<String>();
	Environment global;
	Environment currentEnv;
	Cons programTree;
	Stack<Environment> envStack;

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			new Evaluator(args[0]).evaluate();
		}
		else
		{
			System.out.println("You must give filename as an argument!");
		}
	}

	public Evaluator(String fileName)
	{
		populateBuiltIns();
		parser = new ParserWithEarlyDetection(fileName, builtIns);
		programTree = parser.parse();
		global = parser.getEnv();
		currentEnv = global;
		envStack = new Stack<Environment>();
	}

	public void evaluate()
	{
		try
		{
			currentEnv = Environment.extend(currentEnv);
			evaluateStatementList(getStatementListFromMain());
		}
		catch (UndefinedVariableException e)
		{
			// this shouldnt work (for parser only)
			e.print();
			System.exit(1);
			//e.printStackTrace();
		}
		catch (UndeclaredPrototypeException e)
		{
			// this shouldnt work (for parser only)
			e.print();
			System.exit(1);
			//e.printStackTrace();
		}
		catch (UnsupportedOperatorException e)
		{
			e.print();
			System.exit(1);
		}
		catch (TooFewArgumentsException e) // in function calls
		{
			e.print();
			System.exit(1);
		}
		catch (UninitializedVariableException e) // a variable is just defined, not have an initial value
		{
			e.print();
			System.exit(1);
			//e.printStackTrace();
		}
	}

	private void populateBuiltIns()
	{
		// FEATURE add more built-in names here
		builtIns.add("display");
		builtIns.add("newline");
		builtIns.add("readString");
		builtIns.add("readInteger");
		builtIns.add("createArray");
		builtIns.add("arrayGet");
		builtIns.add("arraySet");
		builtIns.add("parseInteger");
		builtIns.add("not");
		builtIns.add("power");
		builtIns.add("prettyPrint");
	}

	private Lexeme evalBuiltIn(String fname, Cons argList) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		// FEATURE add more built-in evaluations here
		Lexeme retVal = null;

		if (fname.equals("display"))
		{
			if (argList.getLeft() != null)
			{
				Lexeme l = evaluateExpression(argList.getLeft());
				System.out.print(l.getPureVal());
			}
		}
		else if (fname.equals("power"))
		{
			Lexeme pow1 = evaluateExpression(argList.getLeft());
			Lexeme pow2 = evaluateExpression(argList.getRight().getRight().getLeft());
			return new Lexeme(LexemeTypes.INTEGER, (int) Math.pow(pow1.getIntVal(), pow2.getIntVal()), 0);
		}
		else if (fname.equals("prettyPrint"))
		{
			Lexeme name = currentEnv.getCascading(argList.getLeft().getLeft().getValue());
			if (name.getType().equals(LexemeTypes.INTEGER) || name.getType().equals(LexemeTypes.STRING))
			{
				System.out.println(argList.getLeft().getLeft().getValue().getPureVal() + "=" + name.getPureVal2());
			}
			else
			{
				new PrettyPrinter(name.getCons()).print();
			}
		}
		else if (fname.equals("createArray"))
		{
			// CREATE ARRAY WITH SIZE
			// USING ARRAYLIST so O(1) access time
			Lexeme arraySize = evaluateExpression(argList.getLeft());
			Lexeme array = new Lexeme(LexemeTypes.ARRAY, 0);
			array.setArrVal(new ArrayList<Lexeme>(arraySize.getIntVal()));
			while (array.getArrVal().size() < arraySize.getIntVal())
				array.getArrVal().add(new Lexeme(LexemeTypes.UNKNOWN, 0));
			return array;
		}
		else if (fname.equals("arrayGet"))
		{
			// get FROM ARRAY
			// again O(1)
			Lexeme arrayName = argList.getLeft().getLeft().getValue();
			Lexeme arrayIndex = evaluateExpression(argList.getRight().getRight().getLeft());
			return currentEnv.getCascading(arrayName).getArrVal().get(arrayIndex.getIntVal());
		}
		else if (fname.equals("arraySet"))
		{
			// SET IN ARRAY
			// again O(1)
			Lexeme arrayName = argList.getLeft().getLeft().getValue();
			arrayName.setType(LexemeTypes.STRING);
			Lexeme arrayIndex = evaluateExpression(argList.getRight().getRight().getLeft());
			Lexeme newValue = evaluateExpression(argList.getRight().getRight().getRight().getRight().getLeft());
			currentEnv.getCascading(arrayName).getArrVal().set(arrayIndex.getIntVal(), newValue);
		}
		else if (fname.equals("parseInteger"))
		{
			Lexeme val = evaluateExpression(argList.getLeft());
			return new Lexeme(LexemeTypes.INTEGER, Integer.parseInt(val.getStrVal()), 0);
		}
		else if (fname.equals("newline"))
		{
			System.out.println();
		}
		else if (fname.equals("not"))
		{
			Lexeme val = evaluateExpression(argList.getLeft());
			if (val.getPureVal().equals("0"))
			{
				return new Lexeme(LexemeTypes.INTEGER, 1, 0);
			}
			else
			{
				return new Lexeme(LexemeTypes.INTEGER, 0, 0);
			}
		}
		else if (fname.equals("readString"))
		{
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			try
			{
				String line = in.readLine();
				return new Lexeme(LexemeTypes.STRING, line, 0);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (fname.equals("readInteger"))
		{
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			try
			{
				String line = in.readLine();
				return new Lexeme(LexemeTypes.INTEGER, Integer.parseInt(line), 0);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return retVal;
	}

	private Lexeme evaluateStatementList(Cons statementList) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		if (statementList != null)
		{
			Lexeme retVal = evaluateStatement(statementList.getLeft());
			if (retVal == null)
			{
				return evaluateStatementList(statementList.getRight());
			}
			else
			{
				return retVal;
			}
		}
		return null;
	}

	private Lexeme evaluateStatement(Cons statement) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		if (statement.getType().equals(ConsTypes.IFSTATEMENT))
		{
			return evaluateIfStatement(statement);
		}
		else if (statement.getType().equals(ConsTypes.FORSTATEMENT))
		{
			return evaluateForStatement(statement);
		}
		else if (statement.getType().equals(ConsTypes.WHILESTATEMENT))
		{
			return evaluateWhileStatement(statement);
		}
		else if (statement.getType().equals(ConsTypes.REGULARSTATEMENT))
		{
			return evaluateRegularStatement(statement);
		}
		else if (statement.getType().equals(ConsTypes.VARIABLEDECLARATION))
		{
			evaluateVariableDeclaration(statement);
		}
		else if (statement.getType().equals(ConsTypes.VARIABLEASSIGNMENT))
		{
			evaluateVariableAssignment(statement);
		}
		return null;
	}

	private void evaluateVariableAssignment(Cons statement) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		Lexeme l = evaluateExpression(statement.getRight().getRight().getRight());
		l.setCons(new Cons(ConsTypes.DUMMY)); // to identify between INITIALIZED and NONINITIALIZED VARIABLE
		//env.setVar(statement.getRight().getLeft().getValue().getPureVal(), l);
		currentEnv.setVar(statement.getRight().getLeft().getValue().getPureVal(), l);
	}

	private Environment getClassEnvironment(Cons classDec) throws UndefinedVariableException
	{
		Environment selfEnv = classDec.getSelfEnvironment();

		// Extends something?
		if (classDec.getRight().getRight().getLeft() != null)
		{
			Environment envOfExtend = getClassEnvironment(currentEnv.getCascading(classDec.getRight().getRight().getLeft().getRight().getValue()).getCons());
			selfEnv.setPrevEnv(envOfExtend);
		}

		return selfEnv;
	}

	private void evaluateAllVariables(Environment env) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		envStack.push(currentEnv);
		currentEnv = env;
		Environment temp;
		for (temp = env; temp != null; temp = temp.getPrevEnv())
		{
			for (Map.Entry<String, Lexeme> entry : temp.getVariables().entrySet())
			{
				if (entry.getValue().getType().equals(LexemeTypes.PRESENT))
				{
					if (entry.getValue().getCons() != null && entry.getValue().getCons().getType().equals(ConsTypes.VARIABLEDECLARATION))
					{
						evaluateVariableDeclaration(entry.getValue().getCons());
					}
				}
			}
		}
		currentEnv = envStack.pop();
	}

	private void evaluateVariableDeclaration(Cons varDec) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		if (varDec.getRight().getRight() != null)
		{
			// regular variable declaration
			if (varDec.getRight().getRight().getLeft().getType().equals(LexemeTypes.ASSIGN))
			{
				if (varDec.getRight().getRight().getRight().getLeft().getType().equals(LexemeTypes.ID))
				{
					Lexeme currVar = currentEnv.getCascading(varDec.getRight().getRight().getRight().getLeft().getValue());
					if (currVar.getType().equals(LexemeTypes.METHODPTR) || currVar.getType().equals(LexemeTypes.FUNCTIONPTR))
					{
						currentEnv.add(varDec.getRight().getLeft().getValue().getPureVal(), currVar);
						return;
					}
					else if (currVar.getCons().getType().equals(ConsTypes.FUNCTIONDECLARATION))
					{
						Lexeme l = new Lexeme(LexemeTypes.FUNCTIONPTR, 0);

						l.setStrVal(currVar.getCons().getRight().getLeft().getValue().getPureVal());
						currentEnv.add(varDec.getRight().getLeft().getValue().getPureVal(), l);
						return;
					}
				}
				else if (varDec.getRight().getRight().getRight().getLeft().getType().equals(ConsTypes.PRIMARY))
				{
					Lexeme currVar = currentEnv.getCascading(varDec.getRight().getRight().getRight().getLeft().getLeft().getValue());
					Lexeme l = new Lexeme(LexemeTypes.METHODPTR, 0);
					l.setStrVal(varDec.getRight().getRight().getRight().getLeft().getLeft().getValue().getPureVal() + "." + varDec.getRight().getRight().getRight().getLeft().getRight().getRight().getValue().getPureVal());
					currentEnv.add(varDec.getRight().getLeft().getValue().getPureVal(), l);
					return;
				}
				Lexeme l = evaluateExpression(varDec.getRight().getRight().getRight());
				l.setCons(new Cons(ConsTypes.DUMMY)); // to identify between INITIALIZED and NONINITIALIZED VARIABLE
				//env.putVar(varDec.getRight().getLeft().getValue().getPureVal(), l);
				currentEnv.add(varDec.getRight().getLeft().getValue().getPureVal(), l);
			}
			// new class variable declaration
			else if (varDec.getRight().getRight().getLeft().getType().equals(LexemeTypes.ID))
			{
				Environment realEnv = getClassEnvironment(currentEnv.getCascading(varDec.getRight().getLeft().getValue()).getCons());

				// Clone env so we can modify it for the object
				Environment env = new Cloner().deepClone(realEnv);

				evaluateAllVariables(env);

				// modify the env of object with attributes
				Cons attrList = varDec.getRight().getRight().getRight().getRight().getLeft();
				while (attrList != null)
				{
					String name = attrList.getLeft().getLeft().getValue().getPureVal();
					Lexeme val = evaluatePrimary(attrList.getLeft().getRight().getRight());
					val.setCons(new Cons(val.getType()));
					env.setVar(name, val);
					if (attrList.getRight() != null)
					{
						attrList = attrList.getRight().getRight();
					}
					else
					{
						break;
					}
				}

				currentEnv.add(varDec.getRight().getRight().getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.OBJECT, 0));
				Cons objectCons = new Cons(LexemeTypes.OBJECT);
				objectCons.setSelfEnvironment(env);
				currentEnv.getCascading(varDec.getRight().getRight().getLeft().getValue()).setCons(objectCons);
			}
		}
		else
		{
			throw new UninitializedVariableException(varDec.getRight().getLeft().getValue());
			//			currentEnv.add(varDec.getRight().getLeft().getValue().getPureVal(), (Lexeme)null);
		}
	}

	private Lexeme evaluateRegularStatement(Cons statement) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		return evaluateExpression(statement.getRight());
	}

	private Lexeme evaluateExpression(Cons expr) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		Lexeme l = evaluatePrimary(expr.getLeft());

		// more expr
		if (expr.getRight() != null)
		{
			Lexeme l2 = evaluateExpression(expr.getRight().getRight());

			// STRING and STRING
			if (l.getType().equals(LexemeTypes.STRING) && l2.getType().equals(LexemeTypes.STRING))
			{
				if (expr.getRight().getLeft().getType().equals(LexemeTypes.ADD))
				{
					Lexeme res = new Lexeme(LexemeTypes.STRING, l.getStrVal() + l2.getStrVal(), 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.ASSIGN))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getStrVal().equals(l2.getStrVal()) ? 0 : 1, 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.LESSTHAN))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getStrVal().charAt(0) < l2.getStrVal().charAt(0) ? 0 : 1, 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.GREATERTHAN))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getStrVal().charAt(0) > l2.getStrVal().charAt(0) ? 0 : 1, 0);
					return res;
				}
				else
				{
					throw new UnsupportedOperatorException(expr.getRight().getLeft().getValue(), "STRING", "STRING");
				}
			}
			// STRING and INTEGER
			else if (l.getType().equals(LexemeTypes.STRING) && l2.getType().equals(LexemeTypes.INTEGER))
			{
				if (expr.getRight().getLeft().getType().equals(LexemeTypes.ADD))
				{
					Lexeme res = new Lexeme(LexemeTypes.STRING, l.getStrVal() + l2.getIntVal(), 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.MULTIPLY))
				{
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < l2.getIntVal(); i++)
					{
						sb.append(l.getStrVal());
					}
					Lexeme res = new Lexeme(LexemeTypes.STRING, sb.toString(), 0);
					return res;
				}
				else
				{
					throw new UnsupportedOperatorException(expr.getRight().getLeft().getValue(), "STRING", "INTEGER");
				}

			}
			// INTEGER and STRING
			else if (l.getType().equals(LexemeTypes.INTEGER) && l2.getType().equals(LexemeTypes.STRING))
			{
				if (expr.getRight().getLeft().getType().equals(LexemeTypes.ADD))
				{
					Lexeme res = new Lexeme(LexemeTypes.STRING, l.getIntVal() + l2.getStrVal(), 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.MULTIPLY))
				{
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < l.getIntVal(); i++)
					{
						sb.append(l2.getStrVal());
					}
					Lexeme res = new Lexeme(LexemeTypes.STRING, sb.toString(), 0);
					return res;
				}
				else
				{
					throw new UnsupportedOperatorException(expr.getRight().getLeft().getValue(), "INTEGER", "STRING");
				}

			}
			// INTEGER and INTEGER
			else if (l.getType().equals(LexemeTypes.INTEGER) && l2.getType().equals(LexemeTypes.INTEGER))
			{
				if (expr.getRight().getLeft().getType().equals(LexemeTypes.ADD))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getIntVal() + l2.getIntVal(), 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.MULTIPLY))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getIntVal() * l2.getIntVal(), 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.SUBTRACT))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getIntVal() - l2.getIntVal(), 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.DIVIDE))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getIntVal() / l2.getIntVal(), 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.ASSIGN))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getIntVal() == l2.getIntVal() ? 0 : 1, 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.GREATERTHAN))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getIntVal() > l2.getIntVal() ? 0 : 1, 0);
					return res;
				}
				else if (expr.getRight().getLeft().getType().equals(LexemeTypes.LESSTHAN))
				{
					Lexeme res = new Lexeme(LexemeTypes.INTEGER, l.getIntVal() < l2.getIntVal() ? 0 : 1, 0);
					return res;
				}
				else
				{
					throw new UnsupportedOperatorException(expr.getRight().getLeft().getValue(), "INTEGER", "INTEGER");
				}

			}

		}

		return l;
	}

	private Lexeme evaluatePrimary(Cons expr) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		// INTEGER and STRING, return self value
		if (expr.getType().equals(LexemeTypes.INTEGER) || expr.getType().equals(LexemeTypes.STRING))
		{
			return expr.getValue();
		}
		// if it is ID, find value from ENV 
		else if (expr.getType().equals(LexemeTypes.ID))
		{
			//Lexeme l = env.getVar(expr.getValue().getPureVal());
			Lexeme l = currentEnv.getCascading(expr.getValue());

			if (l.getType().equals(LexemeTypes.METHODPTR) || l.getType().equals(LexemeTypes.FUNCTIONPTR))
			{
				return l;
			}

			// CALCULATE the Value if needed
			if (l.getCons() != null && l.getType().equals(LexemeTypes.PRESENT))
			{
				evaluateVariableAssignment(l.getCons());
				l = currentEnv.getCascading(expr.getValue());
			}
			else if (l.getCons() != null && l.getCons().getType().equals(ConsTypes.VARIABLEDECLARATION))
			{
				evaluateVariableDeclaration(l.getCons());
			}
			else if (l.getCons() == null)
			{
				throw new UninitializedVariableException(expr.getValue());
			}

			return l;

		}
		else if (expr.getType().equals(ConsTypes.FUNCTIONCALL))
		{
			return evaluateFunctionCall(expr);
		}
		else if (expr.getType().equals(ConsTypes.PRIMARY))
		{
			return evaluateExpression(expr.getRight().getLeft());
		}
		return null;
	}

	private Lexeme evaluateFunctionCall(Cons fcall) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		Lexeme retVal = null;

		if (fcall.getRight().getRight().getLeft().getType().equals(LexemeTypes.OPAREN))
		// means a regular function call
		{
			if (isBuiltIn(fcall.getRight().getLeft().getValue().getPureVal()))
			{
				return evalBuiltIn(fcall.getRight().getLeft().getValue().getPureVal(), fcall.getRight().getRight().getRight().getLeft());
			}
			else
			{
				Lexeme l1 = currentEnv.getCascading(fcall.getRight().getLeft().getValue());
				if (l1.getType().equals(LexemeTypes.FUNCTIONPTR))
				{
					fcall.getRight().getLeft().getValue().setStrVal(l1.getStrVal());
					return evaluateFunctionCall(fcall);
				}
				else if (l1.getType().equals(LexemeTypes.METHODPTR))
				{
					StringTokenizer tok = new StringTokenizer(l1.getStrVal(), ".");
					String objectName = tok.nextToken();
					String methodName = tok.nextToken();
					Cons temp = fcall.getRight().getRight();
					Cons J1 = new Cons();
					J1.setLeft(new Cons(LexemeTypes.DOT, new Lexeme(LexemeTypes.DOT, 0)));

					Cons J2 = new Cons();
					J2.setLeft(new Cons(LexemeTypes.ID, new Lexeme(LexemeTypes.ID, methodName, 0)));
					J2.setRight(temp);

					J1.setRight(J2);
					fcall.getRight().getLeft().getValue().setStrVal(objectName);
					fcall.getRight().setRight(J1);
					return evaluateFunctionCall(fcall);
				}

				// get the function definition from env.
				Cons fDef = currentEnv.getCascading(fcall.getRight().getLeft().getValue()).getCons();
				//Cons fDef = env.getVar(fcall.getRight().getLeft().getValue().getPureVal()).getCons();

				//create new env for function
				//env.createNewEnv(fcall.getRight().getLeft().getValue().getPureVal());

				Environment fNew = new Environment("CALL");

				//populate with params
				Cons paramList = fDef.getRight().getRight().getRight().getLeft();
				Cons argumentList = fcall.getRight().getRight().getRight().getLeft();

				// function has params?
				try
				{
					Cons tempParamList = paramList;
					Cons tempArgumentList = argumentList;

					while (tempArgumentList != null)
					{
						Lexeme l = evaluateExpression(tempArgumentList.getLeft());
						l.setCons(new Cons(ConsTypes.DUMMY));
						//env.putVar(tempParamList.getLeft().getValue().getPureVal(), l);
						fNew.add(tempParamList.getLeft().getValue().getPureVal(), l);

						if ((tempArgumentList.getRight() == null && tempParamList.getRight() != null) || (tempArgumentList.getRight() != null && tempParamList.getRight() == null))
						{
							throw new TooFewArgumentsException(fcall.getLeft().getValue(), fDef.getRight().getLeft().getValue().getPureVal());
						}
						else if (tempArgumentList.getRight() == null && tempParamList.getRight() == null)
						{
							break;
						}
						else
						{
							tempArgumentList = tempArgumentList.getRight().getRight();
							tempParamList = tempParamList.getRight().getRight();
						}
					}
				}
				catch (NullPointerException e)
				{
					throw new TooFewArgumentsException(fcall.getLeft().getValue(), fDef.getRight().getLeft().getValue().getPureVal());
				}

				envStack.push(currentEnv);
				currentEnv = fDef.getDefinitionEnvironment();
				fNew.setPrevEnv(currentEnv);
				currentEnv = fNew;

				// evaluate the function body
				if (fDef.getRight().getRight().getRight().getRight().getRight().getRight().getLeft() != null)
				{
					retVal = evaluateStatementList(fDef.getRight().getRight().getRight().getRight().getRight().getRight().getLeft());
				}

				currentEnv = envStack.pop();

				return retVal;
			}
		}
		else if (fcall.getRight().getRight().getLeft().getType().equals(LexemeTypes.DOT))
		//means a class method call
		{
			Environment fNew = new Environment("CALL");
			Cons objDef = currentEnv.getCascading(fcall.getRight().getLeft().getValue()).getCons();
			Environment objEnv = objDef.getSelfEnvironment();
			Cons fDef = objEnv.getCascading(fcall.getRight().getRight().getRight().getLeft().getValue()).getCons();

			// populate with args
			Cons argList = fcall.getRight().getRight().getRight().getRight().getRight().getLeft();
			Cons paramList = fDef.getRight().getRight().getRight().getLeft();

			try
			{
				Cons tempParamList = paramList;
				Cons tempArgumentList = argList;

				while (tempArgumentList != null)
				{
					Lexeme l = evaluateExpression(tempArgumentList.getLeft());
					l.setCons(new Cons(ConsTypes.DUMMY));
					//env.putVar(tempParamList.getLeft().getValue().getPureVal(), l);
					fNew.add(tempParamList.getLeft().getValue().getPureVal(), l);

					if ((tempArgumentList.getRight() == null && tempParamList.getRight() != null) || (tempArgumentList.getRight() != null && tempParamList.getRight() == null))
					{
						throw new TooFewArgumentsException(fcall.getLeft().getValue(), fDef.getRight().getLeft().getValue().getPureVal() + "." + fDef.getRight().getRight().getRight().getLeft().getValue().getPureVal());
					}
					else if (tempArgumentList.getRight() == null && tempParamList.getRight() == null)
					{
						break;
					}
					else
					{
						tempArgumentList = tempArgumentList.getRight().getRight();
						tempParamList = tempParamList.getRight().getRight();
					}
				}
			}
			catch (NullPointerException e)
			{
				throw new TooFewArgumentsException(fcall.getLeft().getValue(), fDef.getRight().getLeft().getValue().getPureVal() + "." + fDef.getRight().getRight().getRight().getLeft().getValue().getPureVal());
			}

			envStack.push(currentEnv);
			currentEnv = objEnv;
			fNew.setPrevEnv(currentEnv);
			currentEnv = fNew;

			// evaluate the function body
			if (fDef.getRight().getRight().getRight().getRight().getRight().getRight().getLeft() != null)
			{
				try
				{
					retVal = evaluateStatementList(fDef.getRight().getRight().getRight().getRight().getRight().getRight().getLeft());
				}
				catch (UninitializedVariableException e)
				{
					Lexeme var = e.getVar();
					var.setStrVal(fcall.getRight().getLeft().getValue().getPureVal() + "." + var.getStrVal());
					throw new UninitializedVariableException(var);
				}
			}

			currentEnv = envStack.pop();

			return retVal;
		}
		return retVal;
	}

	private Lexeme evaluateWhileStatement(Cons whi) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		Lexeme retVal = null;

		// evaluate expression to see while's condition
		Lexeme l = evaluateExpression(whi.getRight().getRight().getLeft());

		if (l.getIntVal() == 0) //means TRUE
		{
			//env.createNewEnv("WHILE");
			currentEnv = Environment.extend(currentEnv);

			retVal = evaluateRegularBlock(whi.getRight().getRight().getRight().getRight());

			//env.deleteMostLocal();
			currentEnv = currentEnv.getPrevEnv();
		}

		// re-evaluate expr and recall 'while' if TRUE
		l = evaluateExpression(whi.getRight().getRight().getLeft());

		if (l.getIntVal() == 0)
		{
			retVal = evaluateWhileStatement(whi);
		}

		return retVal;
	}

	private Lexeme evaluateForStatement(Cons forr) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		Lexeme retVal = null;

		//env.createNewEnv("FOR");
		currentEnv = Environment.extend(currentEnv);

		// add the initiazlier variable to new env.
		evaluateVariableDeclaration(forr.getRight().getRight().getLeft());

		// evaluate expr for first time
		Lexeme l = evaluateExpression(forr.getRight().getRight().getRight().getRight().getLeft());

		// while expression is true
		while (l.getIntVal() == 0)
		{
			// evalue the block of 'for'
			retVal = evaluateRegularBlock(forr.getRight().getRight().getRight().getRight().getRight().getRight().getRight().getRight());

			// evaluate the step part of for
			evaluateVariableAssignment(forr.getRight().getRight().getRight().getRight().getRight().getRight().getLeft());

			// reevaluate the condition expr
			l = evaluateExpression(forr.getRight().getRight().getRight().getRight().getLeft());
		}

		//env.deleteMostLocal();
		currentEnv = currentEnv.getPrevEnv();

		return retVal;

	}

	private Lexeme evaluateIfStatement(Cons iff) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		Lexeme retVal = null;

		// evaluate condition of IF first
		Lexeme l = evaluateExpression(iff.getRight().getRight().getLeft());

		// eval if part (because result of the expression is true)
		if (l.getIntVal() == 0)
		{
			//env.createNewEnv("IF");
			currentEnv = Environment.extend(currentEnv);

			retVal = evaluateRegularBlock(iff.getRight().getRight().getRight().getRight().getLeft());

			//env.deleteMostLocal();
			currentEnv = currentEnv.getPrevEnv();

			return retVal; //return here to avoid ELSE part also work
		}

		//check if IF has an ELSE part
		if (iff.getRight().getRight().getRight().getRight().getRight() != null)
		{
			//check this ELSE is 'just else' or 'if else'
			if (iff.getRight().getRight().getRight().getRight().getRight().getRight().getType().equals(ConsTypes.REGULARBLOCK))
			{
				// JUST ELSE, so we must execute this part (because the above 'if' didnt work)
				//env.createNewEnv("ELSE");
				currentEnv = Environment.extend(currentEnv);

				retVal = evaluateRegularBlock(iff.getRight().getRight().getRight().getRight().getRight().getRight());

				currentEnv = currentEnv.getPrevEnv();
				//env.deleteMostLocal();
			}
			else if (iff.getRight().getRight().getRight().getRight().getRight().getRight().getType().equals(ConsTypes.IFSTATEMENT))
			{
				// it is another IF, so call IF again
				retVal = evaluateIfStatement(iff.getRight().getRight().getRight().getRight().getRight().getRight());
			}
		}

		return retVal;
	}

	private Lexeme evaluateRegularBlock(Cons regBlock) throws UndefinedVariableException, UnsupportedOperatorException, UninitializedVariableException, UndeclaredPrototypeException, TooFewArgumentsException
	{
		return evaluateStatementList(regBlock.getRight().getLeft());
	}

	private Cons getMain()
	{
		return programTree.getRight();
	}

	private Cons getStatementListFromMain()
	{
		return getMain().getRight().getRight().getLeft();
	}

	private boolean isBuiltIn(String f)
	{
		return builtIns.contains(f);
	}

}
