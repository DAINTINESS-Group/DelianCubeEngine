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


public class AnalyzeOptimizerTranslationAndExecutionTest {
	private static CubeManager testCubeManager;
	private static Session testSession;
	private static String testSchemaName;
	private static String testTypeOfConnection;
	
	// set up SQP and CubeManager
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "foodmart_reduced");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "sales");
		userInputList.put("inputFolder", "foodmart_reduced");
		
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
	public final void testOptimizerWithFoodmartThirdWorkingExample() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";

		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueries_3rdWorkingExampleOptimizer.txt");
		
		String resultedCubeQueries = "";
								    				 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToOptimizedSingleCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			System.out.println(aq.toString());
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
	
	@Test
	public final void testAnalyzeQueryExecutionWithMQOBeforeResultMapping() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";
		
		ArrayList<AnalyzeQuery> testAnalyzeQueries = new ArrayList<AnalyzeQuery>();
		
		String testResultString = "";
		
		String expectedResultString = "";
		
		if(testTypeOfConnection.equals("Spark")) {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeOptimizerQueryExecutionSparkResults.csv");
		}else {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeOptimizerQueryExecutionResults.csv");
		}
	
		AnalyzeOperatorMultiQueryToSingleQueryOptimizer testAnalyzeOperator = new AnalyzeOperatorMultiQueryToSingleQueryOptimizer(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.executeAnalyzeWithMultiQueryToSingleQueryOptimizer();
		
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
	public final void testAnalyzeQueryExecutionWithMQOAfterResultMapping() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";
		
		ArrayList<AnalyzeQuery> testAnalyzeQueries = new ArrayList<AnalyzeQuery>();
		
		String testResultString = "";
		
		String expectedResultString = "";
		
		if(testTypeOfConnection.equals("Spark")) {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeMQOSparkResults.csv");
		}else {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeMQOResults.csv");
		}
	
		AnalyzeOperatorMultiQueryToSingleQueryOptimizer testAnalyzeOperator = new AnalyzeOperatorMultiQueryToSingleQueryOptimizer(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.executeAnalyzeWithMultiQueryToSingleQueryOptimizer();
		
		testAnalyzeQueries = testAnalyzeOperator.getAnalyzeQueries();
		
		for(AnalyzeQuery aq: testAnalyzeQueries) {
			ArrayList<String> mqoResult = aq.getAnalyzeMQOResult();
			for(String str: mqoResult) {
				testResultString += str + "\n";
			}
		}
		assertEquals(expectedResultString,testResultString);
	}
}
