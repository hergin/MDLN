#
#  Huseyin Ergin - hergin@crimson.ua.edu
#
default: targetList

compile:
	@javac -d bin -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar -sourcepath src src/edu/ua/cs/hergin/proglan/evaluator/Evaluator.java
	@echo "Compiled!"

rpn: compile
	@echo "============================"
	@echo "RPN Calculator (SOURCE CODE)"
	@echo "============================"
	@cat testFiles/RPNcalculator.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/RPNcalculator.mdln
	@echo "\n====================="

fibonacci: compile
	@echo "============================"
	@echo "FIBonacci (SOURCE CODE)"
	@echo "============================"
	@cat testFiles/fibonacci.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/fibonacci.mdln
	@echo "\n====================="

recursion: fibonacci

iteration: factorial

factorial: compile
	@echo "======================="
	@echo "Factorial (SOURCE CODE)"
	@echo "======================="
	@cat testFiles/factorial.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/factorial.mdln
	@echo "\n====================="

conditional: compile
	@echo "======================="
	@echo "Conditional (SOURCE CODE)"
	@echo "======================="
	@cat testFiles/conditional.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/conditional.mdln
	@echo "\n====================="

array: compile
	@echo "==================="
	@echo "Array (SOURCE CODE)"
	@echo "==================="
	@cat testFiles/arrays.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/arrays.mdln
	@echo "\n====================="

functionManipulation: compile
	@echo "=================================="
	@echo "FunctionManipulation (SOURCE CODE)"
	@echo "=================================="
	@cat testFiles/functionManipulation.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/functionManipulation.mdln
	@echo "\n====================="

inheritance: compile
	@echo "============================"
	@echo "Inheritance (SOURCE CODE)"
	@echo "============================"
	@cat testFiles/inheritance.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/inheritance.mdln
	@echo "\n====================="

extension: compile
	@echo "======================="
	@echo "Extension (SOURCE CODE)"
	@echo "======================="
	@cat testFiles/extension.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/extension.mdln
	@echo "\n====================="

reify: compile
	@echo "==================="
	@echo "Reify (SOURCE CODE)"
	@echo "==================="
	@cat testFiles/reify.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/reify.mdln
	@echo "\n====================="

variation: compile
	@echo "======================="
	@echo "Variation (SOURCE CODE)"
	@echo "======================="
	@cat testFiles/variation.mdln -n
	@echo "\n====================="
	@echo "Execute"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/variation.mdln
	@echo "\n====================="
	
object: variation

prettyprint:
	@javac -d bin -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar -sourcepath src src/edu/ua/cs/hergin/proglan/prettyprinter/PrettyPrinter.java
	@echo "======================================"
	@echo "ORIGINAL code of Obfuscated-RPN Calculator"
	@echo "======================================"
	@cat testFiles/Obfuscated-RPNcalculator.mdln -n
	@echo "\n====================="
	@echo "EXECUTE (RESULT OF PRETTY PRINT)"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.prettyprinter.PrettyPrinter testFiles/Obfuscated-RPNcalculator.mdln
	@echo "\n====================="

prettyprintAsAFunction: compile
	@echo "============================================="
	@echo "PrettyPrint as a builtin function SOURCE CODE"
	@echo "============================================="
	@cat testFiles/prettyPrintTest.mdln -n
	@echo "\n====================="
	@echo "EXECUTE"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.evaluator.Evaluator testFiles/prettyPrintTest.mdln
	@echo "\n====================="

detection:
	@javac -d bin -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar -sourcepath src src/edu/ua/cs/hergin/proglan/parser/ParserWithEarlyDetection.java
	@echo "======================================="
	@echo "ORIGINAL CODE (testFiles/FullCode.mdln)"
	@echo "======================================="
	@cat testFiles/FullCode.mdln -n
	@echo "\n====================="
	@echo "EXECUTE ParserWithEarlyDetection"
	@echo "====================="
	@java -cp bin:lib/cloning-1.8.5.jar:lib/objenesis-1.2.jar edu.ua.cs.hergin.proglan.parser.ParserWithEarlyDetection testFiles/FullCode.mdln
	@echo "\n====================="

grammar:
	@echo "=================================================================="
	@echo "GRAMMAR"
	@echo "=================================================================="
	@cat myFiles/grammar.txt
	@echo "\n=================================================================="

manual:
	@echo "=================================================================="
	@echo "MANUAL"
	@echo "=================================================================="
	@cat Manual.txt
	@echo "\n=================================================================="

clean:
	@rm -rf bin/*
	@rm -rf *~
	@echo "Removed class files!"

targetList:
	@echo "Available Targets"
	@echo "================="
	@grep '^[^#[:space:]].*:' Makefile
