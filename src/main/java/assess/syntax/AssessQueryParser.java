// $ANTLR 3.5.2 AssessQuery.g 2023-06-29 07:55:40

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
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "AGAINST", "AGGREGATE", "AS", 
		"ASSESS", "B", "BY", "C", "D", "E", "F", "FLOAT", "FOR", "G", "H", "I", 
		"ID", "INT", "J", "K", "L", "LABELS", "M", "N", "O", "P", "PAST", "Q", 
		"R", "S", "SAVE", "SIGN", "T", "U", "USING", "V", "W", "WITH", "WS", "X", 
		"Y", "Z", "'('", "')'", "','", "'.'", "'/'", "':'", "'='", "'['", "'\\''", 
		"']'", "'benchmark.'", "'inf'", "'{'", "'}'"
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
	// AssessQuery.g:25:1: query returns [AssessQuery query] : WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS AGGREGATE '(' measurement= ID ')' ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling ) ( SAVE AS output_name= ID )? ;
	public final AssessQuery query() throws RecognitionException {
		AssessQuery query = null;


		Token targetCube=null;
		Token measurement=null;
		Token labelingMethod=null;
		Token output_name=null;
		Token AGGREGATE1=null;
		HashMap<String, String> predicates =null;
		HashSet<String> gammas =null;
		List<String> parsedBenchmark =null;
		List<String> updatedComparisonMethods =null;
		List<List<String>> labelingSystem =null;


		    List<String> comparisonMethods = new ArrayList<String>();
		    
		try {
			// AssessQuery.g:29:5: ( WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS AGGREGATE '(' measurement= ID ')' ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling ) ( SAVE AS output_name= ID )? )
			// AssessQuery.g:29:7: WITH targetCube= ID ( FOR predicates= selection_predicates )? BY gammas= group_by_set ASSESS AGGREGATE '(' measurement= ID ')' ( AGAINST parsedBenchmark= benchmark )? ( USING updatedComparisonMethods= comparison_scheme[comparisonMethods] )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling ) ( SAVE AS output_name= ID )?
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
			AGGREGATE1=(Token)match(input,AGGREGATE,FOLLOW_AGGREGATE_in_query147); 
			builder.setAggregationFunction((AGGREGATE1!=null?AGGREGATE1.getText():null));
			match(input,46,FOLLOW_46_in_query157); 
			measurement=(Token)match(input,ID,FOLLOW_ID_in_query163); 
			builder.setMeasurement((measurement!=null?measurement.getText():null));
			match(input,47,FOLLOW_47_in_query167); 
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
					match(input,AGAINST,FOLLOW_AGAINST_in_query177); 
					pushFollow(FOLLOW_benchmark_in_query183);
					parsedBenchmark=benchmark();
					state._fsp--;

					builder.setBenchmarkDetails(parsedBenchmark);
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
					match(input,USING,FOLLOW_USING_in_query203); 
					pushFollow(FOLLOW_comparison_scheme_in_query209);
					updatedComparisonMethods=comparison_scheme(comparisonMethods);
					state._fsp--;

					builder.setDeltaFunctions(updatedComparisonMethods);
					}
					break;

			}

			match(input,LABELS,FOLLOW_LABELS_in_query236); 
			// AssessQuery.g:42:14: (labelingMethod= ID |labelingSystem= custom_labeling )
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==ID) ) {
				alt4=1;
			}
			else if ( (LA4_0==58) ) {
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
					labelingMethod=(Token)match(input,ID,FOLLOW_ID_in_query243); 
					builder.buildLabelingScheme((labelingMethod!=null?labelingMethod.getText():null));
					}
					break;
				case 2 :
					// AssessQuery.g:44:9: labelingSystem= custom_labeling
					{
					pushFollow(FOLLOW_custom_labeling_in_query265);
					labelingSystem=custom_labeling();
					state._fsp--;

					builder.setLabelingRules(labelingSystem);
					}
					break;

			}

			// AssessQuery.g:47:7: ( SAVE AS output_name= ID )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==SAVE) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// AssessQuery.g:47:8: SAVE AS output_name= ID
					{
					match(input,SAVE,FOLLOW_SAVE_in_query290); 
					match(input,AS,FOLLOW_AS_in_query292); 
					output_name=(Token)match(input,ID,FOLLOW_ID_in_query298); 
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
	// AssessQuery.g:55:1: selection_predicates returns [HashMap<String, String> selectionPredicates] : parsed_predicate= predicate ( ',' additional_predicate= predicate )* ;
	public final HashMap<String, String> selection_predicates() throws RecognitionException {
		HashMap<String, String> selectionPredicates = null;


		ParserRuleReturnScope parsed_predicate =null;
		ParserRuleReturnScope additional_predicate =null;

		selectionPredicates = new HashMap<>();
		try {
			// AssessQuery.g:57:5: (parsed_predicate= predicate ( ',' additional_predicate= predicate )* )
			// AssessQuery.g:57:7: parsed_predicate= predicate ( ',' additional_predicate= predicate )*
			{
			pushFollow(FOLLOW_predicate_in_selection_predicates353);
			parsed_predicate=predicate();
			state._fsp--;

			selectionPredicates.put((parsed_predicate!=null?((AssessQueryParser.predicate_return)parsed_predicate).level:null), (parsed_predicate!=null?((AssessQueryParser.predicate_return)parsed_predicate).value:null));
			// AssessQuery.g:58:5: ( ',' additional_predicate= predicate )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==48) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// AssessQuery.g:58:6: ',' additional_predicate= predicate
					{
					match(input,48,FOLLOW_48_in_selection_predicates362); 
					pushFollow(FOLLOW_predicate_in_selection_predicates368);
					additional_predicate=predicate();
					state._fsp--;

					selectionPredicates.put((additional_predicate!=null?((AssessQueryParser.predicate_return)additional_predicate).level:null), (additional_predicate!=null?((AssessQueryParser.predicate_return)additional_predicate).value:null));
					}
					break;

				default :
					break loop6;
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
	// AssessQuery.g:61:1: predicate returns [String level, String value] : level_name= ID '=' '\\'' val= level_value '\\'' ;
	public final AssessQueryParser.predicate_return predicate() throws RecognitionException {
		AssessQueryParser.predicate_return retval = new AssessQueryParser.predicate_return();
		retval.start = input.LT(1);

		Token level_name=null;
		ParserRuleReturnScope val =null;

		try {
			// AssessQuery.g:62:5: (level_name= ID '=' '\\'' val= level_value '\\'' )
			// AssessQuery.g:62:7: level_name= ID '=' '\\'' val= level_value '\\''
			{
			level_name=(Token)match(input,ID,FOLLOW_ID_in_predicate397); 
			retval.level = (level_name!=null?level_name.getText():null);
			match(input,52,FOLLOW_52_in_predicate405); 
			match(input,54,FOLLOW_54_in_predicate407); 
			pushFollow(FOLLOW_level_value_in_predicate413);
			val=level_value();
			state._fsp--;

			retval.value = (val!=null?input.toString(val.start,val.stop):null);
			match(input,54,FOLLOW_54_in_predicate417); 
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
	// AssessQuery.g:66:1: level_value : ( ( ID )+ | date );
	public final AssessQueryParser.level_value_return level_value() throws RecognitionException {
		AssessQueryParser.level_value_return retval = new AssessQueryParser.level_value_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:66:13: ( ( ID )+ | date )
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
					// AssessQuery.g:66:15: ( ID )+
					{
					// AssessQuery.g:66:15: ( ID )+
					int cnt7=0;
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( (LA7_0==ID) ) {
							alt7=1;
						}

						switch (alt7) {
						case 1 :
							// AssessQuery.g:66:15: ID
							{
							match(input,ID,FOLLOW_ID_in_level_value430); 
							}
							break;

						default :
							if ( cnt7 >= 1 ) break loop7;
							EarlyExitException eee = new EarlyExitException(7, input);
							throw eee;
						}
						cnt7++;
					}

					}
					break;
				case 2 :
					// AssessQuery.g:66:21: date
					{
					pushFollow(FOLLOW_date_in_level_value435);
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
	// AssessQuery.g:68:1: date : ( INT | INT '/' INT | INT '/' INT '/' INT );
	public final void date() throws RecognitionException {
		try {
			// AssessQuery.g:68:6: ( INT | INT '/' INT | INT '/' INT '/' INT )
			int alt9=3;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==INT) ) {
				int LA9_1 = input.LA(2);
				if ( (LA9_1==50) ) {
					int LA9_2 = input.LA(3);
					if ( (LA9_2==INT) ) {
						int LA9_4 = input.LA(4);
						if ( (LA9_4==50) ) {
							alt9=3;
						}
						else if ( (LA9_4==54) ) {
							alt9=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 9, 4, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 9, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA9_1==54) ) {
					alt9=1;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}

			switch (alt9) {
				case 1 :
					// AssessQuery.g:69:5: INT
					{
					match(input,INT,FOLLOW_INT_in_date448); 
					}
					break;
				case 2 :
					// AssessQuery.g:70:7: INT '/' INT
					{
					match(input,INT,FOLLOW_INT_in_date456); 
					match(input,50,FOLLOW_50_in_date458); 
					match(input,INT,FOLLOW_INT_in_date460); 
					}
					break;
				case 3 :
					// AssessQuery.g:71:7: INT '/' INT '/' INT
					{
					match(input,INT,FOLLOW_INT_in_date468); 
					match(input,50,FOLLOW_50_in_date470); 
					match(input,INT,FOLLOW_INT_in_date472); 
					match(input,50,FOLLOW_50_in_date474); 
					match(input,INT,FOLLOW_INT_in_date476); 
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
	}
	// $ANTLR end "date"



	// $ANTLR start "group_by_set"
	// AssessQuery.g:73:1: group_by_set returns [HashSet<String> groupBySet] : id= ID ( ',' id= ID )* ;
	public final HashSet<String> group_by_set() throws RecognitionException {
		HashSet<String> groupBySet = null;


		Token id=null;

		groupBySet = new HashSet<>();
		try {
			// AssessQuery.g:75:5: (id= ID ( ',' id= ID )* )
			// AssessQuery.g:75:7: id= ID ( ',' id= ID )*
			{
			id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set502); 
			groupBySet.add((id!=null?id.getText():null));
			// AssessQuery.g:75:42: ( ',' id= ID )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==48) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// AssessQuery.g:75:43: ',' id= ID
					{
					match(input,48,FOLLOW_48_in_group_by_set507); 
					id=(Token)match(input,ID,FOLLOW_ID_in_group_by_set511); 
					groupBySet.add((id!=null?id.getText():null));
					}
					break;

				default :
					break loop10;
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
		return groupBySet;
	}
	// $ANTLR end "group_by_set"



	// $ANTLR start "benchmark"
	// AssessQuery.g:78:1: benchmark returns [List<String> parsedBenchmark] : ( constant_benchmark | external_benchmark | predicate | PAST INT );
	public final List<String> benchmark() throws RecognitionException {
		List<String> parsedBenchmark = null;


		Token INT5=null;
		ParserRuleReturnScope constant_benchmark2 =null;
		ParserRuleReturnScope external_benchmark3 =null;
		ParserRuleReturnScope predicate4 =null;

		parsedBenchmark = new ArrayList<>();
		try {
			// AssessQuery.g:80:5: ( constant_benchmark | external_benchmark | predicate | PAST INT )
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
				if ( (LA11_2==49) ) {
					alt11=2;
				}
				else if ( (LA11_2==52) ) {
					alt11=3;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 11, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
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
					// AssessQuery.g:80:7: constant_benchmark
					{
					pushFollow(FOLLOW_constant_benchmark_in_benchmark544);
					constant_benchmark2=constant_benchmark();
					state._fsp--;

					parsedBenchmark.add("Constant");
					    parsedBenchmark.add((constant_benchmark2!=null?input.toString(constant_benchmark2.start,constant_benchmark2.stop):null));
					    
					}
					break;
				case 2 :
					// AssessQuery.g:84:7: external_benchmark
					{
					pushFollow(FOLLOW_external_benchmark_in_benchmark558);
					external_benchmark3=external_benchmark();
					state._fsp--;

					parsedBenchmark.add("External");
					     parsedBenchmark.add((external_benchmark3!=null?((AssessQueryParser.external_benchmark_return)external_benchmark3).cube:null));
					     parsedBenchmark.add((external_benchmark3!=null?((AssessQueryParser.external_benchmark_return)external_benchmark3).measurement:null));
					     
					}
					break;
				case 3 :
					// AssessQuery.g:89:7: predicate
					{
					pushFollow(FOLLOW_predicate_in_benchmark572);
					predicate4=predicate();
					state._fsp--;

					parsedBenchmark.add("Sibling");
					     parsedBenchmark.add((predicate4!=null?((AssessQueryParser.predicate_return)predicate4).level:null));
					     parsedBenchmark.add((predicate4!=null?((AssessQueryParser.predicate_return)predicate4).value:null));
					    
					}
					break;
				case 4 :
					// AssessQuery.g:94:7: PAST INT
					{
					match(input,PAST,FOLLOW_PAST_in_benchmark586); 
					INT5=(Token)match(input,INT,FOLLOW_INT_in_benchmark588); 
					parsedBenchmark.add("Past");
					    parsedBenchmark.add((INT5!=null?INT5.getText():null));
					    
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
	// AssessQuery.g:100:1: constant_benchmark : ( SIGN )? number= ( INT | FLOAT ) ;
	public final AssessQueryParser.constant_benchmark_return constant_benchmark() throws RecognitionException {
		AssessQueryParser.constant_benchmark_return retval = new AssessQueryParser.constant_benchmark_return();
		retval.start = input.LT(1);

		Token number=null;

		try {
			// AssessQuery.g:100:20: ( ( SIGN )? number= ( INT | FLOAT ) )
			// AssessQuery.g:100:22: ( SIGN )? number= ( INT | FLOAT )
			{
			// AssessQuery.g:100:22: ( SIGN )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==SIGN) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// AssessQuery.g:100:23: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_constant_benchmark608); 
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
	// AssessQuery.g:102:1: external_benchmark returns [String cube, String measurement] : benchmarkCube= ID '.' benchmarkMeasurement= ID ;
	public final AssessQueryParser.external_benchmark_return external_benchmark() throws RecognitionException {
		AssessQueryParser.external_benchmark_return retval = new AssessQueryParser.external_benchmark_return();
		retval.start = input.LT(1);

		Token benchmarkCube=null;
		Token benchmarkMeasurement=null;

		try {
			// AssessQuery.g:103:5: (benchmarkCube= ID '.' benchmarkMeasurement= ID )
			// AssessQuery.g:103:7: benchmarkCube= ID '.' benchmarkMeasurement= ID
			{
			benchmarkCube=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark641); 
			retval.cube = (benchmarkCube!=null?benchmarkCube.getText():null);
			match(input,49,FOLLOW_49_in_external_benchmark645); 
			benchmarkMeasurement=(Token)match(input,ID,FOLLOW_ID_in_external_benchmark655); 
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
	// AssessQuery.g:106:1: comparison_scheme[List<String> comparisonMethods] returns [List<String> updatedComparisonMethods] : method_name= ID '(' ( comparison_scheme[$updatedComparisonMethods] | comparison_args ) ')' ;
	public final List<String> comparison_scheme(List<String> comparisonMethods) throws RecognitionException {
		List<String> updatedComparisonMethods = null;


		Token method_name=null;

		updatedComparisonMethods = comparisonMethods;
		try {
			// AssessQuery.g:108:5: (method_name= ID '(' ( comparison_scheme[$updatedComparisonMethods] | comparison_args ) ')' )
			// AssessQuery.g:108:7: method_name= ID '(' ( comparison_scheme[$updatedComparisonMethods] | comparison_args ) ')'
			{
			method_name=(Token)match(input,ID,FOLLOW_ID_in_comparison_scheme687); 
			updatedComparisonMethods.add((method_name!=null?method_name.getText():null));
			match(input,46,FOLLOW_46_in_comparison_scheme695); 
			// AssessQuery.g:109:9: ( comparison_scheme[$updatedComparisonMethods] | comparison_args )
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==ID) ) {
				int LA13_1 = input.LA(2);
				if ( (LA13_1==48) ) {
					alt13=2;
				}
				else if ( (LA13_1==46) ) {
					alt13=1;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 13, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
					// AssessQuery.g:109:10: comparison_scheme[$updatedComparisonMethods]
					{
					pushFollow(FOLLOW_comparison_scheme_in_comparison_scheme698);
					comparison_scheme(updatedComparisonMethods);
					state._fsp--;

					}
					break;
				case 2 :
					// AssessQuery.g:109:57: comparison_args
					{
					pushFollow(FOLLOW_comparison_args_in_comparison_scheme703);
					comparison_args();
					state._fsp--;

					}
					break;

			}

			match(input,47,FOLLOW_47_in_comparison_scheme706); 
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
	// AssessQuery.g:111:1: comparison_args : ID ',' ( ( 'benchmark.' )? ID | INT ) ;
	public final void comparison_args() throws RecognitionException {
		try {
			// AssessQuery.g:111:17: ( ID ',' ( ( 'benchmark.' )? ID | INT ) )
			// AssessQuery.g:111:19: ID ',' ( ( 'benchmark.' )? ID | INT )
			{
			match(input,ID,FOLLOW_ID_in_comparison_args714); 
			match(input,48,FOLLOW_48_in_comparison_args716); 
			// AssessQuery.g:111:26: ( ( 'benchmark.' )? ID | INT )
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==ID||LA15_0==56) ) {
				alt15=1;
			}
			else if ( (LA15_0==INT) ) {
				alt15=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// AssessQuery.g:111:28: ( 'benchmark.' )? ID
					{
					// AssessQuery.g:111:28: ( 'benchmark.' )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==56) ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// AssessQuery.g:111:29: 'benchmark.'
							{
							match(input,56,FOLLOW_56_in_comparison_args721); 
							}
							break;

					}

					match(input,ID,FOLLOW_ID_in_comparison_args725); 
					}
					break;
				case 2 :
					// AssessQuery.g:111:49: INT
					{
					match(input,INT,FOLLOW_INT_in_comparison_args729); 
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
	// AssessQuery.g:113:1: custom_labeling returns [List<List<String>> labelingTerms] : '{' term= label_term ( ',' term= label_term )* '}' ;
	public final List<List<String>> custom_labeling() throws RecognitionException {
		List<List<String>> labelingTerms = null;


		List<String> term =null;

		labelingTerms = new ArrayList<List<String>>();
		try {
			// AssessQuery.g:115:5: ( '{' term= label_term ( ',' term= label_term )* '}' )
			// AssessQuery.g:115:7: '{' term= label_term ( ',' term= label_term )* '}'
			{
			match(input,58,FOLLOW_58_in_custom_labeling755); 
			pushFollow(FOLLOW_label_term_in_custom_labeling761);
			term=label_term();
			state._fsp--;

			labelingTerms.add(term);
			// AssessQuery.g:116:5: ( ',' term= label_term )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( (LA16_0==48) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// AssessQuery.g:116:6: ',' term= label_term
					{
					match(input,48,FOLLOW_48_in_custom_labeling770); 
					pushFollow(FOLLOW_label_term_in_custom_labeling776);
					term=label_term();
					state._fsp--;

					labelingTerms.add(term);
					}
					break;

				default :
					break loop16;
				}
			}

			match(input,59,FOLLOW_59_in_custom_labeling782); 
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
	// AssessQuery.g:119:1: label_term returns [List<String> term] : range= label_range ':' label= ID ;
	public final List<String> label_term() throws RecognitionException {
		List<String> term = null;


		Token label=null;
		List<String> range =null;

		try {
			// AssessQuery.g:121:5: (range= label_range ':' label= ID )
			// AssessQuery.g:121:7: range= label_range ':' label= ID
			{
			pushFollow(FOLLOW_label_range_in_label_term813);
			range=label_range();
			state._fsp--;

			match(input,51,FOLLOW_51_in_label_term815); 
			label=(Token)match(input,ID,FOLLOW_ID_in_label_term819); 
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
	// AssessQuery.g:123:1: label_range returns [List<String> limits] : (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) ;
	public final List<String> label_range() throws RecognitionException {
		List<String> limits = null;


		Token lowLimit=null;
		Token highLimit=null;
		ParserRuleReturnScope start =null;
		ParserRuleReturnScope end =null;

		limits = new ArrayList<String>();
		try {
			// AssessQuery.g:125:5: ( (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' ) )
			// AssessQuery.g:125:7: (lowLimit= '[' |lowLimit= '(' ) start= range_point ',' end= range_point (highLimit= ')' |highLimit= ']' )
			{
			// AssessQuery.g:125:7: (lowLimit= '[' |lowLimit= '(' )
			int alt17=2;
			int LA17_0 = input.LA(1);
			if ( (LA17_0==53) ) {
				alt17=1;
			}
			else if ( (LA17_0==46) ) {
				alt17=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 17, 0, input);
				throw nvae;
			}

			switch (alt17) {
				case 1 :
					// AssessQuery.g:125:9: lowLimit= '['
					{
					lowLimit=(Token)match(input,53,FOLLOW_53_in_label_range852); 
					}
					break;
				case 2 :
					// AssessQuery.g:125:26: lowLimit= '('
					{
					lowLimit=(Token)match(input,46,FOLLOW_46_in_label_range860); 
					}
					break;

			}

			limits.add((lowLimit!=null?lowLimit.getText():null));
			pushFollow(FOLLOW_range_point_in_label_range876);
			start=range_point();
			state._fsp--;

			 limits.add((start!=null?input.toString(start.start,start.stop):null)); 
			match(input,48,FOLLOW_48_in_label_range880); 
			pushFollow(FOLLOW_range_point_in_label_range892);
			end=range_point();
			state._fsp--;

			 limits.add((end!=null?input.toString(end.start,end.stop):null)); 
			// AssessQuery.g:128:7: (highLimit= ')' |highLimit= ']' )
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==47) ) {
				alt18=1;
			}
			else if ( (LA18_0==55) ) {
				alt18=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 18, 0, input);
				throw nvae;
			}

			switch (alt18) {
				case 1 :
					// AssessQuery.g:128:9: highLimit= ')'
					{
					highLimit=(Token)match(input,47,FOLLOW_47_in_label_range908); 
					}
					break;
				case 2 :
					// AssessQuery.g:128:27: highLimit= ']'
					{
					highLimit=(Token)match(input,55,FOLLOW_55_in_label_range916); 
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
	// AssessQuery.g:131:1: range_point : ( SIGN )? ( INT | FLOAT | 'inf' ) ;
	public final AssessQueryParser.range_point_return range_point() throws RecognitionException {
		AssessQueryParser.range_point_return retval = new AssessQueryParser.range_point_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:131:13: ( ( SIGN )? ( INT | FLOAT | 'inf' ) )
			// AssessQuery.g:131:15: ( SIGN )? ( INT | FLOAT | 'inf' )
			{
			// AssessQuery.g:131:15: ( SIGN )?
			int alt19=2;
			int LA19_0 = input.LA(1);
			if ( (LA19_0==SIGN) ) {
				alt19=1;
			}
			switch (alt19) {
				case 1 :
					// AssessQuery.g:131:15: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_range_point932); 
					}
					break;

			}

			if ( input.LA(1)==FLOAT||input.LA(1)==INT||input.LA(1)==57 ) {
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
	public static final BitSet FOLLOW_ASSESS_in_query145 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_AGGREGATE_in_query147 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_46_in_query157 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_ID_in_query163 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_47_in_query167 = new BitSet(new long[]{0x0000004002000020L});
	public static final BitSet FOLLOW_AGAINST_in_query177 = new BitSet(new long[]{0x0000000840308000L});
	public static final BitSet FOLLOW_benchmark_in_query183 = new BitSet(new long[]{0x0000004002000000L});
	public static final BitSet FOLLOW_USING_in_query203 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_comparison_scheme_in_query209 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_LABELS_in_query236 = new BitSet(new long[]{0x0400000000100000L});
	public static final BitSet FOLLOW_ID_in_query243 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_custom_labeling_in_query265 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_SAVE_in_query290 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_AS_in_query292 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_ID_in_query298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_predicate_in_selection_predicates353 = new BitSet(new long[]{0x0001000000000002L});
	public static final BitSet FOLLOW_48_in_selection_predicates362 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_predicate_in_selection_predicates368 = new BitSet(new long[]{0x0001000000000002L});
	public static final BitSet FOLLOW_ID_in_predicate397 = new BitSet(new long[]{0x0010000000000000L});
	public static final BitSet FOLLOW_52_in_predicate405 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_54_in_predicate407 = new BitSet(new long[]{0x0000000000300000L});
	public static final BitSet FOLLOW_level_value_in_predicate413 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_54_in_predicate417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_level_value430 = new BitSet(new long[]{0x0000000000100002L});
	public static final BitSet FOLLOW_date_in_level_value435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_date448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_date456 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_50_in_date458 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_INT_in_date460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_date468 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_50_in_date470 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_INT_in_date472 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_50_in_date474 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_INT_in_date476 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_group_by_set502 = new BitSet(new long[]{0x0001000000000002L});
	public static final BitSet FOLLOW_48_in_group_by_set507 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_ID_in_group_by_set511 = new BitSet(new long[]{0x0001000000000002L});
	public static final BitSet FOLLOW_constant_benchmark_in_benchmark544 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_external_benchmark_in_benchmark558 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_predicate_in_benchmark572 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PAST_in_benchmark586 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_INT_in_benchmark588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_constant_benchmark608 = new BitSet(new long[]{0x0000000000208000L});
	public static final BitSet FOLLOW_set_in_constant_benchmark616 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_external_benchmark641 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_49_in_external_benchmark645 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_ID_in_external_benchmark655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_scheme687 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_46_in_comparison_scheme695 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_comparison_scheme_in_comparison_scheme698 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_comparison_args_in_comparison_scheme703 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_47_in_comparison_scheme706 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_args714 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_48_in_comparison_args716 = new BitSet(new long[]{0x0100000000300000L});
	public static final BitSet FOLLOW_56_in_comparison_args721 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_ID_in_comparison_args725 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_comparison_args729 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_58_in_custom_labeling755 = new BitSet(new long[]{0x0020400000000000L});
	public static final BitSet FOLLOW_label_term_in_custom_labeling761 = new BitSet(new long[]{0x0801000000000000L});
	public static final BitSet FOLLOW_48_in_custom_labeling770 = new BitSet(new long[]{0x0020400000000000L});
	public static final BitSet FOLLOW_label_term_in_custom_labeling776 = new BitSet(new long[]{0x0801000000000000L});
	public static final BitSet FOLLOW_59_in_custom_labeling782 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_label_range_in_label_term813 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_51_in_label_term815 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_ID_in_label_term819 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_53_in_label_range852 = new BitSet(new long[]{0x0200000800208000L});
	public static final BitSet FOLLOW_46_in_label_range860 = new BitSet(new long[]{0x0200000800208000L});
	public static final BitSet FOLLOW_range_point_in_label_range876 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_48_in_label_range880 = new BitSet(new long[]{0x0200000800208000L});
	public static final BitSet FOLLOW_range_point_in_label_range892 = new BitSet(new long[]{0x0080800000000000L});
	public static final BitSet FOLLOW_47_in_label_range908 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_55_in_label_range916 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_range_point932 = new BitSet(new long[]{0x0200000000208000L});
	public static final BitSet FOLLOW_set_in_range_point935 = new BitSet(new long[]{0x0000000000000002L});
}
