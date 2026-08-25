// $ANTLR 3.4 AssessQuery.g 2026-08-25 16:01:44

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
import java.util.Map;
import java.util.HashMap;

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
            if (state.failed) return parsedQuery;

            match(input,EOF,FOLLOW_EOF_in_parse68); if (state.failed) return parsedQuery;

            if ( state.backtracking==0 ) {parsedQuery = result;}

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
    // AssessQuery.g:25:1: query returns [AssessQuery query] : WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS target_measure ( AGAINST parsedBenchmark= benchmark ( USING firstMethods= comparison_scheme[new ArrayList<String>()] )? ( ',' extraBenchmark= benchmark ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )? )* | USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS labeler ( ',' labeler )* ( SAVE AS output_name= ID )? ;
    public final AssessQuery query() throws RecognitionException {
        AssessQuery query = null;


        Token targetCube=null;
        Token output_name=null;
        HashMap<String, String> predicates =null;

        HashSet<String> gammas =null;

        List<String> parsedBenchmark =null;

        List<String> firstMethods =null;

        List<String> extraBenchmark =null;

        List<String> extraMethods =null;

        List<String> updatedComparisonMethods =null;



            List<String> comparisonMethods = new ArrayList<String>();
            
        try {
            // AssessQuery.g:29:5: ( WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS target_measure ( AGAINST parsedBenchmark= benchmark ( USING firstMethods= comparison_scheme[new ArrayList<String>()] )? ( ',' extraBenchmark= benchmark ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )? )* | USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS labeler ( ',' labeler )* ( SAVE AS output_name= ID )? )
            // AssessQuery.g:29:7: WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS target_measure ( AGAINST parsedBenchmark= benchmark ( USING firstMethods= comparison_scheme[new ArrayList<String>()] )? ( ',' extraBenchmark= benchmark ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )? )* | USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS labeler ( ',' labeler )* ( SAVE AS output_name= ID )?
            {
            match(input,WITH,FOLLOW_WITH_in_query94); if (state.failed) return query;

            targetCube=(Token)match(input,ID,FOLLOW_ID_in_query100); if (state.failed) return query;

            if ( state.backtracking==0 ) {builder.setTargetCubeName((targetCube!=null?targetCube.getText():null));}

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
                    match(input,FOR,FOLLOW_FOR_in_query111); if (state.failed) return query;

                    pushFollow(FOLLOW_selection_predicates_in_query117);
                    predicates=selection_predicates();

                    state._fsp--;
                    if (state.failed) return query;

                    if ( state.backtracking==0 ) {builder.setSelectionPredicates(predicates);}

                    }
                    break;

            }


            match(input,BY,FOLLOW_BY_in_query129); if (state.failed) return query;

            pushFollow(FOLLOW_group_by_set_in_query135);
            gammas=group_by_set();

            state._fsp--;
            if (state.failed) return query;

            if ( state.backtracking==0 ) {builder.setGroupBySet(gammas);}

            match(input,ASSESS,FOLLOW_ASSESS_in_query145); if (state.failed) return query;

            pushFollow(FOLLOW_target_measure_in_query147);
            target_measure();

            state._fsp--;
            if (state.failed) return query;

            // AssessQuery.g:34:7: ( AGAINST parsedBenchmark= benchmark ( USING firstMethods= comparison_scheme[new ArrayList<String>()] )? ( ',' extraBenchmark= benchmark ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )? )* | USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )?
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==AGAINST) ) {
                alt5=1;
            }
            else if ( (LA5_0==USING) ) {
                alt5=2;
            }
            switch (alt5) {
                case 1 :
                    // AssessQuery.g:34:9: AGAINST parsedBenchmark= benchmark ( USING firstMethods= comparison_scheme[new ArrayList<String>()] )? ( ',' extraBenchmark= benchmark ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )? )*
                    {
                    match(input,AGAINST,FOLLOW_AGAINST_in_query158); if (state.failed) return query;

                    pushFollow(FOLLOW_benchmark_in_query164);
                    parsedBenchmark=benchmark();

                    state._fsp--;
                    if (state.failed) return query;

                    if ( state.backtracking==0 ) {builder.addBenchmarkDetails(parsedBenchmark);}

                    // AssessQuery.g:36:9: ( USING firstMethods= comparison_scheme[new ArrayList<String>()] )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==USING) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // AssessQuery.g:36:10: USING firstMethods= comparison_scheme[new ArrayList<String>()]
                            {
                            match(input,USING,FOLLOW_USING_in_query185); if (state.failed) return query;

                            pushFollow(FOLLOW_comparison_scheme_in_query191);
                            firstMethods=comparison_scheme(new ArrayList<String>());

                            state._fsp--;
                            if (state.failed) return query;

                            if ( state.backtracking==0 ) {builder.setDeltaFunctions(firstMethods);}

                            }
                            break;

                    }


                    // AssessQuery.g:38:9: ( ',' extraBenchmark= benchmark ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )? )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==49) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // AssessQuery.g:38:10: ',' extraBenchmark= benchmark ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )?
                    	    {
                    	    match(input,49,FOLLOW_49_in_query215); if (state.failed) return query;

                    	    pushFollow(FOLLOW_benchmark_in_query221);
                    	    extraBenchmark=benchmark();

                    	    state._fsp--;
                    	    if (state.failed) return query;

                    	    if ( state.backtracking==0 ) {builder.addBenchmarkDetails(extraBenchmark);}

                    	    // AssessQuery.g:40:9: ( USING extraMethods= comparison_scheme[new ArrayList<String>()] )?
                    	    int alt3=2;
                    	    int LA3_0 = input.LA(1);

                    	    if ( (LA3_0==USING) ) {
                    	        alt3=1;
                    	    }
                    	    switch (alt3) {
                    	        case 1 :
                    	            // AssessQuery.g:40:10: USING extraMethods= comparison_scheme[new ArrayList<String>()]
                    	            {
                    	            match(input,USING,FOLLOW_USING_in_query242); if (state.failed) return query;

                    	            pushFollow(FOLLOW_comparison_scheme_in_query248);
                    	            extraMethods=comparison_scheme(new ArrayList<String>());

                    	            state._fsp--;
                    	            if (state.failed) return query;

                    	            if ( state.backtracking==0 ) {builder.setDeltaFunctions(extraMethods);}

                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // AssessQuery.g:42:9: USING updatedComparisonMethods= comparison_scheme[comparisonMethods]
                    {
                    match(input,USING,FOLLOW_USING_in_query274); if (state.failed) return query;

                    pushFollow(FOLLOW_comparison_scheme_in_query280);
                    updatedComparisonMethods=comparison_scheme(comparisonMethods);

                    state._fsp--;
                    if (state.failed) return query;

                    if ( state.backtracking==0 ) {builder.setDeltaFunctions(updatedComparisonMethods);}

                    }
                    break;

            }


            match(input,LABELS,FOLLOW_LABELS_in_query316); if (state.failed) return query;

            pushFollow(FOLLOW_labeler_in_query318);
            labeler();

            state._fsp--;
            if (state.failed) return query;

            // AssessQuery.g:47:22: ( ',' labeler )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==49) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // AssessQuery.g:47:23: ',' labeler
            	    {
            	    match(input,49,FOLLOW_49_in_query321); if (state.failed) return query;

            	    pushFollow(FOLLOW_labeler_in_query323);
            	    labeler();

            	    state._fsp--;
            	    if (state.failed) return query;

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            // AssessQuery.g:48:7: ( SAVE AS output_name= ID )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==SAVE) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // AssessQuery.g:48:8: SAVE AS output_name= ID
                    {
                    match(input,SAVE,FOLLOW_SAVE_in_query334); if (state.failed) return query;

                    match(input,AS,FOLLOW_AS_in_query336); if (state.failed) return query;

                    output_name=(Token)match(input,ID,FOLLOW_ID_in_query342); if (state.failed) return query;

                    if ( state.backtracking==0 ) {builder.setOutputName((output_name!=null?output_name.getText():null));}

                    }
                    break;

            }


            if ( state.backtracking==0 ) {query = builder.build();}

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
    // AssessQuery.g:56:1: selection_predicates returns [HashMap<String, String> selectionPredicates] : parsed_predicate= predicate ( ',' additional_predicate= predicate )* ;
    public final HashMap<String, String> selection_predicates() throws RecognitionException {
        HashMap<String, String> selectionPredicates = null;


        AssessQueryParser.predicate_return parsed_predicate =null;

        AssessQueryParser.predicate_return additional_predicate =null;


        selectionPredicates = new HashMap<>();
        try {
            // AssessQuery.g:58:5: (parsed_predicate= predicate ( ',' additional_predicate= predicate )* )
            // AssessQuery.g:58:7: parsed_predicate= predicate ( ',' additional_predicate= predicate )*
            {
            pushFollow(FOLLOW_predicate_in_selection_predicates397);
            parsed_predicate=predicate();

            state._fsp--;
            if (state.failed) return selectionPredicates;

            if ( state.backtracking==0 ) {selectionPredicates.put((parsed_predicate!=null?parsed_predicate.level:null), (parsed_predicate!=null?parsed_predicate.value:null));}

            // AssessQuery.g:59:5: ( ',' additional_predicate= predicate )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==49) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // AssessQuery.g:59:6: ',' additional_predicate= predicate
            	    {
            	    match(input,49,FOLLOW_49_in_selection_predicates406); if (state.failed) return selectionPredicates;

            	    pushFollow(FOLLOW_predicate_in_selection_predicates412);
            	    additional_predicate=predicate();

            	    state._fsp--;
            	    if (state.failed) return selectionPredicates;

            	    if ( state.backtracking==0 ) {selectionPredicates.put((additional_predicate!=null?additional_predicate.level:null), (additional_predicate!=null?additional_predicate.value:null));}

            	    }
            	    break;

            	default :
            	    break loop8;
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
    // AssessQuery.g:62:1: predicate returns [String level, String value] : level_name= ID '=' '\\'' val= level_value '\\'' ;
    public final AssessQueryParser.predicate_return predicate() throws RecognitionException {
        AssessQueryParser.predicate_return retval = new AssessQueryParser.predicate_return();
        retval.start = input.LT(1);


        Token level_name=null;
        AssessQueryParser.level_value_return val =null;


        try {
            // AssessQuery.g:63:5: (level_name= ID '=' '\\'' val= level_value '\\'' )
            // AssessQuery.g:63:7: level_name= ID '=' '\\'' val= level_value '\\''
            {
            level_name=(Token)match(input,ID,FOLLOW_ID_in_predicate441); if (state.failed) return retval;

            if ( state.backtracking==0 ) {retval.level = (level_name!=null?level_name.getText():null);}

            match(input,53,FOLLOW_53_in_predicate449); if (state.failed) return retval;

            match(input,55,FOLLOW_55_in_predicate451); if (state.failed) return retval;

            pushFollow(FOLLOW_level_value_in_predicate457);
            val=level_value();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {retval.value = (val!=null?input.toString(val.start,val.stop):null);}

            match(input,55,FOLLOW_55_in_predicate461); if (state.failed) return retval;

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
    // AssessQuery.g:67:1: level_value : ( ( ID )+ | date );
    public final AssessQueryParser.level_value_return level_value() throws RecognitionException {
        AssessQueryParser.level_value_return retval = new AssessQueryParser.level_value_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:67:13: ( ( ID )+ | date )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==ID) ) {
                alt10=1;
            }
            else if ( (LA10_0==INT) ) {
                alt10=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // AssessQuery.g:67:15: ( ID )+
                    {
                    // AssessQuery.g:67:15: ( ID )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0==ID) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // AssessQuery.g:67:15: ID
                    	    {
                    	    match(input,ID,FOLLOW_ID_in_level_value474); if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);


                    }
                    break;
                case 2 :
                    // AssessQuery.g:67:21: date
                    {
                    pushFollow(FOLLOW_date_in_level_value479);
                    date();

                    state._fsp--;
                    if (state.failed) return retval;

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
    // AssessQuery.g:69:1: date : ( INT | INT '/' INT | INT '/' INT '/' INT );
    public final void date() throws RecognitionException {
        try {
            // AssessQuery.g:69:6: ( INT | INT '/' INT | INT '/' INT '/' INT )
            int alt11=3;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==INT) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==51) ) {
                    int LA11_2 = input.LA(3);

                    if ( (LA11_2==INT) ) {
                        int LA11_4 = input.LA(4);

                        if ( (LA11_4==51) ) {
                            alt11=3;
                        }
                        else if ( (LA11_4==55) ) {
                            alt11=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 11, 4, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 11, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA11_1==55) ) {
                    alt11=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // AssessQuery.g:70:5: INT
                    {
                    match(input,INT,FOLLOW_INT_in_date492); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // AssessQuery.g:71:7: INT '/' INT
                    {
                    match(input,INT,FOLLOW_INT_in_date500); if (state.failed) return ;

                    match(input,51,FOLLOW_51_in_date502); if (state.failed) return ;

                    match(input,INT,FOLLOW_INT_in_date504); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // AssessQuery.g:72:7: INT '/' INT '/' INT
                    {
                    match(input,INT,FOLLOW_INT_in_date512); if (state.failed) return ;

                    match(input,51,FOLLOW_51_in_date514); if (state.failed) return ;

                    match(input,INT,FOLLOW_INT_in_date516); if (state.failed) return ;

                    match(input,51,FOLLOW_51_in_date518); if (state.failed) return ;

                    match(input,INT,FOLLOW_INT_in_date520); if (state.failed) return ;

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
    // AssessQuery.g:74:1: group_by_set returns [HashSet<String> groupBySet] : id= ID ( ',' id= ID )* ;
    public final HashSet<String> group_by_set() throws RecognitionException {
        HashSet<String> groupBySet = null;


        Token id=null;

        groupBySet = new HashSet<>();
        try {
            // AssessQuery.g:76:5: (id= ID ( ',' id= ID )* )
            // AssessQuery.g:76:7: id= ID ( ',' id= ID )*
            {
            id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set546); if (state.failed) return groupBySet;

            if ( state.backtracking==0 ) {groupBySet.add((id!=null?id.getText():null));}

            // AssessQuery.g:76:42: ( ',' id= ID )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==49) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // AssessQuery.g:76:43: ',' id= ID
            	    {
            	    match(input,49,FOLLOW_49_in_group_by_set551); if (state.failed) return groupBySet;

            	    id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set555); if (state.failed) return groupBySet;

            	    if ( state.backtracking==0 ) {groupBySet.add((id!=null?id.getText():null));}

            	    }
            	    break;

            	default :
            	    break loop12;
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
    // AssessQuery.g:79:1: benchmark returns [List<String> parsedBenchmark] : ( constant_benchmark | external_benchmark | predicate | PAST INT );
    public final List<String> benchmark() throws RecognitionException {
        List<String> parsedBenchmark = null;


        Token INT4=null;
        AssessQueryParser.constant_benchmark_return constant_benchmark1 =null;

        AssessQueryParser.external_benchmark_return external_benchmark2 =null;

        AssessQueryParser.predicate_return predicate3 =null;


        parsedBenchmark = new ArrayList<>();
        try {
            // AssessQuery.g:81:5: ( constant_benchmark | external_benchmark | predicate | PAST INT )
            int alt13=4;
            switch ( input.LA(1) ) {
            case FLOAT:
            case INT:
            case SIGN:
                {
                alt13=1;
                }
                break;
            case ID:
                {
                int LA13_2 = input.LA(2);

                if ( (LA13_2==50) ) {
                    alt13=2;
                }
                else if ( (LA13_2==53) ) {
                    alt13=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return parsedBenchmark;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 2, input);

                    throw nvae;

                }
                }
                break;
            case PAST:
                {
                alt13=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return parsedBenchmark;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // AssessQuery.g:81:7: constant_benchmark
                    {
                    pushFollow(FOLLOW_constant_benchmark_in_benchmark588);
                    constant_benchmark1=constant_benchmark();

                    state._fsp--;
                    if (state.failed) return parsedBenchmark;

                    if ( state.backtracking==0 ) {parsedBenchmark.add("Constant");
                        parsedBenchmark.add((constant_benchmark1!=null?input.toString(constant_benchmark1.start,constant_benchmark1.stop):null));
                        }

                    }
                    break;
                case 2 :
                    // AssessQuery.g:85:7: external_benchmark
                    {
                    pushFollow(FOLLOW_external_benchmark_in_benchmark602);
                    external_benchmark2=external_benchmark();

                    state._fsp--;
                    if (state.failed) return parsedBenchmark;

                    if ( state.backtracking==0 ) {parsedBenchmark.add("External");
                         parsedBenchmark.add((external_benchmark2!=null?external_benchmark2.cube:null));
                         parsedBenchmark.add((external_benchmark2!=null?external_benchmark2.measurement:null));
                         }

                    }
                    break;
                case 3 :
                    // AssessQuery.g:90:7: predicate
                    {
                    pushFollow(FOLLOW_predicate_in_benchmark616);
                    predicate3=predicate();

                    state._fsp--;
                    if (state.failed) return parsedBenchmark;

                    if ( state.backtracking==0 ) {parsedBenchmark.add("Sibling");
                         parsedBenchmark.add((predicate3!=null?predicate3.level:null));
                         parsedBenchmark.add((predicate3!=null?predicate3.value:null));
                        }

                    }
                    break;
                case 4 :
                    // AssessQuery.g:95:7: PAST INT
                    {
                    match(input,PAST,FOLLOW_PAST_in_benchmark630); if (state.failed) return parsedBenchmark;

                    INT4=(Token)match(input,INT,FOLLOW_INT_in_benchmark632); if (state.failed) return parsedBenchmark;

                    if ( state.backtracking==0 ) {parsedBenchmark.add("Past");
                        parsedBenchmark.add((INT4!=null?INT4.getText():null));
                        }

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
    // AssessQuery.g:101:1: constant_benchmark : ( SIGN )? number= ( INT | FLOAT ) ;
    public final AssessQueryParser.constant_benchmark_return constant_benchmark() throws RecognitionException {
        AssessQueryParser.constant_benchmark_return retval = new AssessQueryParser.constant_benchmark_return();
        retval.start = input.LT(1);


        Token number=null;

        try {
            // AssessQuery.g:101:20: ( ( SIGN )? number= ( INT | FLOAT ) )
            // AssessQuery.g:101:22: ( SIGN )? number= ( INT | FLOAT )
            {
            // AssessQuery.g:101:22: ( SIGN )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==SIGN) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // AssessQuery.g:101:23: SIGN
                    {
                    match(input,SIGN,FOLLOW_SIGN_in_constant_benchmark652); if (state.failed) return retval;

                    }
                    break;

            }


            number=(Token)input.LT(1);

            if ( input.LA(1)==FLOAT||input.LA(1)==INT ) {
                input.consume();
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
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
    // AssessQuery.g:103:1: external_benchmark returns [String cube, String measurement] : benchmarkCube= ID '.' benchmarkMeasurement= ID ;
    public final AssessQueryParser.external_benchmark_return external_benchmark() throws RecognitionException {
        AssessQueryParser.external_benchmark_return retval = new AssessQueryParser.external_benchmark_return();
        retval.start = input.LT(1);


        Token benchmarkCube=null;
        Token benchmarkMeasurement=null;

        try {
            // AssessQuery.g:104:5: (benchmarkCube= ID '.' benchmarkMeasurement= ID )
            // AssessQuery.g:104:7: benchmarkCube= ID '.' benchmarkMeasurement= ID
            {
            benchmarkCube=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark685); if (state.failed) return retval;

            if ( state.backtracking==0 ) {retval.cube = (benchmarkCube!=null?benchmarkCube.getText():null);}

            match(input,50,FOLLOW_50_in_external_benchmark689); if (state.failed) return retval;

            benchmarkMeasurement=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark699); if (state.failed) return retval;

            if ( state.backtracking==0 ) {retval.measurement = (benchmarkMeasurement!=null?benchmarkMeasurement.getText():null);}

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
    // AssessQuery.g:107:1: comparison_scheme[List<String> comparisonMethods] returns [List<String> updatedComparisonMethods] : method_name= ID '(' ( ( transformed_operand ',' )=> comparison_args | comparison_scheme[$updatedComparisonMethods] ) ')' ;
    public final List<String> comparison_scheme(List<String> comparisonMethods) throws RecognitionException {
        List<String> updatedComparisonMethods = null;


        Token method_name=null;

        updatedComparisonMethods = comparisonMethods;
        try {
            // AssessQuery.g:109:5: (method_name= ID '(' ( ( transformed_operand ',' )=> comparison_args | comparison_scheme[$updatedComparisonMethods] ) ')' )
            // AssessQuery.g:109:7: method_name= ID '(' ( ( transformed_operand ',' )=> comparison_args | comparison_scheme[$updatedComparisonMethods] ) ')'
            {
            method_name=(Token)match(input,ID,FOLLOW_ID_in_comparison_scheme731); if (state.failed) return updatedComparisonMethods;

            if ( state.backtracking==0 ) {updatedComparisonMethods.add((method_name!=null?method_name.getText():null));}

            match(input,46,FOLLOW_46_in_comparison_scheme739); if (state.failed) return updatedComparisonMethods;

            // AssessQuery.g:110:9: ( ( transformed_operand ',' )=> comparison_args | comparison_scheme[$updatedComparisonMethods] )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==ID) ) {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==46) ) {
                    switch ( input.LA(3) ) {
                    case 57:
                        {
                        int LA15_7 = input.LA(4);

                        if ( (LA15_7==ID) ) {
                            int LA15_11 = input.LA(5);

                            if ( (LA15_11==47) && (synpred1_AssessQuery())) {
                                alt15=1;
                            }
                            else if ( (LA15_11==49) ) {
                                alt15=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 15, 11, input);

                                throw nvae;

                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 7, input);

                            throw nvae;

                        }
                        }
                        break;
                    case ID:
                        {
                        int LA15_8 = input.LA(4);

                        if ( (LA15_8==46||LA15_8==49) ) {
                            alt15=2;
                        }
                        else if ( (LA15_8==47) && (synpred1_AssessQuery())) {
                            alt15=1;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 8, input);

                            throw nvae;

                        }
                        }
                        break;
                    case INT:
                        {
                        int LA15_9 = input.LA(4);

                        if ( (LA15_9==47) && (synpred1_AssessQuery())) {
                            alt15=1;
                        }
                        else if ( (LA15_9==49) ) {
                            alt15=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 9, input);

                            throw nvae;

                        }
                        }
                        break;
                    case FLOAT:
                        {
                        int LA15_10 = input.LA(4);

                        if ( (LA15_10==47) && (synpred1_AssessQuery())) {
                            alt15=1;
                        }
                        else if ( (LA15_10==49) ) {
                            alt15=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 15, 10, input);

                            throw nvae;

                        }
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 5, input);

                        throw nvae;

                    }

                }
                else if ( (LA15_1==49) && (synpred1_AssessQuery())) {
                    alt15=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA15_0==57) && (synpred1_AssessQuery())) {
                alt15=1;
            }
            else if ( (LA15_0==INT) && (synpred1_AssessQuery())) {
                alt15=1;
            }
            else if ( (LA15_0==FLOAT) && (synpred1_AssessQuery())) {
                alt15=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return updatedComparisonMethods;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }
            switch (alt15) {
                case 1 :
                    // AssessQuery.g:110:11: ( transformed_operand ',' )=> comparison_args
                    {
                    pushFollow(FOLLOW_comparison_args_in_comparison_scheme750);
                    comparison_args();

                    state._fsp--;
                    if (state.failed) return updatedComparisonMethods;

                    }
                    break;
                case 2 :
                    // AssessQuery.g:111:11: comparison_scheme[$updatedComparisonMethods]
                    {
                    pushFollow(FOLLOW_comparison_scheme_in_comparison_scheme762);
                    comparison_scheme(updatedComparisonMethods);

                    state._fsp--;
                    if (state.failed) return updatedComparisonMethods;

                    }
                    break;

            }


            match(input,47,FOLLOW_47_in_comparison_scheme767); if (state.failed) return updatedComparisonMethods;

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
    // AssessQuery.g:113:1: comparison_args : first= transformed_operand ',' second= transformed_operand ;
    public final void comparison_args() throws RecognitionException {
        String first =null;

        String second =null;


        try {
            // AssessQuery.g:114:5: (first= transformed_operand ',' second= transformed_operand )
            // AssessQuery.g:114:7: first= transformed_operand ',' second= transformed_operand
            {
            pushFollow(FOLLOW_transformed_operand_in_comparison_args783);
            first=transformed_operand();

            state._fsp--;
            if (state.failed) return ;

            match(input,49,FOLLOW_49_in_comparison_args785); if (state.failed) return ;

            pushFollow(FOLLOW_transformed_operand_in_comparison_args791);
            second=transformed_operand();

            state._fsp--;
            if (state.failed) return ;

            if ( state.backtracking==0 ) { if (builder != null) builder.setDeltaOperands(first, second); }

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



    // $ANTLR start "transformed_operand"
    // AssessQuery.g:118:1: transformed_operand returns [String ref] : ( ( ID '(' )=>name= ID '(' inner= operand_ref ')' |o= operand_ref );
    public final String transformed_operand() throws RecognitionException {
        String ref = null;


        Token name=null;
        String inner =null;

        String o =null;


        try {
            // AssessQuery.g:119:5: ( ( ID '(' )=>name= ID '(' inner= operand_ref ')' |o= operand_ref )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ID) ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==46) && (synpred2_AssessQuery())) {
                    alt16=1;
                }
                else if ( (LA16_1==47||LA16_1==49) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ref;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA16_0==FLOAT||LA16_0==INT||LA16_0==57) ) {
                alt16=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ref;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
                case 1 :
                    // AssessQuery.g:119:7: ( ID '(' )=>name= ID '(' inner= operand_ref ')'
                    {
                    name=(Token)match(input,ID,FOLLOW_ID_in_transformed_operand831); if (state.failed) return ref;

                    match(input,46,FOLLOW_46_in_transformed_operand833); if (state.failed) return ref;

                    pushFollow(FOLLOW_operand_ref_in_transformed_operand839);
                    inner=operand_ref();

                    state._fsp--;
                    if (state.failed) return ref;

                    match(input,47,FOLLOW_47_in_transformed_operand841); if (state.failed) return ref;

                    if ( state.backtracking==0 ) { ref = (name!=null?name.getText():null) + "(" + inner + ")"; }

                    }
                    break;
                case 2 :
                    // AssessQuery.g:120:7: o= operand_ref
                    {
                    pushFollow(FOLLOW_operand_ref_in_transformed_operand855);
                    o=operand_ref();

                    state._fsp--;
                    if (state.failed) return ref;

                    if ( state.backtracking==0 ) { ref = o; }

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
    // $ANTLR end "transformed_operand"



    // $ANTLR start "operand_ref"
    // AssessQuery.g:123:1: operand_ref returns [String ref] : ( 'benchmark.' id= ID |id= ID |n= INT |f= FLOAT );
    public final String operand_ref() throws RecognitionException {
        String ref = null;


        Token id=null;
        Token n=null;
        Token f=null;

        try {
            // AssessQuery.g:124:5: ( 'benchmark.' id= ID |id= ID |n= INT |f= FLOAT )
            int alt17=4;
            switch ( input.LA(1) ) {
            case 57:
                {
                alt17=1;
                }
                break;
            case ID:
                {
                alt17=2;
                }
                break;
            case INT:
                {
                alt17=3;
                }
                break;
            case FLOAT:
                {
                alt17=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ref;}
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }

            switch (alt17) {
                case 1 :
                    // AssessQuery.g:124:7: 'benchmark.' id= ID
                    {
                    match(input,57,FOLLOW_57_in_operand_ref878); if (state.failed) return ref;

                    id=(Token)match(input,ID,FOLLOW_ID_in_operand_ref884); if (state.failed) return ref;

                    if ( state.backtracking==0 ) { ref = "benchmark." + (id!=null?id.getText():null); }

                    }
                    break;
                case 2 :
                    // AssessQuery.g:125:7: id= ID
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_operand_ref898); if (state.failed) return ref;

                    if ( state.backtracking==0 ) { ref = (id!=null?id.getText():null); }

                    }
                    break;
                case 3 :
                    // AssessQuery.g:126:7: n= INT
                    {
                    n=(Token)match(input,INT,FOLLOW_INT_in_operand_ref912); if (state.failed) return ref;

                    if ( state.backtracking==0 ) { ref = (n!=null?n.getText():null); }

                    }
                    break;
                case 4 :
                    // AssessQuery.g:127:7: f= FLOAT
                    {
                    f=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_operand_ref926); if (state.failed) return ref;

                    if ( state.backtracking==0 ) { ref = (f!=null?f.getText():null); }

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
    // AssessQuery.g:130:1: target_measure : e= measure_expression ( AS alias= ID )? ;
    public final void target_measure() throws RecognitionException {
        Token alias=null;
        AssessQueryParser.measure_expression_return e =null;


        try {
            // AssessQuery.g:131:5: (e= measure_expression ( AS alias= ID )? )
            // AssessQuery.g:131:7: e= measure_expression ( AS alias= ID )?
            {
            pushFollow(FOLLOW_measure_expression_in_target_measure949);
            e=measure_expression();

            state._fsp--;
            if (state.failed) return ;

            // AssessQuery.g:131:30: ( AS alias= ID )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==AS) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // AssessQuery.g:131:31: AS alias= ID
                    {
                    match(input,AS,FOLLOW_AS_in_target_measure952); if (state.failed) return ;

                    alias=(Token)match(input,ID,FOLLOW_ID_in_target_measure958); if (state.failed) return ;

                    }
                    break;

            }


            if ( state.backtracking==0 ) { builder.setTargetMeasure((e!=null?input.toString(e.start,e.stop):null), (alias!=null?alias.getText():null)); }

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
    // AssessQuery.g:135:1: measure_expression : measure_term ( SIGN measure_term )* ;
    public final AssessQueryParser.measure_expression_return measure_expression() throws RecognitionException {
        AssessQueryParser.measure_expression_return retval = new AssessQueryParser.measure_expression_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:136:5: ( measure_term ( SIGN measure_term )* )
            // AssessQuery.g:136:7: measure_term ( SIGN measure_term )*
            {
            pushFollow(FOLLOW_measure_term_in_measure_expression985);
            measure_term();

            state._fsp--;
            if (state.failed) return retval;

            // AssessQuery.g:136:20: ( SIGN measure_term )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==SIGN) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // AssessQuery.g:136:21: SIGN measure_term
            	    {
            	    match(input,SIGN,FOLLOW_SIGN_in_measure_expression988); if (state.failed) return retval;

            	    pushFollow(FOLLOW_measure_term_in_measure_expression990);
            	    measure_term();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop19;
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
    // AssessQuery.g:139:1: measure_term : measure_factor ( ( '*' | '/' ) measure_factor )* ;
    public final void measure_term() throws RecognitionException {
        try {
            // AssessQuery.g:140:5: ( measure_factor ( ( '*' | '/' ) measure_factor )* )
            // AssessQuery.g:140:7: measure_factor ( ( '*' | '/' ) measure_factor )*
            {
            pushFollow(FOLLOW_measure_factor_in_measure_term1009);
            measure_factor();

            state._fsp--;
            if (state.failed) return ;

            // AssessQuery.g:140:22: ( ( '*' | '/' ) measure_factor )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==48||LA20_0==51) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // AssessQuery.g:140:23: ( '*' | '/' ) measure_factor
            	    {
            	    if ( input.LA(1)==48||input.LA(1)==51 ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_measure_factor_in_measure_term1020);
            	    measure_factor();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop20;
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
    // AssessQuery.g:143:1: measure_factor : ( AGGREGATE '(' measure_expression ')' | '(' measure_expression ')' | ID | INT | FLOAT );
    public final void measure_factor() throws RecognitionException {
        try {
            // AssessQuery.g:144:5: ( AGGREGATE '(' measure_expression ')' | '(' measure_expression ')' | ID | INT | FLOAT )
            int alt21=5;
            switch ( input.LA(1) ) {
            case AGGREGATE:
                {
                alt21=1;
                }
                break;
            case 46:
                {
                alt21=2;
                }
                break;
            case ID:
                {
                alt21=3;
                }
                break;
            case INT:
                {
                alt21=4;
                }
                break;
            case FLOAT:
                {
                alt21=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }

            switch (alt21) {
                case 1 :
                    // AssessQuery.g:144:7: AGGREGATE '(' measure_expression ')'
                    {
                    match(input,AGGREGATE,FOLLOW_AGGREGATE_in_measure_factor1039); if (state.failed) return ;

                    match(input,46,FOLLOW_46_in_measure_factor1041); if (state.failed) return ;

                    pushFollow(FOLLOW_measure_expression_in_measure_factor1043);
                    measure_expression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,47,FOLLOW_47_in_measure_factor1045); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // AssessQuery.g:145:7: '(' measure_expression ')'
                    {
                    match(input,46,FOLLOW_46_in_measure_factor1053); if (state.failed) return ;

                    pushFollow(FOLLOW_measure_expression_in_measure_factor1055);
                    measure_expression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,47,FOLLOW_47_in_measure_factor1057); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // AssessQuery.g:146:7: ID
                    {
                    match(input,ID,FOLLOW_ID_in_measure_factor1065); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // AssessQuery.g:147:7: INT
                    {
                    match(input,INT,FOLLOW_INT_in_measure_factor1073); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // AssessQuery.g:148:7: FLOAT
                    {
                    match(input,FLOAT,FOLLOW_FLOAT_in_measure_factor1081); if (state.failed) return ;

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
    // AssessQuery.g:151:1: labeler : (labelingSystem= custom_labeling ( AS customName= ID )? |schemeName= ID ( '(' parsedArgs= labeler_args ')' )? );
    public final void labeler() throws RecognitionException {
        Token customName=null;
        Token schemeName=null;
        List<List<String>> labelingSystem =null;

        List<String> parsedArgs =null;


         List<String> schemeArgs = null; 
        try {
            // AssessQuery.g:153:5: (labelingSystem= custom_labeling ( AS customName= ID )? |schemeName= ID ( '(' parsedArgs= labeler_args ')' )? )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==59) ) {
                alt24=1;
            }
            else if ( (LA24_0==ID) ) {
                alt24=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;

            }
            switch (alt24) {
                case 1 :
                    // AssessQuery.g:153:7: labelingSystem= custom_labeling ( AS customName= ID )?
                    {
                    pushFollow(FOLLOW_custom_labeling_in_labeler1111);
                    labelingSystem=custom_labeling();

                    state._fsp--;
                    if (state.failed) return ;

                    // AssessQuery.g:153:40: ( AS customName= ID )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==AS) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // AssessQuery.g:153:41: AS customName= ID
                            {
                            match(input,AS,FOLLOW_AS_in_labeler1114); if (state.failed) return ;

                            customName=(Token)match(input,ID,FOLLOW_ID_in_labeler1120); if (state.failed) return ;

                            }
                            break;

                    }


                    if ( state.backtracking==0 ) {builder.addCustomLabeler(labelingSystem, (customName!=null?customName.getText():null));}

                    }
                    break;
                case 2 :
                    // AssessQuery.g:155:7: schemeName= ID ( '(' parsedArgs= labeler_args ')' )?
                    {
                    schemeName=(Token)match(input,ID,FOLLOW_ID_in_labeler1142); if (state.failed) return ;

                    // AssessQuery.g:155:23: ( '(' parsedArgs= labeler_args ')' )?
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==46) ) {
                        alt23=1;
                    }
                    switch (alt23) {
                        case 1 :
                            // AssessQuery.g:155:24: '(' parsedArgs= labeler_args ')'
                            {
                            match(input,46,FOLLOW_46_in_labeler1145); if (state.failed) return ;

                            pushFollow(FOLLOW_labeler_args_in_labeler1151);
                            parsedArgs=labeler_args();

                            state._fsp--;
                            if (state.failed) return ;

                            if ( state.backtracking==0 ) {schemeArgs = parsedArgs;}

                            match(input,47,FOLLOW_47_in_labeler1155); if (state.failed) return ;

                            }
                            break;

                    }


                    if ( state.backtracking==0 ) {builder.addNamedLabeler((schemeName!=null?schemeName.getText():null), schemeArgs);}

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
    // AssessQuery.g:159:1: labeler_args returns [List<String> args] : a= labeler_arg ( ',' b= labeler_arg )* ;
    public final List<String> labeler_args() throws RecognitionException {
        List<String> args = null;


        AssessQueryParser.labeler_arg_return a =null;

        AssessQueryParser.labeler_arg_return b =null;


        args = new ArrayList<String>();
        try {
            // AssessQuery.g:161:5: (a= labeler_arg ( ',' b= labeler_arg )* )
            // AssessQuery.g:161:7: a= labeler_arg ( ',' b= labeler_arg )*
            {
            pushFollow(FOLLOW_labeler_arg_in_labeler_args1199);
            a=labeler_arg();

            state._fsp--;
            if (state.failed) return args;

            if ( state.backtracking==0 ) {args.add((a!=null?input.toString(a.start,a.stop):null));}

            // AssessQuery.g:162:7: ( ',' b= labeler_arg )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==49) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // AssessQuery.g:162:8: ',' b= labeler_arg
            	    {
            	    match(input,49,FOLLOW_49_in_labeler_args1210); if (state.failed) return args;

            	    pushFollow(FOLLOW_labeler_arg_in_labeler_args1216);
            	    b=labeler_arg();

            	    state._fsp--;
            	    if (state.failed) return args;

            	    if ( state.backtracking==0 ) {args.add((b!=null?input.toString(b.start,b.stop):null));}

            	    }
            	    break;

            	default :
            	    break loop25;
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
    // AssessQuery.g:165:1: labeler_arg : ( ID | INT | FLOAT );
    public final AssessQueryParser.labeler_arg_return labeler_arg() throws RecognitionException {
        AssessQueryParser.labeler_arg_return retval = new AssessQueryParser.labeler_arg_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:165:13: ( ID | INT | FLOAT )
            // AssessQuery.g:
            {
            if ( input.LA(1)==FLOAT||(input.LA(1) >= ID && input.LA(1) <= INT) ) {
                input.consume();
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
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
    // AssessQuery.g:167:1: custom_labeling returns [List<List<String>> labelingTerms] : '{' term= label_term ( ',' term= label_term )* '}' ;
    public final List<List<String>> custom_labeling() throws RecognitionException {
        List<List<String>> labelingTerms = null;


        List<String> term =null;


        labelingTerms = new ArrayList<List<String>>();
        try {
            // AssessQuery.g:169:5: ( '{' term= label_term ( ',' term= label_term )* '}' )
            // AssessQuery.g:169:7: '{' term= label_term ( ',' term= label_term )* '}'
            {
            match(input,59,FOLLOW_59_in_custom_labeling1266); if (state.failed) return labelingTerms;

            pushFollow(FOLLOW_label_term_in_custom_labeling1272);
            term=label_term();

            state._fsp--;
            if (state.failed) return labelingTerms;

            if ( state.backtracking==0 ) {labelingTerms.add(term);}

            // AssessQuery.g:170:5: ( ',' term= label_term )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==49) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // AssessQuery.g:170:6: ',' term= label_term
            	    {
            	    match(input,49,FOLLOW_49_in_custom_labeling1281); if (state.failed) return labelingTerms;

            	    pushFollow(FOLLOW_label_term_in_custom_labeling1287);
            	    term=label_term();

            	    state._fsp--;
            	    if (state.failed) return labelingTerms;

            	    if ( state.backtracking==0 ) {labelingTerms.add(term);}

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);


            match(input,60,FOLLOW_60_in_custom_labeling1293); if (state.failed) return labelingTerms;

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
    // AssessQuery.g:173:1: label_term returns [List<String> term] : range= label_range ':' label= ID ;
    public final List<String> label_term() throws RecognitionException {
        List<String> term = null;


        Token label=null;
        List<String> range =null;


        try {
            // AssessQuery.g:175:5: (range= label_range ':' label= ID )
            // AssessQuery.g:175:7: range= label_range ':' label= ID
            {
            pushFollow(FOLLOW_label_range_in_label_term1324);
            range=label_range();

            state._fsp--;
            if (state.failed) return term;

            match(input,52,FOLLOW_52_in_label_term1326); if (state.failed) return term;

            label=(Token)match(input,ID,FOLLOW_ID_in_label_term1330); if (state.failed) return term;

            if ( state.backtracking==0 ) {range.add((label!=null?label.getText():null)); }

            }

            if ( state.backtracking==0 ) {term = range;}
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
    // AssessQuery.g:177:1: label_range returns [List<String> limits] : (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) ;
    public final List<String> label_range() throws RecognitionException {
        List<String> limits = null;


        Token lowLimit=null;
        Token highLimit=null;
        AssessQueryParser.range_point_return start =null;

        AssessQueryParser.range_point_return end =null;


        limits = new ArrayList<String>();
        try {
            // AssessQuery.g:179:5: ( (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) )
            // AssessQuery.g:179:7: (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' )
            {
            // AssessQuery.g:179:7: (lowLimit= '[' |lowLimit= '(' )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==54) ) {
                alt27=1;
            }
            else if ( (LA27_0==46) ) {
                alt27=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return limits;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;

            }
            switch (alt27) {
                case 1 :
                    // AssessQuery.g:179:9: lowLimit= '['
                    {
                    lowLimit=(Token)match(input,54,FOLLOW_54_in_label_range1363); if (state.failed) return limits;

                    }
                    break;
                case 2 :
                    // AssessQuery.g:179:26: lowLimit= '('
                    {
                    lowLimit=(Token)match(input,46,FOLLOW_46_in_label_range1371); if (state.failed) return limits;

                    }
                    break;

            }


            if ( state.backtracking==0 ) {limits.add((lowLimit!=null?lowLimit.getText():null));}

            pushFollow(FOLLOW_range_point_in_label_range1387);
            start=range_point();

            state._fsp--;
            if (state.failed) return limits;

            if ( state.backtracking==0 ) { limits.add((start!=null?input.toString(start.start,start.stop):null)); }

            match(input,49,FOLLOW_49_in_label_range1391); if (state.failed) return limits;

            pushFollow(FOLLOW_range_point_in_label_range1403);
            end=range_point();

            state._fsp--;
            if (state.failed) return limits;

            if ( state.backtracking==0 ) { limits.add((end!=null?input.toString(end.start,end.stop):null)); }

            // AssessQuery.g:182:7: (highLimit= ')' |highLimit= ']' )
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==47) ) {
                alt28=1;
            }
            else if ( (LA28_0==56) ) {
                alt28=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return limits;}
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;

            }
            switch (alt28) {
                case 1 :
                    // AssessQuery.g:182:9: highLimit= ')'
                    {
                    highLimit=(Token)match(input,47,FOLLOW_47_in_label_range1419); if (state.failed) return limits;

                    }
                    break;
                case 2 :
                    // AssessQuery.g:182:27: highLimit= ']'
                    {
                    highLimit=(Token)match(input,56,FOLLOW_56_in_label_range1427); if (state.failed) return limits;

                    }
                    break;

            }


            if ( state.backtracking==0 ) {limits.add((highLimit!=null?highLimit.getText():null));}

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
    // AssessQuery.g:185:1: range_point : ( SIGN )? ( INT | FLOAT | 'inf' ) ;
    public final AssessQueryParser.range_point_return range_point() throws RecognitionException {
        AssessQueryParser.range_point_return retval = new AssessQueryParser.range_point_return();
        retval.start = input.LT(1);


        try {
            // AssessQuery.g:185:13: ( ( SIGN )? ( INT | FLOAT | 'inf' ) )
            // AssessQuery.g:185:15: ( SIGN )? ( INT | FLOAT | 'inf' )
            {
            // AssessQuery.g:185:15: ( SIGN )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==SIGN) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // AssessQuery.g:185:15: SIGN
                    {
                    match(input,SIGN,FOLLOW_SIGN_in_range_point1443); if (state.failed) return retval;

                    }
                    break;

            }


            if ( input.LA(1)==FLOAT||input.LA(1)==INT||input.LA(1)==58 ) {
                input.consume();
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
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

    // $ANTLR start synpred1_AssessQuery
    public final void synpred1_AssessQuery_fragment() throws RecognitionException {
        // AssessQuery.g:110:11: ( transformed_operand ',' )
        // AssessQuery.g:110:12: transformed_operand ','
        {
        pushFollow(FOLLOW_transformed_operand_in_synpred1_AssessQuery744);
        transformed_operand();

        state._fsp--;
        if (state.failed) return ;

        match(input,49,FOLLOW_49_in_synpred1_AssessQuery746); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred1_AssessQuery

    // $ANTLR start synpred2_AssessQuery
    public final void synpred2_AssessQuery_fragment() throws RecognitionException {
        // AssessQuery.g:119:7: ( ID '(' )
        // AssessQuery.g:119:8: ID '('
        {
        match(input,ID,FOLLOW_ID_in_synpred2_AssessQuery821); if (state.failed) return ;

        match(input,46,FOLLOW_46_in_synpred2_AssessQuery823); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_AssessQuery

    // Delegated rules

    public final boolean synpred1_AssessQuery() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_AssessQuery_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_AssessQuery() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_AssessQuery_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

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
    public static final BitSet FOLLOW_AGAINST_in_query158 = new BitSet(new long[]{0x0000000840308000L});
    public static final BitSet FOLLOW_benchmark_in_query164 = new BitSet(new long[]{0x0002004002000000L});
    public static final BitSet FOLLOW_USING_in_query185 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_comparison_scheme_in_query191 = new BitSet(new long[]{0x0002000002000000L});
    public static final BitSet FOLLOW_49_in_query215 = new BitSet(new long[]{0x0000000840308000L});
    public static final BitSet FOLLOW_benchmark_in_query221 = new BitSet(new long[]{0x0002004002000000L});
    public static final BitSet FOLLOW_USING_in_query242 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_comparison_scheme_in_query248 = new BitSet(new long[]{0x0002000002000000L});
    public static final BitSet FOLLOW_USING_in_query274 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_comparison_scheme_in_query280 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_LABELS_in_query316 = new BitSet(new long[]{0x0800000000100000L});
    public static final BitSet FOLLOW_labeler_in_query318 = new BitSet(new long[]{0x0002000400000002L});
    public static final BitSet FOLLOW_49_in_query321 = new BitSet(new long[]{0x0800000000100000L});
    public static final BitSet FOLLOW_labeler_in_query323 = new BitSet(new long[]{0x0002000400000002L});
    public static final BitSet FOLLOW_SAVE_in_query334 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_AS_in_query336 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_query342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_selection_predicates397 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_49_in_selection_predicates406 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_predicate_in_selection_predicates412 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_ID_in_predicate441 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_predicate449 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_55_in_predicate451 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_level_value_in_predicate457 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_55_in_predicate461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_level_value474 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_date_in_level_value479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_date492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_date500 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_date502 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_date504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_date512 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_date514 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_date516 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_date518 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_date520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_group_by_set546 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_49_in_group_by_set551 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_group_by_set555 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_constant_benchmark_in_benchmark588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_benchmark_in_benchmark602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_benchmark616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PAST_in_benchmark630 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_INT_in_benchmark632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_in_constant_benchmark652 = new BitSet(new long[]{0x0000000000208000L});
    public static final BitSet FOLLOW_set_in_constant_benchmark660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_external_benchmark685 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_external_benchmark689 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_external_benchmark699 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_comparison_scheme731 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_comparison_scheme739 = new BitSet(new long[]{0x0200000000308000L});
    public static final BitSet FOLLOW_comparison_args_in_comparison_scheme750 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_comparison_scheme_in_comparison_scheme762 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_comparison_scheme767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_transformed_operand_in_comparison_args783 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_comparison_args785 = new BitSet(new long[]{0x0200000000308000L});
    public static final BitSet FOLLOW_transformed_operand_in_comparison_args791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_transformed_operand831 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_transformed_operand833 = new BitSet(new long[]{0x0200000000308000L});
    public static final BitSet FOLLOW_operand_ref_in_transformed_operand839 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_transformed_operand841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operand_ref_in_transformed_operand855 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_operand_ref878 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_operand_ref884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_operand_ref898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_operand_ref912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_operand_ref926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measure_expression_in_target_measure949 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_AS_in_target_measure952 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_target_measure958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measure_term_in_measure_expression985 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_SIGN_in_measure_expression988 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_term_in_measure_expression990 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_measure_factor_in_measure_term1009 = new BitSet(new long[]{0x0009000000000002L});
    public static final BitSet FOLLOW_set_in_measure_term1012 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_factor_in_measure_term1020 = new BitSet(new long[]{0x0009000000000002L});
    public static final BitSet FOLLOW_AGGREGATE_in_measure_factor1039 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_measure_factor1041 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_expression_in_measure_factor1043 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_measure_factor1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_measure_factor1053 = new BitSet(new long[]{0x0000400000308040L});
    public static final BitSet FOLLOW_measure_expression_in_measure_factor1055 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_measure_factor1057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_measure_factor1065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_measure_factor1073 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_measure_factor1081 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_custom_labeling_in_labeler1111 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_AS_in_labeler1114 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_labeler1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_labeler1142 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_46_in_labeler1145 = new BitSet(new long[]{0x0000000000308000L});
    public static final BitSet FOLLOW_labeler_args_in_labeler1151 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_labeler1155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labeler_arg_in_labeler_args1199 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_49_in_labeler_args1210 = new BitSet(new long[]{0x0000000000308000L});
    public static final BitSet FOLLOW_labeler_arg_in_labeler_args1216 = new BitSet(new long[]{0x0002000000000002L});
    public static final BitSet FOLLOW_59_in_custom_labeling1266 = new BitSet(new long[]{0x0040400000000000L});
    public static final BitSet FOLLOW_label_term_in_custom_labeling1272 = new BitSet(new long[]{0x1002000000000000L});
    public static final BitSet FOLLOW_49_in_custom_labeling1281 = new BitSet(new long[]{0x0040400000000000L});
    public static final BitSet FOLLOW_label_term_in_custom_labeling1287 = new BitSet(new long[]{0x1002000000000000L});
    public static final BitSet FOLLOW_60_in_custom_labeling1293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_label_range_in_label_term1324 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_label_term1326 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_label_term1330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_label_range1363 = new BitSet(new long[]{0x0400000800208000L});
    public static final BitSet FOLLOW_46_in_label_range1371 = new BitSet(new long[]{0x0400000800208000L});
    public static final BitSet FOLLOW_range_point_in_label_range1387 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_label_range1391 = new BitSet(new long[]{0x0400000800208000L});
    public static final BitSet FOLLOW_range_point_in_label_range1403 = new BitSet(new long[]{0x0100800000000000L});
    public static final BitSet FOLLOW_47_in_label_range1419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_label_range1427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SIGN_in_range_point1443 = new BitSet(new long[]{0x0400000000208000L});
    public static final BitSet FOLLOW_set_in_range_point1446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_transformed_operand_in_synpred1_AssessQuery744 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred1_AssessQuery746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_synpred2_AssessQuery821 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_synpred2_AssessQuery823 = new BitSet(new long[]{0x0000000000000002L});

}