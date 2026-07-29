package intentional.labeling;

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
import intentional.labeling.LabelingModel;
import intentional.labeling.schemes.KMeansScheme;
import intentional.labeling.schemes.MedianDistanceScheme;
import result.Cell;
import result.Result;

/**
 * One model, any scheme: the model fits the scheme on a measure of the result and labels the cells, and
 * the label-predominance archetype consumes the labeling regardless of which scheme produced it.
 */
public class MeasureLabelingModelTest {

    private static Result sixCells(String... measures) {
        Result data = new Result();
        for (int i = 0; i < measures.length; i++) {
            data.getCells().add(new Cell(new String[]{"r" + i, measures[i], "1"}, 1));
        }
        return data;
    }

    private static HolisticHighlight extractSingle(Result data, LabelingModel model) {
        CubeQuery query = new CubeQuery("measureLabelingTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult =
                new LabeledResult(query, data, Collections.<LabelingModel>singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals(1, highlights.size());
        return (HolisticHighlight) highlights.highlights().get(0);
    }

    @Test
    public void medianSchemeLabelsCellsAndPredominanceHolds() {
        Result data = sixCells("10", "10", "10", "10", "100", "1");
        MeasureLabelingModel model = new MeasureLabelingModel(data, new MedianDistanceScheme());
        assertEquals(0, model.compute());
        assertEquals(MedianDistanceScheme.NAME, model.getModelName());

        HolisticHighlight holistic = extractSingle(data, model);
        assertTrue("OK predominates (4 of 6)", holistic.execution.result.verdict());
        assertTrue("dominant label OK is reported", holistic.getScores().stream()
                .anyMatch(s -> "OK".equals(s.label)));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
    }

    @Test
    public void kmeansSchemeClustersCellsAndPredominanceHolds() {
        Result data = sixCells("1", "2", "1", "2", "100", "1000");
        MeasureLabelingModel model = new MeasureLabelingModel(data, new KMeansScheme());
        assertEquals(0, model.compute());
        assertEquals(KMeansScheme.NAME, model.getModelName());

        HolisticHighlight holistic = extractSingle(data, model);
        assertTrue("the four low-valued cells form a predominant cluster (4 of 6)",
                holistic.execution.result.verdict());
        assertTrue("dominant cluster label is reported", holistic.getScores().stream()
                .anyMatch(s -> s.label != null && s.label.startsWith("Cluster")));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
    }
}
