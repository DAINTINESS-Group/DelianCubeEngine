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
    package intentional.describe.syntax;
    import intentional.describe.DescribeParams;
}

@lexer::header {
    package intentional.describe.syntax;
}

@parser::members {
	
	//Single object to hold everything instead of using multiple ArrayLists and/or Hashmaps here
    private DescribeParams params = new DescribeParams();    
    
    public DescribeParams getParams(){
    	return params;   
    }
}

start: {
    params = new DescribeParams();
} parse;

parse
    :   singleStatement 
        (
            JOIN 
            { 
               params.setJoined(true);
               params.setJoinType("JOIN");
            }
            singleStatement 
            ON joinCol=WORD 
            { 
               params.setJoinCondition($joinCol.text);
            }
        )*
        EOF
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
    		if(params.getCubeName().equals("")){
    			params.setCubeName($WORD.text);
    		}else{
    			params.appendToCubeName(" JOIN " + $WORD.text);
    		}
        }
    ;


measureList
    :   measureExpression (COMMA measureExpression)*
    ;

measureExpression
    : 	
    	expr=expression
    	
    	//Added the greedy behavior because the parser seems to not know whether the token AS followed by a WORD belongs to the measureExpression or the singleStatement
    	( options {greedy=true;} : AS alias=WORD )?
    	{
    		if ($alias != null){
    			params.addMeasure($expr.text + " AS " + $alias.text);
    		}else{
    			params.addMeasure($expr.text);
   			}
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
            params.addSigmaExpression($w.text + $op.text + $v.text);
            params.addSigmaValue($w.text, $v.text);
        }
    |   //Region IN {A, B}
        w=WORD IN LBRACE vList=valueList RBRACE {
            params.addSigmaExpression($w.text + " IN {" + $vList.text + "}");
            params.addSigmaValue($w.text, $vList.text);
        }
        //Region NOT IN {A, B}
    |   w=WORD NOT IN LBRACE vList=valueList RBRACE {
            params.addSigmaExpression($w.text + " NOT IN {" + $vList.text + "}");
            params.addSigmaValue($w.text, $vList.text);
        }
    |   //Region WITH ...
        w=WORD WITH nested=nestedFilter {
            params.addSigmaExpression($w.text + " WITH " + $nested.text);
            //Stores the whole nested condition as "value"
            params.addSigmaValue($w.text, $nested.text);
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
    :   WORD { params.addGammaExpression($WORD.text); }
    ;

orderExpressionsList
	:	orderExpression (COMMA orderExpression)*
	;

orderExpression
	:	w=WORD (dir=sortDirection)? {
			String direction = ($dir.text == null) ? "" : " " + $dir.text;
			params.addOrderExpression($w.text + direction);
		}
	;

sortDirection
	:	ASC | DESC
	;


usingList
	:	modelName (COMMA modelName)*
	;

modelName
	:	WORD { params.addModel($WORD.text); }
	;


queryAlias
	: 	WORD { params.setQueryAlias($WORD.text); }
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
NOT: 'NOT';
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