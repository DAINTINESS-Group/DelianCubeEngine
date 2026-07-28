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
import highlights.archetypes.labelpredominance.LabelDistributionAlgorithm.Weighting;
import highlights.archetypes.labelpredominance.LabelPredominanceArchetype;
import highlights.archetypes.labelpredominance.VotingRule;
import highlights.instance.AlgorithmExecution;
import highlights.instance.HolisticHighlight;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.CharacterRole;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.MeasureRole;
import highlights.metamodel.ScoreType;
import intentional.result.LabelDomain;
import intentional.result.LabeledResult;
import intentional.result.Labeling;
import intentional.result.LabelingModel;
import result.Cell;
import result.Result;

/**
 * The benchmark-tendency archetype consumes any ordered labeling from the context — with no reference to
 * the model or operator that produced it. A stub labeling model stands in for ASSESS.
 */
public class LabelDistributionTest {

    /** A model that only supplies pre-built labelings. */
    private static final class StubLabelingModel implements LabelingModel {
        private final List<Labeling> labelings;

        StubLabelingModel(List<Labeling> labelings) {
            this.labelings = labelings;
        }

        @Override public String getModelName() { return "stub"; }
        @Override public List<Labeling> labelings() { return labelings; }
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

        Map<Cell, Double> deltas = new LinkedHashMap<>();
        deltas.put(cells[0], 10.0);
        deltas.put(cells[1], 5.0);
        deltas.put(cells[2], 8.0);
        deltas.put(cells[3], -3.0);
        Labeling labeling =
                new Labeling(new LabelDomain(Arrays.asList("low", "mid", "high"), true), labels, 0, deltas);

        LabelingModel model = new StubLabelingModel(Collections.singletonList(labeling));

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

        LabelingModel model = new StubLabelingModel(labelings);

        CubeQuery query = new CubeQuery("twoLabelings");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, data, Collections.singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals("one holistic per labeling, not just the first", 2, highlights.size());
    }

