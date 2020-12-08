package test;

import static org.junit.Assert.*;
import org.junit.Test;

import mainengine.nlq.NLTranslator;
import mainengine.nlq.QueryForm;

public class NLTranslatorTest {

	@Test
	public void test() {
		String queryToBeTested = "Show me the avg of loan amount per account_dim.lvl1,date_dim.lvl2 for account_dim.lvl2='north Moravia' as LoanQuery11_S1_CG-Prtl";
		NLTranslator translator = new NLTranslator();
		QueryForm query = translator.analyzeNLQuery(queryToBeTested);
		String analysedString = query.getCubeName() +
				query.getQueryName() +
				query.getAggregateFunction() +
				query.getMeasure() +
				query.getGamma() + 
				query.getSigma();
		
		assertEquals(analysedString, "CubeName:loan\n"
				+ "Name:LoanQuery11_S1_CG-Prtl\n"
				+ "AggrFunc:avg\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.lvl1,date_dim.lvl2 \n"
				+ "Sigma:account_dim.lvl2='north Moravia' ");
	}

}
