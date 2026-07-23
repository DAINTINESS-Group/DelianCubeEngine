package describe.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import labeling.LabelDomain;
import labeling.Labeling;
import labeling.LabelingModel;
import result.Cell;
import result.CellWrapper;
import result.Result;

/**
 * A {@link LabelingModel} that k-means-clusters the result's cells by their measure value and labels each
 * cell with the cluster it falls in. The clusters form an unordered categorical domain that the
 * {@code labelpredominance} archetype reads to test whether one cluster predominates.
 */
public final class KMeansLabelingModel implements LabelingModel {

    /** Identifies this model's output within an LabeledResult. */
    public static final String NAME = "KMeansApache";

    private static final int DEFAULT_NUM_CLUSTERS = 3;
    private static final int NUM_ITERATIONS = 10000;

    private final Result data;
    private final int numClusters;
    private Labeling clusterLabeling;

    public KMeansLabelingModel(Result data) {
        this(data, DEFAULT_NUM_CLUSTERS);
    }

    public KMeansLabelingModel(Result data, int numClusters) {
        this.data = data;
        this.numClusters = numClusters;
    }

    /** Clusters the cells and assigns each its cluster label; returns 0 if OK, -1 if there are no cells. */
    public int compute() {
        List<Cell> cells = data.getCells();
        if (cells.isEmpty()) {
            return -1;
        }
        int k = Math.min(numClusters, cells.size());

        List<CellWrapper> points = new ArrayList<>(cells.size());
        for (Cell cell : cells) points.add(new CellWrapper(cell));

        KMeansPlusPlusClusterer<CellWrapper> clusterer = new KMeansPlusPlusClusterer<>(k, NUM_ITERATIONS);
        List<CentroidCluster<CellWrapper>> clusters = clusterer.cluster(points);

        List<String> domainLabels = new ArrayList<>();
        Map<Cell, String> labelByCell = new LinkedHashMap<>();
        for (int i = 0; i < clusters.size(); i++) {
            String label = "Cluster " + i;
            domainLabels.add(label);
            for (CellWrapper point : clusters.get(i).getPoints()) {
                labelByCell.put(point.getCell(), label);
            }
        }
        this.clusterLabeling = new Labeling(new LabelDomain(domainLabels, false), labelByCell);
        return 0;
    }

    @Override
    public String getModelName() { return NAME; }

    @Override
    public List<Labeling> labelings() {
        return Collections.singletonList(clusterLabeling);
    }
}
