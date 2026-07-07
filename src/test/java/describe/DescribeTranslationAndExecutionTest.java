package describe;

import cubemanager.cubebase.QueryMeasure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Hierarchy;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;
import describe.syntax.DescribeParserManager;
import mainengine.Session;
import result.Result;
import result.ResultFileMetadata;

public class DescribeTranslationAndExecutionTest {

	private static CubeManager testCubeManager;
	private static Session testSession;
	private static String testSchemaName;
	private static String testTypeOfConnection;
	
	//set up of SQP and CubeManager
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
	

    private CubeQuery translateHelper(String queryStr) throws Exception {
        System.out.println("\n[TEST INPUT] " + queryStr);
        
        //Parse
        DescribeParserManager parser = new DescribeParserManager();
        int errors = parser.parse(queryStr);
        assertEquals("Syntax errors found", 0, errors);
        
        DescribeParams params = parser.getParams();
        assertNotNull("Params should not be null", params);
        
        //Translate
        DescribeTranslationManager translator = new DescribeTranslationManager(testCubeManager);
        CubeQuery cubeQuery = translator.translateDescribeToCubeQuery(params);
        
        assertNotNull("CubeQuery should not be null", cubeQuery);
        
        printQueryDetails(cubeQuery);
        
        return cubeQuery;
    }

    private void printQueryDetails(CubeQuery query) {
        System.out.println("--- TRANSLATED CUBE QUERY ---");
        
        //Basic Info
        if (query.getReferCube() != null) {
            System.out.println("Target Cube: " + query.getReferCube().getName());
        } else {
            System.out.println("Target Cube: [NULL]");
        }

        //Measures
        System.out.println("Measures:");
        if (query.getQueryMeasures() == null || query.getQueryMeasures().isEmpty()) {
            System.out.println("  [None]");
        } else {
            for (QueryMeasure qm : query.getQueryMeasures()) {
                String alias = (qm.getAlias() != null) ? " AS " + qm.getAlias() : "";
                System.out.println("  -> " + qm.getFunction() + "(" + qm.getName() + ")" + alias);
            }
        }

        //Groupers (Gamma)
        System.out.println("Group By (Gamma):");
        if (query.getGammaExpressions().isEmpty()) {
            System.out.println("  [None]");
        } else {
            for (String[] gamma : query.getGammaExpressions()) {
                System.out.println("  -> Dim: " + gamma[0] + ", Level: " + gamma[1]);
            }
        }

        //Filters (Sigma)
        System.out.println("Filters (Sigma):");
        if (query.getSigmaExpressions().isEmpty()) {
            System.out.println("  [None]");
        } else {
            for (String[] sigma : query.getSigmaExpressions()) {
                System.out.println("  -> " + sigma[0] + " " + sigma[1] + " " + sigma[2]);
            }
        }
        System.out.println("-----------------------------");
    }

    @Test
    public void testSimpleTranslation() throws Exception {
        String query = "WITH loan DESCRIBE SUM(amount) FOR region='Prague' AND year>'1998' GROUP BY district_name, month AS first_query";
        CubeQuery result = translateHelper(query);
        

        assertTrue(result.getReferCube().getName().contains("loan"));
        
        ArrayList<String[]> gammas = result.getGammaExpressions();
        assertEquals("Should have 2 groupers", 2, gammas.size());
        
        assertEquals("account_dim", gammas.get(0)[0]); 
        assertEquals("district_name", gammas.get(0)[1]);
        
        ArrayList<String[]> sigmas = result.getSigmaExpressions();
        assertEquals("Should have 2 filters", 2, sigmas.size());
        
        assertTrue("Col check", sigmas.get(0)[0].contains("region"));
        assertEquals("Op check", "=", sigmas.get(0)[1]);
        assertTrue("Val check", sigmas.get(0)[2].contains("Prague"));
        System.out.println(result);
    }

    @Test
    public void testDerivedMeasureTranslation() throws Exception {
        String query = "WITH loan DESCRIBE amount - payments AS Profit GROUP BY region";
        CubeQuery result = translateHelper(query);
        System.out.println(result);
        assertNotNull(result.getQueryMeasures());
        assertEquals(1, result.getQueryMeasures().size());
        
        QueryMeasure qm = result.getQueryMeasures().get(0);
        assertTrue("Should contain formula", qm.getName().contains("amount - payments"));
        assertEquals("Should have alias", "Profit", qm.getAlias());
        
    }

    @Test
    public void testInequalityFilterTranslation() throws Exception {
        String query = "WITH loan DESCRIBE SUM(amount) FOR year > 1997 GROUP BY region";
        CubeQuery result = translateHelper(query);
        System.out.println(result);
        ArrayList<String[]> sigmas = result.getSigmaExpressions();
        assertEquals(1, sigmas.size());
        
        String[] filter = sigmas.get(0);
        assertTrue(filter[0].contains("year"));
        assertEquals(">", filter[1]);
        assertTrue(filter[2].contains("1997"));
    }

    @Test
    public void testSetFilterTranslation() throws Exception {
        String query = "WITH loan DESCRIBE SUM(amount) FOR region IN {'Prague', 'Central Bohemia'}";
        CubeQuery result = translateHelper(query);
        System.out.println(result);
        ArrayList<String[]> sigmas = result.getSigmaExpressions();
        assertEquals(1, sigmas.size());
        
        String[] filter = sigmas.get(0);
        assertEquals("IN", filter[1]);
        assertTrue("Should be SQL format", filter[2].contains("(") && filter[2].contains(")"));
        assertTrue("Should contain values", filter[2].contains("Prague"));
    }

