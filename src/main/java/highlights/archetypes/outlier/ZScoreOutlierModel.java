package highlights.archetypes.outlier;

import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import result.Cell;
import result.Result;

/**
 * The outlier archetype's in-house computation: over one measure of a result, it z-scores each cell against
 * the cells' own distribution and flags those whose absolute z-score exceeds a threshold. Self-contained so
 * the archetype derives its statistic from the raw cells — like the other data-driven archetypes
 * (marginal contribution, entropy) — rather than reaching into the {@code model.*} layer.
 */
final class ZScoreOutlierModel {

    /** The absolute z-score above which a value is treated as an outlier. */
    static final double ABS_ZSCORE_OUTLIER_THRESHOLD = 2.2;

    private final double[] zScores;
    private final double threshold;

    ZScoreOutlierModel(Result data, int measureIndex, double threshold) {
        this.threshold = threshold;
        List<Cell> cells = data.getCells();
        this.zScores = new double[cells.size()];

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Cell cell : cells) stats.addValue(cell.toDouble(measureIndex));
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();

        for (int i = 0; i < cells.size(); i++) {
            zScores[i] = std == 0.0 ? 0.0 : (cells.get(i).toDouble(measureIndex) - mean) / std;
        }
    }

    /** The z-score of the i-th cell (in {@code result.getCells()} order) over the analyzed measure. */
    double zScoreOf(int cellIndex) {
        return zScores[cellIndex];
    }

    /** Whether the i-th cell is an outlier: its absolute z-score exceeds the threshold. */
    boolean isOutlier(int cellIndex) {
        return Math.abs(zScores[cellIndex]) > threshold;
    }
}
