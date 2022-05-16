// $ANTLR 3.4 CubeSql.g 2022-05-15 23:04:33

  package parsermgr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CubeSqlLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int A=4;
    public static final int AND=5;
    public static final int AS=6;
    public static final int AT=7;
    public static final int ATTRIBUTES=8;
    public static final int AVG=9;
    public static final int B=10;
    public static final int BACKSLASH=11;
    public static final int BY=12;
    public static final int C=13;
    public static final int CHILDOF=14;
    public static final int COLON=15;
    public static final int COMMA=16;
    public static final int COUNT=17;
    public static final int CREATE=18;
    public static final int CUBE=19;
    public static final int D=20;
    public static final int DATASOURCE=21;
    public static final int DESCRIPTION=22;
    public static final int DIMENSION=23;
    public static final int DIMENSION_TYPE=24;
    public static final int DOT=25;
    public static final int Digit=26;
    public static final int E=27;
    public static final int F=28;
    public static final int FILE=29;
    public static final int FROM=30;
    public static final int G=31;
    public static final int GROUP=32;
    public static final int H=33;
    public static final int HIERARCHY=34;
    public static final int I=35;
    public static final int ID=36;
    public static final int INI=37;
    public static final int J=38;
    public static final int K=39;
    public static final int L=40;
    public static final int LBRACE=41;
    public static final int LEVEL=42;
    public static final int LEVELS=43;
    public static final int LIST=44;
    public static final int Letter=45;
    public static final int M=46;
    public static final int MAX=47;
    public static final int MEASURES=48;
    public static final int MIN=49;
    public static final int N=50;
    public static final int NAME=51;
    public static final int O=52;
    public static final int OF=53;
    public static final int OR=54;
    public static final int P=55;
    public static final int PATH=56;
    public static final int Q=57;
    public static final int QUESTMARK=58;
    public static final int R=59;
    public static final int RBRACE=60;
    public static final int REFERENCES=61;
    public static final int S=62;
    public static final int SELECT=63;
    public static final int SLASH=64;
    public static final int SUM=65;
    public static final int T=66;
    public static final int TYPE=67;
    public static final int U=68;
    public static final int UNDERSCORE=69;
    public static final int V=70;
    public static final int W=71;
    public static final int WHERE=72;
    public static final int WITH=73;
    public static final int WS=74;
    public static final int X=75;
    public static final int Y=76;
    public static final int Z=77;

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

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:6:7: ( '\"' )
            // CubeSql.g:6:9: '\"'
            {
            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:7:7: ( '(' )
            // CubeSql.g:7:9: '('
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
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:8:7: ( ')' )
            // CubeSql.g:8:9: ')'
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
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:9:7: ( '<' )
            // CubeSql.g:9:9: '<'
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
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:10:7: ( '<=' )
            // CubeSql.g:10:9: '<='
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
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:11:7: ( '=' )
            // CubeSql.g:11:9: '='
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
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:12:7: ( '>=' )
            // CubeSql.g:12:9: '>='
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
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:13:7: ( 'LIKE' )
            // CubeSql.g:13:9: 'LIKE'
            {
            match("LIKE"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:215:4: ( O R )
            // CubeSql.g:215:6: O R
            {
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
    // $ANTLR end "OR"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:217:5: ( A N D )
            // CubeSql.g:217:7: A N D
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
            // CubeSql.g:219:11: ( D A T A S O U R C E )
            // CubeSql.g:219:13: D A T A S O U R C E
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
            // CubeSql.g:221:5: ( T Y P E )
            // CubeSql.g:221:7: T Y P E
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
            // CubeSql.g:223:6: ( ':' )
            // CubeSql.g:223:8: ':'
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
            // CubeSql.g:225:4: ( I N I )
            // CubeSql.g:225:6: I N I
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
            // CubeSql.g:227:5: ( F I L E )
            // CubeSql.g:227:7: F I L E
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
            // CubeSql.g:229:5: ( NAME ( NAME | DOT | SLASH | BACKSLASH )* DOT NAME )
            // CubeSql.g:229:7: NAME ( NAME | DOT | SLASH | BACKSLASH )* DOT NAME
            {
            mNAME(); 


            // CubeSql.g:229:12: ( NAME | DOT | SLASH | BACKSLASH )*
            loop1:
            do {
                int alt1=5;
                alt1 = dfa1.predict(input);
                switch (alt1) {
            	case 1 :
            	    // CubeSql.g:229:13: NAME
            	    {
            	    mNAME(); 


            	    }
            	    break;
            	case 2 :
            	    // CubeSql.g:229:20: DOT
            	    {
            	    mDOT(); 


            	    }
            	    break;
            	case 3 :
            	    // CubeSql.g:229:26: SLASH
            	    {
            	    mSLASH(); 


            	    }
            	    break;
            	case 4 :
            	    // CubeSql.g:229:34: BACKSLASH
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
            // CubeSql.g:230:6: ( '/' )
            // CubeSql.g:230:8: '/'
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
            // CubeSql.g:231:10: ( '\\'' )
            // CubeSql.g:231:12: '\\''
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
            // CubeSql.g:234:7: ( C R E A T E )
            // CubeSql.g:234:9: C R E A T E
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
            // CubeSql.g:236:6: ( C U B E )
            // CubeSql.g:236:9: C U B E
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
            // CubeSql.g:238:11: ( R E F E R E N C E S )
            // CubeSql.g:238:13: R E F E R E N C E S
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
            // CubeSql.g:240:11: ( D I M E N S I O N )
            // CubeSql.g:240:13: D I M E N S I O N
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
            // CubeSql.g:242:15: ( D I M E N S I O N UNDERSCORE T Y P E )
            // CubeSql.g:242:17: D I M E N S I O N UNDERSCORE T Y P E
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
            // CubeSql.g:244:5: ( L I S T )
            // CubeSql.g:244:7: L I S T
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
            // CubeSql.g:246:3: ( O F )
            // CubeSql.g:246:5: O F
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
            // CubeSql.g:248:7: ( L E V E L S )
            // CubeSql.g:248:8: L E V E L S
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
            // CubeSql.g:250:6: ( L E V E L )
            // CubeSql.g:250:8: L E V E L
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
            // CubeSql.g:252:5: ( W I T H )
            // CubeSql.g:252:7: W I T H
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
            // CubeSql.g:254:11: ( A T T R I B U T E S )
            // CubeSql.g:254:13: A T T R I B U T E S
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
            // CubeSql.g:256:3: ( I D )
            // CubeSql.g:256:5: I D
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
            // CubeSql.g:258:12: ( D E S C R I P T I O N )
            // CubeSql.g:258:14: D E S C R I P T I O N
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
            // CubeSql.g:260:3: ( A S )
            // CubeSql.g:260:5: A S
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
            // CubeSql.g:262:4: ( A T )
            // CubeSql.g:262:5: A T
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
            // CubeSql.g:264:10: ( H I E R A R C H Y )
            // CubeSql.g:264:12: H I E R A R C H Y
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

    // $ANTLR start "SELECT"
    public final void mSELECT() throws RecognitionException {
        try {
            int _type = SELECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:266:8: ( S E L E C T )
            // CubeSql.g:266:10: S E L E C T
            {
            mS(); 


            mE(); 


            mL(); 


            mE(); 


            mC(); 


            mT(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SELECT"

    // $ANTLR start "AVG"
    public final void mAVG() throws RecognitionException {
        try {
            int _type = AVG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:268:5: ( A V G )
            // CubeSql.g:268:7: A V G
            {
            mA(); 


            mV(); 


            mG(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AVG"

    // $ANTLR start "SUM"
    public final void mSUM() throws RecognitionException {
        try {
            int _type = SUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:270:5: ( S U M )
            // CubeSql.g:270:7: S U M
            {
            mS(); 


            mU(); 


            mM(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SUM"

    // $ANTLR start "MAX"
    public final void mMAX() throws RecognitionException {
        try {
            int _type = MAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:272:5: ( M A X )
            // CubeSql.g:272:7: M A X
            {
            mM(); 


            mA(); 


            mX(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MAX"

    // $ANTLR start "MIN"
    public final void mMIN() throws RecognitionException {
        try {
            int _type = MIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:274:5: ( M I N )
            // CubeSql.g:274:7: M I N
            {
            mM(); 


            mI(); 


            mN(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MIN"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:276:7: ( C O U N T )
            // CubeSql.g:276:9: C O U N T
            {
            mC(); 


            mO(); 


            mU(); 


            mN(); 


            mT(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:278:7: ( W H E R E )
            // CubeSql.g:278:9: W H E R E
            {
            mW(); 


            mH(); 


            mE(); 


            mR(); 


            mE(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHERE"

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:280:6: ( F R O M )
            // CubeSql.g:280:8: F R O M
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

    // $ANTLR start "GROUP"
    public final void mGROUP() throws RecognitionException {
        try {
            int _type = GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:282:7: ( G R O U P )
            // CubeSql.g:282:9: G R O U P
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
            // CubeSql.g:284:3: ( B Y )
            // CubeSql.g:284:5: B Y
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

    // $ANTLR start "MEASURES"
    public final void mMEASURES() throws RecognitionException {
        try {
            int _type = MEASURES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // CubeSql.g:286:10: ( M E A S U R E S )
            // CubeSql.g:286:12: M E A S U R E S
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
            // CubeSql.g:288:5: ( Letter ( Letter | Digit | '_' | '-' | '~' )* )
            // CubeSql.g:288:7: Letter ( Letter | Digit | '_' | '-' | '~' )*
            {
            mLetter(); 


            // CubeSql.g:288:14: ( Letter | Digit | '_' | '-' | '~' )*
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
            // CubeSql.g:290:7: ( '{' )
            // CubeSql.g:290:8: '{'
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
            // CubeSql.g:291:7: ( '}' )
            // CubeSql.g:291:8: '}'
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
            // CubeSql.g:292:4: ( '.' )
            // CubeSql.g:292:5: '.'
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
            // CubeSql.g:293:6: ( ',' )
            // CubeSql.g:293:7: ','
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
            // CubeSql.g:294:8: ( '>' )
            // CubeSql.g:294:9: '>'
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
            // CubeSql.g:295:10: ( ';' )
            // CubeSql.g:295:11: ';'
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
            // CubeSql.g:296:11: ( '_' )
            // CubeSql.g:296:12: '_'
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
            // CubeSql.g:298:16: ( '0' .. '9' )
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
            // CubeSql.g:299:17: ( 'a' .. 'z' | 'A' .. 'Z' )
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
            // CubeSql.g:300:11: ( 'A' | 'a' )
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
            // CubeSql.g:301:11: ( 'B' | 'b' )
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
            // CubeSql.g:302:11: ( 'C' | 'c' )
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
            // CubeSql.g:303:11: ( 'D' | 'd' )
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
            // CubeSql.g:304:11: ( 'E' | 'e' )
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
            // CubeSql.g:305:11: ( 'F' | 'f' )
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
            // CubeSql.g:306:11: ( 'G' | 'g' )
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
            // CubeSql.g:307:11: ( 'H' | 'h' )
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
            // CubeSql.g:308:11: ( 'I' | 'i' )
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
            // CubeSql.g:309:11: ( 'J' | 'j' )
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
            // CubeSql.g:310:11: ( 'K' | 'k' )
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
            // CubeSql.g:311:11: ( 'L' | 'l' )
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
            // CubeSql.g:312:11: ( 'M' | 'm' )
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
            // CubeSql.g:313:11: ( 'N' | 'n' )
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
            // CubeSql.g:314:11: ( 'O' | 'o' )
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
            // CubeSql.g:315:11: ( 'P' | 'p' )
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
            // CubeSql.g:316:11: ( 'Q' | 'q' )
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
            // CubeSql.g:317:11: ( 'R' | 'r' )
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
            // CubeSql.g:318:11: ( 'S' | 's' )
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
            // CubeSql.g:319:11: ( 'T' | 't' )
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
            // CubeSql.g:320:11: ( 'U' | 'u' )
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
            // CubeSql.g:321:11: ( 'V' | 'v' )
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
            // CubeSql.g:322:11: ( 'W' | 'w' )
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
            // CubeSql.g:323:11: ( 'X' | 'x' )
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
            // CubeSql.g:324:11: ( 'Y' | 'y' )
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
            // CubeSql.g:325:11: ( 'Z' | 'z' )
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
            // CubeSql.g:327:4: ( ( ( ' ' | '\\t' | '\\f' )+ | ( '\\r\\n' | '\\r' | '\\n' ) ) )
            // CubeSql.g:327:6: ( ( ' ' | '\\t' | '\\f' )+ | ( '\\r\\n' | '\\r' | '\\n' ) )
            {
            // CubeSql.g:327:6: ( ( ' ' | '\\t' | '\\f' )+ | ( '\\r\\n' | '\\r' | '\\n' ) )
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
                    // CubeSql.g:328:5: ( ' ' | '\\t' | '\\f' )+
                    {
                    // CubeSql.g:328:5: ( ' ' | '\\t' | '\\f' )+
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
                    // CubeSql.g:331:5: ( '\\r\\n' | '\\r' | '\\n' )
                    {
                    // CubeSql.g:331:5: ( '\\r\\n' | '\\r' | '\\n' )
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
                            // CubeSql.g:331:7: '\\r\\n'
                            {
                            match("\r\n"); 



                            }
                            break;
                        case 2 :
                            // CubeSql.g:332:9: '\\r'
                            {
                            match('\r'); 

                            }
                            break;
                        case 3 :
                            // CubeSql.g:333:9: '\\n'
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
        // CubeSql.g:1:8: ( T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | OR | AND | DATASOURCE | TYPE | COLON | INI | FILE | PATH | SLASH | BACKSLASH | CREATE | CUBE | REFERENCES | DIMENSION | DIMENSION_TYPE | LIST | OF | LEVELS | LEVEL | WITH | ATTRIBUTES | ID | DESCRIPTION | AS | AT | HIERARCHY | SELECT | AVG | SUM | MAX | MIN | COUNT | WHERE | FROM | GROUP | BY | MEASURES | NAME | LBRACE | RBRACE | DOT | COMMA | CHILDOF | QUESTMARK | UNDERSCORE | WS )
        int alt6=54;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // CubeSql.g:1:10: T__78
                {
                mT__78(); 


                }
                break;
            case 2 :
                // CubeSql.g:1:16: T__79
                {
                mT__79(); 


                }
                break;
            case 3 :
                // CubeSql.g:1:22: T__80
                {
                mT__80(); 


                }
                break;
            case 4 :
                // CubeSql.g:1:28: T__81
                {
                mT__81(); 


                }
                break;
            case 5 :
                // CubeSql.g:1:34: T__82
                {
                mT__82(); 


                }
                break;
            case 6 :
                // CubeSql.g:1:40: T__83
                {
                mT__83(); 


                }
                break;
            case 7 :
                // CubeSql.g:1:46: T__84
                {
                mT__84(); 


                }
                break;
            case 8 :
                // CubeSql.g:1:52: T__85
                {
                mT__85(); 


                }
                break;
            case 9 :
                // CubeSql.g:1:58: OR
                {
                mOR(); 


                }
                break;
            case 10 :
                // CubeSql.g:1:61: AND
                {
                mAND(); 


                }
                break;
            case 11 :
                // CubeSql.g:1:65: DATASOURCE
                {
                mDATASOURCE(); 


                }
                break;
            case 12 :
                // CubeSql.g:1:76: TYPE
                {
                mTYPE(); 


                }
                break;
            case 13 :
                // CubeSql.g:1:81: COLON
                {
                mCOLON(); 


                }
                break;
            case 14 :
                // CubeSql.g:1:87: INI
                {
                mINI(); 


                }
                break;
            case 15 :
                // CubeSql.g:1:91: FILE
                {
                mFILE(); 


                }
                break;
            case 16 :
                // CubeSql.g:1:96: PATH
                {
                mPATH(); 


                }
                break;
            case 17 :
                // CubeSql.g:1:101: SLASH
                {
                mSLASH(); 


                }
                break;
            case 18 :
                // CubeSql.g:1:107: BACKSLASH
                {
                mBACKSLASH(); 


                }
                break;
            case 19 :
                // CubeSql.g:1:117: CREATE
                {
                mCREATE(); 


                }
                break;
            case 20 :
                // CubeSql.g:1:124: CUBE
                {
                mCUBE(); 


                }
                break;
            case 21 :
                // CubeSql.g:1:129: REFERENCES
                {
                mREFERENCES(); 


                }
                break;
            case 22 :
                // CubeSql.g:1:140: DIMENSION
                {
                mDIMENSION(); 


                }
                break;
            case 23 :
                // CubeSql.g:1:150: DIMENSION_TYPE
                {
                mDIMENSION_TYPE(); 


                }
                break;
            case 24 :
                // CubeSql.g:1:165: LIST
                {
                mLIST(); 


                }
                break;
            case 25 :
                // CubeSql.g:1:170: OF
                {
                mOF(); 


                }
                break;
            case 26 :
                // CubeSql.g:1:173: LEVELS
                {
                mLEVELS(); 


                }
                break;
            case 27 :
                // CubeSql.g:1:180: LEVEL
                {
                mLEVEL(); 


                }
                break;
            case 28 :
                // CubeSql.g:1:186: WITH
                {
                mWITH(); 


                }
                break;
            case 29 :
                // CubeSql.g:1:191: ATTRIBUTES
                {
                mATTRIBUTES(); 


                }
                break;
            case 30 :
                // CubeSql.g:1:202: ID
                {
                mID(); 


                }
                break;
            case 31 :
                // CubeSql.g:1:205: DESCRIPTION
                {
                mDESCRIPTION(); 


                }
                break;
            case 32 :
                // CubeSql.g:1:217: AS
                {
                mAS(); 


                }
                break;
            case 33 :
                // CubeSql.g:1:220: AT
                {
                mAT(); 


                }
                break;
            case 34 :
                // CubeSql.g:1:223: HIERARCHY
                {
                mHIERARCHY(); 


                }
                break;
            case 35 :
                // CubeSql.g:1:233: SELECT
                {
                mSELECT(); 


                }
                break;
            case 36 :
                // CubeSql.g:1:240: AVG
                {
                mAVG(); 


                }
                break;
            case 37 :
                // CubeSql.g:1:244: SUM
                {
                mSUM(); 


                }
                break;
            case 38 :
                // CubeSql.g:1:248: MAX
                {
                mMAX(); 


                }
                break;
            case 39 :
                // CubeSql.g:1:252: MIN
                {
                mMIN(); 


                }
                break;
            case 40 :
                // CubeSql.g:1:256: COUNT
                {
                mCOUNT(); 


                }
                break;
            case 41 :
                // CubeSql.g:1:262: WHERE
                {
                mWHERE(); 


                }
                break;
            case 42 :
                // CubeSql.g:1:268: FROM
                {
                mFROM(); 


                }
                break;
            case 43 :
                // CubeSql.g:1:273: GROUP
                {
                mGROUP(); 


                }
                break;
            case 44 :
                // CubeSql.g:1:279: BY
                {
                mBY(); 


                }
                break;
            case 45 :
                // CubeSql.g:1:282: MEASURES
                {
                mMEASURES(); 


                }
                break;
            case 46 :
                // CubeSql.g:1:291: NAME
                {
                mNAME(); 


                }
                break;
            case 47 :
                // CubeSql.g:1:296: LBRACE
                {
                mLBRACE(); 


                }
                break;
            case 48 :
                // CubeSql.g:1:303: RBRACE
                {
                mRBRACE(); 


                }
                break;
            case 49 :
                // CubeSql.g:1:310: DOT
                {
                mDOT(); 


                }
                break;
            case 50 :
                // CubeSql.g:1:314: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 51 :
                // CubeSql.g:1:320: CHILDOF
                {
                mCHILDOF(); 


                }
                break;
            case 52 :
                // CubeSql.g:1:328: QUESTMARK
                {
                mQUESTMARK(); 


                }
                break;
            case 53 :
                // CubeSql.g:1:338: UNDERSCORE
                {
                mUNDERSCORE(); 


                }
                break;
            case 54 :
                // CubeSql.g:1:349: WS
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
            return "()* loopback of 229:12: ( NAME | DOT | SLASH | BACKSLASH )*";
        }
    }
    static final String DFA6_eotS =
        "\4\uffff\1\43\1\uffff\1\45\5\47\1\uffff\3\47\2\uffff\11\47\13\uffff"+
        "\1\47\1\uffff\2\47\1\uffff\2\47\1\116\1\117\1\47\1\121\1\123\6\47"+
        "\1\132\17\47\1\152\5\47\2\uffff\1\156\1\uffff\1\47\1\uffff\1\160"+
        "\4\47\1\165\1\uffff\12\47\1\u0080\1\u0081\1\u0082\2\47\1\uffff\1"+
        "\u0085\1\u0086\1\47\1\uffff\1\47\1\uffff\3\47\1\u008c\1\uffff\1"+
        "\u008d\1\u008e\1\47\1\u0090\2\47\1\u0093\3\47\3\uffff\2\47\2\uffff"+
        "\1\u0099\4\47\3\uffff\1\47\1\uffff\1\u00a0\1\47\1\uffff\1\u00a2"+
        "\3\47\1\u00a6\1\uffff\1\u00a7\4\47\1\u00ac\1\uffff\1\47\1\uffff"+
        "\1\47\1\u00af\1\47\2\uffff\4\47\1\uffff\2\47\1\uffff\7\47\1\u00be"+
        "\2\47\1\u00c1\2\47\1\u00c5\1\uffff\1\u00c6\1\u00c7\1\uffff\2\47"+
        "\1\u00ca\3\uffff\1\47\1\u00cc\1\uffff\1\47\1\uffff\1\47\1\u00cf"+
        "\1\uffff";
    static final String DFA6_eofS =
        "\u00d0\uffff";
    static final String DFA6_minS =
        "\1\11\3\uffff\1\75\1\uffff\1\75\5\47\1\uffff\3\47\2\uffff\11\47"+
        "\13\uffff\1\47\1\uffff\2\47\1\uffff\43\47\2\uffff\1\47\1\uffff\1"+
        "\47\1\uffff\6\47\1\uffff\17\47\1\uffff\3\47\1\uffff\1\47\1\uffff"+
        "\4\47\1\uffff\12\47\3\uffff\2\47\2\uffff\5\47\3\uffff\1\47\1\uffff"+
        "\2\47\1\uffff\5\47\1\uffff\6\47\1\uffff\1\47\1\uffff\3\47\2\uffff"+
        "\4\47\1\uffff\2\47\1\uffff\16\47\1\uffff\2\47\1\uffff\3\47\3\uffff"+
        "\2\47\1\uffff\1\47\1\uffff\2\47\1\uffff";
    static final String DFA6_maxS =
        "\1\175\3\uffff\1\75\1\uffff\1\75\5\176\1\uffff\3\176\2\uffff\11"+
        "\176\13\uffff\1\176\1\uffff\2\176\1\uffff\43\176\2\uffff\1\176\1"+
        "\uffff\1\176\1\uffff\6\176\1\uffff\17\176\1\uffff\3\176\1\uffff"+
        "\1\176\1\uffff\4\176\1\uffff\12\176\3\uffff\2\176\2\uffff\5\176"+
        "\3\uffff\1\176\1\uffff\2\176\1\uffff\5\176\1\uffff\6\176\1\uffff"+
        "\1\176\1\uffff\3\176\2\uffff\4\176\1\uffff\2\176\1\uffff\16\176"+
        "\1\uffff\2\176\1\uffff\3\176\3\uffff\2\176\1\uffff\1\176\1\uffff"+
        "\2\176\1\uffff";
    static final String DFA6_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\uffff\1\6\6\uffff\1\15\3\uffff\1\21\1\22"+
        "\11\uffff\1\57\1\60\1\61\1\62\1\64\1\65\1\66\1\5\1\4\1\7\1\63\1"+
        "\uffff\1\56\2\uffff\1\20\43\uffff\1\11\1\31\1\uffff\1\41\1\uffff"+
        "\1\40\6\uffff\1\36\17\uffff\1\54\3\uffff\1\12\1\uffff\1\44\4\uffff"+
        "\1\16\12\uffff\1\45\1\46\1\47\2\uffff\1\10\1\30\5\uffff\1\14\1\17"+
        "\1\52\1\uffff\1\24\2\uffff\1\34\5\uffff\1\33\6\uffff\1\50\1\uffff"+
        "\1\51\3\uffff\1\53\1\32\4\uffff\1\23\2\uffff\1\43\16\uffff\1\55"+
        "\2\uffff\1\26\3\uffff\1\42\1\35\1\13\2\uffff\1\25\1\uffff\1\37\2"+
        "\uffff\1\27";
    static final String DFA6_specialS =
        "\u00d0\uffff}>";
    static final String[] DFA6_transitionS = {
            "\2\41\1\uffff\2\41\22\uffff\1\41\1\uffff\1\1\4\uffff\1\21\1"+
            "\2\1\3\2\uffff\1\36\1\uffff\1\35\1\20\12\uffff\1\14\1\37\1\4"+
            "\1\5\1\6\2\uffff\1\11\1\31\1\17\1\12\1\32\1\16\1\30\1\25\1\15"+
            "\2\32\1\7\1\27\1\32\1\10\2\32\1\22\1\26\1\13\2\32\1\24\3\32"+
            "\4\uffff\1\40\1\uffff\1\11\1\31\1\17\1\12\1\32\1\16\1\30\1\25"+
            "\1\15\2\32\1\23\1\27\1\32\1\10\2\32\1\22\1\26\1\13\2\32\1\24"+
            "\3\32\1\33\1\uffff\1\34",
            "",
            "",
            "",
            "\1\42",
            "",
            "\1\44",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\4\54\1\53\3\54\1\46\21"+
            "\54\4\uffff\1\51\1\uffff\4\54\1\53\3\54\1\50\21\54\3\uffff\1"+
            "\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\5\54\1\56\13\54\1\55"+
            "\10\54\4\uffff\1\51\1\uffff\5\54\1\56\13\54\1\55\10\54\3\uffff"+
            "\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\15\54\1\57\4\54\1\61"+
            "\1\60\1\54\1\62\4\54\4\uffff\1\51\1\uffff\15\54\1\57\4\54\1"+
            "\61\1\60\1\54\1\62\4\54\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\1\63\3\54\1\65\3\54\1"+
            "\64\21\54\4\uffff\1\51\1\uffff\1\63\3\54\1\65\3\54\1\64\21\54"+
            "\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\30\54\1\66\1\54\4\uffff"+
            "\1\51\1\uffff\30\54\1\66\1\54\3\uffff\1\51",
            "",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\3\54\1\70\11\54\1\67"+
            "\14\54\4\uffff\1\51\1\uffff\3\54\1\70\11\54\1\67\14\54\3\uffff"+
            "\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\10\54\1\71\10\54\1\72"+
            "\10\54\4\uffff\1\51\1\uffff\10\54\1\71\10\54\1\72\10\54\3\uffff"+
            "\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\16\54\1\75\2\54\1\73"+
            "\2\54\1\74\5\54\4\uffff\1\51\1\uffff\16\54\1\75\2\54\1\73\2"+
            "\54\1\74\5\54\3\uffff\1\51",
            "",
            "",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\4\54\1\76\25\54\4\uffff"+
            "\1\51\1\uffff\4\54\1\76\25\54\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\4\54\1\53\3\54\1\50\21"+
            "\54\4\uffff\1\51\1\uffff\4\54\1\53\3\54\1\50\21\54\3\uffff\1"+
            "\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\7\54\1\100\1\77\21\54"+
            "\4\uffff\1\51\1\uffff\7\54\1\100\1\77\21\54\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\10\54\1\101\21\54\4\uffff"+
            "\1\51\1\uffff\10\54\1\101\21\54\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\4\54\1\102\17\54\1\103"+
            "\5\54\4\uffff\1\51\1\uffff\4\54\1\102\17\54\1\103\5\54\3\uffff"+
            "\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\1\104\3\54\1\106\3\54"+
            "\1\105\21\54\4\uffff\1\51\1\uffff\1\104\3\54\1\106\3\54\1\105"+
            "\21\54\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\21\54\1\107\10\54\4\uffff"+
            "\1\51\1\uffff\21\54\1\107\10\54\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\30\54\1\110\1\54\4\uffff"+
            "\1\51\1\uffff\30\54\1\110\1\54\3\uffff\1\51",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\32\54\4\uffff\1\51\1"+
            "\uffff\32\54\3\uffff\1\51",
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
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\12\114\1\111\7\114"+
            "\1\112\7\114\4\uffff\1\113\1\uffff\22\114\1\112\7\114\3\uffff"+
            "\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\112\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\112\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\51\2\52\12\51\7\uffff\32\54\4\uffff\1\51\1"+
            "\uffff\32\54\3\uffff\1\51",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\25\114\1\115\4\114"+
            "\4\uffff\1\113\1\uffff\25\114\1\115\4\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\3\114\1\120\26\114"+
            "\4\uffff\1\113\1\uffff\3\114\1\120\26\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\122\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\122\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\6\114\1\124\23\114"+
            "\4\uffff\1\113\1\uffff\6\114\1\124\23\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\125\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\125\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\14\114\1\126\15\114"+
            "\4\uffff\1\113\1\uffff\14\114\1\126\15\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\127\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\127\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\17\114\1\130\12\114"+
            "\4\uffff\1\113\1\uffff\17\114\1\130\12\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\10\114\1\131\21\114"+
            "\4\uffff\1\113\1\uffff\10\114\1\131\21\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\13\114\1\133\16\114"+
            "\4\uffff\1\113\1\uffff\13\114\1\133\16\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\16\114\1\134\13\114"+
            "\4\uffff\1\113\1\uffff\16\114\1\134\13\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\135\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\135\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\1\114\1\136\30\114"+
            "\4\uffff\1\113\1\uffff\1\114\1\136\30\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\24\114\1\137\5\114"+
            "\4\uffff\1\113\1\uffff\24\114\1\137\5\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\5\114\1\140\24\114"+
            "\4\uffff\1\113\1\uffff\5\114\1\140\24\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\141\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\141\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\142\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\142\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\143\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\143\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\13\114\1\144\16\114"+
            "\4\uffff\1\113\1\uffff\13\114\1\144\16\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\14\114\1\145\15\114"+
            "\4\uffff\1\113\1\uffff\14\114\1\145\15\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\27\114\1\146\2\114"+
            "\4\uffff\1\113\1\uffff\27\114\1\146\2\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\15\114\1\147\14\114"+
            "\4\uffff\1\113\1\uffff\15\114\1\147\14\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\1\150\31\114\4\uffff"+
            "\1\113\1\uffff\1\150\31\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\16\114\1\151\13\114"+
            "\4\uffff\1\113\1\uffff\16\114\1\151\13\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\153\25\114"+
            "\4\uffff\1\113\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\154\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\154\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\155\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\155\25\114\3\uffff\1\113",
            "",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\157\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\157\10\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\1\161\31\114\4\uffff"+
            "\1\113\1\uffff\1\161\31\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\162\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\162\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\2\114\1\163\27\114"+
            "\4\uffff\1\113\1\uffff\2\114\1\163\27\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\164\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\164\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\166\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\166\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\14\114\1\167\15\114"+
            "\4\uffff\1\113\1\uffff\14\114\1\167\15\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\1\170\31\114\4\uffff"+
            "\1\113\1\uffff\1\170\31\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\171\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\171\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\15\114\1\172\14\114"+
            "\4\uffff\1\113\1\uffff\15\114\1\172\14\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\173\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\173\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\7\114\1\174\22\114"+
            "\4\uffff\1\113\1\uffff\7\114\1\174\22\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\175\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\175\10\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\176\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\176\10\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\177\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\177\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\u0083\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\u0083\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\24\114\1\u0084\5\114"+
            "\4\uffff\1\113\1\uffff\24\114\1\u0084\5\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\13\114\1\u0087\16\114"+
            "\4\uffff\1\113\1\uffff\13\114\1\u0087\16\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\10\114\1\u0088\21\114"+
            "\4\uffff\1\113\1\uffff\10\114\1\u0088\21\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\u0089\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\u0089\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\15\114\1\u008a\14\114"+
            "\4\uffff\1\113\1\uffff\15\114\1\u008a\14\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\u008b\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\u008b\10\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\u008f\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\u008f\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\u0091\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\u0091\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\u0092\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\u0092\10\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u0094\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u0094\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\1\u0095\31\114\4\uffff"+
            "\1\113\1\uffff\1\u0095\31\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\2\114\1\u0096\27\114"+
            "\4\uffff\1\113\1\uffff\2\114\1\u0096\27\114\3\uffff\1\113",
            "",
            "",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\24\114\1\u0097\5\114"+
            "\4\uffff\1\113\1\uffff\24\114\1\u0097\5\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\17\114\1\u0098\12\114"+
            "\4\uffff\1\113\1\uffff\17\114\1\u0098\12\114\3\uffff\1\113",
            "",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\u009a\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\u009a\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\1\114\1\u009b\30\114"+
            "\4\uffff\1\113\1\uffff\1\114\1\u009b\30\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\16\114\1\u009c\13\114"+
            "\4\uffff\1\113\1\uffff\16\114\1\u009c\13\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\u009d\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\u009d\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\10\114\1\u009e\21\114"+
            "\4\uffff\1\113\1\uffff\10\114\1\u009e\21\114\3\uffff\1\113",
            "",
            "",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u009f\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u009f\25\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u00a1\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u00a1\25\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\u00a3\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\u00a3\10\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\u00a4\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\u00a4\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\u00a5\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\u00a5\10\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\24\114\1\u00a8\5\114"+
            "\4\uffff\1\113\1\uffff\24\114\1\u00a8\5\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\24\114\1\u00a9\5\114"+
            "\4\uffff\1\113\1\uffff\24\114\1\u00a9\5\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\10\114\1\u00aa\21\114"+
            "\4\uffff\1\113\1\uffff\10\114\1\u00aa\21\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\17\114\1\u00ab\12\114"+
            "\4\uffff\1\113\1\uffff\17\114\1\u00ab\12\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\15\114\1\u00ad\14\114"+
            "\4\uffff\1\113\1\uffff\15\114\1\u00ad\14\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\2\114\1\u00ae\27\114"+
            "\4\uffff\1\113\1\uffff\2\114\1\u00ae\27\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u00b0\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u00b0\25\114\3\uffff\1\113",
            "",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\u00b1\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\u00b1\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\21\114\1\u00b2\10\114"+
            "\4\uffff\1\113\1\uffff\21\114\1\u00b2\10\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\16\114\1\u00b3\13\114"+
            "\4\uffff\1\113\1\uffff\16\114\1\u00b3\13\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\u00b4\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\u00b4\6\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\2\114\1\u00b5\27\114"+
            "\4\uffff\1\113\1\uffff\2\114\1\u00b5\27\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\7\114\1\u00b6\22\114"+
            "\4\uffff\1\113\1\uffff\7\114\1\u00b6\22\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\u00b7\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\u00b7\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u00b8\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u00b8\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\2\114\1\u00b9\27\114"+
            "\4\uffff\1\113\1\uffff\2\114\1\u00b9\27\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\15\114\1\u00ba\14\114"+
            "\4\uffff\1\113\1\uffff\15\114\1\u00ba\14\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\10\114\1\u00bb\21\114"+
            "\4\uffff\1\113\1\uffff\10\114\1\u00bb\21\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u00bc\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u00bc\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\30\114\1\u00bd\1\114"+
            "\4\uffff\1\113\1\uffff\30\114\1\u00bd\1\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\u00bf\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\u00bf\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u00c0\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u00c0\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\u00c2"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\16\114\1\u00c3\13\114"+
            "\4\uffff\1\113\1\uffff\16\114\1\u00c3\13\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\22\114\1\u00c4\7\114"+
            "\4\uffff\1\113\1\uffff\22\114\1\u00c4\7\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\23\114\1\u00c8\6\114"+
            "\4\uffff\1\113\1\uffff\23\114\1\u00c8\6\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\15\114\1\u00c9\14\114"+
            "\4\uffff\1\113\1\uffff\15\114\1\u00c9\14\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\30\114\1\u00cb\1\114"+
            "\4\uffff\1\113\1\uffff\30\114\1\u00cb\1\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\17\114\1\u00cd\12\114"+
            "\4\uffff\1\113\1\uffff\17\114\1\u00cd\12\114\3\uffff\1\113",
            "",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\4\114\1\u00ce\25\114"+
            "\4\uffff\1\113\1\uffff\4\114\1\u00ce\25\114\3\uffff\1\113",
            "\1\52\5\uffff\1\113\2\52\12\113\7\uffff\32\114\4\uffff\1\113"+
            "\1\uffff\32\114\3\uffff\1\113",
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
            return "1:1: Tokens : ( T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | OR | AND | DATASOURCE | TYPE | COLON | INI | FILE | PATH | SLASH | BACKSLASH | CREATE | CUBE | REFERENCES | DIMENSION | DIMENSION_TYPE | LIST | OF | LEVELS | LEVEL | WITH | ATTRIBUTES | ID | DESCRIPTION | AS | AT | HIERARCHY | SELECT | AVG | SUM | MAX | MIN | COUNT | WHERE | FROM | GROUP | BY | MEASURES | NAME | LBRACE | RBRACE | DOT | COMMA | CHILDOF | QUESTMARK | UNDERSCORE | WS );";
        }
    }
 

}