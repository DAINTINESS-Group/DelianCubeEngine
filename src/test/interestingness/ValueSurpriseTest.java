package test.interestingness;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import mainengine.SimpleQueryProcessorEngine;

public class ValueSurpriseTest {

	private static SimpleQueryProcessorEngine queryEngine;
	private static List<String> measures = new ArrayList<String>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		queryEngine = new SimpleQueryProcessorEngine(); 
		
		queryEngine.initializeConnectionWithIntrMng("pkdd99", "CinecubesUser",
				"Cinecubes", "pkdd99","", "ExpectedValues/predictions1", "", -1, "loan");
		measures.add("Value Surprise");

	}
	
	@Test
	public void test() throws Exception {

		String[] answer = queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague', date_dim.lvl3 = '1998'", measures);
		assertEquals("4269.75", answer[0]);
		
	}

}
