package model.decisiontree;

import java.util.List;
import java.util.stream.Collectors;

import model.decisiontree.node.DecisionTreeNode;

public class DecisionTree {
    private final double accuracy;
    private final List<String> featureColumnNames;
    private final List<String> nonGeneratorAttributes;
    private final DecisionTreeNode rootNode;
    private final double averageImpurity;
    private final List<DecisionTreePath> paths;
    private String outputPath;

    public DecisionTree(double accuracy,
                        List<String> featureColumnNames,
                        List<String> nonGeneratorAttributes,
                        DecisionTreeNode rootNode,
                        double averageImpurity,
                        List<DecisionTreePath> paths
                        ) {
        this.accuracy = accuracy;
        this.featureColumnNames = featureColumnNames;
        this.nonGeneratorAttributes = nonGeneratorAttributes;
        this.rootNode = rootNode;
        this.averageImpurity = averageImpurity;
        this.paths = paths;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public List<String> getFeatureColumnNames() {
        return featureColumnNames;
    }

    public List<String> getNonGeneratorAttributes() {
        return nonGeneratorAttributes;
    }

    public DecisionTreeNode getRootNode() {
        return rootNode;
    }

    public double getAverageImpurity() {
        return averageImpurity;
    }

    public List<DecisionTreePath> getPaths() {
        return paths;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public String toString() {
        String allPaths = paths.stream()
                .map(DecisionTreePath::toString)
                .collect(Collectors.joining("\n"));

        return "DecisionTree info: "
                + "\n"
                + "featureColumnNames: "
                + String.join(", ", featureColumnNames)
                + "\n"
                + "accuracy: "
                + accuracy
                + "\n"
                + "Non generator columns: "
                + String.join(", ", nonGeneratorAttributes)
                + "\n"
                + "Average impurity: "
                + averageImpurity
                + "\n"
                + "Paths: "
                + "\n"
                + allPaths
                + "\n";
    }
}
