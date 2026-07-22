package describe.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.archetypes.labelpredominance.LabelPredominanceArchetype;
import highlights.instance.HolisticHighlight;
import highlights.metamodel.ArchetypeProperty;
import result.Cell;
import result.Result;
import intentionaloperator.OperatorResult;
import labeling.LabelingModel;

/**
 * The clustering model groups cells by measure value into an unordered cluster labeling, and the
 * label-predominance archetype consumes it to test whether one cluster predominates.
 */
public class KMeansLabelingModelTest {

    @Test
    public void clustersCellsAndPredominanceHolds() {
        Result data = new Result();
        String[][] rows = {
                {"r0", "1", "1"}, {"r1", "2", "1"}, {"r2", "1", "1"},
                {"r3", "2", "1"}, {"r4", "100", "1"}, {"r5", "1000", "1"},
        };
        for (String[] row : rows) data.getCells().add(new Cell(row, 1));

        KMeansLabelingModel model = new KMeansLabelingModel(data);
        assertEquals(0, model.compute());

        CubeQuery query = new CubeQuery("clusterTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        OperatorResult operatorResult =
                new OperatorResult(query, data, Collections.<LabelingModel>singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals(1, highlights.size());

        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        assertTrue("the four low-valued cells form a predominant cluster (4 of 6)",
                holistic.execution.result.verdict());
        assertTrue("dominant cluster label is reported", holistic.getScores().stream()
                .anyMatch(s -> s.label != null && s.label.startsWith("Cluster")));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
    }
}
