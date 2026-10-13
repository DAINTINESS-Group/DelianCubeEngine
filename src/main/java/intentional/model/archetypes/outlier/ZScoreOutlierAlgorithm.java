package intentional.model.archetypes.outlier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cubemanager.cubebase.QueryMeasure;
import intentional.labeling.LabelDomain;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import intentional.model.Model;
import intentional.result.LabeledResult;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import intentional.model.ParameterRole;
import intentional.model.ModelResultImpl;
import result.Cell;

/**
 * Tests the outlier hypothesis over each measure: z-scores each cell against the cells' distribution and
 * labels it {@code outlier} when its absolute z-score exceeds a threshold, {@code normal} otherwise. The
 * per-cell magnitude is the z-score.
 */
public final class ZScoreOutlierAlgorithm implements Model {

    public static final String NAME = "ZScoreOutlier";
    public static final String NORMAL = "normal";
    public static final String OUTLIER = "outlier";

    public static final ParameterRole ABS_Z_THRESHOLD = new ParameterRole(
            "absZThreshold", "Absolute z-score above which a cell is an outlier",
            ZScoreOutlierModel.ABS_ZSCORE_OUTLIER_THRESHOLD);

    @Override public String name() { return NAME; }

    @Override public List<ParameterRole> parameterRoles() {
        return Collections.singletonList(ABS_Z_THRESHOLD);
    }

    @Override
    public LabeledResult run(LabeledResult context) {
        List<ModelResult> produced = new ArrayList<>();
        List<QueryMeasure> measures = context.measures();
        if (measures.isEmpty()) {
            produced.add(runMeasure(context, 0));
        } else {
            for (int index = 0; index < measures.size(); index++) produced.add(runMeasure(context, index));
        }
        context.addModels(produced);
        return context;
    }

    private ModelResult runMeasure(LabeledResult context, int measureIndex) {
        double threshold = ABS_Z_THRESHOLD.defaultValue;
        List<Cell> cells = context.data.getCells();
        ZScoreOutlierModel model = new ZScoreOutlierModel(context.data, measureIndex, threshold);

        Map<Cell, Double> zByCell = new LinkedHashMap<>();
        double maxAbsZ = 0.0;
        int outliers = 0;
        for (int i = 0; i < cells.size(); i++) {
            double z = model.zScoreOf(i);
            zByCell.put(cells.get(i), z);
            if (Math.abs(z) > maxAbsZ) maxAbsZ = Math.abs(z);
            if (model.isOutlier(i)) outliers++;
        }

        Labeling labelling = new Labeling(scheme(threshold), zByCell, measureIndex);
        boolean holds = outliers > 0;
        return new ModelResultImpl(NAME, holds, labelling,
                Collections.singletonList(ParameterInstantiation.ofDefault(ABS_Z_THRESHOLD)))
                .holistic(null, maxAbsZ).measure(context.measureName(measureIndex))
                .metric("outlierCount", (double) outliers);
    }

    private static LabelingScheme scheme(double threshold) {
        LabelDomain domain = new LabelDomain(Arrays.asList(NORMAL, OUTLIER), true);
        return new LabelingScheme() {
            @Override public String name() { return NAME; }
            @Override public String applyLabels(double z) { return Math.abs(z) > threshold ? OUTLIER : NORMAL; }
            @Override public LabelDomain domain() { return domain; }
        };
    }
}
