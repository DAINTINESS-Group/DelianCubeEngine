package assess;

import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AssessOperatorTest {
    private final CubeManager cubeManager = initializeCubeManger();

    private CubeManager initializeCubeManger() {
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
    //TODO: This should return an exception that the query is invalid
    public void executeIncompleteQuery() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '2019-05', region = 'south Moravia'\n" +
                "by region, month assess avg(amount) against region = 'north Moravia'";

        operator.execute(query);
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
    public void runComplexQueryAgainstConstantBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan by month, region AssEsS max(amount) against 100000\n"+
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high}";

        HashMap<Double, String> results = operator.execute(query);
        assertEquals("low", results.get(0.07656));
    }

    @Test
    public void executeQueryWithPastBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '11/1997', region = 'south Moravia' by month, " +
                "region AssEsS max(amount) against PaST 5\n"+
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
}
