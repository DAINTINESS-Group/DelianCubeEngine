package model.decisiontree.node;

import org.apache.spark.mllib.tree.model.Node;

import java.util.Map;
import java.util.List;

public class DecisionTreeNodeParams {

    private final Map<String, Map<Double, String>> indexedToActualValuesForEachIndexedColumn;
    private final String labeledColumnName;
    private final List<String> features;
    private final Node node;

    public DecisionTreeNodeParams(Map<String, Map<Double, String>> indexedToActualValuesForEachIndexedColumn,
                                  String labeledColumnName, List<String> features, Node node) {
        this.indexedToActualValuesForEachIndexedColumn = indexedToActualValuesForEachIndexedColumn;
        this.labeledColumnName = labeledColumnName;
        this.features = features;
        this.node = node;
    }

    public DecisionTreeNodeParams(DecisionTreeNodeParams nodeParams, Node node) {
        this.indexedToActualValuesForEachIndexedColumn = nodeParams.indexedToActualValuesForEachIndexedColumn;
        this.labeledColumnName = nodeParams.labeledColumnName;
        this.features = nodeParams.features;
        this.node = node;
    }

    public Map<String, Map<Double, String>> getIndexedToActualValuesForEachIndexedColumn() {
        return indexedToActualValuesForEachIndexedColumn;
    }

    public String getLabeledColumnName() {
        return labeledColumnName;
    }

    public List<String> getFeatures() {
        return features;
    }

    public Node getNode() {
        return node;
    }
}
