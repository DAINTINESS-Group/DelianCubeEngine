package analyze;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import mainengine.Session;

public class AnalyzeWrongExpressions {
	
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
	
	// test that uses an Analyze intentional query with wrong values in the sigma expressions
	@Test
	public final void testAnalyzeQueryWithWrongExpressionValues() throws IOException {
		String incomingExpression = "ANALYZE sum(amount) FROM loan FOR district_name='Athens' AND month='13-1998' GROUP BY region,day AS mistaken_values";
		
		String expectedReportFileContents = "## ------------------------------------ANALYZE OPERATOR REPORT-------------------------\n\n"
				+ "ANALYZE OPERATOR INTENTIONAL QUERY: \n\n**" 
				+ incomingExpression + "**\n\n"
				+ "**ERROR WAS ENCOUNTERED DURING THE OPERATOR'S EXECUTION**\n\n"
			    + "AnalyzeExecutionError: Expressions or values of the given ANALYZE incoming expression are invalid!Please check.\n"
				+ "\n";
		
		AnalyzeOperator testAnalyzeOperator = new AnalyzeOperator(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.execute();
		
		String reportFileContents = getFileContents("OutputFiles" + File.separator + "mistaken_values" + "-Analyze_Operator_Report.md");
		
		assertEquals(expectedReportFileContents,reportFileContents);
	}
	
	// test that uses an Analyze intentional query with mistakes in the sigma expressions
	@Test
	public final void testAnalyzeQueryWithWrongExpressions() throws IOException {
		String incomingExpression = "ANALYZE sum(amount) FROM loan FOR rgon='Athens' AND y='2030' GROUP BY district_name,month AS mistaken_expressions";
		
		String expectedReportFileContents = "## ------------------------------------ANALYZE OPERATOR REPORT-------------------------\n\n"
				+ "ANALYZE OPERATOR INTENTIONAL QUERY: \n\n**" 
				+ incomingExpression + "**\n\n"
				+ "**ERROR WAS ENCOUNTERED DURING THE OPERATOR'S EXECUTION**\n\n"
			    + "AnalyzeExecutionError: Expressions or values of the given ANALYZE incoming expression are invalid!Please check.\n"
				+ "\n";
		
		AnalyzeOperator testAnalyzeOperator = new AnalyzeOperator(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.execute();
		
		String reportFileContents = getFileContents("OutputFiles" + File.separator + "mistaken_expressions" + "-Analyze_Operator_Report.md");
		
		assertEquals(expectedReportFileContents,reportFileContents);
	}
	
	// test that uses an Analyze intentional query with wrong gamma expressions
	@Test
	public final void testAnalyzeQueryWithWrongGammaExpressions() throws IOException {
		String incomingExpression = "ANALYZE sum(amount) FROM loan FOR region='Prague' AND year='1998' GROUP BY district,month AS mistaken_gamma_expressions";
		
		String expectedReportFileContents = "## ------------------------------------ANALYZE OPERATOR REPORT-------------------------\n\n"
				+ "ANALYZE OPERATOR INTENTIONAL QUERY: \n\n**" 
				+ incomingExpression + "**\n\n"
				+ "**ERROR WAS ENCOUNTERED DURING THE OPERATOR'S EXECUTION**\n\n"
			    + "AnalyzeExecutionError: Expressions or values of the given ANALYZE incoming expression are invalid!Please check.\n"
				+ "\n";
		
		AnalyzeOperator testAnalyzeOperator = new AnalyzeOperator(incomingExpression,testCubeManager,testSchemaName,testTypeOfConnection);
		
		testAnalyzeOperator.execute();
		
		String reportFileContents = getFileContents("OutputFiles" + File.separator + "mistaken_gamma_expressions" + "-Analyze_Operator_Report.md");
		
		assertEquals(expectedReportFileContents,reportFileContents);
	}

}
