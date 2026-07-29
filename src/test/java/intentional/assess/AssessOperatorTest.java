package intentional.assess;

import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.Score;
import intentional.labeling.Labeling;
import intentional.result.LabeledResult;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import result.Cell;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    /** The assessment labeling of the query's result. */
    private Labeling assess(String query) throws RecognitionException {
        return new AssessOperator(cubeManager).execute(query).get(0).labelings().get(0);
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
        String query = "with loan for month = '02/1998', region = 'South Moravia'\n" +
                "by month, region assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        Labeling labeling = assess(query);
        assertEquals("mid_effort", labeling.assignment().values().iterator().next());
    }

    @Test
    public void assessSiblingsWithMultipleCells() throws RecognitionException {
        String query = "with loan for region = 'South Moravia', year = '1994'\n" +
                "by status, region assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        Labeling labeling = assess(query);
        for (Cell cell : labeling.assignment().keySet()) {
            System.out.println(cell.toString(", "));
        }
    }

    @Test
    public void runComplexQueryAgainstConstantBenchmark() throws RecognitionException {
        int constantBenchmark = 10000;
        String query = "with loan for region = 'central Bohemia' " +
                "by month, region AssEsS max(amount) against " + constantBenchmark + "\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, +inf]: high}";

        Labeling labeling = assess(query);
        for (Map.Entry<Cell, String> labeled : labeling.assignment().entrySet()) {
            if (labeled.getKey().toDouble() > constantBenchmark) {
                assertEquals("high", labeled.getValue());
            } else {
                assertEquals("low", labeled.getValue());
            }
        }
    }

    @Test
    public void assessSiblingsWithMissMatchingCells() throws RecognitionException {
        String query = "with loan for region = 'South Moravia', year = '1994'\n" +
                "by month, region, status assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low, [0.3, 0.6): mid, [0.6, 1]: high, (1, +inf): perfect}";

        Labeling labeling = assess(query);
        for (Map.Entry<Cell, String> labeled : labeling.assignment().entrySet()) {
            if (labeled.getKey().toDouble() == 4980.0) {
                assertEquals("low", labeled.getValue());
            }
        }
    }

    @Test
    public void executeQueryWithPastBenchmark() throws RecognitionException {
        String query = "with loan for month = '11/1997', region = 'south Moravia' by month, " +
                "region AssEsS max(amount) agAinSt PaST 5\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high}";

        Labeling labeling = assess(query);
        assertEquals("low", labeling.assignment().values().iterator().next());
    }

    @Test
    public void executeQueryWithPastBenchmarkMissingEntries() throws RecognitionException {
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "region AssEsS max(amount) against PaST 20\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA}";

        Labeling labeling = assess(query);
        assertEquals("ULTRA", labeling.assignment().values().iterator().next());
    }

    @Test
    public void handleBenchmarkResultsCellNumberVaryingCase() throws RecognitionException {
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "status asseSs max(amount) against PaST 20\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA}";
        double magicNumber = 465504.0;
        boolean assertionCompleted = false;

        Labeling labeling = assess(query);
        labeling.assignment().keySet().stream().map(Cell::toDouble).forEach(System.out::println);

        for (Map.Entry<Cell, String> labeled : labeling.assignment().entrySet()) {
            if (labeled.getKey().toDouble() == magicNumber) {
                assertEquals("ULTRA", labeled.getValue());
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

        LabeledResult result = operator.execute(query).get(0);
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
}
