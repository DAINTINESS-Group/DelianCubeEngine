package describe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import describe.syntax.DescribeParserManager;
import mainengine.Session;

/**
 * A test class that checks the validation process of the incoming describe expression.
 * @author Nik-Pt
 */
public class DescribeInputValidationTest {
    
    private static CubeManager testCubeManager;
    private static Session testSession;
    private static String testSchemaName;
    private static String testTypeOfConnection;
    
    private DescribeParserManager parserManager;

    // Set up SQP and CubeManager (Standard boiler-plate, even if Parser is standalone)
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
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

    @Before
    public void setUp() {
        this.parserManager = new DescribeParserManager();
    }
    
    /**
     * Helper method returns TRUE if syntax is correct (0 errors), FALSE otherwise.
     */
    private boolean validateIncomingExpression(String query) {
        try {
            int errors = parserManager.parse(query);
            return errors == 0;
        } catch (RecognitionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public final void testMissingKeywords() {
        //Query missing 'WITH' and 'DESCRIBE'
        String incomingExpression = "loan SUM(amount) FOR region='Prague' AND year='1998' GROUP BY district_name";
        
        boolean isValid = validateIncomingExpression(incomingExpression);
        
        assertFalse("Should fail due to missing keywords", isValid);
    }
    
    @Test
    public final void testMalformedFilter() {
        //'region Prague' instead of 'region = 'Prague''
        String incomingExpression = "WITH loan DESCRIBE SUM(amount) FOR region Prague GROUP BY district_name";
        
        boolean isValid = validateIncomingExpression(incomingExpression);
        
        assertFalse("Should fail due to malformed filter", isValid);
    }
    
    @Test
    public final void testMissingCubeName() {
        String incomingExpression = "WITH DESCRIBE SUM(amount) FOR region='Prague' GROUP BY district_name";
        
        boolean isValid = validateIncomingExpression(incomingExpression);
        
        assertFalse("Should fail due to missing cube name", isValid);
    }
    
    @Test
    public final void testMissingComma() {
        //Missing comma between measures (SUM(amount) AVG(amount))
        String incomingExpression = "WITH loan DESCRIBE SUM(amount) AVG(amount) FOR region='Prague' GROUP BY district_name";
        
        boolean isValid = validateIncomingExpression(incomingExpression);
        
        assertFalse("Should fail due to missing comma", isValid);
    }
    
    @Test
    public final void testCorrectSyntax() {
        String incomingExpression = "WITH loan DESCRIBE SUM(amount), AVG(payments) FOR region='Prague' AND year='1998' GROUP BY district_name";
        
        boolean isValid = validateIncomingExpression(incomingExpression);
        
        assertTrue("Should be valid syntax", isValid);
    }
}