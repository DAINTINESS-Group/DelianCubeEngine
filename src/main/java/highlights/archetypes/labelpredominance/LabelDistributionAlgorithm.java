package highlights.archetypes.labelpredominance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import highlights.instance.AlgorithmExecution;
import highlights.instance.AlgorithmResult;
import highlights.instance.LabelingAlgorithm;
import highlights.instance.ParameterInstantiation;
import highlights.instance.Score;
import highlights.instance.ScoredFinding;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.NamedScoreType;
import highlights.metamodel.ParameterRole;
import highlights.metamodel.ScoreKind;
import highlights.metamodel.ScoreType;
import intentional.result.LabeledResult;
import intentional.labeling.Labeling;
import result.Cell;

/**
 * Tests the label-predominance hypothesis over any {@link Labeling} in the context: the cells vote, each
 * casting its own label, and a {@link VotingRule} elects one label as the result's overall characterization.
 * The property holds when the rule produces a winner. Salient cells are surfaced as exemplars of the
 * winning label and exceptions off it; exceptions are ordered by their distance from the winning label when
 * the domain is ordered, and by magnitude otherwise. Magnitude is the labeling's own when its model
 * attached one, otherwise the studied measure. The model or operator that produced the labeling stays out
 * of view.
 */
public final class LabelDistributionAlgorithm implements LabelingAlgorithm {

    private static final String NAME = "LabelDistribution";
    private static final int SALIENT_PER_GROUP = 3;

    /** A cell's label, valued by its rank in the labeling's ordered domain. */
    public static final ScoreType LABEL = new NamedScoreType("Label", ScoreKind.CATEGORICAL);
    /** The share of the cast vote behind the winning label. */
    public static final ScoreType WINNER_SHARE = new NamedScoreType("WinnerShare");
    /** The magnitude by which a salient cell stands out. */
    public static final ScoreType MAGNITUDE = new NamedScoreType("Magnitude");

    /** The {@link VotingRule} that runs the election; -1 lets each labeling take {@link VotingRule#defaultFor}. */
    public static final ParameterRole VOTING_RULE = new ParameterRole(
            "votingRule", "The VotingRule that runs the election, by ordinal; -1 for the labeling's default", -1);
    /** The {@link Weighting} the ballots are cast under. */
    public static final ParameterRole WEIGHTING = new ParameterRole(
            "weighting", "The Weighting the ballots are cast under, by ordinal", 0);

    private final ElementaryHighlightRole labeledCellRole;
    private final ElectionSpec election;

    public LabelDistributionAlgorithm(ElementaryHighlightRole labeledCellRole) {
        this(labeledCellRole, ElectionSpec.DEFAULT);
    }

    public LabelDistributionAlgorithm(ElementaryHighlightRole labeledCellRole, ElectionSpec election) {
        this.labeledCellRole = labeledCellRole;
        this.election = election;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public List<ParameterRole> parameterRoles() { return Arrays.asList(VOTING_RULE, WEIGHTING); }

    @Override
    public boolean appliesTo(LabeledResult context) {
        return !context.labelings().isEmpty();
    }

    @Override
    public AlgorithmExecution run(LabeledResult context, Labeling labeling) {
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

        List<Score> holisticScores = new ArrayList<>();
        if (winner != null) {
            holisticScores.add(new Score(LABEL, labeling.rankOf(winner), winner));
            holisticScores.add(new Score(WINNER_SHARE, tallies.get(winner) / tallyTotal));
        }

        Weighting weighting = election.weighting();
        List<ParameterInstantiation> parameters = Arrays.asList(
                new ParameterInstantiation(VOTING_RULE, rule.ordinal(), rule.name()),
                new ParameterInstantiation(WEIGHTING, weighting.ordinal(), weighting.name()));

        List<ScoredFinding> salient = selectSalient(labeling, winner);
        AlgorithmResult result = new AlgorithmResult(holds);
        for (Map.Entry<String, Double> e : tallies.entrySet()) {
            result.metric("share_" + e.getKey(), tallyTotal == 0.0 ? 0.0 : e.getValue() / tallyTotal);
        }
        return new AlgorithmExecution(this, parameters, result, holisticScores, salient);
    }

    /** The magnitude by which a cell stands out: the labeling's own if present, else the studied measure. */
    private static double magnitudeOf(Cell cell, Labeling labeling) {
        double magnitude = labeling.magnitudeOf(cell);
        return Math.abs(Double.isNaN(magnitude) ? cell.toDouble(labeling.measureIndex()) : magnitude);
    }

    /** The reference a cell was judged against: the labeling's own if present, else the studied measure. */
    private static double referenceOf(Cell cell, Labeling labeling) {
        double reference = labeling.referenceOf(cell);
        return Math.abs(Double.isNaN(reference) ? cell.toDouble(labeling.measureIndex()) : reference);
    }

    /** The weight of a cell's ballot under the election's {@link Weighting}. */
    private double ballotOf(Cell cell, Labeling labeling) {
        switch (election.weighting()) {
            case MAGNITUDE:
                return magnitudeOf(cell, labeling);
            case REFERENCE:
                return referenceOf(cell, labeling);
            default:
                return 1.0;
        }
    }

    private List<ScoredFinding> selectSalient(Labeling labeling, String dominant) {
        Map<Cell, String> labelByCell = labeling.assignment();
        List<Cell> exemplars = new ArrayList<>();
        List<Cell> exceptions = new ArrayList<>();
        for (Map.Entry<Cell, String> e : labelByCell.entrySet()) {
            if (e.getValue() == null) continue;
            (e.getValue().equals(dominant) ? exemplars : exceptions).add(e.getKey());
        }

        exemplars.sort((a, b) -> Double.compare(magnitudeOf(b, labeling), magnitudeOf(a, labeling)));

        if (labeling.ordered()) {
            int dominantRank = labeling.rankOf(dominant);
            exceptions.sort((a, b) -> {
                int distA = Math.abs(labeling.rankOf(labelByCell.get(a)) - dominantRank);
                int distB = Math.abs(labeling.rankOf(labelByCell.get(b)) - dominantRank);
                if (distA != distB) return Integer.compare(distB, distA);
                return Double.compare(magnitudeOf(b, labeling), magnitudeOf(a, labeling));
            });
        } else {
            exceptions.sort((a, b) -> Double.compare(magnitudeOf(b, labeling), magnitudeOf(a, labeling)));
        }

        List<Cell> salient = new ArrayList<>();
        salient.addAll(exemplars.subList(0, Math.min(SALIENT_PER_GROUP, exemplars.size())));
        salient.addAll(exceptions.subList(0, Math.min(SALIENT_PER_GROUP, exceptions.size())));

        List<ScoredFinding> out = new ArrayList<>();
        for (Cell cell : salient) {
            String label = labelByCell.get(cell);
            List<Score> scores = new ArrayList<>();
            scores.add(new Score(LABEL, labeling.rankOf(label), label));
            double magnitude = labeling.magnitudeOf(cell);
            if (!Double.isNaN(magnitude)) scores.add(new Score(MAGNITUDE, magnitude));
            out.add(ScoredFinding.ofCell(cell, cell.toDouble(labeling.measureIndex()), labeledCellRole, scores));
        }
        return out;
    }
}
