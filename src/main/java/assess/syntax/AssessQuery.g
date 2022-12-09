grammar AssessQuery;
options { language = Java; }

parse : query EOF;

query : WITH target_cube
        (FOR selection_predicates)?
        BY group_by_set
        ASSESS measurement
        (AGAINST benchmark)?
        (USING comparison_function)?
        LABELS labeling_function
      ;

target_cube : ID;

selection_predicates : expr (',' expr)* ;
expr : ID '=' '\'' (ID+|date) '\'';
date : INT('/' INT ('/' INT)? )? ;

group_by_set : ID (',' ID)*;

measurement : ID;

benchmark : constant_benchmark
          | external_benchmark
          | sibling_benchmark
          | past_benchmark
          ;

constant_benchmark : SIGN? (INT | FLOAT);
external_benchmark : ID '.' ID;
sibling_benchmark : expr;
past_benchmark : PAST INT;

comparison_function : ID '(' comparison_args ')';

comparison_args : ID ',' ( ('benchmark.')? ID | INT)
                | comparison_function
                ;

labeling_function : ID
                  | '{' label_term (',' label_term)* '}'
                  ;

label_term : label_range ':' ID;
label_range : ('['|'(') range_point ',' range_point (')'|']');
range_point : SIGN? (INT|FLOAT|'inf');

// -- LEXER PART

// Fragments
fragment A : ('A'|'a');
fragment B : ('B'|'b');
fragment E : ('E'|'e');
fragment F : ('F'|'f');
fragment G : ('G'|'g');
fragment H : ('H'|'h');
fragment I : ('I'|'i');
fragment L : ('L'|'l');
fragment N : ('N'|'n');
fragment O : ('O'|'o');
fragment P : ('P'|'p');
fragment R : ('R'|'r');
fragment S : ('S'|'s');
fragment T : ('T'|'t');
fragment U : ('U'|'u');
fragment W : ('W'|'w');
fragment Y : ('Y'|'y');

// Keywords
AGAINST : A G A I N S T;
ASSESS : A S S E S S;
BY : B Y;
FOR : F O R;
LABELS : L A B E L S;
PAST : P A S T;
USING : U S I N G;
WITH : W I T H;

// LEXICAL TOKENS
SIGN : ('+' | '-');
ID : ('a'..'z'|'A'..'Z')+;
INT : '0'..'9'+;
FLOAT : '0'..'9'+ '.' '0'..'9'+;
WS : (' '|'\t'|'\n'|'\r') {skip();} ;
