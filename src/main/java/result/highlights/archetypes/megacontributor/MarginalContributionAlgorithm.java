package result.highlights.archetypes.megacontributor;

import result.highlights.OperatorResult;
import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.AlgorithmParams;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.NamedScoreType;
import result.highlights.metamodel.ScoreType;
import result.highlights.instance.AlgorithmExecution;
import result.highlights.instance.AlgorithmResult;
import result.highlights.instance.ArchetypeResult;
import result.highlights.instance.Score;
import result.highlights.instance.ScoredFinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import result.Cell;

/**
 * Tests the mega-contributor hypothesis as a marginal contribution: for each breakdown dimension (a
 * group-by level with more than one member), it sums the measure per member — marginalizing over the other
 * dimensions — and checks whether a member holds more than a threshold share of the total. Each dominating
 * member is surfaced as an elementary highlight along its dimension.
 */
public final class MarginalContributionAlgorithm implements Algorithm {

    private static final String NAME = "MarginalContribution";
    private static final double DEFAULT_DOMINANCE_THRESHOLD = 0.5;

    /** The share of the total a member holds along its breakdown dimension. */
    public static final ScoreType CONTRIBUTION_SHARE = new NamedScoreType("ContributionShare");

    private final ElementaryHighlightRole contributorRole;

    public MarginalContributionAlgorithm(ElementaryHighlightRole contributorRole) {
        this.contributorRole = contributorRole;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public AlgorithmParams params() {
        return new AlgorithmParams().set("dominanceThreshold", DEFAULT_DOMINANCE_THRESHOLD);
    }

    @Override
    public boolean appliesTo(OperatorResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public ArchetypeResult run(OperatorResult context, int measureIndex) {
        AlgorithmParams params = params();
        double threshold = params.get("dominanceThreshold", DEFAULT_DOMINANCE_THRESHOLD);

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
        AlgorithmResult verdict = new AlgorithmResult(holds);
        verdict.metric("count", (double) cells.size());

        List<Score> holisticScores = new ArrayList<>();
        holisticScores.add(new Score(CONTRIBUTION_SHARE, topShare));

        AlgorithmExecution execution = new AlgorithmExecution(NAME, params, verdict);
        return new ArchetypeResult(execution, holisticScores, salient);
    }
}
