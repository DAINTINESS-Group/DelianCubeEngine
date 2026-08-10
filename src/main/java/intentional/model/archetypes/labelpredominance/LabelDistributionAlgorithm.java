package intentional.model.archetypes.labelpredominance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import intentional.labeling.LabelDomain;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import intentional.model.Model;
import intentional.result.LabeledResult;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import intentional.model.ParameterRole;
import intentional.model.ModelResultImpl;
import result.Cell;

/**
 * Tests the label-predominance hypothesis over every labelling in the context: the cells vote, each casting
 * its own label, and a {@link VotingRule} elects one label as the result's overall characterization. Holds
 * when the rule produces a winner. It labels each cell {@code exemplar} of the winning label or
 * {@code exception} off it, inheriting the cell's magnitude.
 */
public final class LabelDistributionAlgorithm implements Model {

    public static final String NAME = "LabelDistribution";
    public static final String EXEMPLAR = "exemplar";
    public static final String EXCEPTION = "exception";

    public static final ParameterRole VOTING_RULE = new ParameterRole(
            "votingRule", "The VotingRule that runs the election, by ordinal; -1 for the labeling's default", -1);
    public static final ParameterRole WEIGHTING = new ParameterRole(
            "weighting", "The Weighting the ballots are cast under, by ordinal", 0);

    private final ElectionSpec election;

    public LabelDistributionAlgorithm() {
        this(ElectionSpec.DEFAULT);
    }

    public LabelDistributionAlgorithm(ElectionSpec election) {
        this.election = election;
    }

    @Override public String name() { return NAME; }

    @Override public List<ParameterRole> parameterRoles() {
        return Arrays.asList(VOTING_RULE, WEIGHTING);
    }

    @Override
    public List<ModelResult> run(LabeledResult context) {
        List<ModelResult> out = new ArrayList<>();
        for (Labeling labeling : context.labelings()) out.add(runLabeling(labeling));
        return out;
    }

    private ModelResult runLabeling(Labeling labeling) {
        Map<Cell, String> labelByCell = labeling.assignment();

        Map<String, Double> tallies = new LinkedHashMap<>();
        for (String label : labeling.domain()) tallies.put(label, 0.0);
        double tallyTotal = 0.0;
        for (Map.Entry<Cell, String> e : labelByCell.entrySet()) {
            String label = e.getValue();
            if (label == null) continue;
            double ballot = ballotOf(e.getKey(), labeling);
            tallies.merge(label, ballot, Double::sum);
            tallyTotal += ballot;
        }

        VotingRule rule = election.ruleFor(labeling.ordered());
        String winner = rule.elect(labeling.domain(), labeling.ordered(), tallies, tallyTotal);
        boolean holds = winner != null;
        double winnerShare = winner == null || tallyTotal == 0.0 ? 0.0 : tallies.get(winner) / tallyTotal;

        Map<Cell, Double> driver = new LinkedHashMap<>();
        Map<Cell, Double> magnitudes = new LinkedHashMap<>();
        for (Map.Entry<Cell, String> e : labelByCell.entrySet()) {
            if (e.getValue() == null) continue;
            driver.put(e.getKey(), e.getValue().equals(winner) ? 1.0 : 0.0);
            magnitudes.put(e.getKey(), labeling.magnitudeOf(e.getKey()));
        }
        Labeling labelling = Labeling.withInheritedMagnitudes(
                scheme(), driver, labeling.measureIndex(), magnitudes, Collections.<Cell, Double>emptyMap());

        Weighting weighting = election.weighting();
        List<ParameterInstantiation> parameters = Arrays.asList(
                new ParameterInstantiation(VOTING_RULE, rule.ordinal(), rule.name()),
                new ParameterInstantiation(WEIGHTING, weighting.ordinal(), weighting.name()));

        ModelResultImpl result = new ModelResultImpl(NAME, holds, labelling, parameters).holistic(winner, winnerShare);
        for (Map.Entry<String, Double> e : tallies.entrySet()) {
            result.metric("share_" + e.getKey(), tallyTotal == 0.0 ? 0.0 : e.getValue() / tallyTotal);
        }
        return result;
    }

    private double ballotOf(Cell cell, Labeling labeling) {
        switch (election.weighting()) {
            case MAGNITUDE:
                return abs(labeling.magnitudeOf(cell));
            case REFERENCE:
                return abs(labeling.referenceOf(cell));
            default:
                return 1.0;
        }
    }

    private static double abs(double value) {
        return Double.isNaN(value) ? 0.0 : Math.abs(value);
    }

    private static LabelingScheme scheme() {
        LabelDomain domain = new LabelDomain(Arrays.asList(EXCEPTION, EXEMPLAR), true);
        return new LabelingScheme() {
            @Override public String name() { return NAME; }
            @Override public String applyLabels(double value) { return value >= 1.0 ? EXEMPLAR : EXCEPTION; }
            @Override public LabelDomain domain() { return domain; }
        };
    }
}
