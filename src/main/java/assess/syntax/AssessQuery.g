grammar AssessQuery;
options { language = Java; }

@lexer::header {
package assess.syntax;
}

@parser::header {
package assess.syntax;
import assess.AssessQuery;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import assess.AssessQueryBuilder;
}

parse returns [AssessQuery parsedQuery]
    : result=query EOF {parsedQuery = result;};

query returns [AssessQuery query]
    @init{AssessQueryBuilder builder = new AssessQueryBuilder();}
    : WITH targetCube = ID
      (FOR predicates = selection_predicates)?
      BY gammas = group_by_set
      ASSESS measurement = ID
      // Build the Target Cube Here
      {builder.buildTargetCube($targetCube.text, $measurement.text, predicates, gammas);}

      (AGAINST parsedBenchmark = benchmark)?
      // Build the Benchmark Cube Here

      (USING comparison_function)?
      // Build the Comparison Scheme Here

      LABELS (labelingMethod = ID | labelingSystem = custom_labeling)
      // Build the Labeling Scheme Here

    ;

selection_predicates returns [HashMap<String, String> selectionPredicates]
    @init{$selectionPredicates = new HashMap<>();}
    : parsed_predicate = predicate {$selectionPredicates.put($parsed_predicate.level, $parsed_predicate.value);}
    (',' additional_predicate = predicate {$selectionPredicates.put($additional_predicate.level, $additional_predicate.value);})*
    ;

predicate returns [String level, String value]
    : level_name = ID {$level = $level_name.text;}
    '=' '\'' val = level_value {$value = $val.text;} '\''
    ;

level_value : ID+ | date ;

date : INT '/' INT ('/' INT)? ;

group_by_set returns [HashSet<String> groupBySet]
    @init{$groupBySet = new HashSet<>();}
    : id=ID {$groupBySet.add($id.text);} (',' id=ID)* {$groupBySet.add($id.text);}
    ;

benchmark
    : result = constant_benchmark
    | result = external_benchmark
    | result = sibling_benchmark
    | result = past_benchmark
    ;

constant_benchmark : SIGN? (INT|FLOAT);

external_benchmark : ID '.' ID;

sibling_benchmark : predicate;

past_benchmark : PAST INT;

comparison_function : ID '(' comparison_args ')';

comparison_args
    : ID ',' ( ('benchmark.')? ID | INT)
    | comparison_function
    ;

custom_labeling returns [List<List<String>> labelingTerms]
    @init {labelingTerms = new ArrayList<List<String>>();}
    : '{' term = label_term {labelingTerms.add(term);}(',' term = label_term {labelingTerms.add(term);})* '}'
    ;

label_term returns [List<String> term]
    @after{term = range;}
    : range=label_range ':' label=ID {range.add($label.text); };

label_range returns [List<String> limits]
    @init {limits = new ArrayList<String>();}
    : ( '[' { $limits.add(">="); } | '(' { $limits.add(">"); })
      start = range_point { $limits.add($start.text); } ','
      end = range_point { $limits.add($end.text); }
      (')' { $limits.add("<"); }|']' { $limits.add("<="); })
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

WS : (' '|'\t'|'\n'|'\r') {$channel=HIDDEN;} ;
