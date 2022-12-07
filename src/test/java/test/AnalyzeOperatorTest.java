package test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;


import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;

import mainengine.AnalyzeOperator;

import mainengine.Session;


public class AnalyzeOperatorTest {

	private static CubeManager testCubeManager;
	private static Session testSession;
	private static String testSessionId;
	
	// set up SQP and CubeManager
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star");
		
		testCubeManager = new CubeManager(typeOfConnection, userInputList);
		testSession = new Session(testCubeManager);
		testSessionId = testSession.initialize(typeOfConnection, userInputList);
	}
	
	// test with 2 groupers on middle hierarchy and 2 sigma expressions
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesGroupersInMiddleHierarchy() {
		String analyzeExpression = "ANALYZE min amount FROM loan FOR region=Prague AND year=1998 BY district_name, month";
		
		String cubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.month\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n"
				+ "QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.month\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n"
				+ "QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.day\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String result = operator.getAnalyzeCubeQueries();

		assertEquals(cubeQueries,result);

		
	}
	
	// test with 2 groupers on top hierarchy and 3 sigma expressions
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesGroupersInTopHierarchy() {
		String analyzeExpression = "ANALYZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		String cubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.All_date\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n"
				+ "QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.All_date\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n"
				+ "QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.year\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String result = operator.getAnalyzeCubeQueries();
		
		assertEquals(cubeQueries,result);

	}
	
	// test with 1 grouper on middle hierarchy, 1 on bottom hierarchy and 2 sigma expressions
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesOneGrouperInBottomHierarchy() {
		String analyzeExpression = "ANALYZE sum amount FROM loan FOR region=Prague AND year=1998 BY district_name, SK_day";		
		
		String cubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.SK_day\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n"
				+ "QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_day\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String result = operator.getAnalyzeCubeQueries();
		
		assertEquals(cubeQueries,result);

	}
	
	// test with 2 groupers on bottom hierarchy and 1 sigma expression 
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesGroupersInBottomHierarchy() {
		String analyzeExpression = "ANALYZE min payments FROM loan FOR region=Prague BY account, SK_day";		
		
		String cubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : payments\nGamma Expression: account_dim.account , date_dim.SK_day\nSigma Expression: account_dim.region='Prague'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String result = operator.getAnalyzeCubeQueries();
		
		assertEquals(cubeQueries,result);

	}
}
