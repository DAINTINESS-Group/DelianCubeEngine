package highlights.archetypes.labelpredominance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import highlights.instance.AlgorithmExecution;
import highlights.instance.AlgorithmResult;
import highlights.instance.ExecutableAlgorithm;
import highlights.instance.ParameterInstantiation;
import highlights.instance.Score;
import highlights.instance.ScoredFinding;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.NamedScoreType;
import highlights.metamodel.ParameterRole;
import highlights.metamodel.ScoreType;
import intentional.result.LabeledResult;
import intentional.result.Labeling;
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
public final class LabelDistributionAlgorithm implements ExecutableAlgorithm {

    private static final String NAME = "LabelDistribution";
    private static final int SALIENT_PER_GROUP = 3;

    /** A cell's label, valued by its rank in the labeling's ordered domain. */
    public static final ScoreType LABEL = new NamedScoreType("Label");
    /** The share of labeled cells carrying the winning label. */
    public static final ScoreType DOMINANT_SHARE = new NamedScoreType("DominantShare");
    /** The {@link VotingRule} that ran the election, valued by its ordinal. */
    public static final ScoreType VOTING_RULE = new NamedScoreType("VotingRule");
    /** The magnitude by which a salient cell stands out. */
    public static final ScoreType MAGNITUDE = new NamedScoreType("Magnitude");

    private final ElementaryHighlightRole labeledCellRole;
    /** The imposed rule, or {@code null} to let each labeling get {@link VotingRule#defaultFor}. */
    private final VotingRule votingRule;

    public LabelDistributionAlgorithm(ElementaryHighlightRole labeledCellRole) {
        this(labeledCellRole, null);
    }

    public LabelDistributionAlgorithm(ElementaryHighlightRole labeledCellRole, VotingRule votingRule) {
        this.labeledCellRole = labeledCellRole;
        this.votingRule = votingRule;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public List<ParameterRole> parameterRoles() { return Collections.emptyList(); }

    @Override
    public boolean appliesTo(LabeledResult context) {
        return !context.labelings().isEmpty();
    }

    @Override
    public AlgorithmExecution run(LabeledResult context, int labelingIndex) {
        Labeling labeling = context.labelings().get(labelingIndex);
        Map<Cell, String> labelByCell = labeling.assignment();

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String label : labeling.domain()) counts.put(label, 0);
        int total = 0;
        for (String label : labelByCell.values()) {
            if (label == null) continue;
            counts.merge(label, 1, Integer::sum);
            total++;
        }

        VotingRule rule = votingRule != null ? votingRule : VotingRule.defaultFor(labeling.ordered());
        String winner = rule.elect(labeling.domain(), labeling.ordered(), counts, total);
        boolean holds = winner != null;
        double dominantShare = winner == null ? 0.0 : (double) counts.get(winner) / total;

        List<Score> holisticScores = new ArrayList<>();
        if (winner != null) {
            holisticScores.add(new Score(LABEL, labeling.rankOf(winner), winner));
        }
        holisticScores.add(new Score(DOMINANT_SHARE, dominantShare));
        holisticScores.add(new Score(VOTING_RULE, rule.ordinal(), rule.name()));

        List<ScoredFinding> salient = selectSalient(labeling, winner);
        AlgorithmResult result = new AlgorithmResult(holds);
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            result.metric("share_" + e.getKey(), total == 0 ? 0.0 : (double) e.getValue() / total);
        }
        return new AlgorithmExecution(this, Collections.<ParameterInstantiation>emptyList(), result,
                holisticScores, salient);
    }

    /** The magnitude by which a cell stands out: the labeling's own if present, else the studied measure. */
    private static double magnitudeOf(Cell cell, Labeling labeling) {
        double magnitude = labeling.magnitudeOf(cell);
        return Math.abs(Double.isNaN(magnitude) ? cell.toDouble(labeling.measureIndex()) : magnitude);
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
