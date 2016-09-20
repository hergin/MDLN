/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.recognizer;

public class EndOfInputException extends Exception {

	public EndOfInputException() {
		super();
	}
	
	public void print() {
		System.out.println("EndOfInput: " + "No more lexemes left");
	}
	
}
