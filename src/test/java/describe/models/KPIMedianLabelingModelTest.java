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
import intentional.result.LabeledResult;
import intentional.result.LabelingModel;
import result.Cell;
import result.Result;

/**
 * The KPI model labels each cell Low/OK/High by its distance from the median, and the label-predominance
 * archetype consumes that ordered labeling to produce highlights.
 */
public class KPIMedianLabelingModelTest {

    @Test
    public void labelsCellsByMedianAndPredominanceHolds() {
        Result data = new Result();
        String[][] rows = {
                {"r0", "10", "1"}, {"r1", "10", "1"}, {"r2", "10", "1"},
                {"r3", "10", "1"}, {"r4", "100", "1"}, {"r5", "1", "1"},
        };
        for (String[] row : rows) data.getCells().add(new Cell(row, 1));

        KPIMedianLabelingModel model = new KPIMedianLabelingModel(data);
        assertEquals(0, model.compute());

        CubeQuery query = new CubeQuery("kpiTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult =
                new LabeledResult(query, data, Collections.<LabelingModel>singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals(1, highlights.size());

        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        assertTrue("OK predominates (4 of 6)", holistic.execution.result.verdict());
        assertTrue("dominant label OK is reported", holistic.getScores().stream()
                .anyMatch(s -> "OK".equals(s.label)));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
    }
}
