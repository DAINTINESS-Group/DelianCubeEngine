// $ANTLR 3.4 DescribeOperator.g 2026-01-15 16:15:41

    package intentional.describe.syntax;
    import intentional.describe.DescribeParams;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class DescribeOperatorParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AGGRFUNC", "AND", "AS", "ASC", "BY", "COMMA", "DESC", "DESCRIBE", "DIGIT", "DOT", "EQUAL", "FOR", "GROUP", "GT", "GTE", "IN", "JOIN", "LBRACE", "LETTER", "LPARENTHESIS", "LT", "LTE", "MINUS", "NEQ", "NOT", "NUMBER", "ON", "ORDER", "PLUS", "RBRACE", "RPARENTHESIS", "SLASH", "STAR", "TEXTVALUE", "USING", "WITH", "WORD", "WS"
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
    public static final int USING=38;
    public static final int WITH=39;
    public static final int WORD=40;
    public static final int WS=41;

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
    // DescribeOperator.g:55:1: singleStatement : WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( USING usingList )? ( AS queryAlias )? ;
    public final void singleStatement() throws RecognitionException {
        try {
            // DescribeOperator.g:56:5: ( WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( USING usingList )? ( AS queryAlias )? )
            // DescribeOperator.g:56:9: WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( USING usingList )? ( AS queryAlias )?
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


            // DescribeOperator.g:61:9: ( USING usingList )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==USING) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // DescribeOperator.g:61:10: USING usingList
                    {
                    match(input,USING,FOLLOW_USING_in_singleStatement391); 

                    pushFollow(FOLLOW_usingList_in_singleStatement393);
                    usingList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:62:9: ( AS queryAlias )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==AS) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // DescribeOperator.g:62:10: AS queryAlias
                    {
                    match(input,AS,FOLLOW_AS_in_singleStatement406); 

                    pushFollow(FOLLOW_queryAlias_in_singleStatement408);
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
    // DescribeOperator.g:66:1: cubeName : WORD ;
    public final void cubeName() throws RecognitionException {
        Token WORD1=null;

        try {
            // DescribeOperator.g:67:5: ( WORD )
            // DescribeOperator.g:67:9: WORD
            {
            WORD1=(Token)match(input,WORD,FOLLOW_WORD_in_cubeName430); 

             
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
    // DescribeOperator.g:77:1: measureList : measureExpression ( COMMA measureExpression )* ;
    public final void measureList() throws RecognitionException {
        try {
            // DescribeOperator.g:78:5: ( measureExpression ( COMMA measureExpression )* )
            // DescribeOperator.g:78:9: measureExpression ( COMMA measureExpression )*
            {
            pushFollow(FOLLOW_measureExpression_in_measureList452);
            measureExpression();

            state._fsp--;


            // DescribeOperator.g:78:27: ( COMMA measureExpression )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // DescribeOperator.g:78:28: COMMA measureExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_measureList455); 

            	    pushFollow(FOLLOW_measureExpression_in_measureList457);
            	    measureExpression();

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
    // $ANTLR end "measureList"



    // $ANTLR start "measureExpression"
    // DescribeOperator.g:81:1: measureExpression : expr= expression ( options {greedy=true; } : AS alias= WORD )? ;
    public final void measureExpression() throws RecognitionException {
        Token alias=null;
        DescribeOperatorParser.expression_return expr =null;


        try {
            // DescribeOperator.g:82:5: (expr= expression ( options {greedy=true; } : AS alias= WORD )? )
            // DescribeOperator.g:83:6: expr= expression ( options {greedy=true; } : AS alias= WORD )?
            {
            pushFollow(FOLLOW_expression_in_measureExpression485);
            expr=expression();

            state._fsp--;


            // DescribeOperator.g:86:6: ( options {greedy=true; } : AS alias= WORD )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==AS) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==WORD) ) {
                    alt8=1;
                }
            }
            switch (alt8) {
                case 1 :
                    // DescribeOperator.g:86:33: AS alias= WORD
                    {
                    match(input,AS,FOLLOW_AS_in_measureExpression515); 

                    alias=(Token)match(input,WORD,FOLLOW_WORD_in_measureExpression519); 

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
    // DescribeOperator.g:96:1: expression : term ( ( PLUS | MINUS ) term )* ;
    public final DescribeOperatorParser.expression_return expression() throws RecognitionException {
        DescribeOperatorParser.expression_return retval = new DescribeOperatorParser.expression_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:97:5: ( term ( ( PLUS | MINUS ) term )* )
            // DescribeOperator.g:97:9: term ( ( PLUS | MINUS ) term )*
            {
            pushFollow(FOLLOW_term_in_expression548);
            term();

            state._fsp--;


            // DescribeOperator.g:97:14: ( ( PLUS | MINUS ) term )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==MINUS||LA9_0==PLUS) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // DescribeOperator.g:97:15: ( PLUS | MINUS ) term
            	    {
            	    if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_term_in_expression559);
            	    term();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
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
    // DescribeOperator.g:100:1: term : factor ( ( STAR | SLASH ) factor )* ;
    public final void term() throws RecognitionException {
        try {
            // DescribeOperator.g:101:5: ( factor ( ( STAR | SLASH ) factor )* )
            // DescribeOperator.g:101:9: factor ( ( STAR | SLASH ) factor )*
            {
            pushFollow(FOLLOW_factor_in_term580);
            factor();

            state._fsp--;


            // DescribeOperator.g:101:16: ( ( STAR | SLASH ) factor )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0 >= SLASH && LA10_0 <= STAR)) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // DescribeOperator.g:101:17: ( STAR | SLASH ) factor
            	    {
            	    if ( (input.LA(1) >= SLASH && input.LA(1) <= STAR) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_factor_in_term591);
            	    factor();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
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
    // DescribeOperator.g:104:1: factor : ( WORD | NUMBER | AGGRFUNC LPARENTHESIS expression RPARENTHESIS | LPARENTHESIS expression RPARENTHESIS );
    public final void factor() throws RecognitionException {
        try {
            // DescribeOperator.g:105:5: ( WORD | NUMBER | AGGRFUNC LPARENTHESIS expression RPARENTHESIS | LPARENTHESIS expression RPARENTHESIS )
            int alt11=4;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt11=1;
                }
                break;
            case NUMBER:
                {
                alt11=2;
                }
                break;
            case AGGRFUNC:
                {
                alt11=3;
                }
                break;
            case LPARENTHESIS:
                {
                alt11=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // DescribeOperator.g:105:9: WORD
                    {
                    match(input,WORD,FOLLOW_WORD_in_factor612); 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:106:9: NUMBER
                    {
                    match(input,NUMBER,FOLLOW_NUMBER_in_factor622); 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:107:9: AGGRFUNC LPARENTHESIS expression RPARENTHESIS
                    {
                    match(input,AGGRFUNC,FOLLOW_AGGRFUNC_in_factor632); 

                    match(input,LPARENTHESIS,FOLLOW_LPARENTHESIS_in_factor634); 

                    pushFollow(FOLLOW_expression_in_factor636);
                    expression();

                    state._fsp--;


                    match(input,RPARENTHESIS,FOLLOW_RPARENTHESIS_in_factor638); 

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:108:9: LPARENTHESIS expression RPARENTHESIS
                    {
                    match(input,LPARENTHESIS,FOLLOW_LPARENTHESIS_in_factor651); 

                    pushFollow(FOLLOW_expression_in_factor653);
                    expression();

                    state._fsp--;


                    match(input,RPARENTHESIS,FOLLOW_RPARENTHESIS_in_factor655); 

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
    // DescribeOperator.g:112:1: sigmaExpressionsList : sigmaExpression ( AND sigmaExpression )* ;
    public final void sigmaExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:113:5: ( sigmaExpression ( AND sigmaExpression )* )
            // DescribeOperator.g:113:9: sigmaExpression ( AND sigmaExpression )*
            {
            pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressionsList675);
            sigmaExpression();

            state._fsp--;


            // DescribeOperator.g:113:25: ( AND sigmaExpression )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==AND) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // DescribeOperator.g:113:26: AND sigmaExpression
            	    {
            	    match(input,AND,FOLLOW_AND_in_sigmaExpressionsList678); 

            	    pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressionsList680);
            	    sigmaExpression();

            	    state._fsp--;


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
    // $ANTLR end "sigmaExpressionsList"



    // $ANTLR start "sigmaExpression"
    // DescribeOperator.g:116:1: sigmaExpression : (w= WORD op= comparator v= val |w= WORD IN LBRACE vList= valueList RBRACE |w= WORD NOT IN LBRACE vList= valueList RBRACE |w= WORD WITH nested= nestedFilter );
    public final void sigmaExpression() throws RecognitionException {
        Token w=null;
        String op =null;

        String v =null;

        DescribeOperatorParser.valueList_return vList =null;

        DescribeOperatorParser.nestedFilter_return nested =null;


        try {
            // DescribeOperator.g:117:5: (w= WORD op= comparator v= val |w= WORD IN LBRACE vList= valueList RBRACE |w= WORD NOT IN LBRACE vList= valueList RBRACE |w= WORD WITH nested= nestedFilter )
            int alt13=4;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==WORD) ) {
                switch ( input.LA(2) ) {
                case IN:
                    {
                    alt13=2;
                    }
                    break;
                case NOT:
                    {
                    alt13=3;
                    }
                    break;
                case WITH:
                    {
                    alt13=4;
                    }
                    break;
                case EQUAL:
                case GT:
                case GTE:
                case LT:
                case LTE:
                case NEQ:
                    {
                    alt13=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // DescribeOperator.g:118:9: w= WORD op= comparator v= val
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression712); 

                    pushFollow(FOLLOW_comparator_in_sigmaExpression716);
                    op=comparator();

                    state._fsp--;


                    pushFollow(FOLLOW_val_in_sigmaExpression720);
                    v=val();

                    state._fsp--;



                                params.addSigmaExpression((w!=null?w.getText():null) + op + v);
                                params.addSigmaValue((w!=null?w.getText():null), v);
                            

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:123:9: w= WORD IN LBRACE vList= valueList RBRACE
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression743); 

                    match(input,IN,FOLLOW_IN_in_sigmaExpression745); 

                    match(input,LBRACE,FOLLOW_LBRACE_in_sigmaExpression747); 

                    pushFollow(FOLLOW_valueList_in_sigmaExpression751);
                    vList=valueList();

                    state._fsp--;


                    match(input,RBRACE,FOLLOW_RBRACE_in_sigmaExpression753); 


                                params.addSigmaExpression((w!=null?w.getText():null) + " IN {" + (vList!=null?input.toString(vList.start,vList.stop):null) + "}");
                                params.addSigmaValue((w!=null?w.getText():null), (vList!=null?input.toString(vList.start,vList.stop):null));
                            

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:128:9: w= WORD NOT IN LBRACE vList= valueList RBRACE
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression776); 

                    match(input,NOT,FOLLOW_NOT_in_sigmaExpression778); 

                    match(input,IN,FOLLOW_IN_in_sigmaExpression780); 

                    match(input,LBRACE,FOLLOW_LBRACE_in_sigmaExpression782); 

                    pushFollow(FOLLOW_valueList_in_sigmaExpression786);
                    vList=valueList();

                    state._fsp--;


                    match(input,RBRACE,FOLLOW_RBRACE_in_sigmaExpression788); 


                                params.addSigmaExpression((w!=null?w.getText():null) + " NOT IN {" + (vList!=null?input.toString(vList.start,vList.stop):null) + "}");
                                params.addSigmaValue((w!=null?w.getText():null), (vList!=null?input.toString(vList.start,vList.stop):null));
                            

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:133:9: w= WORD WITH nested= nestedFilter
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression811); 

                    match(input,WITH,FOLLOW_WITH_in_sigmaExpression813); 

                    pushFollow(FOLLOW_nestedFilter_in_sigmaExpression817);
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
    // DescribeOperator.g:140:1: nestedFilter : WORD comparator val ;
    public final DescribeOperatorParser.nestedFilter_return nestedFilter() throws RecognitionException {
        DescribeOperatorParser.nestedFilter_return retval = new DescribeOperatorParser.nestedFilter_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:141:5: ( WORD comparator val )
            // DescribeOperator.g:141:9: WORD comparator val
            {
            match(input,WORD,FOLLOW_WORD_in_nestedFilter838); 

            pushFollow(FOLLOW_comparator_in_nestedFilter840);
            comparator();

            state._fsp--;


            pushFollow(FOLLOW_val_in_nestedFilter842);
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
    // DescribeOperator.g:144:1: valueList : val ( COMMA val )* ;
    public final DescribeOperatorParser.valueList_return valueList() throws RecognitionException {
        DescribeOperatorParser.valueList_return retval = new DescribeOperatorParser.valueList_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:145:5: ( val ( COMMA val )* )
            // DescribeOperator.g:145:9: val ( COMMA val )*
            {
            pushFollow(FOLLOW_val_in_valueList861);
            val();

            state._fsp--;


            // DescribeOperator.g:145:13: ( COMMA val )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==COMMA) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // DescribeOperator.g:145:14: COMMA val
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_valueList864); 

            	    pushFollow(FOLLOW_val_in_valueList866);
            	    val();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop14;
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
    // DescribeOperator.g:149:1: gammaExpressionsList : gammaExpression ( COMMA gammaExpression )* ;
    public final void gammaExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:150:5: ( gammaExpression ( COMMA gammaExpression )* )
            // DescribeOperator.g:150:9: gammaExpression ( COMMA gammaExpression )*
            {
            pushFollow(FOLLOW_gammaExpression_in_gammaExpressionsList888);
            gammaExpression();

            state._fsp--;


            // DescribeOperator.g:150:25: ( COMMA gammaExpression )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // DescribeOperator.g:150:26: COMMA gammaExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_gammaExpressionsList891); 

            	    pushFollow(FOLLOW_gammaExpression_in_gammaExpressionsList893);
            	    gammaExpression();

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
    // $ANTLR end "gammaExpressionsList"



    // $ANTLR start "gammaExpression"
    // DescribeOperator.g:153:1: gammaExpression : WORD ;
    public final void gammaExpression() throws RecognitionException {
        Token WORD2=null;

        try {
            // DescribeOperator.g:154:5: ( WORD )
            // DescribeOperator.g:154:9: WORD
            {
            WORD2=(Token)match(input,WORD,FOLLOW_WORD_in_gammaExpression914); 

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
    // DescribeOperator.g:157:1: orderExpressionsList : orderExpression ( COMMA orderExpression )* ;
    public final void orderExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:158:2: ( orderExpression ( COMMA orderExpression )* )
            // DescribeOperator.g:158:4: orderExpression ( COMMA orderExpression )*
            {
            pushFollow(FOLLOW_orderExpression_in_orderExpressionsList930);
            orderExpression();

            state._fsp--;


            // DescribeOperator.g:158:20: ( COMMA orderExpression )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==COMMA) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // DescribeOperator.g:158:21: COMMA orderExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_orderExpressionsList933); 

            	    pushFollow(FOLLOW_orderExpression_in_orderExpressionsList935);
            	    orderExpression();

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
    // $ANTLR end "orderExpressionsList"



    // $ANTLR start "orderExpression"
    // DescribeOperator.g:161:1: orderExpression : w= WORD (dir= sortDirection )? ;
    public final void orderExpression() throws RecognitionException {
        Token w=null;
        DescribeOperatorParser.sortDirection_return dir =null;


        try {
            // DescribeOperator.g:162:2: (w= WORD (dir= sortDirection )? )
            // DescribeOperator.g:162:4: w= WORD (dir= sortDirection )?
            {
            w=(Token)match(input,WORD,FOLLOW_WORD_in_orderExpression950); 

            // DescribeOperator.g:162:11: (dir= sortDirection )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ASC||LA17_0==DESC) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // DescribeOperator.g:162:12: dir= sortDirection
                    {
                    pushFollow(FOLLOW_sortDirection_in_orderExpression955);
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
    // DescribeOperator.g:168:1: sortDirection : ( ASC | DESC );
    public final DescribeOperatorParser.sortDirection_return sortDirection() throws RecognitionException {
        DescribeOperatorParser.sortDirection_return retval = new DescribeOperatorParser.sortDirection_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:169:2: ( ASC | DESC )
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



    // $ANTLR start "usingList"
    // DescribeOperator.g:173:1: usingList : modelName ( COMMA modelName )* ;
    public final void usingList() throws RecognitionException {
        try {
            // DescribeOperator.g:174:2: ( modelName ( COMMA modelName )* )
            // DescribeOperator.g:174:4: modelName ( COMMA modelName )*
            {
            pushFollow(FOLLOW_modelName_in_usingList986);
            modelName();

            state._fsp--;


            // DescribeOperator.g:174:14: ( COMMA modelName )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==COMMA) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // DescribeOperator.g:174:15: COMMA modelName
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_usingList989); 

            	    pushFollow(FOLLOW_modelName_in_usingList991);
            	    modelName();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
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
    // $ANTLR end "usingList"



    // $ANTLR start "modelName"
    // DescribeOperator.g:177:1: modelName : WORD ;
    public final void modelName() throws RecognitionException {
        Token WORD3=null;

        try {
            // DescribeOperator.g:178:2: ( WORD )
            // DescribeOperator.g:178:4: WORD
            {
            WORD3=(Token)match(input,WORD,FOLLOW_WORD_in_modelName1004); 

             params.addModel((WORD3!=null?WORD3.getText():null)); 

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
    // $ANTLR end "modelName"



    // $ANTLR start "queryAlias"
    // DescribeOperator.g:182:1: queryAlias : WORD ;
    public final void queryAlias() throws RecognitionException {
        Token WORD4=null;

        try {
            // DescribeOperator.g:183:2: ( WORD )
            // DescribeOperator.g:183:5: WORD
            {
            WORD4=(Token)match(input,WORD,FOLLOW_WORD_in_queryAlias1019); 

             params.setQueryAlias((WORD4!=null?WORD4.getText():null)); 

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
    // DescribeOperator.g:186:1: comparator returns [String text] : ( EQUAL | GT | LT | GTE | LTE | NEQ );
    public final String comparator() throws RecognitionException {
        String text = null;


        try {
            // DescribeOperator.g:187:5: ( EQUAL | GT | LT | GTE | LTE | NEQ )
            int alt19=6;
            switch ( input.LA(1) ) {
            case EQUAL:
                {
                alt19=1;
                }
                break;
            case GT:
                {
                alt19=2;
                }
                break;
            case LT:
                {
                alt19=3;
                }
                break;
            case GTE:
                {
                alt19=4;
                }
                break;
            case LTE:
                {
                alt19=5;
                }
                break;
            case NEQ:
                {
                alt19=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }

            switch (alt19) {
                case 1 :
                    // DescribeOperator.g:187:7: EQUAL
                    {
                    match(input,EQUAL,FOLLOW_EQUAL_in_comparator1039); 

                     text = "="; 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:187:32: GT
                    {
                    match(input,GT,FOLLOW_GT_in_comparator1045); 

                     text = ">"; 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:187:54: LT
                    {
                    match(input,LT,FOLLOW_LT_in_comparator1051); 

                     text = "<"; 

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:188:7: GTE
                    {
                    match(input,GTE,FOLLOW_GTE_in_comparator1062); 

                     text = ">="; 

                    }
                    break;
                case 5 :
                    // DescribeOperator.g:188:31: LTE
                    {
                    match(input,LTE,FOLLOW_LTE_in_comparator1068); 

                     text = "<="; 

                    }
                    break;
                case 6 :
                    // DescribeOperator.g:188:55: NEQ
                    {
                    match(input,NEQ,FOLLOW_NEQ_in_comparator1074); 

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
    // DescribeOperator.g:190:1: val returns [String text] : ( WORD | NUMBER | TEXTVALUE );
    public final String val() throws RecognitionException {
        String text = null;


        Token WORD5=null;
        Token NUMBER6=null;
        Token TEXTVALUE7=null;

        try {
            // DescribeOperator.g:191:5: ( WORD | NUMBER | TEXTVALUE )
            int alt20=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt20=1;
                }
                break;
            case NUMBER:
                {
                alt20=2;
                }
                break;
            case TEXTVALUE:
                {
                alt20=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }

            switch (alt20) {
                case 1 :
                    // DescribeOperator.g:191:7: WORD
                    {
                    WORD5=(Token)match(input,WORD,FOLLOW_WORD_in_val1092); 

                     text = (WORD5!=null?WORD5.getText():null); 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:192:7: NUMBER
                    {
                    NUMBER6=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_val1103); 

                     text = (NUMBER6!=null?NUMBER6.getText():null); 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:193:7: TEXTVALUE
                    {
                    TEXTVALUE7=(Token)match(input,TEXTVALUE,FOLLOW_TEXTVALUE_in_val1114); 

                     text = (TEXTVALUE7!=null?TEXTVALUE7.getText():null); 

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
    public static final BitSet FOLLOW_JOIN_in_parse211 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_singleStatement_in_parse240 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_ON_in_parse255 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_WORD_in_parse259 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_EOF_in_parse295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_singleStatement314 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_cubeName_in_singleStatement316 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_DESCRIBE_in_singleStatement327 = new BitSet(new long[]{0x0000010020800010L});
    public static final BitSet FOLLOW_measureList_in_singleStatement329 = new BitSet(new long[]{0x0000004080018042L});
    public static final BitSet FOLLOW_FOR_in_singleStatement341 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_sigmaExpressionsList_in_singleStatement343 = new BitSet(new long[]{0x0000004080010042L});
    public static final BitSet FOLLOW_GROUP_in_singleStatement357 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BY_in_singleStatement359 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_gammaExpressionsList_in_singleStatement361 = new BitSet(new long[]{0x0000004080000042L});
    public static final BitSet FOLLOW_ORDER_in_singleStatement374 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BY_in_singleStatement376 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_orderExpressionsList_in_singleStatement378 = new BitSet(new long[]{0x0000004000000042L});
    public static final BitSet FOLLOW_USING_in_singleStatement391 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_usingList_in_singleStatement393 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AS_in_singleStatement406 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_queryAlias_in_singleStatement408 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_cubeName430 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measureExpression_in_measureList452 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_measureList455 = new BitSet(new long[]{0x0000010020800010L});
    public static final BitSet FOLLOW_measureExpression_in_measureList457 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_expression_in_measureExpression485 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AS_in_measureExpression515 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_WORD_in_measureExpression519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression548 = new BitSet(new long[]{0x0000000104000002L});
    public static final BitSet FOLLOW_set_in_expression551 = new BitSet(new long[]{0x0000010020800010L});
    public static final BitSet FOLLOW_term_in_expression559 = new BitSet(new long[]{0x0000000104000002L});
    public static final BitSet FOLLOW_factor_in_term580 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_set_in_term583 = new BitSet(new long[]{0x0000010020800010L});
    public static final BitSet FOLLOW_factor_in_term591 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_WORD_in_factor612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_factor622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGRFUNC_in_factor632 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_LPARENTHESIS_in_factor634 = new BitSet(new long[]{0x0000010020800010L});
    public static final BitSet FOLLOW_expression_in_factor636 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RPARENTHESIS_in_factor638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPARENTHESIS_in_factor651 = new BitSet(new long[]{0x0000010020800010L});
    public static final BitSet FOLLOW_expression_in_factor653 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RPARENTHESIS_in_factor655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressionsList675 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_sigmaExpressionsList678 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressionsList680 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression712 = new BitSet(new long[]{0x000000000B064000L});
    public static final BitSet FOLLOW_comparator_in_sigmaExpression716 = new BitSet(new long[]{0x0000012020000000L});
    public static final BitSet FOLLOW_val_in_sigmaExpression720 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression743 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_IN_in_sigmaExpression745 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACE_in_sigmaExpression747 = new BitSet(new long[]{0x0000012020000000L});
    public static final BitSet FOLLOW_valueList_in_sigmaExpression751 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RBRACE_in_sigmaExpression753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression776 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_NOT_in_sigmaExpression778 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_IN_in_sigmaExpression780 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACE_in_sigmaExpression782 = new BitSet(new long[]{0x0000012020000000L});
    public static final BitSet FOLLOW_valueList_in_sigmaExpression786 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RBRACE_in_sigmaExpression788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression811 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_WITH_in_sigmaExpression813 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_nestedFilter_in_sigmaExpression817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_nestedFilter838 = new BitSet(new long[]{0x000000000B064000L});
    public static final BitSet FOLLOW_comparator_in_nestedFilter840 = new BitSet(new long[]{0x0000012020000000L});
    public static final BitSet FOLLOW_val_in_nestedFilter842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_val_in_valueList861 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_valueList864 = new BitSet(new long[]{0x0000012020000000L});
    public static final BitSet FOLLOW_val_in_valueList866 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_gammaExpression_in_gammaExpressionsList888 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_gammaExpressionsList891 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_gammaExpression_in_gammaExpressionsList893 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_gammaExpression914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderExpression_in_orderExpressionsList930 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_orderExpressionsList933 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_orderExpression_in_orderExpressionsList935 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_orderExpression950 = new BitSet(new long[]{0x0000000000000482L});
    public static final BitSet FOLLOW_sortDirection_in_orderExpression955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modelName_in_usingList986 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_usingList989 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_modelName_in_usingList991 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_modelName1004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_queryAlias1019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_comparator1039 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_comparator1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_comparator1051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GTE_in_comparator1062 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTE_in_comparator1068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEQ_in_comparator1074 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_val1092 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_val1103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEXTVALUE_in_val1114 = new BitSet(new long[]{0x0000000000000002L});

}