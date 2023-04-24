package assess;

import assess.utils.LabeledCell;
import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * <h2>Author Note</h2>
 * <p>The end result of the labeling process is a list of LabeledCells. This was used
 * as a kind of a "hack" instead of using a HashMap of Cell to String (the label)
 * which would require a hashcode method in the Cell class.</p> <p>It is advised to
 * actually implement the originally thought solution and getting rid of this
 * "hack", but time is always of the essence, so do with it as you please.</p>
 */

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

        List<LabeledCell> results = operator.execute(query);
        assertEquals("mid_effort", results.get(0).label);
    }

    @Test
    public void assessSiblingsWithMultipleCells() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for region = 'South Moravia', year = '1994'\n" +
                "by status assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        double magicNumber = 101448.0; // A known cell value, with a known label
        String expected = "low_effort";

        List<LabeledCell> results = operator.execute(query);

        boolean assertionComplete = false;
        for (LabeledCell labeledCell : results) {
            if (labeledCell.cell.toDouble() == magicNumber) {
                assertEquals(expected, labeledCell.label);
                assertionComplete = true;
            }
        }
        if (!assertionComplete) {
            fail();
        }
    }

    @Test
    public void runComplexQueryAgainstConstantBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        int constantBenchmark = 10000;
        String query = "with loan for region = 'central Bohemia' by month, region AssEsS max(amount) against " + constantBenchmark + "\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, +inf]: high}";

        List<LabeledCell> results = operator.execute(query);

        for (LabeledCell labeledCell : results) {
            if (labeledCell.cell.toDouble() > constantBenchmark) {
                assertEquals("high", labeledCell.label);
            } else {
                assertEquals("low", labeledCell.label);
            }
        }
    }

    @Test
    public void assessSiblingsWithMissMatchingCells() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for region = 'South Moravia', year = '1994'\n" +
                "by month assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        List<LabeledCell> results = operator.execute(query);
        boolean[] comparisonsMade = {false, false};

        for (LabeledCell labeledCell : results) {
            if (labeledCell.cell.toDouble() == 91776.0) {
                assertEquals("low_effort", labeledCell.label);
                comparisonsMade[0] = true;
            } else if (labeledCell.cell.toDouble() == 172936.0) {
               assertEquals("mid_effort", labeledCell.label);
               comparisonsMade[1] = true;
            }
        }

        if (!comparisonsMade[0] || !comparisonsMade[1]) {
            fail();
        }
    }

    @Test
    public void executeQueryWithPastBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '11/1997', region = 'south Moravia' by month, " +
                "region AssEsS max(amount) agAinSt PaST 5\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high}";

        List<LabeledCell> results = operator.execute(query);
        assertEquals("low", results.get(0).label);
    }

    @Test
    public void executeQueryWithPastBenchmarkMissingEntries() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "region AssEsS max(amount) against PaST 20\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA}";

        List<LabeledCell> results = operator.execute(query);
        assertEquals("ULTRA", results.get(0).label);
    }

    @Test
    public void handleBenchmarkResultsCellNumberVaryingCase() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "status asseSs max(amount) against PaST 20\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA}";
        double magicNumber = 465504.0;
        boolean assertionCompleted = false;

        List<LabeledCell> results = operator.execute(query);
        results.stream().map(labeledCell -> labeledCell.cell.toDouble()).forEach(System.out::println);

        for (LabeledCell labeledCell : results) {
            if (labeledCell.cell.toDouble() == magicNumber) {
                assertEquals("ULTRA", labeledCell.label);
                assertionCompleted = true;
            }
        }
        if (!assertionCompleted) {
            fail();
        }
    }
}
