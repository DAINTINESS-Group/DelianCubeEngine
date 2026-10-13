package intentional.labeling.schemes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

public class KMeansSchemeTest {

    private static final double EPSILON = 1e-9;

    /** The within-cluster sum of squares of the values, grouped by the label the fitted scheme gives them. */
    private static double labeledSquares(KMeansScheme scheme, List<Double> values) {
        Map<String, List<Double>> byLabel = new HashMap<>();
        for (double value : values) {
            byLabel.computeIfAbsent(scheme.applyLabels(value), l -> new ArrayList<>()).add(value);
        }
        double total = 0.0;
        for (List<Double> cluster : byLabel.values()) {
            double mean = cluster.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            for (double value : cluster) total += (value - mean) * (value - mean);
        }
        return total;
    }

    /** The least within-cluster sum of squares over every split of the sorted values into k contiguous runs. */
    private static double bruteForceSquares(List<Double> values, int k) {
        double[] sorted = values.stream().mapToDouble(Double::doubleValue).sorted().toArray();
        return bestSplit(sorted, 0, k);
    }

    private static double bestSplit(double[] sorted, int from, int clusters) {
        if (clusters == 1) {
            return squares(sorted, from, sorted.length);
        }
        double best = Double.POSITIVE_INFINITY;
        for (int end = from + 1; end <= sorted.length - (clusters - 1); end++) {
            best = Math.min(best, squares(sorted, from, end) + bestSplit(sorted, end, clusters - 1));
        }
        return best;
    }

    private static double squares(double[] sorted, int from, int to) {
        double mean = 0.0;
        for (int i = from; i < to; i++) mean += sorted[i];
        mean /= (to - from);
        double total = 0.0;
        for (int i = from; i < to; i++) total += (sorted[i] - mean) * (sorted[i] - mean);
        return total;
    }

    @Test
    public void separatesTheLowValuesFromTheOutliers() {
        KMeansScheme scheme = new KMeansScheme(3);
        scheme.fit(Arrays.asList(1.0, 2.0, 1.0, 2.0, 100.0, 1000.0));

        assertEquals("Cluster 0", scheme.applyLabels(1.0));
        assertEquals("Cluster 0", scheme.applyLabels(2.0));
        assertEquals("Cluster 1", scheme.applyLabels(100.0));
        assertEquals("Cluster 2", scheme.applyLabels(1000.0));
    }

    @Test
    public void matchesTheBruteForceOptimumOnRandomValues() {
        Random random = new Random(7);
        for (int trial = 0; trial < 200; trial++) {
            int n = 3 + random.nextInt(8);
            List<Double> values = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                values.add(Math.round(random.nextGaussian() * 100) / 10.0);
            }
            int distinct = (int) values.stream().distinct().count();
            int k = 1 + random.nextInt(Math.min(4, distinct));
            KMeansScheme scheme = new KMeansScheme(k);
            scheme.fit(values);

            assertEquals("trial " + trial + " " + values,
                    bruteForceSquares(values, k), labeledSquares(scheme, values), EPSILON);
        }
    }

    @Test
    public void theSameValuesAlwaysGiveTheSameLabels() {
        List<Double> values = Arrays.asList(5.0, -3.0, 12.0, 0.5, 7.5, -8.0, 30.0, 2.0);
        List<Double> shuffled = new ArrayList<>(values);
        Collections.shuffle(shuffled, new Random(3));

        KMeansScheme first = new KMeansScheme(3);
        first.fit(values);
        KMeansScheme second = new KMeansScheme(3);
        second.fit(shuffled);

        for (double value : values) {
            assertEquals(first.applyLabels(value), second.applyLabels(value));
        }
    }

    @Test
    public void clustersAreOrderedByCentroid() {
        KMeansScheme scheme = new KMeansScheme(3);
        scheme.fit(Arrays.asList(50.0, 51.0, -20.0, -21.0, 5.0, 6.0));

        assertTrue(scheme.domain().ordered());
        assertEquals(Arrays.asList("Cluster 0", "Cluster 1", "Cluster 2"), scheme.domain().labels());
        assertEquals("Cluster 0", scheme.applyLabels(-20.0));
        assertEquals("Cluster 1", scheme.applyLabels(5.0));
        assertEquals("Cluster 2", scheme.applyLabels(51.0));
    }

    @Test
    public void fewerDistinctValuesThanClustersAreAnError() {
        assertThrows(IllegalStateException.class,
                () -> new KMeansScheme(3).fit(Arrays.asList(3.0, 3.0, 9.0, 9.0, 9.0)));
        assertThrows(IllegalStateException.class, () -> new KMeansScheme().fit(Collections.<Double>emptyList()));
    }

    @Test
    public void theDomainIsTheKClustersWhateverTheValues() {
        KMeansScheme scheme = new KMeansScheme(4);
        assertEquals(Arrays.asList("Cluster 0", "Cluster 1", "Cluster 2", "Cluster 3"), scheme.domain().labels());

        scheme.fit(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0));
        assertEquals(Arrays.asList("Cluster 0", "Cluster 1", "Cluster 2", "Cluster 3"), scheme.domain().labels());
    }
}
