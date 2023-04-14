package assess;

import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AssessOperatorTest {
    private final CubeManager cubeManager = initializeCubeManager();

    // Dimensions in Loan Cube: account, date and status
    private CubeManager initializeCubeManager() {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "pkdd99_star");
        userInputList.put("username", "CinecubesUser");
        userInputList.put("password", "Cinecubes");
        userInputList.put("cubeName", "loan");
        userInputList.put("inputFolder", "pkdd99_star");
        CubeManager cubeManager = new CubeManager(typeOfConnection, userInputList);
        Session session = new Session(cubeManager);
        try {
            session.initialize(typeOfConnection, userInputList);
        } catch (RemoteException re) {
            System.exit(0);
        }
        return cubeManager;
    }

    @Test
    public void executeIncompleteQuery() {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '2019-05', region = 'south Moravia'\n" +
                "by region, month assess avg(amount) against region = 'north Moravia'";

        RuntimeException actualException =
                assertThrows(RuntimeException.class, () -> operator.execute(query));

        assertEquals("Invalid Syntax", actualException.getMessage());
    }

    @Test
    public void executeSiblingQuery() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '02/1998', region = 'South Moravia'\n" +
                "by month assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        HashMap<Double, String> results = operator.execute(query);
        Double magicNumber = 0.34126033882443607; // This is the key-value of the comparison
        assertEquals("mid_effort", results.get(magicNumber));
    }

    @Test
    public void assessSiblingsWithMultipleCells() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for region = 'South Moravia', year = '1994'\n" +
                "by status assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        HashMap<Double, String> results = operator.execute(query);
        assertEquals("mid_effort", results.get(0.3080660835762877));
        assertEquals("low_effort", results.get(0.07408453052242985));
    }

    @Test
    public void runComplexQueryAgainstConstantBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan by month, region AssEsS max(amount) against 100000\n"+
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high}";

        HashMap<Double, String> results = operator.execute(query);
        assertEquals("low", results.get(0.07656));
    }

    @Test
    public void assessSiblingsWithMissMatchingCells() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for region = 'South Moravia', year = '1994'\n" +
                "by month assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        HashMap<Double, String> results = operator.execute(query);
        assertEquals("mid_effort", results.get(0.3080660835762877));
        assertEquals("low_effort", results.get(0.07408453052242985));
    }
    @Test
    public void executeQueryWithPastBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '11/1997', region = 'south Moravia' by month, " +
                "region AssEsS max(amount) agAinSt PaST 5\n"+
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high}";

        HashMap<Double, String> results = operator.execute(query);
        assertEquals("low", results.get(0.14454832377089186));
    }

    @Test
    public void executeQueryWithPastBenchmarkMissingEntries() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "region AssEsS max(amount) against PaST 20\n"+
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA}";

        HashMap<Double, String> results = operator.execute(query);
        assertEquals("ULTRA", results.get(1.8861714207264229));
    }

    @Test
    public void executeQueryWithPastBenchmarkMismatchingCells() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "status AssEsS max(amount) against PaST 20\n"+
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA}";

        // This fails as the number of cells of each month is not the same.
        HashMap<Double, String> results = operator.execute(query);
        assertEquals("ULTRA", results.get(1.8861714207264229));
    }
}
