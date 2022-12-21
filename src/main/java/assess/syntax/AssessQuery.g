grammar AssessQuery;
options { language = Java; }

@lexer::header {
package assess.syntax;
}

@parser::header {
package assess.syntax;
import assess.AssessQuery;
import java.util.Arrays;
}

parse returns [AssessQuery parsedQuery]
    : result=query EOF {parsedQuery = result;};

query returns [AssessQuery query]
    @after {
    String parsedTargetCube = $targetCube.text;
    String parsedMeasurement = $measurement.text;

    query = new AssessQuery(parsedTargetCube, parsedGammas, parsedMeasurement);
    }
    : WITH targetCube = ID
      (FOR selection_predicates)?
      BY gammas = group_by_set
      ASSESS measurement = ID
      (AGAINST benchmark)?
      (USING comparison_function)?
      LABELS labeling_function
    ;

selection_predicates
    : level (',' level)*
    ;

level returns [List sigma]
    : ID '=' '\'' value = level_value '\'' ;

level_value returns [String value]
    : ids += ID+
    | date {value = $date.text; }
    ;

date : INT '/' INT ('/' INT)? ;

group_by_set : ID  (',' ID)*;

benchmark
    : constant_benchmark
    | external_benchmark
    | sibling_benchmark
    | past_benchmark
    ;

constant_benchmark
    : SIGN? (INT | FLOAT);

external_benchmark
    : ID '.' ID;

sibling_benchmark : level;

past_benchmark
    : PAST INT;

comparison_function : ID '(' comparison_args ')';

comparison_args : ID ',' ( ('benchmark.')? ID | INT)
                | comparison_function
                ;

labeling_function
    : name = ID
    | '{' term = label_term (',' label_term)* '}'
    ;

label_term returns [List term]
    : range=label_range ':' label=ID ;

label_range
    : start_limit = ( '['|'(' )
      start = range_point ','
      end = range_point
      end_limit = (')'|']')
    ;

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

// Keywords (case-insensitive)
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
FLOAT : INT '.' INT;

WS : (' '|'\t'|'\n'|'\r') {skip();} ;
