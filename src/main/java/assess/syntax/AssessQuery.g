grammar AssessQuery;
options { language = Java; }

parse : query EOF;

query : 'with' ID
        ('for' selection_predicates)?
        'by' group_by_set
        'assess' ID
        ('against' benchmark)?
        ('using' comparison_function)?
        'labels' labeling_function
      ;

selection_predicates : expr (',' expr)* ;
expr : ID '=' '\'' (ID+|date) '\'';
date : INT('/'INT)* ; // TODO: This could be an error if more than 3 ints are given

group_by_set : ID (',' ID)*;

benchmark : SIGN? INT | FLOAT // constant benchmark
          | ID.ID       // external benchmark
          | expr        // sibling benchmark
          | 'past' INT  // past benchmark
          ;

comparison_function : ID '(' ID ',' (('benchmark.')? ID|INT)')'
                    | ID '(' comparison_function ')'
                    ;

labeling_function : ID
                  | '{' label_term (',' label_term)* '}'
                  ;

label_term : label_range ':' ID;
label_range : ('['|'(') range_point ',' range_point (')'|']');
range_point : SIGN? (INT|FLOAT|'inf');

// LEXICAL TOKENS
SIGN : ('+' | '-');
ID : ('a'..'z'|'A'..'Z')+;
INT : '0'..'9'+;
FLOAT : '0'..'9'+ '.' '0'..'9'+;
WS : (' '|'\t'|'\n'|'\r') {skip();} ;
