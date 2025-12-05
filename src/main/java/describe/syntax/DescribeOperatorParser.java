// $ANTLR 3.4 DescribeOperator.g 2025-12-05 11:54:24

    package describe.syntax;
    import describe.DescribeQuery;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class DescribeOperatorParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AGGRFUNC", "AND", "AS", "ASC", "BY", "COMMA", "DESC", "DESCRIBE", "DIGIT", "DOT", "EQUAL", "FOR", "GROUP", "GT", "GTE", "IN", "JOIN", "LBRACE", "LETTER", "LPARENTHESIS", "LT", "LTE", "MINUS", "NEQ", "NUMBER", "ON", "ORDER", "PLUS", "RBRACE", "RPARENTHESIS", "SLASH", "STAR", "TEXTVALUE", "USING", "WITH", "WORD", "WS"
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
        private DescribeQuery query = new DescribeQuery();    
        
        public DescribeQuery getQuery(){
        	return query;   
        }



    // $ANTLR start "start"
    // DescribeOperator.g:34:1: start : parse ;
    public final void start() throws RecognitionException {
        try {
            // DescribeOperator.g:34:6: ( parse )
            // DescribeOperator.g:34:8: parse
            {

                query = new DescribeQuery();


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
    // DescribeOperator.g:38:1: parse : singleStatement ( JOIN singleStatement ON joinCol= WORD )* ;
    public final void parse() throws RecognitionException {
        Token joinCol=null;

        try {
            // DescribeOperator.g:39:5: ( singleStatement ( JOIN singleStatement ON joinCol= WORD )* )
            // DescribeOperator.g:39:9: singleStatement ( JOIN singleStatement ON joinCol= WORD )*
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

            	     
            	                   query.setJoined(true);
            	                   query.setJoinType("JOIN");
            	                

            	    pushFollow(FOLLOW_singleStatement_in_parse240);
            	    singleStatement();

            	    state._fsp--;


            	    match(input,ON,FOLLOW_ON_in_parse255); 

            	    joinCol=(Token)match(input,WORD,FOLLOW_WORD_in_parse259); 

            	     
            	                   query.setJoinCondition((joinCol!=null?joinCol.getText():null));
            	                

            	    }
            	    break;

            	default :
            	    break loop1;
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
    // $ANTLR end "parse"



    // $ANTLR start "singleStatement"
    // DescribeOperator.g:54:1: singleStatement : WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( USING usingList )? ( AS queryAlias )? ;
    public final void singleStatement() throws RecognitionException {
        try {
            // DescribeOperator.g:55:5: ( WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( USING usingList )? ( AS queryAlias )? )
            // DescribeOperator.g:55:9: WITH cubeName DESCRIBE measureList ( FOR sigmaExpressionsList )? ( GROUP BY gammaExpressionsList )? ( ORDER BY orderExpressionsList )? ( USING usingList )? ( AS queryAlias )?
            {
            match(input,WITH,FOLLOW_WITH_in_singleStatement304); 

            pushFollow(FOLLOW_cubeName_in_singleStatement306);
            cubeName();

            state._fsp--;


            match(input,DESCRIBE,FOLLOW_DESCRIBE_in_singleStatement317); 

            pushFollow(FOLLOW_measureList_in_singleStatement319);
            measureList();

            state._fsp--;


            // DescribeOperator.g:57:9: ( FOR sigmaExpressionsList )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==FOR) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // DescribeOperator.g:57:10: FOR sigmaExpressionsList
                    {
                    match(input,FOR,FOLLOW_FOR_in_singleStatement331); 

                    pushFollow(FOLLOW_sigmaExpressionsList_in_singleStatement333);
                    sigmaExpressionsList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:58:9: ( GROUP BY gammaExpressionsList )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==GROUP) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // DescribeOperator.g:58:10: GROUP BY gammaExpressionsList
                    {
                    match(input,GROUP,FOLLOW_GROUP_in_singleStatement347); 

                    match(input,BY,FOLLOW_BY_in_singleStatement349); 

                    pushFollow(FOLLOW_gammaExpressionsList_in_singleStatement351);
                    gammaExpressionsList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:59:9: ( ORDER BY orderExpressionsList )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ORDER) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // DescribeOperator.g:59:10: ORDER BY orderExpressionsList
                    {
                    match(input,ORDER,FOLLOW_ORDER_in_singleStatement364); 

                    match(input,BY,FOLLOW_BY_in_singleStatement366); 

                    pushFollow(FOLLOW_orderExpressionsList_in_singleStatement368);
                    orderExpressionsList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:60:9: ( USING usingList )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==USING) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // DescribeOperator.g:60:10: USING usingList
                    {
                    match(input,USING,FOLLOW_USING_in_singleStatement381); 

                    pushFollow(FOLLOW_usingList_in_singleStatement383);
                    usingList();

                    state._fsp--;


                    }
                    break;

            }


            // DescribeOperator.g:61:9: ( AS queryAlias )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==AS) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // DescribeOperator.g:61:10: AS queryAlias
                    {
                    match(input,AS,FOLLOW_AS_in_singleStatement396); 

                    pushFollow(FOLLOW_queryAlias_in_singleStatement398);
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
            WORD1=(Token)match(input,WORD,FOLLOW_WORD_in_cubeName420); 

             
                		if(query.getCubeName().equals("")){
                			query.setCubeName((WORD1!=null?WORD1.getText():null));
                		}else{
                			query.appendToCubeName(" JOIN " + (WORD1!=null?WORD1.getText():null));
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
            pushFollow(FOLLOW_measureExpression_in_measureList442);
            measureExpression();

            state._fsp--;


            // DescribeOperator.g:77:27: ( COMMA measureExpression )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==COMMA) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // DescribeOperator.g:77:28: COMMA measureExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_measureList445); 

            	    pushFollow(FOLLOW_measureExpression_in_measureList447);
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
    // DescribeOperator.g:80:1: measureExpression : expression ;
    public final void measureExpression() throws RecognitionException {
        DescribeOperatorParser.expression_return expression2 =null;


        try {
            // DescribeOperator.g:81:5: ( expression )
            // DescribeOperator.g:82:9: expression
            {
            pushFollow(FOLLOW_expression_in_measureExpression475);
            expression2=expression();

            state._fsp--;


             
                        query.addMeasure((expression2!=null?input.toString(expression2.start,expression2.stop):null));
                    

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
    // DescribeOperator.g:87:1: expression : term ( ( PLUS | MINUS ) term )* ;
    public final DescribeOperatorParser.expression_return expression() throws RecognitionException {
        DescribeOperatorParser.expression_return retval = new DescribeOperatorParser.expression_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:88:5: ( term ( ( PLUS | MINUS ) term )* )
            // DescribeOperator.g:88:9: term ( ( PLUS | MINUS ) term )*
            {
            pushFollow(FOLLOW_term_in_expression496);
            term();

            state._fsp--;


            // DescribeOperator.g:88:14: ( ( PLUS | MINUS ) term )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==MINUS||LA8_0==PLUS) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // DescribeOperator.g:88:15: ( PLUS | MINUS ) term
            	    {
            	    if ( input.LA(1)==MINUS||input.LA(1)==PLUS ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_term_in_expression507);
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
    // DescribeOperator.g:91:1: term : factor ( ( STAR | SLASH ) factor )* ;
    public final void term() throws RecognitionException {
        try {
            // DescribeOperator.g:92:5: ( factor ( ( STAR | SLASH ) factor )* )
            // DescribeOperator.g:92:9: factor ( ( STAR | SLASH ) factor )*
            {
            pushFollow(FOLLOW_factor_in_term528);
            factor();

            state._fsp--;


            // DescribeOperator.g:92:16: ( ( STAR | SLASH ) factor )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0 >= SLASH && LA9_0 <= STAR)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // DescribeOperator.g:92:17: ( STAR | SLASH ) factor
            	    {
            	    if ( (input.LA(1) >= SLASH && input.LA(1) <= STAR) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_factor_in_term539);
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
    // DescribeOperator.g:95:1: factor : ( WORD | NUMBER | AGGRFUNC LPARENTHESIS expression RPARENTHESIS | LPARENTHESIS expression RPARENTHESIS );
    public final void factor() throws RecognitionException {
        try {
            // DescribeOperator.g:96:5: ( WORD | NUMBER | AGGRFUNC LPARENTHESIS expression RPARENTHESIS | LPARENTHESIS expression RPARENTHESIS )
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
                    // DescribeOperator.g:96:9: WORD
                    {
                    match(input,WORD,FOLLOW_WORD_in_factor560); 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:97:9: NUMBER
                    {
                    match(input,NUMBER,FOLLOW_NUMBER_in_factor570); 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:98:9: AGGRFUNC LPARENTHESIS expression RPARENTHESIS
                    {
                    match(input,AGGRFUNC,FOLLOW_AGGRFUNC_in_factor580); 

                    match(input,LPARENTHESIS,FOLLOW_LPARENTHESIS_in_factor582); 

                    pushFollow(FOLLOW_expression_in_factor584);
                    expression();

                    state._fsp--;


                    match(input,RPARENTHESIS,FOLLOW_RPARENTHESIS_in_factor586); 

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:99:9: LPARENTHESIS expression RPARENTHESIS
                    {
                    match(input,LPARENTHESIS,FOLLOW_LPARENTHESIS_in_factor599); 

                    pushFollow(FOLLOW_expression_in_factor601);
                    expression();

                    state._fsp--;


                    match(input,RPARENTHESIS,FOLLOW_RPARENTHESIS_in_factor603); 

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
    // DescribeOperator.g:103:1: sigmaExpressionsList : sigmaExpression ( AND sigmaExpression )* ;
    public final void sigmaExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:104:5: ( sigmaExpression ( AND sigmaExpression )* )
            // DescribeOperator.g:104:9: sigmaExpression ( AND sigmaExpression )*
            {
            pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressionsList623);
            sigmaExpression();

            state._fsp--;


            // DescribeOperator.g:104:25: ( AND sigmaExpression )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==AND) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // DescribeOperator.g:104:26: AND sigmaExpression
            	    {
            	    match(input,AND,FOLLOW_AND_in_sigmaExpressionsList626); 

            	    pushFollow(FOLLOW_sigmaExpression_in_sigmaExpressionsList628);
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
    // DescribeOperator.g:107:1: sigmaExpression : (w= WORD op= comparator v= val |w= WORD IN LBRACE vList= valueList RBRACE |w= WORD WITH nested= nestedFilter );
    public final void sigmaExpression() throws RecognitionException {
        Token w=null;
        String op =null;

        String v =null;

        DescribeOperatorParser.valueList_return vList =null;

        DescribeOperatorParser.nestedFilter_return nested =null;


        try {
            // DescribeOperator.g:108:5: (w= WORD op= comparator v= val |w= WORD IN LBRACE vList= valueList RBRACE |w= WORD WITH nested= nestedFilter )
            int alt12=3;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==WORD) ) {
                switch ( input.LA(2) ) {
                case IN:
                    {
                    alt12=2;
                    }
                    break;
                case WITH:
                    {
                    alt12=3;
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
                    // DescribeOperator.g:109:9: w= WORD op= comparator v= val
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression660); 

                    pushFollow(FOLLOW_comparator_in_sigmaExpression664);
                    op=comparator();

                    state._fsp--;


                    pushFollow(FOLLOW_val_in_sigmaExpression668);
                    v=val();

                    state._fsp--;



                                query.addSigmaExpression((w!=null?w.getText():null) + op + v);
                                query.addSigmaValue((w!=null?w.getText():null), v);
                            

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:114:9: w= WORD IN LBRACE vList= valueList RBRACE
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression691); 

                    match(input,IN,FOLLOW_IN_in_sigmaExpression693); 

                    match(input,LBRACE,FOLLOW_LBRACE_in_sigmaExpression695); 

                    pushFollow(FOLLOW_valueList_in_sigmaExpression699);
                    vList=valueList();

                    state._fsp--;


                    match(input,RBRACE,FOLLOW_RBRACE_in_sigmaExpression701); 


                                query.addSigmaExpression((w!=null?w.getText():null) + " IN {" + (vList!=null?input.toString(vList.start,vList.stop):null) + "}");
                                query.addSigmaValue((w!=null?w.getText():null), (vList!=null?input.toString(vList.start,vList.stop):null));
                            

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:119:9: w= WORD WITH nested= nestedFilter
                    {
                    w=(Token)match(input,WORD,FOLLOW_WORD_in_sigmaExpression724); 

                    match(input,WITH,FOLLOW_WITH_in_sigmaExpression726); 

                    pushFollow(FOLLOW_nestedFilter_in_sigmaExpression730);
                    nested=nestedFilter();

                    state._fsp--;



                                query.addSigmaExpression((w!=null?w.getText():null) + " WITH " + (nested!=null?input.toString(nested.start,nested.stop):null));
                                //Stores the whole nested condition as "value"
                                query.addSigmaValue((w!=null?w.getText():null), (nested!=null?input.toString(nested.start,nested.stop):null));
                            

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
    // DescribeOperator.g:126:1: nestedFilter : WORD comparator val ;
    public final DescribeOperatorParser.nestedFilter_return nestedFilter() throws RecognitionException {
        DescribeOperatorParser.nestedFilter_return retval = new DescribeOperatorParser.nestedFilter_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:127:5: ( WORD comparator val )
            // DescribeOperator.g:127:9: WORD comparator val
            {
            match(input,WORD,FOLLOW_WORD_in_nestedFilter751); 

            pushFollow(FOLLOW_comparator_in_nestedFilter753);
            comparator();

            state._fsp--;


            pushFollow(FOLLOW_val_in_nestedFilter755);
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
    // DescribeOperator.g:130:1: valueList : val ( COMMA val )* ;
    public final DescribeOperatorParser.valueList_return valueList() throws RecognitionException {
        DescribeOperatorParser.valueList_return retval = new DescribeOperatorParser.valueList_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:131:5: ( val ( COMMA val )* )
            // DescribeOperator.g:131:9: val ( COMMA val )*
            {
            pushFollow(FOLLOW_val_in_valueList774);
            val();

            state._fsp--;


            // DescribeOperator.g:131:13: ( COMMA val )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==COMMA) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // DescribeOperator.g:131:14: COMMA val
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_valueList777); 

            	    pushFollow(FOLLOW_val_in_valueList779);
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
    // DescribeOperator.g:135:1: gammaExpressionsList : gammaExpression ( COMMA gammaExpression )* ;
    public final void gammaExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:136:5: ( gammaExpression ( COMMA gammaExpression )* )
            // DescribeOperator.g:136:9: gammaExpression ( COMMA gammaExpression )*
            {
            pushFollow(FOLLOW_gammaExpression_in_gammaExpressionsList801);
            gammaExpression();

            state._fsp--;


            // DescribeOperator.g:136:25: ( COMMA gammaExpression )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==COMMA) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // DescribeOperator.g:136:26: COMMA gammaExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_gammaExpressionsList804); 

            	    pushFollow(FOLLOW_gammaExpression_in_gammaExpressionsList806);
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
    // DescribeOperator.g:139:1: gammaExpression : WORD ;
    public final void gammaExpression() throws RecognitionException {
        Token WORD3=null;

        try {
            // DescribeOperator.g:140:5: ( WORD )
            // DescribeOperator.g:140:9: WORD
            {
            WORD3=(Token)match(input,WORD,FOLLOW_WORD_in_gammaExpression827); 

             query.addGammaExpression((WORD3!=null?WORD3.getText():null)); 

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
    // DescribeOperator.g:143:1: orderExpressionsList : orderExpression ( COMMA orderExpression )* ;
    public final void orderExpressionsList() throws RecognitionException {
        try {
            // DescribeOperator.g:144:2: ( orderExpression ( COMMA orderExpression )* )
            // DescribeOperator.g:144:4: orderExpression ( COMMA orderExpression )*
            {
            pushFollow(FOLLOW_orderExpression_in_orderExpressionsList843);
            orderExpression();

            state._fsp--;


            // DescribeOperator.g:144:20: ( COMMA orderExpression )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==COMMA) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // DescribeOperator.g:144:21: COMMA orderExpression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_orderExpressionsList846); 

            	    pushFollow(FOLLOW_orderExpression_in_orderExpressionsList848);
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
    // DescribeOperator.g:147:1: orderExpression : w= WORD (dir= sortDirection )? ;
    public final void orderExpression() throws RecognitionException {
        Token w=null;
        DescribeOperatorParser.sortDirection_return dir =null;


        try {
            // DescribeOperator.g:148:2: (w= WORD (dir= sortDirection )? )
            // DescribeOperator.g:148:4: w= WORD (dir= sortDirection )?
            {
            w=(Token)match(input,WORD,FOLLOW_WORD_in_orderExpression863); 

            // DescribeOperator.g:148:11: (dir= sortDirection )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ASC||LA16_0==DESC) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // DescribeOperator.g:148:12: dir= sortDirection
                    {
                    pushFollow(FOLLOW_sortDirection_in_orderExpression868);
                    dir=sortDirection();

                    state._fsp--;


                    }
                    break;

            }



            			String direction = ((dir!=null?input.toString(dir.start,dir.stop):null) == null) ? "" : " " + (dir!=null?input.toString(dir.start,dir.stop):null);
            			query.addOrderExpression((w!=null?w.getText():null) + direction);
            		

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
    // DescribeOperator.g:154:1: sortDirection : ( ASC | DESC );
    public final DescribeOperatorParser.sortDirection_return sortDirection() throws RecognitionException {
        DescribeOperatorParser.sortDirection_return retval = new DescribeOperatorParser.sortDirection_return();
        retval.start = input.LT(1);


        try {
            // DescribeOperator.g:155:2: ( ASC | DESC )
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
    // DescribeOperator.g:159:1: usingList : modelName ( COMMA modelName )* ;
    public final void usingList() throws RecognitionException {
        try {
            // DescribeOperator.g:160:2: ( modelName ( COMMA modelName )* )
            // DescribeOperator.g:160:4: modelName ( COMMA modelName )*
            {
            pushFollow(FOLLOW_modelName_in_usingList899);
            modelName();

            state._fsp--;


            // DescribeOperator.g:160:14: ( COMMA modelName )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==COMMA) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // DescribeOperator.g:160:15: COMMA modelName
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_usingList902); 

            	    pushFollow(FOLLOW_modelName_in_usingList904);
            	    modelName();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
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
    // DescribeOperator.g:163:1: modelName : WORD ;
    public final void modelName() throws RecognitionException {
        Token WORD4=null;

        try {
            // DescribeOperator.g:164:2: ( WORD )
            // DescribeOperator.g:164:4: WORD
            {
            WORD4=(Token)match(input,WORD,FOLLOW_WORD_in_modelName917); 

             query.addModel((WORD4!=null?WORD4.getText():null)); 

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
    // DescribeOperator.g:168:1: queryAlias : WORD ;
    public final void queryAlias() throws RecognitionException {
        Token WORD5=null;

        try {
            // DescribeOperator.g:169:2: ( WORD )
            // DescribeOperator.g:169:5: WORD
            {
            WORD5=(Token)match(input,WORD,FOLLOW_WORD_in_queryAlias932); 

             query.setQueryAlias((WORD5!=null?WORD5.getText():null)); 

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
    // DescribeOperator.g:172:1: comparator returns [String text] : ( EQUAL | GT | LT | GTE | LTE | NEQ );
    public final String comparator() throws RecognitionException {
        String text = null;


        try {
            // DescribeOperator.g:173:5: ( EQUAL | GT | LT | GTE | LTE | NEQ )
            int alt18=6;
            switch ( input.LA(1) ) {
            case EQUAL:
                {
                alt18=1;
                }
                break;
            case GT:
                {
                alt18=2;
                }
                break;
            case LT:
                {
                alt18=3;
                }
                break;
            case GTE:
                {
                alt18=4;
                }
                break;
            case LTE:
                {
                alt18=5;
                }
                break;
            case NEQ:
                {
                alt18=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // DescribeOperator.g:173:7: EQUAL
                    {
                    match(input,EQUAL,FOLLOW_EQUAL_in_comparator952); 

                     text = "="; 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:173:32: GT
                    {
                    match(input,GT,FOLLOW_GT_in_comparator958); 

                     text = ">"; 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:173:54: LT
                    {
                    match(input,LT,FOLLOW_LT_in_comparator964); 

                     text = "<"; 

                    }
                    break;
                case 4 :
                    // DescribeOperator.g:174:7: GTE
                    {
                    match(input,GTE,FOLLOW_GTE_in_comparator975); 

                     text = ">="; 

                    }
                    break;
                case 5 :
                    // DescribeOperator.g:174:31: LTE
                    {
                    match(input,LTE,FOLLOW_LTE_in_comparator981); 

                     text = "<="; 

                    }
                    break;
                case 6 :
                    // DescribeOperator.g:174:55: NEQ
                    {
                    match(input,NEQ,FOLLOW_NEQ_in_comparator987); 

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
    // DescribeOperator.g:176:1: val returns [String text] : ( WORD | NUMBER | TEXTVALUE );
    public final String val() throws RecognitionException {
        String text = null;


        Token WORD6=null;
        Token NUMBER7=null;
        Token TEXTVALUE8=null;

        try {
            // DescribeOperator.g:177:5: ( WORD | NUMBER | TEXTVALUE )
            int alt19=3;
            switch ( input.LA(1) ) {
            case WORD:
                {
                alt19=1;
                }
                break;
            case NUMBER:
                {
                alt19=2;
                }
                break;
            case TEXTVALUE:
                {
                alt19=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }

            switch (alt19) {
                case 1 :
                    // DescribeOperator.g:177:7: WORD
                    {
                    WORD6=(Token)match(input,WORD,FOLLOW_WORD_in_val1005); 

                     text = (WORD6!=null?WORD6.getText():null); 

                    }
                    break;
                case 2 :
                    // DescribeOperator.g:178:7: NUMBER
                    {
                    NUMBER7=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_val1016); 

                     text = (NUMBER7!=null?NUMBER7.getText():null); 

                    }
                    break;
                case 3 :
                    // DescribeOperator.g:179:7: TEXTVALUE
                    {
                    TEXTVALUE8=(Token)match(input,TEXTVALUE,FOLLOW_TEXTVALUE_in_val1027); 

                     text = (TEXTVALUE8!=null?TEXTVALUE8.getText():null); 

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
    public static final BitSet FOLLOW_singleStatement_in_parse186 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_JOIN_in_parse211 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_singleStatement_in_parse240 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ON_in_parse255 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_WORD_in_parse259 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_WITH_in_singleStatement304 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_cubeName_in_singleStatement306 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_DESCRIBE_in_singleStatement317 = new BitSet(new long[]{0x0000008010800010L});
    public static final BitSet FOLLOW_measureList_in_singleStatement319 = new BitSet(new long[]{0x0000002040018042L});
    public static final BitSet FOLLOW_FOR_in_singleStatement331 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_sigmaExpressionsList_in_singleStatement333 = new BitSet(new long[]{0x0000002040010042L});
    public static final BitSet FOLLOW_GROUP_in_singleStatement347 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BY_in_singleStatement349 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_gammaExpressionsList_in_singleStatement351 = new BitSet(new long[]{0x0000002040000042L});
    public static final BitSet FOLLOW_ORDER_in_singleStatement364 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BY_in_singleStatement366 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_orderExpressionsList_in_singleStatement368 = new BitSet(new long[]{0x0000002000000042L});
    public static final BitSet FOLLOW_USING_in_singleStatement381 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_usingList_in_singleStatement383 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AS_in_singleStatement396 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_queryAlias_in_singleStatement398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_cubeName420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_measureExpression_in_measureList442 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_measureList445 = new BitSet(new long[]{0x0000008010800010L});
    public static final BitSet FOLLOW_measureExpression_in_measureList447 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_expression_in_measureExpression475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_expression496 = new BitSet(new long[]{0x0000000084000002L});
    public static final BitSet FOLLOW_set_in_expression499 = new BitSet(new long[]{0x0000008010800010L});
    public static final BitSet FOLLOW_term_in_expression507 = new BitSet(new long[]{0x0000000084000002L});
    public static final BitSet FOLLOW_factor_in_term528 = new BitSet(new long[]{0x0000000C00000002L});
    public static final BitSet FOLLOW_set_in_term531 = new BitSet(new long[]{0x0000008010800010L});
    public static final BitSet FOLLOW_factor_in_term539 = new BitSet(new long[]{0x0000000C00000002L});
    public static final BitSet FOLLOW_WORD_in_factor560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_factor570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGRFUNC_in_factor580 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_LPARENTHESIS_in_factor582 = new BitSet(new long[]{0x0000008010800010L});
    public static final BitSet FOLLOW_expression_in_factor584 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RPARENTHESIS_in_factor586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPARENTHESIS_in_factor599 = new BitSet(new long[]{0x0000008010800010L});
    public static final BitSet FOLLOW_expression_in_factor601 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RPARENTHESIS_in_factor603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressionsList623 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AND_in_sigmaExpressionsList626 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_sigmaExpression_in_sigmaExpressionsList628 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression660 = new BitSet(new long[]{0x000000000B064000L});
    public static final BitSet FOLLOW_comparator_in_sigmaExpression664 = new BitSet(new long[]{0x0000009010000000L});
    public static final BitSet FOLLOW_val_in_sigmaExpression668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression691 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_IN_in_sigmaExpression693 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_LBRACE_in_sigmaExpression695 = new BitSet(new long[]{0x0000009010000000L});
    public static final BitSet FOLLOW_valueList_in_sigmaExpression699 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RBRACE_in_sigmaExpression701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_sigmaExpression724 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_WITH_in_sigmaExpression726 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_nestedFilter_in_sigmaExpression730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_nestedFilter751 = new BitSet(new long[]{0x000000000B064000L});
    public static final BitSet FOLLOW_comparator_in_nestedFilter753 = new BitSet(new long[]{0x0000009010000000L});
    public static final BitSet FOLLOW_val_in_nestedFilter755 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_val_in_valueList774 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_valueList777 = new BitSet(new long[]{0x0000009010000000L});
    public static final BitSet FOLLOW_val_in_valueList779 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_gammaExpression_in_gammaExpressionsList801 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_gammaExpressionsList804 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_gammaExpression_in_gammaExpressionsList806 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_gammaExpression827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_orderExpression_in_orderExpressionsList843 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_orderExpressionsList846 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_orderExpression_in_orderExpressionsList848 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_orderExpression863 = new BitSet(new long[]{0x0000000000000482L});
    public static final BitSet FOLLOW_sortDirection_in_orderExpression868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modelName_in_usingList899 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_usingList902 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_modelName_in_usingList904 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_WORD_in_modelName917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_queryAlias932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_comparator952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_comparator958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_comparator964 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GTE_in_comparator975 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTE_in_comparator981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEQ_in_comparator987 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_val1005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_val1016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEXTVALUE_in_val1027 = new BitSet(new long[]{0x0000000000000002L});

}