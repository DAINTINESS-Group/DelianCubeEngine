package test.nlqueries;

import static org.junit.Assert.*;
import org.junit.Test;

import mainengine.nlq.NLTranslator;
import mainengine.nlq.CubeQueryForm;

public class NLTranslatorTest {

	@Test
	public void testFirstQuery() {
		compareForTest("Describe the avg of loan amount per account_dim.district_name,date_dim.month for account_dim.region='north Moravia' as LoanQuery11_S1_CG-Prtl",
				  "CubeName:loan\n"
				+ "Name:LoanQuery11_S1_CG-Prtl\n"
				+ "AggrFunc:avg\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.district_name,date_dim.month \n"
				+ "Sigma:account_dim.region='north Moravia' ");
	}
	
	@Test
	public void testSecondQuery() {
		compareForTest("Descibe the max of loan amount per account_dim.district_name, status_dim.status for date_dim.month = '1998-01' as LoanQuery12_S1_CG-Dsjnt",
				  "CubeName:loan\n"
				+ "Name:LoanQuery12_S1_CG-Dsjnt\n"
				+ "AggrFunc:max\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.district_name, status_dim.status \n"
				+ "Sigma:date_dim.month = '1998-01' ");
	}
	
	@Test
	public void testThirdQuery() {
		compareForTest("Describe the min of loan amount per account_dim.district_name,date_dim.month for account_dim.region='Prague', date_dim.year = '1998' as LoanQuery21_S2_CG-Cmmn",
				  "CubeName:loan\n"
				+ "Name:LoanQuery21_S2_CG-Cmmn\n"
				+ "AggrFunc:min\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.district_name,date_dim.month \n"
				+ "Sigma:account_dim.region='Prague', date_dim.year = '1998' ");
	}
	
	@Test
	public void testFourthQuery() {
		compareForTest("Describe the sum of loan amount per account_dim.district_name,date_dim.year for account_dim.region='south Moravia',status_dim.status='Running Contract/OK' as LoanQuery22_S2_CG-Prtl",
				  "CubeName:loan\n"
				+ "Name:LoanQuery22_S2_CG-Prtl\n"
				+ "AggrFunc:sum\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.district_name,date_dim.year \n"
				+ "Sigma:account_dim.region='south Moravia',status_dim.status='Running Contract/OK' ");
	}
	
	@Test
	public void testFifthQuery() {
		compareForTest("Describe the sum of loan amount per account_dim.district_name,date_dim.year for account_dim.region='west Bohemia',status_dim.status='Contract Finished/No Problems', date_dim.year = '1996' as LoanQuery31_S3_CG-Prtl",
				  "CubeName:loan\n"
				+ "Name:LoanQuery31_S3_CG-Prtl\n"
				+ "AggrFunc:sum\n"
				+ "Measure:amount\n"
				+ "Gamma:account_dim.district_name,date_dim.year \n"
				+ "Sigma:account_dim.region='west Bohemia',status_dim.status='Contract Finished/No Problems', date_dim.year = '1996' ");
	}
	
	public void compareForTest(String queryToBeTested, String exportedString) {
		NLTranslator translator = new NLTranslator();
		CubeQueryForm query = translator.produceCubeQuery(queryToBeTested);
		
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
