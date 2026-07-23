package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.archetypes.labelpredominance.LabelDistributionAlgorithm;
import highlights.archetypes.labelpredominance.LabelPredominanceArchetype;
import highlights.instance.HolisticHighlight;
import highlights.metamodel.ArchetypeProperty;
import labeling.LabeledResult;
import labeling.DerivedMeasure;
import labeling.LabelDomain;
import labeling.Labeling;
import labeling.LabelingModel;
import result.Cell;
import result.Result;

/**
 * The benchmark-tendency archetype consumes any ordered labeling and a derived measure from the context —
 * with no reference to the model or operator that produced them. A stub labeling model stands in for ASSESS.
 */
public class LabelDistributionTest {

    /** A model that only supplies pre-built labelings and derived measures. */
    private static final class StubLabelingModel implements LabelingModel {
        private final List<Labeling> labelings;
        private final List<DerivedMeasure> derived;

        StubLabelingModel(List<Labeling> labelings, List<DerivedMeasure> derived) {
            this.labelings = labelings;
            this.derived = derived;
        }

        @Override public String getModelName() { return "stub"; }
        @Override public List<Labeling> labelings() { return labelings; }
        @Override public List<DerivedMeasure> derivedMeasures() { return derived; }
    }

    @Test
    public void predominantLabelHoldsAndSurfacesSalientCells() {
        Result data = new Result();
        Cell[] cells = {
                new Cell(new String[]{"r0", "100", "1"}, 1),
                new Cell(new String[]{"r1", "100", "1"}, 1),
                new Cell(new String[]{"r2", "100", "1"}, 1),
                new Cell(new String[]{"r3", "100", "1"}, 1),
        };
        for (Cell c : cells) data.getCells().add(c);

        Map<Cell, String> labels = new LinkedHashMap<>();
        labels.put(cells[0], "high");
        labels.put(cells[1], "high");
        labels.put(cells[2], "high");
        labels.put(cells[3], "low");
        Labeling labeling = new Labeling(new LabelDomain(Arrays.asList("low", "mid", "high"), true), labels);

        Map<Cell, Double> deltas = new LinkedHashMap<>();
        deltas.put(cells[0], 10.0);
        deltas.put(cells[1], 5.0);
        deltas.put(cells[2], 8.0);
        deltas.put(cells[3], -3.0);
        DerivedMeasure delta = new DerivedMeasure(deltas);

        LabelingModel model = new StubLabelingModel(
                Collections.singletonList(labeling), Collections.singletonList(delta));

        CubeQuery query = new CubeQuery("labelTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, data, Collections.singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals(1, highlights.size());

        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        assertTrue("high predominates (3 of 4)", holistic.execution.result.verdict());
        assertTrue("dominant label is reported", holistic.getScores().stream()
                .anyMatch(s -> "high".equals(s.label)));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
        assertTrue("a salient cell carries the derived-measure magnitude", holistic.elementary().stream()
                .flatMap(e -> e.getScores().stream())
                .anyMatch(s -> s.type == LabelDistributionAlgorithm.MAGNITUDE));
    }

    @Test
    public void oneHolisticPerLabeling() {
        Result data = new Result();
        Cell[] cells = {
                new Cell(new String[]{"r0", "100", "1"}, 1),
                new Cell(new String[]{"r1", "100", "1"}, 1),
                new Cell(new String[]{"r2", "100", "1"}, 1),
                new Cell(new String[]{"r3", "100", "1"}, 1),
        };
        for (Cell c : cells) data.getCells().add(c);

        Map<Cell, String> assessment = new LinkedHashMap<>();
        assessment.put(cells[0], "high");
        assessment.put(cells[1], "high");
        assessment.put(cells[2], "low");
        assessment.put(cells[3], "high");
        Map<Cell, String> outlier = new LinkedHashMap<>();
        outlier.put(cells[0], "non-outlier");
        outlier.put(cells[1], "non-outlier");
        outlier.put(cells[2], "non-outlier");
        outlier.put(cells[3], "outlier");

        List<Labeling> labelings = Arrays.asList(
                new Labeling(new LabelDomain(Arrays.asList("low", "mid", "high"), true), assessment),
                new Labeling(new LabelDomain(Arrays.asList("non-outlier", "outlier"), true), outlier));

        LabelingModel model = new StubLabelingModel(labelings, Collections.<DerivedMeasure>emptyList());

        CubeQuery query = new CubeQuery("twoLabelings");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, data, Collections.singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals("one holistic per labeling, not just the first", 2, highlights.size());
    }
}
