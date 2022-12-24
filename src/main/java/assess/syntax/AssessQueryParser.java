// $ANTLR 3.5.2 AssessQuery.g 2022-12-24 19:34:28

package assess.syntax;
import assess.AssessQuery;
import java.util.Arrays;


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



	// $ANTLR start "parse"
	// AssessQuery.g:14:1: parse returns [AssessQuery parsedQuery] : result= query EOF ;
	public final AssessQuery parse() throws RecognitionException {
		AssessQuery parsedQuery = null;


		AssessQuery result =null;

		try {
			// AssessQuery.g:15:5: (result= query EOF )
			// AssessQuery.g:15:7: result= query EOF
			{
			pushFollow(FOLLOW_query_in_parse49);
			result=query();
			state._fsp--;

			match(input,EOF,FOLLOW_EOF_in_parse51); 
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
	// AssessQuery.g:17:1: query returns [AssessQuery query] : WITH targetCube= ID ( FOR selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST benchmark )? ( USING comparison_function )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling ) ;
	public final AssessQuery query() throws RecognitionException {
		AssessQuery query = null;


		Token targetCube=null;
		Token measurement=null;
		Token labelingMethod=null;
		ParserRuleReturnScope gammas =null;
		List<List<String>> labelingSystem =null;

		try {
			// AssessQuery.g:22:5: ( WITH targetCube= ID ( FOR selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST benchmark )? ( USING comparison_function )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling ) )
			// AssessQuery.g:22:7: WITH targetCube= ID ( FOR selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST benchmark )? ( USING comparison_function )? LABELS (labelingMethod= ID |labelingSystem= custom_labeling )
			{
			match(input,WITH,FOLLOW_WITH_in_query78); 
			targetCube=(Token)match(input,ID,FOLLOW_ID_in_query84); 
			// AssessQuery.g:23:7: ( FOR selection_predicates )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==FOR) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// AssessQuery.g:23:8: FOR selection_predicates
					{
					match(input,FOR,FOLLOW_FOR_in_query93); 
					pushFollow(FOLLOW_selection_predicates_in_query95);
					selection_predicates();
					state._fsp--;

					}
					break;

			}

			match(input,BY,FOLLOW_BY_in_query105); 
			pushFollow(FOLLOW_group_by_set_in_query111);
			gammas=group_by_set();
			state._fsp--;

			match(input,ASSESS,FOLLOW_ASSESS_in_query119); 
			measurement=(Token)match(input,ID,FOLLOW_ID_in_query125); 
			// AssessQuery.g:26:7: ( AGAINST benchmark )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==AGAINST) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// AssessQuery.g:26:8: AGAINST benchmark
					{
					match(input,AGAINST,FOLLOW_AGAINST_in_query134); 
					pushFollow(FOLLOW_benchmark_in_query136);
					benchmark();
					state._fsp--;

					}
					break;

			}

			// AssessQuery.g:27:7: ( USING comparison_function )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==USING) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// AssessQuery.g:27:8: USING comparison_function
					{
					match(input,USING,FOLLOW_USING_in_query147); 
					pushFollow(FOLLOW_comparison_function_in_query149);
					comparison_function();
					state._fsp--;

					}
					break;

			}

			match(input,LABELS,FOLLOW_LABELS_in_query159); 
			// AssessQuery.g:28:14: (labelingMethod= ID |labelingSystem= custom_labeling )
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
					// AssessQuery.g:28:15: labelingMethod= ID
					{
					labelingMethod=(Token)match(input,ID,FOLLOW_ID_in_query166); 
					}
					break;
				case 2 :
					// AssessQuery.g:28:37: labelingSystem= custom_labeling
					{
					pushFollow(FOLLOW_custom_labeling_in_query174);
					labelingSystem=custom_labeling();
					state._fsp--;

					}
					break;

			}

			}


				List<String> parsedGammas = Arrays.asList(((gammas!=null?input.toString(gammas.start,gammas.stop):null)).split(","));
			    query = new AssessQuery((targetCube!=null?targetCube.getText():null), parsedGammas, (measurement!=null?measurement.getText():null), (labelingMethod!=null?labelingMethod.getText():null), labelingSystem);
			    
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
	// AssessQuery.g:31:1: selection_predicates : level ( ',' level )* ;
	public final void selection_predicates() throws RecognitionException {
		try {
			// AssessQuery.g:32:5: ( level ( ',' level )* )
			// AssessQuery.g:32:7: level ( ',' level )*
			{
			pushFollow(FOLLOW_level_in_selection_predicates192);
			level();
			state._fsp--;

			// AssessQuery.g:32:13: ( ',' level )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==36) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// AssessQuery.g:32:14: ',' level
					{
					match(input,36,FOLLOW_36_in_selection_predicates195); 
					pushFollow(FOLLOW_level_in_selection_predicates197);
					level();
					state._fsp--;

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
	}
	// $ANTLR end "selection_predicates"



	// $ANTLR start "level"
	// AssessQuery.g:35:1: level returns [List sigma] : ID '=' '\\'' value= level_value '\\'' ;
	public final List level() throws RecognitionException {
		List sigma = null;


		String value =null;

		try {
			// AssessQuery.g:36:5: ( ID '=' '\\'' value= level_value '\\'' )
			// AssessQuery.g:36:7: ID '=' '\\'' value= level_value '\\''
			{
			match(input,ID,FOLLOW_ID_in_level220); 
			match(input,40,FOLLOW_40_in_level222); 
			match(input,42,FOLLOW_42_in_level224); 
			pushFollow(FOLLOW_level_value_in_level230);
			value=level_value();
			state._fsp--;

			match(input,42,FOLLOW_42_in_level232); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return sigma;
	}
	// $ANTLR end "level"



	// $ANTLR start "level_value"
	// AssessQuery.g:38:1: level_value returns [String value] : ( (ids+= ID )+ | date );
	public final String level_value() throws RecognitionException {
		String value = null;


		Token ids=null;
		List<Object> list_ids=null;
		ParserRuleReturnScope date1 =null;

		try {
			// AssessQuery.g:39:5: ( (ids+= ID )+ | date )
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
					// AssessQuery.g:39:7: (ids+= ID )+
					{
					// AssessQuery.g:39:11: (ids+= ID )+
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
							// AssessQuery.g:39:11: ids+= ID
							{
							ids=(Token)match(input,ID,FOLLOW_ID_in_level_value253); 
							if (list_ids==null) list_ids=new ArrayList<Object>();
							list_ids.add(ids);
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
					// AssessQuery.g:40:7: date
					{
					pushFollow(FOLLOW_date_in_level_value262);
					date1=date();
					state._fsp--;

					value = (date1!=null?input.toString(date1.start,date1.stop):null); 
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
		return value;
	}
	// $ANTLR end "level_value"


	public static class date_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "date"
	// AssessQuery.g:43:1: date : INT '/' INT ( '/' INT )? ;
	public final AssessQueryParser.date_return date() throws RecognitionException {
		AssessQueryParser.date_return retval = new AssessQueryParser.date_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:43:6: ( INT '/' INT ( '/' INT )? )
			// AssessQuery.g:43:8: INT '/' INT ( '/' INT )?
			{
			match(input,INT,FOLLOW_INT_in_date277); 
			match(input,38,FOLLOW_38_in_date279); 
			match(input,INT,FOLLOW_INT_in_date281); 
			// AssessQuery.g:43:20: ( '/' INT )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==38) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// AssessQuery.g:43:21: '/' INT
					{
					match(input,38,FOLLOW_38_in_date284); 
					match(input,INT,FOLLOW_INT_in_date286); 
					}
					break;

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
	// $ANTLR end "date"


	public static class group_by_set_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "group_by_set"
	// AssessQuery.g:45:1: group_by_set : ID ( ',' ID )* ;
	public final AssessQueryParser.group_by_set_return group_by_set() throws RecognitionException {
		AssessQueryParser.group_by_set_return retval = new AssessQueryParser.group_by_set_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:45:14: ( ID ( ',' ID )* )
			// AssessQuery.g:45:16: ID ( ',' ID )*
			{
			match(input,ID,FOLLOW_ID_in_group_by_set297); 
			// AssessQuery.g:45:20: ( ',' ID )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==36) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// AssessQuery.g:45:21: ',' ID
					{
					match(input,36,FOLLOW_36_in_group_by_set301); 
					match(input,ID,FOLLOW_ID_in_group_by_set303); 
					}
					break;

				default :
					break loop9;
				}
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
	// $ANTLR end "group_by_set"



	// $ANTLR start "benchmark"
	// AssessQuery.g:47:1: benchmark : ( constant_benchmark | external_benchmark | sibling_benchmark | past_benchmark );
	public final void benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:48:5: ( constant_benchmark | external_benchmark | sibling_benchmark | past_benchmark )
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
					// AssessQuery.g:48:7: constant_benchmark
					{
					pushFollow(FOLLOW_constant_benchmark_in_benchmark317);
					constant_benchmark();
					state._fsp--;

					}
					break;
				case 2 :
					// AssessQuery.g:49:7: external_benchmark
					{
					pushFollow(FOLLOW_external_benchmark_in_benchmark325);
					external_benchmark();
					state._fsp--;

					}
					break;
				case 3 :
					// AssessQuery.g:50:7: sibling_benchmark
					{
					pushFollow(FOLLOW_sibling_benchmark_in_benchmark333);
					sibling_benchmark();
					state._fsp--;

					}
					break;
				case 4 :
					// AssessQuery.g:51:7: past_benchmark
					{
					pushFollow(FOLLOW_past_benchmark_in_benchmark341);
					past_benchmark();
					state._fsp--;

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
	// $ANTLR end "benchmark"



	// $ANTLR start "constant_benchmark"
	// AssessQuery.g:54:1: constant_benchmark : ( SIGN )? ( INT | FLOAT ) ;
	public final void constant_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:55:5: ( ( SIGN )? ( INT | FLOAT ) )
			// AssessQuery.g:55:7: ( SIGN )? ( INT | FLOAT )
			{
			// AssessQuery.g:55:7: ( SIGN )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==SIGN) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// AssessQuery.g:55:7: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_constant_benchmark358); 
					}
					break;

			}

			if ( input.LA(1)==FLOAT||input.LA(1)==INT ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
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
	// $ANTLR end "constant_benchmark"



	// $ANTLR start "external_benchmark"
	// AssessQuery.g:57:1: external_benchmark : ID '.' ID ;
	public final void external_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:58:5: ( ID '.' ID )
			// AssessQuery.g:58:7: ID '.' ID
			{
			match(input,ID,FOLLOW_ID_in_external_benchmark379); 
			match(input,37,FOLLOW_37_in_external_benchmark381); 
			match(input,ID,FOLLOW_ID_in_external_benchmark383); 
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
	// $ANTLR end "external_benchmark"



	// $ANTLR start "sibling_benchmark"
	// AssessQuery.g:60:1: sibling_benchmark : level ;
	public final void sibling_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:60:19: ( level )
			// AssessQuery.g:60:21: level
			{
			pushFollow(FOLLOW_level_in_sibling_benchmark391);
			level();
			state._fsp--;

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
	// $ANTLR end "sibling_benchmark"



	// $ANTLR start "past_benchmark"
	// AssessQuery.g:62:1: past_benchmark : PAST INT ;
	public final void past_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:63:5: ( PAST INT )
			// AssessQuery.g:63:7: PAST INT
			{
			match(input,PAST,FOLLOW_PAST_in_past_benchmark403); 
			match(input,INT,FOLLOW_INT_in_past_benchmark405); 
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
	// $ANTLR end "past_benchmark"



	// $ANTLR start "comparison_function"
	// AssessQuery.g:65:1: comparison_function : ID '(' comparison_args ')' ;
	public final void comparison_function() throws RecognitionException {
		try {
			// AssessQuery.g:65:21: ( ID '(' comparison_args ')' )
			// AssessQuery.g:65:23: ID '(' comparison_args ')'
			{
			match(input,ID,FOLLOW_ID_in_comparison_function413); 
			match(input,34,FOLLOW_34_in_comparison_function415); 
			pushFollow(FOLLOW_comparison_args_in_comparison_function417);
			comparison_args();
			state._fsp--;

			match(input,35,FOLLOW_35_in_comparison_function419); 
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
	// $ANTLR end "comparison_function"



	// $ANTLR start "comparison_args"
	// AssessQuery.g:67:1: comparison_args : ( ID ',' ( ( 'benchmark.' )? ID | INT ) | comparison_function );
	public final void comparison_args() throws RecognitionException {
		try {
			// AssessQuery.g:67:17: ( ID ',' ( ( 'benchmark.' )? ID | INT ) | comparison_function )
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==ID) ) {
				int LA14_1 = input.LA(2);
				if ( (LA14_1==36) ) {
					alt14=1;
				}
				else if ( (LA14_1==34) ) {
					alt14=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 14, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 14, 0, input);
				throw nvae;
			}

			switch (alt14) {
				case 1 :
					// AssessQuery.g:67:19: ID ',' ( ( 'benchmark.' )? ID | INT )
					{
					match(input,ID,FOLLOW_ID_in_comparison_args427); 
					match(input,36,FOLLOW_36_in_comparison_args429); 
					// AssessQuery.g:67:26: ( ( 'benchmark.' )? ID | INT )
					int alt13=2;
					int LA13_0 = input.LA(1);
					if ( (LA13_0==ID||LA13_0==44) ) {
						alt13=1;
					}
					else if ( (LA13_0==INT) ) {
						alt13=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 13, 0, input);
						throw nvae;
					}

					switch (alt13) {
						case 1 :
							// AssessQuery.g:67:28: ( 'benchmark.' )? ID
							{
							// AssessQuery.g:67:28: ( 'benchmark.' )?
							int alt12=2;
							int LA12_0 = input.LA(1);
							if ( (LA12_0==44) ) {
								alt12=1;
							}
							switch (alt12) {
								case 1 :
									// AssessQuery.g:67:29: 'benchmark.'
									{
									match(input,44,FOLLOW_44_in_comparison_args434); 
									}
									break;

							}

							match(input,ID,FOLLOW_ID_in_comparison_args438); 
							}
							break;
						case 2 :
							// AssessQuery.g:67:49: INT
							{
							match(input,INT,FOLLOW_INT_in_comparison_args442); 
							}
							break;

					}

					}
					break;
				case 2 :
					// AssessQuery.g:68:19: comparison_function
					{
					pushFollow(FOLLOW_comparison_function_in_comparison_args463);
					comparison_function();
					state._fsp--;

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
	// $ANTLR end "comparison_args"



	// $ANTLR start "custom_labeling"
	// AssessQuery.g:71:1: custom_labeling returns [List<List<String>> labelingTerms] : '{' term= label_term ( ',' term= label_term )* '}' ;
	public final List<List<String>> custom_labeling() throws RecognitionException {
		List<List<String>> labelingTerms = null;


		List<String> term =null;

		labelingTerms = new ArrayList<List<String>>();
		try {
			// AssessQuery.g:73:5: ( '{' term= label_term ( ',' term= label_term )* '}' )
			// AssessQuery.g:73:7: '{' term= label_term ( ',' term= label_term )* '}'
			{
			match(input,46,FOLLOW_46_in_custom_labeling505); 
			pushFollow(FOLLOW_label_term_in_custom_labeling511);
			term=label_term();
			state._fsp--;

			labelingTerms.add(term);
			// AssessQuery.g:73:55: ( ',' term= label_term )*
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( (LA15_0==36) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// AssessQuery.g:73:56: ',' term= label_term
					{
					match(input,36,FOLLOW_36_in_custom_labeling515); 
					pushFollow(FOLLOW_label_term_in_custom_labeling521);
					term=label_term();
					state._fsp--;

					labelingTerms.add(term);
					}
					break;

				default :
					break loop15;
				}
			}

			match(input,47,FOLLOW_47_in_custom_labeling527); 
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
	// AssessQuery.g:76:1: label_term returns [List<String> term] : range= label_range ':' label= ID ;
	public final List<String> label_term() throws RecognitionException {
		List<String> term = null;


		Token label=null;
		List<String> range =null;

		try {
			// AssessQuery.g:78:5: (range= label_range ':' label= ID )
			// AssessQuery.g:78:7: range= label_range ':' label= ID
			{
			pushFollow(FOLLOW_label_range_in_label_term558);
			range=label_range();
			state._fsp--;

			match(input,39,FOLLOW_39_in_label_term560); 
			label=(Token)match(input,ID,FOLLOW_ID_in_label_term564); 
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
	// AssessQuery.g:80:1: label_range returns [List<String> limits] : ( '[' | '(' ) start= range_point ',' end= range_point ( ')' | ']' ) ;
	public final List<String> label_range() throws RecognitionException {
		List<String> limits = null;


		ParserRuleReturnScope start =null;
		ParserRuleReturnScope end =null;

		limits = new ArrayList<String>();
		try {
			// AssessQuery.g:82:5: ( ( '[' | '(' ) start= range_point ',' end= range_point ( ')' | ']' ) )
			// AssessQuery.g:82:7: ( '[' | '(' ) start= range_point ',' end= range_point ( ')' | ']' )
			{
			// AssessQuery.g:82:7: ( '[' | '(' )
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
					// AssessQuery.g:82:9: '['
					{
					match(input,41,FOLLOW_41_in_label_range593); 
					 limits.add(">="); 
					}
					break;
				case 2 :
					// AssessQuery.g:82:38: '('
					{
					match(input,34,FOLLOW_34_in_label_range599); 
					 limits.add(">"); 
					}
					break;

			}

			pushFollow(FOLLOW_range_point_in_label_range614);
			start=range_point();
			state._fsp--;

			 limits.add((start!=null?input.toString(start.start,start.stop):null)); 
			match(input,36,FOLLOW_36_in_label_range618); 
			pushFollow(FOLLOW_range_point_in_label_range630);
			end=range_point();
			state._fsp--;

			 limits.add((end!=null?input.toString(end.start,end.stop):null)); 
			// AssessQuery.g:85:7: ( ')' | ']' )
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
					// AssessQuery.g:85:8: ')'
					{
					match(input,35,FOLLOW_35_in_label_range641); 
					 limits.add("<"); 
					}
					break;
				case 2 :
					// AssessQuery.g:85:34: ']'
					{
					match(input,43,FOLLOW_43_in_label_range645); 
					 limits.add("<="); 
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
		return limits;
	}
	// $ANTLR end "label_range"


	public static class range_point_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "range_point"
	// AssessQuery.g:88:1: range_point : ( SIGN )? ( INT | FLOAT | 'inf' ) ;
	public final AssessQueryParser.range_point_return range_point() throws RecognitionException {
		AssessQueryParser.range_point_return retval = new AssessQueryParser.range_point_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:88:13: ( ( SIGN )? ( INT | FLOAT | 'inf' ) )
			// AssessQuery.g:88:15: ( SIGN )? ( INT | FLOAT | 'inf' )
			{
			// AssessQuery.g:88:15: ( SIGN )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==SIGN) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// AssessQuery.g:88:15: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_range_point661); 
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



	public static final BitSet FOLLOW_query_in_parse49 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_parse51 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WITH_in_query78 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_query84 = new BitSet(new long[]{0x0000000000001100L});
	public static final BitSet FOLLOW_FOR_in_query93 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_selection_predicates_in_query95 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_BY_in_query105 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_group_by_set_in_query111 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_ASSESS_in_query119 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_query125 = new BitSet(new long[]{0x0000000020080020L});
	public static final BitSet FOLLOW_AGAINST_in_query134 = new BitSet(new long[]{0x0000000004830800L});
	public static final BitSet FOLLOW_benchmark_in_query136 = new BitSet(new long[]{0x0000000020080000L});
	public static final BitSet FOLLOW_USING_in_query147 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_comparison_function_in_query149 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_LABELS_in_query159 = new BitSet(new long[]{0x0000400000010000L});
	public static final BitSet FOLLOW_ID_in_query166 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_custom_labeling_in_query174 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_level_in_selection_predicates192 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_36_in_selection_predicates195 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_level_in_selection_predicates197 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_ID_in_level220 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_40_in_level222 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_level224 = new BitSet(new long[]{0x0000000000030000L});
	public static final BitSet FOLLOW_level_value_in_level230 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_level232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_level_value253 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_date_in_level_value262 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_date277 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_38_in_date279 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_date281 = new BitSet(new long[]{0x0000004000000002L});
	public static final BitSet FOLLOW_38_in_date284 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_date286 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_group_by_set297 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_36_in_group_by_set301 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_group_by_set303 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_constant_benchmark_in_benchmark317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_external_benchmark_in_benchmark325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sibling_benchmark_in_benchmark333 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_past_benchmark_in_benchmark341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_constant_benchmark358 = new BitSet(new long[]{0x0000000000020800L});
	public static final BitSet FOLLOW_set_in_constant_benchmark361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_external_benchmark379 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_37_in_external_benchmark381 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_external_benchmark383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_level_in_sibling_benchmark391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PAST_in_past_benchmark403 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_past_benchmark405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_function413 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_34_in_comparison_function415 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_comparison_args_in_comparison_function417 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_35_in_comparison_function419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_args427 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_36_in_comparison_args429 = new BitSet(new long[]{0x0000100000030000L});
	public static final BitSet FOLLOW_44_in_comparison_args434 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_comparison_args438 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_comparison_args442 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_function_in_comparison_args463 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_46_in_custom_labeling505 = new BitSet(new long[]{0x0000020400000000L});
	public static final BitSet FOLLOW_label_term_in_custom_labeling511 = new BitSet(new long[]{0x0000801000000000L});
	public static final BitSet FOLLOW_36_in_custom_labeling515 = new BitSet(new long[]{0x0000020400000000L});
	public static final BitSet FOLLOW_label_term_in_custom_labeling521 = new BitSet(new long[]{0x0000801000000000L});
	public static final BitSet FOLLOW_47_in_custom_labeling527 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_label_range_in_label_term558 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_39_in_label_term560 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_label_term564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_41_in_label_range593 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_34_in_label_range599 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_range_point_in_label_range614 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_36_in_label_range618 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_range_point_in_label_range630 = new BitSet(new long[]{0x0000080800000000L});
	public static final BitSet FOLLOW_35_in_label_range641 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_43_in_label_range645 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_range_point661 = new BitSet(new long[]{0x0000200000020800L});
	public static final BitSet FOLLOW_set_in_range_point664 = new BitSet(new long[]{0x0000000000000002L});
}
