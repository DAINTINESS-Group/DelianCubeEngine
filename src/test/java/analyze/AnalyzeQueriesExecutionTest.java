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
import mainengine.Session;
import result.Result;

public class AnalyzeQueriesExecutionTest {

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
	public final void testAnalyzeQueryExecution() throws IOException {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region='Prague' AND year='1998' GROUP BY district_name, month AS first_query";
		
		ArrayList<AnalyzeQuery> testAnalyzeQueries = new ArrayList<AnalyzeQuery>();
		
		String testResultString = "";
		
		String expectedResultString = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueryExecutionExpectedResults.csv");
			
		AnalyzeOperator testAnalyzeOperator = new AnalyzeOperator(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.execute();
		
		testAnalyzeQueries = testAnalyzeOperator.getAnalyzeQueries();
		
		for(AnalyzeQuery aq: testAnalyzeQueries) {
			int initialIndex = 0;
			int numOfResults = 0;
			Result r = aq.getAnalyzeQueryResult();
			String[][] testResultArray = r.getResultArray();
			if(testResultArray != null) {
				if(testTypeOfConnection.equals("Spark")) {
					initialIndex = 0;
					numOfResults = testResultArray.length - 2;
				}else if(testTypeOfConnection.equals("RDBMS")) {
					initialIndex = 2;
					numOfResults = testResultArray.length;
				}
				for(int i = initialIndex;i < numOfResults;i++) {
					for(int j = 0;j < testResultArray[i].length;j++) {
						testResultString += testResultArray[i][j] + ", ";
					}
					testResultString = testResultString.substring(0, testResultString.length()-2) + "\n";
				}
				
			}
		}
		assertEquals(expectedResultString,testResultString);
	}

	@Test
	public final void testAnalyzeQueryExecutionWithoutEmptyQueries() throws IOException {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region='south Moravia' GROUP BY account, day AS test_query_2";
		
		ArrayList<AnalyzeQuery> testAnalyzeQueries = new ArrayList<AnalyzeQuery>();
		
		String testResultString = "";
		
		String expectedResultString = "";
		
		if(testTypeOfConnection.equals("Spark")) {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueryExecutionWithoutEmptyQueriesSparkResults.csv");
		}else {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueryExecutionWithoutEmptyQueriesResults.csv");
		}
	
		AnalyzeOperator testAnalyzeOperator = new AnalyzeOperator(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.execute();
		
		testAnalyzeQueries = testAnalyzeOperator.getAnalyzeQueries();
		
		for(AnalyzeQuery aq: testAnalyzeQueries) {
			int initialIndex = 0;
			int numOfResults = 0;
			Result r = aq.getAnalyzeQueryResult();
			String[][] testResultArray = r.getResultArray();
			if(testResultArray != null) {
				if(testTypeOfConnection.equals("Spark")) {
					initialIndex = 0;
					numOfResults = testResultArray.length - 2;
				}else if(testTypeOfConnection.equals("RDBMS")) {
					initialIndex = 2;
					numOfResults = testResultArray.length;
				}
				for(int i = initialIndex;i < numOfResults;i++) {
					for(int j = 0;j < testResultArray[i].length;j++) {
						testResultString += testResultArray[i][j] + ", ";
					}
					testResultString = testResultString.substring(0, testResultString.length()-2) + "\n";
				}
				
			}
		}
		assertEquals(expectedResultString,testResultString);
	}
	@Test
	public final void testAnalyzeQueryExecutionWithSomeEmptyQueries() throws IOException {
		String incomingExpression = "ANALYZE max(amount) FROM loan FOR region='Prague' AND year='1989' AND status='Contract Finished/No Problems' GROUP BY district_name,month,SK_status AS multiple_groupers";
		
		ArrayList<AnalyzeQuery> testAnalyzeQueries = new ArrayList<AnalyzeQuery>();
		
		String testResultString = "";
		
		String expectedResultString =	getFileContents("src/test/resources/OutputFiles/pkdd99_star/AnalyzeQueryExecutionWithSomeEmptyQueriesResults.csv");
		
		AnalyzeOperator testAnalyzeOperator = new AnalyzeOperator(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.execute();
		
		testAnalyzeQueries = testAnalyzeOperator.getAnalyzeQueries();
		
		for(AnalyzeQuery aq: testAnalyzeQueries) {
			int initialIndex = 0;
			int numOfResults = 0;
			Result r = aq.getAnalyzeQueryResult();
			String[][] testResultArray = r.getResultArray();
			if(testResultArray != null) {
				if(testTypeOfConnection.equals("Spark")) {
					initialIndex = 0;
					numOfResults = testResultArray.length - 2;
				}else if(testTypeOfConnection.equals("RDBMS")) {
					initialIndex = 2;
					numOfResults = testResultArray.length;
				}
				for(int i = initialIndex;i < numOfResults;i++) {
					for(int j = 0;j < testResultArray[i].length;j++) {
						testResultString += testResultArray[i][j] + ", ";
					}
					testResultString = testResultString.substring(0, testResultString.length()-2) + "\n";
				}
				
			}
		}
		assertEquals(expectedResultString,testResultString);
	}
}
