/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.recognizer;

import edu.ua.cs.hergin.proglan.lexer.Lexeme;
import edu.ua.cs.hergin.proglan.lexer.LexemeTypes;

public class Recognizer {

	RecognizerUtil util;

	public Recognizer(String fileName) {

		try {
			util = new RecognizerUtil(fileName);
		} catch (EndOfInputException e) {
			System.out.println("File is empty!");
		}

	}

	public void recognize() {
		try {
			program();
		} catch (RecognizerException e) {
			e.print();
			// e.printStackTrace();
		} catch (EndOfInputException e) {
			System.out.println("Source code fits to the grammar!");
		}
	}

	private void program() throws RecognizerException, EndOfInputException {
		if (structListPending()) {
			structList();
		}
		mainClause();
		if (!util.check(LexemeTypes.ENDofINPUT)) {
			throw new RecognizerException("More unnecessary code under main!");
		}
	}

	private void mainClause() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.MAIN);
		util.match(LexemeTypes.OCURLY);
		if (statementListPending()) {
			statementList();
		}
		util.match(LexemeTypes.CCURLY);
	}

	private void structList() throws RecognizerException, EndOfInputException {
		struct();
		if (structListPending()) {
			structList();
		}
	}

	private void struct() throws RecognizerException, EndOfInputException {
		if (classDeclarationPending()) {
			classDeclaration();
		} else if (functionDeclarationPending()) {
			functionDeclaration();
		} else if (prototypeDeclarationPending()) {
			prototypeDeclaration();
		} else {
			variableDeclaration();
		}
	}

	private void prototypeDeclaration() throws RecognizerException,
			EndOfInputException {
		util.match(LexemeTypes.FORWARD);
		util.match(LexemeTypes.ID);
	}

	private boolean prototypeDeclarationPending() {
		return util.check(LexemeTypes.FORWARD);
	}

	private void variableDeclaration() throws RecognizerException,
			EndOfInputException {
		util.match(LexemeTypes.VAR);
		util.match(LexemeTypes.ID);
		if (util.check(LexemeTypes.ASSIGN)) {
			util.match(LexemeTypes.ASSIGN);
			expression();
		} else if (util.check(LexemeTypes.ID)) {
			util.match(LexemeTypes.ID);
			util.match(LexemeTypes.OPAREN);
			if (attributeListPending()) {
				attributeList();
			}
			util.match(LexemeTypes.CPAREN);
		}
	}

	private void attributeList() throws RecognizerException,
			EndOfInputException {
		attribute();
		if (util.check(LexemeTypes.COMMA)) {
			util.match(LexemeTypes.COMMA);
			attributeList();
		}
	}

	private void attribute() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.ID);
		util.match(LexemeTypes.ASSIGN);
		primary();
	}

	private void primary() throws RecognizerException, EndOfInputException {
		if (util.check(LexemeTypes.INTEGER)) {
			util.match(LexemeTypes.INTEGER);
		} else if (util.check(LexemeTypes.STRING)) {
			util.match(LexemeTypes.STRING);
		} else if (util.check(LexemeTypes.ID)) {
			util.match(LexemeTypes.ID);
		} else if (functionCallPending()) {
			functionCall();
		} else {
			util.match(LexemeTypes.OPAREN);
			expression();
			util.match(LexemeTypes.CPAREN);
		}
	}

	private void functionCall() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.CALL);
		util.match(LexemeTypes.ID);
		if (util.check(LexemeTypes.OPAREN)) {
			util.match(LexemeTypes.OPAREN);
			if (argumentListPending()) {
				argumentList();
			}
			util.match(LexemeTypes.CPAREN);
		} else {
			util.match(LexemeTypes.DOT);
			util.match(LexemeTypes.ID);
			util.match(LexemeTypes.OPAREN);
			if (argumentListPending()) {
				argumentList();
			}
			util.match(LexemeTypes.CPAREN);
		}
	}

	private void argumentList() throws RecognizerException, EndOfInputException {
		expression();
		if (util.check(LexemeTypes.COMMA)) {
			util.match(LexemeTypes.COMMA);
			argumentList();
		}
	}

	private boolean argumentListPending() {
		return expressionPending();
	}

	private boolean functionCallPending() {
		return util.check(LexemeTypes.CALL);
	}

	private boolean attributeListPending() {
		return attributePending();
	}

	private boolean attributePending() {
		return util.check(LexemeTypes.ID);
	}

	private boolean variableDeclarationPending() {
		return util.check(LexemeTypes.VAR);
	}

	private void functionDeclaration() throws RecognizerException,
			EndOfInputException {
		util.match(LexemeTypes.FUNCTION);
		util.match(LexemeTypes.ID);
		util.match(LexemeTypes.OPAREN);
		if (parameterListPending()) {
			parameterList();
		}
		util.match(LexemeTypes.CPAREN);
		util.match(LexemeTypes.OCURLY);
		if (statementListPending()) {
			statementList();
		}
		util.match(LexemeTypes.CCURLY);
	}

	private void statementList() throws RecognizerException,
			EndOfInputException {
		statement();
		if (statementListPending()) {
			statementList();
		}
	}

	private boolean statementListPending() {
		return statementPending();
	}

	private boolean statementPending() {
		return ifStatementPending() || forStatementPending()
				|| whileStatementPending() || regularStatementPending()
				|| variableDeclarationPending() || variableAssignmentPending();
	}

	private void statement() throws RecognizerException, EndOfInputException {
		if (ifStatementPending()) {
			ifStatement();
		} else if (forStatementPending()) {
			forStatement();
		} else if (whileStatementPending()) {
			whileStatement();
		} else if (variableDeclarationPending()) {
			variableDeclaration();
		} else if (variableAssignmentPending()) {
			variableAssignment();
		} else {
			regularStatement();
		}
	}

	private void variableAssignment() throws RecognizerException,
			EndOfInputException {
		util.match(LexemeTypes.SET);
		util.match(LexemeTypes.ID);
		util.match(LexemeTypes.ASSIGN);
		expression();
	}

	private boolean variableAssignmentPending() {
		return util.check(LexemeTypes.SET);
	}

	private void regularStatement() throws RecognizerException,
			EndOfInputException {
		if (returnPending()) {
			returnClause();
		}
		expression();
	}

	private void returnClause() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.RETURN);
	}

	private boolean returnPending() {
		return util.check(LexemeTypes.RETURN);
	}

	private boolean regularStatementPending() {
		return expressionPending() || util.check(LexemeTypes.RETURN);
	}

	private boolean expressionPending() {
		return primaryPending();
	}

	private boolean primaryPending() {
		return util.check(LexemeTypes.INTEGER)
				|| util.check(LexemeTypes.STRING) || util.check(LexemeTypes.ID)
				|| util.check(LexemeTypes.OPAREN) || functionCallPending();
	}

	private void whileStatement() throws RecognizerException,
			EndOfInputException {
		util.match(LexemeTypes.WHILE);
		util.match(LexemeTypes.OPAREN);
		expression();
		util.match(LexemeTypes.CPAREN);
		regularBlock();

	}

	private boolean whileStatementPending() {
		return util.check(LexemeTypes.WHILE);
	}

	private void forStatement() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.FOR);
		util.match(LexemeTypes.OPAREN);
		variableDeclaration();
		util.match(LexemeTypes.SEMICOLON);
		expression();
		util.match(LexemeTypes.SEMICOLON);
		variableAssignment();
		util.match(LexemeTypes.CPAREN);
		regularBlock();
	}

	private boolean forStatementPending() {
		return util.check(LexemeTypes.FOR);
	}

	private boolean ifStatementPending() {
		return util.check(LexemeTypes.IF);
	}

	private void ifStatement() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.IF);
		util.match(LexemeTypes.OPAREN);
		expression();
		util.match(LexemeTypes.CPAREN);
		regularBlock();
		if (elseClausePending()) {
			elseClause();
		}
	}

	private void elseClause() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.ELSE);
		if (regularBlockPending()) {
			regularBlock();
		} else {
			ifStatement();
		}
	}

	private boolean regularBlockPending() {
		return util.check(LexemeTypes.OCURLY);
	}

	private boolean elseClausePending() {
		return util.check(LexemeTypes.ELSE);
	}

	private void regularBlock() throws RecognizerException, EndOfInputException {
		util.match(LexemeTypes.OCURLY);
		statementList();
		util.match(LexemeTypes.CCURLY);
	}

	private void expression() throws RecognizerException, EndOfInputException {
		primary();
		if (operatorPending()) {
			operator();
			expression();
		}
	}

	private void operator() throws RecognizerException, EndOfInputException {
		if (util.check(LexemeTypes.ADD)) {
			util.match(LexemeTypes.ADD);
		} else if (util.check(LexemeTypes.SUBTRACT)) {
			util.match(LexemeTypes.SUBTRACT);
		} else if (util.check(LexemeTypes.MULTIPLY)) {
			util.match(LexemeTypes.MULTIPLY);
		} else if (util.check(LexemeTypes.DIVIDE)) {
			util.match(LexemeTypes.DIVIDE);
		} else if (util.check(LexemeTypes.ASSIGN)) {
			util.match(LexemeTypes.ASSIGN);
		} else if (util.check(LexemeTypes.LESSTHAN)) {
			util.match(LexemeTypes.LESSTHAN);
		} else if (util.check(LexemeTypes.GREATERTHAN)) {
			util.match(LexemeTypes.GREATERTHAN);
		} else {
			util.match(LexemeTypes.DOT);
		}
	}

	private boolean operatorPending() {
		return util.check(LexemeTypes.ADD) || util.check(LexemeTypes.SUBTRACT)
				|| util.check(LexemeTypes.MULTIPLY)
				|| util.check(LexemeTypes.DIVIDE)
				|| util.check(LexemeTypes.ASSIGN)
				|| util.check(LexemeTypes.LESSTHAN)
				|| util.check(LexemeTypes.GREATERTHAN)
				|| util.check(LexemeTypes.DOT);
	}

	private void parameterList() throws RecognizerException,
			EndOfInputException {
		util.match(LexemeTypes.ID);
		if (util.check(LexemeTypes.COMMA)) {
			util.match(LexemeTypes.COMMA);
			parameterList();
		}
	}

	private boolean parameterListPending() {
		return expressionPending();
	}

	private boolean functionDeclarationPending() {
		return util.check(LexemeTypes.FUNCTION);
	}

	private void classDeclaration() throws RecognizerException,
			EndOfInputException {
		util.match(LexemeTypes.CLASS);
		util.match(LexemeTypes.ID);
		if (extendsStatementPending()) {
			extendsStatement();
		}

		declarationBlock();

	}

	private void declarationBlock() throws RecognizerException,
			EndOfInputException {

		util.match(LexemeTypes.OCURLY);
		if (structListPending()) {
			structList();
		}
		util.match(LexemeTypes.CCURLY);

	}

	private void extendsStatement() throws RecognizerException,
			EndOfInputException {

		util.match(LexemeTypes.EXTENDS);
		util.match(LexemeTypes.ID);

	}

	private boolean extendsStatementPending() {
		return util.check(LexemeTypes.EXTENDS);
	}

	private boolean classDeclarationPending() {
		return util.check(LexemeTypes.CLASS);
	}

	private boolean structListPending() {
		return structPending();
	}

	private boolean structPending() {
		return classDeclarationPending() || functionDeclarationPending()
				|| variableDeclarationPending()
				|| prototypeDeclarationPending();
	}

}
