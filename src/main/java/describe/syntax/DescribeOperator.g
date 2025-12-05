grammar DescribeOperator;

options {
    output = Java;
}

tokens {
    DESCRIBE; WITH; FOR; GROUP; BY; JOIN; ON; IN; AS; AND;
    AGGRFUNC; COMMA; EQUAL; LPARENTHESIS; RPARENTHESIS; LBRACE; RBRACE;
    PLUS; MINUS; STAR; SLASH;
    GT; LT; GTE; LTE; NEQ;
    TEXTVALUE; NUMBER; LETTER; WORD; WS; DOT;
}

@header {
    package describe.syntax;
    import describe.DescribeQuery;
}

@lexer::header {
    package describe.syntax;
}

@parser::members {
	
	//Single object to hold everything instead of using multiple ArrayLists and/or Hashmaps here
    private DescribeQuery query = new DescribeQuery();    
    
    public DescribeQuery getQuery(){
    	return query;   
    }
}

start: {
    query = new DescribeQuery();
} parse;

parse
    :   singleStatement 
        (
            JOIN 
            { 
               query.setJoined(true);
               query.setJoinType("JOIN");
            }
            singleStatement 
            ON joinCol=WORD 
            { 
               query.setJoinCondition($joinCol.text);
            }
        )*
    ;

singleStatement
    :   WITH cubeName 
        DESCRIBE measureList 
        (FOR sigmaExpressionsList)? 
        (GROUP BY gammaExpressionsList)?
        (ORDER BY orderExpressionsList)?
        (USING usingList)?
        (AS queryAlias)?
    ;


cubeName
    :   WORD { 
    		if(query.getCubeName().equals("")){
    			query.setCubeName($WORD.text);
    		}else{
    			query.appendToCubeName(" JOIN " + $WORD.text);
    		}
        }
    ;


measureList
    :   measureExpression (COMMA measureExpression)*
    ;

measureExpression
    : 
        expression { 
            query.addMeasure($expression.text);
        }
    ;

expression
    :   term ((PLUS | MINUS) term)*
    ;

term
    :   factor ((STAR | SLASH) factor)*
    ;

factor
    :   WORD
    |   NUMBER
    |   AGGRFUNC LPARENTHESIS expression RPARENTHESIS 		// SUM(Sales-Cost)
    |   LPARENTHESIS expression RPARENTHESIS
    ;


sigmaExpressionsList
    :   sigmaExpression (AND sigmaExpression)*
    ;

sigmaExpression
    :   //Year = 2020
        w=WORD op=comparator v=val {
            query.addSigmaExpression($w.text + $op.text + $v.text);
            query.addSigmaValue($w.text, $v.text);
        }
    |   //Region IN {A, B}
        w=WORD IN LBRACE vList=valueList RBRACE {
            query.addSigmaExpression($w.text + " IN {" + $vList.text + "}");
            query.addSigmaValue($w.text, $vList.text);
        }
    |   //Region WITH ...
        w=WORD WITH nested=nestedFilter {
            query.addSigmaExpression($w.text + " WITH " + $nested.text);
            //Stores the whole nested condition as "value"
            query.addSigmaValue($w.text, $nested.text);
        }
    ;

nestedFilter
    :   WORD comparator val
    ;

valueList
    :   val (COMMA val)*
    ;


gammaExpressionsList
    :   gammaExpression (COMMA gammaExpression)*
    ;

gammaExpression
    :   WORD { query.addGammaExpression($WORD.text); }
    ;

orderExpressionsList
	:	orderExpression (COMMA orderExpression)*
	;

orderExpression
	:	w=WORD (dir=sortDirection)? {
			String direction = ($dir.text == null) ? "" : " " + $dir.text;
			query.addOrderExpression($w.text + direction);
		}
	;

sortDirection
	:	ASC | DESC
	;


usingList
	:	modelName (COMMA modelName)*
	;

modelName
	:	WORD { query.addModel($WORD.text); }
	;


queryAlias
	: 	WORD { query.setQueryAlias($WORD.text); }
	;

comparator returns [String text]
    : EQUAL { $text = "="; } | GT { $text = ">"; } | LT { $text = "<"; } 
    | GTE { $text = ">="; } | LTE { $text = "<="; } | NEQ { $text = "!="; };

val returns [String text]
    : WORD { $text = $WORD.text; } 
    | NUMBER { $text = $NUMBER.text; } 
    | TEXTVALUE { $text = $TEXTVALUE.text; };



DESCRIBE: 'DESCRIBE';
WITH: 'WITH';
FOR: 'FOR';
GROUP: 'GROUP';
BY: 'BY';
JOIN: 'JOIN';
ON: 'ON';
IN: 'IN';
AND: 'AND';
AS: 'AS';
ORDER: 'ORDER';
USING: 'USING';
ASC: 'ASC';
DESC: 'DESC';

AGGRFUNC: ('MIN'|'MAX'|'SUM'|'AVG'|'CNT');

PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';

EQUAL: '=';
GT: '>';
LT: '<';
GTE: '>=';
LTE: '<=';
NEQ: '!=';

LPARENTHESIS: '(';
RPARENTHESIS: ')';
LBRACE: '{';
RBRACE: '}';
COMMA: ',';
DOT: '.';

WORD: (LETTER | '_' | DIGIT)+ (DOT (LETTER | '_' | DIGIT)+)*; 

TEXTVALUE: '\''(LETTER|DIGIT|'_'|'/'|'-'|' '|'.')+ '\'';
NUMBER: '\''? ('-')? (DIGIT)+ ('.' (DIGIT)+)? '\''?;

fragment DIGIT: '0'..'9';
fragment LETTER: 'a'..'z' | 'A'..'Z';
WS: (' ' | '\t' | '\r'| '\n'|'\r\n'|'\f') {$channel=HIDDEN;};