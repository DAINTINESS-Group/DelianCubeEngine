package highlights.archetypes.modality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import highlights.instance.AlgorithmExecution;
import highlights.instance.AlgorithmResult;
import highlights.instance.ExecutableAlgorithm;
import highlights.instance.ParameterInstantiation;
import highlights.instance.Score;
import highlights.instance.ScoredFinding;
import highlights.metamodel.NamedScoreType;
import highlights.metamodel.ParameterRole;
import highlights.metamodel.ScoreType;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * Tests the modality hypothesis as the concentration of the measure mass over the result cells: it takes
 * the cells' shares of the total as a probability distribution and measures its normalized Shannon entropy
 * {@code H / ln(n)}. Concentration is {@code 1 - normalizedEntropy}: 0 when the mass is spread uniformly,
 * approaching 1 when a few cells hold most of it. The hypothesis holds when concentration exceeds a
 * threshold. A dataset-level shape verdict — it produces no elementary highlights. Assumes non-negative
 * additive mass; a non-positive total is treated as unassessable (concentration 0).
 */
public final class NormalizedEntropyModalityAlgorithm implements ExecutableAlgorithm {

    private static final String NAME = "NormalizedEntropyModality";

    /** The concentration above which the mass counts as concentrated rather than spread. */
    public static final ParameterRole CONCENTRATION_THRESHOLD = new ParameterRole(
            "concentrationThreshold", "Concentration above which the mass counts as concentrated", 0.5);

    /** How far the mass distribution departs from uniform: {@code 1 - H/ln(n)}, in [0, 1]. */
    public static final ScoreType CONCENTRATION = new NamedScoreType("Concentration");

    @Override
    public String name() { return NAME; }

    @Override
    public List<ParameterRole> parameterRoles() {
        return Collections.singletonList(CONCENTRATION_THRESHOLD);
    }

    @Override
    public boolean appliesTo(LabeledResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public AlgorithmExecution run(LabeledResult context, int measureIndex) {
        ParameterInstantiation concentrationThreshold = ParameterInstantiation.ofDefault(CONCENTRATION_THRESHOLD);
        double threshold = concentrationThreshold.value;

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
        List<Score> holisticScores = new ArrayList<>();
        holisticScores.add(new Score(CONCENTRATION, concentration));
        AlgorithmResult result = new AlgorithmResult(holds).metric("count", (double) n);
        return new AlgorithmExecution(this, Collections.singletonList(concentrationThreshold), result,
                holisticScores, Collections.<ScoredFinding>emptyList());
    }
}
