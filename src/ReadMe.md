+ Node.java is used to represent formulas and propositions.
+ CNFEval.java is used to reduce and evaluate the CNF statement
+ Encoder.java is used to handle file reading and writing and wrap the logic in CNFEval
+ Main contains the logic to build the CNF statement of the puzzle problem, it's also the entrance of the puzzle problem
  + To convert the puzzle to DIMACS format input file
    + Use "java -jar LCS1.jar [path]". [Path] is the file path.
  + To convert and evaluate the puzzle , and then write results to output file directly
    + Use "java -jar LCS1.jar [method] [path]".[method] is the chosen heuristic way for choosing propositions and truth values, it can be "random", "2clause", or "opt". [Path] is the file path.
      + For interim project, only set the method as "2clause". 
  + To read the input file, evaluate CNF, and then write results to the output file
    + Use "java -jar LCS1.jar [method] [input] [output]". [method] is the chosen heuristic way for choosing propositions and truth values, it can be "random", "2clause", or "opt". [input] is the input file path. [output] is the output file path.
      + For interim project, only set the method as "2clause". 
+ Exp contains the code related to running experiment. 
  + To run this experiment, either using an IDE or making a new jar with Exp as the program entrance.