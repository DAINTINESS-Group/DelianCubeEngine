// $ANTLR 3.4 CubeSql.g 2022-05-30 21:00:11

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "AND", "AS", "AT", "ATTRIBUTES", "B", "BACKSLASH", "C", "CHILDOF", "COLON", "COMMA", "CREATE", "CUBE", "D", "DATASOURCE", "DESCRIPTION", "DIMENSION", "DIMENSION_TYPE", "DOT", "Digit", "E", "F", "FILE", "G", "H", "HIERARCHY", "I", "ID", "INI", "J", "K", "L", "LBRACE", "LEVEL", "LEVELS", "LIST", "Letter", "M", "MEASURES", "N", "NAME", "O", "OF", "P", "PATH", "Q", "QUESTMARK", "R", "RBRACE", "REFERENCES", "S", "SLASH", "T", "TYPE", "U", "UNDERSCORE", "V", "W", "WITH", "WS", "X", "Y", "Z"
    };

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
      ArrayList<String> measurelst;
      ArrayList<String> measurefields;
      String tmp_con;
      String dimensionType;
      



    // $ANTLR start "start"
    // CubeSql.g:51:1: start : parse ;
    public final void start() throws RecognitionException {
        try {
            // CubeSql.g:51:7: ( parse )
            // CubeSql.g:51:8: parse
            {

                mode="";
                tmp_con="";
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
                measurelst=new ArrayList<String>();
                measurefields=new ArrayList<String>();
                dimensionsAtCubeDataSource=new ArrayList<String>();
              

            pushFollow(FOLLOW_parse_in_start138);
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
    // CubeSql.g:69:1: parse : ( source_type_statement | ( creation_statement )* );
    public final void parse() throws RecognitionException {
        try {
            // CubeSql.g:69:7: ( source_type_statement | ( creation_statement )* )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==DATASOURCE) ) {
                alt2=1;
            }
            else if ( (LA2_0==EOF||LA2_0==CREATE) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }
            switch (alt2) {
                case 1 :
                    // CubeSql.g:69:11: source_type_statement
                    {
                    sourceType=""; iniFilePath="";

                    pushFollow(FOLLOW_source_type_statement_in_parse151);
                    source_type_statement();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // CubeSql.g:70:5: ( creation_statement )*
                    {
                    // CubeSql.g:70:5: ( creation_statement )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==CREATE) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // CubeSql.g:70:6: creation_statement
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
                    	               

                    	    pushFollow(FOLLOW_creation_statement_in_parse173);
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
    // CubeSql.g:89:1: source_type_statement : DATASOURCE TYPE COLON NAME WITH INI FILE COLON PATH ;
    public final void source_type_statement() throws RecognitionException {
        Token NAME1=null;
        Token PATH2=null;

        try {
            // CubeSql.g:89:22: ( DATASOURCE TYPE COLON NAME WITH INI FILE COLON PATH )
            // CubeSql.g:89:24: DATASOURCE TYPE COLON NAME WITH INI FILE COLON PATH
            {
            match(input,DATASOURCE,FOLLOW_DATASOURCE_in_source_type_statement188); 

            match(input,TYPE,FOLLOW_TYPE_in_source_type_statement190); 

            match(input,COLON,FOLLOW_COLON_in_source_type_statement192); 

            NAME1=(Token)match(input,NAME,FOLLOW_NAME_in_source_type_statement194); 

            sourceType=(NAME1!=null?NAME1.getText():null);

            match(input,WITH,FOLLOW_WITH_in_source_type_statement198); 

            match(input,INI,FOLLOW_INI_in_source_type_statement200); 

            match(input,FILE,FOLLOW_FILE_in_source_type_statement202); 

            match(input,COLON,FOLLOW_COLON_in_source_type_statement204); 

            PATH2=(Token)match(input,PATH,FOLLOW_PATH_in_source_type_statement206); 

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
    // CubeSql.g:91:1: creation_statement : creation QUESTMARK ;
    public final void creation_statement() throws RecognitionException {
        try {
            // CubeSql.g:91:20: ( creation QUESTMARK )
            // CubeSql.g:91:22: creation QUESTMARK
            {
            pushFollow(FOLLOW_creation_in_creation_statement216);
            creation();

            state._fsp--;


            match(input,QUESTMARK,FOLLOW_QUESTMARK_in_creation_statement218); 

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
    // CubeSql.g:93:1: creation : ( creation_cube | creation_dimension );
    public final void creation() throws RecognitionException {
        try {
            // CubeSql.g:93:9: ( creation_cube | creation_dimension )
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
                    // CubeSql.g:93:11: creation_cube
                    {
                    pushFollow(FOLLOW_creation_cube_in_creation225);
                    creation_cube();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // CubeSql.g:93:27: creation_dimension
                    {
                    pushFollow(FOLLOW_creation_dimension_in_creation229);
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
    // CubeSql.g:95:1: creation_cube : create_statement datasource_statement measure_statement referdimension_statement ;
    public final void creation_cube() throws RecognitionException {
        try {
            // CubeSql.g:95:15: ( create_statement datasource_statement measure_statement referdimension_statement )
            // CubeSql.g:95:17: create_statement datasource_statement measure_statement referdimension_statement
            {
            pushFollow(FOLLOW_create_statement_in_creation_cube237);
            create_statement();

            state._fsp--;


            pushFollow(FOLLOW_datasource_statement_in_creation_cube239);
            datasource_statement();

            state._fsp--;


            pushFollow(FOLLOW_measure_statement_in_creation_cube241);
            measure_statement();

            state._fsp--;


            pushFollow(FOLLOW_referdimension_statement_in_creation_cube243);
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
    // CubeSql.g:97:1: creation_dimension : ( create_statement levels_statement hierarchy_statement datasource_statement | create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement );
    public final void creation_dimension() throws RecognitionException {
        try {
            // CubeSql.g:97:20: ( create_statement levels_statement hierarchy_statement datasource_statement | create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement )
            int alt4=2;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // CubeSql.g:97:22: create_statement levels_statement hierarchy_statement datasource_statement
                    {
                    pushFollow(FOLLOW_create_statement_in_creation_dimension251);
                    create_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_levels_statement_in_creation_dimension253);
                    levels_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_hierarchy_statement_in_creation_dimension255);
                    hierarchy_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_datasource_statement_in_creation_dimension257);
                    datasource_statement();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // CubeSql.g:98:7: create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement
                    {
                    pushFollow(FOLLOW_create_statement_in_creation_dimension267);
                    create_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_levels_statement_in_creation_dimension269);
                    levels_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_hierarchy_statement_in_creation_dimension271);
                    hierarchy_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_dimension_type_statement_in_creation_dimension273);
                    dimension_type_statement();

                    state._fsp--;


                    pushFollow(FOLLOW_datasource_statement_in_creation_dimension275);
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
    // CubeSql.g:100:1: create_statement : ( CREATE CUBE NAME | CREATE DIMENSION NAME );
    public final void create_statement() throws RecognitionException {
        Token NAME3=null;
        Token NAME4=null;

        try {
            // CubeSql.g:100:17: ( CREATE CUBE NAME | CREATE DIMENSION NAME )
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
                    // CubeSql.g:100:19: CREATE CUBE NAME
                    {
                    match(input,CREATE,FOLLOW_CREATE_in_create_statement282); 

                    match(input,CUBE,FOLLOW_CUBE_in_create_statement284); 

                    mode="Cube";

                    NAME3=(Token)match(input,NAME,FOLLOW_NAME_in_create_statement288); 

                    name_creation=(NAME3!=null?NAME3.getText():null);

                    }
                    break;
                case 2 :
                    // CubeSql.g:102:19: CREATE DIMENSION NAME
                    {
                    match(input,CREATE,FOLLOW_CREATE_in_create_statement331); 

                    match(input,DIMENSION,FOLLOW_DIMENSION_in_create_statement333); 

                    mode="Dimension";

                    NAME4=(Token)match(input,NAME,FOLLOW_NAME_in_create_statement337); 

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
    // CubeSql.g:104:1: datasource_statement : DATASOURCE NAME ;
    public final void datasource_statement() throws RecognitionException {
        Token NAME5=null;

        try {
            // CubeSql.g:104:22: ( DATASOURCE NAME )
            // CubeSql.g:104:24: DATASOURCE NAME
            {
            match(input,DATASOURCE,FOLLOW_DATASOURCE_in_datasource_statement365); 

            NAME5=(Token)match(input,NAME,FOLLOW_NAME_in_datasource_statement367); 

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
    // CubeSql.g:106:1: referdimension_statement : REFERENCES DIMENSION dimensions ;
    public final void referdimension_statement() throws RecognitionException {
        try {
            // CubeSql.g:106:26: ( REFERENCES DIMENSION dimensions )
            // CubeSql.g:106:28: REFERENCES DIMENSION dimensions
            {
            match(input,REFERENCES,FOLLOW_REFERENCES_in_referdimension_statement378); 

            match(input,DIMENSION,FOLLOW_DIMENSION_in_referdimension_statement380); 

            pushFollow(FOLLOW_dimensions_in_referdimension_statement382);
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
    // CubeSql.g:108:1: measure_statement : MEASURES measures ;
    public final void measure_statement() throws RecognitionException {
        try {
            // CubeSql.g:108:19: ( MEASURES measures )
            // CubeSql.g:108:21: MEASURES measures
            {
            match(input,MEASURES,FOLLOW_MEASURES_in_measure_statement390); 

            pushFollow(FOLLOW_measures_in_measure_statement392);
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
    // CubeSql.g:110:1: levels_statement : LIST OF LEVELS LBRACE levels RBRACE ;
    public final void levels_statement() throws RecognitionException {
        try {
            // CubeSql.g:110:17: ( LIST OF LEVELS LBRACE levels RBRACE )
            // CubeSql.g:110:19: LIST OF LEVELS LBRACE levels RBRACE
            {
            match(input,LIST,FOLLOW_LIST_in_levels_statement399); 

            match(input,OF,FOLLOW_OF_in_levels_statement401); 

            match(input,LEVELS,FOLLOW_LEVELS_in_levels_statement403); 

            match(input,LBRACE,FOLLOW_LBRACE_in_levels_statement405); 

            pushFollow(FOLLOW_levels_in_levels_statement407);
            levels();

            state._fsp--;


            match(input,RBRACE,FOLLOW_RBRACE_in_levels_statement409); 

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
    // CubeSql.g:112:1: hierarchy_statement : HIERARCHY hierarchy ;
    public final void hierarchy_statement() throws RecognitionException {
        try {
            // CubeSql.g:112:21: ( HIERARCHY hierarchy )
            // CubeSql.g:112:23: HIERARCHY hierarchy
            {
            match(input,HIERARCHY,FOLLOW_HIERARCHY_in_hierarchy_statement418); 

            pushFollow(FOLLOW_hierarchy_in_hierarchy_statement420);
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
    // CubeSql.g:114:1: dimension_type_statement : DIMENSION_TYPE NAME ;
    public final void dimension_type_statement() throws RecognitionException {
        Token NAME6=null;

        try {
            // CubeSql.g:114:25: ( DIMENSION_TYPE NAME )
            // CubeSql.g:114:27: DIMENSION_TYPE NAME
            {
            match(input,DIMENSION_TYPE,FOLLOW_DIMENSION_TYPE_in_dimension_type_statement427); 

            NAME6=(Token)match(input,NAME,FOLLOW_NAME_in_dimension_type_statement429); 

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
    // CubeSql.g:116:1: dimensions : dimension ( comma_statement dimension )* ;
    public final void dimensions() throws RecognitionException {
        try {
            // CubeSql.g:116:12: ( dimension ( comma_statement dimension )* )
            // CubeSql.g:116:14: dimension ( comma_statement dimension )*
            {
            pushFollow(FOLLOW_dimension_in_dimensions439);
            dimension();

            state._fsp--;


            // CubeSql.g:116:25: ( comma_statement dimension )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==COMMA||LA6_0==WS) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // CubeSql.g:116:26: comma_statement dimension
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_dimensions443);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_dimension_in_dimensions445);
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
    // CubeSql.g:118:1: dimension : NAME AT sqlfield ;
    public final void dimension() throws RecognitionException {
        try {
            // CubeSql.g:118:11: ( NAME AT sqlfield )
            // CubeSql.g:118:13: NAME AT sqlfield
            {
            match(input,NAME,FOLLOW_NAME_in_dimension456); 

            match(input,AT,FOLLOW_AT_in_dimension458); 

            pushFollow(FOLLOW_sqlfield_in_dimension460);
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
    // CubeSql.g:120:1: measures : measure ( comma_statement measure )* ;
    public final void measures() throws RecognitionException {
        try {
            // CubeSql.g:120:10: ( measure ( comma_statement measure )* )
            // CubeSql.g:120:12: measure ( comma_statement measure )*
            {
            pushFollow(FOLLOW_measure_in_measures468);
            measure();

            state._fsp--;


            // CubeSql.g:120:20: ( comma_statement measure )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA||LA7_0==WS) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // CubeSql.g:120:21: comma_statement measure
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_measures471);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_measure_in_measures473);
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
    // CubeSql.g:122:1: measure : NAME AT measureName= PATH ;
    public final void measure() throws RecognitionException {
        Token measureName=null;
        Token NAME7=null;

        try {
            // CubeSql.g:122:9: ( NAME AT measureName= PATH )
            // CubeSql.g:122:11: NAME AT measureName= PATH
            {
            NAME7=(Token)match(input,NAME,FOLLOW_NAME_in_measure483); 

            measurelst.add((NAME7!=null?NAME7.getText():null));

            match(input,AT,FOLLOW_AT_in_measure487); 

            measureName=(Token)match(input,PATH,FOLLOW_PATH_in_measure491); 

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
    // CubeSql.g:124:1: levels : level ( comma_statement level )* ;
    public final void levels() throws RecognitionException {
        try {
            // CubeSql.g:124:7: ( level ( comma_statement level )* )
            // CubeSql.g:124:9: level ( comma_statement level )*
            {
            pushFollow(FOLLOW_level_in_levels500);
            level();

            state._fsp--;


            // CubeSql.g:124:15: ( comma_statement level )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==COMMA||LA8_0==WS) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // CubeSql.g:124:16: comma_statement level
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_levels503);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_level_in_levels505);
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
    // CubeSql.g:126:1: level : CREATE LEVEL sqlfield attributes_statement WITH ID COLON idName= NAME AND DESCRIPTION COLON descrName= NAME ;
    public final void level() throws RecognitionException {
        Token idName=null;
        Token descrName=null;

        try {
            // CubeSql.g:126:7: ( CREATE LEVEL sqlfield attributes_statement WITH ID COLON idName= NAME AND DESCRIPTION COLON descrName= NAME )
            // CubeSql.g:126:9: CREATE LEVEL sqlfield attributes_statement WITH ID COLON idName= NAME AND DESCRIPTION COLON descrName= NAME
            {
            match(input,CREATE,FOLLOW_CREATE_in_level515); 

            match(input,LEVEL,FOLLOW_LEVEL_in_level517); 

            pushFollow(FOLLOW_sqlfield_in_level519);
            sqlfield();

            state._fsp--;


            pushFollow(FOLLOW_attributes_statement_in_level521);
            attributes_statement();

            state._fsp--;


            match(input,WITH,FOLLOW_WITH_in_level523); 

            match(input,ID,FOLLOW_ID_in_level525); 

            match(input,COLON,FOLLOW_COLON_in_level527); 

            idName=(Token)match(input,NAME,FOLLOW_NAME_in_level531); 

            levelID.put(levelName, (idName!=null?idName.getText():null));

            match(input,AND,FOLLOW_AND_in_level535); 

            match(input,DESCRIPTION,FOLLOW_DESCRIPTION_in_level537); 

            match(input,COLON,FOLLOW_COLON_in_level539); 

            descrName=(Token)match(input,NAME,FOLLOW_NAME_in_level543); 

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
    // CubeSql.g:128:1: attributes_statement : WITH ATTRIBUTES LBRACE attributes RBRACE ;
    public final void attributes_statement() throws RecognitionException {
        try {
            // CubeSql.g:128:21: ( WITH ATTRIBUTES LBRACE attributes RBRACE )
            // CubeSql.g:128:23: WITH ATTRIBUTES LBRACE attributes RBRACE
            {
            match(input,WITH,FOLLOW_WITH_in_attributes_statement552); 

            match(input,ATTRIBUTES,FOLLOW_ATTRIBUTES_in_attributes_statement554); 

            match(input,LBRACE,FOLLOW_LBRACE_in_attributes_statement556); 

            pushFollow(FOLLOW_attributes_in_attributes_statement558);
            attributes();

            state._fsp--;


            match(input,RBRACE,FOLLOW_RBRACE_in_attributes_statement560); 

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
    // CubeSql.g:130:1: attributes : attribute ( comma_statement attribute )* ;
    public final void attributes() throws RecognitionException {
        try {
            // CubeSql.g:130:11: ( attribute ( comma_statement attribute )* )
            // CubeSql.g:130:13: attribute ( comma_statement attribute )*
            {
            attributes.clear();

            pushFollow(FOLLOW_attribute_in_attributes569);
            attribute();

            state._fsp--;


            // CubeSql.g:130:45: ( comma_statement attribute )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==COMMA||LA9_0==WS) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // CubeSql.g:130:46: comma_statement attribute
            	    {
            	    pushFollow(FOLLOW_comma_statement_in_attributes572);
            	    comma_statement();

            	    state._fsp--;


            	    pushFollow(FOLLOW_attribute_in_attributes574);
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
    // CubeSql.g:132:1: attribute : (attrName= NAME COLON typeName= NAME datasource_statement |attrName1= NAME DOT attrName2= NAME COLON typeName= NAME datasource_statement );
    public final void attribute() throws RecognitionException {
        Token attrName=null;
        Token typeName=null;
        Token attrName1=null;
        Token attrName2=null;

        try {
            // CubeSql.g:132:10: (attrName= NAME COLON typeName= NAME datasource_statement |attrName1= NAME DOT attrName2= NAME COLON typeName= NAME datasource_statement )
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
                    // CubeSql.g:132:12: attrName= NAME COLON typeName= NAME datasource_statement
                    {
                    attrName=(Token)match(input,NAME,FOLLOW_NAME_in_attribute587); 

                    attributes.add((attrName!=null?attrName.getText():null));

                    match(input,COLON,FOLLOW_COLON_in_attribute597); 

                    typeName=(Token)match(input,NAME,FOLLOW_NAME_in_attribute601); 

                    attributeTypes.put(((attrName!=null?attrName.getText():null)), ((typeName!=null?typeName.getText():null)));

                    pushFollow(FOLLOW_datasource_statement_in_attribute610);
                    datasource_statement();

                    state._fsp--;


                    attributeDatasource.put(((attrName!=null?attrName.getText():null)), datasource_table);

                    }
                    break;
                case 2 :
                    // CubeSql.g:135:6: attrName1= NAME DOT attrName2= NAME COLON typeName= NAME datasource_statement
                    {
                    attrName1=(Token)match(input,NAME,FOLLOW_NAME_in_attribute621); 

                    match(input,DOT,FOLLOW_DOT_in_attribute623); 

                    attrName2=(Token)match(input,NAME,FOLLOW_NAME_in_attribute627); 

                    attributes.add((attrName1!=null?attrName1.getText():null)+"."+(attrName2!=null?attrName2.getText():null));

                    match(input,COLON,FOLLOW_COLON_in_attribute637); 

                    typeName=(Token)match(input,NAME,FOLLOW_NAME_in_attribute641); 

                    attributeTypes.put(((attrName1!=null?attrName1.getText():null)+"."+(attrName2!=null?attrName2.getText():null)), ((typeName!=null?typeName.getText():null)));

                    pushFollow(FOLLOW_datasource_statement_in_attribute650);
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
    // CubeSql.g:139:1: comma_statement : ( WS )* COMMA ( WS )* ;
    public final void comma_statement() throws RecognitionException {
        try {
            // CubeSql.g:139:16: ( ( WS )* COMMA ( WS )* )
            // CubeSql.g:139:18: ( WS )* COMMA ( WS )*
            {
            // CubeSql.g:139:18: ( WS )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==WS) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // CubeSql.g:139:18: WS
            	    {
            	    match(input,WS,FOLLOW_WS_in_comma_statement659); 

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            match(input,COMMA,FOLLOW_COMMA_in_comma_statement662); 

            // CubeSql.g:139:28: ( WS )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==WS) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // CubeSql.g:139:28: WS
            	    {
            	    match(input,WS,FOLLOW_WS_in_comma_statement664); 

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
    // CubeSql.g:141:1: hierarchy : name1= NAME ( CHILDOF name2= NAME )+ ;
    public final void hierarchy() throws RecognitionException {
        Token name1=null;
        Token name2=null;

        try {
            // CubeSql.g:141:10: (name1= NAME ( CHILDOF name2= NAME )+ )
            // CubeSql.g:141:12: name1= NAME ( CHILDOF name2= NAME )+
            {
            name1=(Token)match(input,NAME,FOLLOW_NAME_in_hierarchy674); 

            hierarchylst.add((name1!=null?name1.getText():null));

            // CubeSql.g:141:56: ( CHILDOF name2= NAME )+
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
            	    // CubeSql.g:141:57: CHILDOF name2= NAME
            	    {
            	    match(input,CHILDOF,FOLLOW_CHILDOF_in_hierarchy679); 

            	    name2=(Token)match(input,NAME,FOLLOW_NAME_in_hierarchy683); 

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
    // CubeSql.g:143:1: sqlfield : (name1= NAME DOT name2= NAME | NAME |name= PATH );
    public final void sqlfield() throws RecognitionException {
        Token name1=null;
        Token name2=null;
        Token name=null;
        Token NAME8=null;

        try {
            // CubeSql.g:143:10: (name1= NAME DOT name2= NAME | NAME |name= PATH )
            int alt14=3;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==NAME) ) {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==DOT) ) {
                    alt14=1;
                }
                else if ( (LA14_1==COMMA||LA14_1==QUESTMARK||(LA14_1 >= WITH && LA14_1 <= WS)) ) {
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
                    // CubeSql.g:143:12: name1= NAME DOT name2= NAME
                    {
                    name1=(Token)match(input,NAME,FOLLOW_NAME_in_sqlfield698); 

                    match(input,DOT,FOLLOW_DOT_in_sqlfield700); 

                    name2=(Token)match(input,NAME,FOLLOW_NAME_in_sqlfield704); 

                     tmp_con+=(name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null);
                    									   if(mode.equals("Dimension")){ customlvllst.add((name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null));
                                                           levelName = (name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null);}
                                                           if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add((name1!=null?name1.getText():null)+"."+(name2!=null?name2.getText():null));}

                    }
                    break;
                case 2 :
                    // CubeSql.g:147:11: NAME
                    {
                    NAME8=(Token)match(input,NAME,FOLLOW_NAME_in_sqlfield718); 

                    tmp_con+= (NAME8!=null?NAME8.getText():null);
                            		if(mode.equals("Dimension")){ customlvllst.add((NAME8!=null?NAME8.getText():null)); levelName=(NAME8!=null?NAME8.getText():null);}
                            		if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add((NAME8!=null?NAME8.getText():null));}

                    }
                    break;
                case 3 :
                    // CubeSql.g:151:11: name= PATH
                    {
                    name=(Token)match(input,PATH,FOLLOW_PATH_in_sqlfield743); 

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

    // Delegated rules


    protected DFA4 dfa4 = new DFA4(this);
    static final String DFA4_eotS =
        "\152\uffff";
    static final String DFA4_eofS =
        "\152\uffff";
    static final String DFA4_minS =
        "\1\17\1\20\2\54\2\47\1\56\1\46\1\44\1\17\1\45\1\54\1\26\1\76\1\54"+
        "\1\10\1\76\1\44\1\54\1\15\2\54\1\15\1\22\2\54\1\22\1\16\1\54\1\16"+
        "\1\54\1\76\1\16\1\54\1\15\1\37\2\54\2\15\1\22\3\54\1\5\1\22\1\16"+
        "\1\23\1\54\1\15\1\16\1\54\2\16\1\17\1\35\1\17\1\45\2\54\1\14\1\26"+
        "\1\76\2\54\1\10\1\14\1\76\1\44\2\uffff\1\54\1\15\2\54\1\15\1\22"+
        "\2\54\1\22\1\16\1\54\1\16\1\54\1\76\1\16\1\54\1\15\1\37\2\54\2\15"+
        "\1\22\3\54\1\5\1\22\1\16\1\23\1\54\1\15\1\16\1\54\1\16";
    static final String DFA4_maxS =
        "\1\17\1\24\2\54\2\47\1\56\1\46\1\44\1\17\1\45\1\60\2\76\1\54\1\10"+
        "\1\76\1\44\1\54\1\26\2\54\1\15\1\22\2\54\1\22\1\77\1\54\2\77\1\76"+
        "\2\77\1\26\1\37\2\54\2\15\1\22\3\54\1\5\1\22\1\77\1\23\1\54\1\15"+
        "\1\77\1\54\3\77\1\35\1\77\1\45\1\54\1\60\1\14\2\76\2\54\1\10\1\25"+
        "\1\76\1\44\2\uffff\1\54\1\26\2\54\1\15\1\22\2\54\1\22\1\77\1\54"+
        "\2\77\1\76\2\77\1\26\1\37\2\54\2\15\1\22\3\54\1\5\1\22\1\77\1\23"+
        "\1\54\1\15\1\77\1\54\1\77";
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
            "\1\14\3\uffff\1\15",
            "\1\16\47\uffff\1\17",
            "\1\17",
            "\1\20",
            "\1\21",
            "\1\17",
            "\1\22",
            "\1\23",
            "\1\25\10\uffff\1\24",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\36\45\uffff\1\37\12\uffff\1\35",
            "\1\40",
            "\1\36\60\uffff\1\35",
            "\1\42\22\uffff\1\41",
            "\1\43",
            "\1\36\45\uffff\1\37\12\uffff\1\35",
            "\1\42\22\uffff\1\41",
            "\1\45\10\uffff\1\44",
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
            "\1\36\45\uffff\1\37\12\uffff\1\35",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\36\45\uffff\1\37\12\uffff\1\35",
            "\1\64",
            "\1\66\45\uffff\1\67\12\uffff\1\65",
            "\1\66\60\uffff\1\65",
            "\1\71\57\uffff\1\70",
            "\1\72",
            "\1\71\57\uffff\1\70",
            "\1\73",
            "\1\74",
            "\1\75\3\uffff\1\76",
            "\1\77",
            "\1\100\47\uffff\1\101",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\77\5\uffff\1\105\2\uffff\1\106",
            "\1\101",
            "\1\107",
            "",
            "",
            "\1\110",
            "\1\112\10\uffff\1\111",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\123\45\uffff\1\124\12\uffff\1\122",
            "\1\125",
            "\1\123\60\uffff\1\122",
            "\1\127\22\uffff\1\126",
            "\1\130",
            "\1\123\45\uffff\1\124\12\uffff\1\122",
            "\1\127\22\uffff\1\126",
            "\1\132\10\uffff\1\131",
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
            "\1\123\45\uffff\1\124\12\uffff\1\122",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\123\45\uffff\1\124\12\uffff\1\122",
            "\1\151",
            "\1\66\45\uffff\1\67\12\uffff\1\65"
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
            return "97:1: creation_dimension : ( create_statement levels_statement hierarchy_statement datasource_statement | create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement );";
        }
    }
 

    public static final BitSet FOLLOW_parse_in_start138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_source_type_statement_in_parse151 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_statement_in_parse173 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_DATASOURCE_in_source_type_statement188 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_TYPE_in_source_type_statement190 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COLON_in_source_type_statement192 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_source_type_statement194 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WITH_in_source_type_statement198 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_INI_in_source_type_statement200 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_FILE_in_source_type_statement202 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COLON_in_source_type_statement204 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_PATH_in_source_type_statement206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_in_creation_statement216 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_QUESTMARK_in_creation_statement218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_cube_in_creation225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creation_dimension_in_creation229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_statement_in_creation_cube237 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_datasource_statement_in_creation_cube239 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_measure_statement_in_creation_cube241 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_referdimension_statement_in_creation_cube243 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_statement_in_creation_dimension251 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_levels_statement_in_creation_dimension253 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_hierarchy_statement_in_creation_dimension255 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_datasource_statement_in_creation_dimension257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_statement_in_creation_dimension267 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_levels_statement_in_creation_dimension269 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_hierarchy_statement_in_creation_dimension271 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_dimension_type_statement_in_creation_dimension273 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_datasource_statement_in_creation_dimension275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_create_statement282 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_CUBE_in_create_statement284 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_create_statement288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_create_statement331 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_DIMENSION_in_create_statement333 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_create_statement337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DATASOURCE_in_datasource_statement365 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_datasource_statement367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REFERENCES_in_referdimension_statement378 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_DIMENSION_in_referdimension_statement380 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_dimensions_in_referdimension_statement382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MEASURES_in_measure_statement390 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_measures_in_measure_statement392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LIST_in_levels_statement399 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_OF_in_levels_statement401 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_LEVELS_in_levels_statement403 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_LBRACE_in_levels_statement405 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_levels_in_levels_statement407 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_levels_statement409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HIERARCHY_in_hierarchy_statement418 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_hierarchy_in_hierarchy_statement420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIMENSION_TYPE_in_dimension_type_statement427 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_dimension_type_statement429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dimension_in_dimensions439 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_comma_statement_in_dimensions443 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_dimension_in_dimensions445 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_NAME_in_dimension456 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_AT_in_dimension458 = new BitSet(new long[]{0x0001100000000000L});
    public static final BitSet FOLLOW_sqlfield_in_dimension460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measure_in_measures468 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_comma_statement_in_measures471 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_measure_in_measures473 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_NAME_in_measure483 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_AT_in_measure487 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_PATH_in_measure491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_level_in_levels500 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_comma_statement_in_levels503 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_level_in_levels505 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_CREATE_in_level515 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEVEL_in_level517 = new BitSet(new long[]{0x0001100000000000L});
    public static final BitSet FOLLOW_sqlfield_in_level519 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_attributes_statement_in_level521 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_WITH_in_level523 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_level525 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COLON_in_level527 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_level531 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_AND_in_level535 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_DESCRIPTION_in_level537 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COLON_in_level539 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_level543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_attributes_statement552 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ATTRIBUTES_in_attributes_statement554 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_LBRACE_in_attributes_statement556 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_attributes_in_attributes_statement558 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_RBRACE_in_attributes_statement560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attribute_in_attributes569 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_comma_statement_in_attributes572 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_attribute_in_attributes574 = new BitSet(new long[]{0x8000000000004002L});
    public static final BitSet FOLLOW_NAME_in_attribute587 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COLON_in_attribute597 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_attribute601 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_datasource_statement_in_attribute610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_attribute621 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DOT_in_attribute623 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_attribute627 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COLON_in_attribute637 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_attribute641 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_datasource_statement_in_attribute650 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_comma_statement659 = new BitSet(new long[]{0x8000000000004000L});
    public static final BitSet FOLLOW_COMMA_in_comma_statement662 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_WS_in_comma_statement664 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_NAME_in_hierarchy674 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_CHILDOF_in_hierarchy679 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_hierarchy683 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_NAME_in_sqlfield698 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_DOT_in_sqlfield700 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_NAME_in_sqlfield704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_sqlfield718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PATH_in_sqlfield743 = new BitSet(new long[]{0x0000000000000002L});

}