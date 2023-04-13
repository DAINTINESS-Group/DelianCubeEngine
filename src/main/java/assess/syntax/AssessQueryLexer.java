// $ANTLR 3.5.2 AssessQuery.g 2023-04-13 10:33:14

package assess.syntax;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class AssessQueryLexer extends Lexer {
	public static final int EOF=-1;
	public static final int T__44=44;
	public static final int T__45=45;
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
	public static final int A=4;
	public static final int AGAINST=5;
	public static final int AGGREGATE=6;
	public static final int ASSESS=7;
	public static final int B=8;
	public static final int BY=9;
	public static final int C=10;
	public static final int D=11;
	public static final int E=12;
	public static final int F=13;
	public static final int FLOAT=14;
	public static final int FOR=15;
	public static final int G=16;
	public static final int H=17;
	public static final int I=18;
	public static final int ID=19;
	public static final int INT=20;
	public static final int J=21;
	public static final int K=22;
	public static final int L=23;
	public static final int LABELS=24;
	public static final int M=25;
	public static final int N=26;
	public static final int O=27;
	public static final int P=28;
	public static final int PAST=29;
	public static final int Q=30;
	public static final int R=31;
	public static final int S=32;
	public static final int SIGN=33;
	public static final int T=34;
	public static final int U=35;
	public static final int USING=36;
	public static final int V=37;
	public static final int W=38;
	public static final int WITH=39;
	public static final int WS=40;
	public static final int X=41;
	public static final int Y=42;
	public static final int Z=43;

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

	// $ANTLR start "T__44"
	public final void mT__44() throws RecognitionException {
		try {
			int _type = T__44;
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
	// $ANTLR end "T__44"

	// $ANTLR start "T__45"
	public final void mT__45() throws RecognitionException {
		try {
			int _type = T__45;
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
	// $ANTLR end "T__45"

	// $ANTLR start "T__46"
	public final void mT__46() throws RecognitionException {
		try {
			int _type = T__46;
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
	// $ANTLR end "T__46"

	// $ANTLR start "T__47"
	public final void mT__47() throws RecognitionException {
		try {
			int _type = T__47;
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
	// $ANTLR end "T__47"

	// $ANTLR start "T__48"
	public final void mT__48() throws RecognitionException {
		try {
			int _type = T__48;
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
	// $ANTLR end "T__48"

	// $ANTLR start "T__49"
	public final void mT__49() throws RecognitionException {
		try {
			int _type = T__49;
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
	// $ANTLR end "T__49"

	// $ANTLR start "T__50"
	public final void mT__50() throws RecognitionException {
		try {
			int _type = T__50;
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
	// $ANTLR end "T__50"

	// $ANTLR start "T__51"
	public final void mT__51() throws RecognitionException {
		try {
			int _type = T__51;
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
	// $ANTLR end "T__51"

	// $ANTLR start "T__52"
	public final void mT__52() throws RecognitionException {
		try {
			int _type = T__52;
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
	// $ANTLR end "T__52"

	// $ANTLR start "T__53"
	public final void mT__53() throws RecognitionException {
		try {
			int _type = T__53;
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
	// $ANTLR end "T__53"

	// $ANTLR start "T__54"
	public final void mT__54() throws RecognitionException {
		try {
			int _type = T__54;
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
	// $ANTLR end "T__54"

	// $ANTLR start "T__55"
	public final void mT__55() throws RecognitionException {
		try {
			int _type = T__55;
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
	// $ANTLR end "T__55"

	// $ANTLR start "T__56"
	public final void mT__56() throws RecognitionException {
		try {
			int _type = T__56;
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
	// $ANTLR end "T__56"

	// $ANTLR start "T__57"
	public final void mT__57() throws RecognitionException {
		try {
			int _type = T__57;
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
	// $ANTLR end "T__57"

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// AssessQuery.g:135:12: ( ( 'A' | 'a' ) )
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
			// AssessQuery.g:136:12: ( ( 'B' | 'b' ) )
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

	// $ANTLR start "C"
	public final void mC() throws RecognitionException {
		try {
			// AssessQuery.g:137:12: ( ( 'C' | 'c' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:138:12: ( ( 'D' | 'd' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:139:12: ( ( 'E' | 'e' ) )
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
			// AssessQuery.g:140:12: ( ( 'F' | 'f' ) )
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
			// AssessQuery.g:141:12: ( ( 'G' | 'g' ) )
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
			// AssessQuery.g:142:12: ( ( 'H' | 'h' ) )
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
			// AssessQuery.g:143:12: ( ( 'I' | 'i' ) )
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

	// $ANTLR start "J"
	public final void mJ() throws RecognitionException {
		try {
			// AssessQuery.g:144:12: ( ( 'J' | 'j' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:145:12: ( ( 'K' | 'k' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:146:12: ( ( 'L' | 'l' ) )
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

	// $ANTLR start "M"
	public final void mM() throws RecognitionException {
		try {
			// AssessQuery.g:147:12: ( ( 'M' | 'm' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:148:12: ( ( 'N' | 'n' ) )
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
			// AssessQuery.g:149:12: ( ( 'O' | 'o' ) )
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
			// AssessQuery.g:150:12: ( ( 'P' | 'p' ) )
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

	// $ANTLR start "Q"
	public final void mQ() throws RecognitionException {
		try {
			// AssessQuery.g:151:12: ( ( 'Q' | 'q' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:152:12: ( ( 'R' | 'r' ) )
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
			// AssessQuery.g:153:12: ( ( 'S' | 's' ) )
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
			// AssessQuery.g:154:12: ( ( 'T' | 't' ) )
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
			// AssessQuery.g:155:12: ( ( 'U' | 'u' ) )
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

	// $ANTLR start "V"
	public final void mV() throws RecognitionException {
		try {
			// AssessQuery.g:156:12: ( ( 'V' | 'v' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:157:12: ( ( 'W' | 'w' ) )
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

	// $ANTLR start "X"
	public final void mX() throws RecognitionException {
		try {
			// AssessQuery.g:158:12: ( ( 'X' | 'x' ) )
			// AssessQuery.g:
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
			// AssessQuery.g:159:12: ( ( 'Y' | 'y' ) )
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

	// $ANTLR start "Z"
	public final void mZ() throws RecognitionException {
		try {
			// AssessQuery.g:160:12: ( ( 'Z' | 'z' ) )
			// AssessQuery.g:
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

	// $ANTLR start "AGAINST"
	public final void mAGAINST() throws RecognitionException {
		try {
			int _type = AGAINST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:163:9: ( A G A I N S T )
			// AssessQuery.g:163:11: A G A I N S T
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
			// AssessQuery.g:164:8: ( A S S E S S )
			// AssessQuery.g:164:10: A S S E S S
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
			// AssessQuery.g:165:4: ( B Y )
			// AssessQuery.g:165:6: B Y
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
			// AssessQuery.g:166:5: ( F O R )
			// AssessQuery.g:166:7: F O R
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
			// AssessQuery.g:167:8: ( L A B E L S )
			// AssessQuery.g:167:10: L A B E L S
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
			// AssessQuery.g:168:6: ( P A S T )
			// AssessQuery.g:168:8: P A S T
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
			// AssessQuery.g:169:7: ( U S I N G )
			// AssessQuery.g:169:9: U S I N G
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
			// AssessQuery.g:170:6: ( W I T H )
			// AssessQuery.g:170:8: W I T H
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

	// $ANTLR start "AGGREGATE"
	public final void mAGGREGATE() throws RecognitionException {
		try {
			int _type = AGGREGATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:172:11: ( ( A V G | A V E R A G E | M I N | M I N I M U M | M A X | M A X I M U M | S U M | C O U N T ) )
			// AssessQuery.g:172:13: ( A V G | A V E R A G E | M I N | M I N I M U M | M A X | M A X I M U M | S U M | C O U N T )
			{
			// AssessQuery.g:172:13: ( A V G | A V E R A G E | M I N | M I N I M U M | M A X | M A X I M U M | S U M | C O U N T )
			int alt1=8;
			switch ( input.LA(1) ) {
			case 'A':
			case 'a':
				{
				int LA1_1 = input.LA(2);
				if ( (LA1_1=='V'||LA1_1=='v') ) {
					int LA1_5 = input.LA(3);
					if ( (LA1_5=='G'||LA1_5=='g') ) {
						alt1=1;
					}
					else if ( (LA1_5=='E'||LA1_5=='e') ) {
						alt1=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 1, 5, input);
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
							new NoViableAltException("", 1, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 'M':
			case 'm':
				{
				int LA1_2 = input.LA(2);
				if ( (LA1_2=='I'||LA1_2=='i') ) {
					int LA1_6 = input.LA(3);
					if ( (LA1_6=='N'||LA1_6=='n') ) {
						int LA1_10 = input.LA(4);
						if ( (LA1_10=='I'||LA1_10=='i') ) {
							alt1=4;
						}

						else {
							alt1=3;
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 1, 6, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA1_2=='A'||LA1_2=='a') ) {
					int LA1_7 = input.LA(3);
					if ( (LA1_7=='X'||LA1_7=='x') ) {
						int LA1_11 = input.LA(4);
						if ( (LA1_11=='I'||LA1_11=='i') ) {
							alt1=6;
						}

						else {
							alt1=5;
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 1, 7, input);
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
							new NoViableAltException("", 1, 2, input);
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
				alt1=7;
				}
				break;
			case 'C':
			case 'c':
				{
				alt1=8;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}
			switch (alt1) {
				case 1 :
					// AssessQuery.g:172:14: A V G
					{
					mA(); 

					mV(); 

					mG(); 

					}
					break;
				case 2 :
					// AssessQuery.g:172:22: A V E R A G E
					{
					mA(); 

					mV(); 

					mE(); 

					mR(); 

					mA(); 

					mG(); 

					mE(); 

					}
					break;
				case 3 :
					// AssessQuery.g:173:13: M I N
					{
					mM(); 

					mI(); 

					mN(); 

					}
					break;
				case 4 :
					// AssessQuery.g:173:21: M I N I M U M
					{
					mM(); 

					mI(); 

					mN(); 

					mI(); 

					mM(); 

					mU(); 

					mM(); 

					}
					break;
				case 5 :
					// AssessQuery.g:174:13: M A X
					{
					mM(); 

					mA(); 

					mX(); 

					}
					break;
				case 6 :
					// AssessQuery.g:174:21: M A X I M U M
					{
					mM(); 

					mA(); 

					mX(); 

					mI(); 

					mM(); 

					mU(); 

					mM(); 

					}
					break;
				case 7 :
					// AssessQuery.g:175:13: S U M
					{
					mS(); 

					mU(); 

					mM(); 

					}
					break;
				case 8 :
					// AssessQuery.g:175:21: C O U N T
					{
					mC(); 

					mO(); 

					mU(); 

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
	// $ANTLR end "AGGREGATE"

	// $ANTLR start "SIGN"
	public final void mSIGN() throws RecognitionException {
		try {
			int _type = SIGN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:178:6: ( ( '+' | '-' ) )
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
			// AssessQuery.g:179:4: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' )+ )
			// AssessQuery.g:179:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' )+
			{
			// AssessQuery.g:179:6: ( 'a' .. 'z' | 'A' .. 'Z' | '_' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// AssessQuery.g:
					{
					if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
	// $ANTLR end "ID"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// AssessQuery.g:181:5: ( ( '0' .. '9' )+ )
			// AssessQuery.g:181:7: ( '0' .. '9' )+
			{
			// AssessQuery.g:181:7: ( '0' .. '9' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
					alt3=1;
				}

				switch (alt3) {
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
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
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
			// AssessQuery.g:182:7: ( INT '.' INT )
			// AssessQuery.g:182:9: INT '.' INT
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
			// AssessQuery.g:184:4: ( ( ' ' | '\\t' | '\\n' | '\\r' ) )
			// AssessQuery.g:184:6: ( ' ' | '\\t' | '\\n' | '\\r' )
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
		// AssessQuery.g:1:8: ( T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | AGAINST | ASSESS | BY | FOR | LABELS | PAST | USING | WITH | AGGREGATE | SIGN | ID | INT | FLOAT | WS )
		int alt4=28;
		alt4 = dfa4.predict(input);
		switch (alt4) {
			case 1 :
				// AssessQuery.g:1:10: T__44
				{
				mT__44(); 

				}
				break;
			case 2 :
				// AssessQuery.g:1:16: T__45
				{
				mT__45(); 

				}
				break;
			case 3 :
				// AssessQuery.g:1:22: T__46
				{
				mT__46(); 

				}
				break;
			case 4 :
				// AssessQuery.g:1:28: T__47
				{
				mT__47(); 

				}
				break;
			case 5 :
				// AssessQuery.g:1:34: T__48
				{
				mT__48(); 

				}
				break;
			case 6 :
				// AssessQuery.g:1:40: T__49
				{
				mT__49(); 

				}
				break;
			case 7 :
				// AssessQuery.g:1:46: T__50
				{
				mT__50(); 

				}
				break;
			case 8 :
				// AssessQuery.g:1:52: T__51
				{
				mT__51(); 

				}
				break;
			case 9 :
				// AssessQuery.g:1:58: T__52
				{
				mT__52(); 

				}
				break;
			case 10 :
				// AssessQuery.g:1:64: T__53
				{
				mT__53(); 

				}
				break;
			case 11 :
				// AssessQuery.g:1:70: T__54
				{
				mT__54(); 

				}
				break;
			case 12 :
				// AssessQuery.g:1:76: T__55
				{
				mT__55(); 

				}
				break;
			case 13 :
				// AssessQuery.g:1:82: T__56
				{
				mT__56(); 

				}
				break;
			case 14 :
				// AssessQuery.g:1:88: T__57
				{
				mT__57(); 

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
				// AssessQuery.g:1:139: AGGREGATE
				{
				mAGGREGATE(); 

				}
				break;
			case 24 :
				// AssessQuery.g:1:149: SIGN
				{
				mSIGN(); 

				}
				break;
			case 25 :
				// AssessQuery.g:1:154: ID
				{
				mID(); 

				}
				break;
			case 26 :
				// AssessQuery.g:1:157: INT
				{
				mINT(); 

				}
				break;
			case 27 :
				// AssessQuery.g:1:161: FLOAT
				{
				mFLOAT(); 

				}
				break;
			case 28 :
				// AssessQuery.g:1:167: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA4 dfa4 = new DFA4(this);
	static final String DFA4_eotS =
		"\13\uffff\2\32\2\uffff\12\32\2\uffff\1\54\1\uffff\1\32\1\57\15\32\2\uffff"+
		"\1\32\1\uffff\1\77\2\32\1\102\1\32\1\104\4\32\3\102\2\32\1\uffff\2\32"+
		"\1\uffff\1\32\1\uffff\1\32\1\121\1\32\1\123\10\32\1\uffff\1\134\1\uffff"+
		"\2\32\1\102\2\32\1\141\1\32\1\143\1\uffff\3\32\1\147\1\uffff\1\102\1\uffff"+
		"\2\102\1\32\1\uffff\1\32\1\uffff";
	static final String DFA4_eofS =
		"\152\uffff";
	static final String DFA4_minS =
		"\1\11\12\uffff\1\131\1\156\2\uffff\1\107\1\131\1\117\2\101\1\123\1\111"+
		"\1\101\1\125\1\117\2\uffff\1\56\1\uffff\1\156\1\101\1\146\1\101\1\123"+
		"\1\105\1\122\1\102\1\123\1\111\1\124\1\116\1\130\1\115\1\125\2\uffff\1"+
		"\143\1\uffff\1\101\1\111\1\105\1\101\1\122\1\101\1\105\1\124\1\116\1\110"+
		"\3\101\1\116\1\150\1\uffff\1\116\1\123\1\uffff\1\101\1\uffff\1\114\1\101"+
		"\1\107\1\101\2\115\1\124\1\155\2\123\1\107\1\123\1\uffff\1\101\1\uffff"+
		"\2\125\1\101\1\141\1\124\1\101\1\105\1\101\1\uffff\2\115\1\162\1\101\1"+
		"\uffff\1\101\1\uffff\2\101\1\153\1\uffff\1\56\1\uffff";
	static final String DFA4_maxS =
		"\1\175\12\uffff\1\171\1\156\2\uffff\1\166\1\171\1\157\2\141\1\163\2\151"+
		"\1\165\1\157\2\uffff\1\71\1\uffff\1\156\1\172\1\146\1\141\1\163\1\147"+
		"\1\162\1\142\1\163\1\151\1\164\1\156\1\170\1\155\1\165\2\uffff\1\143\1"+
		"\uffff\1\172\1\151\1\145\1\172\1\162\1\172\1\145\1\164\1\156\1\150\3\172"+
		"\1\156\1\150\1\uffff\1\156\1\163\1\uffff\1\141\1\uffff\1\154\1\172\1\147"+
		"\1\172\2\155\1\164\1\155\2\163\1\147\1\163\1\uffff\1\172\1\uffff\2\165"+
		"\1\172\1\141\1\164\1\172\1\145\1\172\1\uffff\2\155\1\162\1\172\1\uffff"+
		"\1\172\1\uffff\2\172\1\153\1\uffff\1\56\1\uffff";
	static final String DFA4_acceptS =
		"\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\2\uffff\1\15\1\16"+
		"\12\uffff\1\30\1\31\1\uffff\1\34\17\uffff\1\32\1\33\1\uffff\1\21\17\uffff"+
		"\1\14\2\uffff\1\27\1\uffff\1\22\14\uffff\1\24\1\uffff\1\26\10\uffff\1"+
		"\25\4\uffff\1\20\1\uffff\1\23\3\uffff\1\17\1\uffff\1\13";
	static final String DFA4_specialS =
		"\152\uffff}>";
	static final String[] DFA4_transitionS = {
			"\2\34\2\uffff\1\34\22\uffff\1\34\6\uffff\1\11\1\1\1\2\1\uffff\1\31\1"+
			"\3\1\31\1\4\1\5\12\33\1\6\2\uffff\1\7\3\uffff\1\17\1\20\1\30\2\32\1\21"+
			"\5\32\1\22\1\26\2\32\1\23\2\32\1\27\1\32\1\24\1\32\1\25\3\32\1\10\1\uffff"+
			"\1\12\1\uffff\1\32\1\uffff\1\17\1\13\1\30\2\32\1\21\2\32\1\14\2\32\1"+
			"\22\1\26\2\32\1\23\2\32\1\27\1\32\1\24\1\32\1\25\3\32\1\15\1\uffff\1"+
			"\16",
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
			"\1\36\13\uffff\1\35\23\uffff\1\36",
			"\1\37",
			"",
			"",
			"\1\40\13\uffff\1\41\2\uffff\1\42\20\uffff\1\40\13\uffff\1\41\2\uffff"+
			"\1\42",
			"\1\36\37\uffff\1\36",
			"\1\43\37\uffff\1\43",
			"\1\44\37\uffff\1\44",
			"\1\45\37\uffff\1\45",
			"\1\46\37\uffff\1\46",
			"\1\47\37\uffff\1\47",
			"\1\51\7\uffff\1\50\27\uffff\1\51\7\uffff\1\50",
			"\1\52\37\uffff\1\52",
			"\1\53\37\uffff\1\53",
			"",
			"",
			"\1\55\1\uffff\12\33",
			"",
			"\1\56",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\60",
			"\1\61\37\uffff\1\61",
			"\1\62\37\uffff\1\62",
			"\1\64\1\uffff\1\63\35\uffff\1\64\1\uffff\1\63",
			"\1\65\37\uffff\1\65",
			"\1\66\37\uffff\1\66",
			"\1\67\37\uffff\1\67",
			"\1\70\37\uffff\1\70",
			"\1\71\37\uffff\1\71",
			"\1\72\37\uffff\1\72",
			"\1\73\37\uffff\1\73",
			"\1\74\37\uffff\1\74",
			"\1\75\37\uffff\1\75",
			"",
			"",
			"\1\76",
			"",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\100\37\uffff\1\100",
			"\1\101\37\uffff\1\101",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\103\37\uffff\1\103",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\105\37\uffff\1\105",
			"\1\106\37\uffff\1\106",
			"\1\107\37\uffff\1\107",
			"\1\110\37\uffff\1\110",
			"\10\32\1\111\21\32\4\uffff\1\32\1\uffff\10\32\1\111\21\32",
			"\10\32\1\112\21\32\4\uffff\1\32\1\uffff\10\32\1\112\21\32",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\113\37\uffff\1\113",
			"\1\114",
			"",
			"\1\115\37\uffff\1\115",
			"\1\116\37\uffff\1\116",
			"",
			"\1\117\37\uffff\1\117",
			"",
			"\1\120\37\uffff\1\120",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\122\37\uffff\1\122",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\124\37\uffff\1\124",
			"\1\125\37\uffff\1\125",
			"\1\126\37\uffff\1\126",
			"\1\127",
			"\1\130\37\uffff\1\130",
			"\1\131\37\uffff\1\131",
			"\1\132\37\uffff\1\132",
			"\1\133\37\uffff\1\133",
			"",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"",
			"\1\135\37\uffff\1\135",
			"\1\136\37\uffff\1\136",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\137",
			"\1\140\37\uffff\1\140",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\142\37\uffff\1\142",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"",
			"\1\144\37\uffff\1\144",
			"\1\145\37\uffff\1\145",
			"\1\146",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\32\32\4\uffff\1\32\1\uffff\32\32",
			"\1\150",
			"",
			"\1\151",
			""
	};

	static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
	static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
	static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
	static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
	static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
	static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
	static final short[][] DFA4_transition;

	static {
		int numStates = DFA4_transitionS.length;
		DFA4_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
		}
	}

	protected class DFA4 extends DFA {

		public DFA4(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 4;
			this.eot = DFA4_eot;
			this.eof = DFA4_eof;
			this.min = DFA4_min;
			this.max = DFA4_max;
			this.accept = DFA4_accept;
			this.special = DFA4_special;
			this.transition = DFA4_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | AGAINST | ASSESS | BY | FOR | LABELS | PAST | USING | WITH | AGGREGATE | SIGN | ID | INT | FLOAT | WS );";
		}
	}

}
