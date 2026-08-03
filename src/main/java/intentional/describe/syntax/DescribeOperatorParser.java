// $ANTLR 3.4 DescribeOperator.g 2026-08-03 20:28:13

    package intentional.describe.syntax;
    import intentional.describe.DescribeParams;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class DescribeOperatorParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AGGRFUNC", "AND", "AS", "ASC", "BY", "COMMA", "DESC", "DESCRIBE", "DIGIT", "DOT", "EQUAL", "FOR", "GROUP", "GT", "GTE", "IN", "JOIN", "LBRACE", "LETTER", "LPARENTHESIS", "LT", "LTE", "MINUS", "NEQ", "NOT", "NUMBER", "ON", "ORDER", "PLUS", "RBRACE", "RPARENTHESIS", "SLASH", "STAR", "TEXTVALUE", "WITH", "WORD", "WS"
    };

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
    public static final int NOT=28;
    public static final int NUMBER=29;
    public static final int ON=30;
    public static final int ORDER=31;
    public static final int PLUS=32;
    public static final int RBRACE=33;
    public static final int RPARENTHESIS=34;
    public static final int SLASH=35;
    public static final int STAR=36;
    public static final int TEXTVALUE=37;
    public static final int WITH=38;
    public static final int WORD=39;
    public static final int WS=40;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public DescribeOperatorParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public DescribeOperatorParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return DescribeOperatorParser.tokenNames; }
    public String getGrammarFileName() { return "DescribeOperator.g"; }


    	
    	//Single object to hold everything instead of using multiple ArrayLists and/or Hashmaps here
        private DescribeParams params = new DescribeParams();    
        
        public DescribeParams getParams(){
        	return params;   
        }



    // $ANTLR start "start"
    // DescribeOperator.g:34:1: start : parse ;
    public final void start() throws RecognitionException {
        try {
            // DescribeOperator.g:34:6: ( parse )
            // DescribeOperator.g:34:8: parse
            {

                params = new DescribeParams();


            pushFollow(FOLLOW_parse_in_start172);
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
    // DescribeOperator.g:38:1: parse : singleStatement ( JOIN singleStatement ON joinCol= WORD )* EOF ;
    public final void parse() throws RecognitionException {
        Token joinCol=null;

        try {
            // DescribeOperator.g:39:5: ( singleStatement ( JOIN singleStatement ON joinCol= WORD )* EOF )
            // DescribeOperator.g:39:9: singleStatement ( JOIN singleStatement ON joinCol= WORD )* EOF
            {
            pushFollow(FOLLOW_singleStatement_in_parse186);
            singleStatement();

            state._fsp--;


            // DescribeOperator.g:40:9: ( JOIN singleStatement ON joinCol= WORD )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==JOIN) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // DescribeOperator.g:41:13: JOIN singleStatement ON joinCol= WORD
            	    {
            	    match(input,JOIN,FOLLOW_JOIN_in_parse211); 

            	     
            	                   params.setJoined(true);
            	                   params.setJoinType("JOIN");
            	                

            	    pushFollow(FOLLOW_singleStatement_in_parse240);
            	    singleStatement();

            	    state._fsp--;


            	    match(input,ON,FOLLOW_ON_in_parse255); 

            	    joinCol=(Token)match(input,WORD,FOLLOW_WORD_in_parse259); 

            	     
            	                   params.setJoinCondition((joinCol!=null?joinCol.getText():null));
            	                

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            match(input,EOF,FOLLOW_EOF_in_parse295); 

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



    // $ANTLR start "singleStatement"
    // DescribeOperator.g:55:1: singleStatement : WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( AS queryAlias )? ;
    public final void singleStatement() throws RecognitionException {
        try {
            // DescribeOperator.g:56:5: ( WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( AS queryAlias )? )
            // DescribeOperator.g:56:9: WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( AS queryAlias )?
            {
            match(input,WITH,FOLLOW_WITH_in_singleStatement314); 

            pushFollow(FOLLOW_cubeName_in_singleStatement316);
            cubeName();

            state._fsp--;


            match(input,DESCRIBE,FOLLOW_DESCRIBE_in_singleStatement327); 

            pushFollow(FOLLOW_measureList_in_singleStatement329);
            measureList();

            state._fsp--;


            // DescribeOperator.g:58:9: ( FOR sigmaExpressionsList )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==FOR) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // DescribeOperator.g:58:10: FOR sigmaExpressionsList
                    {
                    match(input,FOR,FOLLOW_FOR_in_singleStatement341); 

                    pushFollow(FOLLOW_sigmaExpressionsList_in_singleStatement343);
                    sigmaExpressionsList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:59:9: ( GROUP BY gammaExpressionsList )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==GROUP) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // DescribeOperator.g:59:10: GROUP BY gammaExpressionsList
                    {
                    match(input,GROUP,FOLLOW_GROUP_in_singleStatement357); 

                    match(input,BY,FOLLOW_BY_in_singleStatement359); 

                    pushFollow(FOLLOW_gammaExpressionsList_in_singleStatement361);
                    gammaExpressionsList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:60:9: ( ORDER BY orderExpressionsList )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ORDER) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // DescribeOperator.g:60:10: ORDER BY orderExpressionsList
                    {
                    match(input,ORDER,FOLLOW_ORDER_in_singleStatement374); 

                    match(input,BY,FOLLOW_BY_in_singleStatement376); 

                    pushFollow(FOLLOW_orderExpressionsList_in_singleStatement378);
                    orderExpressionsList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:61:9: ( AS queryAlias )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==AS) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // DescribeOperator.g:61:10: AS queryAlias
                    {
                    match(input,AS,FOLLOW_AS_in_singleStatement391); 

                    pushFollow(FOLLOW_queryAlias_in_singleStatement393);
                    queryAlias();

                    state._fsp--;


                    }
                    break;

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
    // $ANTLR end "singleStatement"



    // $ANTLR start "cubeName"
    // DescribeOperator.g:65:1: cubeName : WORD ;
    public final void cubeName() throws RecognitionException {
        Token WORD1=null;

        try {
            // DescribeOperator.g:66:5: ( WORD )
            // DescribeOperator.g:66:9: WORD
            {
            WORD1=(Token)match(input,WORD,FOLLOW_WORD_in_cubeName415); 

             
                		if(params.getCubeName().equals("")){
                			params.setCubeName((WORD1!=null?WORD1.getText():null));
                		}else{
                			params.appendToCubeName(" JOIN " + (WORD1!=null?WORD1.getText():null));
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
    // $ANTLR end "cubeName"



    // $ANTLR start "measureList"
    // DescribeOperator.g:76:1: measureList : measureExpression ( COMMA measureExpression )* ;
    public final void measureList() throws RecognitionException {
        try {
            // DescribeOperator.g:77:5: ( measureExpression ( COMMA measureExpression )* )
            // DescribeOperator.g:77:9: measureExpression ( COMMA measureExpression )*
            {
            pushFollow(FOLLOW_measureExpression_in_measureList437);
            measureExpression();

            state._fsp--;


            // DescribeOperator.g:77:27: ( COMMA measureExpression )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==COMMA) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // DescribeOperator.g:77:28: COMMA measureExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_measureList440); 

            	    pushFollow(FOLLOW_measureExpression_in_measureList442);
            	    measureExpression();

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
    // $ANTLR end "measureList"



    // $ANTLR start "measureExpression"
    // DescribeOperator.g:80:1: measureExpression : expr= expression ( options {greedy=true; } : AS alias= WORD )? ;
    public final void measureExpression() throws RecognitionException {
        Token alias=null;
        DescribeOperatorParser.expression_return expr =null;


        try {
            // DescribeOperator.g:81:5: (expr= expression ( options {greedy=true; } : AS alias= WORD )? )
            // DescribeOperator.g:82:6: expr= expression ( options {greedy=true; } : AS alias= WORD )?
            {
            pushFollow(FOLLOW_expression_in_measureExpression470);
            expr=expression();

            state._fsp--;


            // DescribeOperator.g:85:6: ( options {greedy=true; } : AS alias= WORD )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==AS) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==WORD) ) {
                    alt7=1;
                }
            }
            switch (alt7) {
                case 1 :
                    // DescribeOperator.g:85:33: AS alias= WORD
                    {
                    match(input,AS,FOLLOW_AS_in_measureExpression500); 

                    alias=(Token)match(input,WORD,FOLLOW_WORD_in_measureExpression504); 

                    }
                    break;

            }



                		if (alias != null){
                			params.addMeasure((expr!=null?input.toString(expr.start,expr.stop):null) + " AS " + (alias!=null?alias.getText():null));
                		}else{
                			params.addMeasure((expr!=null?input.toString(expr.start,expr.stop):null));
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
    // $ANTLR end "measureExpression"


    public static class expression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "expression"
    // DescribeOperator.g:95:1: expression : term ( ( PLUS | MINUS ) term )* ;
    public final DescribeOperatorParser.expression_return expression() throws RecognitionException {
        DescribeOperatorParser.expression_return retval = new DescribeOperatorParser.expression_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:96:5: ( term ( ( PLUS | MINUS ) term )* )
            // DescribeOperator.g:96:9: term ( ( PLUS | MINUS ) term )*
            {
            pushFollow(FOLLOW_term_in_expression533);
            term();

            state._fsp--;


            // DescribeOperator.g:96:14: ( ( PLUS | MINUS ) term )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==MINUS||LA8_0==PLUS) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // DescribeOperator.g:96:15: ( PLUS | MINUS ) term
            	    {
            	    if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_term_in_expression544);
            	    term();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


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
    // $ANTLR end "expression"



    // $ANTLR start "term"
    // DescribeOperator.g:99:1: term : factor ( ( STAR | SLASH ) factor )* ;
    public final void term() throws RecognitionException {
        try {
            // DescribeOperator.g:100:5: ( factor ( ( STAR | SLASH ) factor )* )
            // DescribeOperator.g:100:9: factor ( ( STAR | SLASH ) factor )*
            {
            pushFollow(FOLLOW_factor_in_term565);
            factor();

            state._fsp--;


            // DescribeOperator.g:100:16: ( ( STAR | SLASH ) factor )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0 >= SLASH && LA9_0 <= STAR)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // DescribeOperator.g:100:17: ( STAR | SLASH ) factor
            	    {
            	    if ( (input.LA(1) >= SLASH && input.LA(1) <= STAR) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_factor_in_term576);
            	    factor();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
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
    // $ANTLR end "term"



    // $ANTLR start "factor"
    // DescribeOperator.g:103:1: factor : ( WORD | NUMBER | AGGRFUNC LPARENTHESIS expression RPARENTHESIS | LPARENTHESIS expression RPARENTHESIS );
    public final void factor() throws RecognitionException {
        try {
            // DescribeOperator.g:104:5: ( WORD | NUMBER | AGGRFUNC LPARENTHESIS expression RPARENTHESIS | LPARENTHESIS expression RPARENTHESIS )
            int alt10=4;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt10=1;
                }
                break;
            case NUMBER:
                {
                alt10=2;
                }
                break;
            case AGGRFUNC:
                {
                alt10=3;
                }
                break;
            case LPARENTHESIS:
                {
                alt10=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
                case 1 :
                    // DescribeOperator.g:104:9: WORD
                    {
                    match(input,WORD,FOLLOW_WORD_in_factor597); 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:105:9: NUMBER
                    {
                    match(input,NUMBER,FOLLOW_NUMBER_in_factor607); 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:106:9: AGGRFUNC LPARENTHESIS expression RPARENTHESIS
                    {
                    match(input,AGGRFUNC,FOLLOW_AGGRFUNC_in_factor617); 

                    match(input,LPARENTHESIS,FOLLOW_LPARENTHESIS_in_factor619); 

                    pushFollow(FOLLOW_expression_in_factor621);
                    expression();

                    state._fsp--;


                    match(input,RPARENTHESIS,FOLLOW_RPARENTHESIS_in_factor623); 

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:107:9: LPARENTHESIS expression RPARENTHESIS
                    {
                    match(input,LPARENTHESIS,FOLLOW_LPARENTHESIS_in_factor636); 

                    pushFollow(FOLLOW_expression_in_factor638);
                    expression();

                    state._fsp--;


                    match(input,RPARENTHESIS,FOLLOW_RPARENTHESIS_in_factor640); 

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
    // $ANTLR end "factor"



    // $ANTLR start "sigmaExpressionsList"
    // DescribeOperator.g:111:1: sigmaExpressionsList : sigmaExpression ( AND sigmaExpression )* ;
    public final void sigmaExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:112:5: ( sigmaExpression ( AND sigmaExpression )* )
            // DescribeOperator.g:112:9: sigmaExpression ( AND sigmaExpression )*
            {
            pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressionsList660);
            sigmaExpression();

            state._fsp--;


            // DescribeOperator.g:112:25: ( AND sigmaExpression )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==AND) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // DescribeOperator.g:112:26: AND sigmaExpression
            	    {
            	    match(input,AND,FOLLOW_AND_in_sigmaExpressionsList663); 

            	    pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressionsList665);
            	    sigmaExpression();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop11;
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
    // $ANTLR end "sigmaExpressionsList"



    // $ANTLR start "sigmaExpression"
    // DescribeOperator.g:115:1: sigmaExpression : (w= WORD op= comparator v= val |w= WORD IN LBRACE vList= valueList RBRACE |w= WORD NOT IN LBRACE vList= valueList RBRACE |w= WORD WITH nested= nestedFilter );
    public final void sigmaExpression() throws RecognitionException {
        Token w=null;
        String op =null;

        String v =null;

        DescribeOperatorParser.valueList_return vList =null;

        DescribeOperatorParser.nestedFilter_return nested =null;


        try {
            // DescribeOperator.g:116:5: (w= WORD op= comparator v= val |w= WORD IN LBRACE vList= valueList RBRACE |w= WORD NOT IN LBRACE vList= valueList RBRACE |w= WORD WITH nested= nestedFilter )
            int alt12=4;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==WORD) ) {
                switch ( input.LA(2) ) {
                case IN:
                    {
                    alt12=2;
                    }
                    break;
                case NOT:
                    {
                    alt12=3;
                    }
                    break;
                case WITH:
                    {
                    alt12=4;
                    }
                    break;
                case EQUAL:
                case GT:
                case GTE:
                case LT:
                case LTE:
                case NEQ:
                    {
                    alt12=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }
            switch (alt12) {
                case 1 :
                    // DescribeOperator.g:117:9: w= WORD op= comparator v= val
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression697); 

                    pushFollow(FOLLOW_comparator_in_sigmaExpression701);
                    op=comparator();

                    state._fsp--;


                    pushFollow(FOLLOW_val_in_sigmaExpression705);
                    v=val();

                    state._fsp--;



                                params.addSigmaExpression((w!=null?w.getText():null) + op + v);
                                params.addSigmaValue((w!=null?w.getText():null), v);
                            

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:122:9: w= WORD IN LBRACE vList= valueList RBRACE
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression728); 

                    match(input,IN,FOLLOW_IN_in_sigmaExpression730); 

                    match(input,LBRACE,FOLLOW_LBRACE_in_sigmaExpression732); 

                    pushFollow(FOLLOW_valueList_in_sigmaExpression736);
                    vList=valueList();

                    state._fsp--;


                    match(input,RBRACE,FOLLOW_RBRACE_in_sigmaExpression738); 


                                params.addSigmaExpression((w!=null?w.getText():null) + " IN {" + (vList!=null?input.toString(vList.start,vList.stop):null) + "}");
                                params.addSigmaValue((w!=null?w.getText():null), (vList!=null?input.toString(vList.start,vList.stop):null));
                            

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:127:9: w= WORD NOT IN LBRACE vList= valueList RBRACE
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression761); 

                    match(input,NOT,FOLLOW_NOT_in_sigmaExpression763); 

                    match(input,IN,FOLLOW_IN_in_sigmaExpression765); 

                    match(input,LBRACE,FOLLOW_LBRACE_in_sigmaExpression767); 

                    pushFollow(FOLLOW_valueList_in_sigmaExpression771);
                    vList=valueList();

                    state._fsp--;


                    match(input,RBRACE,FOLLOW_RBRACE_in_sigmaExpression773); 


                                params.addSigmaExpression((w!=null?w.getText():null) + " NOT IN {" + (vList!=null?input.toString(vList.start,vList.stop):null) + "}");
                                params.addSigmaValue((w!=null?w.getText():null), (vList!=null?input.toString(vList.start,vList.stop):null));
                            

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:132:9: w= WORD WITH nested= nestedFilter
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression796); 

                    match(input,WITH,FOLLOW_WITH_in_sigmaExpression798); 

                    pushFollow(FOLLOW_nestedFilter_in_sigmaExpression802);
                    nested=nestedFilter();

                    state._fsp--;



                                params.addSigmaExpression((w!=null?w.getText():null) + " WITH " + (nested!=null?input.toString(nested.start,nested.stop):null));
                                //Stores the whole nested condition as "value"
                                params.addSigmaValue((w!=null?w.getText():null), (nested!=null?input.toString(nested.start,nested.stop):null));
                            

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
    // $ANTLR end "sigmaExpression"


    public static class nestedFilter_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "nestedFilter"
    // DescribeOperator.g:139:1: nestedFilter : WORD comparator val ;
    public final DescribeOperatorParser.nestedFilter_return nestedFilter() throws RecognitionException {
        DescribeOperatorParser.nestedFilter_return retval = new DescribeOperatorParser.nestedFilter_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:140:5: ( WORD comparator val )
            // DescribeOperator.g:140:9: WORD comparator val
            {
            match(input,WORD,FOLLOW_WORD_in_nestedFilter823); 

            pushFollow(FOLLOW_comparator_in_nestedFilter825);
            comparator();

            state._fsp--;


            pushFollow(FOLLOW_val_in_nestedFilter827);
            val();

            state._fsp--;


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
    // $ANTLR end "nestedFilter"


    public static class valueList_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "valueList"
    // DescribeOperator.g:143:1: valueList : val ( COMMA val )* ;
    public final DescribeOperatorParser.valueList_return valueList() throws RecognitionException {
        DescribeOperatorParser.valueList_return retval = new DescribeOperatorParser.valueList_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:144:5: ( val ( COMMA val )* )
            // DescribeOperator.g:144:9: val ( COMMA val )*
            {
            pushFollow(FOLLOW_val_in_valueList846);
            val();

            state._fsp--;


            // DescribeOperator.g:144:13: ( COMMA val )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==COMMA) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // DescribeOperator.g:144:14: COMMA val
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_valueList849); 

            	    pushFollow(FOLLOW_val_in_valueList851);
            	    val();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


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
    // $ANTLR end "valueList"



    // $ANTLR start "gammaExpressionsList"
    // DescribeOperator.g:148:1: gammaExpressionsList : gammaExpression ( COMMA gammaExpression )* ;
    public final void gammaExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:149:5: ( gammaExpression ( COMMA gammaExpression )* )
            // DescribeOperator.g:149:9: gammaExpression ( COMMA gammaExpression )*
            {
            pushFollow(FOLLOW_gammaExpression_in_gammaExpressionsList873);
            gammaExpression();

            state._fsp--;


            // DescribeOperator.g:149:25: ( COMMA gammaExpression )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==COMMA) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // DescribeOperator.g:149:26: COMMA gammaExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_gammaExpressionsList876); 

            	    pushFollow(FOLLOW_gammaExpression_in_gammaExpressionsList878);
            	    gammaExpression();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop14;
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
    // $ANTLR end "gammaExpressionsList"



    // $ANTLR start "gammaExpression"
    // DescribeOperator.g:152:1: gammaExpression : WORD ;
    public final void gammaExpression() throws RecognitionException {
        Token WORD2=null;

        try {
            // DescribeOperator.g:153:5: ( WORD )
            // DescribeOperator.g:153:9: WORD
            {
            WORD2=(Token)match(input,WORD,FOLLOW_WORD_in_gammaExpression899); 

             params.addGammaExpression((WORD2!=null?WORD2.getText():null)); 

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
    // $ANTLR end "gammaExpression"



    // $ANTLR start "orderExpressionsList"
    // DescribeOperator.g:156:1: orderExpressionsList : orderExpression ( COMMA orderExpression )* ;
    public final void orderExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:157:2: ( orderExpression ( COMMA orderExpression )* )
            // DescribeOperator.g:157:4: orderExpression ( COMMA orderExpression )*
            {
            pushFollow(FOLLOW_orderExpression_in_orderExpressionsList915);
            orderExpression();

            state._fsp--;


            // DescribeOperator.g:157:20: ( COMMA orderExpression )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // DescribeOperator.g:157:21: COMMA orderExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_orderExpressionsList918); 

            	    pushFollow(FOLLOW_orderExpression_in_orderExpressionsList920);
            	    orderExpression();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop15;
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
    // $ANTLR end "orderExpressionsList"



    // $ANTLR start "orderExpression"
    // DescribeOperator.g:160:1: orderExpression : w= WORD (dir= sortDirection )? ;
    public final void orderExpression() throws RecognitionException {
        Token w=null;
        DescribeOperatorParser.sortDirection_return dir =null;


        try {
            // DescribeOperator.g:161:2: (w= WORD (dir= sortDirection )? )
            // DescribeOperator.g:161:4: w= WORD (dir= sortDirection )?
            {
            w=(Token)match(input,WORD,FOLLOW_WORD_in_orderExpression935); 

            // DescribeOperator.g:161:11: (dir= sortDirection )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ASC||LA16_0==DESC) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // DescribeOperator.g:161:12: dir= sortDirection
                    {
                    pushFollow(FOLLOW_sortDirection_in_orderExpression940);
                    dir=sortDirection();

                    state._fsp--;


                    }
                    break;

            }



            			String direction = ((dir!=null?input.toString(dir.start,dir.stop):null) == null) ? "" : " " + (dir!=null?input.toString(dir.start,dir.stop):null);
            			params.addOrderExpression((w!=null?w.getText():null) + direction);
            		

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
    // $ANTLR end "orderExpression"


    public static class sortDirection_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "sortDirection"
    // DescribeOperator.g:167:1: sortDirection : ( ASC | DESC );
    public final DescribeOperatorParser.sortDirection_return sortDirection() throws RecognitionException {
        DescribeOperatorParser.sortDirection_return retval = new DescribeOperatorParser.sortDirection_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:168:2: ( ASC | DESC )
            // DescribeOperator.g:
            {
            if ( input.LA(1)==ASC||input.LA(1)==DESC ) {
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
    // $ANTLR end "sortDirection"



    // $ANTLR start "queryAlias"
    // DescribeOperator.g:172:1: queryAlias : WORD ;
    public final void queryAlias() throws RecognitionException {
        Token WORD3=null;

        try {
            // DescribeOperator.g:173:2: ( WORD )
            // DescribeOperator.g:173:5: WORD
            {
            WORD3=(Token)match(input,WORD,FOLLOW_WORD_in_queryAlias972); 

             params.setQueryAlias((WORD3!=null?WORD3.getText():null)); 

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
    // $ANTLR end "queryAlias"



    // $ANTLR start "comparator"
    // DescribeOperator.g:176:1: comparator returns [String text] : ( EQUAL | GT | LT | GTE | LTE | NEQ );
    public final String comparator() throws RecognitionException {
        String text = null;


        try {
            // DescribeOperator.g:177:5: ( EQUAL | GT | LT | GTE | LTE | NEQ )
            int alt17=6;
            switch ( input.LA(1) ) {
            case EQUAL:
                {
                alt17=1;
                }
                break;
            case GT:
                {
                alt17=2;
                }
                break;
            case LT:
                {
                alt17=3;
                }
                break;
            case GTE:
                {
                alt17=4;
                }
                break;
            case LTE:
                {
                alt17=5;
                }
                break;
            case NEQ:
                {
                alt17=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }

            switch (alt17) {
                case 1 :
                    // DescribeOperator.g:177:7: EQUAL
                    {
                    match(input,EQUAL,FOLLOW_EQUAL_in_comparator992); 

                     text = "="; 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:177:32: GT
                    {
                    match(input,GT,FOLLOW_GT_in_comparator998); 

                     text = ">"; 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:177:54: LT
                    {
                    match(input,LT,FOLLOW_LT_in_comparator1004); 

                     text = "<"; 

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:178:7: GTE
                    {
                    match(input,GTE,FOLLOW_GTE_in_comparator1015); 

                     text = ">="; 

                    }
                    break;
                case 5 :
                    // DescribeOperator.g:178:31: LTE
                    {
                    match(input,LTE,FOLLOW_LTE_in_comparator1021); 

                     text = "<="; 

                    }
                    break;
                case 6 :
                    // DescribeOperator.g:178:55: NEQ
                    {
                    match(input,NEQ,FOLLOW_NEQ_in_comparator1027); 

                     text = "!="; 

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
        return text;
    }
    // $ANTLR end "comparator"



    // $ANTLR start "val"
    // DescribeOperator.g:180:1: val returns [String text] : ( WORD | NUMBER | TEXTVALUE );
    public final String val() throws RecognitionException {
        String text = null;


        Token WORD4=null;
        Token NUMBER5=null;
        Token TEXTVALUE6=null;

        try {
            // DescribeOperator.g:181:5: ( WORD | NUMBER | TEXTVALUE )
            int alt18=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt18=1;
                }
                break;
            case NUMBER:
                {
                alt18=2;
                }
                break;
            case TEXTVALUE:
                {
                alt18=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // DescribeOperator.g:181:7: WORD
                    {
                    WORD4=(Token)match(input,WORD,FOLLOW_WORD_in_val1045); 

                     text = (WORD4!=null?WORD4.getText():null); 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:182:7: NUMBER
                    {
                    NUMBER5=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_val1056); 

                     text = (NUMBER5!=null?NUMBER5.getText():null); 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:183:7: TEXTVALUE
                    {
                    TEXTVALUE6=(Token)match(input,TEXTVALUE,FOLLOW_TEXTVALUE_in_val1067); 

                     text = (TEXTVALUE6!=null?TEXTVALUE6.getText():null); 

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
        return text;
    }
    // $ANTLR end "val"

    // Delegated rules


 

    public static final BitSet FOLLOW_parse_in_start172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_singleStatement_in_parse186 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_JOIN_in_parse211 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_singleStatement_in_parse240 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_ON_in_parse255 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_WORD_in_parse259 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_EOF_in_parse295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_singleStatement314 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_cubeName_in_singleStatement316 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_DESCRIBE_in_singleStatement327 = new BitSet(new long[]{0x0000008020800010L});
    public static final BitSet FOLLOW_measureList_in_singleStatement329 = new BitSet(new long[]{0x0000000080018042L});
    public static final BitSet FOLLOW_FOR_in_singleStatement341 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_sigmaExpressionsList_in_singleStatement343 = new BitSet(new long[]{0x0000000080010042L});
    public static final BitSet FOLLOW_GROUP_in_singleStatement357 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BY_in_singleStatement359 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_gammaExpressionsList_in_singleStatement361 = new BitSet(new long[]{0x0000000080000042L});
    public static final BitSet FOLLOW_ORDER_in_singleStatement374 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BY_in_singleStatement376 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_orderExpressionsList_in_singleStatement378 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AS_in_singleStatement391 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_queryAlias_in_singleStatement393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_cubeName415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measureExpression_in_measureList437 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_measureList440 = new BitSet(new long[]{0x0000008020800010L});
    public static final BitSet FOLLOW_measureExpression_in_measureList442 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_expression_in_measureExpression470 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AS_in_measureExpression500 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_WORD_in_measureExpression504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression533 = new BitSet(new long[]{0x0000000104000002L});
    public static final BitSet FOLLOW_set_in_expression536 = new BitSet(new long[]{0x0000008020800010L});
    public static final BitSet FOLLOW_term_in_expression544 = new BitSet(new long[]{0x0000000104000002L});
    public static final BitSet FOLLOW_factor_in_term565 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_set_in_term568 = new BitSet(new long[]{0x0000008020800010L});
    public static final BitSet FOLLOW_factor_in_term576 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_WORD_in_factor597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_factor607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGRFUNC_in_factor617 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_LPARENTHESIS_in_factor619 = new BitSet(new long[]{0x0000008020800010L});
    public static final BitSet FOLLOW_expression_in_factor621 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RPARENTHESIS_in_factor623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPARENTHESIS_in_factor636 = new BitSet(new long[]{0x0000008020800010L});
    public static final BitSet FOLLOW_expression_in_factor638 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RPARENTHESIS_in_factor640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressionsList660 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_sigmaExpressionsList663 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressionsList665 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression697 = new BitSet(new long[]{0x000000000B064000L});
    public static final BitSet FOLLOW_comparator_in_sigmaExpression701 = new BitSet(new long[]{0x000000A020000000L});
    public static final BitSet FOLLOW_val_in_sigmaExpression705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression728 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_IN_in_sigmaExpression730 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACE_in_sigmaExpression732 = new BitSet(new long[]{0x000000A020000000L});
    public static final BitSet FOLLOW_valueList_in_sigmaExpression736 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RBRACE_in_sigmaExpression738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression761 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NOT_in_sigmaExpression763 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_IN_in_sigmaExpression765 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACE_in_sigmaExpression767 = new BitSet(new long[]{0x000000A020000000L});
    public static final BitSet FOLLOW_valueList_in_sigmaExpression771 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RBRACE_in_sigmaExpression773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression796 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_WITH_in_sigmaExpression798 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_nestedFilter_in_sigmaExpression802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_nestedFilter823 = new BitSet(new long[]{0x000000000B064000L});
    public static final BitSet FOLLOW_comparator_in_nestedFilter825 = new BitSet(new long[]{0x000000A020000000L});
    public static final BitSet FOLLOW_val_in_nestedFilter827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_val_in_valueList846 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_valueList849 = new BitSet(new long[]{0x000000A020000000L});
    public static final BitSet FOLLOW_val_in_valueList851 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_gammaExpression_in_gammaExpressionsList873 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_gammaExpressionsList876 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_gammaExpression_in_gammaExpressionsList878 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_gammaExpression899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderExpression_in_orderExpressionsList915 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_orderExpressionsList918 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_orderExpression_in_orderExpressionsList920 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_orderExpression935 = new BitSet(new long[]{0x0000000000000482L});
    public static final BitSet FOLLOW_sortDirection_in_orderExpression940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_queryAlias972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_comparator992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_comparator998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_comparator1004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GTE_in_comparator1015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTE_in_comparator1021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEQ_in_comparator1027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_val1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_val1056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEXTVALUE_in_val1067 = new BitSet(new long[]{0x0000000000000002L});

}