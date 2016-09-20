/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.recognizer;

public class RecognizerException extends Exception {

	String definition;

	public RecognizerException(String def) {
		super();
		this.definition = def;
	}

	public RecognizerException(int l, String expected, String taken) {
		super();
		this.definition = new String("Line: " + l + " -> Rule: "
				+ this.getStackTrace()[2].getMethodName() + "\nExpected type: "
				+ expected + ", but received type: " + taken);

	}

	public RecognizerException() {
		super();
		this.definition = new String("Unknown error!");
	}

	public String getError() {
		return definition;
	}

	public void print() {
		System.out.println("RECOGNIZER_EXCEPTION: " + definition);
	}

}
