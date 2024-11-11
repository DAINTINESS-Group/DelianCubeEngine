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
	public final void testExpressionWithGroupersOnMiddleHierarchy() throws IOException {
		String incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR year='1997' AND country='USA' AND product_subcategory='Milk' GROUP BY month, state AS 1st_working_example";
		//incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR year='1997' AND product_category='Breakfast Foods' AND store_country='USA' GROUP BY quarter, product_subcategory AS 2nd_working_example";
		//incomingExpression = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q1' AND store_state='CA' AND media='Radio' GROUP BY month, region AS 3rd_working_example";
		String expectedCubeQueries = getFileContents("src/test/resources/OutputFiles/foodmart_reduced/AnalyzeQueries_WorkingExamples.txt");
		
		String resultedCubeQueries = "";
								    				 
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeTranslationManager.validateIncomingExpression();
		
		ArrayList<AnalyzeQuery> result = testAnalyzeTranslationManager.translateToUpdatedCubeQueries();
		
		for(AnalyzeQuery aq:result) {
			System.out.println(aq.toString());
			resultedCubeQueries += aq.toString() + "\n";
		}

		assertEquals(expectedCubeQueries,resultedCubeQueries);
	}

}
