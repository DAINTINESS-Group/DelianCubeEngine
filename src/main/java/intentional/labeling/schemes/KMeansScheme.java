package intentional.labeling.schemes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import intentional.labeling.LabelDomain;
import intentional.labeling.LabelingScheme;

/**
 * A data-driven {@link LabelingScheme} that labels each value with its nearest centroid under exact
 * one-dimensional k-means. Clusters are ordered by centroid; fitting needs at least k distinct values.
 */
public class KMeansScheme implements LabelingScheme {

    /** Identifies this scheme's labelings within a result. */
    public static final String NAME = "KMeansApache";

    private static final int DEFAULT_NUM_CLUSTERS = 3;

    private final int numClusters;
    private final LabelDomain domain;
    private double[] centroids;

    public KMeansScheme() {
        this(DEFAULT_NUM_CLUSTERS);
    }

    public KMeansScheme(int numClusters) {
        if (numClusters < 1) {
            throw new IllegalArgumentException("k-means needs at least one cluster, got " + numClusters);
        }
        this.numClusters = numClusters;
        List<String> labels = new ArrayList<>(numClusters);
        for (int c = 0; c < numClusters; c++) {
            labels.add("Cluster " + c);
        }
        this.domain = new LabelDomain(labels, true);
    }

    @Override
    public void fit(Collection<Double> values) {
        double[] sorted = values.stream().mapToDouble(Double::doubleValue).sorted().toArray();
        int distinct = sorted.length == 0 ? 0 : distinctCount(sorted);
        if (distinct < numClusters) {
            throw new IllegalStateException(String.format(
                    "%s(%d) needs at least %d distinct values to cluster; got %d",
                    NAME, numClusters, numClusters, distinct));
        }
        int[] starts = optimalClusterStarts(sorted, numClusters);

        this.centroids = new double[numClusters];
        for (int c = 0; c < numClusters; c++) {
            int end = c + 1 < numClusters ? starts[c + 1] : sorted.length;
            double sum = 0.0;
            for (int i = starts[c]; i < end; i++) sum += sorted[i];
            centroids[c] = sum / (end - starts[c]);
        }
    }

    @Override
    public String name() { return NAME; }

    @Override
    public String applyLabels(double value) {
        if (centroids == null) {
            throw new IllegalStateException(
                    "The scheme labels by the clusters of the value distribution; fit it before labeling");
        }
        int nearest = 0;
        for (int i = 1; i < centroids.length; i++) {
            if (Math.abs(value - centroids[i]) < Math.abs(value - centroids[nearest])) nearest = i;
        }
        return "Cluster " + nearest;
    }

    @Override
    public LabelDomain domain() {
        return domain;
    }

    private static int distinctCount(double[] sorted) {
        int distinct = 1;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] != sorted[i - 1]) distinct++;
        }
        return distinct;
    }

    /**
     * The first index of each cluster in the optimal partition. cost[m][i]: least sum of squares of values
     * 0..i in m + 1 clusters; start[m][i]: first index of the last one.
     */
    private static int[] optimalClusterStarts(double[] sorted, int k) {
        int n = sorted.length;
        double[] sums = new double[n + 1];
        double[] squares = new double[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + sorted[i];
            squares[i + 1] = squares[i] + sorted[i] * sorted[i];
        }
        double[][] cost = new double[k][n];
        int[][] start = new int[k][n];
        for (int i = 0; i < n; i++) {
            cost[0][i] = withinSquares(sums, squares, 0, i);
        }
        for (int m = 1; m < k; m++) {
            fillRow(cost, start, sums, squares, m, m, n - 1, m, n - 1);
        }

        int[] starts = new int[k];
        int last = n - 1;
        for (int m = k - 1; m >= 0; m--) {
            starts[m] = m == 0 ? 0 : start[m][last];
            last = starts[m] - 1;
        }
        return starts;
    }

    /** Fills cost[m][from..to]; the best start never decreases with i, so it is searched in [low, high]. */
    private static void fillRow(double[][] cost, int[][] start, double[] sums, double[] squares,
                                int m, int from, int to, int low, int high) {
        if (from > to) {
            return;
        }
        int mid = (from + to) >>> 1;
        double best = Double.POSITIVE_INFINITY;
        int bestStart = Math.max(m, low);
        for (int j = Math.max(m, low); j <= Math.min(mid, high); j++) {
            double candidate = cost[m - 1][j - 1] + withinSquares(sums, squares, j, mid);
            if (candidate < best) {
                best = candidate;
                bestStart = j;
            }
        }
        cost[m][mid] = best;
        start[m][mid] = bestStart;
        fillRow(cost, start, sums, squares, m, from, mid - 1, low, bestStart);
        fillRow(cost, start, sums, squares, m, mid + 1, to, bestStart, high);
    }

    /** Sum of squared deviations of sorted values j..i. */
    private static double withinSquares(double[] sums, double[] squares, int j, int i) {
        int count = i - j + 1;
        double sum = sums[i + 1] - sums[j];
        return Math.max(0.0, squares[i + 1] - squares[j] - sum * sum / count);
    }
}
