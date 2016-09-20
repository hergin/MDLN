Huseyin Ergin - hergin@crimson.ua.edu
=====================================

TIP: When you run example with make, you will first see the source code of the program
then output of the program while execution.

Show the available targets of MAKE
    
    > make
    
Run RPN calculator (features ClassDecl, FunctionDecl, FuncCall, Object, Arrays, Input/Output, WhileLoop, Comments, IfElse)
  [enter your numbers using "enter" between each, write 'q' to get result]

    > make rpn

The targets in DPL spec page

    > make recursion
    > make iteration
    > make conditional
    > make array
    > make extension
    > make reify
    > make variation
    > make object

SOME OTHER SAMPLE PROGRAMS
==========================

Run Function Manipulation (function assignments etc)

    > make functionManipulation
    
Run Factorial (features Recursive functions, ForLoop, FuncCall, IfElse, I/O)

    > make factorial

Run Fibonacci (features Recursive functions, FuncCall, IfElse, I/O)

    > make fibonacci
    
Run Arrays (features Arrays, Comments, FuncCall)
  [O(1) arrays can be seen in Evaluator.java code between lines 126-154]

    > make array
    
Run Inheritance (features inheritance, ClassDecl, FuncDecl, FuncCall)

    > make inheritance
    
Show Grammar

    > make grammar
    
Pretty Print source code (RPN source code)

    > make prettyprint

Pretty Print as a function (to print defined functions in a style)

    > make prettyprintAsAFunction
    
Detection of variables before used

    > make detection

Show language manual

    > make manual
    
Clean class files

    > make clean
    
STANDALONE evaluator (sample source codes are in testFiles/ folder)

    > ./evaluator fileName

External libraries:
===================
--> Objenesis: http://objenesis.googlecode.com/svn/docs/index.html
--> Java Deep Cloning: http://code.google.com/p/cloning/

***NOTE: MAKEFILE assumes your OS has `javac`

***NOTE2: These scripts are all tested in Ubuntu 12.04

       
