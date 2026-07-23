package highlights.archetypes.megacontributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import highlights.instance.AlgorithmExecution;
import highlights.instance.AlgorithmResult;
import highlights.instance.ParameterInstantiation;
import highlights.instance.ExecutableAlgorithm;
import highlights.instance.Score;
import highlights.instance.ScoredFinding;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.NamedScoreType;
import highlights.metamodel.ParameterRole;
import highlights.metamodel.ScoreType;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * Tests the mega-contributor hypothesis as a marginal contribution: for each breakdown dimension (a
 * group-by level with more than one member), it sums the measure per member — marginalizing over the other
 * dimensions — and checks whether a member holds more than a threshold share of the total. Each dominating
 * member is surfaced as an elementary highlight along its dimension.
 */
public final class MarginalContributionAlgorithm implements ExecutableAlgorithm {

    private static final String NAME = "MarginalContribution";

    /** The minimum share of the total a member must hold along its breakdown dimension to dominate. */
    public static final ParameterRole DOMINANCE_THRESHOLD =
            new ParameterRole("dominanceThreshold", "Minimum share of the total to dominate", 0.5);

    /** The share of the total a member holds along its breakdown dimension. */
    public static final ScoreType CONTRIBUTION_SHARE = new NamedScoreType("ContributionShare");

    private final ElementaryHighlightRole contributorRole;

    public MarginalContributionAlgorithm(ElementaryHighlightRole contributorRole) {
        this.contributorRole = contributorRole;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public List<ParameterRole> parameterRoles() {
        return Collections.singletonList(DOMINANCE_THRESHOLD);
    }

    @Override
    public boolean appliesTo(LabeledResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public AlgorithmExecution run(LabeledResult context, int measureIndex) {
        ParameterInstantiation dominance = ParameterInstantiation.ofDefault(DOMINANCE_THRESHOLD);
        double threshold = dominance.value;

        List<Cell> cells = context.data.getCells();
        double total = 0.0;
        for (Cell c : cells) total += c.toDouble(measureIndex);
        int dimensions = cells.isEmpty() ? 0 : cells.get(0).getDimensionMembers().size();

        double topShare = 0.0;
        List<ScoredFinding> salient = new ArrayList<>();
        for (int d = 0; d < dimensions; d++) {
            Map<String, Double> marginal = new LinkedHashMap<>();
            for (Cell c : cells) {
                marginal.merge(c.getDimensionMembers().get(d), c.toDouble(measureIndex), Double::sum);
            }
            if (marginal.size() < 2) continue; // a pinned/filter dimension, not a breakdown

            for (Map.Entry<String, Double> e : marginal.entrySet()) {
                double share = total == 0.0 ? 0.0 : e.getValue() / total;
                if (share > topShare) topShare = share;
                if (share > threshold) {
                    List<Score> scores = new ArrayList<>();
                    scores.add(new Score(CONTRIBUTION_SHARE, share));
                    salient.add(ScoredFinding.marginal(d, e.getKey(), e.getValue(), contributorRole, scores));
                }
            }
        }

        boolean holds = !salient.isEmpty();
        List<Score> holisticScores = new ArrayList<>();
        holisticScores.add(new Score(CONTRIBUTION_SHARE, topShare));
        AlgorithmResult result = new AlgorithmResult(holds).metric("count", (double) cells.size());
        return new AlgorithmExecution(this, Collections.singletonList(dominance), result, holisticScores, salient);
    }
}
