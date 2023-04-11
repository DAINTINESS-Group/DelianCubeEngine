package analyze;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
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
		String typeOfConnection = "Spark";
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
	
	public String getFileContents(String fileName) throws IOException {
		String retString = "";
		String str;
		File file = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(file));
		while((str = br.readLine()) != null) {
			retString += str + "\n";
		}
		br.close();
		return retString;
	}
	
	@Test
	public final void testExpressionWithGroupersOnMiddleHierarchy() throws IOException {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region='Prague' AND year='1998' GROUP BY district_name, month AS first_query";
		
		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueries_FirstQuery.txt");
		
		String resultedCubeQueries = "";
								    				 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	@Test
	public final void testExpressionWithGroupersOnTopHierarchy() throws IOException {
		String incomingExpression = "ANALYZE max(amount) FROM loan FOR region='Prague' AND year='1998' AND status='Contract Finished/No Problems' GROUP BY All_account, All_date AS test_query";
		
		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueries_TestQuery.txt");
		
		String resultedCubeQueries = "";
									 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);

	}
	
	@Test
	public final void testExpressionWithGroupersOnBottomHierarchy() throws IOException {
		String incomingExpression = "ANALYZE sum(amount) FROM loan FOR district_name='Jihlava' AND month='1999-04' GROUP BY account, SK_day AS large_query";		
		
		String expectedCubeQueriesRDBMS = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueries_LargeQuery_RDBMS.txt");
		
		String expectedCubeQueriesSpark = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueries_LargeQuery_Spark.txt");
		String resultedCubeQueries = "";
									 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			resultedCubeQueries += aq.toString() + "\n";
		}
		
		if(testTypeOfConnection.equals("Spark")) {
			assertEquals(expectedCubeQueriesSpark,resultedCubeQueries);
		}else {
			assertEquals(expectedCubeQueriesRDBMS,resultedCubeQueries);
		}
	}
	
	@Test
	public final void testThreeGroupers() throws IOException {
		String incomingExpression = "ANALYZE max(amount) FROM loan FOR region='Prague' AND year='1989' AND status='Contract Finished/No Problems' GROUP BY district_name,month,SK_status AS multiple_groupers";
		
		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueries_MultipleGroupers.txt");
		
		String resultedCubeQueries = "";
									
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
}
