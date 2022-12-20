package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import mainengine.AnalyzeOperator;
import mainengine.Session;

public class AnalyzeOperatorValidateIncomingExpressionTest {

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
	
	// test FROM keyword missing from incoming expression
	@Test
	public final void testMissingFromKeyword() {
		String incomingExpression = "ANALYZE max amount loan FOR region=Prague year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test BY keyword missing from incoming expression
	@Test
	public final void testMissingByKeyword() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if '=' char and sigma expression values have gaps between them
	@Test
	public final void testInvalidGapsInSigmaExpressions() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region = Prague AND year = 1998 AND status = Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if the values of sigma expressions contain gaps
	@Test
	public final void testInvalidGapsInSigmaExpressionsValues() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract Finished/No Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if the gamma expressions are not seperated by gap
	@Test
	public final void testInvalidGapsInGammaExpressions() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account,All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if ANALYZE keyword is invalid
	@Test
	public final void testInvalidAnalyzeKeyword() {
		String incomingExpression = "ANALZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if aggregate function is invalid
	@Test
	public final void testInvalidAggregateFunction() {
		String incomingExpression = "ANALYZE maxx amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if the incoming expression has more than one measure
	@Test
	public final void testInvalidNumberOfMeasures() {
		String incomingExpression = "ANALYZE max amount amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if the incoming expression has more than one cube names
	@Test
	public final void testInvalidNumberOfCubes() {
		String incomingExpression = "ANALYZE max amount FROM loan loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if the sigma dimensions exist
	@Test
	public final void testSigmaExpressionsExistance() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR area=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if gamma expressions exist
	@Test
	public final void testGammaExpressionsExistance() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if FOR keyword is missing
	@Test
	public final void testMissingForKeyword() {
		String incomingExpression = "ANALYZE max amount FROM loan region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test if AND keyword is missing in case of multiple sigma expressions
	@Test
	public final void testMissingAndKeyword() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region=Prague year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertFalse("Incoming expression is valid:",result);
	}
	
	// test the correct syntax of an incoming expression
	@Test
	public final void testCorrectIncomingExpression() {
		String incomingExpression = "ANALYZE max amount FROM loan FOR region=Prague AND year=1998 AND status=Contract_Finished/No_Problems BY All_account, All_date";
		
		AnalyzeOperator operator = new AnalyzeOperator(incomingExpression,testCubeManager);
		
		boolean result = operator.validateIncomingExpression(incomingExpression);
		
		assertTrue("Incoming expression is valid:",result);
	}
}
