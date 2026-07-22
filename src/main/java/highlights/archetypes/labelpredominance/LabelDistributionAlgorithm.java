package highlights.archetypes.labelpredominance;

import intentionaloperator.OperatorResult;
import labeling.DerivedMeasure;
import labeling.Labeling;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import highlights.instance.ArchetypeResult;
import highlights.instance.Score;
import highlights.instance.ScoredFinding;
import highlights.metamodel.Algorithm;
import highlights.metamodel.AlgorithmParams;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.NamedScoreType;
import highlights.metamodel.ScoreType;
import result.Cell;

/**
 * Tests the label-predominance hypothesis over any {@link Labeling} in the context: it summarizes the
 * distribution of the per-cell labels and holds when one label predominates (a majority). Salient cells are
 * surfaced as exemplars of the dominant label and exceptions off it; exceptions are ordered by their
 * distance from the dominant label when the domain is ordered, and by magnitude otherwise. Magnitude is a
 * {@link DerivedMeasure} when the labeling's model provides one, otherwise the studied measure. The model
 * or operator that produced the labeling stays out of view.
 */
public final class LabelDistributionAlgorithm implements Algorithm {

    private static final String NAME = "LabelDistribution";
    private static final int SALIENT_PER_GROUP = 3;

    /** A cell's label, valued by its rank in the labeling's ordered domain. */
    public static final ScoreType LABEL = new NamedScoreType("Label");
    /** The share of labeled cells carrying the predominant label. */
    public static final ScoreType DOMINANT_SHARE = new NamedScoreType("DominantShare");
    /** The magnitude by which a salient cell stands out. */
    public static final ScoreType MAGNITUDE = new NamedScoreType("Magnitude");

    private final ElementaryHighlightRole labeledCellRole;

    public LabelDistributionAlgorithm(ElementaryHighlightRole labeledCellRole) {
        this.labeledCellRole = labeledCellRole;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public AlgorithmParams params() { return new AlgorithmParams(); }

    @Override
    public boolean appliesTo(OperatorResult context) {
        return !context.labelings().isEmpty();
    }

    @Override
    public ArchetypeResult run(OperatorResult context, int labelingIndex) {
        Labeling labeling = context.labelings().get(labelingIndex);
        List<DerivedMeasure> derived = context.derivedMeasures();
        DerivedMeasure magnitude = labelingIndex < derived.size() ? derived.get(labelingIndex) : null;
        Map<Cell, String> labelByCell = labeling.assignment();

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String label : labeling.domain()) counts.put(label, 0);
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

        List<Score> holisticScores = new ArrayList<>();
        if (dominant != null) {
            holisticScores.add(new Score(LABEL, labeling.rankOf(dominant), dominant));
        }
        holisticScores.add(new Score(DOMINANT_SHARE, dominantShare));

        List<ScoredFinding> salient = selectSalient(labeling, dominant, magnitude);
        ArchetypeResult result = new ArchetypeResult(holds, holisticScores, salient);
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            result.metric("share_" + e.getKey(), total == 0 ? 0.0 : (double) e.getValue() / total);
        }
        return result;
    }

    /** The magnitude by which a cell stands out: the derived measure if present, else the studied measure. */
    private static double magnitudeOf(Cell cell, DerivedMeasure magnitude) {
        return Math.abs(magnitude != null ? magnitude.of(cell) : cell.toDouble(0));
    }

    private List<ScoredFinding> selectSalient(Labeling labeling, String dominant, DerivedMeasure magnitude) {
        Map<Cell, String> labelByCell = labeling.assignment();
        List<Cell> exemplars = new ArrayList<>();
        List<Cell> exceptions = new ArrayList<>();
        for (Map.Entry<Cell, String> e : labelByCell.entrySet()) {
            if (e.getValue() == null) continue;
            (e.getValue().equals(dominant) ? exemplars : exceptions).add(e.getKey());
        }

        exemplars.sort((a, b) -> Double.compare(magnitudeOf(b, magnitude), magnitudeOf(a, magnitude)));

        if (labeling.ordered()) {
            int dominantRank = labeling.rankOf(dominant);
            exceptions.sort((a, b) -> {
                int distA = Math.abs(labeling.rankOf(labelByCell.get(a)) - dominantRank);
                int distB = Math.abs(labeling.rankOf(labelByCell.get(b)) - dominantRank);
                if (distA != distB) return Integer.compare(distB, distA);
                return Double.compare(magnitudeOf(b, magnitude), magnitudeOf(a, magnitude));
            });
        } else {
            exceptions.sort((a, b) -> Double.compare(magnitudeOf(b, magnitude), magnitudeOf(a, magnitude)));
        }

        List<Cell> salient = new ArrayList<>();
        salient.addAll(exemplars.subList(0, Math.min(SALIENT_PER_GROUP, exemplars.size())));
        salient.addAll(exceptions.subList(0, Math.min(SALIENT_PER_GROUP, exceptions.size())));

        List<ScoredFinding> out = new ArrayList<>();
        for (Cell cell : salient) {
            String label = labelByCell.get(cell);
            List<Score> scores = new ArrayList<>();
            scores.add(new Score(LABEL, labeling.rankOf(label), label));
            if (magnitude != null) scores.add(new Score(MAGNITUDE, magnitude.of(cell)));
            out.add(ScoredFinding.ofCell(cell, cell.toDouble(0), labeledCellRole, scores));
        }
        return out;
    }
}
