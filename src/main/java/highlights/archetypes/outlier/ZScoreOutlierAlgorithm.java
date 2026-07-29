package highlights.archetypes.outlier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import highlights.instance.AlgorithmExecution;
import highlights.instance.AlgorithmResult;
import highlights.instance.MeasureAlgorithm;
import highlights.instance.ParameterInstantiation;
import highlights.instance.Score;
import highlights.instance.ScoredFinding;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.NamedScoreType;
import highlights.metamodel.ParameterRole;
import highlights.metamodel.ScoreType;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * Tests the outlier hypothesis over a single measure: builds a config-free {@link ZScoreOutlierModel} over
 * the result's cells for the given measure, and surfaces every cell whose z-score exceeds the model's
 * threshold as an elementary highlight. Because the outlier verdict is per measure, the model is built for
 * the {@code measureIndex} the extractor hands it — one holistic per query measure falls out of the
 * extractor's own measure loop. The computation is the archetype's own, kept out of the {@code model.*}
 * layer so no operator need run a model for outliers to be found.
 */
public final class ZScoreOutlierAlgorithm implements MeasureAlgorithm {

    private static final String NAME = "ZScoreOutlier";

    /** The absolute z-score above which a cell is flagged as an outlier. */
    public static final ParameterRole ABS_Z_THRESHOLD = new ParameterRole(
            "absZThreshold", "Absolute z-score above which a cell is an outlier",
            ZScoreOutlierModel.ABS_ZSCORE_OUTLIER_THRESHOLD);

    /** A cell's z-score over the analyzed measure's distribution. */
    public static final ScoreType ZSCORE = new NamedScoreType("ZScore");

    private final ElementaryHighlightRole outlierRole;

    public ZScoreOutlierAlgorithm(ElementaryHighlightRole outlierRole) {
        this.outlierRole = outlierRole;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public List<ParameterRole> parameterRoles() {
        return Collections.singletonList(ABS_Z_THRESHOLD);
    }

    @Override
    public boolean appliesTo(LabeledResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public AlgorithmExecution run(LabeledResult context, int measureIndex) {
        ParameterInstantiation zThreshold = ParameterInstantiation.ofDefault(ABS_Z_THRESHOLD);
        ZScoreOutlierModel model = new ZScoreOutlierModel(context.data, measureIndex, zThreshold.value);

        List<Cell> cells = context.data.getCells();
        double maxAbsZ = 0.0;
        List<ScoredFinding> salient = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            double z = model.zScoreOf(i);
            if (Math.abs(z) > maxAbsZ) maxAbsZ = Math.abs(z);
            if (model.isOutlier(i)) {
                List<Score> scores = new ArrayList<>();
                scores.add(new Score(ZSCORE, z));
                salient.add(ScoredFinding.ofCell(cells.get(i), cells.get(i).toDouble(measureIndex), outlierRole, scores));
            }
        }

        boolean holds = !salient.isEmpty();
        List<Score> holisticScores = new ArrayList<>();
        holisticScores.add(new Score(ZSCORE, maxAbsZ));
        AlgorithmResult result = new AlgorithmResult(holds).metric("outlierCount", (double) salient.size());
        return new AlgorithmExecution(this, Collections.singletonList(zThreshold), result, holisticScores, salient);
    }
}
