package assess;

import result.highlights.OperatorResult;
import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.AlgorithmParams;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.InterestingnessFacet;
import result.highlights.metamodel.LabelScoreType;
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
 * Tests the benchmark-tendency hypothesis: reads the {@link AssessModel}'s per-cell labels from the
 * {@link OperatorResult} and summarizes their distribution over the user's label domain. The holistic
 * holds when one label predominates (a majority).
 */
public final class LabelDistributionAlgorithm implements Algorithm {

    private static final String NAME = "LabelDistribution";
    private static final int SALIENT_PER_GROUP = 3;

    private final List<String> orderedLabels;
    private final LabelScoreType labelType;
    private final ElementaryHighlightRole assessedCellRole;

    public LabelDistributionAlgorithm(List<String> orderedLabels, ElementaryHighlightRole assessedCellRole) {
        this.orderedLabels = orderedLabels;
        this.labelType = new LabelScoreType("AssessmentLabel", orderedLabels);
        this.assessedCellRole = assessedCellRole;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public AlgorithmParams params() { return new AlgorithmParams(); }

    @Override
    public boolean appliesTo(OperatorResult context) {
        return context.model(AssessModel.NAME) instanceof AssessModel;
    }

    @Override
    public ArchetypeResult run(OperatorResult context) {
        AssessModel assess = (AssessModel) context.model(AssessModel.NAME);
        Map<Cell, String> labelByCell = assess.getLabels();

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String label : orderedLabels) counts.put(label, 0);
        int total = 0;
        for (String label : labelByCell.values()) {
            if (label == null) continue;
            counts.merge(label, 1, Integer::sum);
            total++;
        }

        String dominant = null;
        int dominantCount = 0;
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() > dominantCount) { dominantCount = e.getValue(); dominant = e.getKey(); }
        }
        double dominantShare = total == 0 ? 0.0 : (double) dominantCount / total;
        boolean holds = dominantShare > 0.5;

        AlgorithmResult verdict = new AlgorithmResult(holds);
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            verdict.metric("share_" + e.getKey(), total == 0 ? 0.0 : (double) e.getValue() / total);
        }
        verdict.metric("dominantShare", dominantShare);

        List<Score> holisticScores = new ArrayList<>();
        if (dominant != null) {
            holisticScores.add(new Score(labelType, labelType.rankOf(dominant), dominant));
        }
        holisticScores.add(new Score(InterestingnessFacet.PECULIARITY, dominantShare));

        AlgorithmExecution execution = new AlgorithmExecution(NAME, params(), verdict);
        return new ArchetypeResult(execution, holisticScores, selectSalient(assess, labelByCell, dominant));
    }

    private List<ScoredFinding> selectSalient(AssessModel assess, Map<Cell, String> labelByCell, String dominant) {
        List<Cell> exemplars = new ArrayList<>();
        List<Cell> exceptions = new ArrayList<>();
        for (Map.Entry<Cell, String> e : labelByCell.entrySet()) {
            if (e.getValue() == null) continue;
            (e.getValue().equals(dominant) ? exemplars : exceptions).add(e.getKey());
        }

        exemplars.sort((a, b) -> Double.compare(Math.abs(assess.deltaOf(b)), Math.abs(assess.deltaOf(a))));

        int dominantRank = labelType.rankOf(dominant);
        exceptions.sort((a, b) -> {
            int distA = Math.abs(labelType.rankOf(labelByCell.get(a)) - dominantRank);
            int distB = Math.abs(labelType.rankOf(labelByCell.get(b)) - dominantRank);
            if (distA != distB) return Integer.compare(distB, distA);
            return Double.compare(Math.abs(assess.deltaOf(b)), Math.abs(assess.deltaOf(a)));
        });

        List<Cell> salient = new ArrayList<>();
        salient.addAll(exemplars.subList(0, Math.min(SALIENT_PER_GROUP, exemplars.size())));
        salient.addAll(exceptions.subList(0, Math.min(SALIENT_PER_GROUP, exceptions.size())));

        List<ScoredFinding> out = new ArrayList<>();
        for (Cell cell : salient) {
            String label = labelByCell.get(cell);
            List<Score> scores = new ArrayList<>();
            scores.add(new Score(labelType, labelType.rankOf(label), label));
            scores.add(new Score(InterestingnessFacet.PECULIARITY, assess.deltaOf(cell)));
            out.add(ScoredFinding.ofCell(cell, assessedCellRole, scores));
        }
        return out;
    }
}
