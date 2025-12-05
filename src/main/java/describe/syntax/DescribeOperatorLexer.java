// $ANTLR 3.4 DescribeOperator.g 2025-12-04 15:18:21

    package describe.syntax;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class DescribeOperatorLexer extends Lexer {
    public static final int EOF=-1;
    public static final int AGGRFUNC=4;
    public static final int AND=5;
    public static final int AS=6;
    public static final int ASC=7;
    public static final int BY=8;
    public static final int COMMA=9;
    public static final int DESC=10;
    public static final int DESCRIBE=11;
    public static final int DIGIT=12;
    public static final int DOT=13;
    public static final int EQUAL=14;
    public static final int FOR=15;
    public static final int GROUP=16;
    public static final int GT=17;
    public static final int GTE=18;
    public static final int IN=19;
    public static final int JOIN=20;
    public static final int LBRACE=21;
    public static final int LETTER=22;
    public static final int LPARENTHESIS=23;
    public static final int LT=24;
    public static final int LTE=25;
    public static final int MINUS=26;
    public static final int NEQ=27;
    public static final int NUMBER=28;
    public static final int ON=29;
    public static final int ORDER=30;
    public static final int PLUS=31;
    public static final int RBRACE=32;
    public static final int RPARENTHESIS=33;
    public static final int SLASH=34;
    public static final int STAR=35;
    public static final int TEXTVALUE=36;
    public static final int USING=37;
    public static final int WITH=38;
    public static final int WORD=39;
    public static final int WS=40;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public DescribeOperatorLexer() {} 
    public DescribeOperatorLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public DescribeOperatorLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "DescribeOperator.g"; }

    // $ANTLR start "DESCRIBE"
    public final void mDESCRIBE() throws RecognitionException {
        try {
            int _type = DESCRIBE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:183:9: ( 'DESCRIBE' )
            // DescribeOperator.g:183:11: 'DESCRIBE'
            {
            match("DESCRIBE"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DESCRIBE"

    // $ANTLR start "WITH"
    public final void mWITH() throws RecognitionException {
        try {
            int _type = WITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:184:5: ( 'WITH' )
            // DescribeOperator.g:184:7: 'WITH'
            {
            match("WITH"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WITH"

    // $ANTLR start "FOR"
    public final void mFOR() throws RecognitionException {
        try {
            int _type = FOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:185:4: ( 'FOR' )
            // DescribeOperator.g:185:6: 'FOR'
            {
            match("FOR"); 



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
            // DescribeOperator.g:186:6: ( 'GROUP' )
            // DescribeOperator.g:186:8: 'GROUP'
            {
            match("GROUP"); 



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
            // DescribeOperator.g:187:3: ( 'BY' )
            // DescribeOperator.g:187:5: 'BY'
            {
            match("BY"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BY"

    // $ANTLR start "JOIN"
    public final void mJOIN() throws RecognitionException {
        try {
            int _type = JOIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:188:5: ( 'JOIN' )
            // DescribeOperator.g:188:7: 'JOIN'
            {
            match("JOIN"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "JOIN"

    // $ANTLR start "ON"
    public final void mON() throws RecognitionException {
        try {
            int _type = ON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:189:3: ( 'ON' )
            // DescribeOperator.g:189:5: 'ON'
            {
            match("ON"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ON"

    // $ANTLR start "IN"
    public final void mIN() throws RecognitionException {
        try {
            int _type = IN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:190:3: ( 'IN' )
            // DescribeOperator.g:190:5: 'IN'
            {
            match("IN"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IN"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:191:4: ( 'AND' )
            // DescribeOperator.g:191:6: 'AND'
            {
            match("AND"); 



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
            // DescribeOperator.g:192:3: ( 'AS' )
            // DescribeOperator.g:192:5: 'AS'
            {
            match("AS"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AS"

    // $ANTLR start "ORDER"
    public final void mORDER() throws RecognitionException {
        try {
            int _type = ORDER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:193:6: ( 'ORDER' )
            // DescribeOperator.g:193:8: 'ORDER'
            {
            match("ORDER"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ORDER"

    // $ANTLR start "USING"
    public final void mUSING() throws RecognitionException {
        try {
            int _type = USING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:194:6: ( 'USING' )
            // DescribeOperator.g:194:8: 'USING'
            {
            match("USING"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "USING"

    // $ANTLR start "ASC"
    public final void mASC() throws RecognitionException {
        try {
            int _type = ASC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:195:4: ( 'ASC' )
            // DescribeOperator.g:195:6: 'ASC'
            {
            match("ASC"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ASC"

    // $ANTLR start "DESC"
    public final void mDESC() throws RecognitionException {
        try {
            int _type = DESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:196:5: ( 'DESC' )
            // DescribeOperator.g:196:7: 'DESC'
            {
            match("DESC"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DESC"

    // $ANTLR start "AGGRFUNC"
    public final void mAGGRFUNC() throws RecognitionException {
        try {
            int _type = AGGRFUNC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:198:9: ( ( 'MIN' | 'MAX' | 'SUM' | 'AVG' | 'CNT' ) )
            // DescribeOperator.g:198:11: ( 'MIN' | 'MAX' | 'SUM' | 'AVG' | 'CNT' )
            {
            // DescribeOperator.g:198:11: ( 'MIN' | 'MAX' | 'SUM' | 'AVG' | 'CNT' )
            int alt1=5;
            switch ( input.LA(1) ) {
            case 'M':
                {
                int LA1_1 = input.LA(2);

                if ( (LA1_1=='I') ) {
                    alt1=1;
                }
                else if ( (LA1_1=='A') ) {
                    alt1=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;

                }
                }
                break;
            case 'S':
                {
                alt1=3;
                }
                break;
            case 'A':
                {
                alt1=4;
                }
                break;
            case 'C':
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
                    // DescribeOperator.g:198:12: 'MIN'
                    {
                    match("MIN"); 



                    }
                    break;
                case 2 :
                    // DescribeOperator.g:198:18: 'MAX'
                    {
                    match("MAX"); 



                    }
                    break;
                case 3 :
                    // DescribeOperator.g:198:24: 'SUM'
                    {
                    match("SUM"); 



                    }
                    break;
                case 4 :
                    // DescribeOperator.g:198:30: 'AVG'
                    {
                    match("AVG"); 



                    }
                    break;
                case 5 :
                    // DescribeOperator.g:198:36: 'CNT'
                    {
                    match("CNT"); 



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

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:200:5: ( '+' )
            // DescribeOperator.g:200:7: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:201:6: ( '-' )
            // DescribeOperator.g:201:8: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "STAR"
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:202:5: ( '*' )
            // DescribeOperator.g:202:7: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STAR"

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:203:6: ( '/' )
            // DescribeOperator.g:203:8: '/'
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
    // $ANTLR end "SLASH"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:205:6: ( '=' )
            // DescribeOperator.g:205:8: '='
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

    // $ANTLR start "GT"
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:206:3: ( '>' )
            // DescribeOperator.g:206:5: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GT"

    // $ANTLR start "LT"
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:207:3: ( '<' )
            // DescribeOperator.g:207:5: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LT"

    // $ANTLR start "GTE"
    public final void mGTE() throws RecognitionException {
        try {
            int _type = GTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:208:4: ( '>=' )
            // DescribeOperator.g:208:6: '>='
            {
            match(">="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GTE"

    // $ANTLR start "LTE"
    public final void mLTE() throws RecognitionException {
        try {
            int _type = LTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:209:4: ( '<=' )
            // DescribeOperator.g:209:6: '<='
            {
            match("<="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LTE"

    // $ANTLR start "NEQ"
    public final void mNEQ() throws RecognitionException {
        try {
            int _type = NEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:210:4: ( '!=' )
            // DescribeOperator.g:210:6: '!='
            {
            match("!="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NEQ"

    // $ANTLR start "LPARENTHESIS"
    public final void mLPARENTHESIS() throws RecognitionException {
        try {
            int _type = LPARENTHESIS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:212:13: ( '(' )
            // DescribeOperator.g:212:15: '('
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
            // DescribeOperator.g:213:13: ( ')' )
            // DescribeOperator.g:213:15: ')'
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

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:214:7: ( '{' )
            // DescribeOperator.g:214:9: '{'
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
    // $ANTLR end "LBRACE"

    // $ANTLR start "RBRACE"
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:215:7: ( '}' )
            // DescribeOperator.g:215:9: '}'
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
    // $ANTLR end "RBRACE"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:216:6: ( ',' )
            // DescribeOperator.g:216:8: ','
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

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:217:4: ( '.' )
            // DescribeOperator.g:217:6: '.'
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
    // $ANTLR end "DOT"

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
        try {
            int _type = WORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:219:5: ( ( LETTER | '_' | DIGIT )+ ( DOT ( LETTER | '_' | DIGIT )+ )* )
            // DescribeOperator.g:219:7: ( LETTER | '_' | DIGIT )+ ( DOT ( LETTER | '_' | DIGIT )+ )*
            {
            // DescribeOperator.g:219:7: ( LETTER | '_' | DIGIT )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // DescribeOperator.g:
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
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            // DescribeOperator.g:219:31: ( DOT ( LETTER | '_' | DIGIT )+ )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='.') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // DescribeOperator.g:219:32: DOT ( LETTER | '_' | DIGIT )+
            	    {
            	    mDOT(); 


            	    // DescribeOperator.g:219:36: ( LETTER | '_' | DIGIT )+
            	    int cnt3=0;
            	    loop3:
            	    do {
            	        int alt3=2;
            	        int LA3_0 = input.LA(1);

            	        if ( ((LA3_0 >= '0' && LA3_0 <= '9')||(LA3_0 >= 'A' && LA3_0 <= 'Z')||LA3_0=='_'||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
            	            alt3=1;
            	        }


            	        switch (alt3) {
            	    	case 1 :
            	    	    // DescribeOperator.g:
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
            	    	    if ( cnt3 >= 1 ) break loop3;
            	                EarlyExitException eee =
            	                    new EarlyExitException(3, input);
            	                throw eee;
            	        }
            	        cnt3++;
            	    } while (true);


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


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
            // DescribeOperator.g:221:10: ( '\\'' ( LETTER | DIGIT | '_' | '/' | '-' | ' ' | '.' )+ '\\'' )
            // DescribeOperator.g:221:12: '\\'' ( LETTER | DIGIT | '_' | '/' | '-' | ' ' | '.' )+ '\\''
            {
            match('\''); 

            // DescribeOperator.g:221:16: ( LETTER | DIGIT | '_' | '/' | '-' | ' ' | '.' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==' '||(LA5_0 >= '-' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // DescribeOperator.g:
            	    {
            	    if ( input.LA(1)==' '||(input.LA(1) >= '-' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


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
            // DescribeOperator.g:222:7: ( ( '\\'' )? ( '-' )? ( DIGIT )+ ( '.' ( DIGIT )+ )? ( '\\'' )? )
            // DescribeOperator.g:222:9: ( '\\'' )? ( '-' )? ( DIGIT )+ ( '.' ( DIGIT )+ )? ( '\\'' )?
            {
            // DescribeOperator.g:222:9: ( '\\'' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\'') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // DescribeOperator.g:222:9: '\\''
                    {
                    match('\''); 

                    }
                    break;

            }


            // DescribeOperator.g:222:15: ( '-' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='-') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // DescribeOperator.g:222:16: '-'
                    {
                    match('-'); 

                    }
                    break;

            }


            // DescribeOperator.g:222:22: ( DIGIT )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // DescribeOperator.g:
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
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            // DescribeOperator.g:222:31: ( '.' ( DIGIT )+ )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='.') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // DescribeOperator.g:222:32: '.' ( DIGIT )+
                    {
                    match('.'); 

                    // DescribeOperator.g:222:36: ( DIGIT )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // DescribeOperator.g:
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
                    	    if ( cnt9 >= 1 ) break loop9;
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);


                    }
                    break;

            }


            // DescribeOperator.g:222:47: ( '\\'' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\'') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // DescribeOperator.g:222:47: '\\''
                    {
                    match('\''); 

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
    // $ANTLR end "NUMBER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // DescribeOperator.g:224:15: ( '0' .. '9' )
            // DescribeOperator.g:
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
            // DescribeOperator.g:225:16: ( 'a' .. 'z' | 'A' .. 'Z' )
            // DescribeOperator.g:
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

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // DescribeOperator.g:226:3: ( ( ' ' | '\\t' | '\\r' | '\\n' | '\\r\\n' | '\\f' ) )
            // DescribeOperator.g:226:5: ( ' ' | '\\t' | '\\r' | '\\n' | '\\r\\n' | '\\f' )
            {
            // DescribeOperator.g:226:5: ( ' ' | '\\t' | '\\r' | '\\n' | '\\r\\n' | '\\f' )
            int alt12=6;
            switch ( input.LA(1) ) {
            case ' ':
                {
                alt12=1;
                }
                break;
            case '\t':
                {
                alt12=2;
                }
                break;
            case '\r':
                {
                int LA12_3 = input.LA(2);

                if ( (LA12_3=='\n') ) {
                    alt12=5;
                }
                else {
                    alt12=3;
                }
                }
                break;
            case '\n':
                {
                alt12=4;
                }
                break;
            case '\f':
                {
                alt12=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // DescribeOperator.g:226:6: ' '
                    {
                    match(' '); 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:226:12: '\\t'
                    {
                    match('\t'); 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:226:19: '\\r'
                    {
                    match('\r'); 

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:226:25: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 5 :
                    // DescribeOperator.g:226:30: '\\r\\n'
                    {
                    match("\r\n"); 



                    }
                    break;
                case 6 :
                    // DescribeOperator.g:226:37: '\\f'
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

    public void mTokens() throws RecognitionException {
        // DescribeOperator.g:1:8: ( DESCRIBE | WITH | FOR | GROUP | BY | JOIN | ON | IN | AND | AS | ORDER | USING | ASC | DESC | AGGRFUNC | PLUS | MINUS | STAR | SLASH | EQUAL | GT | LT | GTE | LTE | NEQ | LPARENTHESIS | RPARENTHESIS | LBRACE | RBRACE | COMMA | DOT | WORD | TEXTVALUE | NUMBER | WS )
        int alt13=35;
        alt13 = dfa13.predict(input);
        switch (alt13) {
            case 1 :
                // DescribeOperator.g:1:10: DESCRIBE
                {
                mDESCRIBE(); 


                }
                break;
            case 2 :
                // DescribeOperator.g:1:19: WITH
                {
                mWITH(); 


                }
                break;
            case 3 :
                // DescribeOperator.g:1:24: FOR
                {
                mFOR(); 


                }
                break;
            case 4 :
                // DescribeOperator.g:1:28: GROUP
                {
                mGROUP(); 


                }
                break;
            case 5 :
                // DescribeOperator.g:1:34: BY
                {
                mBY(); 


                }
                break;
            case 6 :
                // DescribeOperator.g:1:37: JOIN
                {
                mJOIN(); 


                }
                break;
            case 7 :
                // DescribeOperator.g:1:42: ON
                {
                mON(); 


                }
                break;
            case 8 :
                // DescribeOperator.g:1:45: IN
                {
                mIN(); 


                }
                break;
            case 9 :
                // DescribeOperator.g:1:48: AND
                {
                mAND(); 


                }
                break;
            case 10 :
                // DescribeOperator.g:1:52: AS
                {
                mAS(); 


                }
                break;
            case 11 :
                // DescribeOperator.g:1:55: ORDER
                {
                mORDER(); 


                }
                break;
            case 12 :
                // DescribeOperator.g:1:61: USING
                {
                mUSING(); 


                }
                break;
            case 13 :
                // DescribeOperator.g:1:67: ASC
                {
                mASC(); 


                }
                break;
            case 14 :
                // DescribeOperator.g:1:71: DESC
                {
                mDESC(); 


                }
                break;
            case 15 :
                // DescribeOperator.g:1:76: AGGRFUNC
                {
                mAGGRFUNC(); 


                }
                break;
            case 16 :
                // DescribeOperator.g:1:85: PLUS
                {
                mPLUS(); 


                }
                break;
            case 17 :
                // DescribeOperator.g:1:90: MINUS
                {
                mMINUS(); 


                }
                break;
            case 18 :
                // DescribeOperator.g:1:96: STAR
                {
                mSTAR(); 


                }
                break;
            case 19 :
                // DescribeOperator.g:1:101: SLASH
                {
                mSLASH(); 


                }
                break;
            case 20 :
                // DescribeOperator.g:1:107: EQUAL
                {
                mEQUAL(); 


                }
                break;
            case 21 :
                // DescribeOperator.g:1:113: GT
                {
                mGT(); 


                }
                break;
            case 22 :
                // DescribeOperator.g:1:116: LT
                {
                mLT(); 


                }
                break;
            case 23 :
                // DescribeOperator.g:1:119: GTE
                {
                mGTE(); 


                }
                break;
            case 24 :
                // DescribeOperator.g:1:123: LTE
                {
                mLTE(); 


                }
                break;
            case 25 :
                // DescribeOperator.g:1:127: NEQ
                {
                mNEQ(); 


                }
                break;
            case 26 :
                // DescribeOperator.g:1:131: LPARENTHESIS
                {
                mLPARENTHESIS(); 


                }
                break;
            case 27 :
                // DescribeOperator.g:1:144: RPARENTHESIS
                {
                mRPARENTHESIS(); 


                }
                break;
            case 28 :
                // DescribeOperator.g:1:157: LBRACE
                {
                mLBRACE(); 


                }
                break;
            case 29 :
                // DescribeOperator.g:1:164: RBRACE
                {
                mRBRACE(); 


                }
                break;
            case 30 :
                // DescribeOperator.g:1:171: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 31 :
                // DescribeOperator.g:1:177: DOT
                {
                mDOT(); 


                }
                break;
            case 32 :
                // DescribeOperator.g:1:181: WORD
                {
                mWORD(); 


                }
                break;
            case 33 :
                // DescribeOperator.g:1:186: TEXTVALUE
                {
                mTEXTVALUE(); 


                }
                break;
            case 34 :
                // DescribeOperator.g:1:196: NUMBER
                {
                mNUMBER(); 


                }
                break;
            case 35 :
                // DescribeOperator.g:1:203: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA13_eotS =
        "\1\uffff\15\36\1\uffff\1\61\3\uffff\1\64\1\66\7\uffff\1\36\3\uffff"+
        "\4\36\1\77\1\36\1\101\1\36\1\103\1\36\1\106\6\36\10\uffff\1\62\1"+
        "\uffff\2\36\1\122\1\36\1\uffff\1\36\1\uffff\1\36\1\uffff\1\126\1"+
        "\127\1\uffff\1\130\1\36\4\130\1\36\2\uffff\1\134\1\135\1\uffff\1"+
        "\36\1\137\1\36\3\uffff\1\36\1\62\1\36\2\uffff\1\143\1\uffff\1\144"+
        "\1\145\1\36\3\uffff\1\36\1\150\1\uffff";
    static final String DFA13_eofS =
        "\151\uffff";
    static final String DFA13_minS =
        "\1\11\1\105\1\111\1\117\1\122\1\131\1\117\3\116\1\123\1\101\1\125"+
        "\1\116\1\uffff\1\60\3\uffff\2\75\7\uffff\1\47\1\40\2\uffff\1\123"+
        "\1\124\1\122\1\117\1\56\1\111\1\56\1\104\1\56\1\104\1\56\1\107\1"+
        "\111\1\116\1\130\1\115\1\124\6\uffff\1\60\2\40\1\uffff\1\103\1\110"+
        "\1\56\1\125\1\uffff\1\116\1\uffff\1\105\1\uffff\2\56\1\uffff\1\56"+
        "\1\116\4\56\1\47\1\uffff\1\40\2\56\1\uffff\1\120\1\56\1\122\3\uffff"+
        "\1\107\1\40\1\111\2\uffff\1\56\1\uffff\2\56\1\102\3\uffff\1\105"+
        "\1\56\1\uffff";
    static final String DFA13_maxS =
        "\1\175\1\105\1\111\1\117\1\122\1\131\1\117\1\122\1\116\1\126\1\123"+
        "\1\111\1\125\1\116\1\uffff\1\71\3\uffff\2\75\7\uffff\1\71\1\172"+
        "\2\uffff\1\123\1\124\1\122\1\117\1\172\1\111\1\172\1\104\1\172\1"+
        "\104\1\172\1\107\1\111\1\116\1\130\1\115\1\124\6\uffff\3\172\1\uffff"+
        "\1\103\1\110\1\172\1\125\1\uffff\1\116\1\uffff\1\105\1\uffff\2\172"+
        "\1\uffff\1\172\1\116\4\172\1\71\1\uffff\3\172\1\uffff\1\120\1\172"+
        "\1\122\3\uffff\1\107\1\172\1\111\2\uffff\1\172\1\uffff\2\172\1\102"+
        "\3\uffff\1\105\1\172\1\uffff";
    static final String DFA13_acceptS =
        "\16\uffff\1\20\1\uffff\1\22\1\23\1\24\2\uffff\1\31\1\32\1\33\1\34"+
        "\1\35\1\36\1\37\2\uffff\1\40\1\43\21\uffff\1\21\1\42\1\27\1\25\1"+
        "\30\1\26\3\uffff\1\41\4\uffff\1\5\1\uffff\1\7\1\uffff\1\10\2\uffff"+
        "\1\12\7\uffff\1\41\3\uffff\1\3\3\uffff\1\11\1\15\1\17\3\uffff\1"+
        "\16\1\2\1\uffff\1\6\3\uffff\1\4\1\13\1\14\2\uffff\1\1";
    static final String DFA13_specialS =
        "\151\uffff}>";
    static final String[] DFA13_transitionS = {
            "\2\37\1\uffff\2\37\22\uffff\1\37\1\25\5\uffff\1\35\1\26\1\27"+
            "\1\20\1\16\1\32\1\17\1\33\1\21\12\34\2\uffff\1\24\1\22\1\23"+
            "\2\uffff\1\11\1\5\1\15\1\1\1\36\1\3\1\4\1\36\1\10\1\6\2\36\1"+
            "\13\1\36\1\7\3\36\1\14\1\36\1\12\1\36\1\2\3\36\4\uffff\1\36"+
            "\1\uffff\32\36\1\30\1\uffff\1\31",
            "\1\40",
            "\1\41",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46\3\uffff\1\47",
            "\1\50",
            "\1\51\4\uffff\1\52\2\uffff\1\53",
            "\1\54",
            "\1\56\7\uffff\1\55",
            "\1\57",
            "\1\60",
            "",
            "\12\62",
            "",
            "",
            "",
            "\1\63",
            "\1\65",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\62\6\uffff\1\67\1\uffff\12\34",
            "\1\72\14\uffff\1\70\2\72\12\71\7\uffff\32\72\4\uffff\1\72\1"+
            "\uffff\32\72",
            "",
            "",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\100",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\102",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\104",
            "\1\36\1\uffff\12\36\7\uffff\2\36\1\105\27\36\4\uffff\1\36\1"+
            "\uffff\32\36",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\115\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\72\6\uffff\1\72\5\uffff\3\72\12\71\7\uffff\32\72\4\uffff"+
            "\1\72\1\uffff\32\72",
            "\1\72\6\uffff\1\116\5\uffff\1\72\1\117\1\72\12\71\7\uffff\32"+
            "\72\4\uffff\1\72\1\uffff\32\72",
            "",
            "\1\120",
            "\1\121",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\123",
            "",
            "\1\124",
            "",
            "\1\125",
            "",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\131",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\62\10\uffff\12\115",
            "",
            "\1\72\6\uffff\1\72\5\uffff\3\72\12\132\7\uffff\32\72\4\uffff"+
            "\1\72\1\uffff\32\72",
            "\1\36\1\uffff\12\36\7\uffff\21\36\1\133\10\36\4\uffff\1\36"+
            "\1\uffff\32\36",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "\1\136",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\140",
            "",
            "",
            "",
            "\1\141",
            "\1\72\6\uffff\1\116\5\uffff\3\72\12\132\7\uffff\32\72\4\uffff"+
            "\1\72\1\uffff\32\72",
            "\1\142",
            "",
            "",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            "\1\146",
            "",
            "",
            "",
            "\1\147",
            "\1\36\1\uffff\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( DESCRIBE | WITH | FOR | GROUP | BY | JOIN | ON | IN | AND | AS | ORDER | USING | ASC | DESC | AGGRFUNC | PLUS | MINUS | STAR | SLASH | EQUAL | GT | LT | GTE | LTE | NEQ | LPARENTHESIS | RPARENTHESIS | LBRACE | RBRACE | COMMA | DOT | WORD | TEXTVALUE | NUMBER | WS );";
        }
    }
 

}