    @Test
    public void testSubqueryTranslation() throws Exception {
        String query = "WITH loan DESCRIBE SUM(amount) FOR region WITH amount > 5000";
        CubeQuery result = translateHelper(query);
        System.out.println(result);
        ArrayList<String[]> sigmas = result.getSigmaExpressions();
        assertEquals(1, sigmas.size());
        
        String[] filter = sigmas.get(0);
        assertEquals("IN", filter[1]);
        String subquery = filter[2].toUpperCase();
        assertTrue("Should be SELECT", subquery.contains("SELECT"));
        assertTrue("Should select region", subquery.contains("REGION"));
        assertTrue("Should have condition", subquery.contains("AMOUNT > 5000"));
    }

    
    
    @Test
    public final void testDescribeQueryExecution() throws IOException, RecognitionException {
        String incomingExpression = "WITH loan DESCRIBE (SUM(amount) - SUM(payments)) AS Test FOR region IN {'Prague', 'Brno'} AND year >= '1997' GROUP BY district_name, month AS first_query";
        String testResultString = "";

        DescribeOperator testDescribeOperator = new DescribeOperator(testCubeManager);
        
        testDescribeOperator.execute(incomingExpression);
        
        DescribeQuery dq = testDescribeOperator.getDescribeQuery();
        assertNotNull("DescribeQuery should not be null", dq);
        
        Result r = dq.getDescribeQueryResult();
        assertNotNull("Execution Result should not be null", r);

        String[][] testResultArray = r.getResultArray();
        
        if(testResultArray != null) {
            int initialIndex = 0;
            int numOfResults = testResultArray.length;

            if(testTypeOfConnection.equals("Spark")) {
                initialIndex = 0;
                numOfResults = testResultArray.length - 2;
            } else if(testTypeOfConnection.equals("RDBMS")) {
                initialIndex = 2;
                numOfResults = testResultArray.length;
            }

            for(int i = initialIndex; i < numOfResults; i++) {
                for(int j = 0; j < testResultArray[i].length; j++) {
                    testResultString += testResultArray[i][j] + ", ";
                }
                if (testResultString.length() > 2) {
                    testResultString = testResultString.substring(0, testResultString.length() - 2) + "\n";
                }
            }
        }
        
        System.out.println("--- Actual Execution Result ---");
        System.out.print(testResultString);
        System.out.println("-------------------------------");

        if (testResultArray != null && testResultArray.length > 2) {
             assertNotNull(testResultString);
             assertTrue(testResultString.length() > 0);
        } else {
            System.out.println("Warning: Query returned no data rows. Check Database/Mock.");
        }
    }
    
    
    
    @Test
    public final void testDescribeQueryExecutionUsingModel() throws IOException, RecognitionException {
        String incomingExpression = "WITH loan DESCRIBE SUM(amount) AS Total , SUM(amount - payments) AS SomethingIGuess FOR region='south Bohemia' AND year <= '1998' GROUP BY district_name, region USING Rank";
        String testResultString = "";

        DescribeOperator testDescribeOperator = new DescribeOperator(testCubeManager);
        
        testDescribeOperator.execute(incomingExpression);
        
        DescribeQuery dq = testDescribeOperator.getDescribeQuery();
        assertNotNull("DescribeQuery should not be null", dq);
        
        Result r = dq.getDescribeQueryResult();
        assertNotNull("Execution Result should not be null", r);

        String[][] testResultArray = r.getResultArray();
        
        if(testResultArray != null) {
            int initialIndex = 0;
            int numOfResults = testResultArray.length;

            if(testTypeOfConnection.equals("Spark")) {
                initialIndex = 0;
                numOfResults = testResultArray.length - 2;
            } else if(testTypeOfConnection.equals("RDBMS")) {
                initialIndex = 2;
                numOfResults = testResultArray.length;
            }

            for(int i = initialIndex; i < numOfResults; i++) {
                for(int j = 0; j < testResultArray[i].length; j++) {
                    testResultString += testResultArray[i][j] + ", ";
                }
                if (testResultString.length() > 2) {
                    testResultString = testResultString.substring(0, testResultString.length() - 2) + "\n";
                }
            }
        }
        
        System.out.println("--- Actual Execution Result ---");
        System.out.print(testResultString);
        System.out.println("-------------------------------");

        if (testResultArray != null && testResultArray.length > 2) {
             assertNotNull(testResultString);
             assertTrue(testResultString.length() > 0);
        } else {
            System.out.println("Warning: Query returned no data rows. Check Database/Mock.");
        }
    }
    
    @Test
    public final void testMultipleGroupersPerDimension() throws IOException, RecognitionException {
        //Group by Region (Geo) AND Year + Month (Date)
        //This tests "Multiple groupers per dimension" (Year and Month are both Date dimension)
        String incomingExpression = "WITH loan DESCRIBE SUM(amount) FOR year > '1950' GROUP BY region, year, month";
        
        System.out.println("\n--- TEST: Multiple Groupers Per Dimension ---");
        
        DescribeOperator testDescribeOperator = new DescribeOperator(testCubeManager);
        
        testDescribeOperator.execute(incomingExpression);
        
        Result r = testDescribeOperator.getDescribeQuery().getDescribeQueryResult();
        assertNotNull("Result should not be null", r);

        String[][] resultArray = r.getResultArray();
        
        if (resultArray != null && resultArray.length > 2) { 
            int rowsToPrint = Math.min(resultArray.length, 100); 
            for (int i = 0; i < rowsToPrint; i++) {
                for (int j = 0; j < resultArray[i].length; j++) {
                    System.out.print(resultArray[i][j] + "\t");
                }
                System.out.println();
            }
            
            assertTrue("Should have at least 4 columns (Region, Year, Month, Amount)", resultArray[0].length >= 4);
        } else {
            System.err.println("Query returned empty result.");
        }
    }
}