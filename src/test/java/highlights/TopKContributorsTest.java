package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cubemanager.cubebase.CubeQuery;
import intentional.labeling.Labeling;
import intentional.model.ModelResult;
import intentional.model.archetypes.DefaultArchetypes;
import intentional.model.archetypes.topk.TopKContributionAlgorithm;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * Top-k labels the k largest contributors of a breakdown dimension {@code topContributor}: with default k=3
 * over five members it labels the three biggest and leaves the two smallest {@code other}.
 */
public class TopKContributorsTest {

    private static final String[] MEMBERS = {"c", "a", "e", "b", "d"};
    private static final double[] VALUES  = {100, 500,  10, 300,  50};

    @Test
    public void reportsTopThreeByValue() {
        Result data = new Result();
        for (int i = 0; i < MEMBERS.length; i++) {
            data.getCells().add(new Cell(new String[]{MEMBERS[i], Double.toString(VALUES[i]), "1"}, 1));
        }

        CubeQuery query = new CubeQuery("topKTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, data, null);
        List<ModelResult> results = HighlightTestSupport.models(
                operatorResult, DefaultArchetypes.subset("TopKContributors"));
        assertEquals("one top-k result for the single measure", 1, results.size());

        Labeling labelling = results.get(0).labelling();
        List<Double> reported = new ArrayList<>();
        for (Map.Entry<Cell, String> e : labelling.assignment().entrySet()) {
            if (TopKContributionAlgorithm.TOP_CONTRIBUTOR.equals(e.getValue())) {
                reported.add(e.getKey().toDouble(0));
            }
        }

        assertEquals("k largest contributors, in descending value order",
                java.util.Arrays.asList(500.0, 300.0, 100.0), reported);
        assertFalse("50 is below the cut", reported.contains(50.0));
        assertFalse("10 is below the cut", reported.contains(10.0));
    }
}
