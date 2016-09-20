/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.parser;

import java.util.ArrayList;
import java.util.List;

import edu.ua.cs.hergin.proglan.environment.Environment;
import edu.ua.cs.hergin.proglan.environment.EnvironmentUtil;
import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.parser.Cons.ConsTypes;
import edu.ua.cs.hergin.proglan.prettyprinter.PrettyPrinter;
import edu.ua.cs.hergin.proglan.recognizer.EndOfInputException;
import edu.ua.cs.hergin.proglan.recognizer.RecognizerException;

public class ParserWithEarlyDetection
{

	ParserUtil util;

	//EnvironmentUtil env;

	Environment global;

	Environment mostLocal;

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			List<String> l = new ArrayList<String>();
			l.add("display");
			ParserWithEarlyDetection p = new ParserWithEarlyDetection(args[0], l);
			p.parse();
			System.out.print("No problem!");
		}
		else
		{
			System.out.println("You must give filename as an argument!");
		}
	}

	public ParserWithEarlyDetection(String fileName, List<String> l)
	{

		try
		{
			util = new ParserUtil(fileName);
			// env = new EnvironmentUtil();
			global = new Environment("GLOBAL");
			mostLocal = global;
			fillBuiltInFunctions(l);
		}
		catch (EndOfInputException e)
		{
			System.out.println("File is empty!");
		}

	}

	public void fillBuiltInFunctions(List<String> builtIns)
	{
		for (String s : builtIns)
		{
			// env.putVar(s, new Lexeme(LexemeTypes.PRESENT, 0));
			mostLocal.add(s, new Lexeme(LexemeTypes.PRESENT, 0));
		}
	}

	public Cons parse()
	{
		Cons res = null;
		try
		{
			res = program();
		}
		catch (RecognizerException e)
		{
			e.print();
			System.exit(1);
			// e.printStackTrace();
		}
		catch (UndeclaredPrototypeException e)
		{
			e.print();
			System.exit(1);
			// e.printStackTrace();
		}
		catch (UndefinedVariableException e)
		{
			e.print();
			System.exit(1);
			// e.printStackTrace();
		}
		catch (EndOfInputException e)
		{
			// e.printStackTrace();
			System.out.println("Source code fits to the grammar!");
		}
		return res;
	}

	private Cons program() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.PROGRAM);
		if (structListPending())
		{
			res.setLeft(structList());
		}
		res.setRight(mainClause());
		if (!util.check(LexemeTypes.ENDofINPUT))
		{
			throw new RecognizerException("More unnecessary code under main!");
		}
		// env.undeclaredPrototypesInGlobal();
		mostLocal.undeclaredPrototypes();
		return res;
	}

	private Cons mainClause() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.MAIN);
		res.setLeft(util.match(LexemeTypes.MAIN));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.OCURLY));
		res.setRight(J1);

		mostLocal = Environment.extend(mostLocal);

		Cons J2 = new Cons();
		if (statementListPending())
		{
			J2.setLeft(statementList());
		}
		J2.setRight(util.match(LexemeTypes.CCURLY));
		J1.setRight(J2);

		mostLocal.undeclaredPrototypes();
		mostLocal = mostLocal.getPrevEnv();

		return res;
	}

	private Cons structList() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.STRUCTLIST);
		res.setLeft(struct());
		if (structListPending())
		{
			res.setRight(structList());
		}
		return res;
	}

	private Cons struct() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		if (classDeclarationPending())
		{
			return classDeclaration();
		}
		else if (functionDeclarationPending())
		{
			return functionDeclaration();
		}
		else if (prototypeDeclarationPending())
		{
			return prototypeDeclaration();
		}
		else
		{
			return variableDeclaration();
		}
	}

	private Cons functionDeclaration() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.FUNCTIONDECLARATION);
		res.setLeft(util.match(LexemeTypes.FUNCTION));

		res.setDefinitionEnvironment(mostLocal);

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.ID));
		res.setRight(J1);

		// env.putVar(J1.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);
		mostLocal.add(J1.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);

		// env.createNewEnv(J1.getLeft().getValue().getPureVal());
		mostLocal = Environment.extend(mostLocal);

		Cons J2 = new Cons();
		J2.setLeft(util.match(LexemeTypes.OPAREN));
		J1.setRight(J2);

		Cons J3 = new Cons();
		if (parameterListPending())
		{
			J3.setLeft(parameterList());
		}
		J2.setRight(J3);

		Cons J4 = new Cons();
		J4.setLeft(util.match(LexemeTypes.CPAREN));
		J3.setRight(J4);

		Cons J5 = new Cons();
		J5.setLeft(util.match(LexemeTypes.OCURLY));
		J4.setRight(J5);

		Cons J6 = new Cons();
		if (statementListPending())
		{
			J6.setLeft(statementList());
		}
		J6.setRight(util.match(LexemeTypes.CCURLY));
		J5.setRight(J6);

		// env.deleteMostLocal();
		mostLocal.undeclaredPrototypes();
		mostLocal = mostLocal.getPrevEnv();

		return res;
	}

	private Cons parameterList() throws RecognizerException, EndOfInputException
	{
		Cons res = new Cons(ConsTypes.PARAMETERLIST);
		res.setLeft(util.match(LexemeTypes.ID));

		// env.putVar(res.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0));
		mostLocal.add(res.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0));

		if (util.check(LexemeTypes.COMMA))
		{
			Cons J1 = new Cons();
			J1.setLeft(util.match(LexemeTypes.COMMA));
			J1.setRight(parameterList());
			res.setRight(J1);
		}

		return res;
	}

	private Cons statementList() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.STATEMENTLIST);
		res.setLeft(statement());
		if (statementListPending())
		{
			res.setRight(statementList());
		}
		return res;
	}

	private Cons statement() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		if (ifStatementPending())
		{
			return ifStatement();
		}
		else if (forStatementPending())
		{
			return forStatement();
		}
		else if (whileStatementPending())
		{
			return whileStatement();
		}
		else if (variableDeclarationPending())
		{
			return variableDeclaration();
		}
		else if (variableAssignmentPending())
		{
			return variableAssignment();
		}
		else
		{
			return regularStatement();
		}
	}

	private Cons variableAssignment() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.VARIABLEASSIGNMENT);
		res.setLeft(util.match(LexemeTypes.SET));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.ID));
		res.setRight(J1);

		// env.getVar(J1.getLeft().getValue());
		mostLocal.getCascading(J1.getLeft().getValue());

		Cons J2 = new Cons();
		J2.setLeft(util.match(LexemeTypes.ASSIGN));
		J2.setRight(expression());
		J1.setRight(J2);

		return res;
	}

	private boolean variableAssignmentPending()
	{
		return util.check(LexemeTypes.SET);
	}

	private Cons whileStatement() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.WHILESTATEMENT);
		res.setLeft(util.match(LexemeTypes.WHILE));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.OPAREN));
		res.setRight(J1);

		Cons J2 = new Cons();
		J2.setLeft(expression());
		J1.setRight(J2);

		// env.createNewEnv("WHILE");
		mostLocal = Environment.extend(mostLocal);

		Cons J3 = new Cons();
		J3.setLeft(util.match(LexemeTypes.CPAREN));
		J3.setRight(regularBlock());
		J2.setRight(J3);

		// env.deleteMostLocal();
		mostLocal.undeclaredPrototypes();
		mostLocal = mostLocal.getPrevEnv();

		return res;
	}

	private Cons forStatement() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.FORSTATEMENT);
		res.setLeft(util.match(LexemeTypes.FOR));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.OPAREN));
		res.setRight(J1);

		// env.createNewEnv("FOR");
		mostLocal = Environment.extend(mostLocal);

		Cons J2 = new Cons();
		J2.setLeft(variableDeclaration());
		J1.setRight(J2);

		Cons J3 = new Cons();
		J3.setLeft(util.match(LexemeTypes.SEMICOLON));
		J2.setRight(J3);

		Cons J4 = new Cons();
		J4.setLeft(expression());
		J3.setRight(J4);

		Cons J5 = new Cons();
		J5.setLeft(util.match(LexemeTypes.SEMICOLON));
		J4.setRight(J5);

		Cons J6 = new Cons();
		J6.setLeft(variableAssignment());
		J5.setRight(J6);

		Cons J7 = new Cons();
		J7.setLeft(util.match(LexemeTypes.CPAREN));
		J7.setRight(regularBlock());
		J6.setRight(J7);

		// env.deleteMostLocal();
		mostLocal.undeclaredPrototypes();
		mostLocal = mostLocal.getPrevEnv();

		return res;
	}

	private Cons variableDeclaration() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.VARIABLEDECLARATION);
		res.setDefinitionEnvironment(mostLocal);
		res.setLeft(util.match(LexemeTypes.VAR));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.ID));
		res.setRight(J1);

		Cons J2 = new Cons();
		if (util.check(LexemeTypes.ASSIGN))
		{

			// env.putVar(J1.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);
			mostLocal.add(J1.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);

			J2.setLeft(util.match(LexemeTypes.ASSIGN));
			J2.setRight(expression());
			J1.setRight(J2);
		}
		else if (util.check(LexemeTypes.ID))
		{

			// check class name
			// env.getVar(J1.getLeft().getValue());
			mostLocal.getCascading(J1.getLeft().getValue());

			J2.setLeft(util.match(LexemeTypes.ID));
			J1.setRight(J2);

			// env.putVar(J2.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);
			mostLocal.add(J2.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);

			Cons J3 = new Cons();
			J3.setLeft(util.match(LexemeTypes.OPAREN));
			J2.setRight(J3);

			Cons J4 = new Cons();
			if (attributeListPending())
			{
				J4.setLeft(attributeList());
			}
			J4.setRight(util.match(LexemeTypes.CPAREN));
			J3.setRight(J4);
		}
		else
		{
			// env.putVar(J1.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0));
			mostLocal.add(J1.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0));
		}

		return res;
	}

	private Cons attributeList() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.ATTRIBUTELIST);
		res.setLeft(attribute());

		Cons J1 = new Cons();
		if (util.check(LexemeTypes.COMMA))
		{
			J1.setLeft(util.match(LexemeTypes.COMMA));
			J1.setRight(attributeList());
			res.setRight(J1);
		}

		return res;
	}

	private Cons attribute() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.ATTRIBUTE);
		res.setLeft(util.match(LexemeTypes.ID));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.ASSIGN));
		J1.setRight(primary());
		res.setRight(J1);

		return res;
	}

	private Cons regularStatement() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.REGULARSTATEMENT);
		if (returnPending())
		{
			res.setLeft(returnClause());
		}
		res.setRight(expression());
		return res;
	}

	private Cons returnClause() throws RecognizerException, EndOfInputException
	{
		return util.match(LexemeTypes.RETURN);
	}

	private Cons ifStatement() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.IFSTATEMENT);
		res.setLeft(util.match(LexemeTypes.IF));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.OPAREN));
		res.setRight(J1);

		Cons J2 = new Cons();
		J2.setLeft(expression());
		J1.setRight(J2);

		Cons J3 = new Cons();
		J3.setLeft(util.match(LexemeTypes.CPAREN));
		J2.setRight(J3);

		// env.createNewEnv("IF");
		mostLocal = Environment.extend(mostLocal);

		Cons J4 = new Cons();
		J4.setLeft(regularBlock());

		// env.deleteMostLocal();
		mostLocal.undeclaredPrototypes();
		mostLocal = mostLocal.getPrevEnv();

		if (elseClausePending())
		{
			J4.setRight(elseClause());
		}
		J3.setRight(J4);

		return res;
	}

	private Cons elseClause() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.ELSE);
		res.setLeft(util.match(LexemeTypes.ELSE));
		if (regularBlockPending())
		{
			// env.createNewEnv("ELSE");
			mostLocal = Environment.extend(mostLocal);
			res.setRight(regularBlock());
			// env.deleteMostLocal();
			mostLocal.undeclaredPrototypes();
			mostLocal = mostLocal.getPrevEnv();

		}
		else
		{
			res.setRight(ifStatement());
		}
		return res;
	}

	private Cons regularBlock() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.REGULARBLOCK);
		res.setLeft(util.match(LexemeTypes.OCURLY));

		Cons J1 = new Cons();
		J1.setLeft(statementList());
		J1.setRight(util.match(LexemeTypes.CCURLY));
		res.setRight(J1);

		return res;
	}

	private Cons classDeclaration() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.CLASSDECLARATION);
		res.setDefinitionEnvironment(mostLocal);
		res.setLeft(util.match(LexemeTypes.CLASS));

		Cons temp = new Cons(ConsTypes.JOIN);
		temp.setLeft(util.match(LexemeTypes.ID));

		// env.putVar(temp.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);
		mostLocal.add(temp.getLeft().getValue().getPureVal(), new Lexeme(LexemeTypes.PRESENT, 0), res);

		Cons temp2 = new Cons(ConsTypes.JOIN);

		if (extendsStatementPending())
		{
			temp2.setLeft(extendsStatement());
		}

		// env.createNewEnv(temp.getLeft().getValue().getPureVal());
		mostLocal = Environment.extend(mostLocal);

		temp2.setRight(declarationBlock());
		res.setSelfEnvironment(mostLocal);

		// env.deleteMostLocal();
		mostLocal.undeclaredPrototypes();
		mostLocal = mostLocal.getPrevEnv();

		temp.setRight(temp2);

		res.setRight(temp);

		return res;
	}

	private Cons extendsStatement() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.EXTENDS);
		res.setLeft(util.match(LexemeTypes.EXTENDS));
		res.setRight(util.match(LexemeTypes.ID));

		// env.getVar(res.getRight().getValue());
		mostLocal.getCascading(res.getRight().getValue());

		return res;
	}

	private Cons declarationBlock() throws RecognizerException, EndOfInputException, UndefinedVariableException, UndeclaredPrototypeException
	{
		Cons res = new Cons(ConsTypes.DECLARATIONBLOCK);
		res.setLeft(util.match(LexemeTypes.OCURLY));

		Cons temp = new Cons(ConsTypes.JOIN);
		if (structListPending())
		{
			temp.setLeft(structList());
		}
		temp.setRight(util.match(LexemeTypes.CCURLY));

		res.setRight(temp);

		return res;
	}

	private Cons expression() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(Cons.ConsTypes.EXPRESSION);
		res.setLeft(primary());
		if (operatorPending())
		{
			Cons temp = new Cons();
			temp.setLeft(operator());
			temp.setRight(expression());
			res.setRight(temp);
		}
		return res;
	}

	private Cons primary() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.PRIMARY);
		if (util.check(LexemeTypes.INTEGER))
		{
			return util.match(LexemeTypes.INTEGER);
		}
		else if (util.check(LexemeTypes.STRING))
		{
			return util.match(LexemeTypes.STRING);
		}
		else if (util.check(LexemeTypes.ID))
		{
			Cons res2 = util.match(LexemeTypes.ID);
			// env.getVar(res2.getValue());
			if (util.check(LexemeTypes.DOT))
			{
				res.setLeft(res2);
				Cons J1 = new Cons();
				J1.setLeft(util.match(LexemeTypes.DOT));
				J1.setRight(util.match(LexemeTypes.ID));
				res.setRight(J1);
				return res;
			}
			else
			{
				mostLocal.getCascading(res2.getValue());
			}

			return res2;
		}
		else if (functionCallPending())
		{
			return functionCall();
		}
		else
		{
			res.setLeft(util.match(LexemeTypes.OPAREN));
			Cons J1 = new Cons();
			J1.setLeft(expression());
			J1.setRight(util.match(LexemeTypes.CPAREN));
			res.setRight(J1);
		}
		return res;
	}

	private Cons functionCall() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.FUNCTIONCALL);
		res.setLeft(util.match(LexemeTypes.CALL));

		Cons J1 = new Cons();
		J1.setLeft(util.match(LexemeTypes.ID));
		res.setRight(J1);

		// env.getVar(J1.getLeft().getValue());
		mostLocal.getCascading(J1.getLeft().getValue());

		Cons J2 = new Cons();
		if (util.check(LexemeTypes.OPAREN))
		{
			J2.setLeft(util.match(LexemeTypes.OPAREN));
			J1.setRight(J2);

			Cons J3 = new Cons();
			if (argumentListPending())
			{
				J3.setLeft(argumentList());
			}
			J3.setRight(util.match(LexemeTypes.CPAREN));
			J2.setRight(J3);
		}
		else
		{
			J2.setLeft(util.match(LexemeTypes.DOT));
			J1.setRight(J2);

			Cons J3 = new Cons();
			J3.setLeft(util.match(LexemeTypes.ID));
			J2.setRight(J3);

			Cons J4 = new Cons();
			J4.setLeft(util.match(LexemeTypes.OPAREN));
			J3.setRight(J4);

			Cons J5 = new Cons();
			if (argumentListPending())
			{
				J5.setLeft(argumentList());
			}
			J5.setRight(util.match(LexemeTypes.CPAREN));
			J4.setRight(J5);
		}

		return res;
	}

	private Cons argumentList() throws RecognizerException, EndOfInputException, UndefinedVariableException
	{
		Cons res = new Cons(ConsTypes.ARGUMENTLIST);
		res.setLeft(expression());

		if (util.check(LexemeTypes.COMMA))
		{
			Cons J1 = new Cons();
			J1.setLeft(util.match(LexemeTypes.COMMA));
			J1.setRight(argumentList());
			res.setRight(J1);
		}

		return res;
	}

	private Cons operator() throws RecognizerException, EndOfInputException
	{
		if (util.check(LexemeTypes.ADD))
		{
			return util.match(LexemeTypes.ADD);
		}
		else if (util.check(LexemeTypes.SUBTRACT))
		{
			return util.match(LexemeTypes.SUBTRACT);
		}
		else if (util.check(LexemeTypes.MULTIPLY))
		{
			return util.match(LexemeTypes.MULTIPLY);
		}
		else if (util.check(LexemeTypes.DIVIDE))
		{
			return util.match(LexemeTypes.DIVIDE);
		}
		else if (util.check(LexemeTypes.ASSIGN))
		{
			return util.match(LexemeTypes.ASSIGN);
		}
		else if (util.check(LexemeTypes.LESSTHAN))
		{
			return util.match(LexemeTypes.LESSTHAN);
		}
		else if (util.check(LexemeTypes.GREATERTHAN))
		{
			return util.match(LexemeTypes.GREATERTHAN);
		}
		else
		{
			return util.match(LexemeTypes.DOT);
		}
	}

	private boolean operatorPending()
	{
		return util.check(LexemeTypes.ADD) || util.check(LexemeTypes.SUBTRACT) || util.check(LexemeTypes.MULTIPLY) || util.check(LexemeTypes.DIVIDE) || util.check(LexemeTypes.ASSIGN) || util.check(LexemeTypes.LESSTHAN) || util.check(LexemeTypes.GREATERTHAN) || util.check(LexemeTypes.DOT);
	}

	private boolean parameterListPending()
	{
		return expressionPending();
	}

	private boolean functionDeclarationPending()
	{
		return util.check(LexemeTypes.FUNCTION);
	}

	private boolean regularBlockPending()
	{
		return util.check(LexemeTypes.OCURLY);
	}

	private boolean elseClausePending()
	{
		return util.check(LexemeTypes.ELSE);
	}

	private boolean forStatementPending()
	{
		return util.check(LexemeTypes.FOR);
	}

	private boolean ifStatementPending()
	{
		return util.check(LexemeTypes.IF);
	}

	private boolean whileStatementPending()
	{
		return util.check(LexemeTypes.WHILE);
	}

	private boolean returnPending()
	{
		return util.check(LexemeTypes.RETURN);
	}

	private boolean regularStatementPending()
	{
		return expressionPending() || util.check(LexemeTypes.RETURN);
	}

	private boolean expressionPending()
	{
		return primaryPending();
	}

	private boolean primaryPending()
	{
		return util.check(LexemeTypes.INTEGER) || util.check(LexemeTypes.STRING) || util.check(LexemeTypes.ID) || util.check(LexemeTypes.OPAREN) || functionCallPending();
	}

	private boolean statementListPending()
	{
		return statementPending();
	}

	private boolean statementPending()
	{
		return ifStatementPending() || forStatementPending() || whileStatementPending() || regularStatementPending() || variableDeclarationPending() || variableAssignmentPending();
	}

	private boolean argumentListPending()
	{
		return expressionPending();
	}

	private boolean functionCallPending()
	{
		return util.check(LexemeTypes.CALL);
	}

	private boolean attributeListPending()
	{
		return attributePending();
	}

	private boolean attributePending()
	{
		return util.check(LexemeTypes.ID);
	}

	private boolean variableDeclarationPending()
	{
		return util.check(LexemeTypes.VAR);
	}

	private boolean extendsStatementPending()
	{
		return util.check(LexemeTypes.EXTENDS);
	}

	private boolean classDeclarationPending()
	{
		return util.check(LexemeTypes.CLASS);
	}

	private boolean structListPending()
	{
		return structPending();
	}

	private Cons prototypeDeclaration() throws RecognizerException, EndOfInputException
	{
		Cons res = new Cons(ConsTypes.PROTOTYPE);
		res.setLeft(util.match(LexemeTypes.FORWARD));
		res.setRight(util.match(LexemeTypes.ID));

		// env.putVar(res.getRight().getValue().getPureVal(), new Lexeme(LexemeTypes.PROTOTYPE, 0));
		mostLocal.add(res.getRight().getValue().getPureVal(), new Lexeme(LexemeTypes.PROTOTYPE, 0));

		return res;
	}

	private boolean prototypeDeclarationPending()
	{
		return util.check(LexemeTypes.FORWARD);
	}

	private boolean structPending()
	{
		return classDeclarationPending() || functionDeclarationPending() || variableDeclarationPending() || prototypeDeclarationPending();
	}

	public Environment getEnv()
	{
		return global;
	}

}
