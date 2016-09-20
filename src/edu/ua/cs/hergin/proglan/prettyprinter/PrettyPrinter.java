/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.prettyprinter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.ua.cs.hergin.proglan.evaluator.Evaluator;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;
import edu.ua.cs.hergin.proglan.parser.Cons;
import edu.ua.cs.hergin.proglan.parser.Parser;
import edu.ua.cs.hergin.proglan.parser.Cons.ConsTypes;

public class PrettyPrinter
{

	Cons program;
	int tabLength = 4;
	StringBuilder sb = new StringBuilder();

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			Parser p = new Parser(args[0]);
			PrettyPrinter pp = new PrettyPrinter(p.parse());
			pp.print();
		}
		else
		{
			System.out.println("You must give filename as an argument!");
		}
	}

	public PrettyPrinter(Cons p)
	{
		program = p;
	}

	public void print()
	{
		printRecursive(program, 0);

		// deleting blank lines
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(sb.toString().getBytes())));
		sb = new StringBuilder();
		String line;
		try
		{
			while ((line = br.readLine()) != null)
			{
				if (!line.trim().equals(""))
				{
					sb.append(line + "\n");
				}
			}
			br.close();
		}
		catch (IOException e)
		{
		}

		display();
	}

	private void display()
	{
		System.out.print(sb.toString());
	}

	private void printRecursive(Cons c, int ind)
	{
		if (c.getType().equals(ConsTypes.CLASSDECLARATION))
		{
			printClassDeclaration(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.FUNCTIONDECLARATION))
		{
			printFunctionDeclaration(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.IFSTATEMENT))
		{
			printIfStatement(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.FORSTATEMENT))
		{
			printForStatement(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.WHILESTATEMENT))
		{
			printWhileStatement(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.VARIABLEDECLARATION))
		{
			printVariableDeclaration(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.VARIABLEASSIGNMENT))
		{
			printVariableAssignment(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.REGULARSTATEMENT))
		{
			printRegularStatement(c, ind);
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.PROTOTYPE))
		{
			echo("forward");
			space();
			echo(c.getRight().getValue().getPureVal2());
			newline();
			printSpace(ind);
		}
		else if (c.getType().equals(ConsTypes.MAIN))
		{
			printMain(c, ind);
			newline();
			printSpace(ind);
		}
		else
		{
			if (c.getLeft() != null)
				printRecursive(c.getLeft(), ind);
			if (c.getRight() != null)
				printRecursive(c.getRight(), ind);

		}
	}

	private void printVariableAssignment(Cons c, int ind)
	{
		echo("set");
		space();
		echo(c.getRight().getLeft().getValue().getPureVal2());
		echo("=");
		printExpression(c.getRight().getRight().getRight(), ind);
	}

	private void printMain(Cons c, int ind)
	{
		echo("main {");
		newline();
		printSpace(tabLength);
		if (c.getRight().getRight().getLeft() != null)
		{
			printStatementList(c.getRight().getRight().getLeft(), ind + tabLength);
		}
		newline();
		echo("}");
	}

	private void printStatementList(Cons c, int ind)
	{
		printRecursive(c.getLeft(), ind);
		if (c.getRight() != null)
		{
			printStatementList(c.getRight(), ind);
		}
	}

	private void printWhileStatement(Cons c, int ind)
	{
		echo("while(");
		printExpression(c.getRight().getRight().getLeft(), ind);
		echo(")");
		space();
		printRegularBlock(c.getRight().getRight().getRight().getRight(), ind);
	}

	private void printForStatement(Cons c, int ind)
	{
		echo("for(");
		printVariableDeclaration(c.getRight().getRight().getLeft(), ind);
		echo(";");
		printExpression(c.getRight().getRight().getRight().getRight().getLeft(), ind);
		echo(";");
		printVariableAssignment(c.getRight().getRight().getRight().getRight().getRight().getRight().getLeft(), ind);
		echo(")");
		space();
		printRegularBlock(c.getRight().getRight().getRight().getRight().getRight().getRight().getRight().getRight(), ind);
	}

	private void printRegularStatement(Cons c, int ind)
	{
		if (c.getLeft() != null)
		{
			echo("return");
			space();
		}
		printExpression(c.getRight(), ind);
	}

	private void printVariableDeclaration(Cons c, int ind)
	{
		echo("var");
		space();
		echo(c.getRight().getLeft().getValue().getPureVal2()); // nameOfVariable
																// (firstIDfield)
		if (c.getRight().getRight() != null)
			if (c.getRight().getRight().getLeft().getType().equals(LexemeTypes.ASSIGN))
			{
				echo("=");
				printExpression(c.getRight().getRight().getRight(), ind);
			}
			else if (c.getRight().getRight().getLeft().getType().equals(LexemeTypes.ID))
			{
				space();
				echo(c.getRight().getRight().getLeft().getValue().getPureVal2()); // secondIDfield
				echo("(");
				if (c.getRight().getRight().getRight().getRight().getLeft() != null)
					printAttributeList(c.getRight().getRight().getRight().getRight().getLeft(), ind);
				echo(")");
			}
	}

	private void printAttributeList(Cons c, int ind)
	{
		echo(c.getLeft().getLeft().getValue().getPureVal2()); // nameOfFirstAttribute
		echo("=");
		printPrimary(c.getLeft().getRight().getRight(), ind);
		if (c.getRight() != null)
		{
			echo(",");
			printAttributeList(c.getRight().getRight(), ind);
		}
	}

	private void printPrimary(Cons c, int ind)
	{
		if (c.getType().equals(LexemeTypes.INTEGER) || c.getType().equals(LexemeTypes.STRING) || c.getType().equals(LexemeTypes.ID))
		{
			echo(c.getValue().getPureVal2());
		}
		else if (c.getType().equals(ConsTypes.PRIMARY))
		{
			if (c.getRight().getLeft().getType().equals(ConsTypes.EXPRESSION))
			{
				echo("(");
				printExpression(c.getRight().getLeft(), ind);
				echo(")");
			}
			else
			{
				echo(c.getLeft().getValue().getPureVal2() + "." + c.getLeft().getLeft().getValue().getPureVal2());
			}
		}
		else if (c.getType().equals(ConsTypes.FUNCTIONCALL))
		{
			printFunctionCall(c, ind);
		}
	}

	private void printFunctionCall(Cons c, int ind)
	{
		echo("call");
		space();
		echo(c.getRight().getLeft().getValue().getPureVal2()); // firstIDincall
		if (c.getRight().getRight().getLeft().getType().equals(LexemeTypes.OPAREN))
		{
			echo("(");
			if (c.getRight().getRight().getRight().getLeft() != null)
			{
				printArgumentList(c.getRight().getRight().getRight().getLeft(), ind);
			}
			echo(")");
		}
		else
		{ // DOT
			echo(".");
			echo(c.getRight().getRight().getRight().getLeft().getValue().getPureVal2()); // secondIDincall
			echo("(");
			if (c.getRight().getRight().getRight().getRight().getRight().getLeft() != null)
			{
				printArgumentList(c.getRight().getRight().getRight().getRight().getRight().getLeft(), ind);
			}
			echo(")");
		}
	}

	private void printArgumentList(Cons c, int ind)
	{
		printExpression(c.getLeft(), ind);
		if (c.getRight() != null)
		{
			echo(",");
			printArgumentList(c.getRight().getRight(), ind);
		}
	}

	private void printIfStatement(Cons c, int ind)
	{
		echo("if(");
		printExpression(c.getRight().getRight().getLeft(), ind);
		echo(")");
		space();
		printRegularBlock(c.getRight().getRight().getRight().getRight().getLeft(), ind);
		if (c.getRight().getRight().getRight().getRight().getRight() != null)
		{ // else?
			space();
			echo("else");
			space();
			if (c.getRight().getRight().getRight().getRight().getRight().getRight().getType().equals(ConsTypes.IFSTATEMENT))
			{ // if
				printIfStatement(c.getRight().getRight().getRight().getRight().getRight().getRight(), ind);
			}
			else
			{ // regularElse
				printRegularBlock(c.getRight().getRight().getRight().getRight().getRight().getRight(), ind);
			}
		}
	}

	private void printRegularBlock(Cons c, int ind)
	{
		echo("{");
		newline();
		printSpace(ind + tabLength);
		printRecursive(c.getRight().getLeft(), ind + tabLength);
		newline();
		printSpace(ind);
		echo("}");
	}

	private void printExpression(Cons c, int ind)
	{
		printPrimary(c.getLeft(), ind);
		if (c.getRight() != null)
		{
			printOperator(c.getRight().getLeft(), ind);
			printExpression(c.getRight().getRight(), ind);
		}
	}

	private void printOperator(Cons c, int ind)
	{
		if (c.getType().equals(LexemeTypes.ADD))
			echo("+");
		else if (c.getType().equals(LexemeTypes.SUBTRACT))
			echo("-");
		else if (c.getType().equals(LexemeTypes.MULTIPLY))
			echo("*");
		else if (c.getType().equals(LexemeTypes.DIVIDE))
			echo("/");
		else if (c.getType().equals(LexemeTypes.ASSIGN))
			echo("=");
		else if (c.getType().equals(LexemeTypes.LESSTHAN))
			echo("<");
		else if (c.getType().equals(LexemeTypes.GREATERTHAN))
			echo(">");
		else if (c.getType().equals(LexemeTypes.DOT))
			echo(".");
	}

	private void printFunctionDeclaration(Cons c, int ind)
	{
		echo("function");
		space();
		echo(c.getRight().getLeft().getValue().getPureVal2()); // functionName
		echo("(");
		if (c.getRight().getRight().getRight().getLeft() != null) // parameterList
			printParameterList(c.getRight().getRight().getRight().getLeft(), ind);
		echo(")");
		space();
		echoln("{");
		printSpace(ind + tabLength);
		if (c.getRight().getRight().getRight().getRight().getRight().getRight().getLeft() != null)
		{ // statementList
			printRecursive(c.getRight().getRight().getRight().getRight().getRight().getRight().getLeft(), ind + tabLength);
		}
		newline();
		printSpace(ind);
		echo("}");
	}

	private void printParameterList(Cons c, int ind)
	{
		echo(c.getLeft().getValue().getPureVal2());
		if (c.getRight() != null)
		{
			echo(",");
			printParameterList(c.getRight().getRight(), ind);
		}
	}

	private void printDeclarationBlock(Cons c, int ind)
	{
		echoln("{");
		printSpace(ind + tabLength);
		if (c.getRight().getLeft() != null)
		{ // structList
			printRecursive(c.getRight().getLeft(), ind + tabLength);
		}
		newline();
		printSpace(ind);
		echo("}");
	}

	private void printClassDeclaration(Cons c, int ind)
	{
		echo("class");
		space();
		echo(c.getRight().getLeft().getValue().getPureVal2()); // class name
		if (c.getRight().getRight().getLeft() != null)
		{ // extends?
			space();
			echo("extends");
			space();
			echo(c.getRight().getRight().getLeft().getRight().getValue().getPureVal2()); // extended class name
		}
		space();
		printDeclarationBlock(c.getRight().getRight().getRight(), ind); // declarationBlock
	}

	private void printSpace(int ctr)
	{
		for (int i = 0; i < ctr; i++)
		{
			echo(" ");
		}
	}

	private void space()
	{
		echo(" ");
	}

	private void newline()
	{
		sb.append("\n");
		// System.out.println();
	}

	private void echo(String s)
	{
		sb.append(s);
		// System.out.print(s);
	}

	private void echoln(String s)
	{
		sb.append(s + "\n");
		// System.out.println(s);
	}

	public String getString()
	{
		return sb.toString();
	}

}
