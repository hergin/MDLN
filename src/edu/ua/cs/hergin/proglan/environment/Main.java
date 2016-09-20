/*
 * Huseyin Ergin - hergin@crimson.ua.edu
 */
package edu.ua.cs.hergin.proglan.environment;

public class Main {

	public static void main(String[] args) {
		
		print(">Creating new environment...\n");
		EnvironmentUtil env = new EnvironmentUtil();
		env.displayAll();
		
		print(">Adding variable x with value 3\n");
		env.putVar("x", 3);
		env.displayAll();
		
		print(">Extending the environment with y:4 and z:\"hello\"\n");
		env.extend("Global", new StringLexemePair("y", 4), new StringLexemePair("z", "hello"));
		env.displayAll();
		
		print(">Setting the value of x from 3 to 5");
		env.setVar("x", 5);
		env.displayAll();
		
		print(">Add new local environment");
		env.createNewEnv();
		env.displayAll();
		
		print(">Add x with value 12");
		env.putVar("x", 12);
		env.displayAll();
		
		print(">Setting the value of most local x from 12 to 15");
		env.setVar("x", 15);
		env.displayAll();
		
	}

	private static void print(String s) {
		System.out.println(s);
	}

}
