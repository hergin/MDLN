package edu.ua.cs.hergin.proglan.evaluator.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.evaluator.Evaluator;

public class EvaluatorTest {

	@Test
	public void test() {
		Evaluator evaluator = new Evaluator("testFiles/RPNcalculator.mdln");
		evaluator.evaluate();
		System.out.println();
	}

}
