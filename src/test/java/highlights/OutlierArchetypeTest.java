package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.archetypes.outlier.OutlierArchetype;
import highlights.archetypes.outlier.ZScoreOutlierAlgorithm;
import highlights.instance.ElementaryHighlight;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.Score;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.ScoreType;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * Verifies the outlier archetype is evaluated over <em>every</em> query measure: a two-measure result in
 * which the outlier sits in a different cell per measure must yield one holding holistic per measure, each
 * flagging its own cell.
 */
public class OutlierArchetypeTest {

    // Measure 0 (Revenue) spikes on the last cell; measure 1 (Units) spikes on the fourth. Both |z| > 2.2.
    private static final double[] REVENUE = {100, 105, 98, 102, 96, 95, 103, 400};
    private static final double[] UNITS   = { 50,  52, 48, 900, 51, 49,  53,  50};

    @Test
    public void flagsOutliersPerMeasure() {
        Result data = new Result();
        for (int i = 0; i < REVENUE.length; i++) {
            String[] row = {"region" + i, Double.toString(REVENUE[i]), Double.toString(UNITS[i]), "1"};
            data.getCells().add(new Cell(row, 2));
        }

        CubeQuery query = new CubeQuery("outlierTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "revenue", "revenue");
        query.addQueryMeasure("sum", "units", "units");

        LabeledResult operatorResult = new LabeledResult(query, data, null);
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(OutlierArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);

        // One holistic per measure, both holding, each flagging exactly one cell.
        List<HolisticHighlight> holistics = new ArrayList<>();
        for (Highlight h : highlights.highlights()) holistics.add((HolisticHighlight) h);
        assertEquals("one outlier holistic per query measure", 2, holistics.size());

        List<Double> flaggedValues = new ArrayList<>();
        for (HolisticHighlight holistic : holistics) {
            assertTrue("holistic must hold", holistic.execution.result.verdict());
            assertEquals("exactly one outlier cell", 1, holistic.elementary().size());

            ElementaryHighlight outlier = holistic.elementary().get(0);
            flaggedValues.add(outlier.measureValue.value);
            assertTrue("z-score above threshold", zScoreOf(outlier) > 2.2);

            // Renders without an NPE even though this test's schema leaves mainMeasure unresolved (null).
            assertNotNull("holistic renders", holistic.toText());
            assertNotNull("elementary renders", outlier.toText());
        }

        // Measure 0 flags the 400 cell (its Revenue outlier); measure 1 flags the 900 cell (its Units
        // outlier) — different cells, proving each measure is evaluated on its own distribution.
        assertTrue("Revenue outlier (400) surfaced", flaggedValues.contains(400.0));
        assertTrue("Units outlier (900) surfaced", flaggedValues.contains(900.0));
    }

    private static double zScoreOf(ElementaryHighlight outlier) {
        ScoreType zscore = ZScoreOutlierAlgorithm.ZSCORE;
        for (Score score : outlier.getScores()) {
            if (score.type == zscore) return Math.abs(score.value);
        }
        throw new AssertionError("no z-score on outlier elementary");
    }
}
