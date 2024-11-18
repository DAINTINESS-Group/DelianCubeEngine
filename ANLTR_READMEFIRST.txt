In order to run a new AntLR grammar, you move the antlr jar file into the correct path
that includes the g file with the grammar and then run the following command at the command prompt:

java -cp antlr-3.4-complete.jar org.antlr.Tool AnalyzeOperator.g,

where AnalyzeOperator is the AntLR grammar.