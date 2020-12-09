package test;

import static org.junit.Assert.*;
import org.junit.Test;

import mainengine.nlq.NLTranslator;
import mainengine.nlq.QueryForm;

public class NLTranslatorTest {

	@Test
	public void testFirstQuery() {
		compareForTest("Show me the avg of loan amount per account_dim.lvl1,date_dim.lvl2 for account_dim.lvl2='north Moravia' as LoanQuery11_S1_CG-Prtl",
				  "CubeName:loan\n"
				+ "Name:LoanQuery11_S1_CG-Prtl\n"
				+ "AggrFunc:avg\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.lvl1,date_dim.lvl2 \n"
				+ "Sigma:account_dim.lvl2='north Moravia' ");
	}
	
	@Test
	public void testSecondQuery() {
		compareForTest("Show me the max of loan amount per account_dim.lvl1, status_dim.lvl1 for date_dim.lvl2 = '1998-01' as LoanQuery12_S1_CG-Dsjnt",
				  "CubeName:loan\n"
				+ "Name:LoanQuery12_S1_CG-Dsjnt\n"
				+ "AggrFunc:max\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.lvl1, status_dim.lvl1 \n"
				+ "Sigma:date_dim.lvl2 = '1998-01' ");
	}
	
	@Test
	public void testThirdQuery() {
		compareForTest("Show me the min of loan amount per account_dim.lvl1,date_dim.lvl2 for account_dim.lvl2='Prague', date_dim.lvl3 = '1998' as LoanQuery21_S2_CG-Cmmn",
				  "CubeName:loan\n"
				+ "Name:LoanQuery21_S2_CG-Cmmn\n"
				+ "AggrFunc:min\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.lvl1,date_dim.lvl2 \n"
				+ "Sigma:account_dim.lvl2='Prague', date_dim.lvl3 = '1998' ");
	}
	
	@Test
	public void testFourthQuery() {
		compareForTest("Show me the sum of loan amount per account_dim.lvl1,date_dim.lvl3 for account_dim.lvl2='south Moravia',status_dim.lvl0='Running Contract/OK' as LoanQuery22_S2_CG-Prtl",
				  "CubeName:loan\n"
				+ "Name:LoanQuery22_S2_CG-Prtl\n"
				+ "AggrFunc:sum\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.lvl1,date_dim.lvl3 \n"
				+ "Sigma:account_dim.lvl2='south Moravia',status_dim.lvl0='Running Contract/OK' ");
	}
	
	@Test
	public void testFifthQuery() {
		compareForTest("Show me the sum of loan amount per account_dim.lvl1,date_dim.lvl3 for account_dim.lvl2='west Bohemia',status_dim.lvl0='Contract Finished/No Problems', date_dim.lvl3 = '1996' as LoanQuery31_S3_CG-Prtl",
				  "CubeName:loan\n"
				+ "Name:LoanQuery31_S3_CG-Prtl\n"
				+ "AggrFunc:sum\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.lvl1,date_dim.lvl3 \n"
				+ "Sigma:account_dim.lvl2='west Bohemia',status_dim.lvl0='Contract Finished/No Problems', date_dim.lvl3 = '1996' ");
	}
	
	public void compareForTest(String queryToBeTested, String exportedString) {
		NLTranslator translator = new NLTranslator();
		QueryForm query = translator.analyzeNLQuery(queryToBeTested);
		
		String analysedString = query.getCubeName() +
				query.getQueryName() +
				query.getAggregateFunction() +
				query.getMeasure() +
				query.getGamma() + 
				query.getSigma();
		analysedString = query.toString();
		
		assertEquals(analysedString, exportedString);
		
	}

}
