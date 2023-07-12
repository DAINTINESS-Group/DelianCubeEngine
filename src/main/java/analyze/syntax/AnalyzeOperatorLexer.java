// $ANTLR 3.5.1 C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g 2023-06-04 14:06:07

	package analyze.syntax;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class AnalyzeOperatorLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public AnalyzeOperatorLexer() {} 
	public AnalyzeOperatorLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public AnalyzeOperatorLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g"; }

	// $ANTLR start "ANALYZE"
	public final void mANALYZE() throws RecognitionException {
		try {
			int _type = ANALYZE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:59:8: ( A N A L Y Z E )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:59:10: A N A L Y Z E
			{
			mA(); 

			mN(); 

			mA(); 

			mL(); 

			mY(); 

			mZ(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ANALYZE"

	// $ANTLR start "AND"
	public final void mAND() throws RecognitionException {
		try {
			int _type = AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:61:4: ( A N D )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:61:5: A N D
			{
			mA(); 

			mN(); 

			mD(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AND"

	// $ANTLR start "AS"
	public final void mAS() throws RecognitionException {
		try {
			int _type = AS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:63:3: ( A S )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:63:5: A S
			{
			mA(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AS"

	// $ANTLR start "FROM"
	public final void mFROM() throws RecognitionException {
		try {
			int _type = FROM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:65:5: ( F R O M )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:65:7: F R O M
			{
			mF(); 

			mR(); 

			mO(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FROM"

	// $ANTLR start "FOR"
	public final void mFOR() throws RecognitionException {
		try {
			int _type = FOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:67:4: ( F O R )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:67:6: F O R
			{
			mF(); 

			mO(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FOR"

	// $ANTLR start "GROUP"
	public final void mGROUP() throws RecognitionException {
		try {
			int _type = GROUP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:69:6: ( G R O U P )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:69:7: G R O U P
			{
			mG(); 

			mR(); 

			mO(); 

			mU(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GROUP"

	// $ANTLR start "BY"
	public final void mBY() throws RecognitionException {
		try {
			int _type = BY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:71:3: ( B Y )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:71:5: B Y
			{
			mB(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BY"

	// $ANTLR start "AGGRFUNC"
	public final void mAGGRFUNC() throws RecognitionException {
		try {
			int _type = AGGRFUNC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:9: ( ( M I N | M A X | S U M | A V G | C N T ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:10: ( M I N | M A X | S U M | A V G | C N T )
			{
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:10: ( M I N | M A X | S U M | A V G | C N T )
			int alt1=5;
			switch ( input.LA(1) ) {
			case 'M':
			case 'm':
				{
				int LA1_1 = input.LA(2);
				if ( (LA1_1=='I'||LA1_1=='i') ) {
					alt1=1;
				}
				else if ( (LA1_1=='A'||LA1_1=='a') ) {
					alt1=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 1, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 'S':
			case 's':
				{
				alt1=3;
				}
				break;
			case 'A':
			case 'a':
				{
				alt1=4;
				}
				break;
			case 'C':
			case 'c':
				{
				alt1=5;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}
			switch (alt1) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:11: M I N
					{
					mM(); 

					mI(); 

					mN(); 

					}
					break;
				case 2 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:17: M A X
					{
					mM(); 

					mA(); 

					mX(); 

					}
					break;
				case 3 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:23: S U M
					{
					mS(); 

					mU(); 

					mM(); 

					}
					break;
				case 4 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:29: A V G
					{
					mA(); 

					mV(); 

					mG(); 

					}
					break;
				case 5 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:73:35: C N T
					{
					mC(); 

					mN(); 

					mT(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AGGRFUNC"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:75:6: ( ',' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:75:8: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "EQUAL"
	public final void mEQUAL() throws RecognitionException {
		try {
			int _type = EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:77:6: ( '=' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:77:8: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQUAL"

	// $ANTLR start "LPARENTHESIS"
	public final void mLPARENTHESIS() throws RecognitionException {
		try {
			int _type = LPARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:79:13: ( '(' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:79:15: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPARENTHESIS"

	// $ANTLR start "RPARENTHESIS"
	public final void mRPARENTHESIS() throws RecognitionException {
		try {
			int _type = RPARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:81:13: ( ')' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:81:15: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPARENTHESIS"

	// $ANTLR start "WORD"
	public final void mWORD() throws RecognitionException {
		try {
			int _type = WORD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:83:5: ( ( LETTER | '_' | DIGIT )+ )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:83:7: ( LETTER | '_' | DIGIT )+
			{
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:83:7: ( LETTER | '_' | DIGIT )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WORD"

	// $ANTLR start "TEXTVALUE"
	public final void mTEXTVALUE() throws RecognitionException {
		try {
			int _type = TEXTVALUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:85:10: ( '\\'' ( LETTER | '_' | '/' | '-' | ' ' | '.' )+ '\\'' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:85:12: '\\'' ( LETTER | '_' | '/' | '-' | ' ' | '.' )+ '\\''
			{
			match('\''); 
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:85:16: ( LETTER | '_' | '/' | '-' | ' ' | '.' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==' '||(LA3_0 >= '-' && LA3_0 <= '/')||(LA3_0 >= 'A' && LA3_0 <= 'Z')||LA3_0=='_'||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
					{
					if ( input.LA(1)==' '||(input.LA(1) >= '-' && input.LA(1) <= '/')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
			}

			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TEXTVALUE"

	// $ANTLR start "NUMBER"
	public final void mNUMBER() throws RecognitionException {
		try {
			int _type = NUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:7: ( '\\'' ( '-' )? ( DIGIT | '-' )+ ( '.' ( DIGIT | '-' )+ )? '\\'' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:9: '\\'' ( '-' )? ( DIGIT | '-' )+ ( '.' ( DIGIT | '-' )+ )? '\\''
			{
			match('\''); 
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:13: ( '-' )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0=='-') ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:13: '-'
					{
					match('-'); 
					}
					break;

			}

			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:17: ( DIGIT | '-' )+
			int cnt5=0;
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0=='-'||(LA5_0 >= '0' && LA5_0 <= '9')) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
					{
					if ( input.LA(1)=='-'||(input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt5 >= 1 ) break loop5;
					EarlyExitException eee = new EarlyExitException(5, input);
					throw eee;
				}
				cnt5++;
			}

			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:29: ( '.' ( DIGIT | '-' )+ )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0=='.') ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:30: '.' ( DIGIT | '-' )+
					{
					match('.'); 
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:87:33: ( DIGIT | '-' )+
					int cnt6=0;
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( (LA6_0=='-'||(LA6_0 >= '0' && LA6_0 <= '9')) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
							{
							if ( input.LA(1)=='-'||(input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
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

			}

			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMBER"

	// $ANTLR start "DIGIT"
	public final void mDIGIT() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:89:16: ( '0' .. '9' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIGIT"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:90:17: ( 'a' .. 'z' | 'A' .. 'Z' )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LETTER"

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:91:11: ( ( 'A' | 'a' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "A"

	// $ANTLR start "B"
	public final void mB() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:92:11: ( ( 'B' | 'b' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "B"

	// $ANTLR start "C"
	public final void mC() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:93:11: ( ( 'C' | 'c' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "C"

	// $ANTLR start "D"
	public final void mD() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:94:11: ( ( 'D' | 'd' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "D"

	// $ANTLR start "E"
	public final void mE() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:95:11: ( ( 'E' | 'e' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "E"

	// $ANTLR start "F"
	public final void mF() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:96:11: ( ( 'F' | 'f' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "F"

	// $ANTLR start "G"
	public final void mG() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:97:11: ( ( 'G' | 'g' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "G"

	// $ANTLR start "H"
	public final void mH() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:98:11: ( ( 'H' | 'h' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "H"

	// $ANTLR start "I"
	public final void mI() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:99:11: ( ( 'I' | 'i' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "I"

	// $ANTLR start "J"
	public final void mJ() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:100:11: ( ( 'J' | 'j' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "J"

	// $ANTLR start "K"
	public final void mK() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:101:11: ( ( 'K' | 'k' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K"

	// $ANTLR start "L"
	public final void mL() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:102:11: ( ( 'L' | 'l' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "L"

	// $ANTLR start "M"
	public final void mM() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:103:11: ( ( 'M' | 'm' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "M"

	// $ANTLR start "N"
	public final void mN() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:104:11: ( ( 'N' | 'n' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "N"

	// $ANTLR start "O"
	public final void mO() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:105:11: ( ( 'O' | 'o' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "O"

	// $ANTLR start "P"
	public final void mP() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:106:11: ( ( 'P' | 'p' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "P"

	// $ANTLR start "Q"
	public final void mQ() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:107:11: ( ( 'Q' | 'q' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Q"

	// $ANTLR start "R"
	public final void mR() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:108:11: ( ( 'R' | 'r' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "R"

	// $ANTLR start "S"
	public final void mS() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:109:11: ( ( 'S' | 's' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "S"

	// $ANTLR start "T"
	public final void mT() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:110:11: ( ( 'T' | 't' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T"

	// $ANTLR start "U"
	public final void mU() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:111:11: ( ( 'U' | 'u' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "U"

	// $ANTLR start "V"
	public final void mV() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:112:11: ( ( 'V' | 'v' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "V"

	// $ANTLR start "W"
	public final void mW() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:113:11: ( ( 'W' | 'w' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "W"

	// $ANTLR start "X"
	public final void mX() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:114:11: ( ( 'X' | 'x' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "X"

	// $ANTLR start "Y"
	public final void mY() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:115:11: ( ( 'Y' | 'y' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Y"

	// $ANTLR start "Z"
	public final void mZ() throws RecognitionException {
		try {
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:116:11: ( ( 'Z' | 'z' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:
			{
			if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Z"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:3: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\r\\n' | '\\f' ) )
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:5: ( ' ' | '\\t' | '\\r' | '\\n' | '\\r\\n' | '\\f' )
			{
			// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:5: ( ' ' | '\\t' | '\\r' | '\\n' | '\\r\\n' | '\\f' )
			int alt8=6;
			switch ( input.LA(1) ) {
			case ' ':
				{
				alt8=1;
				}
				break;
			case '\t':
				{
				alt8=2;
				}
				break;
			case '\r':
				{
				int LA8_3 = input.LA(2);
				if ( (LA8_3=='\n') ) {
					alt8=5;
				}

				else {
					alt8=3;
				}

				}
				break;
			case '\n':
				{
				alt8=4;
				}
				break;
			case '\f':
				{
				alt8=6;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 8, 0, input);
				throw nvae;
			}
			switch (alt8) {
				case 1 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:6: ' '
					{
					match(' '); 
					}
					break;
				case 2 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:12: '\\t'
					{
					match('\t'); 
					}
					break;
				case 3 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:19: '\\r'
					{
					match('\r'); 
					}
					break;
				case 4 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:25: '\\n'
					{
					match('\n'); 
					}
					break;
				case 5 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:30: '\\r\\n'
					{
					match("\r\n"); 

					}
					break;
				case 6 :
					// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:118:37: '\\f'
					{
					match('\f'); 
					}
					break;

			}

			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:8: ( ANALYZE | AND | AS | FROM | FOR | GROUP | BY | AGGRFUNC | COMMA | EQUAL | LPARENTHESIS | RPARENTHESIS | WORD | TEXTVALUE | NUMBER | WS )
		int alt9=16;
		alt9 = dfa9.predict(input);
		switch (alt9) {
			case 1 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:10: ANALYZE
				{
				mANALYZE(); 

				}
				break;
			case 2 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:18: AND
				{
				mAND(); 

				}
				break;
			case 3 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:22: AS
				{
				mAS(); 

				}
				break;
			case 4 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:25: FROM
				{
				mFROM(); 

				}
				break;
			case 5 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:30: FOR
				{
				mFOR(); 

				}
				break;
			case 6 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:34: GROUP
				{
				mGROUP(); 

				}
				break;
			case 7 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:40: BY
				{
				mBY(); 

				}
				break;
			case 8 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:43: AGGRFUNC
				{
				mAGGRFUNC(); 

				}
				break;
			case 9 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:52: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 10 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:58: EQUAL
				{
				mEQUAL(); 

				}
				break;
			case 11 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:64: LPARENTHESIS
				{
				mLPARENTHESIS(); 

				}
				break;
			case 12 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:77: RPARENTHESIS
				{
				mRPARENTHESIS(); 

				}
				break;
			case 13 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:90: WORD
				{
				mWORD(); 

				}
				break;
			case 14 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:95: TEXTVALUE
				{
				mTEXTVALUE(); 

				}
				break;
			case 15 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:105: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 16 :
				// C:\\Users\\mariosjkb\\eclipse-workspace\\DelianCubeEngine\\src\\main\\java\\analyze\\syntax\\AnalyzeOperator.g:1:112: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA9 dfa9 = new DFA9(this);
	static final String DFA9_eotS =
		"\1\uffff\7\14\7\uffff\1\14\1\37\4\14\1\44\4\14\3\uffff\1\14\1\55\1\uffff"+
		"\1\56\1\14\1\60\1\14\1\uffff\4\56\3\uffff\1\14\2\uffff\1\64\1\uffff\1"+
		"\14\1\uffff\1\14\1\uffff\1\67\1\14\1\uffff\1\71\1\uffff";
	static final String DFA9_eofS =
		"\72\uffff";
	static final String DFA9_minS =
		"\1\11\1\116\1\117\1\122\1\131\1\101\1\125\1\116\5\uffff\1\40\1\uffff\1"+
		"\101\1\60\1\107\1\117\1\122\1\117\1\60\1\116\1\130\1\115\1\124\1\40\2"+
		"\uffff\1\114\1\60\1\uffff\1\60\1\115\1\60\1\125\1\uffff\4\60\1\uffff\2"+
		"\40\1\131\2\uffff\1\60\1\uffff\1\120\1\40\1\132\1\uffff\1\60\1\105\1\uffff"+
		"\1\60\1\uffff";
	static final String DFA9_maxS =
		"\1\172\1\166\2\162\1\171\1\151\1\165\1\156\5\uffff\1\172\1\uffff\1\144"+
		"\1\172\1\147\1\157\1\162\1\157\1\172\1\156\1\170\1\155\1\164\1\172\2\uffff"+
		"\1\154\1\172\1\uffff\1\172\1\155\1\172\1\165\1\uffff\4\172\1\uffff\2\172"+
		"\1\171\2\uffff\1\172\1\uffff\1\160\2\172\1\uffff\1\172\1\145\1\uffff\1"+
		"\172\1\uffff";
	static final String DFA9_acceptS =
		"\10\uffff\1\11\1\12\1\13\1\14\1\15\1\uffff\1\20\14\uffff\1\16\1\17\2\uffff"+
		"\1\3\4\uffff\1\7\4\uffff\1\16\3\uffff\1\2\1\10\1\uffff\1\5\3\uffff\1\4"+
		"\2\uffff\1\6\1\uffff\1\1";
	static final String DFA9_specialS =
		"\72\uffff}>";
	static final String[] DFA9_transitionS = {
			"\2\16\1\uffff\2\16\22\uffff\1\16\6\uffff\1\15\1\12\1\13\2\uffff\1\10"+
			"\3\uffff\12\14\3\uffff\1\11\3\uffff\1\1\1\4\1\7\2\14\1\2\1\3\5\14\1\5"+
			"\5\14\1\6\7\14\4\uffff\1\14\1\uffff\1\1\1\4\1\7\2\14\1\2\1\3\5\14\1\5"+
			"\5\14\1\6\7\14",
			"\1\17\4\uffff\1\20\2\uffff\1\21\27\uffff\1\17\4\uffff\1\20\2\uffff\1"+
			"\21",
			"\1\23\2\uffff\1\22\34\uffff\1\23\2\uffff\1\22",
			"\1\24\37\uffff\1\24",
			"\1\25\37\uffff\1\25",
			"\1\27\7\uffff\1\26\27\uffff\1\27\7\uffff\1\26",
			"\1\30\37\uffff\1\30",
			"\1\31\37\uffff\1\31",
			"",
			"",
			"",
			"",
			"",
			"\1\33\14\uffff\1\32\2\33\12\34\7\uffff\32\33\4\uffff\1\33\1\uffff\32"+
			"\33",
			"",
			"\1\35\2\uffff\1\36\34\uffff\1\35\2\uffff\1\36",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\1\40\37\uffff\1\40",
			"\1\41\37\uffff\1\41",
			"\1\42\37\uffff\1\42",
			"\1\43\37\uffff\1\43",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\1\45\37\uffff\1\45",
			"\1\46\37\uffff\1\46",
			"\1\47\37\uffff\1\47",
			"\1\50\37\uffff\1\50",
			"\1\33\6\uffff\1\51\5\uffff\1\52\1\53\1\33\12\34\7\uffff\32\33\4\uffff"+
			"\1\33\1\uffff\32\33",
			"",
			"",
			"\1\54\37\uffff\1\54",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\1\57\37\uffff\1\57",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\1\61\37\uffff\1\61",
			"",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"",
			"\1\33\6\uffff\1\51\5\uffff\1\52\1\53\1\33\12\34\7\uffff\32\33\4\uffff"+
			"\1\33\1\uffff\32\33",
			"\1\33\6\uffff\1\33\5\uffff\1\62\2\33\12\34\7\uffff\32\33\4\uffff\1\33"+
			"\1\uffff\32\33",
			"\1\63\37\uffff\1\63",
			"",
			"",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"",
			"\1\65\37\uffff\1\65",
			"\1\33\6\uffff\1\51\5\uffff\1\62\2\33\12\34\7\uffff\32\33\4\uffff\1\33"+
			"\1\uffff\32\33",
			"\1\66\37\uffff\1\66",
			"",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			"\1\70\37\uffff\1\70",
			"",
			"\12\14\7\uffff\32\14\4\uffff\1\14\1\uffff\32\14",
			""
	};

	static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
	static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
	static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
	static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
	static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
	static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
	static final short[][] DFA9_transition;

	static {
		int numStates = DFA9_transitionS.length;
		DFA9_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
		}
	}

	protected class DFA9 extends DFA {

		public DFA9(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 9;
			this.eot = DFA9_eot;
			this.eof = DFA9_eof;
			this.min = DFA9_min;
			this.max = DFA9_max;
			this.accept = DFA9_accept;
			this.special = DFA9_special;
			this.transition = DFA9_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( ANALYZE | AND | AS | FROM | FOR | GROUP | BY | AGGRFUNC | COMMA | EQUAL | LPARENTHESIS | RPARENTHESIS | WORD | TEXTVALUE | NUMBER | WS );";
		}
	}

}
