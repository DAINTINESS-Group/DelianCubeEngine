package analyze;

import static org.junit.Assert.*;

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

public class AnalyzeOptimizerAndUpdatedExecutionTest {

	private static CubeManager testCubeManager;
	private static Session testSession;
	private static String testSchemaName;
	private static String testTypeOfConnection;
	
	// set up SQP and CubeManager
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "tpc_ds_cube_1gb");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "store_sales");
		userInputList.put("inputFolder", "tpc_ds_cube_1gb");
		
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
	public final void testAnalyzeMQOExecution() throws IOException {
		//String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";

		String incomingExpression = "ANALYZE sum(ss_ext_sales_price) FROM store_sales FOR month='11-2000' AND category='Women' GROUP BY year, category AS Q42";

		
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
	
	@Test
	public final void testPlainAnalyzeExecution() throws IOException {
		//String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";

		String incomingExpression = "ANALYZE sum(ss_ext_sales_price) FROM store_sales FOR month='11-2000' AND category='Women' GROUP BY year, category AS Q42";

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
				System.out.println("Number of resulted tuples for " + aq.getType() + " Modified Gamma " + aq.getModifiedGammaValue()+ " Modified Sigma " + aq.getModifiedSigmaValue() + " = " + (numOfResults - initialIndex));

			}
		}
		assertEquals(expectedResultString,testResultString);
	}
	
	@Test
	public final void testDuoAnalyzeExecution() throws IOException {
		String incomingExpression = "ANALYZE sum(ss_ext_sales_price) FROM store_sales FOR month='11-2000' AND category='Women' GROUP BY year, category AS Q42";

		//String incomingExpression = "ANALYZE sum(ss_ext_sales_price) FROM store_sales FOR month = '11-2000' AND category='Women'  GROUP BY year, category AS Q42";

		ArrayList<AnalyzeQuery> testAnalyzeQueries = new ArrayList<AnalyzeQuery>();
		
		String testResultString = "";
		
		String expectedResultString = "";
		
		if(testTypeOfConnection.equals("Spark")) {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueryExecutionSparkResults.csv");
		}else {
			expectedResultString = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueryExecutionResults.csv");
		}
	
		AnalyzeOperatorMultiQueryToDuoQueryOptimizer testAnalyzeOperator = new AnalyzeOperatorMultiQueryToDuoQueryOptimizer(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.executeAnalyzeWithMultiQueryToDuoQueryOptimizer();
		
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
