package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.archetypes.topk.TopKContributorsArchetype;
import highlights.instance.ElementaryHighlight;
import highlights.instance.HolisticHighlight;
import highlights.metamodel.ArchetypeProperty;
import labeling.LabeledResult;
import result.Cell;
import result.Result;

/**
 * Top-k surfaces the k largest contributors of a breakdown dimension, ranked by value: with default k=3
 * over five members it reports the three biggest in descending order and omits the two smallest.
 */
public class TopKContributorsTest {

    // member -> contribution; deliberately out of order in the result to prove the algorithm ranks.
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
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(TopKContributorsArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals("one top-k holistic for the single measure", 1, highlights.size());

        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        List<Double> reported = new ArrayList<>();
        for (ElementaryHighlight e : holistic.elementary()) reported.add(e.measureValue.value);

        // Exactly the three largest, in descending value order.
        assertEquals("k largest contributors", java.util.Arrays.asList(500.0, 300.0, 100.0), reported);
        assertFalse("50 is below the cut", reported.contains(50.0));
        assertFalse("10 is below the cut", reported.contains(10.0));
    }
}
