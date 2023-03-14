package test.analyzeOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import analyze.AnalyzeTranslationManager;
import cubemanager.CubeManager;
import mainengine.Session;

/**
 * A test class that checks the validation process of the incoming analyze expression
 * @author Marios Iakovidis(mariosjkb)
 */

public class AnalyzeInputValidationTest {
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
	
	// test incoming expression without FROM and AND keywords
	@Test
	public final void testMissingKeywords() {
		String incomingExpression = "ANALYZE min(amount) loan FOR region='Prague'  year='1998' GROUP BY district_name, month AS query";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		int numOfErrors = testAnalyzeTranslationManager.validateIncomingExpression();
		
		assertNotEquals(0,numOfErrors);
	}
	
	//test incoming expression without "" in the sigma expressions values
	@Test
	public final void testMissingDoubleQuotes() {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region=Prague AND year=1998 GROUP BY district_name, month AS query";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		int numOfErrors = testAnalyzeTranslationManager.validateIncomingExpression();
		
		assertNotEquals(0,numOfErrors);
	}
	
	//test incoming expression with missing aggregate function, cubeName and gamma expressions
	@Test
	public final void testMissingStatements() {
		String incomingExpression = "ANALYZE (amount) FROM FOR region='Prague' AND year='1998' GROUP BY district_name AS query";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		int numOfErrors = testAnalyzeTranslationManager.validateIncomingExpression();
		
		assertNotEquals(0,numOfErrors);
	}
	
	//test incoming expression with missing comma between gamma expressions
	@Test
	public final void testMissingComma() {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region=\"Prague\" AND year=\"1998\" GROUP BY district_name month AS query";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		int numOfErrors = testAnalyzeTranslationManager.validateIncomingExpression();
		
		assertNotEquals(0,numOfErrors);
	}
	
	// test incoming expression without AS clause
	@Test
	public final void testMissingAlias() {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region= 'Prague' AND year='1998' GROUP BY district_name,month";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		int numOfErrors = testAnalyzeTranslationManager.validateIncomingExpression();
		
		assertNotEquals(0,numOfErrors);
	}
	
	// test incoming expression with correct syntax
	@Test
	public final void testCorrectSyntax() {
		String incomingExpression = "ANALYZE min(amount) FROM loan FOR region= 'Prague' AND year='1998' GROUP BY district_name,month AS query";
		
		AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		int numOfErrors = testAnalyzeTranslationManager.validateIncomingExpression();
		
		assertEquals(0,numOfErrors);
	}
}
