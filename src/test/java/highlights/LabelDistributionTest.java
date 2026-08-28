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

import cubemanager.cubebase.CubeQuery;
import intentional.assess.ConsensusModel;
import intentional.labeling.LabelDomain;
import intentional.labeling.consensus.ConsensusRule;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import intentional.model.ModelResultImpl;
import intentional.model.archetypes.labelpredominance.ElectionSpec;
import intentional.model.archetypes.labelpredominance.LabelDistributionAlgorithm;
import intentional.model.archetypes.labelpredominance.VotingRule;
import intentional.model.archetypes.labelpredominance.Weighting;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * Label-predominance consumes any ordered labelling: the cells vote and a rule elects a winning label, which
 * becomes the result's holistic label; the winner's cells are labelled exemplars, the rest exceptions.
 */
public class LabelDistributionTest {

    private static LabelingScheme lookupScheme(List<String> domainLabels, Map<Double, String> labelByValue) {
        return new LabelingScheme() {
            @Override public String name() { return "stub"; }
            @Override public String applyLabels(double value) { return labelByValue.get(value); }
            @Override public LabelDomain domain() { return new LabelDomain(domainLabels, true); }
        };
    }

    private static Cell[] cells(int count) {
        Cell[] cells = new Cell[count];
        for (int i = 0; i < count; i++) cells[i] = new Cell(new String[]{"r" + i, "100", "1"}, 1);
        return cells;
    }

    private static Result resultOf(Cell[] cells) {
        Result data = new Result();
        for (Cell c : cells) data.getCells().add(c);
        return data;
    }

    private static LabeledResult operatorResult(String queryName, Result data, List<Labeling> labelings) {
        CubeQuery query = new CubeQuery(queryName);
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");
        List<ModelResultImpl> models = new ArrayList<>();
        for (Labeling labeling : labelings) {
            models.add(new ModelResultImpl(labeling.schemeName(), true, labeling,
                    Collections.<ParameterInstantiation>emptyList()).origin(ModelOrigin.OPERATOR));
        }
        return new LabeledResult(query, data, models);
    }

    private static Map<Double, String> rankLabels(String... labels) {
        Map<Double, String> byValue = new LinkedHashMap<>();
        for (int i = 0; i < labels.length; i++) byValue.put((double) i, labels[i]);
        return byValue;
    }

    private static List<ModelResult> predominance(LabeledResult operatorResult, ElectionSpec election) {
        new LabelDistributionAlgorithm(election).run(operatorResult);
        return operatorResult.archetypeModels();
    }

    private static Labeling consensusOf(LabeledResult operatorResult) {
        new ConsensusModel(ConsensusRule.KEMENY).run(operatorResult);
        for (ModelResult model : operatorResult.models()) {
            if (model.modelName().equals(ConsensusModel.NAME)) return model.labelling();
        }
        return null;
    }

