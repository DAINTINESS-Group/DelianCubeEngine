// $ANTLR 3.5.1 C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g 2023-01-27 13:24:40

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
		"B", "BY", "C", "COMMA", "D", "DIGIT", "E", "ENDANALYZE", "EQUAL", "F", 
		"FOR", "FROM", "G", "GROUP", "H", "I", "J", "K", "L", "LETTER", "M", "N", 
		"NUMBER", "O", "P", "Q", "R", "S", "T", "TEXTVALUE", "U", "V", "W", "WORD", 
		"WS", "X", "Y", "Z"
	};
	public static final int EOF=-1;
	public static final int A=4;
	public static final int AGGRFUNC=5;
	public static final int ANALYZE=6;
	public static final int AND=7;
	public static final int B=8;
	public static final int BY=9;
	public static final int C=10;
	public static final int COMMA=11;
	public static final int D=12;
	public static final int DIGIT=13;
	public static final int E=14;
	public static final int ENDANALYZE=15;
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
	public static final int M=28;
	public static final int N=29;
	public static final int NUMBER=30;
	public static final int O=31;
	public static final int P=32;
	public static final int Q=33;
	public static final int R=34;
	public static final int S=35;
	public static final int T=36;
	public static final int TEXTVALUE=37;
	public static final int U=38;
	public static final int V=39;
	public static final int W=40;
	public static final int WORD=41;
	public static final int WS=42;
	public static final int X=43;
	public static final int Y=44;
	public static final int Z=45;

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



	// $ANTLR start "start"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:29:1: start : parse ;
	public final void start() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:29:6: ( parse )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:29:7: parse
			{

				sigmaExpressions = new ArrayList<String>();
				sigmaExpressionsValues = new HashMap<String,String>();
				gammaExpressions = new ArrayList<String>();

			pushFollow(FOLLOW_parse_in_start78);
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:35:1: parse : ANALYZE aggrfunc measure FROM cubeName FOR sigmaExpressions GROUP BY gammaExpressions ENDANALYZE ;
	public final void parse() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:35:6: ( ANALYZE aggrfunc measure FROM cubeName FOR sigmaExpressions GROUP BY gammaExpressions ENDANALYZE )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:35:8: ANALYZE aggrfunc measure FROM cubeName FOR sigmaExpressions GROUP BY gammaExpressions ENDANALYZE
			{
			match(input,ANALYZE,FOLLOW_ANALYZE_in_parse85); 
			pushFollow(FOLLOW_aggrfunc_in_parse87);
			aggrfunc();
			state._fsp--;

			pushFollow(FOLLOW_measure_in_parse89);
			measure();
			state._fsp--;

			match(input,FROM,FOLLOW_FROM_in_parse91); 
			pushFollow(FOLLOW_cubeName_in_parse93);
			cubeName();
			state._fsp--;

			match(input,FOR,FOLLOW_FOR_in_parse95); 
			pushFollow(FOLLOW_sigmaExpressions_in_parse97);
			sigmaExpressions();
			state._fsp--;

			match(input,GROUP,FOLLOW_GROUP_in_parse99); 
			match(input,BY,FOLLOW_BY_in_parse101); 
			pushFollow(FOLLOW_gammaExpressions_in_parse103);
			gammaExpressions();
			state._fsp--;

			match(input,ENDANALYZE,FOLLOW_ENDANALYZE_in_parse105); 
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:37:1: aggrfunc : AGGRFUNC ;
	public final void aggrfunc() throws RecognitionException {
		Token AGGRFUNC1=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:37:9: ( AGGRFUNC )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:37:10: AGGRFUNC
			{
			AGGRFUNC1=(Token)match(input,AGGRFUNC,FOLLOW_AGGRFUNC_in_aggrfunc111); 
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:39:1: measure : WORD ;
	public final void measure() throws RecognitionException {
		Token WORD2=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:39:8: ( WORD )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:39:10: WORD
			{
			WORD2=(Token)match(input,WORD,FOLLOW_WORD_in_measure119); 
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:41:1: cubeName : WORD ;
	public final void cubeName() throws RecognitionException {
		Token WORD3=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:41:9: ( WORD )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:41:11: WORD
			{
			WORD3=(Token)match(input,WORD,FOLLOW_WORD_in_cubeName127); 
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:43:1: sigmaExpressions : sigmaExpression ( AND sigmaExpression )* ;
	public final void sigmaExpressions() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:43:17: ( sigmaExpression ( AND sigmaExpression )* )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:43:19: sigmaExpression ( AND sigmaExpression )*
			{
			pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressions135);
			sigmaExpression();
			state._fsp--;

			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:43:34: ( AND sigmaExpression )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==AND) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:43:35: AND sigmaExpression
					{
					match(input,AND,FOLLOW_AND_in_sigmaExpressions137); 
					pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressions139);
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:45:1: sigmaExpression : ( sigmaExpressionNumberValue | sigmaExpressionTextValue );
	public final void sigmaExpression() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:45:16: ( sigmaExpressionNumberValue | sigmaExpressionTextValue )
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
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:45:18: sigmaExpressionNumberValue
					{
					pushFollow(FOLLOW_sigmaExpressionNumberValue_in_sigmaExpression148);
					sigmaExpressionNumberValue();
					state._fsp--;

					}
					break;
				case 2 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:45:45: sigmaExpressionTextValue
					{
					pushFollow(FOLLOW_sigmaExpressionTextValue_in_sigmaExpression150);
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:47:1: sigmaExpressionNumberValue : WORD EQUAL NUMBER ;
	public final void sigmaExpressionNumberValue() throws RecognitionException {
		Token WORD4=null;
		Token NUMBER5=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:47:27: ( WORD EQUAL NUMBER )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:47:29: WORD EQUAL NUMBER
			{
			WORD4=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpressionNumberValue158); 
			match(input,EQUAL,FOLLOW_EQUAL_in_sigmaExpressionNumberValue160); 
			NUMBER5=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_sigmaExpressionNumberValue162); 
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:49:1: sigmaExpressionTextValue : WORD EQUAL TEXTVALUE ;
	public final void sigmaExpressionTextValue() throws RecognitionException {
		Token WORD6=null;
		Token TEXTVALUE7=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:49:25: ( WORD EQUAL TEXTVALUE )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:49:27: WORD EQUAL TEXTVALUE
			{
			WORD6=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpressionTextValue171); 
			match(input,EQUAL,FOLLOW_EQUAL_in_sigmaExpressionTextValue173); 
			TEXTVALUE7=(Token)match(input,TEXTVALUE,FOLLOW_TEXTVALUE_in_sigmaExpressionTextValue175); 
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
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:51:1: gammaExpressions : gammaExpression COMMA gammaExpression ;
	public final void gammaExpressions() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:51:17: ( gammaExpression COMMA gammaExpression )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:51:19: gammaExpression COMMA gammaExpression
			{
			pushFollow(FOLLOW_gammaExpression_in_gammaExpressions185);
			gammaExpression();
			state._fsp--;

			match(input,COMMA,FOLLOW_COMMA_in_gammaExpressions187); 
			pushFollow(FOLLOW_gammaExpression_in_gammaExpressions189);
			gammaExpression();
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
	// $ANTLR end "gammaExpressions"



	// $ANTLR start "gammaExpression"
	// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:53:1: gammaExpression : WORD ;
	public final void gammaExpression() throws RecognitionException {
		Token WORD8=null;

		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:53:16: ( WORD )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:53:18: WORD
			{
			WORD8=(Token)match(input,WORD,FOLLOW_WORD_in_gammaExpression196); 
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

	// Delegated rules



	public static final BitSet FOLLOW_parse_in_start78 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ANALYZE_in_parse85 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_aggrfunc_in_parse87 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_measure_in_parse89 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_FROM_in_parse91 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_cubeName_in_parse93 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_FOR_in_parse95 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_sigmaExpressions_in_parse97 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_GROUP_in_parse99 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_BY_in_parse101 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_gammaExpressions_in_parse103 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_ENDANALYZE_in_parse105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AGGRFUNC_in_aggrfunc111 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_measure119 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_cubeName127 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressions135 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_AND_in_sigmaExpressions137 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressions139 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_sigmaExpressionNumberValue_in_sigmaExpression148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sigmaExpressionTextValue_in_sigmaExpression150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_sigmaExpressionNumberValue158 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_EQUAL_in_sigmaExpressionNumberValue160 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_NUMBER_in_sigmaExpressionNumberValue162 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_sigmaExpressionTextValue171 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_EQUAL_in_sigmaExpressionTextValue173 = new BitSet(new long[]{0x0000002000000000L});
	public static final BitSet FOLLOW_TEXTVALUE_in_sigmaExpressionTextValue175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_gammaExpression_in_gammaExpressions185 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_COMMA_in_gammaExpressions187 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_gammaExpression_in_gammaExpressions189 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_gammaExpression196 = new BitSet(new long[]{0x0000000000000002L});
}
