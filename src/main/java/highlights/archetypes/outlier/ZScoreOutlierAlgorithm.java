package highlights.archetypes.outlier;

import labeling.LabeledResult;

import java.util.ArrayList;
import java.util.List;

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
 * Tests the outlier hypothesis over a single measure: builds a config-free {@link ZScoreOutlierModel} over
 * the result's cells for the given measure, and surfaces every cell whose z-score exceeds the model's
 * threshold as an elementary highlight. Because the outlier verdict is per measure, the model is built for
 * the {@code measureIndex} the extractor hands it — one holistic per query measure falls out of the
 * extractor's own measure loop. The computation is the archetype's own, kept out of the {@code model.*}
 * layer so no operator need run a model for outliers to be found.
 */
public final class ZScoreOutlierAlgorithm implements Algorithm {

    private static final String NAME = "ZScoreOutlier";

    /** A cell's z-score over the analyzed measure's distribution. */
    public static final ScoreType ZSCORE = new NamedScoreType("ZScore");

    private final ElementaryHighlightRole outlierRole;

    public ZScoreOutlierAlgorithm(ElementaryHighlightRole outlierRole) {
        this.outlierRole = outlierRole;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public AlgorithmParams params() {
        return new AlgorithmParams().set("absZThreshold", ZScoreOutlierModel.ABS_ZSCORE_OUTLIER_THRESHOLD);
    }

    @Override
    public boolean appliesTo(LabeledResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public ArchetypeResult run(LabeledResult context, int measureIndex) {
        double threshold = params().get("absZThreshold", ZScoreOutlierModel.ABS_ZSCORE_OUTLIER_THRESHOLD);
        ZScoreOutlierModel model = new ZScoreOutlierModel(context.data, measureIndex, threshold);

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
        return new ArchetypeResult(holds, holisticScores, salient)
                .metric("outlierCount", (double) salient.size());
    }
}
