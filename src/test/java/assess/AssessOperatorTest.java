package assess;

import assess.models.AssessModel;
import assess.utils.LabeledCell;
import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.Score;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import intentionaloperator.OperatorResult;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

        assertEquals("Invalid Query Syntax", actualException.getMessage());
    }

    @Test
    public void executeSiblingQuery() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '02/1998', region = 'South Moravia'\n" +
                "by month, region assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        List<LabeledCell> labeledCells = ((AssessModel) operator.execute(query).get(0).model(AssessModel.NAME)).getLabeledCells();
        assertEquals("mid_effort", labeledCells.get(0).label);
    }

    @Test
    public void assessSiblingsWithMultipleCells() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for region = 'South Moravia', year = '1994'\n" +
                "by status, region assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        List<LabeledCell> labeledCells = ((AssessModel) operator.execute(query).get(0).model(AssessModel.NAME)).getLabeledCells();
        for (LabeledCell labeledCell : labeledCells) {
            System.out.println(labeledCell.cell.toString(", "));
        }
    }

    @Test
    public void runComplexQueryAgainstConstantBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        int constantBenchmark = 10000;
        String query = "with loan for region = 'central Bohemia' " +
                "by month, region AssEsS max(amount) against " + constantBenchmark + "\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, +inf]: high}";

        List<LabeledCell> labeledCells = ((AssessModel) operator.execute(query).get(0).model(AssessModel.NAME)).getLabeledCells();

        for (LabeledCell labeledCell : labeledCells) {
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
                "by month, region, status assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low, [0.3, 0.6): mid, [0.6, 1]: high, (1, +inf): perfect}";

        List<LabeledCell> labeledCells = ((AssessModel) operator.execute(query).get(0).model(AssessModel.NAME)).getLabeledCells();

        for (LabeledCell labeledCell : labeledCells) {
            if (labeledCell.cell.toDouble() == 4980.0) {
                assertEquals("low", labeledCell.label);
            }
        }
    }

    @Test
    public void executeQueryWithPastBenchmark() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '11/1997', region = 'south Moravia' by month, " +
                "region AssEsS max(amount) agAinSt PaST 5\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high}";

        List<LabeledCell> labeledCells = ((AssessModel) operator.execute(query).get(0).model(AssessModel.NAME)).getLabeledCells();
        assertEquals("low", labeledCells.get(0).label);
    }

    @Test
    public void executeQueryWithPastBenchmarkMissingEntries() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "region AssEsS max(amount) against PaST 20\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA}";

        List<LabeledCell> labeledCells = ((AssessModel) operator.execute(query).get(0).model(AssessModel.NAME)).getLabeledCells();
        assertEquals("ULTRA", labeledCells.get(0).label);
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

        List<LabeledCell> labeledCells = ((AssessModel) operator.execute(query).get(0).model(AssessModel.NAME)).getLabeledCells();
        labeledCells.stream().map(labeledCell -> labeledCell.cell.toDouble()).forEach(System.out::println);

        for (LabeledCell labeledCell : labeledCells) {
            if (labeledCell.cell.toDouble() == magicNumber) {
                assertEquals("ULTRA", labeledCell.label);
                assertionCompleted = true;
            }
        }
        if (!assertionCompleted) {
            fail();
        }
    }

    @Test
    public void executeProducesHighlights() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "WITH loan\n" +
                "FOR year = '1997'\n" +
                "BY region, year, status\n" +
                "ASSESS sum(amount)\n" +
                "AGAINST PAST 2\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.001, 0.05]: low, (0.05, 0.1]: high, (0.1, +inf): ultra}\n" +
                "SAVE AS PastBenchmarkHighlightsTest";

        OperatorResult result = operator.execute(query).get(0);
        HighlightSet highlights = new HighlightExtractor()
                .extract(result, IntentionalProfile.ASSESS.archetypes(), CubeSchemaResolver.from(cubeManager));

        HolisticHighlight labelPredominance = holisticFor(highlights, "LabelPredominance");
        HolisticHighlight megaContributor = holisticFor(highlights, "MegaContributor");

        assertNotNull("expected a LabelPredominance highlight", labelPredominance);
        assertNotNull("expected a MegaContributor highlight (sum is additive)", megaContributor);
        assertTrue("LabelPredominance should hold", labelPredominance.execution.result.verdict());
        assertTrue("MegaContributor should hold", megaContributor.execution.result.verdict());

        // the dominant member is surfaced as an elementary highlight, above the 0.5 dominance threshold
        assertFalse(megaContributor.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",
                scoreOf(megaContributor, "ContributionShare") > 0.5);
    }

    private static HolisticHighlight holisticFor(HighlightSet highlights, String archetypeName) {
        for (Highlight h : highlights.highlights()) {
            if (h instanceof HolisticHighlight
                    && ((HolisticHighlight) h).archetype.name.equals(archetypeName)) {
                return (HolisticHighlight) h;
            }
        }
        return null;
    }

    private static double scoreOf(HolisticHighlight highlight, String scoreType) {
        for (Score score : highlight.getScores()) {
            if (score.type.name().equals(scoreType)) return score.value;
        }
        return Double.NaN;
    }

    /*
    @Test
    public void saveResultsInPredefinedOutputFile() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "status asseSs max(amount) against PaST 20\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA} " +
                "SAVE aS filename_test";
        operator.execute(query);
        File outputFile = new File("OutputFiles/assessments/filename_test.md");
        assertTrue(outputFile.exists());
    }*/
}