    @Test
    public void predominantLabelHoldsAndSurfacesSalientCells() {
        Cell[] cells = cells(4);
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
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), labelByDelta), deltas);

        LabeledResult operatorResult = operatorResult("labelTest", resultOf(cells),
                Collections.singletonList(labeling));
        ModelResult result = predominance(operatorResult, ElectionSpec.DEFAULT).get(0);

        assertTrue("high predominates (3 of 4)", result.verdict());
        assertEquals("dominant label is reported", "high", result.holisticLabel());
        assertFalse("cells are labelled", result.labelling().assignment().isEmpty());
        assertTrue("an exemplar carries the labeled quantity as magnitude",
                !Double.isNaN(result.labelling().magnitudeOf(cells[0])));
    }

    @Test
    public void oneResultPerLabeling() {
        Cell[] cells = cells(4);
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
                new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), rankLabels("low", "mid", "high")), assessment),
                new Labeling(lookupScheme(Arrays.asList("non-outlier", "outlier"), rankLabels("non-outlier", "outlier")), outlierness));

        LabeledResult operatorResult = operatorResult("twoLabelings", resultOf(cells), labelings);
        assertEquals("one result per labeling", 2, predominance(operatorResult, ElectionSpec.DEFAULT).size());
    }

    @Test
    public void sharedDomainLabelingsGainAConsensus() {
        Cell[] cells = cells(5);
        List<Labeling> labelings = Arrays.asList(
                labelingOf(cells, "s1", 0, 1, 1, 1, 2),
                labelingOf(cells, "s2", 0, 0, 1, 2, 2),
                labelingOf(cells, "s3", 0, 0, 1, 1, 2));

        LabeledResult operatorResult = operatorResult("consensusTest", resultOf(cells), labelings);
        assertEquals("the consensus names its group", "Consensus(s1,s2,s3)",
                consensusOf(operatorResult).schemeName());
        assertEquals("one result per labeling plus the consensus", 4,
                predominance(operatorResult, ElectionSpec.DEFAULT).size());
    }

    private static Labeling labelingOf(Cell[] cells, String schemeName, double... ranks) {
        Map<Cell, Double> rankByCell = new LinkedHashMap<>();
        for (int i = 0; i < cells.length; i++) rankByCell.put(cells[i], ranks[i]);
        Map<Double, String> labelByRank = rankLabels("low", "mid", "high");
        LabelingScheme scheme = new LabelingScheme() {
            @Override public String name() { return schemeName; }
            @Override public String applyLabels(double value) { return labelByRank.get(value); }
            @Override public LabelDomain domain() { return new LabelDomain(Arrays.asList("low", "mid", "high"), true); }
        };
        return new Labeling(scheme, rankByCell);
    }

    @Test
    public void medianVoterElectsTheCenterWithoutAPlurality() {
        Cell[] cells = cells(5);
        Map<Cell, Double> ranks = new LinkedHashMap<>();
        ranks.put(cells[0], 0.0);
        ranks.put(cells[1], 0.0);
        ranks.put(cells[2], 1.0);
        ranks.put(cells[3], 2.0);
        ranks.put(cells[4], 2.0);
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), rankLabels("low", "mid", "high")), ranks);

        ModelResult result = predominance(operatorResult("medianTest", resultOf(cells),
                Collections.singletonList(labeling)), ElectionSpec.DEFAULT).get(0);
        assertTrue("the median voter elects a winner", result.verdict());
        assertEquals("the center label wins", "mid", result.holisticLabel());
    }

    @Test
    public void knifeEdgeVoteDoesNotHold() {
        Cell[] cells = cells(4);
        Map<Cell, Double> ranks = new LinkedHashMap<>();
        ranks.put(cells[0], 0.0);
        ranks.put(cells[1], 0.0);
        ranks.put(cells[2], 2.0);
        ranks.put(cells[3], 2.0);
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), rankLabels("low", "mid", "high")), ranks);

        ModelResult result = predominance(operatorResult("knifeEdgeTest", resultOf(cells),
                Collections.singletonList(labeling)), ElectionSpec.DEFAULT).get(0);
        assertFalse("an even split across two labels elects no one", result.verdict());
    }

    @Test
    public void magnitudeWeightingShiftsTheWinner() {
        Cell[] cells = cells(5);
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
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), labelByValue), quantity);

        ModelResult byCount = predominance(operatorResult("weightingTest", resultOf(cells),
                Collections.singletonList(labeling)), ElectionSpec.DEFAULT).get(0);
        ModelResult byVolume = predominance(operatorResult("weightingTest", resultOf(cells),
                Collections.singletonList(labeling)),
                new ElectionSpec(VotingRule.MEDIAN_VOTER, Weighting.MAGNITUDE)).get(0);

        assertEquals("counted ballots elect the center label", "mid", byCount.holisticLabel());
        assertEquals("volume-weighted ballots elect the label holding the barycenter", "low",
                byVolume.holisticLabel());
    }

    @Test
    public void referenceWeightingFollowsTheExpectedVolume() {
        Cell[] cells = cells(5);
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
        Labeling labeling = new Labeling(lookupScheme(Arrays.asList("low", "mid", "high"), rankLabels("low", "mid", "high")), ranks, 0, references);

        ModelResult byReference = predominance(operatorResult("referenceTest", resultOf(cells),
                Collections.singletonList(labeling)),
                new ElectionSpec(VotingRule.MEDIAN_VOTER, Weighting.REFERENCE)).get(0);
        assertEquals("ballots weighted by the judged-against values elect the expected label", "low",
                byReference.holisticLabel());
    }

    @Test
    public void magnitudeWeightedElectionOverConsensusReadsInheritedVolume() {
        Cell[] cells = cells(5);
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
        List<String> domain = Arrays.asList("low", "mid", "high");

        List<Labeling> labelings = Arrays.asList(
                new Labeling(lookupScheme(domain, labelByValue), quantity),
                new Labeling(lookupScheme(domain, labelByValue), quantity));
        LabeledResult operatorResult = operatorResult("consensusElection", resultOf(cells), labelings);
        Labeling consensus = consensusOf(operatorResult);
        assertEquals("the consensus inherits the group's magnitude, not the bucket rank", 8.0,
                consensus.magnitudeOf(cells[0]), 1e-9);

        ModelResult byCount = predominance(operatorResult("consensusElection", resultOf(cells),
                Collections.singletonList(consensus)), ElectionSpec.DEFAULT).get(0);
        ModelResult byVolume = predominance(operatorResult("consensusElection", resultOf(cells),
                Collections.singletonList(consensus)),
                new ElectionSpec(VotingRule.MEDIAN_VOTER, Weighting.MAGNITUDE)).get(0);

        assertEquals("counted ballots over the consensus elect the center label", "mid", byCount.holisticLabel());
        assertEquals("magnitude-weighted ballots over the consensus follow the inherited volume", "low",
                byVolume.holisticLabel());
    }
}
