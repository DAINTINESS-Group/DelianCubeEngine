package test.analyzeOperator;

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
	
	@Test
	public final void testExpressionWithGroupersOnMiddleHierarchy() {
		String incomingExpression = "ANALYZE min amount FROM loan FOR region=\"Prague\" AND year=\"1998\" GROUP BY district_name, month;";
		
		String expectedCubeQueries = "[QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.month\nSigma Expression: account_dim.region=\"Prague\" AND date_dim.year=\"1998\","
									 + " QueryName: AnalyzeSiblingQuery-1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.month\nSigma Expression: account_dim.All_account=\"ALL\" AND date_dim.year=\"1998\","
								     + " QueryName: AnalyzeSiblingQuery-2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: date_dim.Year , account_dim.district_name\nSigma Expression: account_dim.region=\"Prague\" AND date_dim.All_date=\"ALL\","
									 + " QueryName: AnalyzeDrillDownQuery-1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.month\nSigma Expression: account_dim.district_name=\"Hl.m. Praha\" AND date_dim.year=\"1998\","
								     + " QueryName: AnalyzeDrillDownQuery-2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-01\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-3\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-02\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-4\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-03\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-5\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-04\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-6\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-05\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-7\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-06\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-8\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-07\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-9\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-08\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-10\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-09\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-11\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-10\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-12\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-11\" AND account_dim.region=\"Prague\","
								     + " QueryName: AnalyzeDrillDownQuery-13\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.Day\nSigma Expression: date_dim.Month=\"1998-12\" AND account_dim.region=\"Prague\"]";
								     
									 
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

		
	}
	
	@Test
	public final void testExpressionWithGroupersOnTopHierarchy() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region=\"Prague\" AND year=\"1998\" AND status=\"Contract Finished/No Problems\" GROUP BY All_account, All_date;";
		
		String expectedCubeQueries = "[QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.All_date\nSigma Expression: account_dim.region=\"Prague\" AND date_dim.year=\"1998\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeSiblingQuery-1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.All_date\nSigma Expression: account_dim.All_account=\"ALL\" AND date_dim.year=\"1998\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeSiblingQuery-2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: date_dim.All_date , account_dim.All_account\nSigma Expression: account_dim.region=\"Prague\" AND date_dim.All_date=\"ALL\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-1\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.All_date\nSigma Expression: account_dim.district_name=\"Hl.m. Praha\" AND date_dim.year=\"1998\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-2\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-01\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-3\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-02\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-4\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-03\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-5\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-04\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-6\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-05\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-7\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-06\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-8\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-07\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-9\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-08\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-10\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-09\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-11\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-10\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-12\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-11\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\","
									 +" QueryName: AnalyzeDrillDownQuery-13\nBasicCube : loan_cube\nAggregate Function : Max\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.Year\nSigma Expression: date_dim.Month=\"1998-12\" AND account_dim.region=\"Prague\" AND status_dim.status=\"Contract Finished/No Problems\"]";
									 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	@Test
	public final void testExpressionWithGroupersOnBottomHierarchy() {
		String incomingExpression = "ANALYZE sum amount FROM loan FOR district_name=\"Jihlava\" AND month=\"1999-04\" GROUP BY account, SK_day;";		
		
		String expectedCubeQueries = "[QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_day\nSigma Expression: account_dim.district_name=\"Jihlava\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeSiblingQuery-1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.SK_day\nSigma Expression: account_dim.region=\"south Moravia\" AND date_dim.month=\"1999-04\","
				                     +" QueryName: AnalyzeSiblingQuery-2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: date_dim.Day , account_dim.account\nSigma Expression: account_dim.district_name=\"Jihlava\" AND date_dim.Year=\"1999\","
									 +" QueryName: AnalyzeDrillDownQuery-1\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"155\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-2\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"478\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-3\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"513\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-4\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"675\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-5\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"713\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-6\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"920\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-7\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"949\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-8\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"1246\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-9\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"1320\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-10\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"1414\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-11\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2056\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-12\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2155\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-13\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2221\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-14\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2319\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-15\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2623\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-16\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2664\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-17\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2711\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-18\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2803\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-19\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"2909\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-20\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3337\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-21\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3376\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-22\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3448\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-23\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3480\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-24\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3538\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-25\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3556\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-26\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3576\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-27\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3676\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-28\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"3903\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-29\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"4026\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-30\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"4105\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-31\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"4480\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-32\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"5126\" AND date_dim.month=\"1999-04\","
									 +" QueryName: AnalyzeDrillDownQuery-33\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-01\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-34\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-02\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-35\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-03\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-36\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-04\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-37\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-05\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-38\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-06\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-39\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-07\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-40\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-08\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-41\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-09\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-42\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-10\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-43\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-11\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-44\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-12\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-45\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-13\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-46\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-14\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-47\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-15\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-48\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-16\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-49\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-17\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-50\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-18\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-51\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-19\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-52\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-20\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-53\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-21\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-54\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-22\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-55\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-23\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-56\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-24\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-57\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-25\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-58\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-26\" AND account_dim.district_name=\"Jihlava\","
									 +" QueryName: AnalyzeDrillDownQuery-59\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-27\" AND account_dim.district_name=\"Jihlava\","
 									 +" QueryName: AnalyzeDrillDownQuery-60\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-28\" AND account_dim.district_name=\"Jihlava\","
 									 +" QueryName: AnalyzeDrillDownQuery-61\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-29\" AND account_dim.district_name=\"Jihlava\","
 									 +" QueryName: AnalyzeDrillDownQuery-62\nBasicCube : loan_cube\nAggregate Function : Sum\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.SK_Day\nSigma Expression: date_dim.Day=\"1999-04-30\" AND account_dim.district_name=\"Jihlava\"]";
									 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
	
	@Test
	public final void testExpressionWithGroupersOnBottomAndMiddleHierarchy() {
		String incomingExpression = "ANALYZE min amount FROM loan FOR region=\"south Moravia\" GROUP BY account, day;";		
		
		String expectedCubeQueries = "[QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account , date_dim.day\nSigma Expression: account_dim.region=\"south Moravia\","
									+" QueryName: AnalyzeSiblingQuery-1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.district_name , date_dim.day\nSigma Expression: account_dim.All_account=\"ALL\","
									+" QueryName: AnalyzeSiblingQuery-2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: date_dim.Month , account_dim.account\nSigma Expression: account_dim.region=\"south Moravia\","
									+" QueryName: AnalyzeDrillDownQuery-1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Prostejov\","
									+" QueryName: AnalyzeDrillDownQuery-2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Hodonin\","
									+" QueryName: AnalyzeDrillDownQuery-3\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Brno - mesto\","
									+" QueryName: AnalyzeDrillDownQuery-4\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Zlin\","
									+" QueryName: AnalyzeDrillDownQuery-5\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Vyskov\","
									+" QueryName: AnalyzeDrillDownQuery-6\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Brno - venkov\","
									+" QueryName: AnalyzeDrillDownQuery-7\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Blansko\","
									+" QueryName: AnalyzeDrillDownQuery-8\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Znojmo\","
									+" QueryName: AnalyzeDrillDownQuery-9\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Jihlava\","
									+" QueryName: AnalyzeDrillDownQuery-10\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Kromeriz\","
									+" QueryName: AnalyzeDrillDownQuery-11\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Zdar nad Sazavou\","
									+" QueryName: AnalyzeDrillDownQuery-12\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Trebic\","
									+" QueryName: AnalyzeDrillDownQuery-13\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Breclav\","
									+" QueryName: AnalyzeDrillDownQuery-14\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.account_id , date_dim.day\nSigma Expression: account_dim.district_name=\"Uherske Hradiste\"]";
									
		
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	@Test
	public final void testExpressionWithGroupersOnMiddleAndTopHierarchy() {
		String incomingExpression = "ANALYZE min amount FROM loan FOR region=\"Prague\" GROUP BY All_account, status;";		
		
		String expectedCubeQueries = "[QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.All_account , status_dim.status\nSigma Expression: account_dim.region=\"Prague\","
									+" QueryName: AnalyzeSiblingQuery-1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.All_account , status_dim.status\nSigma Expression: account_dim.All_account=\"ALL\","
									+" QueryName: AnalyzeSiblingQuery-2\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: status_dim.All_status , account_dim.All_account\nSigma Expression: account_dim.region=\"Prague\","
									+" QueryName: AnalyzeDrillDownQuery-1\nBasicCube : loan_cube\nAggregate Function : Min\nMeasure : amount\nGamma Expression: account_dim.region , status_dim.status\nSigma Expression: account_dim.district_name=\"Hl.m. Praha\"]";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
		
	@Test
	public final void testExpressionWithGroupersOnBottomAndTopHierarchy() {
		String incomingExpression = "ANALYZE avg amount FROM loan FOR account=\"100\" AND year=\"1999\" GROUP BY All_account, SK_day;";		
		
		String expectedCubeQueries = "[QueryName: AnalyzeBaseQuery\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_day\nSigma Expression: account_dim.account=\"100\" AND date_dim.year=\"1999\","
									+" QueryName: AnalyzeSiblingQuery-1\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_day\nSigma Expression: account_dim.district_name=\"Usti nad Labem\" AND date_dim.year=\"1999\","
									+" QueryName: AnalyzeSiblingQuery-2\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: date_dim.Day , account_dim.All_account\nSigma Expression: account_dim.account=\"100\" AND date_dim.All_date=\"ALL\","
									+" QueryName: AnalyzeDrillDownQuery-1\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.region , date_dim.SK_day\nSigma Expression: account_dim.account_id=\"100\" AND date_dim.year=\"1999\","
									+" QueryName: AnalyzeDrillDownQuery-2\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-01\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-3\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-02\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-4\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-03\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-5\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-04\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-6\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-05\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-7\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-06\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-8\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-07\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-9\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-08\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-10\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-09\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-11\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-10\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-12\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-11\" AND account_dim.account=\"100\","
									+" QueryName: AnalyzeDrillDownQuery-13\nBasicCube : loan_cube\nAggregate Function : Avg\nMeasure : amount\nGamma Expression: account_dim.All_account , date_dim.SK_Day\nSigma Expression: date_dim.Month=\"1999-12\" AND account_dim.account=\"100\"]";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<CubeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		String resultedCubeQueries = result.toString();

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
}
