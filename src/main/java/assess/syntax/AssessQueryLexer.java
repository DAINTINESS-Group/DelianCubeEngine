// $ANTLR 3.5.2 AssessQuery.g 2023-02-19 20:56:17

package assess.syntax;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class AssessQueryLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public AssessQueryLexer() {} 
	public AssessQueryLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public AssessQueryLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "AssessQuery.g"; }

	// $ANTLR start "T__34"
	public final void mT__34() throws RecognitionException {
		try {
			int _type = T__34;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:11:7: ( '(' )
			// AssessQuery.g:11:9: '('
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
	// $ANTLR end "T__34"

	// $ANTLR start "T__35"
	public final void mT__35() throws RecognitionException {
		try {
			int _type = T__35;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:12:7: ( ')' )
			// AssessQuery.g:12:9: ')'
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
	// $ANTLR end "T__35"

	// $ANTLR start "T__36"
	public final void mT__36() throws RecognitionException {
		try {
			int _type = T__36;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:13:7: ( ',' )
			// AssessQuery.g:13:9: ','
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
	// $ANTLR end "T__36"

	// $ANTLR start "T__37"
	public final void mT__37() throws RecognitionException {
		try {
			int _type = T__37;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:14:7: ( '.' )
			// AssessQuery.g:14:9: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__37"

	// $ANTLR start "T__38"
	public final void mT__38() throws RecognitionException {
		try {
			int _type = T__38;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:15:7: ( '/' )
			// AssessQuery.g:15:9: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__38"

	// $ANTLR start "T__39"
	public final void mT__39() throws RecognitionException {
		try {
			int _type = T__39;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:16:7: ( ':' )
			// AssessQuery.g:16:9: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__39"

	// $ANTLR start "T__40"
	public final void mT__40() throws RecognitionException {
		try {
			int _type = T__40;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:17:7: ( '=' )
			// AssessQuery.g:17:9: '='
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
	// $ANTLR end "T__40"

	// $ANTLR start "T__41"
	public final void mT__41() throws RecognitionException {
		try {
			int _type = T__41;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:18:7: ( '[' )
			// AssessQuery.g:18:9: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__41"

	// $ANTLR start "T__42"
	public final void mT__42() throws RecognitionException {
		try {
			int _type = T__42;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:19:7: ( '\\'' )
			// AssessQuery.g:19:9: '\\''
			{
			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__42"

	// $ANTLR start "T__43"
	public final void mT__43() throws RecognitionException {
		try {
			int _type = T__43;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:20:7: ( ']' )
			// AssessQuery.g:20:9: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__43"

	// $ANTLR start "T__44"
	public final void mT__44() throws RecognitionException {
		try {
			int _type = T__44;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:21:7: ( 'benchmark.' )
			// AssessQuery.g:21:9: 'benchmark.'
			{
			match("benchmark."); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__44"

	// $ANTLR start "T__45"
	public final void mT__45() throws RecognitionException {
		try {
			int _type = T__45;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:22:7: ( 'inf' )
			// AssessQuery.g:22:9: 'inf'
			{
			match("inf"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__45"

	// $ANTLR start "T__46"
	public final void mT__46() throws RecognitionException {
		try {
			int _type = T__46;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:23:7: ( '{' )
			// AssessQuery.g:23:9: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__46"

	// $ANTLR start "T__47"
	public final void mT__47() throws RecognitionException {
		try {
			int _type = T__47;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:24:7: ( '}' )
			// AssessQuery.g:24:9: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__47"

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// AssessQuery.g:127:12: ( ( 'A' | 'a' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:128:12: ( ( 'B' | 'b' ) )
			// AssessQuery.g:
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

	// $ANTLR start "E"
	public final void mE() throws RecognitionException {
		try {
			// AssessQuery.g:129:12: ( ( 'E' | 'e' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:130:12: ( ( 'F' | 'f' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:131:12: ( ( 'G' | 'g' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:132:12: ( ( 'H' | 'h' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:133:12: ( ( 'I' | 'i' ) )
			// AssessQuery.g:
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

	// $ANTLR start "L"
	public final void mL() throws RecognitionException {
		try {
			// AssessQuery.g:134:12: ( ( 'L' | 'l' ) )
			// AssessQuery.g:
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

	// $ANTLR start "N"
	public final void mN() throws RecognitionException {
		try {
			// AssessQuery.g:135:12: ( ( 'N' | 'n' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:136:12: ( ( 'O' | 'o' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:137:12: ( ( 'P' | 'p' ) )
			// AssessQuery.g:
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

	// $ANTLR start "R"
	public final void mR() throws RecognitionException {
		try {
			// AssessQuery.g:138:12: ( ( 'R' | 'r' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:139:12: ( ( 'S' | 's' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:140:12: ( ( 'T' | 't' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:141:12: ( ( 'U' | 'u' ) )
			// AssessQuery.g:
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

	// $ANTLR start "W"
	public final void mW() throws RecognitionException {
		try {
			// AssessQuery.g:142:12: ( ( 'W' | 'w' ) )
			// AssessQuery.g:
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

	// $ANTLR start "Y"
	public final void mY() throws RecognitionException {
		try {
			// AssessQuery.g:143:12: ( ( 'Y' | 'y' ) )
			// AssessQuery.g:
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

	// $ANTLR start "AGAINST"
	public final void mAGAINST() throws RecognitionException {
		try {
			int _type = AGAINST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:146:9: ( A G A I N S T )
			// AssessQuery.g:146:11: A G A I N S T
			{
			mA(); 

			mG(); 

			mA(); 

			mI(); 

			mN(); 

			mS(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AGAINST"

	// $ANTLR start "ASSESS"
	public final void mASSESS() throws RecognitionException {
		try {
			int _type = ASSESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:147:8: ( A S S E S S )
			// AssessQuery.g:147:10: A S S E S S
			{
			mA(); 

			mS(); 

			mS(); 

			mE(); 

			mS(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASSESS"

	// $ANTLR start "BY"
	public final void mBY() throws RecognitionException {
		try {
			int _type = BY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:148:4: ( B Y )
			// AssessQuery.g:148:6: B Y
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

	// $ANTLR start "FOR"
	public final void mFOR() throws RecognitionException {
		try {
			int _type = FOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:149:5: ( F O R )
			// AssessQuery.g:149:7: F O R
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

	// $ANTLR start "LABELS"
	public final void mLABELS() throws RecognitionException {
		try {
			int _type = LABELS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:150:8: ( L A B E L S )
			// AssessQuery.g:150:10: L A B E L S
			{
			mL(); 

			mA(); 

			mB(); 

			mE(); 

			mL(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LABELS"

	// $ANTLR start "PAST"
	public final void mPAST() throws RecognitionException {
		try {
			int _type = PAST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:151:6: ( P A S T )
			// AssessQuery.g:151:8: P A S T
			{
			mP(); 

			mA(); 

			mS(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PAST"

	// $ANTLR start "USING"
	public final void mUSING() throws RecognitionException {
		try {
			int _type = USING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:152:7: ( U S I N G )
			// AssessQuery.g:152:9: U S I N G
			{
			mU(); 

			mS(); 

			mI(); 

			mN(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "USING"

	// $ANTLR start "WITH"
	public final void mWITH() throws RecognitionException {
		try {
			int _type = WITH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:153:6: ( W I T H )
			// AssessQuery.g:153:8: W I T H
			{
			mW(); 

			mI(); 

			mT(); 

			mH(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WITH"

	// $ANTLR start "SIGN"
	public final void mSIGN() throws RecognitionException {
		try {
			int _type = SIGN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:156:6: ( ( '+' | '-' ) )
			// AssessQuery.g:
			{
			if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SIGN"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:157:4: ( ( 'a' .. 'z' | 'A' .. 'Z' )+ )
			// AssessQuery.g:157:6: ( 'a' .. 'z' | 'A' .. 'Z' )+
			{
			// AssessQuery.g:157:6: ( 'a' .. 'z' | 'A' .. 'Z' )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= 'A' && LA1_0 <= 'Z')||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// AssessQuery.g:
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
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:159:5: ( ( '0' .. '9' )+ )
			// AssessQuery.g:159:7: ( '0' .. '9' )+
			{
			// AssessQuery.g:159:7: ( '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// AssessQuery.g:
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
	// $ANTLR end "INT"

	// $ANTLR start "FLOAT"
	public final void mFLOAT() throws RecognitionException {
		try {
			int _type = FLOAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:160:7: ( INT '.' INT )
			// AssessQuery.g:160:9: INT '.' INT
			{
			mINT(); 

			match('.'); 
			mINT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FLOAT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:162:4: ( ( ' ' | '\\t' | '\\n' | '\\r' ) )
			// AssessQuery.g:162:6: ( ' ' | '\\t' | '\\n' | '\\r' )
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
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
		// AssessQuery.g:1:8: ( T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | AGAINST | ASSESS | BY | FOR | LABELS | PAST | USING | WITH | SIGN | ID | INT | FLOAT | WS )
		int alt3=27;
		alt3 = dfa3.predict(input);
		switch (alt3) {
			case 1 :
				// AssessQuery.g:1:10: T__34
				{
				mT__34(); 

				}
				break;
			case 2 :
				// AssessQuery.g:1:16: T__35
				{
				mT__35(); 

				}
				break;
			case 3 :
				// AssessQuery.g:1:22: T__36
				{
				mT__36(); 

				}
				break;
			case 4 :
				// AssessQuery.g:1:28: T__37
				{
				mT__37(); 

				}
				break;
			case 5 :
				// AssessQuery.g:1:34: T__38
				{
				mT__38(); 

				}
				break;
			case 6 :
				// AssessQuery.g:1:40: T__39
				{
				mT__39(); 

				}
				break;
			case 7 :
				// AssessQuery.g:1:46: T__40
				{
				mT__40(); 

				}
				break;
			case 8 :
				// AssessQuery.g:1:52: T__41
				{
				mT__41(); 

				}
				break;
			case 9 :
				// AssessQuery.g:1:58: T__42
				{
				mT__42(); 

				}
				break;
			case 10 :
				// AssessQuery.g:1:64: T__43
				{
				mT__43(); 

				}
				break;
			case 11 :
				// AssessQuery.g:1:70: T__44
				{
				mT__44(); 

				}
				break;
			case 12 :
				// AssessQuery.g:1:76: T__45
				{
				mT__45(); 

				}
				break;
			case 13 :
				// AssessQuery.g:1:82: T__46
				{
				mT__46(); 

				}
				break;
			case 14 :
				// AssessQuery.g:1:88: T__47
				{
				mT__47(); 

				}
				break;
			case 15 :
				// AssessQuery.g:1:94: AGAINST
				{
				mAGAINST(); 

				}
				break;
			case 16 :
				// AssessQuery.g:1:102: ASSESS
				{
				mASSESS(); 

				}
				break;
			case 17 :
				// AssessQuery.g:1:109: BY
				{
				mBY(); 

				}
				break;
			case 18 :
				// AssessQuery.g:1:112: FOR
				{
				mFOR(); 

				}
				break;
			case 19 :
				// AssessQuery.g:1:116: LABELS
				{
				mLABELS(); 

				}
				break;
			case 20 :
				// AssessQuery.g:1:123: PAST
				{
				mPAST(); 

				}
				break;
			case 21 :
				// AssessQuery.g:1:128: USING
				{
				mUSING(); 

				}
				break;
			case 22 :
				// AssessQuery.g:1:134: WITH
				{
				mWITH(); 

				}
				break;
			case 23 :
				// AssessQuery.g:1:139: SIGN
				{
				mSIGN(); 

				}
				break;
			case 24 :
				// AssessQuery.g:1:144: ID
				{
				mID(); 

				}
				break;
			case 25 :
				// AssessQuery.g:1:147: INT
				{
				mINT(); 

				}
				break;
			case 26 :
				// AssessQuery.g:1:151: FLOAT
				{
				mFLOAT(); 

				}
				break;
			case 27 :
				// AssessQuery.g:1:157: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA3 dfa3 = new DFA3(this);
	static final String DFA3_eotS =
		"\13\uffff\2\27\2\uffff\7\27\2\uffff\1\44\1\uffff\1\27\1\47\10\27\2\uffff"+
		"\1\27\1\uffff\1\61\2\27\1\64\5\27\1\uffff\2\27\1\uffff\1\27\1\75\1\27"+
		"\1\77\4\27\1\uffff\1\104\1\uffff\2\27\1\107\1\110\1\uffff\1\27\1\112\2"+
		"\uffff\1\27\1\uffff\1\27\1\uffff";
	static final String DFA3_eofS =
		"\115\uffff";
	static final String DFA3_minS =
		"\1\11\12\uffff\1\131\1\156\2\uffff\1\107\1\131\1\117\2\101\1\123\1\111"+
		"\2\uffff\1\56\1\uffff\1\156\1\101\1\146\1\101\1\123\1\122\1\102\1\123"+
		"\1\111\1\124\2\uffff\1\143\1\uffff\1\101\1\111\1\105\1\101\1\105\1\124"+
		"\1\116\1\110\1\150\1\uffff\1\116\1\123\1\uffff\1\114\1\101\1\107\1\101"+
		"\1\155\3\123\1\uffff\1\101\1\uffff\1\141\1\124\2\101\1\uffff\1\162\1\101"+
		"\2\uffff\1\153\1\uffff\1\56\1\uffff";
	static final String DFA3_maxS =
		"\1\175\12\uffff\1\171\1\156\2\uffff\1\163\1\171\1\157\2\141\1\163\1\151"+
		"\2\uffff\1\71\1\uffff\1\156\1\172\1\146\1\141\1\163\1\162\1\142\1\163"+
		"\1\151\1\164\2\uffff\1\143\1\uffff\1\172\1\151\1\145\1\172\1\145\1\164"+
		"\1\156\2\150\1\uffff\1\156\1\163\1\uffff\1\154\1\172\1\147\1\172\1\155"+
		"\3\163\1\uffff\1\172\1\uffff\1\141\1\164\2\172\1\uffff\1\162\1\172\2\uffff"+
		"\1\153\1\uffff\1\56\1\uffff";
	static final String DFA3_acceptS =
		"\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\2\uffff\1\15\1\16"+
		"\7\uffff\1\27\1\30\1\uffff\1\33\12\uffff\1\31\1\32\1\uffff\1\21\11\uffff"+
		"\1\14\2\uffff\1\22\10\uffff\1\24\1\uffff\1\26\4\uffff\1\25\2\uffff\1\20"+
		"\1\23\1\uffff\1\17\1\uffff\1\13";
	static final String DFA3_specialS =
		"\115\uffff}>";
	static final String[] DFA3_transitionS = {
			"\2\31\2\uffff\1\31\22\uffff\1\31\6\uffff\1\11\1\1\1\2\1\uffff\1\26\1"+
			"\3\1\26\1\4\1\5\12\30\1\6\2\uffff\1\7\3\uffff\1\17\1\20\3\27\1\21\5\27"+
			"\1\22\3\27\1\23\4\27\1\24\1\27\1\25\3\27\1\10\1\uffff\1\12\3\uffff\1"+
			"\17\1\13\3\27\1\21\2\27\1\14\2\27\1\22\3\27\1\23\4\27\1\24\1\27\1\25"+
			"\3\27\1\15\1\uffff\1\16",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\33\13\uffff\1\32\23\uffff\1\33",
			"\1\34",
			"",
			"",
			"\1\35\13\uffff\1\36\23\uffff\1\35\13\uffff\1\36",
			"\1\33\37\uffff\1\33",
			"\1\37\37\uffff\1\37",
			"\1\40\37\uffff\1\40",
			"\1\41\37\uffff\1\41",
			"\1\42\37\uffff\1\42",
			"\1\43\37\uffff\1\43",
			"",
			"",
			"\1\45\1\uffff\12\30",
			"",
			"\1\46",
			"\32\27\6\uffff\32\27",
			"\1\50",
			"\1\51\37\uffff\1\51",
			"\1\52\37\uffff\1\52",
			"\1\53\37\uffff\1\53",
			"\1\54\37\uffff\1\54",
			"\1\55\37\uffff\1\55",
			"\1\56\37\uffff\1\56",
			"\1\57\37\uffff\1\57",
			"",
			"",
			"\1\60",
			"",
			"\32\27\6\uffff\32\27",
			"\1\62\37\uffff\1\62",
			"\1\63\37\uffff\1\63",
			"\32\27\6\uffff\32\27",
			"\1\65\37\uffff\1\65",
			"\1\66\37\uffff\1\66",
			"\1\67\37\uffff\1\67",
			"\1\70\37\uffff\1\70",
			"\1\71",
			"",
			"\1\72\37\uffff\1\72",
			"\1\73\37\uffff\1\73",
			"",
			"\1\74\37\uffff\1\74",
			"\32\27\6\uffff\32\27",
			"\1\76\37\uffff\1\76",
			"\32\27\6\uffff\32\27",
			"\1\100",
			"\1\101\37\uffff\1\101",
			"\1\102\37\uffff\1\102",
			"\1\103\37\uffff\1\103",
			"",
			"\32\27\6\uffff\32\27",
			"",
			"\1\105",
			"\1\106\37\uffff\1\106",
			"\32\27\6\uffff\32\27",
			"\32\27\6\uffff\32\27",
			"",
			"\1\111",
			"\32\27\6\uffff\32\27",
			"",
			"",
			"\1\113",
			"",
			"\1\114",
			""
	};

	static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
	static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
	static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
	static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
	static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
	static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
	static final short[][] DFA3_transition;

	static {
		int numStates = DFA3_transitionS.length;
		DFA3_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
		}
	}

	protected class DFA3 extends DFA {

		public DFA3(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 3;
			this.eot = DFA3_eot;
			this.eof = DFA3_eof;
			this.min = DFA3_min;
			this.max = DFA3_max;
			this.accept = DFA3_accept;
			this.special = DFA3_special;
			this.transition = DFA3_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | AGAINST | ASSESS | BY | FOR | LABELS | PAST | USING | WITH | SIGN | ID | INT | FLOAT | WS );";
		}
	}

}
