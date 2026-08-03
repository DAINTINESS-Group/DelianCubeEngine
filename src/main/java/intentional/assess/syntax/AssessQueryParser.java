// $ANTLR 3.4 AssessQuery.g 2026-08-03 21:38:25

package intentional.assess.syntax;
import intentional.assess.AssessQuery;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import intentional.assess.AssessQueryBuilder;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class AssessQueryParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "AGAINST", "AGGREGATE", "AS", "ASSESS", "B", "BY", "C", "D", "E", "F", "FLOAT", "FOR", "G", "H", "I", "ID", "INT", "J", "K", "L", "LABELS", "M", "N", "O", "P", "PAST", "Q", "R", "S", "SAVE", "SIGN", "T", "U", "USING", "V", "W", "WITH", "WS", "X", "Y", "Z", "'('", "')'", "'*'", "','", "'.'", "'/'", "':'", "'='", "'['", "'\\''", "']'", "'benchmark.'", "'inf'", "'{'", "'}'"
    };

    public static final int EOF=-1;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int A=4;
    public static final int AGAINST=5;
    public static final int AGGREGATE=6;
    public static final int AS=7;
    public static final int ASSESS=8;
    public static final int B=9;
    public static final int BY=10;
    public static final int C=11;
    public static final int D=12;
    public static final int E=13;
    public static final int F=14;
    public static final int FLOAT=15;
    public static final int FOR=16;
    public static final int G=17;
    public static final int H=18;
    public static final int I=19;
    public static final int ID=20;
    public static final int INT=21;
    public static final int J=22;
    public static final int K=23;
    public static final int L=24;
    public static final int LABELS=25;
    public static final int M=26;
    public static final int N=27;
    public static final int O=28;
    public static final int P=29;
    public static final int PAST=30;
    public static final int Q=31;
    public static final int R=32;
    public static final int S=33;
    public static final int SAVE=34;
    public static final int SIGN=35;
    public static final int T=36;
    public static final int U=37;
    public static final int USING=38;
    public static final int V=39;
    public static final int W=40;
    public static final int WITH=41;
    public static final int WS=42;
    public static final int X=43;
    public static final int Y=44;
    public static final int Z=45;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public AssessQueryParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public AssessQueryParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return AssessQueryParser.tokenNames; }
    public String getGrammarFileName() { return "AssessQuery.g"; }


       AssessQueryBuilder builder;



    // $ANTLR start "parse"
    // AssessQuery.g:21:1: parse[AssessQueryBuilder builder] returns [AssessQuery parsedQuery] : result= query EOF ;
    public final AssessQuery parse(AssessQueryBuilder builder) throws RecognitionException {
        AssessQuery parsedQuery = null;


        AssessQuery result =null;


        this.builder = builder;
        try {
            // AssessQuery.g:23:5: (result= query EOF )
            // AssessQuery.g:23:7: result= query EOF
            {
            pushFollow(FOLLOW_query_in_parse66);
            result=query();

            state._fsp--;


            match(input,EOF,FOLLOW_EOF_in_parse68); 

            parsedQuery = result;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return parsedQuery;
    }
    // $ANTLR end "parse"



    // $ANTLR start "query"
    // AssessQuery.g:25:1: query returns [AssessQuery query] : WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS target_measure ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS labeler ( ',' labeler )* ( SAVE AS output_name= ID )? ;
    public final AssessQuery query() throws RecognitionException {
        AssessQuery query = null;


        Token targetCube=null;
        Token output_name=null;
        HashMap<String, String> predicates =null;

        HashSet<String> gammas =null;

        List<String> parsedBenchmark =null;

        List<String> updatedComparisonMethods =null;



            List<String> comparisonMethods = new ArrayList<String>();
            
        try {
            // AssessQuery.g:29:5: ( WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS target_measure ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS labeler ( ',' labeler )* ( SAVE AS output_name= ID )? )
            // AssessQuery.g:29:7: WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS target_measure ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS labeler ( ',' labeler )* ( SAVE AS output_name= ID )?
            {
            match(input,WITH,FOLLOW_WITH_in_query94); 

            targetCube=(Token)match(input,ID,FOLLOW_ID_in_query100); 

            builder.setTargetCubeName((targetCube!=null?targetCube.getText():null));

            // AssessQuery.g:30:7: ( FOR predicates= selection_predicates )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==FOR) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // AssessQuery.g:30:8: FOR predicates= selection_predicates
                    {
                    match(input,FOR,FOLLOW_FOR_in_query111); 

                    pushFollow(FOLLOW_selection_predicates_in_query117);
                    predicates=selection_predicates();

                    state._fsp--;


                    builder.setSelectionPredicates(predicates);

                    }
                    break;

            }


            match(input,BY,FOLLOW_BY_in_query129); 

            pushFollow(FOLLOW_group_by_set_in_query135);
            gammas=group_by_set();

            state._fsp--;


            builder.setGroupBySet(gammas);

            match(input,ASSESS,FOLLOW_ASSESS_in_query145); 

            pushFollow(FOLLOW_target_measure_in_query147);
            target_measure();

            state._fsp--;


            // AssessQuery.g:34:7: ( AGAINST parsedBenchmark= benchmark )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==AGAINST) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // AssessQuery.g:34:8: AGAINST parsedBenchmark= benchmark
                    {
                    match(input,AGAINST,FOLLOW_AGAINST_in_query157); 

                    pushFollow(FOLLOW_benchmark_in_query163);
                    parsedBenchmark=benchmark();

                    state._fsp--;


                    builder.setBenchmarkDetails(parsedBenchmark);

                    }
                    break;

            }


            // AssessQuery.g:37:7: ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==USING) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // AssessQuery.g:37:8: USING updatedComparisonMethods= comparison_scheme[comparisonMethods]
                    {
                    match(input,USING,FOLLOW_USING_in_query183); 

                    pushFollow(FOLLOW_comparison_scheme_in_query189);
                    updatedComparisonMethods=comparison_scheme(comparisonMethods);

                    state._fsp--;


                    builder.setDeltaFunctions(updatedComparisonMethods);

                    }
                    break;

            }


            match(input,LABELS,FOLLOW_LABELS_in_query216); 

            pushFollow(FOLLOW_labeler_in_query218);
            labeler();

            state._fsp--;


            // AssessQuery.g:41:22: ( ',' labeler )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==49) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // AssessQuery.g:41:23: ',' labeler
            	    {
            	    match(input,49,FOLLOW_49_in_query221); 

            	    pushFollow(FOLLOW_labeler_in_query223);
            	    labeler();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            // AssessQuery.g:42:7: ( SAVE AS output_name= ID )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==SAVE) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // AssessQuery.g:42:8: SAVE AS output_name= ID
                    {
                    match(input,SAVE,FOLLOW_SAVE_in_query234); 

                    match(input,AS,FOLLOW_AS_in_query236); 

                    output_name=(Token)match(input,ID,FOLLOW_ID_in_query242); 

                    builder.setOutputName((output_name!=null?output_name.getText():null));

                    }
                    break;

            }


            query = builder.build();

            }

        }
        catch (RecognitionException re) {

                    reportError(re);
                    throw new RuntimeException("Invalid Query Syntax");
                
        }

        finally {
        	// do for sure before leaving
        }
        return query;
    }
    // $ANTLR end "query"



    // $ANTLR start "selection_predicates"
    // AssessQuery.g:50:1: selection_predicates returns [HashMap<String, String> selectionPredicates] : parsed_predicate= predicate ( ',' additional_predicate= predicate )* ;
    public final HashMap<String, String> selection_predicates() throws RecognitionException {
        HashMap<String, String> selectionPredicates = null;


        AssessQueryParser.predicate_return parsed_predicate =null;

        AssessQueryParser.predicate_return additional_predicate =null;


        selectionPredicates = new HashMap<>();
        try {
            // AssessQuery.g:52:5: (parsed_predicate= predicate ( ',' additional_predicate= predicate )* )
            // AssessQuery.g:52:7: parsed_predicate= predicate ( ',' additional_predicate= predicate )*
            {
            pushFollow(FOLLOW_predicate_in_selection_predicates297);
            parsed_predicate=predicate();

            state._fsp--;


            selectionPredicates.put((parsed_predicate!=null?parsed_predicate.level:null), (parsed_predicate!=null?parsed_predicate.value:null));

            // AssessQuery.g:53:5: ( ',' additional_predicate= predicate )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==49) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // AssessQuery.g:53:6: ',' additional_predicate= predicate
            	    {
            	    match(input,49,FOLLOW_49_in_selection_predicates306); 

            	    pushFollow(FOLLOW_predicate_in_selection_predicates312);
            	    additional_predicate=predicate();

            	    state._fsp--;


            	    selectionPredicates.put((additional_predicate!=null?additional_predicate.level:null), (additional_predicate!=null?additional_predicate.value:null));

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return selectionPredicates;
    }
    // $ANTLR end "selection_predicates"


    public static class predicate_return extends ParserRuleReturnScope {
        public String level;
        public String value;
    };


    // $ANTLR start "predicate"
    // AssessQuery.g:56:1: predicate returns [String level, String value] : level_name= ID '=' '\\'' val= level_value '\\'' ;
    public final AssessQueryParser.predicate_return predicate() throws RecognitionException {
        AssessQueryParser.predicate_return retval = new AssessQueryParser.predicate_return();
        retval.start = input.LT(1);


        Token level_name=null;
        AssessQueryParser.level_value_return val =null;


        try {
            // AssessQuery.g:57:5: (level_name= ID '=' '\\'' val= level_value '\\'' )
            // AssessQuery.g:57:7: level_name= ID '=' '\\'' val= level_value '\\''
            {
            level_name=(Token)match(input,ID,FOLLOW_ID_in_predicate341); 

            retval.level = (level_name!=null?level_name.getText():null);

            match(input,53,FOLLOW_53_in_predicate349); 

            match(input,55,FOLLOW_55_in_predicate351); 

            pushFollow(FOLLOW_level_value_in_predicate357);
            val=level_value();

            state._fsp--;


            retval.value = (val!=null?input.toString(val.start,val.stop):null);

            match(input,55,FOLLOW_55_in_predicate361); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "predicate"


    public static class level_value_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "level_value"
    // AssessQuery.g:61:1: level_value : ( ( ID )+ | date );
    public final AssessQueryParser.level_value_return level_value() throws RecognitionException {
        AssessQueryParser.level_value_return retval = new AssessQueryParser.level_value_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:61:13: ( ( ID )+ | date )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ID) ) {
                alt8=1;
            }
            else if ( (LA8_0==INT) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // AssessQuery.g:61:15: ( ID )+
                    {
                    // AssessQuery.g:61:15: ( ID )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==ID) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // AssessQuery.g:61:15: ID
                    	    {
                    	    match(input,ID,FOLLOW_ID_in_level_value374); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    }
                    break;
                case 2 :
                    // AssessQuery.g:61:21: date
                    {
                    pushFollow(FOLLOW_date_in_level_value379);
                    date();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "level_value"



    // $ANTLR start "date"
    // AssessQuery.g:63:1: date : ( INT | INT '/' INT | INT '/' INT '/' INT );
    public final void date() throws RecognitionException {
        try {
            // AssessQuery.g:63:6: ( INT | INT '/' INT | INT '/' INT '/' INT )
            int alt9=3;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==INT) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==51) ) {
                    int LA9_2 = input.LA(3);

                    if ( (LA9_2==INT) ) {
                        int LA9_4 = input.LA(4);

                        if ( (LA9_4==51) ) {
                            alt9=3;
                        }
                        else if ( (LA9_4==55) ) {
                            alt9=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 9, 4, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA9_1==55) ) {
                    alt9=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // AssessQuery.g:64:5: INT
                    {
                    match(input,INT,FOLLOW_INT_in_date392); 

                    }
                    break;
                case 2 :
                    // AssessQuery.g:65:7: INT '/' INT
                    {
                    match(input,INT,FOLLOW_INT_in_date400); 

                    match(input,51,FOLLOW_51_in_date402); 

                    match(input,INT,FOLLOW_INT_in_date404); 

                    }
                    break;
                case 3 :
                    // AssessQuery.g:66:7: INT '/' INT '/' INT
                    {
                    match(input,INT,FOLLOW_INT_in_date412); 

                    match(input,51,FOLLOW_51_in_date414); 

                    match(input,INT,FOLLOW_INT_in_date416); 

                    match(input,51,FOLLOW_51_in_date418); 

                    match(input,INT,FOLLOW_INT_in_date420); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "date"



    // $ANTLR start "group_by_set"
    // AssessQuery.g:68:1: group_by_set returns [HashSet<String> groupBySet] : id= ID ( ',' id= ID )* ;
    public final HashSet<String> group_by_set() throws RecognitionException {
        HashSet<String> groupBySet = null;


        Token id=null;

        groupBySet = new HashSet<>();
        try {
            // AssessQuery.g:70:5: (id= ID ( ',' id= ID )* )
            // AssessQuery.g:70:7: id= ID ( ',' id= ID )*
            {
            id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set446); 

            groupBySet.add((id!=null?id.getText():null));

            // AssessQuery.g:70:42: ( ',' id= ID )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==49) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // AssessQuery.g:70:43: ',' id= ID
            	    {
            	    match(input,49,FOLLOW_49_in_group_by_set451); 

            	    id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set455); 

            	    groupBySet.add((id!=null?id.getText():null));

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return groupBySet;
    }
    // $ANTLR end "group_by_set"



    // $ANTLR start "benchmark"
    // AssessQuery.g:73:1: benchmark returns [List<String> parsedBenchmark] : ( constant_benchmark | external_benchmark | predicate | PAST INT );
    public final List<String> benchmark() throws RecognitionException {
        List<String> parsedBenchmark = null;


        Token INT4=null;
        AssessQueryParser.constant_benchmark_return constant_benchmark1 =null;

        AssessQueryParser.external_benchmark_return external_benchmark2 =null;

        AssessQueryParser.predicate_return predicate3 =null;


        parsedBenchmark = new ArrayList<>();
        try {
            // AssessQuery.g:75:5: ( constant_benchmark | external_benchmark | predicate | PAST INT )
            int alt11=4;
            switch ( input.LA(1) ) {
            case FLOAT:
            case INT:
            case SIGN:
                {
                alt11=1;
                }
                break;
            case ID:
                {
                int LA11_2 = input.LA(2);

                if ( (LA11_2==50) ) {
                    alt11=2;
                }
                else if ( (LA11_2==53) ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 2, input);

                    throw nvae;

                }
                }
                break;
            case PAST:
                {
                alt11=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // AssessQuery.g:75:7: constant_benchmark
                    {
                    pushFollow(FOLLOW_constant_benchmark_in_benchmark488);
                    constant_benchmark1=constant_benchmark();

                    state._fsp--;


                    parsedBenchmark.add("Constant");
                        parsedBenchmark.add((constant_benchmark1!=null?input.toString(constant_benchmark1.start,constant_benchmark1.stop):null));
                        

                    }
                    break;
                case 2 :
                    // AssessQuery.g:79:7: external_benchmark
                    {
                    pushFollow(FOLLOW_external_benchmark_in_benchmark502);
                    external_benchmark2=external_benchmark();

                    state._fsp--;


                    parsedBenchmark.add("External");
                         parsedBenchmark.add((external_benchmark2!=null?external_benchmark2.cube:null));
                         parsedBenchmark.add((external_benchmark2!=null?external_benchmark2.measurement:null));
                         

                    }
                    break;
                case 3 :
                    // AssessQuery.g:84:7: predicate
                    {
                    pushFollow(FOLLOW_predicate_in_benchmark516);
                    predicate3=predicate();

                    state._fsp--;


                    parsedBenchmark.add("Sibling");
                         parsedBenchmark.add((predicate3!=null?predicate3.level:null));
                         parsedBenchmark.add((predicate3!=null?predicate3.value:null));
                        

                    }
                    break;
                case 4 :
                    // AssessQuery.g:89:7: PAST INT
                    {
                    match(input,PAST,FOLLOW_PAST_in_benchmark530); 

                    INT4=(Token)match(input,INT,FOLLOW_INT_in_benchmark532); 

                    parsedBenchmark.add("Past");
                        parsedBenchmark.add((INT4!=null?INT4.getText():null));
                        

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return parsedBenchmark;
    }
    // $ANTLR end "benchmark"


    public static class constant_benchmark_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "constant_benchmark"
    // AssessQuery.g:95:1: constant_benchmark : ( SIGN )? number= ( INT | FLOAT ) ;
    public final AssessQueryParser.constant_benchmark_return constant_benchmark() throws RecognitionException {
        AssessQueryParser.constant_benchmark_return retval = new AssessQueryParser.constant_benchmark_return();
        retval.start = input.LT(1);


        Token number=null;

        try {
            // AssessQuery.g:95:20: ( ( SIGN )? number= ( INT | FLOAT ) )
            // AssessQuery.g:95:22: ( SIGN )? number= ( INT | FLOAT )
            {
            // AssessQuery.g:95:22: ( SIGN )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==SIGN) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // AssessQuery.g:95:23: SIGN
                    {
                    match(input,SIGN,FOLLOW_SIGN_in_constant_benchmark552); 

                    }
                    break;

            }


            number=(Token)input.LT(1);

            if ( input.LA(1)==FLOAT||input.LA(1)==INT ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "constant_benchmark"


    public static class external_benchmark_return extends ParserRuleReturnScope {
        public String cube;
        public String measurement;
    };


    // $ANTLR start "external_benchmark"
    // AssessQuery.g:97:1: external_benchmark returns [String cube, String measurement] : benchmarkCube= ID '.' benchmarkMeasurement= ID ;
    public final AssessQueryParser.external_benchmark_return external_benchmark() throws RecognitionException {
        AssessQueryParser.external_benchmark_return retval = new AssessQueryParser.external_benchmark_return();
        retval.start = input.LT(1);


        Token benchmarkCube=null;
        Token benchmarkMeasurement=null;

        try {
            // AssessQuery.g:98:5: (benchmarkCube= ID '.' benchmarkMeasurement= ID )
            // AssessQuery.g:98:7: benchmarkCube= ID '.' benchmarkMeasurement= ID
            {
            benchmarkCube=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark585); 

            retval.cube = (benchmarkCube!=null?benchmarkCube.getText():null);

            match(input,50,FOLLOW_50_in_external_benchmark589); 

            benchmarkMeasurement=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark599); 

            retval.measurement = (benchmarkMeasurement!=null?benchmarkMeasurement.getText():null);

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "external_benchmark"



    // $ANTLR start "comparison_scheme"
    // AssessQuery.g:101:1: comparison_scheme[List<String> comparisonMethods] returns [List<String> updatedComparisonMethods] : method_name= ID '(' ( comparison_scheme[$updatedComparisonMethods] | comparison_args ) ')' ;
    public final List<String> comparison_scheme(List<String> comparisonMethods) throws RecognitionException {
        List<String> updatedComparisonMethods = null;


        Token method_name=null;

        updatedComparisonMethods = comparisonMethods;
        try {
            // AssessQuery.g:103:5: (method_name= ID '(' ( comparison_scheme[$updatedComparisonMethods] | comparison_args ) ')' )
            // AssessQuery.g:103:7: method_name= ID '(' ( comparison_scheme[$updatedComparisonMethods] | comparison_args ) ')'
            {
            method_name=(Token)match(input,ID,FOLLOW_ID_in_comparison_scheme631); 

            updatedComparisonMethods.add((method_name!=null?method_name.getText():null));

            match(input,46,FOLLOW_46_in_comparison_scheme639); 

            // AssessQuery.g:104:9: ( comparison_scheme[$updatedComparisonMethods] | comparison_args )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==ID) ) {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==46) ) {
                    alt13=1;
                }
                else if ( (LA13_1==49) ) {
                    alt13=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA13_0==FLOAT||LA13_0==INT||LA13_0==57) ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // AssessQuery.g:104:10: comparison_scheme[$updatedComparisonMethods]
                    {
                    pushFollow(FOLLOW_comparison_scheme_in_comparison_scheme642);
                    comparison_scheme(updatedComparisonMethods);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // AssessQuery.g:104:57: comparison_args
                    {
                    pushFollow(FOLLOW_comparison_args_in_comparison_scheme647);
                    comparison_args();

                    state._fsp--;


                    }
                    break;

            }


            match(input,47,FOLLOW_47_in_comparison_scheme650); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return updatedComparisonMethods;
    }
    // $ANTLR end "comparison_scheme"



    // $ANTLR start "comparison_args"
    // AssessQuery.g:106:1: comparison_args : first= operand_ref ',' second= operand_ref ;
    public final void comparison_args() throws RecognitionException {
        String first =null;

        String second =null;


        try {
            // AssessQuery.g:107:5: (first= operand_ref ',' second= operand_ref )
            // AssessQuery.g:107:7: first= operand_ref ',' second= operand_ref
            {
            pushFollow(FOLLOW_operand_ref_in_comparison_args666);
            first=operand_ref();

            state._fsp--;


            match(input,49,FOLLOW_49_in_comparison_args668); 

            pushFollow(FOLLOW_operand_ref_in_comparison_args674);
            second=operand_ref();

            state._fsp--;


             if (builder != null) builder.setDeltaOperands(first, second); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "comparison_args"



    // $ANTLR start "operand_ref"
    // AssessQuery.g:111:1: operand_ref returns [String ref] : ( 'benchmark.' id= ID |id= ID |n= INT |f= FLOAT );
    public final String operand_ref() throws RecognitionException {
        String ref = null;


        Token id=null;
        Token n=null;
        Token f=null;

        try {
            // AssessQuery.g:112:5: ( 'benchmark.' id= ID |id= ID |n= INT |f= FLOAT )
            int alt14=4;
            switch ( input.LA(1) ) {
            case 57:
                {
                alt14=1;
                }
                break;
            case ID:
                {
                alt14=2;
                }
                break;
            case INT:
                {
                alt14=3;
                }
                break;
            case FLOAT:
                {
                alt14=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }

            switch (alt14) {
                case 1 :
                    // AssessQuery.g:112:7: 'benchmark.' id= ID
                    {
                    match(input,57,FOLLOW_57_in_operand_ref703); 

                    id=(Token)match(input,ID,FOLLOW_ID_in_operand_ref709); 

                     ref = "benchmark." + (id!=null?id.getText():null); 

                    }
                    break;
                case 2 :
                    // AssessQuery.g:113:7: id= ID
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_operand_ref723); 

                     ref = (id!=null?id.getText():null); 

                    }
                    break;
                case 3 :
                    // AssessQuery.g:114:7: n= INT
                    {
                    n=(Token)match(input,INT,FOLLOW_INT_in_operand_ref737); 

                     ref = (n!=null?n.getText():null); 

                    }
                    break;
                case 4 :
                    // AssessQuery.g:115:7: f= FLOAT
                    {
                    f=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_operand_ref751); 

                     ref = (f!=null?f.getText():null); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ref;
    }
    // $ANTLR end "operand_ref"



    // $ANTLR start "target_measure"
    // AssessQuery.g:118:1: target_measure : e= measure_expression ( AS alias= ID )? ;
    public final void target_measure() throws RecognitionException {
        Token alias=null;
        AssessQueryParser.measure_expression_return e =null;


        try {
            // AssessQuery.g:119:5: (e= measure_expression ( AS alias= ID )? )
            // AssessQuery.g:119:7: e= measure_expression ( AS alias= ID )?
            {
            pushFollow(FOLLOW_measure_expression_in_target_measure774);
            e=measure_expression();

            state._fsp--;


            // AssessQuery.g:119:30: ( AS alias= ID )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==AS) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // AssessQuery.g:119:31: AS alias= ID
                    {
                    match(input,AS,FOLLOW_AS_in_target_measure777); 

                    alias=(Token)match(input,ID,FOLLOW_ID_in_target_measure783); 

                    }
                    break;

            }


             builder.setTargetMeasure((e!=null?input.toString(e.start,e.stop):null), (alias!=null?alias.getText():null)); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "target_measure"


    public static class measure_expression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "measure_expression"
    // AssessQuery.g:123:1: measure_expression : measure_term ( SIGN measure_term )* ;
    public final AssessQueryParser.measure_expression_return measure_expression() throws RecognitionException {
        AssessQueryParser.measure_expression_return retval = new AssessQueryParser.measure_expression_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:124:5: ( measure_term ( SIGN measure_term )* )
            // AssessQuery.g:124:7: measure_term ( SIGN measure_term )*
            {
            pushFollow(FOLLOW_measure_term_in_measure_expression810);
            measure_term();

            state._fsp--;


            // AssessQuery.g:124:20: ( SIGN measure_term )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==SIGN) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // AssessQuery.g:124:21: SIGN measure_term
            	    {
            	    match(input,SIGN,FOLLOW_SIGN_in_measure_expression813); 

            	    pushFollow(FOLLOW_measure_term_in_measure_expression815);
            	    measure_term();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "measure_expression"



    // $ANTLR start "measure_term"
    // AssessQuery.g:127:1: measure_term : measure_factor ( ( '*' | '/' ) measure_factor )* ;
    public final void measure_term() throws RecognitionException {
        try {
            // AssessQuery.g:128:5: ( measure_factor ( ( '*' | '/' ) measure_factor )* )
            // AssessQuery.g:128:7: measure_factor ( ( '*' | '/' ) measure_factor )*
            {
            pushFollow(FOLLOW_measure_factor_in_measure_term834);
            measure_factor();

            state._fsp--;


            // AssessQuery.g:128:22: ( ( '*' | '/' ) measure_factor )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==48||LA17_0==51) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // AssessQuery.g:128:23: ( '*' | '/' ) measure_factor
            	    {
            	    if ( input.LA(1)==48||input.LA(1)==51 ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_measure_factor_in_measure_term845);
            	    measure_factor();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "measure_term"



    // $ANTLR start "measure_factor"
    // AssessQuery.g:131:1: measure_factor : ( AGGREGATE '(' measure_expression ')' | '(' measure_expression ')' | ID | INT | FLOAT );
    public final void measure_factor() throws RecognitionException {
        try {
            // AssessQuery.g:132:5: ( AGGREGATE '(' measure_expression ')' | '(' measure_expression ')' | ID | INT | FLOAT )
            int alt18=5;
            switch ( input.LA(1) ) {
            case AGGREGATE:
                {
                alt18=1;
                }
                break;
            case 46:
                {
                alt18=2;
                }
                break;
            case ID:
                {
                alt18=3;
                }
                break;
            case INT:
                {
                alt18=4;
                }
                break;
            case FLOAT:
                {
                alt18=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // AssessQuery.g:132:7: AGGREGATE '(' measure_expression ')'
                    {
                    match(input,AGGREGATE,FOLLOW_AGGREGATE_in_measure_factor864); 

                    match(input,46,FOLLOW_46_in_measure_factor866); 

                    pushFollow(FOLLOW_measure_expression_in_measure_factor868);
                    measure_expression();

                    state._fsp--;


                    match(input,47,FOLLOW_47_in_measure_factor870); 

                    }
                    break;
                case 2 :
                    // AssessQuery.g:133:7: '(' measure_expression ')'
                    {
                    match(input,46,FOLLOW_46_in_measure_factor878); 

                    pushFollow(FOLLOW_measure_expression_in_measure_factor880);
                    measure_expression();

                    state._fsp--;


                    match(input,47,FOLLOW_47_in_measure_factor882); 

                    }
                    break;
                case 3 :
                    // AssessQuery.g:134:7: ID
                    {
                    match(input,ID,FOLLOW_ID_in_measure_factor890); 

                    }
                    break;
                case 4 :
                    // AssessQuery.g:135:7: INT
                    {
                    match(input,INT,FOLLOW_INT_in_measure_factor898); 

                    }
                    break;
                case 5 :
                    // AssessQuery.g:136:7: FLOAT
                    {
                    match(input,FLOAT,FOLLOW_FLOAT_in_measure_factor906); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "measure_factor"



    // $ANTLR start "labeler"
    // AssessQuery.g:139:1: labeler : (labelingSystem= custom_labeling ( AS customName= ID )? |schemeName= ID ( '(' parsedArgs= labeler_args ')' )? );
    public final void labeler() throws RecognitionException {
        Token customName=null;
        Token schemeName=null;
        List<List<String>> labelingSystem =null;

        List<String> parsedArgs =null;


         List<String> schemeArgs = null; 
        try {
            // AssessQuery.g:141:5: (labelingSystem= custom_labeling ( AS customName= ID )? |schemeName= ID ( '(' parsedArgs= labeler_args ')' )? )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==59) ) {
                alt21=1;
            }
            else if ( (LA21_0==ID) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }
            switch (alt21) {
                case 1 :
                    // AssessQuery.g:141:7: labelingSystem= custom_labeling ( AS customName= ID )?
                    {
                    pushFollow(FOLLOW_custom_labeling_in_labeler936);
                    labelingSystem=custom_labeling();

                    state._fsp--;


                    // AssessQuery.g:141:40: ( AS customName= ID )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==AS) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // AssessQuery.g:141:41: AS customName= ID
                            {
                            match(input,AS,FOLLOW_AS_in_labeler939); 

                            customName=(Token)match(input,ID,FOLLOW_ID_in_labeler945); 

                            }
                            break;

                    }


                    builder.addCustomLabeler(labelingSystem, (customName!=null?customName.getText():null));

                    }
                    break;
                case 2 :
                    // AssessQuery.g:143:7: schemeName= ID ( '(' parsedArgs= labeler_args ')' )?
                    {
                    schemeName=(Token)match(input,ID,FOLLOW_ID_in_labeler967); 

                    // AssessQuery.g:143:23: ( '(' parsedArgs= labeler_args ')' )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==46) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // AssessQuery.g:143:24: '(' parsedArgs= labeler_args ')'
                            {
                            match(input,46,FOLLOW_46_in_labeler970); 

                            pushFollow(FOLLOW_labeler_args_in_labeler976);
                            parsedArgs=labeler_args();

                            state._fsp--;


                            schemeArgs = parsedArgs;

                            match(input,47,FOLLOW_47_in_labeler980); 

                            }
                            break;

                    }


                    builder.addNamedLabeler((schemeName!=null?schemeName.getText():null), schemeArgs);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "labeler"



    // $ANTLR start "labeler_args"
    // AssessQuery.g:147:1: labeler_args returns [List<String> args] : a= labeler_arg ( ',' b= labeler_arg )* ;
    public final List<String> labeler_args() throws RecognitionException {
        List<String> args = null;


        AssessQueryParser.labeler_arg_return a =null;

        AssessQueryParser.labeler_arg_return b =null;


        args = new ArrayList<String>();
        try {
            // AssessQuery.g:149:5: (a= labeler_arg ( ',' b= labeler_arg )* )
            // AssessQuery.g:149:7: a= labeler_arg ( ',' b= labeler_arg )*
            {
            pushFollow(FOLLOW_labeler_arg_in_labeler_args1024);
            a=labeler_arg();

            state._fsp--;


            args.add((a!=null?input.toString(a.start,a.stop):null));

            // AssessQuery.g:150:7: ( ',' b= labeler_arg )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==49) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // AssessQuery.g:150:8: ',' b= labeler_arg
            	    {
            	    match(input,49,FOLLOW_49_in_labeler_args1035); 

            	    pushFollow(FOLLOW_labeler_arg_in_labeler_args1041);
            	    b=labeler_arg();

            	    state._fsp--;


            	    args.add((b!=null?input.toString(b.start,b.stop):null));

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return args;
    }
    // $ANTLR end "labeler_args"


    public static class labeler_arg_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "labeler_arg"
    // AssessQuery.g:153:1: labeler_arg : ( ID | INT | FLOAT );
    public final AssessQueryParser.labeler_arg_return labeler_arg() throws RecognitionException {
        AssessQueryParser.labeler_arg_return retval = new AssessQueryParser.labeler_arg_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:153:13: ( ID | INT | FLOAT )
            // AssessQuery.g:
            {
            if ( input.LA(1)==FLOAT||(input.LA(1) >= ID && input.LA(1) <= INT) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "labeler_arg"



    // $ANTLR start "custom_labeling"
    // AssessQuery.g:155:1: custom_labeling returns [List<List<String>> labelingTerms] : '{' term= label_term ( ',' term= label_term )* '}' ;
    public final List<List<String>> custom_labeling() throws RecognitionException {
        List<List<String>> labelingTerms = null;


        List<String> term =null;


        labelingTerms = new ArrayList<List<String>>();
        try {
            // AssessQuery.g:157:5: ( '{' term= label_term ( ',' term= label_term )* '}' )
            // AssessQuery.g:157:7: '{' term= label_term ( ',' term= label_term )* '}'
            {
            match(input,59,FOLLOW_59_in_custom_labeling1091); 

            pushFollow(FOLLOW_label_term_in_custom_labeling1097);
            term=label_term();

            state._fsp--;


            labelingTerms.add(term);

            // AssessQuery.g:158:5: ( ',' term= label_term )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==49) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // AssessQuery.g:158:6: ',' term= label_term
            	    {
            	    match(input,49,FOLLOW_49_in_custom_labeling1106); 

            	    pushFollow(FOLLOW_label_term_in_custom_labeling1112);
            	    term=label_term();

            	    state._fsp--;


            	    labelingTerms.add(term);

            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);


            match(input,60,FOLLOW_60_in_custom_labeling1118); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return labelingTerms;
    }
    // $ANTLR end "custom_labeling"



    // $ANTLR start "label_term"
    // AssessQuery.g:161:1: label_term returns [List<String> term] : range= label_range ':' label= ID ;
    public final List<String> label_term() throws RecognitionException {
        List<String> term = null;


        Token label=null;
        List<String> range =null;


        try {
            // AssessQuery.g:163:5: (range= label_range ':' label= ID )
            // AssessQuery.g:163:7: range= label_range ':' label= ID
            {
            pushFollow(FOLLOW_label_range_in_label_term1149);
            range=label_range();

            state._fsp--;


            match(input,52,FOLLOW_52_in_label_term1151); 

            label=(Token)match(input,ID,FOLLOW_ID_in_label_term1155); 

            range.add((label!=null?label.getText():null)); 

            }

            term = range;
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return term;
    }
    // $ANTLR end "label_term"



    // $ANTLR start "label_range"
    // AssessQuery.g:165:1: label_range returns [List<String> limits] : (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) ;
    public final List<String> label_range() throws RecognitionException {
        List<String> limits = null;


        Token lowLimit=null;
        Token highLimit=null;
        AssessQueryParser.range_point_return start =null;

        AssessQueryParser.range_point_return end =null;


        limits = new ArrayList<String>();
        try {
            // AssessQuery.g:167:5: ( (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) )
            // AssessQuery.g:167:7: (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' )
            {
            // AssessQuery.g:167:7: (lowLimit= '[' |lowLimit= '(' )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==54) ) {
                alt24=1;
            }
            else if ( (LA24_0==46) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;

            }
            switch (alt24) {
                case 1 :
                    // AssessQuery.g:167:9: lowLimit= '['
                    {
                    lowLimit=(Token)match(input,54,FOLLOW_54_in_label_range1188); 

                    }
                    break;
                case 2 :
                    // AssessQuery.g:167:26: lowLimit= '('
                    {
                    lowLimit=(Token)match(input,46,FOLLOW_46_in_label_range1196); 

                    }
                    break;

            }


            limits.add((lowLimit!=null?lowLimit.getText():null));

            pushFollow(FOLLOW_range_point_in_label_range1212);
            start=range_point();

            state._fsp--;


             limits.add((start!=null?input.toString(start.start,start.stop):null)); 

            match(input,49,FOLLOW_49_in_label_range1216); 

            pushFollow(FOLLOW_range_point_in_label_range1228);
            end=range_point();

            state._fsp--;


             limits.add((end!=null?input.toString(end.start,end.stop):null)); 

            // AssessQuery.g:170:7: (highLimit= ')' |highLimit= ']' )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==47) ) {
                alt25=1;
            }
            else if ( (LA25_0==56) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }
            switch (alt25) {
                case 1 :
                    // AssessQuery.g:170:9: highLimit= ')'
                    {
                    highLimit=(Token)match(input,47,FOLLOW_47_in_label_range1244); 

                    }
                    break;
                case 2 :
                    // AssessQuery.g:170:27: highLimit= ']'
                    {
                    highLimit=(Token)match(input,56,FOLLOW_56_in_label_range1252); 

                    }
                    break;

            }


            limits.add((highLimit!=null?highLimit.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return limits;
    }
    // $ANTLR end "label_range"


    public static class range_point_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "range_point"
    // AssessQuery.g:173:1: range_point : ( SIGN )? ( INT | FLOAT | 'inf' ) ;
    public final AssessQueryParser.range_point_return range_point() throws RecognitionException {
        AssessQueryParser.range_point_return retval = new AssessQueryParser.range_point_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:173:13: ( ( SIGN )? ( INT | FLOAT | 'inf' ) )
            // AssessQuery.g:173:15: ( SIGN )? ( INT | FLOAT | 'inf' )
            {
            // AssessQuery.g:173:15: ( SIGN )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==SIGN) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // AssessQuery.g:173:15: SIGN
                    {
                    match(input,SIGN,FOLLOW_SIGN_in_range_point1268); 

                    }
                    break;

            }


            if ( input.LA(1)==FLOAT||input.LA(1)==INT||input.LA(1)==58 ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "range_point"

    // Delegated rules


 

    public static final BitSet FOLLOW_query_in_parse66 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_parse68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_query94 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_query100 = new BitSet(new long[]{0x0000000000010400L});
    public static final BitSet FOLLOW_FOR_in_query111 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_selection_predicates_in_query117 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_BY_in_query129 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_group_by_set_in_query135 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ASSESS_in_query145 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_target_measure_in_query147 = new BitSet(new long[]{0x0000004002000020L});
    public static final BitSet FOLLOW_AGAINST_in_query157 = new BitSet(new long[]{0x0000000840308000L});
    public static final BitSet FOLLOW_benchmark_in_query163 = new BitSet(new long[]{0x0000004002000000L});
    public static final BitSet FOLLOW_USING_in_query183 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_comparison_scheme_in_query189 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_LABELS_in_query216 = new BitSet(new long[]{0x0800000000100000L});
    public static final BitSet FOLLOW_labeler_in_query218 = new BitSet(new long[]{0x0002000400000002L});
    public static final BitSet FOLLOW_49_in_query221 = new BitSet(new long[]{0x0800000000100000L});
    public static final BitSet FOLLOW_labeler_in_query223 = new BitSet(new long[]{0x0002000400000002L});
    public static final BitSet FOLLOW_SAVE_in_query234 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_AS_in_query236 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_query242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_selection_predicates297 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_49_in_selection_predicates306 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_predicate_in_selection_predicates312 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_ID_in_predicate341 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_predicate349 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_55_in_predicate351 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_level_value_in_predicate357 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_55_in_predicate361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_level_value374 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_date_in_level_value379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_date392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_date400 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_date402 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_date404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_date412 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_date414 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_date416 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_date418 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_date420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_group_by_set446 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_49_in_group_by_set451 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_group_by_set455 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_constant_benchmark_in_benchmark488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_benchmark_in_benchmark502 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_benchmark516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PAST_in_benchmark530 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_benchmark532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_in_constant_benchmark552 = new BitSet(new long[]{0x0000000000208000L});
    public static final BitSet FOLLOW_set_in_constant_benchmark560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_external_benchmark585 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_external_benchmark589 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_external_benchmark599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_comparison_scheme631 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_comparison_scheme639 = new BitSet(new long[]{0x0200000000308000L});
    public static final BitSet FOLLOW_comparison_scheme_in_comparison_scheme642 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_comparison_args_in_comparison_scheme647 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_comparison_scheme650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operand_ref_in_comparison_args666 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_comparison_args668 = new BitSet(new long[]{0x0200000000308000L});
    public static final BitSet FOLLOW_operand_ref_in_comparison_args674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_operand_ref703 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_operand_ref709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_operand_ref723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_operand_ref737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_operand_ref751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measure_expression_in_target_measure774 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_AS_in_target_measure777 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_target_measure783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measure_term_in_measure_expression810 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_SIGN_in_measure_expression813 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_term_in_measure_expression815 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_measure_factor_in_measure_term834 = new BitSet(new long[]{0x0009000000000002L});
    public static final BitSet FOLLOW_set_in_measure_term837 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_factor_in_measure_term845 = new BitSet(new long[]{0x0009000000000002L});
    public static final BitSet FOLLOW_AGGREGATE_in_measure_factor864 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_measure_factor866 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_expression_in_measure_factor868 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_measure_factor870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_measure_factor878 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_expression_in_measure_factor880 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_measure_factor882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_measure_factor890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_measure_factor898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_measure_factor906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_custom_labeling_in_labeler936 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_AS_in_labeler939 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_labeler945 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labeler967 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_46_in_labeler970 = new BitSet(new long[]{0x0000000000308000L});
    public static final BitSet FOLLOW_labeler_args_in_labeler976 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_labeler980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labeler_arg_in_labeler_args1024 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_49_in_labeler_args1035 = new BitSet(new long[]{0x0000000000308000L});
    public static final BitSet FOLLOW_labeler_arg_in_labeler_args1041 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_59_in_custom_labeling1091 = new BitSet(new long[]{0x0040400000000000L});
    public static final BitSet FOLLOW_label_term_in_custom_labeling1097 = new BitSet(new long[]{0x1002000000000000L});
    public static final BitSet FOLLOW_49_in_custom_labeling1106 = new BitSet(new long[]{0x0040400000000000L});
    public static final BitSet FOLLOW_label_term_in_custom_labeling1112 = new BitSet(new long[]{0x1002000000000000L});
    public static final BitSet FOLLOW_60_in_custom_labeling1118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_label_range_in_label_term1149 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_label_term1151 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_label_term1155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_label_range1188 = new BitSet(new long[]{0x0400000800208000L});
    public static final BitSet FOLLOW_46_in_label_range1196 = new BitSet(new long[]{0x0400000800208000L});
    public static final BitSet FOLLOW_range_point_in_label_range1212 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_label_range1216 = new BitSet(new long[]{0x0400000800208000L});
    public static final BitSet FOLLOW_range_point_in_label_range1228 = new BitSet(new long[]{0x0100800000000000L});
    public static final BitSet FOLLOW_47_in_label_range1244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_label_range1252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_in_range_point1268 = new BitSet(new long[]{0x0400000000208000L});
    public static final BitSet FOLLOW_set_in_range_point1271 = new BitSet(new long[]{0x0000000000000002L});

}