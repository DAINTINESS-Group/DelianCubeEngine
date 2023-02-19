// $ANTLR 3.5.2 AssessQuery.g 2023-02-19 20:14:07

package assess.syntax;
import assess.AssessQuery;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import assess.AssessQueryBuilder;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class AssessQueryParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "AGAINST", "ASSESS", "B", 
		"BY", "E", "F", "FLOAT", "FOR", "G", "H", "I", "ID", "INT", "L", "LABELS", 
		"N", "O", "P", "PAST", "R", "S", "SIGN", "T", "U", "USING", "W", "WITH", 
		"WS", "Y", "'('", "')'", "','", "'.'", "'/'", "':'", "'='", "'['", "'\\''", 
		"']'", "'benchmark.'", "'inf'", "'{'", "'}'"
	};
	public static final int EOF=-1;
	public static final int T__34=34;
	public static final int T__35=35;
	public static final int T__36=36;
	public static final int T__37=37;
	public static final int T__38=38;
	public static final int T__39=39;
	public static final int T__40=40;
	public static final int T__41=41;
	public static final int T__42=42;
	public static final int T__43=43;
	public static final int T__44=44;
	public static final int T__45=45;
	public static final int T__46=46;
	public static final int T__47=47;
	public static final int A=4;
	public static final int AGAINST=5;
	public static final int ASSESS=6;
	public static final int B=7;
	public static final int BY=8;
	public static final int E=9;
	public static final int F=10;
	public static final int FLOAT=11;
	public static final int FOR=12;
	public static final int G=13;
	public static final int H=14;
	public static final int I=15;
	public static final int ID=16;
	public static final int INT=17;
	public static final int L=18;
	public static final int LABELS=19;
	public static final int N=20;
	public static final int O=21;
	public static final int P=22;
	public static final int PAST=23;
	public static final int R=24;
	public static final int S=25;
	public static final int SIGN=26;
	public static final int T=27;
	public static final int U=28;
	public static final int USING=29;
	public static final int W=30;
	public static final int WITH=31;
	public static final int WS=32;
	public static final int Y=33;

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

	@Override public String[] getTokenNames() { return AssessQueryParser.tokenNames; }
	@Override public String getGrammarFileName() { return "AssessQuery.g"; }


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
	// AssessQuery.g:25:1: query returns [AssessQuery query] : WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling ) ;
	public final AssessQuery query() throws RecognitionException {
		AssessQuery query = null;


		Token targetCube=null;
		Token measurement=null;
		Token labelingMethod=null;
		HashMap<String, String> predicates =null;
		HashSet<String> gammas =null;
		List<String> parsedBenchmark =null;
		List<String> updatedComparisonMethods =null;
		List<List<String>> labelingSystem =null;


		    List<String> comparisonMethods = new ArrayList<String>();
		    
		try {
			// AssessQuery.g:29:5: ( WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling ) )
			// AssessQuery.g:29:7: WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling )
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
			measurement=(Token)match(input,ID,FOLLOW_ID_in_query151); 
			builder.setMeasurement((measurement!=null?measurement.getText():null));
			builder.buildTargetCubeQueryString();
			// AssessQuery.g:35:7: ( AGAINST parsedBenchmark= benchmark )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==AGAINST) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// AssessQuery.g:35:8: AGAINST parsedBenchmark= benchmark
					{
					match(input,AGAINST,FOLLOW_AGAINST_in_query171); 
					pushFollow(FOLLOW_benchmark_in_query177);
					parsedBenchmark=benchmark();
					state._fsp--;

					}
					break;

			}

			// AssessQuery.g:38:7: ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==USING) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// AssessQuery.g:38:8: USING updatedComparisonMethods= comparison_scheme[comparisonMethods]
					{
					match(input,USING,FOLLOW_USING_in_query196); 
					pushFollow(FOLLOW_comparison_scheme_in_query202);
					updatedComparisonMethods=comparison_scheme(comparisonMethods);
					state._fsp--;

					builder.buildDeltaScheme(updatedComparisonMethods);
					}
					break;

			}

			match(input,LABELS,FOLLOW_LABELS_in_query229); 
			// AssessQuery.g:42:14: (labelingMethod= ID |labelingSystem= custom_labeling )
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==ID) ) {
				alt4=1;
			}
			else if ( (LA4_0==46) ) {
				alt4=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}

			switch (alt4) {
				case 1 :
					// AssessQuery.g:42:15: labelingMethod= ID
					{
					labelingMethod=(Token)match(input,ID,FOLLOW_ID_in_query236); 
					builder.buildLabelingScheme((labelingMethod!=null?labelingMethod.getText():null));
					}
					break;
				case 2 :
					// AssessQuery.g:44:9: labelingSystem= custom_labeling
					{
					pushFollow(FOLLOW_custom_labeling_in_query258);
					labelingSystem=custom_labeling();
					state._fsp--;

					builder.buildLabelingScheme(labelingSystem);
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return query;
	}
	// $ANTLR end "query"



	// $ANTLR start "selection_predicates"
	// AssessQuery.g:49:1: selection_predicates returns [HashMap<String, String> selectionPredicates] : parsed_predicate= predicate ( ',' additional_predicate= predicate )* ;
	public final HashMap<String, String> selection_predicates() throws RecognitionException {
		HashMap<String, String> selectionPredicates = null;


		ParserRuleReturnScope parsed_predicate =null;
		ParserRuleReturnScope additional_predicate =null;

		selectionPredicates = new HashMap<>();
		try {
			// AssessQuery.g:51:5: (parsed_predicate= predicate ( ',' additional_predicate= predicate )* )
			// AssessQuery.g:51:7: parsed_predicate= predicate ( ',' additional_predicate= predicate )*
			{
			pushFollow(FOLLOW_predicate_in_selection_predicates307);
			parsed_predicate=predicate();
			state._fsp--;

			selectionPredicates.put((parsed_predicate!=null?((AssessQueryParser.predicate_return)parsed_predicate).level:null), (parsed_predicate!=null?((AssessQueryParser.predicate_return)parsed_predicate).value:null));
			// AssessQuery.g:52:5: ( ',' additional_predicate= predicate )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==36) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// AssessQuery.g:52:6: ',' additional_predicate= predicate
					{
					match(input,36,FOLLOW_36_in_selection_predicates316); 
					pushFollow(FOLLOW_predicate_in_selection_predicates322);
					additional_predicate=predicate();
					state._fsp--;

					selectionPredicates.put((additional_predicate!=null?((AssessQueryParser.predicate_return)additional_predicate).level:null), (additional_predicate!=null?((AssessQueryParser.predicate_return)additional_predicate).value:null));
					}
					break;

				default :
					break loop5;
				}
			}

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
	// AssessQuery.g:55:1: predicate returns [String level, String value] : level_name= ID '=' '\\'' val= level_value '\\'' ;
	public final AssessQueryParser.predicate_return predicate() throws RecognitionException {
		AssessQueryParser.predicate_return retval = new AssessQueryParser.predicate_return();
		retval.start = input.LT(1);

		Token level_name=null;
		ParserRuleReturnScope val =null;

		try {
			// AssessQuery.g:56:5: (level_name= ID '=' '\\'' val= level_value '\\'' )
			// AssessQuery.g:56:7: level_name= ID '=' '\\'' val= level_value '\\''
			{
			level_name=(Token)match(input,ID,FOLLOW_ID_in_predicate351); 
			retval.level = (level_name!=null?level_name.getText():null);
			match(input,40,FOLLOW_40_in_predicate359); 
			match(input,42,FOLLOW_42_in_predicate361); 
			pushFollow(FOLLOW_level_value_in_predicate367);
			val=level_value();
			state._fsp--;

			retval.value = (val!=null?input.toString(val.start,val.stop):null);
			match(input,42,FOLLOW_42_in_predicate371); 
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
	// AssessQuery.g:60:1: level_value : ( ( ID )+ | date );
	public final AssessQueryParser.level_value_return level_value() throws RecognitionException {
		AssessQueryParser.level_value_return retval = new AssessQueryParser.level_value_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:60:13: ( ( ID )+ | date )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==ID) ) {
				alt7=1;
			}
			else if ( (LA7_0==INT) ) {
				alt7=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// AssessQuery.g:60:15: ( ID )+
					{
					// AssessQuery.g:60:15: ( ID )+
					int cnt6=0;
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( (LA6_0==ID) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							// AssessQuery.g:60:15: ID
							{
							match(input,ID,FOLLOW_ID_in_level_value384); 
							}
							break;

						default :
							if ( cnt6 >= 1 ) break loop6;
							EarlyExitException eee = new EarlyExitException(6, input);
							throw eee;
						}
						cnt6++;
					}

					}
					break;
				case 2 :
					// AssessQuery.g:60:21: date
					{
					pushFollow(FOLLOW_date_in_level_value389);
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
	// AssessQuery.g:62:1: date : INT '/' INT ( '/' INT )? ;
	public final void date() throws RecognitionException {
		try {
			// AssessQuery.g:62:6: ( INT '/' INT ( '/' INT )? )
			// AssessQuery.g:62:8: INT '/' INT ( '/' INT )?
			{
			match(input,INT,FOLLOW_INT_in_date398); 
			match(input,38,FOLLOW_38_in_date400); 
			match(input,INT,FOLLOW_INT_in_date402); 
			// AssessQuery.g:62:20: ( '/' INT )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==38) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// AssessQuery.g:62:21: '/' INT
					{
					match(input,38,FOLLOW_38_in_date405); 
					match(input,INT,FOLLOW_INT_in_date407); 
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "date"



	// $ANTLR start "group_by_set"
	// AssessQuery.g:64:1: group_by_set returns [HashSet<String> groupBySet] : id= ID ( ',' id= ID )* ;
	public final HashSet<String> group_by_set() throws RecognitionException {
		HashSet<String> groupBySet = null;


		Token id=null;

		groupBySet = new HashSet<>();
		try {
			// AssessQuery.g:66:5: (id= ID ( ',' id= ID )* )
			// AssessQuery.g:66:7: id= ID ( ',' id= ID )*
			{
			id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set436); 
			groupBySet.add((id!=null?id.getText():null));
			// AssessQuery.g:66:42: ( ',' id= ID )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==36) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// AssessQuery.g:66:43: ',' id= ID
					{
					match(input,36,FOLLOW_36_in_group_by_set441); 
					id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set445); 
					}
					break;

				default :
					break loop9;
				}
			}

			groupBySet.add((id!=null?id.getText():null));
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
	// AssessQuery.g:69:1: benchmark returns [List<String> parsedBenchmark] : ( constant_benchmark | external_benchmark | predicate | PAST INT );
	public final List<String> benchmark() throws RecognitionException {
		List<String> parsedBenchmark = null;


		Token INT4=null;
		ParserRuleReturnScope constant_benchmark1 =null;
		ParserRuleReturnScope external_benchmark2 =null;
		ParserRuleReturnScope predicate3 =null;

		parsedBenchmark = new ArrayList<>();
		try {
			// AssessQuery.g:71:5: ( constant_benchmark | external_benchmark | predicate | PAST INT )
			int alt10=4;
			switch ( input.LA(1) ) {
			case FLOAT:
			case INT:
			case SIGN:
				{
				alt10=1;
				}
				break;
			case ID:
				{
				int LA10_2 = input.LA(2);
				if ( (LA10_2==37) ) {
					alt10=2;
				}
				else if ( (LA10_2==40) ) {
					alt10=3;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 10, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case PAST:
				{
				alt10=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}
			switch (alt10) {
				case 1 :
					// AssessQuery.g:71:7: constant_benchmark
					{
					pushFollow(FOLLOW_constant_benchmark_in_benchmark478);
					constant_benchmark1=constant_benchmark();
					state._fsp--;

					parsedBenchmark.add("Constant");
					    parsedBenchmark.add((constant_benchmark1!=null?input.toString(constant_benchmark1.start,constant_benchmark1.stop):null));
					    
					}
					break;
				case 2 :
					// AssessQuery.g:75:7: external_benchmark
					{
					pushFollow(FOLLOW_external_benchmark_in_benchmark492);
					external_benchmark2=external_benchmark();
					state._fsp--;

					parsedBenchmark.add("External");
					     parsedBenchmark.add((external_benchmark2!=null?((AssessQueryParser.external_benchmark_return)external_benchmark2).cube:null));
					     parsedBenchmark.add((external_benchmark2!=null?((AssessQueryParser.external_benchmark_return)external_benchmark2).measurement:null));
					     
					}
					break;
				case 3 :
					// AssessQuery.g:80:7: predicate
					{
					pushFollow(FOLLOW_predicate_in_benchmark506);
					predicate3=predicate();
					state._fsp--;

					parsedBenchmark.add("Sibling");
					     parsedBenchmark.add((predicate3!=null?((AssessQueryParser.predicate_return)predicate3).level:null));
					     parsedBenchmark.add((predicate3!=null?((AssessQueryParser.predicate_return)predicate3).value:null));
					    
					}
					break;
				case 4 :
					// AssessQuery.g:85:7: PAST INT
					{
					match(input,PAST,FOLLOW_PAST_in_benchmark520); 
					INT4=(Token)match(input,INT,FOLLOW_INT_in_benchmark522); 
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
	// AssessQuery.g:91:1: constant_benchmark : ( SIGN )? number= ( INT | FLOAT ) ;
	public final AssessQueryParser.constant_benchmark_return constant_benchmark() throws RecognitionException {
		AssessQueryParser.constant_benchmark_return retval = new AssessQueryParser.constant_benchmark_return();
		retval.start = input.LT(1);

		Token number=null;

		try {
			// AssessQuery.g:91:20: ( ( SIGN )? number= ( INT | FLOAT ) )
			// AssessQuery.g:91:22: ( SIGN )? number= ( INT | FLOAT )
			{
			// AssessQuery.g:91:22: ( SIGN )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==SIGN) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// AssessQuery.g:91:23: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_constant_benchmark542); 
					}
					break;

			}

			number=input.LT(1);
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
	// AssessQuery.g:93:1: external_benchmark returns [String cube, String measurement] : benchmarkCube= ID '.' benchmarkMeasurement= ID ;
	public final AssessQueryParser.external_benchmark_return external_benchmark() throws RecognitionException {
		AssessQueryParser.external_benchmark_return retval = new AssessQueryParser.external_benchmark_return();
		retval.start = input.LT(1);

		Token benchmarkCube=null;
		Token benchmarkMeasurement=null;

		try {
			// AssessQuery.g:94:5: (benchmarkCube= ID '.' benchmarkMeasurement= ID )
			// AssessQuery.g:94:7: benchmarkCube= ID '.' benchmarkMeasurement= ID
			{
			benchmarkCube=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark575); 
			retval.cube = (benchmarkCube!=null?benchmarkCube.getText():null);
			match(input,37,FOLLOW_37_in_external_benchmark579); 
			benchmarkMeasurement=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark589); 
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
	// AssessQuery.g:97:1: comparison_scheme[List<String> comparisonMethods] returns [List<String> updatedComparisonMethods] : (method_name= ID '(' comparison_scheme[$updatedComparisonMethods] | comparison_args ')' );
	public final List<String> comparison_scheme(List<String> comparisonMethods) throws RecognitionException {
		List<String> updatedComparisonMethods = null;


		Token method_name=null;

		updatedComparisonMethods = comparisonMethods;
		try {
			// AssessQuery.g:99:5: (method_name= ID '(' comparison_scheme[$updatedComparisonMethods] | comparison_args ')' )
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==ID) ) {
				int LA12_1 = input.LA(2);
				if ( (LA12_1==36) ) {
					alt12=2;
				}
				else if ( (LA12_1==34) ) {
					alt12=1;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 12, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}

			switch (alt12) {
				case 1 :
					// AssessQuery.g:99:7: method_name= ID '(' comparison_scheme[$updatedComparisonMethods]
					{
					method_name=(Token)match(input,ID,FOLLOW_ID_in_comparison_scheme621); 
					updatedComparisonMethods.add((method_name!=null?method_name.getText():null));
					match(input,34,FOLLOW_34_in_comparison_scheme629); 
					pushFollow(FOLLOW_comparison_scheme_in_comparison_scheme631);
					comparison_scheme(updatedComparisonMethods);
					state._fsp--;

					}
					break;
				case 2 :
					// AssessQuery.g:100:56: comparison_args ')'
					{
					pushFollow(FOLLOW_comparison_args_in_comparison_scheme636);
					comparison_args();
					state._fsp--;

					match(input,35,FOLLOW_35_in_comparison_scheme637); 
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
		return updatedComparisonMethods;
	}
	// $ANTLR end "comparison_scheme"



	// $ANTLR start "comparison_args"
	// AssessQuery.g:102:1: comparison_args : ID ',' ( ( 'benchmark.' )? ID | INT ) ;
	public final void comparison_args() throws RecognitionException {
		try {
			// AssessQuery.g:102:17: ( ID ',' ( ( 'benchmark.' )? ID | INT ) )
			// AssessQuery.g:102:19: ID ',' ( ( 'benchmark.' )? ID | INT )
			{
			match(input,ID,FOLLOW_ID_in_comparison_args645); 
			match(input,36,FOLLOW_36_in_comparison_args647); 
			// AssessQuery.g:102:26: ( ( 'benchmark.' )? ID | INT )
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==ID||LA14_0==44) ) {
				alt14=1;
			}
			else if ( (LA14_0==INT) ) {
				alt14=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 14, 0, input);
				throw nvae;
			}

			switch (alt14) {
				case 1 :
					// AssessQuery.g:102:28: ( 'benchmark.' )? ID
					{
					// AssessQuery.g:102:28: ( 'benchmark.' )?
					int alt13=2;
					int LA13_0 = input.LA(1);
					if ( (LA13_0==44) ) {
						alt13=1;
					}
					switch (alt13) {
						case 1 :
							// AssessQuery.g:102:29: 'benchmark.'
							{
							match(input,44,FOLLOW_44_in_comparison_args652); 
							}
							break;

					}

					match(input,ID,FOLLOW_ID_in_comparison_args656); 
					}
					break;
				case 2 :
					// AssessQuery.g:102:49: INT
					{
					match(input,INT,FOLLOW_INT_in_comparison_args660); 
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "comparison_args"



	// $ANTLR start "custom_labeling"
	// AssessQuery.g:104:1: custom_labeling returns [List<List<String>> labelingTerms] : '{' term= label_term ( ',' term= label_term )* '}' ;
	public final List<List<String>> custom_labeling() throws RecognitionException {
		List<List<String>> labelingTerms = null;


		List<String> term =null;

		labelingTerms = new ArrayList<List<String>>();
		try {
			// AssessQuery.g:106:5: ( '{' term= label_term ( ',' term= label_term )* '}' )
			// AssessQuery.g:106:7: '{' term= label_term ( ',' term= label_term )* '}'
			{
			match(input,46,FOLLOW_46_in_custom_labeling686); 
			pushFollow(FOLLOW_label_term_in_custom_labeling692);
			term=label_term();
			state._fsp--;

			labelingTerms.add(term);
			// AssessQuery.g:107:5: ( ',' term= label_term )*
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( (LA15_0==36) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// AssessQuery.g:107:6: ',' term= label_term
					{
					match(input,36,FOLLOW_36_in_custom_labeling701); 
					pushFollow(FOLLOW_label_term_in_custom_labeling707);
					term=label_term();
					state._fsp--;

					labelingTerms.add(term);
					}
					break;

				default :
					break loop15;
				}
			}

			match(input,47,FOLLOW_47_in_custom_labeling713); 
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
	// AssessQuery.g:110:1: label_term returns [List<String> term] : range= label_range ':' label= ID ;
	public final List<String> label_term() throws RecognitionException {
		List<String> term = null;


		Token label=null;
		List<String> range =null;

		try {
			// AssessQuery.g:112:5: (range= label_range ':' label= ID )
			// AssessQuery.g:112:7: range= label_range ':' label= ID
			{
			pushFollow(FOLLOW_label_range_in_label_term744);
			range=label_range();
			state._fsp--;

			match(input,39,FOLLOW_39_in_label_term746); 
			label=(Token)match(input,ID,FOLLOW_ID_in_label_term750); 
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
	// AssessQuery.g:114:1: label_range returns [List<String> limits] : (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) ;
	public final List<String> label_range() throws RecognitionException {
		List<String> limits = null;


		Token lowLimit=null;
		Token highLimit=null;
		ParserRuleReturnScope start =null;
		ParserRuleReturnScope end =null;

		limits = new ArrayList<String>();
		try {
			// AssessQuery.g:116:5: ( (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) )
			// AssessQuery.g:116:7: (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' )
			{
			// AssessQuery.g:116:7: (lowLimit= '[' |lowLimit= '(' )
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==41) ) {
				alt16=1;
			}
			else if ( (LA16_0==34) ) {
				alt16=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}

			switch (alt16) {
				case 1 :
					// AssessQuery.g:116:9: lowLimit= '['
					{
					lowLimit=(Token)match(input,41,FOLLOW_41_in_label_range783); 
					}
					break;
				case 2 :
					// AssessQuery.g:116:26: lowLimit= '('
					{
					lowLimit=(Token)match(input,34,FOLLOW_34_in_label_range791); 
					}
					break;

			}

			limits.add((lowLimit!=null?lowLimit.getText():null));
			pushFollow(FOLLOW_range_point_in_label_range807);
			start=range_point();
			state._fsp--;

			 limits.add((start!=null?input.toString(start.start,start.stop):null)); 
			match(input,36,FOLLOW_36_in_label_range811); 
			pushFollow(FOLLOW_range_point_in_label_range823);
			end=range_point();
			state._fsp--;

			 limits.add((end!=null?input.toString(end.start,end.stop):null)); 
			// AssessQuery.g:119:7: (highLimit= ')' |highLimit= ']' )
			int alt17=2;
			int LA17_0 = input.LA(1);
			if ( (LA17_0==35) ) {
				alt17=1;
			}
			else if ( (LA17_0==43) ) {
				alt17=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 17, 0, input);
				throw nvae;
			}

			switch (alt17) {
				case 1 :
					// AssessQuery.g:119:9: highLimit= ')'
					{
					highLimit=(Token)match(input,35,FOLLOW_35_in_label_range839); 
					}
					break;
				case 2 :
					// AssessQuery.g:119:27: highLimit= ']'
					{
					highLimit=(Token)match(input,43,FOLLOW_43_in_label_range847); 
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
	// AssessQuery.g:122:1: range_point : ( SIGN )? ( INT | FLOAT | 'inf' ) ;
	public final AssessQueryParser.range_point_return range_point() throws RecognitionException {
		AssessQueryParser.range_point_return retval = new AssessQueryParser.range_point_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:122:13: ( ( SIGN )? ( INT | FLOAT | 'inf' ) )
			// AssessQuery.g:122:15: ( SIGN )? ( INT | FLOAT | 'inf' )
			{
			// AssessQuery.g:122:15: ( SIGN )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==SIGN) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// AssessQuery.g:122:15: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_range_point863); 
					}
					break;

			}

			if ( input.LA(1)==FLOAT||input.LA(1)==INT||input.LA(1)==45 ) {
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
	public static final BitSet FOLLOW_WITH_in_query94 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_query100 = new BitSet(new long[]{0x0000000000001100L});
	public static final BitSet FOLLOW_FOR_in_query111 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_selection_predicates_in_query117 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_query129 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_group_by_set_in_query135 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_ASSESS_in_query145 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_query151 = new BitSet(new long[]{0x0000000020080020L});
	public static final BitSet FOLLOW_AGAINST_in_query171 = new BitSet(new long[]{0x0000000004830800L});
	public static final BitSet FOLLOW_benchmark_in_query177 = new BitSet(new long[]{0x0000000020080000L});
	public static final BitSet FOLLOW_USING_in_query196 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_comparison_scheme_in_query202 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_LABELS_in_query229 = new BitSet(new long[]{0x0000400000010000L});
	public static final BitSet FOLLOW_ID_in_query236 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_custom_labeling_in_query258 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_predicate_in_selection_predicates307 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_36_in_selection_predicates316 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_predicate_in_selection_predicates322 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_ID_in_predicate351 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_40_in_predicate359 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_predicate361 = new BitSet(new long[]{0x0000000000030000L});
	public static final BitSet FOLLOW_level_value_in_predicate367 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_predicate371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_level_value384 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_date_in_level_value389 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_date398 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_38_in_date400 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_date402 = new BitSet(new long[]{0x0000004000000002L});
	public static final BitSet FOLLOW_38_in_date405 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_date407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_group_by_set436 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_36_in_group_by_set441 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_group_by_set445 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_constant_benchmark_in_benchmark478 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_external_benchmark_in_benchmark492 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_predicate_in_benchmark506 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PAST_in_benchmark520 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_benchmark522 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_constant_benchmark542 = new BitSet(new long[]{0x0000000000020800L});
	public static final BitSet FOLLOW_set_in_constant_benchmark550 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_external_benchmark575 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_37_in_external_benchmark579 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_external_benchmark589 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_scheme621 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_34_in_comparison_scheme629 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_comparison_scheme_in_comparison_scheme631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_args_in_comparison_scheme636 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_35_in_comparison_scheme637 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_args645 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_36_in_comparison_args647 = new BitSet(new long[]{0x0000100000030000L});
	public static final BitSet FOLLOW_44_in_comparison_args652 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_comparison_args656 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_comparison_args660 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_46_in_custom_labeling686 = new BitSet(new long[]{0x0000020400000000L});
	public static final BitSet FOLLOW_label_term_in_custom_labeling692 = new BitSet(new long[]{0x0000801000000000L});
	public static final BitSet FOLLOW_36_in_custom_labeling701 = new BitSet(new long[]{0x0000020400000000L});
	public static final BitSet FOLLOW_label_term_in_custom_labeling707 = new BitSet(new long[]{0x0000801000000000L});
	public static final BitSet FOLLOW_47_in_custom_labeling713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_label_range_in_label_term744 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_39_in_label_term746 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_label_term750 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_41_in_label_range783 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_34_in_label_range791 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_range_point_in_label_range807 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_36_in_label_range811 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_range_point_in_label_range823 = new BitSet(new long[]{0x0000080800000000L});
	public static final BitSet FOLLOW_35_in_label_range839 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_43_in_label_range847 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_range_point863 = new BitSet(new long[]{0x0000200000020800L});
	public static final BitSet FOLLOW_set_in_range_point866 = new BitSet(new long[]{0x0000000000000002L});
}
