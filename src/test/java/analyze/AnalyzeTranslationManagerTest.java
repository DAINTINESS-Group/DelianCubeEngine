package analyze;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;


import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import analyze.AnalyzeTranslationManager;
import mainengine.Session;


public class AnalyzeTranslationManagerTest {

	private static CubeManager testCubeManager;
	private static Session testSession;
	private static String testSchemaName;
	private static String testTypeOfConnection;
	
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
		
		testSchemaName = userInputList.get("schemaName");
		testCubeManager = new CubeManager(typeOfConnection, userInputList);
		testSession = new Session(testCubeManager);
		testSession.initialize(typeOfConnection, userInputList);
		testTypeOfConnection = typeOfConnection;
	}
	
	@Test
	public final void testExpressionWithGroupersOnMiddleHierarchy() {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region='Prague' AND year='1998' GROUP BY district_name, month AS first_query";
		
		String expectedCubeQueries = "[QueryName: first_query-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.month\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998',"
									 + " QueryName: first_query-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.month\nSigma Expression: account_dim.All_account='ALL' AND date_dim.year='1998',"
								     + " QueryName: first_query-AnalyzeSiblingQuery_date_dim\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Year\nSigma Expression: date_dim.All_date='ALL' AND account_dim.region='Prague',"
									 + " QueryName: first_query-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.month\nSigma Expression: account_dim.district_name='Hl.m. Praha' AND date_dim.year='1998',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-01' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-02' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_3\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-03' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_4\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-04' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_5\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-05' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_6\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-06' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_7\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-07' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_8\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-08' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_9\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-09' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_10\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-10' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_11\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-11' AND account_dim.region='Prague',"
								     + " QueryName: first_query-AnalyzeDrillDownQuery_date_dim_12\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month='1998-12' AND account_dim.region='Prague']";
								     
									 
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

		
	}
	
	@Test
	public final void testExpressionWithGroupersOnTopHierarchy() {
		String incomingExpression = "ANALYZE max(amount) FROM loan FOR region='Prague' AND year='1998' AND status='Contract Finished/No Problems' GROUP BY All_account, All_date AS test_query";
		
		String expectedCubeQueries = "[QueryName: test_query-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.All_date\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.All_date\nSigma Expression: account_dim.All_account='ALL' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeSiblingQuery_date_dim\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.All_date\nSigma Expression: date_dim.All_date='ALL' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.All_date\nSigma Expression: account_dim.district_name='Hl.m. Praha' AND date_dim.year='1998' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-01' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-02' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_3\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-03' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_4\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-04' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_5\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-05' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_6\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-06' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_7\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-07' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_8\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-08' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_9\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-09' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_10\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-10' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_11\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-11' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									 +" QueryName: test_query-AnalyzeDrillDownQuery_date_dim_12\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month='1998-12' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems']";
									 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	@Test
	public final void testExpressionWithGroupersOnBottomHierarchy() {
		String incomingExpression = "ANALYZE sum(amount) FROM loan FOR district_name='Jihlava' AND month='1999-04' GROUP BY account, SK_day AS large_query";		
		
		String expectedCubeQueriesRDBMS = "[QueryName: large_query-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_day\nSigma Expression: account_dim.district_name='Jihlava' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.SK_day\nSigma Expression: account_dim.region='south Moravia' AND date_dim.month='1999-04',"
				                     +" QueryName: large_query-AnalyzeSiblingQuery_date_dim\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.Day\nSigma Expression: date_dim.Year='1999' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='155' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='478' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='513' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='675' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_5\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='713' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_6\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='920' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_7\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='949' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_8\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='1246' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_9\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='1320' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_10\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='1414' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_11\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2056' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_12\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2155' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_13\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2221' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_14\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2319' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_15\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2623' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_16\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2664' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_17\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2711' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_18\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2803' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_19\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2909' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_20\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3337' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_21\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3376' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_22\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3448' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_23\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3480' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_24\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3538' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_25\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3556' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_26\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3576' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_27\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3676' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_28\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3903' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_29\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='4026' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_30\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='4105' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_31\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='4480' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_32\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='5126' AND date_dim.month='1999-04',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-01' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-02' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-03' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-04' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_5\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-05' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_6\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-06' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_7\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-07' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_8\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-08' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_9\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-09' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_10\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-10' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_11\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-11' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_12\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-12' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_13\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-13' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_14\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-14' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_15\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-15' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_16\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-16' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_17\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-17' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_18\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-18' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_19\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-19' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_20\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-20' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_21\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-21' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_22\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-22' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_23\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-23' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_24\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-24' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_25\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-25' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_26\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-26' AND account_dim.district_name='Jihlava',"
									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_27\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-27' AND account_dim.district_name='Jihlava',"
 									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_28\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-28' AND account_dim.district_name='Jihlava',"
 									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_29\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-29' AND account_dim.district_name='Jihlava',"
 									 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_30\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-30' AND account_dim.district_name='Jihlava']";
		
		String expectedCubeQueriesSpark = "[QueryName: large_query-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_day\nSigma Expression: account_dim.district_name='Jihlava' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.SK_day\nSigma Expression: account_dim.region='south Moravia' AND date_dim.month='1999-04',"
                +" QueryName: large_query-AnalyzeSiblingQuery_date_dim\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.Day\nSigma Expression: date_dim.Year='1999' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='1246' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='1320' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='1414' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='155' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_5\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2056' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_6\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2155' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_7\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2221' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_8\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2319' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_9\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2623' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_10\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2664' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_11\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2711' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_12\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2803' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_13\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='2909' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_14\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3337' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_15\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3376' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_16\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3448' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_17\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3480' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_18\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3538' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_19\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3556' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_20\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3576' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_21\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3676' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_22\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='3903' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_23\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='4026' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_24\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='4105' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_25\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='4480' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_26\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='478' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_27\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='5126' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_28\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='513' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_29\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='675' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_30\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='713' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_31\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='920' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_account_dim_32\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id='949' AND date_dim.month='1999-04',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-01' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-02' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-03' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-04' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_5\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-05' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_6\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-06' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_7\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-07' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_8\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-08' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_9\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-09' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_10\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-10' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_11\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-11' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_12\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-12' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_13\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-13' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_14\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-14' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_15\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-15' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_16\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-16' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_17\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-17' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_18\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-18' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_19\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-19' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_20\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-20' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_21\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-21' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_22\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-22' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_23\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-23' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_24\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-24' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_25\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-25' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_26\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-26' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_27\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-27' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_28\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-28' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_29\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-29' AND account_dim.district_name='Jihlava',"
				 +" QueryName: large_query-AnalyzeDrillDownQuery_date_dim_30\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day='1999-04-30' AND account_dim.district_name='Jihlava']";
									 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();
		
		if(testTypeOfConnection.equals("Spark")) {
			assertEquals(expectedCubeQueriesSpark,resultedCubeQueries);
		}else {
			assertEquals(expectedCubeQueriesRDBMS,resultedCubeQueries);
		}
	}
	
	@Test
	public final void testExpressionWithGroupersOnBottomAndMiddleHierarchy() {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region='south Moravia' GROUP BY account, day AS test_query_2";		
		
		
		String expectedCubeQueriesRDBMS = "[QueryName: test_query_2-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.day\nSigma Expression: account_dim.region='south Moravia',"
				+" QueryName: test_query_2-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.day\nSigma Expression: account_dim.All_account='ALL',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Prostejov',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Hodonin',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_3\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Brno - mesto',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_4\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Zlin',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_5\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Vyskov',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_6\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Brno - venkov',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_7\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Blansko',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_8\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Znojmo',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_9\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Jihlava',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_10\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Kromeriz',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_11\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Zdar nad Sazavou',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_12\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Trebic',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_13\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Breclav',"
				+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_14\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Uherske Hradiste']";
		
		String expectedCubeQueriesSpark = "[QueryName: test_query_2-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.day\nSigma Expression: account_dim.region='south Moravia',"
									+" QueryName: test_query_2-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.day\nSigma Expression: account_dim.All_account='ALL',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Blansko',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Breclav',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_3\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Brno - mesto',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_4\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Brno - venkov',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_5\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Hodonin',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_6\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Jihlava',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_7\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Kromeriz',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_8\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Prostejov',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_9\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Trebic',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_10\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Uherske Hradiste',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_11\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Vyskov',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_12\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Zdar nad Sazavou',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_13\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Zlin',"
									+" QueryName: test_query_2-AnalyzeDrillDownQuery_account_dim_14\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name='Znojmo']";
									
		
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();
		
		if(testTypeOfConnection.equals("Spark")) {
			assertEquals(expectedCubeQueriesSpark,resultedCubeQueries);
		}else {
			assertEquals(expectedCubeQueriesRDBMS,resultedCubeQueries);
		}

	}
	
	@Test
	public final void testExpressionWithGroupersOnMiddleAndTopHierarchy() {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region='Prague' GROUP BY All_account, status AS small_query";		
		
		String expectedCubeQueries = "[QueryName: small_query-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.All_account , status_dim.status\nSigma Expression: account_dim.region='Prague',"
									+" QueryName: small_query-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.All_account , status_dim.status\nSigma Expression: account_dim.All_account='ALL',"
									+" QueryName: small_query-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.region , status_dim.status\nSigma Expression: account_dim.district_name='Hl.m. Praha']";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
		
	@Test
	public final void testExpressionWithGroupersOnBottomAndTopHierarchy() {
		String incomingExpression = "ANALYZE avg(amount) FROM loan FOR account='100' AND year='1999' GROUP BY All_account, SK_day AS average_function_query";		
		
		String expectedCubeQueries = "[QueryName: average_function_query-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_day\nSigma Expression: account_dim.account='100' AND date_dim.year='1999',"
									+" QueryName: average_function_query-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_day\nSigma Expression: account_dim.district_name='Usti nad Labem' AND date_dim.year='1999',"
									+" QueryName: average_function_query-AnalyzeSiblingQuery_date_dim\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Day\nSigma Expression: date_dim.All_date='ALL' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.SK_day\nSigma Expression: account_dim.account_id='100' AND date_dim.year='1999',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_1\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-01' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_2\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-02' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_3\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-03' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_4\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-04' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_5\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-05' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_6\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-06' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_7\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-07' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_8\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-08' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_9\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-09' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_10\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-10' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_11\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-11' AND account_dim.account='100',"
									+" QueryName: average_function_query-AnalyzeDrillDownQuery_date_dim_12\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month='1999-12' AND account_dim.account='100']";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	@Test
	public final void testThreeGroupers() {
		String incomingExpression = "ANALYZE max(amount) FROM loan FOR region='Prague' AND year='1989' AND status='Contract Finished/No Problems' GROUP BY district_name,month,SK_status AS multiple_groupers";
		
		String expectedCubeQueries = "[QueryName: multiple_groupers-AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.month , status_dim.SK_status\nSigma Expression: account_dim.region='Prague' AND date_dim.year='1989' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeSiblingQuery_account_dim\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.month , status_dim.SK_status\nSigma Expression: account_dim.All_account='ALL' AND date_dim.year='1989' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeSiblingQuery_date_dim\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Year , status_dim.SK_status\nSigma Expression: date_dim.All_date='ALL' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeSiblingQuery_status_dim\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.month , status_dim.status\nSigma Expression: status_dim.All_status='ALL' AND account_dim.region='Prague' AND date_dim.year='1989',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_account_dim_1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.month , status_dim.SK_status\nSigma Expression: account_dim.district_name='Hl.m. Praha' AND date_dim.year='1989' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-01' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-02' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_3\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-03' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_4\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-04' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_5\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-05' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_6\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-06' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_7\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-07' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_8\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-08' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_9\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-09' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_10\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-10' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_11\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-11' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_date_dim_12\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day , status_dim.SK_status\nSigma Expression: date_dim.Month='1989-12' AND account_dim.region='Prague' AND status_dim.status='Contract Finished/No Problems',"
									+" QueryName: multiple_groupers-AnalyzeDrillDownQuery_status_dim_1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.month , status_dim.SK_status\nSigma Expression: status_dim.SK_status='2' AND account_dim.region='Prague' AND date_dim.year='1989']";
									
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
}
