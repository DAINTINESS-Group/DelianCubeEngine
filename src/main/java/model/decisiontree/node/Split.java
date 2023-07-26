package model.decisiontree.node;

import scala.collection.JavaConversions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Split {
    private final DecisionTreeNodeParams nodeParams;
    private String feature;
    private double threshold;
    private FeatureType featureType;
    private List<String> categories;

    public Split(DecisionTreeNodeParams nodeParams) {
        this.nodeParams = nodeParams;
        if (nodeParams.getNode().isLeaf())
            return;
        this.feature = findFeature();
        this.threshold = findThreshold();
        this.featureType = findFeatureType();
        this.categories = findCategories();
    }

    private String findFeature() {
        return nodeParams.getFeatures().get(nodeParams.getNode().split().get().feature());
    }

    private double findThreshold() {
        return nodeParams.getNode().split().get().threshold();
    }

    private FeatureType findFeatureType() {
        String featureType = nodeParams.getNode().split().get().featureType().toString();
        if (featureType.equals("Continuous"))
            return FeatureType.CONTINUOUS;
        else
            return FeatureType.CATEGORICAL;
    }

    private List<String> findCategories() {
        List<String> splitCategories = new ArrayList<>();

        if (featureType == FeatureType.CATEGORICAL) {

            List<Double> splitIndexedCategories = JavaConversions
                    .asJavaCollection(nodeParams.getNode().split().get().categories())
                    .stream()
                    .map(x -> (double) x)
                    .collect(Collectors.toList());
            for (Double indexedValue : splitIndexedCategories) {
                String actualValue = nodeParams
                        .getIndexedToActualValuesForEachIndexedColumn()
                        .get(feature)
                        .get(indexedValue);
                splitCategories.add(actualValue);
            }
        }
        return splitCategories;
    }

    public String getFeature() {
        return feature;
    }

    public double getThreshold() {
        return threshold;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        if (nodeParams.getNode().isLeaf())
            return "Split = None";
        return "Split = (" +
                "feature = " + feature  +
                ", threshold = " + threshold +
                ", featureType = " + featureType +
                ", categories = (" + String.join(", ", categories) +
                "))";
    }
}
