package result.highlights.archetypes.modality;

import result.highlights.OperatorResult;
import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.AlgorithmParams;
import result.highlights.metamodel.NamedScoreType;
import result.highlights.metamodel.ScoreType;
import result.highlights.instance.AlgorithmExecution;
import result.highlights.instance.AlgorithmResult;
import result.highlights.instance.ArchetypeResult;
import result.highlights.instance.Score;
import result.highlights.instance.ScoredFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import result.Cell;

/**
 * Tests the modality hypothesis as the concentration of the measure mass over the result cells: it takes
 * the cells' shares of the total as a probability distribution and measures its normalized Shannon entropy
 * {@code H / ln(n)}. Concentration is {@code 1 - normalizedEntropy}: 0 when the mass is spread uniformly,
 * approaching 1 when a few cells hold most of it. The hypothesis holds when concentration exceeds a
 * threshold. A dataset-level shape verdict — it produces no elementary highlights. Assumes non-negative
 * additive mass; a non-positive total is treated as unassessable (concentration 0).
 */
public final class NormalizedEntropyModalityAlgorithm implements Algorithm {

    private static final String NAME = "NormalizedEntropyModality";
    private static final double DEFAULT_CONCENTRATION_THRESHOLD = 0.5;

    /** How far the mass distribution departs from uniform: {@code 1 - H/ln(n)}, in [0, 1]. */
    public static final ScoreType CONCENTRATION = new NamedScoreType("Concentration");

    @Override
    public String name() { return NAME; }

    @Override
    public AlgorithmParams params() {
        return new AlgorithmParams().set("concentrationThreshold", DEFAULT_CONCENTRATION_THRESHOLD);
    }

    @Override
    public boolean appliesTo(OperatorResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public ArchetypeResult run(OperatorResult context, int measureIndex) {
        double threshold = params().get("concentrationThreshold", DEFAULT_CONCENTRATION_THRESHOLD);

        List<Cell> cells = context.data.getCells();
        int n = cells.size();
        double total = 0.0;
        for (Cell c : cells) total += c.toDouble(measureIndex);

        double concentration = 0.0;
        if (n >= 2 && total > 0.0) {
            double entropy = 0.0;
            for (Cell c : cells) {
                double v = c.toDouble(measureIndex);
                if (v > 0.0) {
                    double p = v / total;
                    entropy -= p * Math.log(p);
                }
            }
            double normalized = entropy / Math.log(n);
            concentration = 1.0 - normalized;
            if (concentration < 0.0) concentration = 0.0;
            if (concentration > 1.0) concentration = 1.0;
        }

        boolean holds = concentration > threshold;
        AlgorithmResult verdict = new AlgorithmResult(holds);
        verdict.metric("concentration", concentration).metric("count", (double) n);

        List<Score> holisticScores = new ArrayList<>();
        holisticScores.add(new Score(CONCENTRATION, concentration));

        AlgorithmExecution execution = new AlgorithmExecution(NAME, params(), verdict);
        return new ArchetypeResult(execution, holisticScores, Collections.<ScoredFinding>emptyList());
    }
}