    @Test
    public void medianVoterElectsTheCenterWithoutAPlurality() {
        Result data = new Result();
        Cell[] cells = {
                new Cell(new String[]{"r0", "100", "1"}, 1),
                new Cell(new String[]{"r1", "100", "1"}, 1),
                new Cell(new String[]{"r2", "100", "1"}, 1),
                new Cell(new String[]{"r3", "100", "1"}, 1),
                new Cell(new String[]{"r4", "100", "1"}, 1),
        };
        for (Cell c : cells) data.getCells().add(c);

        Map<Cell, String> labels = new LinkedHashMap<>();
        labels.put(cells[0], "low");
        labels.put(cells[1], "low");
        labels.put(cells[2], "mid");
        labels.put(cells[3], "high");
        labels.put(cells[4], "high");
        Labeling labeling = new Labeling(new LabelDomain(Arrays.asList("low", "mid", "high"), true), labels);
        LabelingModel model = new StubLabelingModel(Collections.singletonList(labeling));

        CubeQuery query = new CubeQuery("medianTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, data, Collections.singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        assertTrue("the median voter elects a winner", holistic.execution.result.verdict());
        assertTrue("the center label wins although low and high out-poll it", holistic.getScores().stream()
                .anyMatch(s -> "mid".equals(s.label)));
    }

    @Test
    public void knifeEdgeVoteDoesNotHold() {
        Result data = new Result();
        Cell[] cells = {
                new Cell(new String[]{"r0", "100", "1"}, 1),
                new Cell(new String[]{"r1", "100", "1"}, 1),
                new Cell(new String[]{"r2", "100", "1"}, 1),
                new Cell(new String[]{"r3", "100", "1"}, 1),
        };
        for (Cell c : cells) data.getCells().add(c);

        Map<Cell, String> labels = new LinkedHashMap<>();
        labels.put(cells[0], "low");
        labels.put(cells[1], "low");
        labels.put(cells[2], "high");
        labels.put(cells[3], "high");
        Labeling labeling = new Labeling(new LabelDomain(Arrays.asList("low", "mid", "high"), true), labels);
        LabelingModel model = new StubLabelingModel(Collections.singletonList(labeling));

        CubeQuery query = new CubeQuery("knifeEdgeTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, data, Collections.singletonList(model));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        assertFalse("an even split across two labels elects no one", holistic.execution.result.verdict());
    }

    @Test
    public void magnitudeWeightingShiftsTheWinner() {
        Result data = new Result();
        Cell[] cells = {
                new Cell(new String[]{"r0", "100", "1"}, 1),
                new Cell(new String[]{"r1", "100", "1"}, 1),
                new Cell(new String[]{"r2", "100", "1"}, 1),
                new Cell(new String[]{"r3", "100", "1"}, 1),
                new Cell(new String[]{"r4", "100", "1"}, 1),
        };
        for (Cell c : cells) data.getCells().add(c);

        Map<Cell, String> labels = new LinkedHashMap<>();
        labels.put(cells[0], "low");
        labels.put(cells[1], "low");
        labels.put(cells[2], "mid");
        labels.put(cells[3], "high");
        labels.put(cells[4], "high");
        Map<Cell, Double> magnitudes = new LinkedHashMap<>();
        magnitudes.put(cells[0], 8.0);
        magnitudes.put(cells[1], 3.0);
        magnitudes.put(cells[2], 0.0);
        magnitudes.put(cells[3], 2.0);
        magnitudes.put(cells[4], 7.0);
        Labeling labeling = new Labeling(
                new LabelDomain(Arrays.asList("low", "mid", "high"), true), labels, 0, magnitudes);
        LabelingModel model = new StubLabelingModel(Collections.singletonList(labeling));

        CubeQuery query = new CubeQuery("weightingTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");
        LabeledResult operatorResult = new LabeledResult(query, data, Collections.singletonList(model));

        ElementaryHighlightRole role = new ElementaryHighlightRole(
                "LabeledCell",
                Collections.singletonList(new CharacterRole("LabeledCell")),
                new MeasureRole("LabeledMeasure"),
                Collections.<ScoreType>singletonList(LabelDistributionAlgorithm.MAGNITUDE));

        AlgorithmExecution byCount = new LabelDistributionAlgorithm(role).run(operatorResult, 0);
        AlgorithmExecution byVolume = new LabelDistributionAlgorithm(
                role, VotingRule.MEDIAN_VOTER, Weighting.MAGNITUDE).run(operatorResult, 0);

        assertTrue("counted ballots elect the center label", byCount.holisticScores.stream()
                .anyMatch(s -> "mid".equals(s.label)));
        assertTrue("volume-weighted ballots elect the label holding the barycenter",
                byVolume.holisticScores.stream().anyMatch(s -> "low".equals(s.label)));
    }

    @Test
    public void referenceWeightingFollowsTheExpectedVolume() {
        Result data = new Result();
        Cell[] cells = {
                new Cell(new String[]{"r0", "100", "1"}, 1),
                new Cell(new String[]{"r1", "100", "1"}, 1),
                new Cell(new String[]{"r2", "100", "1"}, 1),
                new Cell(new String[]{"r3", "100", "1"}, 1),
                new Cell(new String[]{"r4", "100", "1"}, 1),
        };
        for (Cell c : cells) data.getCells().add(c);

        Map<Cell, String> labels = new LinkedHashMap<>();
        labels.put(cells[0], "low");
        labels.put(cells[1], "low");
        labels.put(cells[2], "mid");
        labels.put(cells[3], "high");
        labels.put(cells[4], "high");
        Map<Cell, Double> references = new LinkedHashMap<>();
        references.put(cells[0], 20.0);
        references.put(cells[1], 15.0);
        references.put(cells[2], 1.0);
        references.put(cells[3], 2.0);
        references.put(cells[4], 2.0);
        Labeling labeling = new Labeling(
                new LabelDomain(Arrays.asList("low", "mid", "high"), true), labels, 0,
                Collections.<Cell, Double>emptyMap(), references);
        LabelingModel model = new StubLabelingModel(Collections.singletonList(labeling));

        CubeQuery query = new CubeQuery("referenceTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");
        LabeledResult operatorResult = new LabeledResult(query, data, Collections.singletonList(model));

        ElementaryHighlightRole role = new ElementaryHighlightRole(
                "LabeledCell",
                Collections.singletonList(new CharacterRole("LabeledCell")),
                new MeasureRole("LabeledMeasure"),
                Collections.<ScoreType>singletonList(LabelDistributionAlgorithm.MAGNITUDE));

        AlgorithmExecution byReference = new LabelDistributionAlgorithm(
                role, VotingRule.MEDIAN_VOTER, Weighting.REFERENCE).run(operatorResult, 0);

        assertTrue("ballots weighted by the judged-against values elect the label expected to carry the volume",
                byReference.holisticScores.stream().anyMatch(s -> "low".equals(s.label)));
    }
}
