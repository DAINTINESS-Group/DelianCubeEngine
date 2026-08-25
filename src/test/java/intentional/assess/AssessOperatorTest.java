package intentional.assess;

import cubemanager.CubeManager;
import highlights.HighlightTestSupport;
import highlights.HighlightSet;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import intentional.labeling.Labeling;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
import intentional.result.LabeledResult;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import result.Cell;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    public void holisticTransformNormalizesTheComparisonColumn() throws RecognitionException {
        String query = "with loan for year = '1997' by district_name, year assess sum(amount) " +
                "against year = '1996'\n" +
                "using minMaxNorm(difference(amount, benchmark.amount))\n" +
                "labels {[0, 0.5): low, [0.5, 1]: high}";

        Labeling labeling = assess(query);
        assertFalse("expected matched cells", labeling.assignment().isEmpty());

        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (Cell cell : labeling.assignment().keySet()) {
            double magnitude = labeling.magnitudeOf(cell);
            assertTrue("normalized magnitude outside [0,1]: " + magnitude,
                    magnitude >= 0.0 && magnitude <= 1.0);
            assertEquals(magnitude < 0.5 ? "low" : "high", labeling.of(cell));
            min = Math.min(min, magnitude);
            max = Math.max(max, magnitude);
        }
        assertEquals("the column's smallest difference maps to zero", 0.0, min, 1e-9);
        assertEquals("the column's largest difference maps to one", 1.0, max, 1e-9);
    }

    @Test
    public void operandTransformsNormalizeEachPopulationSeparately() throws RecognitionException {
        String query = "with loan for year = '1997' by district_name, year assess sum(amount) " +
                "against year = '1996'\n" +
                "using difference(zscore(amount), zscore(benchmark.amount))\n" +
                "labels {[-inf, 0): fell, [0, +inf]: rose}";

        Labeling labeling = assess(query);
        assertFalse("expected matched cells", labeling.assignment().isEmpty());

        double sum = 0.0;
        double maxAbsolute = 0.0;
        for (Cell cell : labeling.assignment().keySet()) {
            double magnitude = labeling.magnitudeOf(cell);
            assertEquals(magnitude < 0 ? "fell" : "rose", labeling.of(cell));
            sum += magnitude;
            maxAbsolute = Math.max(maxAbsolute, Math.abs(magnitude));
        }
        assertEquals("both populations' z-scores each sum to zero", 0.0, sum, 1e-6);
        assertTrue("expected real spread between the standings", maxAbsolute > 0.0);
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
    public void benchmarklessAssessLabelsTheRawMeasure() throws RecognitionException {
        String query = "with loan for region = 'south Bohemia' by district_name, region\n" +
                "assess sum(amount)\n" +
                "labels {[0, 500000): small, [500000, +inf): big} AS size, EquiDepth(small, big)";

        LabeledResult result = new AssessOperator(cubeManager).execute(query).get(0);

        assertEquals("one labeling per scheme", 2, result.labelings().size() - consensuses(result).size());
        assertEquals("size", result.labelings().get(0).schemeName());
        assertEquals("the shared domain yields one consensus", 1, consensuses(result).size());

        Labeling labeling = result.labelings().get(0);
        Cell first = labeling.assignment().keySet().iterator().next();
        assertEquals("with no benchmark, the labeled quantity is the raw measure",
                first.toDouble(), labeling.magnitudeOf(first), 0.0001);
    }

    @Test
    public void derivedMeasureAssessLabelsTheExpression() throws RecognitionException {
        String query = "with loan for region = 'south Bohemia' by district_name, region\n" +
                "assess sum(amount) - sum(payments) AS Profit\n" +
                "labels {[-inf, 100000): thin, [100000, +inf): fat} AS margin, EquiDepth(thin, fat)";

        LabeledResult result = new AssessOperator(cubeManager).execute(query).get(0);

        assertEquals("Profit", result.query.getQueryMeasures().get(0).getAlias());
        assertEquals("one labeling per scheme", 2, result.labelings().size() - consensuses(result).size());
        assertEquals(1, consensuses(result).size());

        Labeling labeling = result.labelings().get(0);
        Cell first = labeling.assignment().keySet().iterator().next();
        assertEquals("the labeled quantity is the derived measure",
                first.toDouble(), labeling.magnitudeOf(first), 0.0001);
    }

    @Test
    public void multipleBenchmarksAssessUnderOneScheme() throws RecognitionException {
        String query = "with loan for year = '1997' by district_name, year\n" +
                "assess sum(amount) AS total\n" +
                "against year = '1996' using difference(zscore(total), zscore(benchmark.total)),\n" +
                "        PAST 2 using ratio(total, benchmark.total),\n" +
                "        500000\n" +
                "labels EquiDepth(low, mid, high)";

        LabeledResult result = new AssessOperator(cubeManager).execute(query).get(0);

        List<String> benchmarkTags = new ArrayList<>();
        for (ModelResult model : result.models()) {
            if (model.origin() == ModelOrigin.OPERATOR) {
                benchmarkTags.add(model.parameters().get(0).label);
            }
        }
        assertEquals(Arrays.asList("year = '1996'", "past 2", "constant 500000"), benchmarkTags);

        List<Labeling> labelings = result.labelings();
        assertEquals("three comparisons and their cross-benchmark consensus", 4, labelings.size());
        assertEquals(1, consensuses(result).size());
        assertEquals("Consensus(EquiDepth)", consensuses(result).get(0).schemeName());

        Labeling standingVsSibling = labelings.get(0);
        Labeling ratioVsPast = labelings.get(1);
        Labeling rawVsConstant = labelings.get(2);
        assertFalse(standingVsSibling.assignment().isEmpty());
        assertFalse(ratioVsPast.assignment().isEmpty());
        assertFalse(rawVsConstant.assignment().isEmpty());

        double zSum = 0.0;
        for (Cell cell : standingVsSibling.assignment().keySet()) {
            zSum += standingVsSibling.magnitudeOf(cell);
        }
        assertEquals("each side's z-scores sum to zero over the matched cells", 0.0, zSum, 1e-6);

        for (Cell cell : ratioVsPast.assignment().keySet()) {
            assertTrue("a ratio against past sums is positive", ratioVsPast.magnitudeOf(cell) > 0.0);
            assertTrue("the past reference is a real sum", ratioVsPast.referenceOf(cell) > 0.0);
        }

        for (Cell cell : rawVsConstant.assignment().keySet()) {
            assertEquals("an entry without USING compares raw values",
                    cell.toDouble(), rawVsConstant.magnitudeOf(cell), 0.0001);
            assertEquals("the constant benchmark is every cell's reference",
                    500000.0, rawVsConstant.referenceOf(cell), 0.0001);
        }
    }

    @Test
    public void multipleBenchmarksRejectMultipleSchemes() {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for year = '1997' by district_name, year\n" +
                "assess sum(amount) AS total\n" +
                "against year = '1996' using ratio(total, benchmark.total), 500000\n" +
                "labels EquiDepth(low, high), EquiWidth(low, high)";

        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> operator.execute(query));
        assertTrue(error.getMessage().contains("single labeling scheme"));
    }

    @Test
    public void constantDeltaOperandDividesByTheConstant() throws RecognitionException {
        int divisor = 20000;
        String query = "with loan for region = 'central Bohemia' " +
                "by month, region assess max(amount) against 10000\n" +
                "using ratio(amount, " + divisor + ")\n" +
                "labels {[0.0, 0.5]: low, (0.5, +inf]: high}";

        Labeling labeling = assess(query);
        for (Map.Entry<Cell, String> labeled : labeling.assignment().entrySet()) {
            double expectedRatio = labeled.getKey().toDouble() / divisor;
            assertEquals("the delta divides by the written constant, not the AGAINST value",
                    expectedRatio, labeling.magnitudeOf(labeled.getKey()), 0.0001);
            assertEquals(expectedRatio > 0.5 ? "high" : "low", labeled.getValue());
        }
    }

    @Test
    public void unresolvableDeltaOperandFailsLoudly() {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for region = 'central Bohemia' " +
                "by month, region assess max(amount) against 10000\n" +
                "using ratio(duration, benchmark.duration)\n" +
                "labels {[0.0, 0.5]: low, (0.5, +inf]: high}";

        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> operator.execute(query));
        assertTrue(error.getMessage().contains("does not resolve"));
    }

    @Test
    public void parenthesizedTargetMeasureWithConstantParses() throws RecognitionException {
        String query = "with loan for region = 'south Bohemia' by district_name, region\n" +
                "assess (sum(amount) - sum(payments)) / 1000 AS ProfitK\n" +
                "labels {[-inf, 100): thin, [100, +inf): fat}";

        LabeledResult result = new AssessOperator(cubeManager).execute(query).get(0);

        assertEquals("ProfitK", result.query.getQueryMeasures().get(0).getAlias());
        Labeling labeling = result.labelings().get(0);
        assertFalse(labeling.assignment().isEmpty());
    }

    @Test
    public void derivedMeasureAssessAgainstPastBenchmark() throws RecognitionException {
        String query = "with loan for month = '11/1997', region = 'south Moravia' by month, region\n" +
                "AssEsS max(amount) - max(payments) AS Spread agAinSt PaST 5\n" +
                "using ratio(Spread, benchmark.Spread)\n" +
                "labels {[0.0, 1]: shrunk, (1, +inf): grown}";

        Labeling labeling = assess(query);
        assertFalse("the benchmark carries the derived measure too", labeling.assignment().isEmpty());
        Cell first = labeling.assignment().keySet().iterator().next();
        assertFalse("a matched benchmark value rides as the reference",
                Double.isNaN(labeling.referenceOf(first)));
    }

    @Test
    public void multiSchemeLabelsProduceLabelingsAndAConsensus() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "WITH loan\n" +
                "FOR year = '1997'\n" +
                "BY region, year, status\n" +
                "ASSESS sum(amount)\n" +
                "AGAINST PAST 2\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.001, 0.05]: low, (0.05, 0.1]: high, (0.1, +inf): ultra} AS analyst,\n" +
                "       EquiDepth(low, high, ultra),\n" +
                "       EquiWidth(low, high, ultra)";

        LabeledResult result = operator.execute(query).get(0);

        assertEquals("one labeling per scheme", 3, result.labelings().size() - consensuses(result).size());
        assertEquals("analyst", result.labelings().get(0).schemeName());
        assertEquals("EquiDepth", result.labelings().get(1).schemeName());
        assertEquals("EquiWidth", result.labelings().get(2).schemeName());
        assertEquals("the shared domain yields one consensus", 1, consensuses(result).size());
        assertEquals("Consensus(analyst,EquiDepth,EquiWidth)", consensuses(result).get(0).schemeName());
    }

    @Test
    public void executeProducesHighlights() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "WITH loan\n" +
                "FOR year = '1997'\n" +
                "BY region, year, status\n" +
                "ASSESS sum(amount) AS total\n" +
                "AGAINST PAST 2\n" +
                "USING ratio(absolute(total, benchmark.total))\n" +
                "LABELS {[0.001, 0.05]: low, (0.05, 0.1]: high, (0.1, +inf): ultra}\n" +
                "SAVE AS PastBenchmarkHighlightsTest";

        LabeledResult result = operator.execute(query).get(0);
        HighlightSet highlights = HighlightTestSupport.highlights(result, IntentionalProfile.ASSESS.archetypes(), cubeManager);

        HolisticHighlight labelPredominance = holisticFor(highlights, "LabelPredominance");
        HolisticHighlight megaContributor = holisticFor(highlights, "MegaContributor");

        assertNotNull("expected a LabelPredominance highlight", labelPredominance);
        assertNotNull("expected a MegaContributor highlight (sum is additive)", megaContributor);
        assertTrue("LabelPredominance should hold", labelPredominance.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributor.modelResult.verdict());

        // the dominant member is surfaced as an elementary highlight, above the 0.5 dominance threshold
        assertFalse(megaContributor.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",
                megaContributor.modelResult.holisticMagnitude() > 0.5);
    }

    private static HolisticHighlight holisticFor(HighlightSet highlights, String archetypeName) {
        for (Highlight h : highlights.highlights()) {
            if (h instanceof HolisticHighlight
                    && ((HolisticHighlight) h).archetypeName.equals(archetypeName)) {
                return (HolisticHighlight) h;
            }
        }
        return null;
    }

    private static List<Labeling> consensuses(LabeledResult result) {
        List<Labeling> out = new ArrayList<>();
        for (Labeling labeling : result.labelings()) {
            if (labeling.schemeName().startsWith("Consensus")) out.add(labeling);
        }
        return out;
    }
}
