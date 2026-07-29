package intentional.labeling.schemes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import intentional.labeling.LabelDomain;
import intentional.labeling.LabelingScheme;

/**
 * A data-driven {@link LabelingScheme} that k-means-clusters the values and labels each value with the
 * cluster it falls in — an unordered categorical domain of one label per cluster. Must be
 * {@link #fit(Collection) fitted} before it labels.
 */
public class KMeansScheme implements LabelingScheme {

    /** Identifies this scheme's labelings within a result. */
    public static final String NAME = "KMeansApache";

    private static final int DEFAULT_NUM_CLUSTERS = 3;
    private static final int NUM_ITERATIONS = 10000;

    private final int numClusters;
    private double[] centroids;
    private LabelDomain domain;

    public KMeansScheme() {
        this(DEFAULT_NUM_CLUSTERS);
    }

    public KMeansScheme(int numClusters) {
        this.numClusters = numClusters;
    }

    /** Clusters the values; the labels are the clusters found. */
    @Override
    public void fit(Collection<Double> values) {
        int k = Math.min(numClusters, values.size());
        List<DoublePoint> points = new ArrayList<>(values.size());
        for (double value : values) points.add(new DoublePoint(new double[]{value}));

        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(k, NUM_ITERATIONS);
        List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(points);

        this.centroids = new double[clusters.size()];
        List<String> labels = new ArrayList<>(clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            centroids[i] = clusters.get(i).getCenter().getPoint()[0];
            labels.add("Cluster " + i);
        }
        this.domain = new LabelDomain(labels, false);
    }

    @Override
    public String name() { return NAME; }

    /** The label of the cluster whose centroid is nearest to the value. */
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
        if (domain == null) {
            throw new IllegalStateException(
                    "The scheme's domain is the clusters found; fit it before reading the domain");
        }
        return domain;
    }
}
