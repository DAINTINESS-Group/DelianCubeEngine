package intentional.model.archetypes.modality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cubemanager.cubebase.QueryMeasure;
import intentional.model.Model;
import intentional.result.LabeledResult;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import intentional.model.ParameterRole;
import intentional.model.ModelResultImpl;
import result.Cell;

/**
 * Tests the modality hypothesis as the concentration of the measure mass over the result cells: the cells'
 * shares of the total, read as a probability distribution, and their normalized Shannon entropy. Concentration
 * is {@code 1 - H/ln(n)}. Holds when concentration exceeds a threshold. Holistic-only.
 */
public final class NormalizedEntropyModalityAlgorithm implements Model {

    public static final String NAME = "NormalizedEntropyModality";

    public static final ParameterRole CONCENTRATION_THRESHOLD = new ParameterRole(
            "concentrationThreshold", "Concentration above which the mass counts as concentrated", 0.5);

    public static final String CONCENTRATION = "Concentration";

    @Override public String name() { return NAME; }

    @Override public List<ParameterRole> parameterRoles() {
        return Collections.singletonList(CONCENTRATION_THRESHOLD);
    }

    @Override
    public List<ModelResult> run(LabeledResult context) {
        List<ModelResult> out = new ArrayList<>();
        List<QueryMeasure> measures = context.measures();
        for (int index = 0; index < measures.size(); index++) {
            if (!measures.get(index).getAggregationFunction().additive) continue;
            out.add(runMeasure(context, index));
        }
        return out;
    }

    private ModelResult runMeasure(LabeledResult context, int measureIndex) {
        double threshold = CONCENTRATION_THRESHOLD.defaultValue;
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
            concentration = 1.0 - entropy / Math.log(n);
            if (concentration < 0.0) concentration = 0.0;
            if (concentration > 1.0) concentration = 1.0;
        }

        boolean holds = concentration > threshold;
        return new ModelResultImpl(NAME, holds, null,
                Collections.singletonList(ParameterInstantiation.ofDefault(CONCENTRATION_THRESHOLD)))
                .holistic(null, concentration).measure(context.measureName(measureIndex)).metric("count", (double) n);
    }
}
