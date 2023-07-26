package model.decisiontree.generator;

import model.decisiontree.datapreparation.DecisionTreeDataProcessor;
import model.decisiontree.datapreparation.AttributesFinder;
import model.decisiontree.datapreparation.DecisionTreePathsFinder;
import model.decisiontree.DecisionTree;
import model.decisiontree.node.DecisionTreeNode;
import model.decisiontree.node.DecisionTreeNodeParams;
import model.decisiontree.input.DecisionTreeParams;

import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;


import java.util.ArrayList;
import java.util.List;

public class DecisionTreeGenerator implements IDecisionTreeGenerator {

    private final Dataset<Row> dataset;
    private final DecisionTreeParams decisionTreeParams;
    private AttributesFinder attributesFinder;
    private DecisionTreeDataProcessor decisionTreeDataProcessor;
    private DecisionTreeClassificationModel model;
    private double accuracy;

    public DecisionTreeGenerator(DecisionTreeParams decisionTreeParams, Dataset<Row> dataset) {
        this.decisionTreeParams = decisionTreeParams;
        this.dataset = dataset;
    }

    public DecisionTree computeDecisionTree() {
        initializeVariables();
        trainDecisionTreeModel();
        calculateAccuracy();
        return createDecisionTree();
    }

    private void initializeVariables() {
        attributesFinder = new AttributesFinder(decisionTreeParams, dataset);
        decisionTreeDataProcessor = new DecisionTreeDataProcessor(decisionTreeParams, attributesFinder, dataset);
    }

    private void trainDecisionTreeModel() {
        DecisionTreeClassifier decisionTreeClassifier = new DecisionTreeClassifier()
                .setLabelCol(decisionTreeParams.getLabeledColumnName() + "_indexed")
                .setImpurity(decisionTreeParams.getImpurity())
                .setMaxDepth(decisionTreeParams.getMaxDepth())
                .setMinInfoGain(decisionTreeParams.getMinInfoGain());
        model = decisionTreeClassifier.fit(decisionTreeDataProcessor.getTrainingData());
    }

    private void calculateAccuracy() {
        Dataset<Row> predictions = model.transform(decisionTreeDataProcessor.getTestData());
        MulticlassClassificationEvaluator evaluator =
                new MulticlassClassificationEvaluator()
                        .setLabelCol(decisionTreeParams.getLabeledColumnName() + "_indexed")
                        .setPredictionCol("prediction")
                        .setMetricName("accuracy");
        accuracy = evaluator.evaluate(predictions);
    }

    private DecisionTree createDecisionTree() {
        DecisionTreeNode rootNode = createDecisionTreeRootNode();
        return new DecisionTree(
                accuracy,
                attributesFinder.getAllFeatures(),
                attributesFinder.getNonGeneratingAttributes(),
                rootNode,
                calculateAverageImpurity(rootNode),
                new DecisionTreePathsFinder(rootNode).getPaths());
    }

    private DecisionTreeNode createDecisionTreeRootNode() {
        DecisionTreeNodeParams nodeParams = new DecisionTreeNodeParams(
                decisionTreeDataProcessor.getIndexedToActualValuesForEachIndexedColumn(),
                decisionTreeParams.getLabeledColumnName(),
                attributesFinder.getAllFeatures(),
                model.toOld().topNode());
        return new DecisionTreeNode(nodeParams);
    }

    private double calculateAverageImpurity(DecisionTreeNode rootNode) {
        List<Double> impurities = traverseNodesDFS(new ArrayList<>(), rootNode);
        double impuritySum = impurities.stream().mapToDouble(x -> x).sum();
        return impuritySum / impurities.size();
    }

    private List<Double> traverseNodesDFS(List<Double> impurities, DecisionTreeNode dtNode) {
        if (dtNode.isLeaf()) {
            impurities.add(dtNode.getImpurity());
            return impurities;
        }
        traverseNodesDFS(impurities, dtNode.getLeftNode());
        traverseNodesDFS(impurities, dtNode.getRightNode());
        return impurities;
    }
}
