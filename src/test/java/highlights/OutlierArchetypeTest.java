package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cubemanager.cubebase.CubeQuery;
import intentional.labeling.Labeling;
import intentional.model.ModelResult;
import intentional.model.archetypes.DefaultArchetypes;
import intentional.model.archetypes.outlier.ZScoreOutlierAlgorithm;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * The outlier model runs over every query measure: a two-measure result whose outlier sits in a different
 * cell per measure yields one holding result per measure, each flagging its own cell.
 */
public class OutlierArchetypeTest {

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
        List<ModelResult> results = HighlightTestSupport.models(operatorResult, DefaultArchetypes.subset("Outlier"));
        assertEquals("one outlier result per query measure", 2, results.size());

        List<Double> flaggedValues = new ArrayList<>();
        for (ModelResult m : results) {
            assertTrue("result must hold", m.verdict());
            Labeling labelling = m.labelling();
            List<Cell> outliers = new ArrayList<>();
            for (Map.Entry<Cell, String> e : labelling.assignment().entrySet()) {
                if (ZScoreOutlierAlgorithm.OUTLIER.equals(e.getValue())) outliers.add(e.getKey());
            }
            assertEquals("exactly one outlier cell", 1, outliers.size());
            Cell outlier = outliers.get(0);
            assertTrue("z-score above threshold", Math.abs(labelling.magnitudeOf(outlier)) > 2.2);
            flaggedValues.add(outlier.toDouble(labelling.measureIndex()));
        }

        assertTrue("Revenue outlier (400) surfaced", flaggedValues.contains(400.0));
        assertTrue("Units outlier (900) surfaced", flaggedValues.contains(900.0));
    }
}
