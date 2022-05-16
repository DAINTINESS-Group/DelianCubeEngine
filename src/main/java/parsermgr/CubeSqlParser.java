// $ANTLR 3.4 CubeSql.g 2022-05-15 23:04:33

  package parsermgr;
  
  import java.util.ArrayList;
  import java.util.HashMap;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CubeSqlParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "AND", "AS", "AT", "ATTRIBUTES", "AVG", "B", "BACKSLASH", "BY", "C", "CHILDOF", "COLON", "COMMA", "COUNT", "CREATE", "CUBE", "D", "DATASOURCE", "DESCRIPTION", "DIMENSION", "DIMENSION_TYPE", "DOT", "Digit", "E", "F", "FILE", "FROM", "G", "GROUP", "H", "HIERARCHY", "I", "ID", "INI", "J", "K", "L", "LBRACE", "LEVEL", "LEVELS", "LIST", "Letter", "M", "MAX", "MEASURES", "MIN", "N", "NAME", "O", "OF", "OR", "P", "PATH", "Q", "QUESTMARK", "R", "RBRACE", "REFERENCES", "S", "SELECT", "SLASH", "SUM", "T", "TYPE", "U", "UNDERSCORE", "V", "W", "WHERE", "WITH", "WS", "X", "Y", "Z", "'\"'", "'('", "')'", "'<'", "'<='", "'='", "'>='", "'LIKE'"
    };

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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public CubeSqlParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public CubeSqlParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return CubeSqlParser.tokenNames; }
    public String getGrammarFileName() { return "CubeSql.g"; }


      String sourceType;
      String iniFilePath;
      String mode;
      String name_creation;
      String datasource_table;
      String levelName;
      HashMap<String, String> levelID;
      HashMap<String, String> levelDescription;
      ArrayList<String> dimensionlst;
      ArrayList<String> hierarchylst;
      ArrayList<String> originallvllst;
      ArrayList<String> customlvllst;
      ArrayList<String> dimensionsAtCubeDataSource;
      ArrayList<String> attributes;
      HashMap<String, ArrayList<String>> levelAttributes;
      HashMap<String, String> attributeTypes;
      HashMap<String, String> attributeDatasource;
      ArrayList<String> conditionlst;
      ArrayList<String> tablelst;
      ArrayList<String> groupperlst;
      ArrayList<String> measurelst;
      ArrayList<String> measurefields;
      String aggregatefunc;
      String tmp_con;
      boolean group;
      String dimensionType;
      



    // $ANTLR start "start"
    // CubeSql.g:56:1: start : parse ;
    public final void start() throws RecognitionException {
        try {
            // CubeSql.g:56:7: ( parse )
            // CubeSql.g:56:8: parse
            {

                mode="";
                tmp_con="";
                group=false;
                dimensionlst=new ArrayList<String>();
                hierarchylst=new ArrayList<String>();
                originallvllst=new ArrayList<String>();
                customlvllst=new ArrayList<String>();
                levelID = new HashMap<String, String>();
                levelDescription = new HashMap<String, String>();
                attributes = new ArrayList<String>();
                levelAttributes = new HashMap<String, ArrayList<String>>();
                attributeTypes = new HashMap<String, String>();
                attributeDatasource = new HashMap<String, String>();
                conditionlst=new ArrayList<String>();
                groupperlst=new ArrayList<String>();
                tablelst=new ArrayList<String>();
                measurelst=new ArrayList<String>();
                measurefields=new ArrayList<String>();
                dimensionsAtCubeDataSource=new ArrayList<String>();
              

            pushFollow(FOLLOW_parse_in_start158);
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
        return ;
    }
    // $ANTLR end "start"



    // $ANTLR start "parse"
    // CubeSql.g:78:1: parse : ( source_type_statement | ( creation_statement )* | sql_query );
    public final void parse() throws RecognitionException {
        try {
            // CubeSql.g:78:7: ( source_type_statement | ( creation_statement )* | sql_query )
            int alt2=3;
            switch ( input.LA(1) ) {
            case DATASOURCE:
                {
                alt2=1;
                }
                break;
            case EOF:
            case CREATE:
                {
                alt2=2;
                }
                break;
            case SELECT:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // CubeSql.g:78:11: source_type_statement
                    {
                    sourceType=""; iniFilePath="";

                    pushFollow(FOLLOW_source_type_statement_in_parse171);
                    source_type_statement();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // CubeSql.g:79:5: ( creation_statement )*
                    {
                    // CubeSql.g:79:5: ( creation_statement )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==CREATE) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // CubeSql.g:79:6: creation_statement
                    	    {
                    	      mode="";
                    	    			levelName="";
                    	                name_creation="";
                    	                datasource_table="";
                    	                dimensionType = "other";
                    	                if(hierarchylst.size()>0)  hierarchylst.clear();
                    	                if(originallvllst.size()>0)  originallvllst.clear();
                    	                if(customlvllst.size()>0)  customlvllst.clear();
                    	                if(attributes.size()>0) attributes.clear();
                    	                if(levelAttributes.size()>0) levelAttributes.clear();
                    	                if(attributeTypes.size()>0) attributeTypes.clear();
                    	                if(attributeDatasource.size()>0) attributeDatasource.clear();
                    	                if(measurelst.size()>0) measurelst.clear();
                    	                if(measurefields.size()>0) measurefields.clear();
                    	                if(dimensionsAtCubeDataSource.size()>0) dimensionsAtCubeDataSource.clear();
                    	               

                    	    pushFollow(FOLLOW_creation_statement_in_parse193);
                    	    creation_statement();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // CubeSql.g:96:11: sql_query
                    {
                    pushFollow(FOLLOW_sql_query_in_parse209);
                    sql_query();

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
        return ;
    }
    // $ANTLR end "parse"



    // $ANTLR start "source_type_statement"
    // CubeSql.g:99:1: source_type_statement : DATASOURCE TYPE COLON NAME WITH INI FILE COLON PATH ;
    public final void source_type_statement() throws RecognitionException {
        Token NAME1=null;
        Token PATH2=null;

        try {
            // CubeSql.g:99:22: ( DATASOURCE TYPE COLON NAME WITH INI FILE COLON PATH )
            // CubeSql.g:99:24: DATASOURCE TYPE COLON NAME WITH INI FILE COLON PATH
            {
            match(input,DATASOURCE,FOLLOW_DATASOURCE_in_source_type_statement220); 

            match(input,TYPE,FOLLOW_TYPE_in_source_type_statement222); 

            match(input,COLON,FOLLOW_COLON_in_source_type_statement224); 

            NAME1=(Token)match(input,NAME,FOLLOW_NAME_in_source_type_statement226); 

            sourceType=(NAME1!=null?NAME1.getText():null);

            match(input,WITH,FOLLOW_WITH_in_source_type_statement230); 

            match(input,INI,FOLLOW_INI_in_source_type_statement232); 

            match(input,FILE,FOLLOW_FILE_in_source_type_statement234); 

            match(input,COLON,FOLLOW_COLON_in_source_type_statement236); 

            PATH2=(Token)match(input,PATH,FOLLOW_PATH_in_source_type_statement238); 

            iniFilePath=(PATH2!=null?PATH2.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "source_type_statement"



    // $ANTLR start "creation_statement"
    // CubeSql.g:101:1: creation_statement : creation QUESTMARK ;
    public final void creation_statement() throws RecognitionException {
        try {
            // CubeSql.g:101:20: ( creation QUESTMARK )
            // CubeSql.g:101:22: creation QUESTMARK
            {
            pushFollow(FOLLOW_creation_in_creation_statement248);
            creation();

            state._fsp--;


            match(input,QUESTMARK,FOLLOW_QUESTMARK_in_creation_statement250); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "creation_statement"



    // $ANTLR start "creation"
    // CubeSql.g:103:1: creation : ( creation_cube | creation_dimension );
    public final void creation() throws RecognitionException {
        try {
            // CubeSql.g:103:9: ( creation_cube | creation_dimension )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==CREATE) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==CUBE) ) {
                    int LA3_2 = input.LA(3);

                    if ( (LA3_2==NAME) ) {
                        int LA3_4 = input.LA(4);

                        if ( (LA3_4==DATASOURCE) ) {
                            alt3=1;
                        }
                        else if ( (LA3_4==LIST) ) {
                            alt3=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 3, 4, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA3_1==DIMENSION) ) {
                    int LA3_3 = input.LA(3);

                    if ( (LA3_3==NAME) ) {
                        int LA3_5 = input.LA(4);

                        if ( (LA3_5==DATASOURCE) ) {
                            alt3=1;
                        }
                        else if ( (LA3_5==LIST) ) {
                            alt3=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 3, 5, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 3, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // CubeSql.g:103:11: creation_cube
                    {
                    pushFollow(FOLLOW_creation_cube_in_creation257);
                    creation_cube();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // CubeSql.g:103:27: creation_dimension
                    {
                    pushFollow(FOLLOW_creation_dimension_in_creation261);
                    creation_dimension();

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
        return ;
    }
    // $ANTLR end "creation"



    // $ANTLR start "creation_cube"
    // CubeSql.g:105:1: creation_cube : create_statement datasource_statement measure_statement referdimension_statement ;
    public final void creation_cube() throws RecognitionException {
        try {
            // CubeSql.g:105:15: ( create_statement datasource_statement measure_statement referdimension_statement )
            // CubeSql.g:105:17: create_statement datasource_statement measure_statement referdimension_statement
            {
            pushFollow(FOLLOW_create_statement_in_creation_cube269);
            create_statement();

            state._fsp--;


            pushFollow(FOLLOW_datasource_statement_in_creation_cube271);
            datasource_statement();

            state._fsp--;


            pushFollow(FOLLOW_measure_statement_in_creation_cube273);
            measure_statement();

            state._fsp--;


            pushFollow(FOLLOW_referdimension_statement_in_creation_cube275);
            referdimension_statement();

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
        return ;
    }
    // $ANTLR end "creation_cube"



    // $ANTLR start "creation_dimension"
    // CubeSql.g:107:1: creation_dimension : ( create_statement levels_statement hierarchy_statement datasource_statement | create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement );
    public final void creation_dimension() throws RecognitionException {
        try {
            // CubeSql.g:107:20: ( create_statement levels_statement hierarchy_statement datasource_statement | create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement )
            int alt4=2;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // CubeSql.g:107:22: create_statement levels_statement hierarchy_statement datasource_statement
                    {
                    pushFollow(FOLLOW_create_statement_in_creation_dimension283);
                    create_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_levels_statement_in_creation_dimension285);
                    levels_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_hierarchy_statement_in_creation_dimension287);
                    hierarchy_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_datasource_statement_in_creation_dimension289);
                    datasource_statement();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // CubeSql.g:108:7: create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement
                    {
                    pushFollow(FOLLOW_create_statement_in_creation_dimension299);
                    create_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_levels_statement_in_creation_dimension301);
                    levels_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_hierarchy_statement_in_creation_dimension303);
                    hierarchy_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_dimension_type_statement_in_creation_dimension305);
                    dimension_type_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_datasource_statement_in_creation_dimension307);
                    datasource_statement();

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
        return ;
    }
    // $ANTLR end "creation_dimension"



    // $ANTLR start "create_statement"
    // CubeSql.g:110:1: create_statement : ( CREATE CUBE NAME | CREATE DIMENSION NAME );
    public final void create_statement() throws RecognitionException {
        Token NAME3=null;
        Token NAME4=null;

        try {
            // CubeSql.g:110:17: ( CREATE CUBE NAME | CREATE DIMENSION NAME )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==CREATE) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==CUBE) ) {
                    alt5=1;
                }
                else if ( (LA5_1==DIMENSION) ) {
                    alt5=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // CubeSql.g:110:19: CREATE CUBE NAME
                    {
                    match(input,CREATE,FOLLOW_CREATE_in_create_statement314); 

                    match(input,CUBE,FOLLOW_CUBE_in_create_statement316); 

                    mode="Cube";

                    NAME3=(Token)match(input,NAME,FOLLOW_NAME_in_create_statement320); 

                    name_creation=(NAME3!=null?NAME3.getText():null);

                    }
                    break;
                case 2 :
                    // CubeSql.g:112:19: CREATE DIMENSION NAME
                    {
                    match(input,CREATE,FOLLOW_CREATE_in_create_statement363); 

                    match(input,DIMENSION,FOLLOW_DIMENSION_in_create_statement365); 

                    mode="Dimension";

                    NAME4=(Token)match(input,NAME,FOLLOW_NAME_in_create_statement369); 

                    name_creation=(NAME4!=null?NAME4.getText():null).replace("~"," "); dimensionlst.add((NAME4!=null?NAME4.getText():null));

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
        return ;
    }
    // $ANTLR end "create_statement"



    // $ANTLR start "datasource_statement"
    // CubeSql.g:114:1: datasource_statement : DATASOURCE NAME ;
    public final void datasource_statement() throws RecognitionException {
        Token NAME5=null;

        try {
            // CubeSql.g:114:22: ( DATASOURCE NAME )
            // CubeSql.g:114:24: DATASOURCE NAME
            {
            match(input,DATASOURCE,FOLLOW_DATASOURCE_in_datasource_statement397); 

            NAME5=(Token)match(input,NAME,FOLLOW_NAME_in_datasource_statement399); 

            datasource_table=(NAME5!=null?NAME5.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "datasource_statement"



    // $ANTLR start "referdimension_statement"
    // CubeSql.g:116:1: referdimension_statement : REFERENCES DIMENSION dimensions ;
    public final void referdimension_statement() throws RecognitionException {
        try {
            // CubeSql.g:116:26: ( REFERENCES DIMENSION dimensions )
            // CubeSql.g:116:28: REFERENCES DIMENSION dimensions
            {
            match(input,REFERENCES,FOLLOW_REFERENCES_in_referdimension_statement410); 

            match(input,DIMENSION,FOLLOW_DIMENSION_in_referdimension_statement412); 

            pushFollow(FOLLOW_dimensions_in_referdimension_statement414);
            dimensions();

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
        return ;
    }
    // $ANTLR end "referdimension_statement"



    // $ANTLR start "measure_statement"
    // CubeSql.g:118:1: measure_statement : MEASURES measures ;
    public final void measure_statement() throws RecognitionException {
        try {
            // CubeSql.g:118:19: ( MEASURES measures )
            // CubeSql.g:118:21: MEASURES measures
            {
            match(input,MEASURES,FOLLOW_MEASURES_in_measure_statement422); 

            pushFollow(FOLLOW_measures_in_measure_statement424);
            measures();

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
        return ;
    }
    // $ANTLR end "measure_statement"



    // $ANTLR start "levels_statement"
    // CubeSql.g:120:1: levels_statement : LIST OF LEVELS LBRACE levels RBRACE ;
    public final void levels_statement() throws RecognitionException {
        try {
            // CubeSql.g:120:17: ( LIST OF LEVELS LBRACE levels RBRACE )
            // CubeSql.g:120:19: LIST OF LEVELS LBRACE levels RBRACE
            {
            match(input,LIST,FOLLOW_LIST_in_levels_statement431); 

            match(input,OF,FOLLOW_OF_in_levels_statement433); 

            match(input,LEVELS,FOLLOW_LEVELS_in_levels_statement435); 

            match(input,LBRACE,FOLLOW_LBRACE_in_levels_statement437); 

            pushFollow(FOLLOW_levels_in_levels_statement439);
            levels();

            state._fsp--;


            match(input,RBRACE,FOLLOW_RBRACE_in_levels_statement441); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "levels_statement"



    // $ANTLR start "hierarchy_statement"
    // CubeSql.g:122:1: hierarchy_statement : HIERARCHY hierarchy ;
    public final void hierarchy_statement() throws RecognitionException {
        try {
            // CubeSql.g:122:21: ( HIERARCHY hierarchy )
            // CubeSql.g:122:23: HIERARCHY hierarchy
            {
            match(input,HIERARCHY,FOLLOW_HIERARCHY_in_hierarchy_statement450); 

            pushFollow(FOLLOW_hierarchy_in_hierarchy_statement452);
            hierarchy();

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
        return ;
    }
    // $ANTLR end "hierarchy_statement"



    // $ANTLR start "dimension_type_statement"
    // CubeSql.g:124:1: dimension_type_statement : DIMENSION_TYPE NAME ;
    public final void dimension_type_statement() throws RecognitionException {
        Token NAME6=null;

        try {
            // CubeSql.g:124:25: ( DIMENSION_TYPE NAME )
            // CubeSql.g:124:27: DIMENSION_TYPE NAME
            {
            match(input,DIMENSION_TYPE,FOLLOW_DIMENSION_TYPE_in_dimension_type_statement459); 

            NAME6=(Token)match(input,NAME,FOLLOW_NAME_in_dimension_type_statement461); 

            dimensionType = (NAME6!=null?NAME6.getText():null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "dimension_type_statement"



    // $ANTLR start "dimensions"
    // CubeSql.g:126:1: dimensions : dimension ( comma_statement dimension )* ;
    public final void dimensions() throws RecognitionException {
        try {
            // CubeSql.g:126:12: ( dimension ( comma_statement dimension )* )
            // CubeSql.g:126:14: dimension ( comma_statement dimension )*
            {
            pushFollow(FOLLOW_dimension_in_dimensions471);
            dimension();

            state._fsp--;


            // CubeSql.g:126:25: ( comma_statement dimension )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==COMMA||LA6_0==WS) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // CubeSql.g:126:26: comma_statement dimension
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_dimensions475);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_dimension_in_dimensions477);
            	    dimension();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "dimensions"



    // $ANTLR start "dimension"
    // CubeSql.g:128:1: dimension : NAME AT sqlfield ;
    public final void dimension() throws RecognitionException {
        try {
            // CubeSql.g:128:11: ( NAME AT sqlfield )
            // CubeSql.g:128:13: NAME AT sqlfield
            {
            match(input,NAME,FOLLOW_NAME_in_dimension488); 

            match(input,AT,FOLLOW_AT_in_dimension490); 

            pushFollow(FOLLOW_sqlfield_in_dimension492);
            sqlfield();

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
        return ;
    }
    // $ANTLR end "dimension"



    // $ANTLR start "measures"
    // CubeSql.g:130:1: measures : measure ( comma_statement measure )* ;
    public final void measures() throws RecognitionException {
        try {
            // CubeSql.g:130:10: ( measure ( comma_statement measure )* )
            // CubeSql.g:130:12: measure ( comma_statement measure )*
            {
            pushFollow(FOLLOW_measure_in_measures500);
            measure();

            state._fsp--;


            // CubeSql.g:130:20: ( comma_statement measure )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA||LA7_0==WS) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // CubeSql.g:130:21: comma_statement measure
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_measures503);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_measure_in_measures505);
            	    measure();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "measures"



    // $ANTLR start "measure"
    // CubeSql.g:132:1: measure : NAME AT measureName= PATH ;
    public final void measure() throws RecognitionException {
        Token measureName=null;
        Token NAME7=null;

        try {
            // CubeSql.g:132:9: ( NAME AT measureName= PATH )
            // CubeSql.g:132:11: NAME AT measureName= PATH
            {
            NAME7=(Token)match(input,NAME,FOLLOW_NAME_in_measure515); 

            measurelst.add((NAME7!=null?NAME7.getText():null));

            match(input,AT,FOLLOW_AT_in_measure519); 

            measureName=(Token)match(input,PATH,FOLLOW_PATH_in_measure523); 

            measurefields.add((measureName!=null?measureName.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "measure"



    // $ANTLR start "levels"
    // CubeSql.g:134:1: levels : level ( comma_statement level )* ;
    public final void levels() throws RecognitionException {
        try {
            // CubeSql.g:134:7: ( level ( comma_statement level )* )
            // CubeSql.g:134:9: level ( comma_statement level )*
            {
            pushFollow(FOLLOW_level_in_levels532);
            level();

            state._fsp--;


            // CubeSql.g:134:15: ( comma_statement level )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==COMMA||LA8_0==WS) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // CubeSql.g:134:16: comma_statement level
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_levels535);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_level_in_levels537);
            	    level();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "levels"



    // $ANTLR start "level"
    // CubeSql.g:136:1: level : CREATE LEVEL sqlfield attributes_statement WITH ID COLON idName= NAME AND DESCRIPTION COLON descrName= NAME ;
    public final void level() throws RecognitionException {
        Token idName=null;
        Token descrName=null;

        try {
            // CubeSql.g:136:7: ( CREATE LEVEL sqlfield attributes_statement WITH ID COLON idName= NAME AND DESCRIPTION COLON descrName= NAME )
            // CubeSql.g:136:9: CREATE LEVEL sqlfield attributes_statement WITH ID COLON idName= NAME AND DESCRIPTION COLON descrName= NAME
            {
            match(input,CREATE,FOLLOW_CREATE_in_level547); 

            match(input,LEVEL,FOLLOW_LEVEL_in_level549); 

            pushFollow(FOLLOW_sqlfield_in_level551);
            sqlfield();

            state._fsp--;


            pushFollow(FOLLOW_attributes_statement_in_level553);
            attributes_statement();

            state._fsp--;


            match(input,WITH,FOLLOW_WITH_in_level555); 

            match(input,ID,FOLLOW_ID_in_level557); 

            match(input,COLON,FOLLOW_COLON_in_level559); 

            idName=(Token)match(input,NAME,FOLLOW_NAME_in_level563); 

            levelID.put(levelName, (idName!=null?idName.getText():null));

            match(input,AND,FOLLOW_AND_in_level567); 

            match(input,DESCRIPTION,FOLLOW_DESCRIPTION_in_level569); 

            match(input,COLON,FOLLOW_COLON_in_level571); 

            descrName=(Token)match(input,NAME,FOLLOW_NAME_in_level575); 

            levelDescription.put(levelName, (descrName!=null?descrName.getText():null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "level"



    // $ANTLR start "attributes_statement"
    // CubeSql.g:138:1: attributes_statement : WITH ATTRIBUTES LBRACE attributes RBRACE ;
    public final void attributes_statement() throws RecognitionException {
        try {
            // CubeSql.g:138:21: ( WITH ATTRIBUTES LBRACE attributes RBRACE )
            // CubeSql.g:138:23: WITH ATTRIBUTES LBRACE attributes RBRACE
            {
            match(input,WITH,FOLLOW_WITH_in_attributes_statement584); 

            match(input,ATTRIBUTES,FOLLOW_ATTRIBUTES_in_attributes_statement586); 

            match(input,LBRACE,FOLLOW_LBRACE_in_attributes_statement588); 

            pushFollow(FOLLOW_attributes_in_attributes_statement590);
            attributes();

            state._fsp--;


            match(input,RBRACE,FOLLOW_RBRACE_in_attributes_statement592); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "attributes_statement"



    // $ANTLR start "attributes"
    // CubeSql.g:140:1: attributes : attribute ( comma_statement attribute )* ;
    public final void attributes() throws RecognitionException {
        try {
            // CubeSql.g:140:11: ( attribute ( comma_statement attribute )* )
            // CubeSql.g:140:13: attribute ( comma_statement attribute )*
            {
            attributes.clear();

            pushFollow(FOLLOW_attribute_in_attributes601);
            attribute();

            state._fsp--;


            // CubeSql.g:140:45: ( comma_statement attribute )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==COMMA||LA9_0==WS) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // CubeSql.g:140:46: comma_statement attribute
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_attributes604);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_attribute_in_attributes606);
            	    attribute();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            levelAttributes.put(levelName, (ArrayList)attributes.clone()); originallvllst.add(attributeDatasource.get(attributes.get(0)));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "attributes"



    // $ANTLR start "attribute"
    // CubeSql.g:142:1: attribute : (attrName= NAME COLON typeName= NAME datasource_statement |attrName1= NAME DOT attrName2= NAME COLON typeName= NAME datasource_statement );
    public final void attribute() throws RecognitionException {
        Token attrName=null;
        Token typeName=null;
        Token attrName1=null;
        Token attrName2=null;

        try {
            // CubeSql.g:142:10: (attrName= NAME COLON typeName= NAME datasource_statement |attrName1= NAME DOT attrName2= NAME COLON typeName= NAME datasource_statement )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==NAME) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==DOT) ) {
                    alt10=2;
                }
                else if ( (LA10_1==COLON) ) {
                    alt10=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // CubeSql.g:142:12: attrName= NAME COLON typeName= NAME datasource_statement
                    {
                    attrName=(Token)match(input,NAME,FOLLOW_NAME_in_attribute619); 

                    attributes.add((attrName!=null?attrName.getText():null));

                    match(input,COLON,FOLLOW_COLON_in_attribute629); 

                    typeName=(Token)match(input,NAME,FOLLOW_NAME_in_attribute633); 

                    attributeTypes.put(((attrName!=null?attrName.getText():null)), ((typeName!=null?typeName.getText():null)));

                    pushFollow(FOLLOW_datasource_statement_in_attribute642);
                    datasource_statement();

                    state._fsp--;


                    attributeDatasource.put(((attrName!=null?attrName.getText():null)), datasource_table);

                    }
                    break;
                case 2 :
                    // CubeSql.g:145:6: attrName1= NAME DOT attrName2= NAME COLON typeName= NAME datasource_statement
                    {
                    attrName1=(Token)match(input,NAME,FOLLOW_NAME_in_attribute653); 

                    match(input,DOT,FOLLOW_DOT_in_attribute655); 

                    attrName2=(Token)match(input,NAME,FOLLOW_NAME_in_attribute659); 

                    attributes.add((attrName1!=null?attrName1.getText():null)+"."+(attrName2!=null?attrName2.getText():null));

                    match(input,COLON,FOLLOW_COLON_in_attribute669); 

                    typeName=(Token)match(input,NAME,FOLLOW_NAME_in_attribute673); 

                    attributeTypes.put(((attrName1!=null?attrName1.getText():null)+"."+(attrName2!=null?attrName2.getText():null)), ((typeName!=null?typeName.getText():null)));

                    pushFollow(FOLLOW_datasource_statement_in_attribute682);
                    datasource_statement();

                    state._fsp--;


                    attributeDatasource.put(((attrName!=null?attrName.getText():null)), datasource_table);

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
        return ;
    }
    // $ANTLR end "attribute"



    // $ANTLR start "comma_statement"
    // CubeSql.g:149:1: comma_statement : ( WS )* COMMA ( WS )* ;
    public final void comma_statement() throws RecognitionException {
        try {
            // CubeSql.g:149:16: ( ( WS )* COMMA ( WS )* )
            // CubeSql.g:149:18: ( WS )* COMMA ( WS )*
            {
            // CubeSql.g:149:18: ( WS )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==WS) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // CubeSql.g:149:18: WS
            	    {
            	    match(input,WS,FOLLOW_WS_in_comma_statement691); 

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            match(input,COMMA,FOLLOW_COMMA_in_comma_statement694); 

            // CubeSql.g:149:28: ( WS )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==WS) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // CubeSql.g:149:28: WS
            	    {
            	    match(input,WS,FOLLOW_WS_in_comma_statement696); 

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "comma_statement"



    // $ANTLR start "hierarchy"
    // CubeSql.g:151:1: hierarchy : name1= NAME ( CHILDOF name2= NAME )+ ;
    public final void hierarchy() throws RecognitionException {
        Token name1=null;
        Token name2=null;

        try {
            // CubeSql.g:151:10: (name1= NAME ( CHILDOF name2= NAME )+ )
            // CubeSql.g:151:12: name1= NAME ( CHILDOF name2= NAME )+
            {
            name1=(Token)match(input,NAME,FOLLOW_NAME_in_hierarchy706); 

            hierarchylst.add((name1!=null?name1.getText():null));

            // CubeSql.g:151:56: ( CHILDOF name2= NAME )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==CHILDOF) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // CubeSql.g:151:57: CHILDOF name2= NAME
            	    {
            	    match(input,CHILDOF,FOLLOW_CHILDOF_in_hierarchy711); 

            	    name2=(Token)match(input,NAME,FOLLOW_NAME_in_hierarchy715); 

            	    hierarchylst.add((name2!=null?name2.getText():null));

            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "hierarchy"



    // $ANTLR start "sqlfield"
    // CubeSql.g:153:1: sqlfield : (name1= NAME DOT name2= NAME | NAME |name= PATH );
    public final void sqlfield() throws RecognitionException {
        Token name1=null;
        Token name2=null;
        Token name=null;
        Token NAME8=null;

        try {
            // CubeSql.g:153:10: (name1= NAME DOT name2= NAME | NAME |name= PATH )
            int alt14=3;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==NAME) ) {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==DOT) ) {
                    alt14=1;
                }
                else if ( (LA14_1==EOF||LA14_1==AND||LA14_1==CHILDOF||LA14_1==COMMA||LA14_1==GROUP||LA14_1==OR||LA14_1==QUESTMARK||(LA14_1 >= WITH && LA14_1 <= WS)||(LA14_1 >= 80 && LA14_1 <= 85)) ) {
                    alt14=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA14_0==PATH) ) {
                alt14=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }
            switch (alt14) {
                case 1 :
                    // CubeSql.g:153:12: name1= NAME DOT name2= NAME
                    {
                    name1=(Token)match(input,NAME,FOLLOW_NAME_in_sqlfield730); 

                    match(input,DOT,FOLLOW_DOT_in_sqlfield732); 

                    name2=(Token)match(input,NAME,FOLLOW_NAME_in_sqlfield736); 

                     tmp_con+=(name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null);
                    									   if(mode.equals("Dimension")){ customlvllst.add((name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null));
                                                           levelName = (name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null);}
                                                           if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add((name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null));}

                    }
                    break;
                case 2 :
                    // CubeSql.g:157:11: NAME
                    {
                    NAME8=(Token)match(input,NAME,FOLLOW_NAME_in_sqlfield750); 

                    tmp_con+= (NAME8!=null?NAME8.getText():null);
                            		if(mode.equals("Dimension")){ customlvllst.add((NAME8!=null?NAME8.getText():null)); levelName=(NAME8!=null?NAME8.getText():null);}
                            		if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add((NAME8!=null?NAME8.getText():null));}

                    }
                    break;
                case 3 :
                    // CubeSql.g:161:11: name= PATH
                    {
                    name=(Token)match(input,PATH,FOLLOW_PATH_in_sqlfield775); 

                    tmp_con+= (name!=null?name.getText():null);
                            			 if(mode.equals("Dimension")){ customlvllst.add((name!=null?name.getText():null)); levelName=(name!=null?name.getText():null);}
                            			 if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add((name!=null?name.getText():null));}

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
        return ;
    }
    // $ANTLR end "sqlfield"



    // $ANTLR start "sql_query"
    // CubeSql.g:168:1: sql_query : select_statement from_statement where_statement group_statement ;
    public final void sql_query() throws RecognitionException {
        try {
            // CubeSql.g:168:10: ( select_statement from_statement where_statement group_statement )
            // CubeSql.g:168:12: select_statement from_statement where_statement group_statement
            {
            pushFollow(FOLLOW_select_statement_in_sql_query795);
            select_statement();

            state._fsp--;


            pushFollow(FOLLOW_from_statement_in_sql_query809);
            from_statement();

            state._fsp--;


            pushFollow(FOLLOW_where_statement_in_sql_query822);
            where_statement();

            state._fsp--;


            pushFollow(FOLLOW_group_statement_in_sql_query835);
            group_statement();

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
        return ;
    }
    // $ANTLR end "sql_query"



    // $ANTLR start "select_statement"
    // CubeSql.g:173:1: select_statement : SELECT grouppers COMMA aggregate_statement ;
    public final void select_statement() throws RecognitionException {
        try {
            // CubeSql.g:173:17: ( SELECT grouppers COMMA aggregate_statement )
            // CubeSql.g:173:19: SELECT grouppers COMMA aggregate_statement
            {
            match(input,SELECT,FOLLOW_SELECT_in_select_statement853); 

            pushFollow(FOLLOW_grouppers_in_select_statement855);
            grouppers();

            state._fsp--;


            match(input,COMMA,FOLLOW_COMMA_in_select_statement857); 

            pushFollow(FOLLOW_aggregate_statement_in_select_statement859);
            aggregate_statement();

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
        return ;
    }
    // $ANTLR end "select_statement"



    // $ANTLR start "aggregate_statement"
    // CubeSql.g:175:1: aggregate_statement : aggregate_func '(' sqlfield ')' ;
    public final void aggregate_statement() throws RecognitionException {
        try {
            // CubeSql.g:175:21: ( aggregate_func '(' sqlfield ')' )
            // CubeSql.g:175:23: aggregate_func '(' sqlfield ')'
            {
            tmp_con="";

            pushFollow(FOLLOW_aggregate_func_in_aggregate_statement869);
            aggregate_func();

            state._fsp--;


            match(input,79,FOLLOW_79_in_aggregate_statement871); 

            pushFollow(FOLLOW_sqlfield_in_aggregate_statement873);
            sqlfield();

            state._fsp--;


            match(input,80,FOLLOW_80_in_aggregate_statement875); 

            aggregatefunc=tmp_con;tmp_con="";

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "aggregate_statement"



    // $ANTLR start "aggregate_func"
    // CubeSql.g:177:1: aggregate_func : ( SUM | AVG | COUNT | MAX | MIN );
    public final void aggregate_func() throws RecognitionException {
        Token SUM9=null;
        Token AVG10=null;
        Token COUNT11=null;
        Token MAX12=null;
        Token MIN13=null;

        try {
            // CubeSql.g:177:16: ( SUM | AVG | COUNT | MAX | MIN )
            int alt15=5;
            switch ( input.LA(1) ) {
            case SUM:
                {
                alt15=1;
                }
                break;
            case AVG:
                {
                alt15=2;
                }
                break;
            case COUNT:
                {
                alt15=3;
                }
                break;
            case MAX:
                {
                alt15=4;
                }
                break;
            case MIN:
                {
                alt15=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }

            switch (alt15) {
                case 1 :
                    // CubeSql.g:177:18: SUM
                    {
                    SUM9=(Token)match(input,SUM,FOLLOW_SUM_in_aggregate_func885); 

                    tmp_con=(SUM9!=null?SUM9.getText():null)+"~";

                    }
                    break;
                case 2 :
                    // CubeSql.g:178:19: AVG
                    {
                    AVG10=(Token)match(input,AVG,FOLLOW_AVG_in_aggregate_func907); 

                    tmp_con=(AVG10!=null?AVG10.getText():null)+"~";

                    }
                    break;
                case 3 :
                    // CubeSql.g:179:19: COUNT
                    {
                    COUNT11=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_func930); 

                    tmp_con=(COUNT11!=null?COUNT11.getText():null)+"~";

                    }
                    break;
                case 4 :
                    // CubeSql.g:180:19: MAX
                    {
                    MAX12=(Token)match(input,MAX,FOLLOW_MAX_in_aggregate_func952); 

                    tmp_con=(MAX12!=null?MAX12.getText():null)+"~";

                    }
                    break;
                case 5 :
                    // CubeSql.g:181:19: MIN
                    {
                    MIN13=(Token)match(input,MIN,FOLLOW_MIN_in_aggregate_func974); 

                    tmp_con=(MIN13!=null?MIN13.getText():null)+"~";

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
        return ;
    }
    // $ANTLR end "aggregate_func"



    // $ANTLR start "from_statement"
    // CubeSql.g:183:1: from_statement : FROM tables ;
    public final void from_statement() throws RecognitionException {
        try {
            // CubeSql.g:183:16: ( FROM tables )
            // CubeSql.g:183:18: FROM tables
            {
            match(input,FROM,FOLLOW_FROM_in_from_statement984); 

            pushFollow(FOLLOW_tables_in_from_statement986);
            tables();

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
        return ;
    }
    // $ANTLR end "from_statement"



    // $ANTLR start "tables"
    // CubeSql.g:185:1: tables : table ( COMMA table )* ;
    public final void tables() throws RecognitionException {
        try {
            // CubeSql.g:185:8: ( table ( COMMA table )* )
            // CubeSql.g:185:10: table ( COMMA table )*
            {
            pushFollow(FOLLOW_table_in_tables994);
            table();

            state._fsp--;


            // CubeSql.g:185:16: ( COMMA table )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==COMMA) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // CubeSql.g:185:17: COMMA table
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_tables997); 

            	    pushFollow(FOLLOW_table_in_tables999);
            	    table();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "tables"



    // $ANTLR start "table"
    // CubeSql.g:187:1: table : (name1= NAME AS name2= NAME |name1= NAME name2= NAME | NAME );
    public final void table() throws RecognitionException {
        Token name1=null;
        Token name2=null;
        Token NAME14=null;

        try {
            // CubeSql.g:187:7: (name1= NAME AS name2= NAME |name1= NAME name2= NAME | NAME )
            int alt17=3;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==NAME) ) {
                switch ( input.LA(2) ) {
                case AS:
                    {
                    alt17=1;
                    }
                    break;
                case NAME:
                    {
                    alt17=2;
                    }
                    break;
                case COMMA:
                case GROUP:
                case WHERE:
                    {
                    alt17=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }
            switch (alt17) {
                case 1 :
                    // CubeSql.g:187:9: name1= NAME AS name2= NAME
                    {
                    name1=(Token)match(input,NAME,FOLLOW_NAME_in_table1011); 

                    match(input,AS,FOLLOW_AS_in_table1013); 

                    name2=(Token)match(input,NAME,FOLLOW_NAME_in_table1017); 

                    tablelst.add((name1!=null?name1.getText():null)+"~"+(name2!=null?name2.getText():null));

                    }
                    break;
                case 2 :
                    // CubeSql.g:188:9: name1= NAME name2= NAME
                    {
                    name1=(Token)match(input,NAME,FOLLOW_NAME_in_table1032); 

                    name2=(Token)match(input,NAME,FOLLOW_NAME_in_table1036); 

                    tablelst.add((name1!=null?name1.getText():null)+"~"+(name2!=null?name2.getText():null));

                    }
                    break;
                case 3 :
                    // CubeSql.g:189:9: NAME
                    {
                    NAME14=(Token)match(input,NAME,FOLLOW_NAME_in_table1048); 

                    tablelst.add((NAME14!=null?NAME14.getText():null));

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
        return ;
    }
    // $ANTLR end "table"



    // $ANTLR start "where_statement"
    // CubeSql.g:191:1: where_statement : ( WHERE cond_statement ( boolean_expr cond_statement )* |);
    public final void where_statement() throws RecognitionException {
        try {
            // CubeSql.g:191:17: ( WHERE cond_statement ( boolean_expr cond_statement )* |)
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==WHERE) ) {
                alt19=1;
            }
            else if ( (LA19_0==GROUP) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }
            switch (alt19) {
                case 1 :
                    // CubeSql.g:191:19: WHERE cond_statement ( boolean_expr cond_statement )*
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_where_statement1058); 

                    tmp_con="";

                    pushFollow(FOLLOW_cond_statement_in_where_statement1062);
                    cond_statement();

                    state._fsp--;


                    conditionlst.add(tmp_con);

                    // CubeSql.g:191:83: ( boolean_expr cond_statement )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==AND||LA18_0==OR) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // CubeSql.g:191:84: boolean_expr cond_statement
                    	    {
                    	    pushFollow(FOLLOW_boolean_expr_in_where_statement1067);
                    	    boolean_expr();

                    	    state._fsp--;


                    	    tmp_con="";

                    	    pushFollow(FOLLOW_cond_statement_in_where_statement1071);
                    	    cond_statement();

                    	    state._fsp--;


                    	    conditionlst.add(tmp_con);

                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // CubeSql.g:192:21: 
                    {
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
        return ;
    }
    // $ANTLR end "where_statement"



    // $ANTLR start "cond_statement"
    // CubeSql.g:193:1: cond_statement : ( sqlfield operator sqlfield | sqlfield operator quote_statement NAME quote_statement | sqlfield operator quote_statement ( Digit )+ NAME quote_statement | sqlfield operator ( Digit )+ | sqlfield operator quote_statement quote_statement );
    public final void cond_statement() throws RecognitionException {
        Token NAME17=null;
        Token Digit19=null;
        Token NAME20=null;
        Token Digit22=null;
        CubeSqlParser.operator_return operator15 =null;

        CubeSqlParser.operator_return operator16 =null;

        CubeSqlParser.operator_return operator18 =null;

        CubeSqlParser.operator_return operator21 =null;

        CubeSqlParser.operator_return operator23 =null;


        try {
            // CubeSql.g:193:16: ( sqlfield operator sqlfield | sqlfield operator quote_statement NAME quote_statement | sqlfield operator quote_statement ( Digit )+ NAME quote_statement | sqlfield operator ( Digit )+ | sqlfield operator quote_statement quote_statement )
            int alt22=5;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==NAME) ) {
                int LA22_1 = input.LA(2);

                if ( (LA22_1==DOT) ) {
                    int LA22_3 = input.LA(3);

                    if ( (LA22_3==NAME) ) {
                        int LA22_5 = input.LA(4);

                        if ( (LA22_5==CHILDOF||(LA22_5 >= 81 && LA22_5 <= 85)) ) {
                            switch ( input.LA(5) ) {
                            case NAME:
                            case PATH:
                                {
                                alt22=1;
                                }
                                break;
                            case BACKSLASH:
                            case 78:
                                {
                                switch ( input.LA(6) ) {
                                case NAME:
                                    {
                                    alt22=2;
                                    }
                                    break;
                                case Digit:
                                    {
                                    alt22=3;
                                    }
                                    break;
                                case BACKSLASH:
                                case 78:
                                    {
                                    alt22=5;
                                    }
                                    break;
                                default:
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 22, 7, input);

                                    throw nvae;

                                }

                                }
                                break;
                            case Digit:
                                {
                                alt22=4;
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 22, 4, input);

                                throw nvae;

                            }

                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 22, 5, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 3, input);

                        throw nvae;

                    }
                }
                else if ( (LA22_1==CHILDOF||(LA22_1 >= 81 && LA22_1 <= 85)) ) {
                    switch ( input.LA(3) ) {
                    case NAME:
                    case PATH:
                        {
                        alt22=1;
                        }
                        break;
                    case BACKSLASH:
                    case 78:
                        {
                        switch ( input.LA(4) ) {
                        case NAME:
                            {
                            alt22=2;
                            }
                            break;
                        case Digit:
                            {
                            alt22=3;
                            }
                            break;
                        case BACKSLASH:
                        case 78:
                            {
                            alt22=5;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 22, 7, input);

                            throw nvae;

                        }

                        }
                        break;
                    case Digit:
                        {
                        alt22=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 4, input);

                        throw nvae;

                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA22_0==PATH) ) {
                int LA22_2 = input.LA(2);

                if ( (LA22_2==CHILDOF||(LA22_2 >= 81 && LA22_2 <= 85)) ) {
                    switch ( input.LA(3) ) {
                    case NAME:
                    case PATH:
                        {
                        alt22=1;
                        }
                        break;
                    case BACKSLASH:
                    case 78:
                        {
                        switch ( input.LA(4) ) {
                        case NAME:
                            {
                            alt22=2;
                            }
                            break;
                        case Digit:
                            {
                            alt22=3;
                            }
                            break;
                        case BACKSLASH:
                        case 78:
                            {
                            alt22=5;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 22, 7, input);

                            throw nvae;

                        }

                        }
                        break;
                    case Digit:
                        {
                        alt22=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 4, input);

                        throw nvae;

                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 2, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;

            }
            switch (alt22) {
                case 1 :
                    // CubeSql.g:193:18: sqlfield operator sqlfield
                    {
                    pushFollow(FOLLOW_sqlfield_in_cond_statement1103);
                    sqlfield();

                    state._fsp--;


                    pushFollow(FOLLOW_operator_in_cond_statement1105);
                    operator15=operator();

                    state._fsp--;


                    tmp_con+="~"+(operator15!=null?input.toString(operator15.start,operator15.stop):null)+"~";

                    pushFollow(FOLLOW_sqlfield_in_cond_statement1109);
                    sqlfield();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // CubeSql.g:194:19: sqlfield operator quote_statement NAME quote_statement
                    {
                    pushFollow(FOLLOW_sqlfield_in_cond_statement1130);
                    sqlfield();

                    state._fsp--;


                    pushFollow(FOLLOW_operator_in_cond_statement1132);
                    operator16=operator();

                    state._fsp--;


                    tmp_con+="~"+(operator16!=null?input.toString(operator16.start,operator16.stop):null)+"~";

                    pushFollow(FOLLOW_quote_statement_in_cond_statement1137);
                    quote_statement();

                    state._fsp--;


                    NAME17=(Token)match(input,NAME,FOLLOW_NAME_in_cond_statement1139); 

                    pushFollow(FOLLOW_quote_statement_in_cond_statement1141);
                    quote_statement();

                    state._fsp--;


                    tmp_con+="'"+(NAME17!=null?NAME17.getText():null)+"'";

                    }
                    break;
                case 3 :
                    // CubeSql.g:195:19: sqlfield operator quote_statement ( Digit )+ NAME quote_statement
                    {
                    pushFollow(FOLLOW_sqlfield_in_cond_statement1163);
                    sqlfield();

                    state._fsp--;


                    pushFollow(FOLLOW_operator_in_cond_statement1165);
                    operator18=operator();

                    state._fsp--;


                    tmp_con+="~"+(operator18!=null?input.toString(operator18.start,operator18.stop):null)+"~";

                    pushFollow(FOLLOW_quote_statement_in_cond_statement1170);
                    quote_statement();

                    state._fsp--;


                    // CubeSql.g:195:89: ( Digit )+
                    int cnt20=0;
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==Digit) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // CubeSql.g:195:90: Digit
                    	    {
                    	    Digit19=(Token)match(input,Digit,FOLLOW_Digit_in_cond_statement1173); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt20 >= 1 ) break loop20;
                                EarlyExitException eee =
                                    new EarlyExitException(20, input);
                                throw eee;
                        }
                        cnt20++;
                    } while (true);


                    NAME20=(Token)match(input,NAME,FOLLOW_NAME_in_cond_statement1177); 

                    pushFollow(FOLLOW_quote_statement_in_cond_statement1180);
                    quote_statement();

                    state._fsp--;


                    tmp_con+="'"+(Digit19!=null?Digit19.getText():null)+(NAME20!=null?NAME20.getText():null)+"'";

                    }
                    break;
                case 4 :
                    // CubeSql.g:196:19: sqlfield operator ( Digit )+
                    {
                    pushFollow(FOLLOW_sqlfield_in_cond_statement1202);
                    sqlfield();

                    state._fsp--;


                    pushFollow(FOLLOW_operator_in_cond_statement1204);
                    operator21=operator();

                    state._fsp--;


                    tmp_con+="~"+(operator21!=null?input.toString(operator21.start,operator21.stop):null)+"~";

                    // CubeSql.g:196:72: ( Digit )+
                    int cnt21=0;
                    loop21:
                    do {
                        int alt21=2;
                        int LA21_0 = input.LA(1);

                        if ( (LA21_0==Digit) ) {
                            alt21=1;
                        }


                        switch (alt21) {
                    	case 1 :
                    	    // CubeSql.g:196:73: Digit
                    	    {
                    	    Digit22=(Token)match(input,Digit,FOLLOW_Digit_in_cond_statement1209); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt21 >= 1 ) break loop21;
                                EarlyExitException eee =
                                    new EarlyExitException(21, input);
                                throw eee;
                        }
                        cnt21++;
                    } while (true);


                    tmp_con+=(Digit22!=null?Digit22.getText():null);

                    }
                    break;
                case 5 :
                    // CubeSql.g:197:19: sqlfield operator quote_statement quote_statement
                    {
                    pushFollow(FOLLOW_sqlfield_in_cond_statement1233);
                    sqlfield();

                    state._fsp--;


                    pushFollow(FOLLOW_operator_in_cond_statement1235);
                    operator23=operator();

                    state._fsp--;


                    tmp_con+="~"+(operator23!=null?input.toString(operator23.start,operator23.stop):null)+"~";

                    pushFollow(FOLLOW_quote_statement_in_cond_statement1240);
                    quote_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_quote_statement_in_cond_statement1243);
                    quote_statement();

                    state._fsp--;


                    tmp_con+="'"+"'";

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
        return ;
    }
    // $ANTLR end "cond_statement"



    // $ANTLR start "boolean_expr"
    // CubeSql.g:200:1: boolean_expr : ( OR | AND );
    public final void boolean_expr() throws RecognitionException {
        try {
            // CubeSql.g:200:14: ( OR | AND )
            // CubeSql.g:
            {
            if ( input.LA(1)==AND||input.LA(1)==OR ) {
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
        return ;
    }
    // $ANTLR end "boolean_expr"



    // $ANTLR start "quote_statement"
    // CubeSql.g:202:1: quote_statement : ( '\"' | '\\'' );
    public final void quote_statement() throws RecognitionException {
        try {
            // CubeSql.g:202:16: ( '\"' | '\\'' )
            // CubeSql.g:
            {
            if ( input.LA(1)==BACKSLASH||input.LA(1)==78 ) {
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
        return ;
    }
    // $ANTLR end "quote_statement"


    public static class operator_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "operator"
    // CubeSql.g:204:1: operator : ( '=' | '>=' | '<=' | '>' | '<' | 'LIKE' );
    public final CubeSqlParser.operator_return operator() throws RecognitionException {
        CubeSqlParser.operator_return retval = new CubeSqlParser.operator_return();
        retval.start = input.LT(1);


        try {
            // CubeSql.g:204:10: ( '=' | '>=' | '<=' | '>' | '<' | 'LIKE' )
            // CubeSql.g:
            {
            if ( input.LA(1)==CHILDOF||(input.LA(1) >= 81 && input.LA(1) <= 85) ) {
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
    // $ANTLR end "operator"



    // $ANTLR start "group_statement"
    // CubeSql.g:211:1: group_statement : GROUP BY grouppers ;
    public final void group_statement() throws RecognitionException {
        try {
            // CubeSql.g:211:17: ( GROUP BY grouppers )
            // CubeSql.g:211:19: GROUP BY grouppers
            {
            match(input,GROUP,FOLLOW_GROUP_in_group_statement1391); 

            match(input,BY,FOLLOW_BY_in_group_statement1393); 

            group=true;

            pushFollow(FOLLOW_grouppers_in_group_statement1397);
            grouppers();

            state._fsp--;


            group=false;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "group_statement"



    // $ANTLR start "grouppers"
    // CubeSql.g:213:1: grouppers : sqlfield ( comma_statement sqlfield ) ;
    public final void grouppers() throws RecognitionException {
        try {
            // CubeSql.g:213:11: ( sqlfield ( comma_statement sqlfield ) )
            // CubeSql.g:213:13: sqlfield ( comma_statement sqlfield )
            {
            tmp_con="";

            pushFollow(FOLLOW_sqlfield_in_grouppers1409);
            sqlfield();

            state._fsp--;


            if(group){ groupperlst.add(tmp_con);}

            // CubeSql.g:213:76: ( comma_statement sqlfield )
            // CubeSql.g:213:77: comma_statement sqlfield
            {
            pushFollow(FOLLOW_comma_statement_in_grouppers1414);
            comma_statement();

            state._fsp--;


            tmp_con="";

            pushFollow(FOLLOW_sqlfield_in_grouppers1418);
            sqlfield();

            state._fsp--;


            if(group)groupperlst.add(tmp_con);

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
        return ;
    }
    // $ANTLR end "grouppers"

    // Delegated rules


    protected DFA4 dfa4 = new DFA4(this);
    static final String DFA4_eotS =
        "\152\uffff";
    static final String DFA4_eofS =
        "\152\uffff";
    static final String DFA4_minS =
        "\1\22\1\23\2\63\2\54\1\65\1\53\1\51\1\22\1\52\1\63\1\31\1\111\1"+
        "\63\1\10\1\111\1\51\1\63\1\17\2\63\1\17\1\25\2\63\1\25\1\20\1\63"+
        "\1\20\1\63\1\111\1\20\1\63\1\17\1\44\2\63\2\17\1\25\3\63\1\5\1\25"+
        "\1\20\1\26\1\63\1\17\1\20\1\63\2\20\1\22\1\42\1\22\1\52\2\63\1\16"+
        "\1\31\1\111\2\63\1\10\1\16\1\111\1\51\2\uffff\1\63\1\17\2\63\1\17"+
        "\1\25\2\63\1\25\1\20\1\63\1\20\1\63\1\111\1\20\1\63\1\17\1\44\2"+
        "\63\2\17\1\25\3\63\1\5\1\25\1\20\1\26\1\63\1\17\1\20\1\63\1\20";
    static final String DFA4_maxS =
        "\1\22\1\27\2\63\2\54\1\65\1\53\1\51\1\22\1\52\1\70\2\111\1\63\1"+
        "\10\1\111\1\51\1\63\1\31\2\63\1\17\1\25\2\63\1\25\1\112\1\63\2\112"+
        "\1\111\2\112\1\31\1\44\2\63\2\17\1\25\3\63\1\5\1\25\1\112\1\26\1"+
        "\63\1\17\1\112\1\63\3\112\1\42\1\112\1\52\1\63\1\70\1\16\2\111\2"+
        "\63\1\10\1\30\1\111\1\51\2\uffff\1\63\1\31\2\63\1\17\1\25\2\63\1"+
        "\25\1\112\1\63\2\112\1\111\2\112\1\31\1\44\2\63\2\17\1\25\3\63\1"+
        "\5\1\25\1\112\1\26\1\63\1\17\1\112\1\63\1\112";
    static final String DFA4_acceptS =
        "\105\uffff\1\1\1\2\43\uffff";
    static final String DFA4_specialS =
        "\152\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\1",
            "\1\2\3\uffff\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\14\4\uffff\1\15",
            "\1\16\57\uffff\1\17",
            "\1\17",
            "\1\20",
            "\1\21",
            "\1\17",
            "\1\22",
            "\1\23",
            "\1\25\11\uffff\1\24",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\36\53\uffff\1\37\15\uffff\1\35",
            "\1\40",
            "\1\36\71\uffff\1\35",
            "\1\42\26\uffff\1\41",
            "\1\43",
            "\1\36\53\uffff\1\37\15\uffff\1\35",
            "\1\42\26\uffff\1\41",
            "\1\45\11\uffff\1\44",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\36\53\uffff\1\37\15\uffff\1\35",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\36\53\uffff\1\37\15\uffff\1\35",
            "\1\64",
            "\1\66\53\uffff\1\67\15\uffff\1\65",
            "\1\66\71\uffff\1\65",
            "\1\71\67\uffff\1\70",
            "\1\72",
            "\1\71\67\uffff\1\70",
            "\1\73",
            "\1\74",
            "\1\75\4\uffff\1\76",
            "\1\77",
            "\1\100\57\uffff\1\101",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\77\6\uffff\1\105\2\uffff\1\106",
            "\1\101",
            "\1\107",
            "",
            "",
            "\1\110",
            "\1\112\11\uffff\1\111",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\123\53\uffff\1\124\15\uffff\1\122",
            "\1\125",
            "\1\123\71\uffff\1\122",
            "\1\127\26\uffff\1\126",
            "\1\130",
            "\1\123\53\uffff\1\124\15\uffff\1\122",
            "\1\127\26\uffff\1\126",
            "\1\132\11\uffff\1\131",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\123\53\uffff\1\124\15\uffff\1\122",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\123\53\uffff\1\124\15\uffff\1\122",
            "\1\151",
            "\1\66\53\uffff\1\67\15\uffff\1\65"
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

    class DFA4 extends DFA {

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
        public String getDescription() {
            return "107:1: creation_dimension : ( create_statement levels_statement hierarchy_statement datasource_statement | create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement );";
        }
    }
 

    public static final BitSet FOLLOW_parse_in_start158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_source_type_statement_in_parse171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_statement_in_parse193 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_sql_query_in_parse209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATASOURCE_in_source_type_statement220 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_TYPE_in_source_type_statement222 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_COLON_in_source_type_statement224 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_source_type_statement226 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_WITH_in_source_type_statement230 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_INI_in_source_type_statement232 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_FILE_in_source_type_statement234 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_COLON_in_source_type_statement236 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_PATH_in_source_type_statement238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_in_creation_statement248 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_QUESTMARK_in_creation_statement250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_cube_in_creation257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_dimension_in_creation261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_statement_in_creation_cube269 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_datasource_statement_in_creation_cube271 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_measure_statement_in_creation_cube273 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_referdimension_statement_in_creation_cube275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_statement_in_creation_dimension283 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_levels_statement_in_creation_dimension285 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_hierarchy_statement_in_creation_dimension287 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_datasource_statement_in_creation_dimension289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_statement_in_creation_dimension299 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_levels_statement_in_creation_dimension301 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_hierarchy_statement_in_creation_dimension303 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_dimension_type_statement_in_creation_dimension305 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_datasource_statement_in_creation_dimension307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_create_statement314 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_CUBE_in_create_statement316 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_create_statement320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_create_statement363 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_DIMENSION_in_create_statement365 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_create_statement369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATASOURCE_in_datasource_statement397 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_datasource_statement399 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REFERENCES_in_referdimension_statement410 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_DIMENSION_in_referdimension_statement412 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_dimensions_in_referdimension_statement414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MEASURES_in_measure_statement422 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_measures_in_measure_statement424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LIST_in_levels_statement431 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_OF_in_levels_statement433 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_LEVELS_in_levels_statement435 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_LBRACE_in_levels_statement437 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_levels_in_levels_statement439 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_levels_statement441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HIERARCHY_in_hierarchy_statement450 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_hierarchy_in_hierarchy_statement452 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIMENSION_TYPE_in_dimension_type_statement459 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_dimension_type_statement461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dimension_in_dimensions471 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_comma_statement_in_dimensions475 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_dimension_in_dimensions477 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_NAME_in_dimension488 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_AT_in_dimension490 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_sqlfield_in_dimension492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measure_in_measures500 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_comma_statement_in_measures503 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_measure_in_measures505 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_NAME_in_measure515 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_AT_in_measure519 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_PATH_in_measure523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_level_in_levels532 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_comma_statement_in_levels535 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_level_in_levels537 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_CREATE_in_level547 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_LEVEL_in_level549 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_sqlfield_in_level551 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_attributes_statement_in_level553 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_WITH_in_level555 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_ID_in_level557 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_COLON_in_level559 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_level563 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_AND_in_level567 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DESCRIPTION_in_level569 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_COLON_in_level571 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_level575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_attributes_statement584 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ATTRIBUTES_in_attributes_statement586 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_LBRACE_in_attributes_statement588 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_attributes_in_attributes_statement590 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_attributes_statement592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attribute_in_attributes601 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_comma_statement_in_attributes604 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_attribute_in_attributes606 = new BitSet(new long[]{0x0000000000010002L,0x0000000000000400L});
    public static final BitSet FOLLOW_NAME_in_attribute619 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_COLON_in_attribute629 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attribute633 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_datasource_statement_in_attribute642 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_attribute653 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_DOT_in_attribute655 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attribute659 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_COLON_in_attribute669 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_attribute673 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_datasource_statement_in_attribute682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_comma_statement691 = new BitSet(new long[]{0x0000000000010000L,0x0000000000000400L});
    public static final BitSet FOLLOW_COMMA_in_comma_statement694 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_WS_in_comma_statement696 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_NAME_in_hierarchy706 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_CHILDOF_in_hierarchy711 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_hierarchy715 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_NAME_in_sqlfield730 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_DOT_in_sqlfield732 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_sqlfield736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sqlfield750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATH_in_sqlfield775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_select_statement_in_sql_query795 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_from_statement_in_sql_query809 = new BitSet(new long[]{0x0000000100000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_where_statement_in_sql_query822 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_group_statement_in_sql_query835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_select_statement853 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_grouppers_in_select_statement855 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_COMMA_in_select_statement857 = new BitSet(new long[]{0x0002800000020200L,0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_statement_in_select_statement859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_aggregate_func_in_aggregate_statement869 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_aggregate_statement871 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_sqlfield_in_aggregate_statement873 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_80_in_aggregate_statement875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUM_in_aggregate_func885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AVG_in_aggregate_func907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_aggregate_func930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MAX_in_aggregate_func952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MIN_in_aggregate_func974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FROM_in_from_statement984 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_tables_in_from_statement986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_in_tables994 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_COMMA_in_tables997 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_table_in_tables999 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_NAME_in_table1011 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_AS_in_table1013 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_table1017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_table1032 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_table1036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_table1048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHERE_in_where_statement1058 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_cond_statement_in_where_statement1062 = new BitSet(new long[]{0x0040000000000022L});
    public static final BitSet FOLLOW_boolean_expr_in_where_statement1067 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_cond_statement_in_where_statement1071 = new BitSet(new long[]{0x0040000000000022L});
    public static final BitSet FOLLOW_sqlfield_in_cond_statement1103 = new BitSet(new long[]{0x0000000000004000L,0x00000000003E0000L});
    public static final BitSet FOLLOW_operator_in_cond_statement1105 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_sqlfield_in_cond_statement1109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sqlfield_in_cond_statement1130 = new BitSet(new long[]{0x0000000000004000L,0x00000000003E0000L});
    public static final BitSet FOLLOW_operator_in_cond_statement1132 = new BitSet(new long[]{0x0000000000000800L,0x0000000000004000L});
    public static final BitSet FOLLOW_quote_statement_in_cond_statement1137 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_NAME_in_cond_statement1139 = new BitSet(new long[]{0x0000000000000800L,0x0000000000004000L});
    public static final BitSet FOLLOW_quote_statement_in_cond_statement1141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sqlfield_in_cond_statement1163 = new BitSet(new long[]{0x0000000000004000L,0x00000000003E0000L});
    public static final BitSet FOLLOW_operator_in_cond_statement1165 = new BitSet(new long[]{0x0000000000000800L,0x0000000000004000L});
    public static final BitSet FOLLOW_quote_statement_in_cond_statement1170 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_Digit_in_cond_statement1173 = new BitSet(new long[]{0x0008000004000000L});
    public static final BitSet FOLLOW_NAME_in_cond_statement1177 = new BitSet(new long[]{0x0000000000000800L,0x0000000000004000L});
    public static final BitSet FOLLOW_quote_statement_in_cond_statement1180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sqlfield_in_cond_statement1202 = new BitSet(new long[]{0x0000000000004000L,0x00000000003E0000L});
    public static final BitSet FOLLOW_operator_in_cond_statement1204 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_Digit_in_cond_statement1209 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_sqlfield_in_cond_statement1233 = new BitSet(new long[]{0x0000000000004000L,0x00000000003E0000L});
    public static final BitSet FOLLOW_operator_in_cond_statement1235 = new BitSet(new long[]{0x0000000000000800L,0x0000000000004000L});
    public static final BitSet FOLLOW_quote_statement_in_cond_statement1240 = new BitSet(new long[]{0x0000000000000800L,0x0000000000004000L});
    public static final BitSet FOLLOW_quote_statement_in_cond_statement1243 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_group_statement1391 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_BY_in_group_statement1393 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_grouppers_in_group_statement1397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sqlfield_in_grouppers1409 = new BitSet(new long[]{0x0000000000010000L,0x0000000000000400L});
    public static final BitSet FOLLOW_comma_statement_in_grouppers1414 = new BitSet(new long[]{0x0108000000000000L});
    public static final BitSet FOLLOW_sqlfield_in_grouppers1418 = new BitSet(new long[]{0x0000000000000002L});

}