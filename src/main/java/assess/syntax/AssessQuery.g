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

@members {
   AssessQueryBuilder builder;
}

parse [AssessQueryBuilder builder] returns [AssessQuery parsedQuery]
    @init {this.builder = builder;}
    : result=query EOF {parsedQuery = result;};

query returns [AssessQuery query]
    @init{
    List<String> comparisonMethods = new ArrayList<String>();
    }
    : WITH targetCube = ID {builder.setTargetCubeName($targetCube.text);}
      (FOR predicates = selection_predicates {builder.setSelectionPredicates(predicates);})?
      BY gammas = group_by_set {builder.setGroupBySet(gammas);}
      ASSESS AGGREGATE {builder.setAggregationFunction($AGGREGATE.text);}
      '(' measurement = ID {builder.setMeasurement($measurement.text);} ')'

      (AGAINST parsedBenchmark = benchmark
      {builder.setBenchmarkDetails(parsedBenchmark);})?

      (USING updatedComparisonMethods = comparison_scheme[comparisonMethods]
      {builder.setDeltaFunctions(updatedComparisonMethods);})?

      // Build the Labeling Scheme Here
      LABELS (labelingMethod = ID
      {builder.buildLabelingScheme($labelingMethod.text);}
      | labelingSystem = custom_labeling
      {builder.setLabelingRules(labelingSystem);}
      )
      {$query = builder.build();}
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

date : INT ('-' INT ('-' INT)?)?  ;

group_by_set returns [HashSet<String> groupBySet]
    @init{$groupBySet = new HashSet<>();}
    : id=ID {$groupBySet.add($id.text);} (',' id=ID)* {$groupBySet.add($id.text);}
    ;

benchmark returns [List<String> parsedBenchmark]
    @init{$parsedBenchmark = new ArrayList<>();}
    : constant_benchmark
    {$parsedBenchmark.add("Constant");
    $parsedBenchmark.add($constant_benchmark.text);
    }
    | external_benchmark
    {$parsedBenchmark.add("External");
     $parsedBenchmark.add($external_benchmark.cube);
     $parsedBenchmark.add($external_benchmark.measurement);
     }
    | predicate
    {$parsedBenchmark.add("Sibling");
     $parsedBenchmark.add($predicate.level);
     $parsedBenchmark.add($predicate.value);
    }
    | PAST INT
    {$parsedBenchmark.add("Past");
    $parsedBenchmark.add($INT.text);
    }
    ;

constant_benchmark : (SIGN)? number = (INT|FLOAT) ;

external_benchmark returns [String cube, String measurement]
    : benchmarkCube = ID {$cube = $benchmarkCube.text;} '.'
    benchmarkMeasurement = ID {$measurement = $benchmarkMeasurement.text;};

comparison_scheme [List<String> comparisonMethods] returns [List<String> updatedComparisonMethods]
    @init{$updatedComparisonMethods = $comparisonMethods;}
    : method_name = ID {$updatedComparisonMethods.add($method_name.text);}
    '(' comparison_scheme[$updatedComparisonMethods] | comparison_args')';

comparison_args : ID ',' ( ('benchmark.')? ID | INT);

custom_labeling returns [List<List<String>> labelingTerms]
    @init {labelingTerms = new ArrayList<List<String>>();}
    : '{' term = label_term {labelingTerms.add(term);}
    (',' term = label_term {labelingTerms.add(term);})* '}'
    ;

label_term returns [List<String> term]
    @after{term = range;}
    : range=label_range ':' label=ID {range.add($label.text); };

label_range returns [List<String> limits]
    @init {limits = new ArrayList<String>();}
    : ( lowLimit = '[' | lowLimit = '(' ) {$limits.add($lowLimit.text);}
      start = range_point { $limits.add($start.text); } ','
      end = range_point { $limits.add($end.text); }
      ( highLimit = ')' | highLimit = ']') {$limits.add($highLimit.text);}
    ;

range_point : SIGN? (INT|FLOAT|'inf');

// -- LEXER PART

// Fragments
fragment A : ('A'|'a');
fragment B : ('B'|'b');
fragment C : ('C'|'c');
fragment D : ('D'|'d');
fragment E : ('E'|'e');
fragment F : ('F'|'f');
fragment G : ('G'|'g');
fragment H : ('H'|'h');
fragment I : ('I'|'i');
fragment J : ('J'|'j');
fragment K : ('K'|'k');
fragment L : ('L'|'l');
fragment M : ('M'|'m');
fragment N : ('N'|'n');
fragment O : ('O'|'o');
fragment P : ('P'|'p');
fragment Q : ('Q'|'q');
fragment R : ('R'|'r');
fragment S : ('S'|'s');
fragment T : ('T'|'t');
fragment U : ('U'|'u');
fragment V : ('V'|'v');
fragment W : ('W'|'w');
fragment X : ('X'|'x');
fragment Y : ('Y'|'y');
fragment Z : ('Z'|'z');

// Keywords (case-insensitive)
AGAINST : A G A I N S T;
ASSESS : A S S E S S;
BY : B Y;
FOR : F O R;
LABELS : L A B E L S;
PAST : P A S T;
USING : U S I N G;
WITH : W I T H;

AGGREGATE : (A V G | A V E R A G E
          | M I N | M I N I M U M
          | M A X | M A X I M U M
          | S U M | C O U N T);

// LEXICAL TOKENS
SIGN : ('+' | '-');
ID : ('a'..'z'|'A'..'Z'|'_')+;

INT : '0'..'9'+;
FLOAT : INT '.' INT;

WS : (' '|'\t'|'\n'|'\r') {$channel=HIDDEN;} ;
