// $ANTLR 3.5.1 C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g 2023-02-22 13:25:59

	package analyze.syntax;
	
	import java.util.ArrayList;
	import java.util.HashMap;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class AnalyzeOperatorParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "AGGRFUNC", "ANALYZE", "AND", 
		"AS", "B", "BY", "C", "COMMA", "D", "DIGIT", "E", "EQUAL", "F", "FOR", 
		"FROM", "G", "GROUP", "H", "I", "J", "K", "L", "LETTER", "LPARENTHESIS", 
		"M", "N", "NUMBER", "O", "P", "Q", "R", "RPARENTHESIS", "S", "T", "TEXTVALUE", 
		"U", "V", "W", "WORD", "WS", "X", "Y", "Z"
	};
	public static final int EOF=-1;
	public static final int A=4;
	public static final int AGGRFUNC=5;
	public static final int ANALYZE=6;
	public static final int AND=7;
	public static final int AS=8;
	public static final int B=9;
	public static final int BY=10;
	public static final int C=11;
	public static final int COMMA=12;
	public static final int D=13;
	public static final int DIGIT=14;
	public static final int E=15;
	public static final int EQUAL=16;
	public static final int F=17;
	public static final int FOR=18;
	public static final int FROM=19;
	public static final int G=20;
	public static final int GROUP=21;
	public static final int H=22;
	public static final int I=23;
	public static final int J=24;
	public static final int K=25;
	public static final int L=26;
	public static final int LETTER=27;
	public static final int LPARENTHESIS=28;
	public static final int M=29;
	public static final int N=30;
	public static final int NUMBER=31;
	public static final int O=32;
	public static final int P=33;
	public static final int Q=34;
	public static final int R=35;
	public static final int RPARENTHESIS=36;
	public static final int S=37;
	public static final int T=38;
	public static final int TEXTVALUE=39;
	public static final int U=40;
	public static final int V=41;
	public static final int W=42;
	public static final int WORD=43;
	public static final int WS=44;
	public static final int X=45;
	public static final int Y=46;
	public static final int Z=47;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public AnalyzeOperatorParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public AnalyzeOperatorParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return AnalyzeOperatorParser.tokenNames; }
	@Override public String getGrammarFileName() { return "C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g"; }


		String aggrFunc;
		String measure;
		String cubeName;
		ArrayList<String> sigmaExpressions;
		HashMap<String,String> sigmaExpressionsValues;
		ArrayList<String> gammaExpressions;
		String queryAlias;



	// $ANTLR start "start"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:30:1: start : parse ;
	public final void start() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:30:6: ( parse )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:30:7: parse
			{

				sigmaExpressions = new ArrayList<String>();
				sigmaExpressionsValues = new HashMap<String,String>();
				gammaExpressions = new ArrayList<String>();

			pushFollow(FOLLOW_parse_in_start82);
			parse();
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
	// $ANTLR end "start"



	// $ANTLR start "parse"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:36:1: parse : ANALYZE aggrfunc LPARENTHESIS measure RPARENTHESIS FROM cubeName FOR sigmaExpressions GROUP BY gammaExpressions AS queryAlias ;
	public final void parse() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:36:6: ( ANALYZE aggrfunc LPARENTHESIS measure RPARENTHESIS FROM cubeName FOR sigmaExpressions GROUP BY gammaExpressions AS queryAlias )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:36:8: ANALYZE aggrfunc LPARENTHESIS measure RPARENTHESIS FROM cubeName FOR sigmaExpressions GROUP BY gammaExpressions AS queryAlias
			{
			match(input,ANALYZE,FOLLOW_ANALYZE_in_parse89); 
			pushFollow(FOLLOW_aggrfunc_in_parse91);
			aggrfunc();
			state._fsp--;

			match(input,LPARENTHESIS,FOLLOW_LPARENTHESIS_in_parse93); 
			pushFollow(FOLLOW_measure_in_parse95);
			measure();
			state._fsp--;

			match(input,RPARENTHESIS,FOLLOW_RPARENTHESIS_in_parse97); 
			match(input,FROM,FOLLOW_FROM_in_parse99); 
			pushFollow(FOLLOW_cubeName_in_parse101);
			cubeName();
			state._fsp--;

			match(input,FOR,FOLLOW_FOR_in_parse103); 
			pushFollow(FOLLOW_sigmaExpressions_in_parse105);
			sigmaExpressions();
			state._fsp--;

			match(input,GROUP,FOLLOW_GROUP_in_parse107); 
			match(input,BY,FOLLOW_BY_in_parse109); 
			pushFollow(FOLLOW_gammaExpressions_in_parse111);
			gammaExpressions();
			state._fsp--;

			match(input,AS,FOLLOW_AS_in_parse113); 
			pushFollow(FOLLOW_queryAlias_in_parse115);
			queryAlias();
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
	// $ANTLR end "parse"



	// $ANTLR start "aggrfunc"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:38:1: aggrfunc : AGGRFUNC ;
	public final void aggrfunc() throws RecognitionException {
		Token AGGRFUNC1=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:38:9: ( AGGRFUNC )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:38:10: AGGRFUNC
			{
			AGGRFUNC1=(Token)match(input,AGGRFUNC,FOLLOW_AGGRFUNC_in_aggrfunc121); 
			aggrFunc=(AGGRFUNC1!=null?AGGRFUNC1.getText():null);
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
	// $ANTLR end "aggrfunc"



	// $ANTLR start "measure"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:40:1: measure : WORD ;
	public final void measure() throws RecognitionException {
		Token WORD2=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:40:8: ( WORD )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:40:10: WORD
			{
			WORD2=(Token)match(input,WORD,FOLLOW_WORD_in_measure129); 
			measure=(WORD2!=null?WORD2.getText():null);
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
	// $ANTLR end "measure"



	// $ANTLR start "cubeName"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:42:1: cubeName : WORD ;
	public final void cubeName() throws RecognitionException {
		Token WORD3=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:42:9: ( WORD )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:42:11: WORD
			{
			WORD3=(Token)match(input,WORD,FOLLOW_WORD_in_cubeName137); 
			cubeName=(WORD3!=null?WORD3.getText():null);
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
	// $ANTLR end "cubeName"



	// $ANTLR start "sigmaExpressions"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:44:1: sigmaExpressions : sigmaExpression ( AND sigmaExpression )* ;
	public final void sigmaExpressions() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:44:17: ( sigmaExpression ( AND sigmaExpression )* )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:44:19: sigmaExpression ( AND sigmaExpression )*
			{
			pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressions145);
			sigmaExpression();
			state._fsp--;

			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:44:34: ( AND sigmaExpression )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==AND) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:44:35: AND sigmaExpression
					{
					match(input,AND,FOLLOW_AND_in_sigmaExpressions147); 
					pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressions149);
					sigmaExpression();
					state._fsp--;

					}
					break;

				default :
					break loop1;
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
	// $ANTLR end "sigmaExpressions"



	// $ANTLR start "sigmaExpression"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:46:1: sigmaExpression : ( sigmaExpressionNumberValue | sigmaExpressionTextValue );
	public final void sigmaExpression() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:46:16: ( sigmaExpressionNumberValue | sigmaExpressionTextValue )
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==WORD) ) {
				int LA2_1 = input.LA(2);
				if ( (LA2_1==EQUAL) ) {
					int LA2_2 = input.LA(3);
					if ( (LA2_2==NUMBER) ) {
						alt2=1;
					}
					else if ( (LA2_2==TEXTVALUE) ) {
						alt2=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 2, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 2, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}

			switch (alt2) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:46:18: sigmaExpressionNumberValue
					{
					pushFollow(FOLLOW_sigmaExpressionNumberValue_in_sigmaExpression158);
					sigmaExpressionNumberValue();
					state._fsp--;

					}
					break;
				case 2 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:46:45: sigmaExpressionTextValue
					{
					pushFollow(FOLLOW_sigmaExpressionTextValue_in_sigmaExpression160);
					sigmaExpressionTextValue();
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
	// $ANTLR end "sigmaExpression"



	// $ANTLR start "sigmaExpressionNumberValue"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:48:1: sigmaExpressionNumberValue : WORD EQUAL NUMBER ;
	public final void sigmaExpressionNumberValue() throws RecognitionException {
		Token WORD4=null;
		Token NUMBER5=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:48:27: ( WORD EQUAL NUMBER )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:48:29: WORD EQUAL NUMBER
			{
			WORD4=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpressionNumberValue168); 
			match(input,EQUAL,FOLLOW_EQUAL_in_sigmaExpressionNumberValue170); 
			NUMBER5=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_sigmaExpressionNumberValue172); 
			sigmaExpressions.add((WORD4!=null?WORD4.getText():null));sigmaExpressionsValues.put((WORD4!=null?WORD4.getText():null),(NUMBER5!=null?NUMBER5.getText():null));
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
	// $ANTLR end "sigmaExpressionNumberValue"



	// $ANTLR start "sigmaExpressionTextValue"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:50:1: sigmaExpressionTextValue : WORD EQUAL TEXTVALUE ;
	public final void sigmaExpressionTextValue() throws RecognitionException {
		Token WORD6=null;
		Token TEXTVALUE7=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:50:25: ( WORD EQUAL TEXTVALUE )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:50:27: WORD EQUAL TEXTVALUE
			{
			WORD6=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpressionTextValue181); 
			match(input,EQUAL,FOLLOW_EQUAL_in_sigmaExpressionTextValue183); 
			TEXTVALUE7=(Token)match(input,TEXTVALUE,FOLLOW_TEXTVALUE_in_sigmaExpressionTextValue185); 
			sigmaExpressions.add((WORD6!=null?WORD6.getText():null));sigmaExpressionsValues.put((WORD6!=null?WORD6.getText():null),(TEXTVALUE7!=null?TEXTVALUE7.getText():null));
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
	// $ANTLR end "sigmaExpressionTextValue"



	// $ANTLR start "gammaExpressions"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:52:1: gammaExpressions : gammaExpression ( COMMA gammaExpression )* ;
	public final void gammaExpressions() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:52:17: ( gammaExpression ( COMMA gammaExpression )* )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:52:19: gammaExpression ( COMMA gammaExpression )*
			{
			pushFollow(FOLLOW_gammaExpression_in_gammaExpressions195);
			gammaExpression();
			state._fsp--;

			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:52:35: ( COMMA gammaExpression )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==COMMA) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:52:36: COMMA gammaExpression
					{
					match(input,COMMA,FOLLOW_COMMA_in_gammaExpressions198); 
					pushFollow(FOLLOW_gammaExpression_in_gammaExpressions200);
					gammaExpression();
					state._fsp--;

					}
					break;

				default :
					break loop3;
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
	// $ANTLR end "gammaExpressions"



	// $ANTLR start "gammaExpression"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:54:1: gammaExpression : WORD ;
	public final void gammaExpression() throws RecognitionException {
		Token WORD8=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:54:16: ( WORD )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:54:18: WORD
			{
			WORD8=(Token)match(input,WORD,FOLLOW_WORD_in_gammaExpression209); 
			gammaExpressions.add((WORD8!=null?WORD8.getText():null));
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
	// $ANTLR end "gammaExpression"



	// $ANTLR start "queryAlias"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:56:1: queryAlias : WORD ;
	public final void queryAlias() throws RecognitionException {
		Token WORD9=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:56:11: ( WORD )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:56:13: WORD
			{
			WORD9=(Token)match(input,WORD,FOLLOW_WORD_in_queryAlias217); 
			queryAlias=(WORD9!=null?WORD9.getText():null);
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
	// $ANTLR end "queryAlias"

	// Delegated rules



	public static final BitSet FOLLOW_parse_in_start82 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ANALYZE_in_parse89 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_aggrfunc_in_parse91 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_LPARENTHESIS_in_parse93 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_measure_in_parse95 = new BitSet(new long[]{0x0000001000000000L});
	public static final BitSet FOLLOW_RPARENTHESIS_in_parse97 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_FROM_in_parse99 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_cubeName_in_parse101 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_FOR_in_parse103 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_sigmaExpressions_in_parse105 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_GROUP_in_parse107 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_BY_in_parse109 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_gammaExpressions_in_parse111 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_AS_in_parse113 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_queryAlias_in_parse115 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AGGRFUNC_in_aggrfunc121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_measure129 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_cubeName137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressions145 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_AND_in_sigmaExpressions147 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressions149 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_sigmaExpressionNumberValue_in_sigmaExpression158 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sigmaExpressionTextValue_in_sigmaExpression160 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_sigmaExpressionNumberValue168 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_EQUAL_in_sigmaExpressionNumberValue170 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_NUMBER_in_sigmaExpressionNumberValue172 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_sigmaExpressionTextValue181 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_EQUAL_in_sigmaExpressionTextValue183 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_TEXTVALUE_in_sigmaExpressionTextValue185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_gammaExpression_in_gammaExpressions195 = new BitSet(new long[]{0x0000000000001002L});
	public static final BitSet FOLLOW_COMMA_in_gammaExpressions198 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_gammaExpression_in_gammaExpressions200 = new BitSet(new long[]{0x0000000000001002L});
	public static final BitSet FOLLOW_WORD_in_gammaExpression209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_queryAlias217 = new BitSet(new long[]{0x0000000000000002L});
}
