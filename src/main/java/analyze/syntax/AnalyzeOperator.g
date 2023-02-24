grammar AnalyzeOperator;

options {
	output = Java;
}

tokens{ANALYZE;AND;AS;AGGRFUNC;FROM;FOR;GROUP;BY;COMMA;EQUAL;LPARENTHESIS;RPARENTHESIS;TEXTVALUE;NUMBER;LETTER;WORD;WS;}

@header{
	package analyze.syntax;
	
	import java.util.ArrayList;
	import java.util.HashMap;
}

@parser::members{
	String aggrFunc;
	String measure;
	String cubeName;
	ArrayList<String> sigmaExpressions;
	HashMap<String,String> sigmaExpressionsValues;
	ArrayList<String> gammaExpressions;
	String queryAlias;
}

@lexer::header {
	package parsemgr;
}

start:{
	sigmaExpressions = new ArrayList<String>();
	sigmaExpressionsValues = new HashMap<String,String>();
	gammaExpressions = new ArrayList<String>();
}parse;

parse: ANALYZE aggrfunc LPARENTHESIS measure RPARENTHESIS FROM cubeName FOR sigmaExpressions GROUP BY gammaExpressions AS queryAlias;

aggrfunc:AGGRFUNC{aggrFunc=$AGGRFUNC.text;};

measure: WORD{measure=$WORD.text;};

cubeName: WORD{cubeName=$WORD.text;};

sigmaExpressions: sigmaExpression(AND sigmaExpression)*;

sigmaExpression: sigmaExpressionNumberValue|sigmaExpressionTextValue;	

sigmaExpressionNumberValue: WORD EQUAL NUMBER {sigmaExpressions.add($WORD.text);sigmaExpressionsValues.put($WORD.text,$NUMBER.text);};

sigmaExpressionTextValue: WORD EQUAL TEXTVALUE {sigmaExpressions.add($WORD.text);sigmaExpressionsValues.put($WORD.text,$TEXTVALUE.text);};	

gammaExpressions: gammaExpression (COMMA gammaExpression)*;

gammaExpression: WORD{gammaExpressions.add($WORD.text);};

queryAlias: WORD{queryAlias=$WORD.text;};	


ANALYZE: A N A L Y Z E;

AND:A N D;

AS: A S;	

FROM: F R O M;

FOR: F O R;

GROUP:G R O U P;

BY: B Y;

AGGRFUNC:(M I N|M A X|S U M|A V G|C N T);

COMMA: ',';

EQUAL: '=';

LPARENTHESIS: '(';

RPARENTHESIS: ')';		 

WORD: (LETTER | '_'|DIGIT)+;

TEXTVALUE: '\''(LETTER|'_'|'/'|'-'|' ')+ '\'';

NUMBER: '\'''-'?(DIGIT|'-')+('.'(DIGIT|'-')+)?'\'' ;

fragment DIGIT :  '0'..'9';
fragment LETTER :  'a'..'z' |  'A'..'Z';
fragment A:('A'|'a');
fragment B:('B'|'b');
fragment C:('C'|'c');
fragment D:('D'|'d');
fragment E:('E'|'e');
fragment F:('F'|'f');
fragment G:('G'|'g');
fragment H:('H'|'h');
fragment I:('I'|'i');
fragment J:('J'|'j');
fragment K:('K'|'k');
fragment L:('L'|'l');
fragment M:('M'|'m');
fragment N:('N'|'n');
fragment O:('O'|'o');
fragment P:('P'|'p');
fragment Q:('Q'|'q');
fragment R:('R'|'r');
fragment S:('S'|'s');
fragment T:('T'|'t');
fragment U:('U'|'u');
fragment V:('V'|'v');
fragment W:('W'|'w');
fragment X:('X'|'x');
fragment Y:('Y'|'y');
fragment Z:('Z'|'z');

WS: (' ' | '\t' | '\r'| '\n'|'\r\n'|'\f') {$channel=HIDDEN;};