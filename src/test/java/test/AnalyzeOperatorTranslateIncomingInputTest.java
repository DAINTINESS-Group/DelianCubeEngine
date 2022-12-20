package test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;


import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;

import mainengine.AnalyzeOperator;

import mainengine.Session;


public class AnalyzeOperatorTranslateIncomingInputTest {

	private static CubeManager testCubeManager;
	private static Session testSession;
	
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
		testSession.initialize(typeOfConnection, userInputList);
	}
	
	// test with 2 groupers on middle hierarchy and 2 sigma expressions
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesGroupersOnMiddleHierarchy() {
		String analyzeExpression = "ANALYZE min amount FROM loan FOR region=Prague AND year=1998 BY district_name, month";
		
		String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.month\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.month\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.day\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , account_dim.region\nSigma Expression: account_dim.All_account='ALL' AND date_dim.year='1998'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: date_dim.month , account_dim.region\nSigma Expression: account_dim.All_account='ALL' AND date_dim.year='1998'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 3\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.year\nSigma Expression: account_dim.region='Prague' AND date_dim.All_date='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 4\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: date_dim.month , date_dim.year\nSigma Expression: account_dim.region='Prague' AND date_dim.All_date='ALL'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

		
	}
	
	// test with 2 groupers on top hierarchy and 3 sigma expressions
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesGroupersOnTopHierarchy() {
		String analyzeExpression = "ANALYZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.All_date\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.All_date\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.year\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , account_dim.region\nSigma Expression: account_dim.All_account='ALL' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.All_date , account_dim.region\nSigma Expression: account_dim.All_account='ALL' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 3\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.year\nSigma Expression: account_dim.region='Prague' AND date_dim.All_date='ALL' AND status_dim.status='Contract Finished/No Problems'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 4\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.All_date , date_dim.year\nSigma Expression: account_dim.region='Prague' AND date_dim.All_date='ALL' AND status_dim.status='Contract Finished/No Problems'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 5\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , status_dim.status\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 6\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.All_date , status_dim.status\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.All_status='ALL'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
		
		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	// test with 1 grouper on middle hierarchy, 1 on bottom hierarchy and 2 sigma expressions
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesOneGrouperOnBottomHierarchy() {
		String analyzeExpression = "ANALYZE sum amount FROM loan FOR district_name=Brno_-_mesto AND month=1999-04 BY region, SK_day";		
		
		String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.SK_day\nSigma Expression: account_dim.district_name='Brno - mesto' AND date_dim.month='1999-04'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.SK_day\nSigma Expression: account_dim.district_name='Brno - mesto' AND date_dim.month='1999-04'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.region , account_dim.district_name\nSigma Expression: account_dim.region='south Moravia' AND date_dim.month='1999-04'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.SK_day , account_dim.district_name\nSigma Expression: account_dim.region='south Moravia' AND date_dim.month='1999-04'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.month\nSigma Expression: account_dim.district_name='Brno - mesto' AND date_dim.year='1999'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.SK_day , date_dim.month\nSigma Expression: account_dim.district_name='Brno - mesto' AND date_dim.year='1999'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
		
		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	// test with 2 groupers on bottom hierarchy and 1 sigma expression 
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesGroupersOnBottomHierarchy() {
		String analyzeExpression = "ANALYZE min amount FROM loan FOR region=south_Moravia BY account, SK_day";		
		
		String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_day\nSigma Expression: account_dim.region='south Moravia'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , account_dim.region\nSigma Expression: account_dim.All_account='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: date_dim.SK_day , account_dim.region\nSigma Expression: account_dim.All_account='ALL'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
		
		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	// test with 2 groupers on bottom hierarchy and 1 sigma expression on top hierarchy
		@Test
		public final void testTranslateInputAnalyzeExpressionToCubeQueriesOneSigmaExpressionOnTopHierarchy() {
			String analyzeExpression = "ANALYZE min amount FROM loan FOR All_account=ALL BY account, SK_day";		
			
			String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_day\nSigma Expression: account_dim.All_account='ALL'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , account_dim.All_account\nSigma Expression: account_dim.All_account='ALL'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: date_dim.SK_day , account_dim.All_account\nSigma Expression: account_dim.All_account='ALL'\n";
			
			AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
			
			operator.execute();
			
			String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
			
			assertEquals(expectedCubeQueries,resultedCubeQueries);

		}
		
		// test 1 sigma expression on bottom hierarchy
		@Test
		public final void testTranslateInputAnalyzeExpressionToCubeQueriesOneSigmaExpressionOnBottomHierarchy() {
			String analyzeExpression = "ANALYZE avg amount FROM loan FOR account=100 BY district_name, day";		
			
			String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.day\nSigma Expression: account_dim.account='100'\n"
										+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.day\nSigma Expression: account_dim.account='100'\n"
										+"QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.SK_day\nSigma Expression: account_dim.account='100'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.district_name , account_dim.account\nSigma Expression: account_dim.district_name='Usti nad Labem'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: date_dim.day , account_dim.account\nSigma Expression: account_dim.district_name='Usti nad Labem'\n";
			
			AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
			
			operator.execute();
			
			String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
			
			assertEquals(expectedCubeQueries,resultedCubeQueries);

		}

	// test with 2 sigma expressions on top hierarchy
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesTwoSigmaExpressionOnTopHierarchy() {
		String analyzeExpression = "ANALYZE max amount FROM loan FOR All_account=ALL AND All_date=ALL BY region, year";
		
		String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.year\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.year\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.month\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , account_dim.All_account\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.year , account_dim.All_account\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 3\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.All_date\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 4\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.year , date_dim.All_date\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
		
		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
	
	// test with 2 sigma expressions on bottom hierarchy
		@Test
		public final void testTranslateInputAnalyzeExpressionToCubeQueriesTwoSigmaExpressionOnBottomHierarchy() {
			String analyzeExpression = "ANALYZE max amount FROM loan FOR account=184 AND SK_day=19990408 BY region, year";
			
			String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.year\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408'\n"
										+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.year\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408'\n"
										+"QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.month\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , account_dim.account\nSigma Expression: account_dim.district_name='Vsetin' AND date_dim.SK_day='19990408'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.year , account_dim.account\nSigma Expression: account_dim.district_name='Vsetin' AND date_dim.SK_day='19990408'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 3\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.SK_day\nSigma Expression: account_dim.account='184' AND date_dim.day='1999-04-08'\n"
										+"QueryName: AnalyzeBaseQuery-Sibling 4\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.year , date_dim.SK_day\nSigma Expression: account_dim.account='184' AND date_dim.day='1999-04-08'\n";
										
			AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
			
			operator.execute();
			
			String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
			
			assertEquals(expectedCubeQueries,resultedCubeQueries);
		}
	
	// test with 3 sigma expressions on bottom hierarchy
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesThreeSigmaExpressionsOnBottomHierarchy() {
		String analyzeExpression = "ANALYZE sum amount FROM loan FOR account=184 AND SK_day=19990408 AND SK_status=3 BY status, month";
		
		String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , date_dim.month\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408' AND status_dim.SK_status='3'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.SK_status , date_dim.month\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408' AND status_dim.SK_status='3'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , date_dim.day\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408' AND status_dim.SK_status='3'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , account_dim.account\nSigma Expression: account_dim.district_name='Vsetin' AND date_dim.SK_day='19990408' AND status_dim.SK_status='3'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.month , account_dim.account\nSigma Expression: account_dim.district_name='Vsetin' AND date_dim.SK_day='19990408' AND status_dim.SK_status='3'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , date_dim.SK_day\nSigma Expression: account_dim.account='184' AND date_dim.day='1999-04-08' AND status_dim.SK_status='3'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.month , date_dim.SK_day\nSigma Expression: account_dim.account='184' AND date_dim.day='1999-04-08' AND status_dim.SK_status='3'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 5\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , status_dim.SK_status\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408' AND status_dim.status='Running Contract/Client in Debt'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 6\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.month , status_dim.SK_status\nSigma Expression: account_dim.account='184' AND date_dim.SK_day='19990408' AND status_dim.status='Running Contract/Client in Debt'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
		
		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
	
	// test with 3 sigma expressions on top hierarchy
	@Test
	public final void testTranslateInputAnalyzeExpressionToCubeQueriesThreeSigmaExpressionsOnTopHierarchy() {
		String analyzeExpression = "ANALYZE sum amount FROM loan FOR All_account=ALL AND All_date=ALL AND All_status=ALL BY status, month";
		
		String expectedCubeQueries = "QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , date_dim.month\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.SK_status , date_dim.month\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Drill Down 2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , date_dim.day\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , account_dim.All_account\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.month , account_dim.All_account\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , date_dim.All_date\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.month , date_dim.All_date\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 5\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: status_dim.status , status_dim.All_status\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n"
									+"QueryName: AnalyzeBaseQuery-Sibling 6\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.month , status_dim.All_status\nSigma Expression: account_dim.All_account='ALL' AND date_dim.All_date='ALL' AND status_dim.All_status='ALL'\n";
		
		AnalyzeOperator operator = new AnalyzeOperator(analyzeExpression,testCubeManager);
		
		operator.execute();
		
		String resultedCubeQueries = operator.getAnalyzeCubeQueriesString();
		
		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
}
