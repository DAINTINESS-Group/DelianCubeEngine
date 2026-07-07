package result.highlights.archetypes.outlier;

import result.highlights.OperatorResult;
import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.AlgorithmParams;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.NamedScoreType;
import result.highlights.metamodel.ScoreType;
import result.highlights.instance.AlgorithmExecution;
import result.highlights.instance.AlgorithmResult;
import result.highlights.instance.ArchetypeResult;
import result.highlights.instance.Score;
import result.highlights.instance.ScoredFinding;

import java.util.ArrayList;
import java.util.List;

import model.outlier.OutlierModel;
import result.Cell;

/**
 * Tests the outlier hypothesis over a single measure: builds a config-free {@link OutlierModel} over the
 * result's cells for the given measure, and surfaces every cell whose z-score exceeds the model's
 * threshold as an elementary highlight. Because the outlier verdict is per measure, the model is built for
 * the {@code measureIndex} the extractor hands it — one holistic per query measure falls out of the
 * extractor's own measure loop.
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
        return new AlgorithmParams().set("absZThreshold", OutlierModel.ABS_ZSCORE_OUTLIER_THRESHOLD);
    }

    @Override
    public boolean appliesTo(OperatorResult context) {
        return context.data != null && !context.data.getCells().isEmpty();
    }

    @Override
    public ArchetypeResult run(OperatorResult context, int measureIndex) {
        double threshold = params().get("absZThreshold", OutlierModel.ABS_ZSCORE_OUTLIER_THRESHOLD);
        OutlierModel model = new OutlierModel(context.data, measureIndex, threshold);
        model.compute();

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
        AlgorithmResult verdict = new AlgorithmResult(holds);
        verdict.metric("outlierCount", (double) salient.size()).metric("maxAbsZ", maxAbsZ);

        List<Score> holisticScores = new ArrayList<>();
        holisticScores.add(new Score(ZSCORE, maxAbsZ));

        AlgorithmExecution execution = new AlgorithmExecution(NAME, params(), verdict);
        return new ArchetypeResult(execution, holisticScores, salient);
    }
}
