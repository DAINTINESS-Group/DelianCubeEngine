package model.decisiontree.node;

public class Predict {
    private final String prediction;
    private final double probability;

    public Predict(DecisionTreeNodeParams nodeParams) {
        this.prediction = findPrediction(nodeParams, nodeParams.getNode().predict().predict());
        this.probability = nodeParams.getNode().predict().prob();
    }

    public Predict(DecisionTreeNodeParams nodeParams,
                   org.apache.spark.mllib.tree.model.Predict predict) {
        this.prediction = findPrediction(nodeParams, predict.predict());
        this.probability = predict.predict();
    }

    private String findPrediction(DecisionTreeNodeParams nodeParams, double indexedValue) {
        return nodeParams
                .getIndexedToActualValuesForEachIndexedColumn()
                .get(nodeParams.getLabeledColumnName())
                .get(indexedValue);
    }

    public String getPrediction() {
        return prediction;
    }

    public double getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return "Predict = (prediction = " + prediction +
                ", probability = " + probability + ")";
    }
}
