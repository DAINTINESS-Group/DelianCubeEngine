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

public class AnalyzeUpdatedQueriesTranslationAndExecutionTest {
	
	private static CubeManager testCubeManager;
	private static Session testSession;
	private static String testSchemaName;
	private static String testTypeOfConnection;
	
	// set up SQP and CubeManager
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		String typeOfConnection = "Spark";
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
	public final void testExpressionWithFoodmartFirstWorkingExample() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR year='1997' AND country='USA' AND product_subcategory='Milk' GROUP BY month, state AS 1st_working_example";

		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueries_1stWorkingExample.txt");
		
		String resultedCubeQueries = "";
								    				 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToUpdatedCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			//System.out.println(aq.toString());
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
	
	@Test
	public final void testExpressionWithFoodmartSecondWorkingExample() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR year='1997' AND product_category='Breakfast Foods' AND store_country='USA' GROUP BY quarter, product_subcategory AS 2nd_working_example";

		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueries_2ndWorkingExample.txt");
		
		String resultedCubeQueries = "";
								    				 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToUpdatedCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			//System.out.println(aq.toString());
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
	
	@Test
	public final void testExpressionWithFoodmartThirdWorkingExample() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";

		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueries_3rdWorkingExample.txt");
		
		String resultedCubeQueries = "";
								    				 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToUpdatedCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			//System.out.println(aq.toString());
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}
	
	@Test
	public final void testAnalyzeQueryExecution() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";
		
		ArrayList<AnalyzeQuery> testAnalyzeQueries = new ArrayList<AnalyzeQuery>();
		
		String testResultString = "";
		
		String expectedResultString = "";
		
		if(testTypeOfConnection.equals("Spark")) {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueryExecutionSparkResults.csv");
		}else {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueryExecutionResults.csv");
		}
	
		AnalyzeOperatorUpdated testAnalyzeOperator = new AnalyzeOperatorUpdated(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.executeUpdatedAnalyze();
		
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
