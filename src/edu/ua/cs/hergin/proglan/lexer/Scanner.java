/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.lexer;

public class Scanner {

	String fileName;

	public Scanner(String f) {
		fileName = f;
	}

	/*
	 * classic scanner. read the tokens in file until EOF
	 */
	public void scanner() {

		Lexeme token;
		Lexer lexer = new Lexer(fileName);

		do {
			token = lexer.lex();
			System.out.println(token);
		} while (!token.getType().equals(LexemeTypes.ENDofINPUT));

	}

	public static void main(String[] args) {
		if (args.length > 0) {
			new Scanner(args[0]).scanner();
		} else {
			System.out.println("You must give filename as an argument!");
		}

	}

}
