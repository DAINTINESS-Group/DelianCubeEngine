// $ANTLR 3.4 CubeSql.g 2022-05-30 21:00:12

  package parsermgr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CubeSqlLexer extends Lexer {
    public static final int EOF=-1;
    public static final int A=4;
    public static final int AND=5;
    public static final int AS=6;
    public static final int AT=7;
    public static final int ATTRIBUTES=8;
    public static final int B=9;
    public static final int BACKSLASH=10;
    public static final int C=11;
    public static final int CHILDOF=12;
    public static final int COLON=13;
    public static final int COMMA=14;
    public static final int CREATE=15;
    public static final int CUBE=16;
    public static final int D=17;
    public static final int DATASOURCE=18;
    public static final int DESCRIPTION=19;
    public static final int DIMENSION=20;
    public static final int DIMENSION_TYPE=21;
    public static final int DOT=22;
    public static final int Digit=23;
    public static final int E=24;
    public static final int F=25;
    public static final int FILE=26;
    public static final int G=27;
    public static final int H=28;
    public static final int HIERARCHY=29;
    public static final int I=30;
    public static final int ID=31;
    public static final int INI=32;
    public static final int J=33;
    public static final int K=34;
    public static final int L=35;
    public static final int LBRACE=36;
    public static final int LEVEL=37;
    public static final int LEVELS=38;
    public static final int LIST=39;
    public static final int Letter=40;
    public static final int M=41;
    public static final int MEASURES=42;
    public static final int N=43;
    public static final int NAME=44;
    public static final int O=45;
    public static final int OF=46;
    public static final int P=47;
    public static final int PATH=48;
    public static final int Q=49;
    public static final int QUESTMARK=50;
    public static final int R=51;
    public static final int RBRACE=52;
    public static final int REFERENCES=53;
    public static final int S=54;
    public static final int SLASH=55;
    public static final int T=56;
    public static final int TYPE=57;
    public static final int U=58;
    public static final int UNDERSCORE=59;
    public static final int V=60;
    public static final int W=61;
    public static final int WITH=62;
    public static final int WS=63;
    public static final int X=64;
    public static final int Y=65;
    public static final int Z=66;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public CubeSqlLexer() {} 
    public CubeSqlLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CubeSqlLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "CubeSql.g"; }

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:156:5: ( A N D )
            // CubeSql.g:156:7: A N D
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

    // $ANTLR start "DATASOURCE"
    public final void mDATASOURCE() throws RecognitionException {
        try {
            int _type = DATASOURCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:158:11: ( D A T A S O U R C E )
            // CubeSql.g:158:13: D A T A S O U R C E
            {
            mD(); 


            mA(); 


            mT(); 


            mA(); 


            mS(); 


            mO(); 


            mU(); 


            mR(); 


            mC(); 


            mE(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DATASOURCE"

    // $ANTLR start "TYPE"
    public final void mTYPE() throws RecognitionException {
        try {
            int _type = TYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:160:5: ( T Y P E )
            // CubeSql.g:160:7: T Y P E
            {
            mT(); 


            mY(); 


            mP(); 


            mE(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TYPE"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:162:6: ( ':' )
            // CubeSql.g:162:8: ':'
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
    // $ANTLR end "COLON"

    // $ANTLR start "INI"
    public final void mINI() throws RecognitionException {
        try {
            int _type = INI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:164:4: ( I N I )
            // CubeSql.g:164:6: I N I
            {
            mI(); 


            mN(); 


            mI(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INI"

    // $ANTLR start "FILE"
    public final void mFILE() throws RecognitionException {
        try {
            int _type = FILE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:166:5: ( F I L E )
            // CubeSql.g:166:7: F I L E
            {
            mF(); 


            mI(); 


            mL(); 


            mE(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FILE"

    // $ANTLR start "PATH"
    public final void mPATH() throws RecognitionException {
        try {
            int _type = PATH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:168:5: ( NAME ( NAME | DOT | SLASH | BACKSLASH )* DOT NAME )
            // CubeSql.g:168:7: NAME ( NAME | DOT | SLASH | BACKSLASH )* DOT NAME
            {
            mNAME(); 


            // CubeSql.g:168:12: ( NAME | DOT | SLASH | BACKSLASH )*
            loop1:
            do {
                int alt1=5;
                alt1 = dfa1.predict(input);
                switch (alt1) {
            	case 1 :
            	    // CubeSql.g:168:13: NAME
            	    {
            	    mNAME(); 


            	    }
            	    break;
            	case 2 :
            	    // CubeSql.g:168:20: DOT
            	    {
            	    mDOT(); 


            	    }
            	    break;
            	case 3 :
            	    // CubeSql.g:168:26: SLASH
            	    {
            	    mSLASH(); 


            	    }
            	    break;
            	case 4 :
            	    // CubeSql.g:168:34: BACKSLASH
            	    {
            	    mBACKSLASH(); 


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            mDOT(); 


            mNAME(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PATH"

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:169:6: ( '/' )
            // CubeSql.g:169:8: '/'
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

    // $ANTLR start "BACKSLASH"
    public final void mBACKSLASH() throws RecognitionException {
        try {
            int _type = BACKSLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:170:10: ( '\\'' )
            // CubeSql.g:170:12: '\\''
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
    // $ANTLR end "BACKSLASH"

    // $ANTLR start "CREATE"
    public final void mCREATE() throws RecognitionException {
        try {
            int _type = CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:173:7: ( C R E A T E )
            // CubeSql.g:173:9: C R E A T E
            {
            mC(); 


            mR(); 


            mE(); 


            mA(); 


            mT(); 


            mE(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CREATE"

    // $ANTLR start "CUBE"
    public final void mCUBE() throws RecognitionException {
        try {
            int _type = CUBE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:175:6: ( C U B E )
            // CubeSql.g:175:9: C U B E
            {
            mC(); 


            mU(); 


            mB(); 


            mE(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CUBE"

    // $ANTLR start "REFERENCES"
    public final void mREFERENCES() throws RecognitionException {
        try {
            int _type = REFERENCES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:177:11: ( R E F E R E N C E S )
            // CubeSql.g:177:13: R E F E R E N C E S
            {
            mR(); 


            mE(); 


            mF(); 


            mE(); 


            mR(); 


            mE(); 


            mN(); 


            mC(); 


            mE(); 


            mS(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "REFERENCES"

    // $ANTLR start "DIMENSION"
    public final void mDIMENSION() throws RecognitionException {
        try {
            int _type = DIMENSION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:179:11: ( D I M E N S I O N )
            // CubeSql.g:179:13: D I M E N S I O N
            {
            mD(); 


            mI(); 


            mM(); 


            mE(); 


            mN(); 


            mS(); 


            mI(); 


            mO(); 


            mN(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIMENSION"

    // $ANTLR start "DIMENSION_TYPE"
    public final void mDIMENSION_TYPE() throws RecognitionException {
        try {
            int _type = DIMENSION_TYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:181:15: ( D I M E N S I O N UNDERSCORE T Y P E )
            // CubeSql.g:181:17: D I M E N S I O N UNDERSCORE T Y P E
            {
            mD(); 


            mI(); 


            mM(); 


            mE(); 


            mN(); 


            mS(); 


            mI(); 


            mO(); 


            mN(); 


            mUNDERSCORE(); 


            mT(); 


            mY(); 


            mP(); 


            mE(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIMENSION_TYPE"

    // $ANTLR start "LIST"
    public final void mLIST() throws RecognitionException {
        try {
            int _type = LIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:183:5: ( L I S T )
            // CubeSql.g:183:7: L I S T
            {
            mL(); 


            mI(); 


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
    // $ANTLR end "LIST"

    // $ANTLR start "OF"
    public final void mOF() throws RecognitionException {
        try {
            int _type = OF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:185:3: ( O F )
            // CubeSql.g:185:5: O F
            {
            mO(); 


            mF(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OF"

    // $ANTLR start "LEVELS"
    public final void mLEVELS() throws RecognitionException {
        try {
            int _type = LEVELS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:187:7: ( L E V E L S )
            // CubeSql.g:187:8: L E V E L S
            {
            mL(); 


            mE(); 


            mV(); 


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
    // $ANTLR end "LEVELS"

    // $ANTLR start "LEVEL"
    public final void mLEVEL() throws RecognitionException {
        try {
            int _type = LEVEL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:189:6: ( L E V E L )
            // CubeSql.g:189:8: L E V E L
            {
            mL(); 


            mE(); 


            mV(); 


            mE(); 


            mL(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LEVEL"

    // $ANTLR start "WITH"
    public final void mWITH() throws RecognitionException {
        try {
            int _type = WITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:191:5: ( W I T H )
            // CubeSql.g:191:7: W I T H
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

    // $ANTLR start "ATTRIBUTES"
    public final void mATTRIBUTES() throws RecognitionException {
        try {
            int _type = ATTRIBUTES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:193:11: ( A T T R I B U T E S )
            // CubeSql.g:193:13: A T T R I B U T E S
            {
            mA(); 


            mT(); 


            mT(); 


            mR(); 


            mI(); 


            mB(); 


            mU(); 


            mT(); 


            mE(); 


            mS(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTRIBUTES"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:195:3: ( I D )
            // CubeSql.g:195:5: I D
            {
            mI(); 


            mD(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "DESCRIPTION"
    public final void mDESCRIPTION() throws RecognitionException {
        try {
            int _type = DESCRIPTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:197:12: ( D E S C R I P T I O N )
            // CubeSql.g:197:14: D E S C R I P T I O N
            {
            mD(); 


            mE(); 


            mS(); 


            mC(); 


            mR(); 


            mI(); 


            mP(); 


            mT(); 


            mI(); 


            mO(); 


            mN(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DESCRIPTION"

    // $ANTLR start "AS"
    public final void mAS() throws RecognitionException {
        try {
            int _type = AS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:199:3: ( A S )
            // CubeSql.g:199:5: A S
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

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:201:4: ( A T )
            // CubeSql.g:201:5: A T
            {
            mA(); 


            mT(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AT"

    // $ANTLR start "HIERARCHY"
    public final void mHIERARCHY() throws RecognitionException {
        try {
            int _type = HIERARCHY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:203:10: ( H I E R A R C H Y )
            // CubeSql.g:203:12: H I E R A R C H Y
            {
            mH(); 


            mI(); 


            mE(); 


            mR(); 


            mA(); 


            mR(); 


            mC(); 


            mH(); 


            mY(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HIERARCHY"

    // $ANTLR start "MEASURES"
    public final void mMEASURES() throws RecognitionException {
        try {
            int _type = MEASURES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:205:10: ( M E A S U R E S )
            // CubeSql.g:205:12: M E A S U R E S
            {
            mM(); 


            mE(); 


            mA(); 


            mS(); 


            mU(); 


            mR(); 


            mE(); 


            mS(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MEASURES"

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:207:5: ( Letter ( Letter | Digit | '_' | '-' | '~' )* )
            // CubeSql.g:207:7: Letter ( Letter | Digit | '_' | '-' | '~' )*
            {
            mLetter(); 


            // CubeSql.g:207:14: ( Letter | Digit | '_' | '-' | '~' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='-'||(LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')||LA2_0=='~') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // CubeSql.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z')||input.LA(1)=='~' ) {
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
            	    break loop2;
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
    // $ANTLR end "NAME"

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:209:7: ( '{' )
            // CubeSql.g:209:8: '{'
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
            // CubeSql.g:210:7: ( '}' )
            // CubeSql.g:210:8: '}'
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

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:211:4: ( '.' )
            // CubeSql.g:211:5: '.'
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

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:212:6: ( ',' )
            // CubeSql.g:212:7: ','
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

    // $ANTLR start "CHILDOF"
    public final void mCHILDOF() throws RecognitionException {
        try {
            int _type = CHILDOF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:213:8: ( '>' )
            // CubeSql.g:213:9: '>'
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
    // $ANTLR end "CHILDOF"

    // $ANTLR start "QUESTMARK"
    public final void mQUESTMARK() throws RecognitionException {
        try {
            int _type = QUESTMARK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:214:10: ( ';' )
            // CubeSql.g:214:11: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "QUESTMARK"

    // $ANTLR start "UNDERSCORE"
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            int _type = UNDERSCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:215:11: ( '_' )
            // CubeSql.g:215:12: '_'
            {
            match('_'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNDERSCORE"

    // $ANTLR start "Digit"
    public final void mDigit() throws RecognitionException {
        try {
            // CubeSql.g:217:16: ( '0' .. '9' )
            // CubeSql.g:
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
    // $ANTLR end "Digit"

    // $ANTLR start "Letter"
    public final void mLetter() throws RecognitionException {
        try {
            // CubeSql.g:218:17: ( 'a' .. 'z' | 'A' .. 'Z' )
            // CubeSql.g:
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
    // $ANTLR end "Letter"

    // $ANTLR start "A"
    public final void mA() throws RecognitionException {
        try {
            // CubeSql.g:219:11: ( 'A' | 'a' )
            // CubeSql.g:
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
            // CubeSql.g:220:11: ( 'B' | 'b' )
            // CubeSql.g:
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
            // CubeSql.g:221:11: ( 'C' | 'c' )
            // CubeSql.g:
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
            // CubeSql.g:222:11: ( 'D' | 'd' )
            // CubeSql.g:
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
            // CubeSql.g:223:11: ( 'E' | 'e' )
            // CubeSql.g:
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
            // CubeSql.g:224:11: ( 'F' | 'f' )
            // CubeSql.g:
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
            // CubeSql.g:225:11: ( 'G' | 'g' )
            // CubeSql.g:
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
            // CubeSql.g:226:11: ( 'H' | 'h' )
            // CubeSql.g:
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
            // CubeSql.g:227:11: ( 'I' | 'i' )
            // CubeSql.g:
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
            // CubeSql.g:228:11: ( 'J' | 'j' )
            // CubeSql.g:
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
            // CubeSql.g:229:11: ( 'K' | 'k' )
            // CubeSql.g:
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
            // CubeSql.g:230:11: ( 'L' | 'l' )
            // CubeSql.g:
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
            // CubeSql.g:231:11: ( 'M' | 'm' )
            // CubeSql.g:
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
            // CubeSql.g:232:11: ( 'N' | 'n' )
            // CubeSql.g:
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
            // CubeSql.g:233:11: ( 'O' | 'o' )
            // CubeSql.g:
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
            // CubeSql.g:234:11: ( 'P' | 'p' )
            // CubeSql.g:
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
            // CubeSql.g:235:11: ( 'Q' | 'q' )
            // CubeSql.g:
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
            // CubeSql.g:236:11: ( 'R' | 'r' )
            // CubeSql.g:
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
            // CubeSql.g:237:11: ( 'S' | 's' )
            // CubeSql.g:
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
            // CubeSql.g:238:11: ( 'T' | 't' )
            // CubeSql.g:
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
            // CubeSql.g:239:11: ( 'U' | 'u' )
            // CubeSql.g:
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
            // CubeSql.g:240:11: ( 'V' | 'v' )
            // CubeSql.g:
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
            // CubeSql.g:241:11: ( 'W' | 'w' )
            // CubeSql.g:
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
            // CubeSql.g:242:11: ( 'X' | 'x' )
            // CubeSql.g:
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
            // CubeSql.g:243:11: ( 'Y' | 'y' )
            // CubeSql.g:
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
            // CubeSql.g:244:11: ( 'Z' | 'z' )
            // CubeSql.g:
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
            // CubeSql.g:246:4: ( ( ( ' ' | '\\t' | '\\f' )+ | ( '\\r\\n' | '\\r' | '\\n' ) ) )
            // CubeSql.g:246:6: ( ( ' ' | '\\t' | '\\f' )+ | ( '\\r\\n' | '\\r' | '\\n' ) )
            {
            // CubeSql.g:246:6: ( ( ' ' | '\\t' | '\\f' )+ | ( '\\r\\n' | '\\r' | '\\n' ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\t'||LA5_0=='\f'||LA5_0==' ') ) {
                alt5=1;
            }
            else if ( (LA5_0=='\n'||LA5_0=='\r') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // CubeSql.g:247:5: ( ' ' | '\\t' | '\\f' )+
                    {
                    // CubeSql.g:247:5: ( ' ' | '\\t' | '\\f' )+
                    int cnt3=0;
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0=='\t'||LA3_0=='\f'||LA3_0==' ') ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // CubeSql.g:
                    	    {
                    	    if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
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
                case 2 :
                    // CubeSql.g:250:5: ( '\\r\\n' | '\\r' | '\\n' )
                    {
                    // CubeSql.g:250:5: ( '\\r\\n' | '\\r' | '\\n' )
                    int alt4=3;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='\r') ) {
                        int LA4_1 = input.LA(2);

                        if ( (LA4_1=='\n') ) {
                            alt4=1;
                        }
                        else {
                            alt4=2;
                        }
                    }
                    else if ( (LA4_0=='\n') ) {
                        alt4=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                        throw nvae;

                    }
                    switch (alt4) {
                        case 1 :
                            // CubeSql.g:250:7: '\\r\\n'
                            {
                            match("\r\n"); 



                            }
                            break;
                        case 2 :
                            // CubeSql.g:251:9: '\\r'
                            {
                            match('\r'); 

                            }
                            break;
                        case 3 :
                            // CubeSql.g:252:9: '\\n'
                            {
                            match('\n'); 

                            }
                            break;

                    }


                    }
                    break;

            }


             _channel = HIDDEN; 

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
        // CubeSql.g:1:8: ( AND | DATASOURCE | TYPE | COLON | INI | FILE | PATH | SLASH | BACKSLASH | CREATE | CUBE | REFERENCES | DIMENSION | DIMENSION_TYPE | LIST | OF | LEVELS | LEVEL | WITH | ATTRIBUTES | ID | DESCRIPTION | AS | AT | HIERARCHY | MEASURES | NAME | LBRACE | RBRACE | DOT | COMMA | CHILDOF | QUESTMARK | UNDERSCORE | WS )
        int alt6=35;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // CubeSql.g:1:10: AND
                {
                mAND(); 


                }
                break;
            case 2 :
                // CubeSql.g:1:14: DATASOURCE
                {
                mDATASOURCE(); 


                }
                break;
            case 3 :
                // CubeSql.g:1:25: TYPE
                {
                mTYPE(); 


                }
                break;
            case 4 :
                // CubeSql.g:1:30: COLON
                {
                mCOLON(); 


                }
                break;
            case 5 :
                // CubeSql.g:1:36: INI
                {
                mINI(); 


                }
                break;
            case 6 :
                // CubeSql.g:1:40: FILE
                {
                mFILE(); 


                }
                break;
            case 7 :
                // CubeSql.g:1:45: PATH
                {
                mPATH(); 


                }
                break;
            case 8 :
                // CubeSql.g:1:50: SLASH
                {
                mSLASH(); 


                }
                break;
            case 9 :
                // CubeSql.g:1:56: BACKSLASH
                {
                mBACKSLASH(); 


                }
                break;
            case 10 :
                // CubeSql.g:1:66: CREATE
                {
                mCREATE(); 


                }
                break;
            case 11 :
                // CubeSql.g:1:73: CUBE
                {
                mCUBE(); 


                }
                break;
            case 12 :
                // CubeSql.g:1:78: REFERENCES
                {
                mREFERENCES(); 


                }
                break;
            case 13 :
                // CubeSql.g:1:89: DIMENSION
                {
                mDIMENSION(); 


                }
                break;
            case 14 :
                // CubeSql.g:1:99: DIMENSION_TYPE
                {
                mDIMENSION_TYPE(); 


                }
                break;
            case 15 :
                // CubeSql.g:1:114: LIST
                {
                mLIST(); 


                }
                break;
            case 16 :
                // CubeSql.g:1:119: OF
                {
                mOF(); 


                }
                break;
            case 17 :
                // CubeSql.g:1:122: LEVELS
                {
                mLEVELS(); 


                }
                break;
            case 18 :
                // CubeSql.g:1:129: LEVEL
                {
                mLEVEL(); 


                }
                break;
            case 19 :
                // CubeSql.g:1:135: WITH
                {
                mWITH(); 


                }
                break;
            case 20 :
                // CubeSql.g:1:140: ATTRIBUTES
                {
                mATTRIBUTES(); 


                }
                break;
            case 21 :
                // CubeSql.g:1:151: ID
                {
                mID(); 


                }
                break;
            case 22 :
                // CubeSql.g:1:154: DESCRIPTION
                {
                mDESCRIPTION(); 


                }
                break;
            case 23 :
                // CubeSql.g:1:166: AS
                {
                mAS(); 


                }
                break;
            case 24 :
                // CubeSql.g:1:169: AT
                {
                mAT(); 


                }
                break;
            case 25 :
                // CubeSql.g:1:172: HIERARCHY
                {
                mHIERARCHY(); 


                }
                break;
            case 26 :
                // CubeSql.g:1:182: MEASURES
                {
                mMEASURES(); 


                }
                break;
            case 27 :
                // CubeSql.g:1:191: NAME
                {
                mNAME(); 


                }
                break;
            case 28 :
                // CubeSql.g:1:196: LBRACE
                {
                mLBRACE(); 


                }
                break;
            case 29 :
                // CubeSql.g:1:203: RBRACE
                {
                mRBRACE(); 


                }
                break;
            case 30 :
                // CubeSql.g:1:210: DOT
                {
                mDOT(); 


                }
                break;
            case 31 :
                // CubeSql.g:1:214: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 32 :
                // CubeSql.g:1:220: CHILDOF
                {
                mCHILDOF(); 


                }
                break;
            case 33 :
                // CubeSql.g:1:228: QUESTMARK
                {
                mQUESTMARK(); 


                }
                break;
            case 34 :
                // CubeSql.g:1:238: UNDERSCORE
                {
                mUNDERSCORE(); 


                }
                break;
            case 35 :
                // CubeSql.g:1:249: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA1 dfa1 = new DFA1(this);
    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA1_eotS =
        "\5\uffff\1\7\2\uffff\2\7";
    static final String DFA1_eofS =
        "\12\uffff";
    static final String DFA1_minS =
        "\2\47\3\uffff\1\47\2\uffff\2\47";
    static final String DFA1_maxS =
        "\2\172\3\uffff\1\176\2\uffff\2\176";
    static final String DFA1_acceptS =
        "\2\uffff\1\1\1\3\1\4\1\uffff\1\2\1\5\2\uffff";
    static final String DFA1_specialS =
        "\12\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\4\6\uffff\1\1\1\3\21\uffff\32\2\6\uffff\32\2",
            "\1\6\6\uffff\2\6\21\uffff\32\5\6\uffff\32\5",
            "",
            "",
            "",
            "\1\6\5\uffff\1\11\2\6\12\11\7\uffff\32\10\4\uffff\1\11\1\uffff"+
            "\32\10\3\uffff\1\11",
            "",
            "",
            "\1\6\5\uffff\1\11\2\6\12\11\7\uffff\32\10\4\uffff\1\11\1\uffff"+
            "\32\10\3\uffff\1\11",
            "\1\6\5\uffff\1\11\2\6\12\11\7\uffff\32\10\4\uffff\1\11\1\uffff"+
            "\32\10\3\uffff\1\11"
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "()* loopback of 168:12: ( NAME | DOT | SLASH | BACKSLASH )*";
        }
    }
    static final String DFA6_eotS =
        "\1\uffff\3\31\1\uffff\3\31\2\uffff\7\31\11\uffff\1\31\1\63\1\31"+
        "\1\uffff\1\65\6\31\1\73\6\31\1\102\3\31\1\106\2\31\1\uffff\1\31"+
        "\1\uffff\4\31\1\114\1\uffff\6\31\1\uffff\3\31\1\uffff\4\31\1\132"+
        "\1\uffff\1\133\1\31\1\135\1\31\1\137\1\31\1\141\6\31\2\uffff\1\31"+
        "\1\uffff\1\31\1\uffff\1\152\1\uffff\6\31\1\162\1\31\1\uffff\1\164"+
        "\6\31\1\uffff\1\31\1\uffff\10\31\1\u0084\2\31\1\u0087\2\31\1\u008b"+
        "\1\uffff\1\u008c\1\u008d\1\uffff\2\31\1\u0090\3\uffff\1\31\1\u0092"+
        "\1\uffff\1\31\1\uffff\1\31\1\u0095\1\uffff";
    static final String DFA6_eofS =
        "\u0096\uffff";
    static final String DFA6_minS =
        "\1\11\3\47\1\uffff\3\47\2\uffff\7\47\11\uffff\3\47\1\uffff\25\47"+
        "\1\uffff\1\47\1\uffff\5\47\1\uffff\6\47\1\uffff\3\47\1\uffff\5\47"+
        "\1\uffff\15\47\2\uffff\1\47\1\uffff\1\47\1\uffff\1\47\1\uffff\10"+
        "\47\1\uffff\7\47\1\uffff\1\47\1\uffff\17\47\1\uffff\2\47\1\uffff"+
        "\3\47\3\uffff\2\47\1\uffff\1\47\1\uffff\2\47\1\uffff";
    static final String DFA6_maxS =
        "\1\175\3\176\1\uffff\3\176\2\uffff\7\176\11\uffff\3\176\1\uffff"+
        "\25\176\1\uffff\1\176\1\uffff\5\176\1\uffff\6\176\1\uffff\3\176"+
        "\1\uffff\5\176\1\uffff\15\176\2\uffff\1\176\1\uffff\1\176\1\uffff"+
        "\1\176\1\uffff\10\176\1\uffff\7\176\1\uffff\1\176\1\uffff\17\176"+
        "\1\uffff\2\176\1\uffff\3\176\3\uffff\2\176\1\uffff\1\176\1\uffff"+
        "\2\176\1\uffff";
    static final String DFA6_acceptS =
        "\4\uffff\1\4\3\uffff\1\10\1\11\7\uffff\1\34\1\35\1\36\1\37\1\40"+
        "\1\41\1\42\1\43\1\33\3\uffff\1\7\25\uffff\1\30\1\uffff\1\27\5\uffff"+
        "\1\25\6\uffff\1\20\3\uffff\1\1\5\uffff\1\5\15\uffff\1\3\1\6\1\uffff"+
        "\1\13\1\uffff\1\17\1\uffff\1\23\10\uffff\1\22\7\uffff\1\12\1\uffff"+
        "\1\21\17\uffff\1\32\2\uffff\1\15\3\uffff\1\31\1\24\1\2\2\uffff\1"+
        "\14\1\uffff\1\26\2\uffff\1\16";
    static final String DFA6_specialS =
        "\u0096\uffff}>";
    static final String[] DFA6_transitionS = {
            "\2\30\1\uffff\2\30\22\uffff\1\30\6\uffff\1\11\4\uffff\1\24\1"+
            "\uffff\1\23\1\10\12\uffff\1\4\1\26\2\uffff\1\25\2\uffff\1\1"+
            "\1\20\1\7\1\2\1\20\1\6\1\20\1\16\1\5\2\20\1\13\1\17\1\20\1\14"+
            "\2\20\1\12\1\20\1\3\2\20\1\15\3\20\4\uffff\1\27\1\uffff\1\1"+
            "\1\20\1\7\1\2\1\20\1\6\1\20\1\16\1\5\2\20\1\13\1\17\1\20\1\14"+
            "\2\20\1\12\1\20\1\3\2\20\1\15\3\20\1\21\1\uffff\1\22",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\15\37\1\32\4\37\1\36"+
            "\1\33\6\37\4\uffff\1\34\1\uffff\15\37\1\32\4\37\1\36\1\33\6"+
            "\37\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\1\40\3\37\1\42\3\37\1"+
            "\41\21\37\4\uffff\1\34\1\uffff\1\40\3\37\1\42\3\37\1\41\21\37"+
            "\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\30\37\1\43\1\37\4\uffff"+
            "\1\34\1\uffff\30\37\1\43\1\37\3\uffff\1\34",
            "",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\3\37\1\45\11\37\1\44"+
            "\14\37\4\uffff\1\34\1\uffff\3\37\1\45\11\37\1\44\14\37\3\uffff"+
            "\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\10\37\1\46\21\37\4\uffff"+
            "\1\34\1\uffff\10\37\1\46\21\37\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\21\37\1\47\2\37\1\50"+
            "\5\37\4\uffff\1\34\1\uffff\21\37\1\47\2\37\1\50\5\37\3\uffff"+
            "\1\34",
            "",
            "",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\4\37\1\51\25\37\4\uffff"+
            "\1\34\1\uffff\4\37\1\51\25\37\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\4\37\1\53\3\37\1\52\21"+
            "\37\4\uffff\1\34\1\uffff\4\37\1\53\3\37\1\52\21\37\3\uffff\1"+
            "\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\5\37\1\54\24\37\4\uffff"+
            "\1\34\1\uffff\5\37\1\54\24\37\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\10\37\1\55\21\37\4\uffff"+
            "\1\34\1\uffff\10\37\1\55\21\37\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\10\37\1\56\21\37\4\uffff"+
            "\1\34\1\uffff\10\37\1\56\21\37\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\4\37\1\57\25\37\4\uffff"+
            "\1\34\1\uffff\4\37\1\57\25\37\3\uffff\1\34",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\32\37\4\uffff\1\34\1"+
            "\uffff\32\37\3\uffff\1\34",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\3\61\1\60\26\61\4\uffff"+
            "\1\62\1\uffff\3\61\1\60\26\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\64\6\61\4\uffff"+
            "\1\62\1\uffff\23\61\1\64\6\61\3\uffff\1\62",
            "\1\35\5\uffff\1\34\2\35\12\34\7\uffff\32\37\4\uffff\1\34\1"+
            "\uffff\32\37\3\uffff\1\34",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\66\6\61\4\uffff"+
            "\1\62\1\uffff\23\61\1\66\6\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\14\61\1\67\15\61\4\uffff"+
            "\1\62\1\uffff\14\61\1\67\15\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\70\7\61\4\uffff"+
            "\1\62\1\uffff\22\61\1\70\7\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\17\61\1\71\12\61\4\uffff"+
            "\1\62\1\uffff\17\61\1\71\12\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\10\61\1\72\21\61\4\uffff"+
            "\1\62\1\uffff\10\61\1\72\21\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\13\61\1\74\16\61\4\uffff"+
            "\1\62\1\uffff\13\61\1\74\16\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\75\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\75\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\1\61\1\76\30\61\4\uffff"+
            "\1\62\1\uffff\1\61\1\76\30\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\5\61\1\77\24\61\4\uffff"+
            "\1\62\1\uffff\5\61\1\77\24\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\100\7\61\4\uffff"+
            "\1\62\1\uffff\22\61\1\100\7\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\25\61\1\101\4\61\4\uffff"+
            "\1\62\1\uffff\25\61\1\101\4\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\103\6\61\4\uffff"+
            "\1\62\1\uffff\23\61\1\103\6\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\104\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\104\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\1\105\31\61\4\uffff\1"+
            "\62\1\uffff\1\105\31\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\21\61\1\107\10\61\4\uffff"+
            "\1\62\1\uffff\21\61\1\107\10\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\1\110\31\61\4\uffff\1"+
            "\62\1\uffff\1\110\31\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\111\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\111\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\2\61\1\112\27\61\4\uffff"+
            "\1\62\1\uffff\2\61\1\112\27\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\113\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\113\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\115\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\115\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\1\116\31\61\4\uffff\1"+
            "\62\1\uffff\1\116\31\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\117\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\117\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\120\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\120\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\121\6\61\4\uffff"+
            "\1\62\1\uffff\23\61\1\121\6\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\122\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\122\25\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\7\61\1\123\22\61\4\uffff"+
            "\1\62\1\uffff\7\61\1\123\22\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\21\61\1\124\10\61\4\uffff"+
            "\1\62\1\uffff\21\61\1\124\10\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\125\7\61\4\uffff"+
            "\1\62\1\uffff\22\61\1\125\7\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\10\61\1\126\21\61\4\uffff"+
            "\1\62\1\uffff\10\61\1\126\21\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\127\7\61\4\uffff"+
            "\1\62\1\uffff\22\61\1\127\7\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\15\61\1\130\14\61\4\uffff"+
            "\1\62\1\uffff\15\61\1\130\14\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\21\61\1\131\10\61\4\uffff"+
            "\1\62\1\uffff\21\61\1\131\10\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\134\6\61\4\uffff"+
            "\1\62\1\uffff\23\61\1\134\6\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\21\61\1\136\10\61\4\uffff"+
            "\1\62\1\uffff\21\61\1\136\10\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\13\61\1\140\16\61\4\uffff"+
            "\1\62\1\uffff\13\61\1\140\16\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\1\142\31\61\4\uffff\1"+
            "\62\1\uffff\1\142\31\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\24\61\1\143\5\61\4\uffff"+
            "\1\62\1\uffff\24\61\1\143\5\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\1\61\1\144\30\61\4\uffff"+
            "\1\62\1\uffff\1\61\1\144\30\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\16\61\1\145\13\61\4\uffff"+
            "\1\62\1\uffff\16\61\1\145\13\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\146\7\61\4\uffff"+
            "\1\62\1\uffff\22\61\1\146\7\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\10\61\1\147\21\61\4\uffff"+
            "\1\62\1\uffff\10\61\1\147\21\61\3\uffff\1\62",
            "",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\150\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\150\25\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\151\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\151\25\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\153\7\61\4\uffff"+
            "\1\62\1\uffff\22\61\1\153\7\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\21\61\1\154\10\61\4\uffff"+
            "\1\62\1\uffff\21\61\1\154\10\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\21\61\1\155\10\61\4\uffff"+
            "\1\62\1\uffff\21\61\1\155\10\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\24\61\1\156\5\61\4\uffff"+
            "\1\62\1\uffff\24\61\1\156\5\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\24\61\1\157\5\61\4\uffff"+
            "\1\62\1\uffff\24\61\1\157\5\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\10\61\1\160\21\61\4\uffff"+
            "\1\62\1\uffff\10\61\1\160\21\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\17\61\1\161\12\61\4\uffff"+
            "\1\62\1\uffff\17\61\1\161\12\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\15\61\1\163\14\61\4\uffff"+
            "\1\62\1\uffff\15\61\1\163\14\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\2\61\1\165\27\61\4\uffff"+
            "\1\62\1\uffff\2\61\1\165\27\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\166\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\166\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\167\6\61\4\uffff"+
            "\1\62\1\uffff\23\61\1\167\6\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\21\61\1\170\10\61\4\uffff"+
            "\1\62\1\uffff\21\61\1\170\10\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\16\61\1\171\13\61\4\uffff"+
            "\1\62\1\uffff\16\61\1\171\13\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\172\6\61\4\uffff"+
            "\1\62\1\uffff\23\61\1\172\6\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\2\61\1\173\27\61\4\uffff"+
            "\1\62\1\uffff\2\61\1\173\27\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\7\61\1\174\22\61\4\uffff"+
            "\1\62\1\uffff\7\61\1\174\22\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\175\7\61\4\uffff"+
            "\1\62\1\uffff\22\61\1\175\7\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\176\25\61\4\uffff"+
            "\1\62\1\uffff\4\61\1\176\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\2\61\1\177\27\61\4\uffff"+
            "\1\62\1\uffff\2\61\1\177\27\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\15\61\1\u0080\14\61\4"+
            "\uffff\1\62\1\uffff\15\61\1\u0080\14\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\10\61\1\u0081\21\61\4"+
            "\uffff\1\62\1\uffff\10\61\1\u0081\21\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\u0082\25\61\4"+
            "\uffff\1\62\1\uffff\4\61\1\u0082\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\30\61\1\u0083\1\61\4"+
            "\uffff\1\62\1\uffff\30\61\1\u0083\1\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\u0085\7\61\4"+
            "\uffff\1\62\1\uffff\22\61\1\u0085\7\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\u0086\25\61\4"+
            "\uffff\1\62\1\uffff\4\61\1\u0086\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\u0088"+
            "\1\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\16\61\1\u0089\13\61\4"+
            "\uffff\1\62\1\uffff\16\61\1\u0089\13\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\22\61\1\u008a\7\61\4"+
            "\uffff\1\62\1\uffff\22\61\1\u008a\7\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\23\61\1\u008e\6\61\4"+
            "\uffff\1\62\1\uffff\23\61\1\u008e\6\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\15\61\1\u008f\14\61\4"+
            "\uffff\1\62\1\uffff\15\61\1\u008f\14\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "",
            "",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\30\61\1\u0091\1\61\4"+
            "\uffff\1\62\1\uffff\30\61\1\u0091\1\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\17\61\1\u0093\12\61\4"+
            "\uffff\1\62\1\uffff\17\61\1\u0093\12\61\3\uffff\1\62",
            "",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\4\61\1\u0094\25\61\4"+
            "\uffff\1\62\1\uffff\4\61\1\u0094\25\61\3\uffff\1\62",
            "\1\35\5\uffff\1\62\2\35\12\62\7\uffff\32\61\4\uffff\1\62\1"+
            "\uffff\32\61\3\uffff\1\62",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( AND | DATASOURCE | TYPE | COLON | INI | FILE | PATH | SLASH | BACKSLASH | CREATE | CUBE | REFERENCES | DIMENSION | DIMENSION_TYPE | LIST | OF | LEVELS | LEVEL | WITH | ATTRIBUTES | ID | DESCRIPTION | AS | AT | HIERARCHY | MEASURES | NAME | LBRACE | RBRACE | DOT | COMMA | CHILDOF | QUESTMARK | UNDERSCORE | WS );";
        }
    }
 

}