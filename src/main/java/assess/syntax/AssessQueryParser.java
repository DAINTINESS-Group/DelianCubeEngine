// $ANTLR 3.5.2 AssessQuery.g 2022-12-23 18:59:53

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
	// AssessQuery.g:17:1: query returns [AssessQuery query] : WITH targetCube= ID ( FOR selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST benchmark )? ( USING comparison_function )? LABELS labeling_function ;
	public final AssessQuery query() throws RecognitionException {
		AssessQuery query = null;


		Token targetCube=null;
		Token measurement=null;
		ParserRuleReturnScope gammas =null;

		try {
			// AssessQuery.g:26:5: ( WITH targetCube= ID ( FOR selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST benchmark )? ( USING comparison_function )? LABELS labeling_function )
			// AssessQuery.g:26:7: WITH targetCube= ID ( FOR selection_predicates )? BY gammas= group_by_set ASSESS measurement= ID ( AGAINST benchmark )? ( USING comparison_function )? LABELS labeling_function
			{
			match(input,WITH,FOLLOW_WITH_in_query78); 
			targetCube=(Token)match(input,ID,FOLLOW_ID_in_query84); 
			// AssessQuery.g:27:7: ( FOR selection_predicates )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==FOR) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// AssessQuery.g:27:8: FOR selection_predicates
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
			// AssessQuery.g:30:7: ( AGAINST benchmark )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==AGAINST) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// AssessQuery.g:30:8: AGAINST benchmark
					{
					match(input,AGAINST,FOLLOW_AGAINST_in_query134); 
					pushFollow(FOLLOW_benchmark_in_query136);
					benchmark();
					state._fsp--;

					}
					break;

			}

			// AssessQuery.g:31:7: ( USING comparison_function )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==USING) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// AssessQuery.g:31:8: USING comparison_function
					{
					match(input,USING,FOLLOW_USING_in_query147); 
					pushFollow(FOLLOW_comparison_function_in_query149);
					comparison_function();
					state._fsp--;

					}
					break;

			}

			match(input,LABELS,FOLLOW_LABELS_in_query159); 
			pushFollow(FOLLOW_labeling_function_in_query161);
			labeling_function();
			state._fsp--;

			}


			    String parsedTargetCube = (targetCube!=null?targetCube.getText():null);
			    String parsedMeasurement = (measurement!=null?measurement.getText():null);
				List<String> parsedGammas = Arrays.asList(((gammas!=null?input.toString(gammas.start,gammas.stop):null)).split(","));


			    query = new AssessQuery(parsedTargetCube, parsedGammas, parsedMeasurement);
			    
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
	// AssessQuery.g:35:1: selection_predicates : level ( ',' level )* ;
	public final void selection_predicates() throws RecognitionException {
		try {
			// AssessQuery.g:36:5: ( level ( ',' level )* )
			// AssessQuery.g:36:7: level ( ',' level )*
			{
			pushFollow(FOLLOW_level_in_selection_predicates178);
			level();
			state._fsp--;

			// AssessQuery.g:36:13: ( ',' level )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==36) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// AssessQuery.g:36:14: ',' level
					{
					match(input,36,FOLLOW_36_in_selection_predicates181); 
					pushFollow(FOLLOW_level_in_selection_predicates183);
					level();
					state._fsp--;

					}
					break;

				default :
					break loop4;
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
	// AssessQuery.g:39:1: level returns [List sigma] : ID '=' '\\'' value= level_value '\\'' ;
	public final List level() throws RecognitionException {
		List sigma = null;


		String value =null;

		try {
			// AssessQuery.g:40:5: ( ID '=' '\\'' value= level_value '\\'' )
			// AssessQuery.g:40:7: ID '=' '\\'' value= level_value '\\''
			{
			match(input,ID,FOLLOW_ID_in_level206); 
			match(input,40,FOLLOW_40_in_level208); 
			match(input,42,FOLLOW_42_in_level210); 
			pushFollow(FOLLOW_level_value_in_level216);
			value=level_value();
			state._fsp--;

			match(input,42,FOLLOW_42_in_level218); 
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
	// AssessQuery.g:42:1: level_value returns [String value] : ( (ids+= ID )+ | date );
	public final String level_value() throws RecognitionException {
		String value = null;


		Token ids=null;
		List<Object> list_ids=null;
		ParserRuleReturnScope date1 =null;

		try {
			// AssessQuery.g:43:5: ( (ids+= ID )+ | date )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==ID) ) {
				alt6=1;
			}
			else if ( (LA6_0==INT) ) {
				alt6=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// AssessQuery.g:43:7: (ids+= ID )+
					{
					// AssessQuery.g:43:11: (ids+= ID )+
					int cnt5=0;
					loop5:
					while (true) {
						int alt5=2;
						int LA5_0 = input.LA(1);
						if ( (LA5_0==ID) ) {
							alt5=1;
						}

						switch (alt5) {
						case 1 :
							// AssessQuery.g:43:11: ids+= ID
							{
							ids=(Token)match(input,ID,FOLLOW_ID_in_level_value239); 
							if (list_ids==null) list_ids=new ArrayList<Object>();
							list_ids.add(ids);
							}
							break;

						default :
							if ( cnt5 >= 1 ) break loop5;
							EarlyExitException eee = new EarlyExitException(5, input);
							throw eee;
						}
						cnt5++;
					}

					}
					break;
				case 2 :
					// AssessQuery.g:44:7: date
					{
					pushFollow(FOLLOW_date_in_level_value248);
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
	// AssessQuery.g:47:1: date : INT '/' INT ( '/' INT )? ;
	public final AssessQueryParser.date_return date() throws RecognitionException {
		AssessQueryParser.date_return retval = new AssessQueryParser.date_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:47:6: ( INT '/' INT ( '/' INT )? )
			// AssessQuery.g:47:8: INT '/' INT ( '/' INT )?
			{
			match(input,INT,FOLLOW_INT_in_date263); 
			match(input,38,FOLLOW_38_in_date265); 
			match(input,INT,FOLLOW_INT_in_date267); 
			// AssessQuery.g:47:20: ( '/' INT )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==38) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// AssessQuery.g:47:21: '/' INT
					{
					match(input,38,FOLLOW_38_in_date270); 
					match(input,INT,FOLLOW_INT_in_date272); 
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
	// AssessQuery.g:49:1: group_by_set : ID ( ',' ID )* ;
	public final AssessQueryParser.group_by_set_return group_by_set() throws RecognitionException {
		AssessQueryParser.group_by_set_return retval = new AssessQueryParser.group_by_set_return();
		retval.start = input.LT(1);

		try {
			// AssessQuery.g:49:14: ( ID ( ',' ID )* )
			// AssessQuery.g:49:16: ID ( ',' ID )*
			{
			match(input,ID,FOLLOW_ID_in_group_by_set283); 
			// AssessQuery.g:49:20: ( ',' ID )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==36) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// AssessQuery.g:49:21: ',' ID
					{
					match(input,36,FOLLOW_36_in_group_by_set287); 
					match(input,ID,FOLLOW_ID_in_group_by_set289); 
					}
					break;

				default :
					break loop8;
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
	// AssessQuery.g:51:1: benchmark : ( constant_benchmark | external_benchmark | sibling_benchmark | past_benchmark );
	public final void benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:52:5: ( constant_benchmark | external_benchmark | sibling_benchmark | past_benchmark )
			int alt9=4;
			switch ( input.LA(1) ) {
			case FLOAT:
			case INT:
			case SIGN:
				{
				alt9=1;
				}
				break;
			case ID:
				{
				int LA9_2 = input.LA(2);
				if ( (LA9_2==37) ) {
					alt9=2;
				}
				else if ( (LA9_2==40) ) {
					alt9=3;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case PAST:
				{
				alt9=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}
			switch (alt9) {
				case 1 :
					// AssessQuery.g:52:7: constant_benchmark
					{
					pushFollow(FOLLOW_constant_benchmark_in_benchmark303);
					constant_benchmark();
					state._fsp--;

					}
					break;
				case 2 :
					// AssessQuery.g:53:7: external_benchmark
					{
					pushFollow(FOLLOW_external_benchmark_in_benchmark311);
					external_benchmark();
					state._fsp--;

					}
					break;
				case 3 :
					// AssessQuery.g:54:7: sibling_benchmark
					{
					pushFollow(FOLLOW_sibling_benchmark_in_benchmark319);
					sibling_benchmark();
					state._fsp--;

					}
					break;
				case 4 :
					// AssessQuery.g:55:7: past_benchmark
					{
					pushFollow(FOLLOW_past_benchmark_in_benchmark327);
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
	// AssessQuery.g:58:1: constant_benchmark : ( SIGN )? ( INT | FLOAT ) ;
	public final void constant_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:59:5: ( ( SIGN )? ( INT | FLOAT ) )
			// AssessQuery.g:59:7: ( SIGN )? ( INT | FLOAT )
			{
			// AssessQuery.g:59:7: ( SIGN )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==SIGN) ) {
				alt10=1;
			}
			switch (alt10) {
				case 1 :
					// AssessQuery.g:59:7: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_constant_benchmark344); 
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
	// AssessQuery.g:61:1: external_benchmark : ID '.' ID ;
	public final void external_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:62:5: ( ID '.' ID )
			// AssessQuery.g:62:7: ID '.' ID
			{
			match(input,ID,FOLLOW_ID_in_external_benchmark365); 
			match(input,37,FOLLOW_37_in_external_benchmark367); 
			match(input,ID,FOLLOW_ID_in_external_benchmark369); 
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
	// AssessQuery.g:64:1: sibling_benchmark : level ;
	public final void sibling_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:64:19: ( level )
			// AssessQuery.g:64:21: level
			{
			pushFollow(FOLLOW_level_in_sibling_benchmark377);
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
	// AssessQuery.g:66:1: past_benchmark : PAST INT ;
	public final void past_benchmark() throws RecognitionException {
		try {
			// AssessQuery.g:67:5: ( PAST INT )
			// AssessQuery.g:67:7: PAST INT
			{
			match(input,PAST,FOLLOW_PAST_in_past_benchmark389); 
			match(input,INT,FOLLOW_INT_in_past_benchmark391); 
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
	// AssessQuery.g:69:1: comparison_function : ID '(' comparison_args ')' ;
	public final void comparison_function() throws RecognitionException {
		try {
			// AssessQuery.g:69:21: ( ID '(' comparison_args ')' )
			// AssessQuery.g:69:23: ID '(' comparison_args ')'
			{
			match(input,ID,FOLLOW_ID_in_comparison_function399); 
			match(input,34,FOLLOW_34_in_comparison_function401); 
			pushFollow(FOLLOW_comparison_args_in_comparison_function403);
			comparison_args();
			state._fsp--;

			match(input,35,FOLLOW_35_in_comparison_function405); 
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
	// AssessQuery.g:71:1: comparison_args : ( ID ',' ( ( 'benchmark.' )? ID | INT ) | comparison_function );
	public final void comparison_args() throws RecognitionException {
		try {
			// AssessQuery.g:71:17: ( ID ',' ( ( 'benchmark.' )? ID | INT ) | comparison_function )
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==ID) ) {
				int LA13_1 = input.LA(2);
				if ( (LA13_1==36) ) {
					alt13=1;
				}
				else if ( (LA13_1==34) ) {
					alt13=2;
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
					// AssessQuery.g:71:19: ID ',' ( ( 'benchmark.' )? ID | INT )
					{
					match(input,ID,FOLLOW_ID_in_comparison_args413); 
					match(input,36,FOLLOW_36_in_comparison_args415); 
					// AssessQuery.g:71:26: ( ( 'benchmark.' )? ID | INT )
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==ID||LA12_0==44) ) {
						alt12=1;
					}
					else if ( (LA12_0==INT) ) {
						alt12=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 12, 0, input);
						throw nvae;
					}

					switch (alt12) {
						case 1 :
							// AssessQuery.g:71:28: ( 'benchmark.' )? ID
							{
							// AssessQuery.g:71:28: ( 'benchmark.' )?
							int alt11=2;
							int LA11_0 = input.LA(1);
							if ( (LA11_0==44) ) {
								alt11=1;
							}
							switch (alt11) {
								case 1 :
									// AssessQuery.g:71:29: 'benchmark.'
									{
									match(input,44,FOLLOW_44_in_comparison_args420); 
									}
									break;

							}

							match(input,ID,FOLLOW_ID_in_comparison_args424); 
							}
							break;
						case 2 :
							// AssessQuery.g:71:49: INT
							{
							match(input,INT,FOLLOW_INT_in_comparison_args428); 
							}
							break;

					}

					}
					break;
				case 2 :
					// AssessQuery.g:72:19: comparison_function
					{
					pushFollow(FOLLOW_comparison_function_in_comparison_args449);
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



	// $ANTLR start "labeling_function"
	// AssessQuery.g:75:1: labeling_function : (name= ID | '{' term= label_term ( ',' label_term )* '}' );
	public final void labeling_function() throws RecognitionException {
		Token name=null;
		List term =null;

		try {
			// AssessQuery.g:76:5: (name= ID | '{' term= label_term ( ',' label_term )* '}' )
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==ID) ) {
				alt15=1;
			}
			else if ( (LA15_0==46) ) {
				alt15=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// AssessQuery.g:76:7: name= ID
					{
					name=(Token)match(input,ID,FOLLOW_ID_in_labeling_function482); 
					}
					break;
				case 2 :
					// AssessQuery.g:77:7: '{' term= label_term ( ',' label_term )* '}'
					{
					match(input,46,FOLLOW_46_in_labeling_function490); 
					pushFollow(FOLLOW_label_term_in_labeling_function496);
					term=label_term();
					state._fsp--;

					// AssessQuery.g:77:29: ( ',' label_term )*
					loop14:
					while (true) {
						int alt14=2;
						int LA14_0 = input.LA(1);
						if ( (LA14_0==36) ) {
							alt14=1;
						}

						switch (alt14) {
						case 1 :
							// AssessQuery.g:77:30: ',' label_term
							{
							match(input,36,FOLLOW_36_in_labeling_function499); 
							pushFollow(FOLLOW_label_term_in_labeling_function501);
							label_term();
							state._fsp--;

							}
							break;

						default :
							break loop14;
						}
					}

					match(input,47,FOLLOW_47_in_labeling_function505); 
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
	// $ANTLR end "labeling_function"



	// $ANTLR start "label_term"
	// AssessQuery.g:80:1: label_term returns [List term] : range= label_range ':' label= ID ;
	public final List label_term() throws RecognitionException {
		List term = null;


		Token label=null;

		try {
			// AssessQuery.g:81:5: (range= label_range ':' label= ID )
			// AssessQuery.g:81:7: range= label_range ':' label= ID
			{
			pushFollow(FOLLOW_label_range_in_label_term528);
			label_range();
			state._fsp--;

			match(input,39,FOLLOW_39_in_label_term530); 
			label=(Token)match(input,ID,FOLLOW_ID_in_label_term534); 
			}

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
	// AssessQuery.g:83:1: label_range : start_limit= ( '[' | '(' ) start= range_point ',' end= range_point end_limit= ( ')' | ']' ) ;
	public final void label_range() throws RecognitionException {
		Token start_limit=null;
		Token end_limit=null;

		try {
			// AssessQuery.g:84:5: (start_limit= ( '[' | '(' ) start= range_point ',' end= range_point end_limit= ( ')' | ']' ) )
			// AssessQuery.g:84:7: start_limit= ( '[' | '(' ) start= range_point ',' end= range_point end_limit= ( ')' | ']' )
			{
			start_limit=input.LT(1);
			if ( input.LA(1)==34||input.LA(1)==41 ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_range_point_in_label_range569);
			range_point();
			state._fsp--;

			match(input,36,FOLLOW_36_in_label_range571); 
			pushFollow(FOLLOW_range_point_in_label_range583);
			range_point();
			state._fsp--;

			end_limit=input.LT(1);
			if ( input.LA(1)==35||input.LA(1)==43 ) {
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
	// $ANTLR end "label_range"



	// $ANTLR start "range_point"
	// AssessQuery.g:90:1: range_point : ( SIGN )? ( INT | FLOAT | 'inf' ) ;
	public final void range_point() throws RecognitionException {
		try {
			// AssessQuery.g:90:13: ( ( SIGN )? ( INT | FLOAT | 'inf' ) )
			// AssessQuery.g:90:15: ( SIGN )? ( INT | FLOAT | 'inf' )
			{
			// AssessQuery.g:90:15: ( SIGN )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==SIGN) ) {
				alt16=1;
			}
			switch (alt16) {
				case 1 :
					// AssessQuery.g:90:15: SIGN
					{
					match(input,SIGN,FOLLOW_SIGN_in_range_point612); 
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

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
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
	public static final BitSet FOLLOW_labeling_function_in_query161 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_level_in_selection_predicates178 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_36_in_selection_predicates181 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_level_in_selection_predicates183 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_ID_in_level206 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_40_in_level208 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_level210 = new BitSet(new long[]{0x0000000000030000L});
	public static final BitSet FOLLOW_level_value_in_level216 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_level218 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_level_value239 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_date_in_level_value248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_date263 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_38_in_date265 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_date267 = new BitSet(new long[]{0x0000004000000002L});
	public static final BitSet FOLLOW_38_in_date270 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_date272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_group_by_set283 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_36_in_group_by_set287 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_group_by_set289 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_constant_benchmark_in_benchmark303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_external_benchmark_in_benchmark311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sibling_benchmark_in_benchmark319 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_past_benchmark_in_benchmark327 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_constant_benchmark344 = new BitSet(new long[]{0x0000000000020800L});
	public static final BitSet FOLLOW_set_in_constant_benchmark347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_external_benchmark365 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_37_in_external_benchmark367 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_external_benchmark369 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_level_in_sibling_benchmark377 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PAST_in_past_benchmark389 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_in_past_benchmark391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_function399 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_34_in_comparison_function401 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_comparison_args_in_comparison_function403 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_35_in_comparison_function405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_comparison_args413 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_36_in_comparison_args415 = new BitSet(new long[]{0x0000100000030000L});
	public static final BitSet FOLLOW_44_in_comparison_args420 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_comparison_args424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_comparison_args428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_function_in_comparison_args449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_labeling_function482 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_46_in_labeling_function490 = new BitSet(new long[]{0x0000020400000000L});
	public static final BitSet FOLLOW_label_term_in_labeling_function496 = new BitSet(new long[]{0x0000801000000000L});
	public static final BitSet FOLLOW_36_in_labeling_function499 = new BitSet(new long[]{0x0000020400000000L});
	public static final BitSet FOLLOW_label_term_in_labeling_function501 = new BitSet(new long[]{0x0000801000000000L});
	public static final BitSet FOLLOW_47_in_labeling_function505 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_label_range_in_label_term528 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_39_in_label_term530 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_ID_in_label_term534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_label_range551 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_range_point_in_label_range569 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_36_in_label_range571 = new BitSet(new long[]{0x0000200004020800L});
	public static final BitSet FOLLOW_range_point_in_label_range583 = new BitSet(new long[]{0x0000080800000000L});
	public static final BitSet FOLLOW_set_in_label_range595 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SIGN_in_range_point612 = new BitSet(new long[]{0x0000200000020800L});
	public static final BitSet FOLLOW_set_in_range_point615 = new BitSet(new long[]{0x0000000000000002L});
}
