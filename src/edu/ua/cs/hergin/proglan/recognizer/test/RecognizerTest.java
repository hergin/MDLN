package edu.ua.cs.hergin.proglan.recognizer.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ua.cs.hergin.proglan.recognizer.Recognizer;

public class RecognizerTest {

	@Test
	public void testEmptyFile() {
		System.out.println("File: testFiles/recognizerTest-emptyFile.mdln");
		Recognizer r1 = new Recognizer("testFiles/recognizerTest-emptyFile.mdln"); // should print "File is empty!"
		System.out.println("SHOULD BE: File is empty!");
		System.out.println("----------------");
	}
	
	@Test
	public void testShortestCode() {
		System.out.println("File: testFiles/recognizerTest-shortestCode.mdln");
		Recognizer r2 = new Recognizer("testFiles/recognizerTest-shortestCode.mdln");
		r2.recognize(); // should print "Source code fits to the grammar!"
		System.out.println("SHOULD BE: Source code fits to the grammar!");
		System.out.println("----------------");
	}
	
	@Test
	public void testFailCode1() {
		System.out.println("File: testFiles/recognizerTest-FailCode1.mdln");
		Recognizer r3 = new Recognizer("testFiles/recognizerTest-FailCode1.mdln");
		r3.recognize(); // should print "More unnecessary code under main!"
		System.out.println("SHOULD BE: More unnecessary code under main!");
		System.out.println("----------------");
	}
	
	@Test
	public void testSuccessfulCode() {
		System.out.println("File: testFiles/recognizerTest-successfulCode.mdln");
		Recognizer r3 = new Recognizer("testFiles/recognizerTest-successfulCode.mdln");
		r3.recognize(); // should print "Source code fits to the grammar!"
		System.out.println("SHOULD BE: Source code fits to the grammar!");
		System.out.println("----------------");
	}
	
	@Test
	public void testSuccessfulCode2() {
		System.out.println("File: testFiles/recognizerTest-successfulCode2.mdln");
		Recognizer r3 = new Recognizer("testFiles/recognizerTest-successfulCode2.mdln");
		r3.recognize(); // should print "Source code fits to the grammar!"
		System.out.println("SHOULD BE: Source code fits to the grammar!");
		System.out.println("----------------");
	}
	
	@Test
	public void testSuccessfulCode3() {
		System.out.println("File: testFiles/recognizerTest-successfulCode3.mdln");
		Recognizer r3 = new Recognizer("testFiles/recognizerTest-successfulCode3.mdln");
		r3.recognize(); // should print "Source code fits to the grammar!"
		System.out.println("SHOULD BE: Source code fits to the grammar!");
		System.out.println("----------------");
	}
	
	@Test
	public void testVeryLargeSource() {
		System.out.println("File: testFiles/recognizerTest-veryLargeSource.mdln");
		Recognizer r3 = new Recognizer("testFiles/recognizerTest-veryLargeSource.mdln");
		r3.recognize(); // should print "Source code fits to the grammar!"
		System.out.println("SHOULD BE: Source code fits to the grammar!");
		System.out.println("----------------");
	}
	
	@Test
	public void testFullCode() {
		System.out.println("File: testFiles/FullCode.mdln");
		Recognizer r3 = new Recognizer("testFiles/FullCode.mdln");
		r3.recognize(); // should print "Source code fits to the grammar!"
		System.out.println("SHOULD BE: Source code fits to the grammar!");
		System.out.println("----------------");
	}

}
