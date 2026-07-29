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
import intentional.labeling.LabelDomain;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingModel;
import intentional.labeling.LabelingScheme;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * The benchmark-tendency archetype consumes any ordered labeling from the context — with no reference to
 * the model or operator that produced it. A stub labeling model supplies pre-built labelings, each labeled
 * under a stub scheme that maps each quantity value to its label by lookup.
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

    /** A scheme labeling each value by lookup over an ordered domain. */
    private static LabelingScheme lookupScheme(List<String> domainLabels, Map<Double, String> labelByValue) {
        return new LabelingScheme() {
            @Override public String name() { return "stub"; }
            @Override public String applyLabels(double value) { return labelByValue.get(value); }
            @Override public LabelDomain domain() { return new LabelDomain(domainLabels, true); }
        };
    }

    private static Cell[] cells(int count) {
        Cell[] cells = new Cell[count];
        for (int i = 0; i < count; i++) {
            cells[i] = new Cell(new String[]{"r" + i, "100", "1"}, 1);
        }
        return cells;
    }

    private static Result resultOf(Cell[] cells) {
        Result data = new Result();
        for (Cell c : cells) data.getCells().add(c);
        return data;
    }

    private static LabeledResult operatorResult(String queryName, Result data, LabelingModel model) {
        CubeQuery query = new CubeQuery(queryName);
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");
        return new LabeledResult(query, data, Collections.singletonList(model));
    }

    private static Map<Double, String> rankLabels(String... labels) {
        Map<Double, String> byValue = new LinkedHashMap<>();
        for (int i = 0; i < labels.length; i++) byValue.put((double) i, labels[i]);
        return byValue;
    }

    private static ElementaryHighlightRole labeledCellRole() {
        return new ElementaryHighlightRole(
                "LabeledCell",
                Collections.singletonList(new CharacterRole("LabeledCell")),
                new MeasureRole("LabeledMeasure"),
                Collections.<ScoreType>singletonList(LabelDistributionAlgorithm.MAGNITUDE));
    }

    @Test
    public void predominantLabelHoldsAndSurfacesSalientCells() {
        Cell[] cells = cells(4);
        Result data = resultOf(cells);

        Map<Cell, Double> deltas = new LinkedHashMap<>();
        deltas.put(cells[0], 10.0);
        deltas.put(cells[1], 5.0);
        deltas.put(cells[2], 8.0);
        deltas.put(cells[3], -3.0);
        Map<Double, String> labelByDelta = new LinkedHashMap<>();
        labelByDelta.put(10.0, "high");
        labelByDelta.put(5.0, "high");
        labelByDelta.put(8.0, "high");
        labelByDelta.put(-3.0, "low");
        Labeling labeling =
                new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), labelByDelta), deltas);

        LabeledResult operatorResult = operatorResult(
                "labelTest", data, new StubLabelingModel(Collections.singletonList(labeling)));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals(1, highlights.size());

        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        assertTrue("high predominates (3 of 4)", holistic.execution.result.verdict());
        assertTrue("dominant label is reported", holistic.getScores().stream()
                .anyMatch(s -> "high".equals(s.label)));
        assertFalse("salient cells surfaced", holistic.elementary().isEmpty());
        assertTrue("a salient cell carries the labeled quantity as magnitude", holistic.elementary().stream()
                .flatMap(e -> e.getScores().stream())
                .anyMatch(s -> s.type == LabelDistributionAlgorithm.MAGNITUDE));
    }

    @Test
    public void oneHolisticPerLabeling() {
        Cell[] cells = cells(4);
        Result data = resultOf(cells);

        Map<Cell, Double> assessment = new LinkedHashMap<>();
        assessment.put(cells[0], 2.0);
        assessment.put(cells[1], 2.0);
        assessment.put(cells[2], 0.0);
        assessment.put(cells[3], 2.0);
        Map<Cell, Double> outlierness = new LinkedHashMap<>();
        outlierness.put(cells[0], 0.0);
        outlierness.put(cells[1], 0.0);
        outlierness.put(cells[2], 0.0);
        outlierness.put(cells[3], 1.0);

        List<Labeling> labelings = Arrays.asList(
                new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"),
                        rankLabels("low", "mid", "high")), assessment),
                new Labeling(lookupScheme(Arrays.asList("non-outlier", "outlier"),
                        rankLabels("non-outlier", "outlier")), outlierness));

        LabeledResult operatorResult = operatorResult(
                "twoLabelings", data, new StubLabelingModel(labelings));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals("one holistic per labeling, not just the first", 2, highlights.size());
    }

    @Test
    public void medianVoterElectsTheCenterWithoutAPlurality() {
        Cell[] cells = cells(5);
        Result data = resultOf(cells);

        Map<Cell, Double> ranks = new LinkedHashMap<>();
        ranks.put(cells[0], 0.0);
        ranks.put(cells[1], 0.0);
        ranks.put(cells[2], 1.0);
        ranks.put(cells[3], 2.0);
        ranks.put(cells[4], 2.0);
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"),
                rankLabels("low", "mid", "high")), ranks);

        LabeledResult operatorResult = operatorResult(
                "medianTest", data, new StubLabelingModel(Collections.singletonList(labeling)));
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
        Cell[] cells = cells(4);
        Result data = resultOf(cells);

        Map<Cell, Double> ranks = new LinkedHashMap<>();
        ranks.put(cells[0], 0.0);
        ranks.put(cells[1], 0.0);
        ranks.put(cells[2], 2.0);
        ranks.put(cells[3], 2.0);
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"),
                rankLabels("low", "mid", "high")), ranks);

        LabeledResult operatorResult = operatorResult(
                "knifeEdgeTest", data, new StubLabelingModel(Collections.singletonList(labeling)));
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(LabelPredominanceArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        HolisticHighlight holistic = (HolisticHighlight) highlights.highlights().get(0);
        assertFalse("an even split across two labels elects no one", holistic.execution.result.verdict());
    }

    @Test
    public void magnitudeWeightingShiftsTheWinner() {
        Cell[] cells = cells(5);
        Result data = resultOf(cells);

        Map<Cell, Double> quantity = new LinkedHashMap<>();
        quantity.put(cells[0], 8.0);
        quantity.put(cells[1], 3.0);
        quantity.put(cells[2], 0.0);
        quantity.put(cells[3], 2.0);
        quantity.put(cells[4], 7.0);
        Map<Double, String> labelByValue = new LinkedHashMap<>();
        labelByValue.put(8.0, "low");
        labelByValue.put(3.0, "low");
        labelByValue.put(0.0, "mid");
        labelByValue.put(2.0, "high");
        labelByValue.put(7.0, "high");
        Labeling labeling =
                new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), labelByValue), quantity);

        LabeledResult operatorResult = operatorResult(
                "weightingTest", data, new StubLabelingModel(Collections.singletonList(labeling)));
        ElementaryHighlightRole role = labeledCellRole();

        AlgorithmExecution byCount = new LabelDistributionAlgorithm(role).run(operatorResult, labeling);
        AlgorithmExecution byVolume = new LabelDistributionAlgorithm(
                role, VotingRule.MEDIAN_VOTER, Weighting.MAGNITUDE).run(operatorResult, labeling);

        assertTrue("counted ballots elect the center label", byCount.holisticScores.stream()
                .anyMatch(s -> "mid".equals(s.label)));
        assertTrue("volume-weighted ballots elect the label holding the barycenter",
                byVolume.holisticScores.stream().anyMatch(s -> "low".equals(s.label)));
    }

    @Test
    public void referenceWeightingFollowsTheExpectedVolume() {
        Cell[] cells = cells(5);
        Result data = resultOf(cells);

        Map<Cell, Double> ranks = new LinkedHashMap<>();
        ranks.put(cells[0], 0.0);
        ranks.put(cells[1], 0.0);
        ranks.put(cells[2], 1.0);
        ranks.put(cells[3], 2.0);
        ranks.put(cells[4], 2.0);
        Map<Cell, Double> references = new LinkedHashMap<>();
        references.put(cells[0], 20.0);
        references.put(cells[1], 15.0);
        references.put(cells[2], 1.0);
        references.put(cells[3], 2.0);
        references.put(cells[4], 2.0);
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"),
                rankLabels("low", "mid", "high")), ranks, 0, references);

        LabeledResult operatorResult = operatorResult(
                "referenceTest", data, new StubLabelingModel(Collections.singletonList(labeling)));
        ElementaryHighlightRole role = labeledCellRole();

        AlgorithmExecution byReference = new LabelDistributionAlgorithm(
                role, VotingRule.MEDIAN_VOTER, Weighting.REFERENCE).run(operatorResult, labeling);

        assertTrue("ballots weighted by the judged-against values elect the label expected to carry the volume",
                byReference.holisticScores.stream().anyMatch(s -> "low".equals(s.label)));
    }
}
