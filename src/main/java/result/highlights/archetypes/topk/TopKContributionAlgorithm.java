package result.highlights.archetypes.topk;

import result.highlights.OperatorResult;
import result.highlights.archetypes.megacontributor.MarginalContributionAlgorithm;
import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.AlgorithmParams;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.instance.ArchetypeResult;
import result.highlights.instance.Score;
import result.highlights.instance.ScoredFinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import result.Cell;

/**
 * Tests the top-k hypothesis as a marginal contribution: for each breakdown dimension (a group-by level
 * with more than one member), it sums the measure per member — marginalizing over the other dimensions —
 * ranks the members by contribution (absolute value) and surfaces the top k. Ranking is by value, not
 * share: within a breakdown the two order identically (share is value over a fixed total), and value stays
 * meaningful when the measure can be zero or negative. Share is reported alongside as a score.
 */
public final class TopKContributionAlgorithm implements Algorithm {

    private static final String NAME = "TopKContribution";
    private static final int DEFAULT_K = 3;

    private final ElementaryHighlightRole topContributorRole;

    public TopKContributionAlgorithm(ElementaryHighlightRole topContributorRole) {
        this.topContributorRole = topContributorRole;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public AlgorithmParams params() {
        return new AlgorithmParams().set("k", DEFAULT_K);
    }

    @Override
    public boolean appliesTo(OperatorResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public ArchetypeResult run(OperatorResult context, int measureIndex) {
        int k = (int) params().get("k", DEFAULT_K);

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

            List<Map.Entry<String, Double>> ranked = new ArrayList<>(marginal.entrySet());
            ranked.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            int limit = Math.min(k, ranked.size());
            for (int i = 0; i < limit; i++) {
                Map.Entry<String, Double> member = ranked.get(i);
                double share = total == 0.0 ? 0.0 : member.getValue() / total;
                if (share > topShare) topShare = share;
                List<Score> scores = new ArrayList<>();
                scores.add(new Score(MarginalContributionAlgorithm.CONTRIBUTION_SHARE, share));
                salient.add(ScoredFinding.marginal(d, member.getKey(), member.getValue(),
                        topContributorRole, scores));
            }
        }

        boolean holds = !salient.isEmpty();
        List<Score> holisticScores = new ArrayList<>();
        holisticScores.add(new Score(MarginalContributionAlgorithm.CONTRIBUTION_SHARE, topShare));
        return new ArchetypeResult(holds, holisticScores, salient)
                .metric("k", (double) k);
    }
}
