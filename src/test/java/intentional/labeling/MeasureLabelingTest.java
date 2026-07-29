package intentional.labeling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.archetypes.labelpredominance.LabelPredominanceArchetype;
import highlights.instance.HolisticHighlight;
import highlights.metamodel.ArchetypeProperty;
import intentional.labeling.schemes.KMeansScheme;
import intentional.labeling.schemes.MedianDistanceScheme;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * A measure labeled under any scheme: the labeling is built from the measure's values, and the
 * label-predominance archetype consumes it regardless of which scheme produced it.
 */
public class MeasureLabelingTest {

    private static Result sixCells(String... measures) {
        Result data = new Result();
        for (int i = 0; i < measures.length; i++) {
            data.getCells().add(new Cell(new String[]{"r" + i, measures[i], "1"}, 1));
        }
        return data;
    }

    private static Labeling labelMeasure(Result data, LabelingScheme scheme) {
        Map<Cell, Double> valueByCell = new LinkedHashMap<>();
        for (Cell cell : data.getCells()) valueByCell.put(cell, cell.toDouble(0));
        return new Labeling(scheme, valueByCell, 0);
    }

    private static HolisticHighlight extractSingle(Result data, Labeling labeling) {
        CubeQuery query = new CubeQuery("measureLabelingTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult =
                new LabeledResult(query, data, Collections.singletonList(labeling));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals(1, highlights.size());
        return (HolisticHighlight) highlights.highlights().get(0);
    }

    @Test
    public void medianSchemeLabelsCellsAndPredominanceHolds() {
        Result data = sixCells("10", "10", "10", "10", "100", "1");
        Labeling labeling = labelMeasure(data, new MedianDistanceScheme());
        assertEquals(MedianDistanceScheme.NAME, labeling.schemeName());

        HolisticHighlight holistic = extractSingle(data, labeling);
        assertTrue("OK predominates (4 of 6)", holistic.execution.result.verdict());
        assertTrue("dominant label OK is reported", holistic.getScores().stream()
                .anyMatch(s -> "OK".equals(s.label)));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
    }

    @Test
    public void kmeansSchemeClustersCellsAndPredominanceHolds() {
        Result data = sixCells("1", "2", "1", "2", "100", "1000");
        Labeling labeling = labelMeasure(data, new KMeansScheme());
        assertEquals(KMeansScheme.NAME, labeling.schemeName());

        HolisticHighlight holistic = extractSingle(data, labeling);
        assertTrue("the four low-valued cells form a predominant cluster (4 of 6)",
                holistic.execution.result.verdict());
        assertTrue("dominant cluster label is reported", holistic.getScores().stream()
                .anyMatch(s -> s.label != null && s.label.startsWith("Cluster")));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
    }
}
