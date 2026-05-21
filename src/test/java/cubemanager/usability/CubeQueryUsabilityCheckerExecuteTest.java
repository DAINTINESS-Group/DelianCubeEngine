package cubemanager.usability;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import mainengine.Session;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import result.Cell;
import result.Result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * test that verifies {@link CubeQueryUsabilityChecker#executeCubeQueryWithUsability}
 * produces the same result as a direct DB execution of q^n
 *
 * <p>
 * <b>Prerequisites:</b>
 * <ul>
 * <li>MySQL server running at 127.0.0.1:3306</li>
 * <li>Schema pkdd99_star loaded with the loan data</li>
 * <li>Credentials: user CinecubesUser/ password Cinecubes</li>
 * </ul>
 *
 * <p>
 * Queries used (loanQueries_Usability.txt as a reference, but we don't need the .txt to exist):
 *
 * <pre>
 *   q^b (LoanQuery21_S2_CG-Cmmn):
 *       Gamma: account_dim.district_name, date_dim.month
 *       Sigma: account_dim.region='Prague', date_dim.year='1998'
 *       Measure: Min(amount)
 *
 *   q^n (LoanQuery21_S2_CG-Cmmn2):
 *       Gamma: account_dim.district_name, date_dim.year
 *       Sigma: account_dim.district_name='Hl.m. Praha', date_dim.year='1998'
 *       Measure: Min(amount)
 * </pre>
 *
 * <p>
 * Both queries satisfy all six conditions of Theorem 9.1 (cube usability),
 * so the usability must yield the same result as the direct DB execution
 */
public class CubeQueryUsabilityCheckerExecuteTest {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/pkdd99_star?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "CinecubesUser";
    private static final String DB_PASSWORD = "Cinecubes";
    private static final String SCHEMA = "pkdd99_star";
    private static final String CUBE_NAME = "loan";
    private static final String INPUT_FOLDER = "pkdd99_star";

    private static CubeManager cubeManager;


    //q^b query
    private static final String QB_STRING = "CubeName:loan\n" +
            "Name: LoanQuery21_S2_CG-Cmmn\n" +
            "AggrFunc:Min\n" +
            "Measure:amount\n" +
            "Gamma:account_dim.district_name,date_dim.month\n" +
            "Sigma:account_dim.region='Prague', date_dim.year='1998'";


    // q^n query
    private static final String QN_STRING = "CubeName:loan\n" +
            "Name: LoanQuery21_S2_CG-Cmmn2\n" +
            "AggrFunc:Min\n" +
            "Measure:amount\n" +
            "Gamma:account_dim.district_name,date_dim.year\n" +
            "Sigma:account_dim.district_name='Hl.m. Praha', date_dim.year='1998'";


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Skip the entire class if the DB is not reachable
        boolean dbAvailable = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            c.close();
            dbAvailable = true;
        } catch (Exception e) {
            System.out.println(
                    "[Integration] DB not available – skipping usability integration tests. (" + e.getMessage() + ")");
        }
        Assume.assumeTrue("MySQL database not available; skipping integration tests", dbAvailable);

        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", SCHEMA);
        userInputList.put("username", DB_USER);
        userInputList.put("password", DB_PASSWORD);
        userInputList.put("cubeName", CUBE_NAME);
        userInputList.put("inputFolder", INPUT_FOLDER);

        cubeManager = new CubeManager("RDBMS", userInputList);
        Session session = new Session(cubeManager);
        session.initialize("RDBMS", userInputList);
    }


    /**
     * <ol>
     * <li>Execute q^b against the DB</li>
     * <li>Execute q^n against the DB</li>
     * <li>Run executeCubeQueryWithUsability(q^n) using q^b's result</li>
     * <li>Assert usability result == db result - cell by cell</li>
     * </ol>
     */
    @Test
    public void testUsabilityResultMatchesDirectDBResult() throws Exception {
        HashMap<String, String> queryParams = new HashMap<>();

        //Step 1: execute q^b in DB
        CubeQuery qb = cubeManager.createCubeQueryFromString(QB_STRING, queryParams);
        Result qbResult = cubeManager.executeQuery(qb);
        assertNotNull("q^b DB result must not be null", qbResult);
        assertFalse("q^b DB result must have at least one cell", qbResult.getCells().isEmpty());

        //Step 2: execute q^n in DB
        CubeQuery qn = cubeManager.createCubeQueryFromString(QN_STRING, queryParams);
        Result dbResult = cubeManager.executeQuery(qn);
        assertNotNull("q^n DB result must not be null", dbResult);

        //Step 3: execute q^n with usability
        CubeQuery qnUsability = cubeManager.createCubeQueryFromString(QN_STRING, queryParams);

        CubeQueryUsabilityChecker.resetInstance();
        CubeQueryUsabilityChecker checker = CubeQueryUsabilityChecker.getInstance();
        checker.setQueries(qnUsability, qb, cubeManager.getCubeBase());

        assertTrue(checker.executeCubeQueryWithUsability(qnUsability));

        Result usabilityResult = checker.getComputedResult();
        assertNotNull("computedResult must be populated after usability execution", usabilityResult);

        //Step 4: compare the two results cell by cell
        assertResultsMatch(dbResult, usabilityResult);
    }


    /**
     * Asserts that db result contains exactly the same cells as ones from usability
     */
    private void assertResultsMatch(Result expected, Result actual) {
        assertNotNull("actual result must not be null", actual);

        ArrayList<Cell> expectedCells = expected.getCells();
        ArrayList<Cell> actualCells = actual.getCells();

        assertEquals(
                "Number of result cells must match between DB and usability execution",
                expectedCells.size(), actualCells.size());

        //Index actual cells by dimension key
        java.util.Map<String, Cell> actualByKey = new java.util.HashMap<>();
        for (Cell c : actualCells) {
            actualByKey.put(c.getDimensionMembers().toString(), c);
        }

        for (Cell exp : expectedCells) {
            String key = exp.getDimensionMembers().toString();
            Cell act = actualByKey.get(key);
            assertNotNull(
                    "Cell with dimension key " + key + " present in db result but missing in usability result",
                    act);

            ArrayList<String> expMeasures = exp.getMeasures();
            ArrayList<String> actMeasures = act.getMeasures();
            assertEquals("Measure count must match for key " + key,
                    expMeasures.size(), actMeasures.size());
            for (int m = 0; m < expMeasures.size(); m++) {
                assertEquals(expMeasures.get(m), actMeasures.get(m));
            }
        }
    }
}
