package model.decisiontree.generator;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import model.decisiontree.input.DecisionTreeParams;

public class DecisionTreeGeneratorFactory {
    private final DecisionTreeParams decisionTreeParams;
    private final Dataset<Row> dataset;

    public DecisionTreeGeneratorFactory(DecisionTreeParams decisionTreeParams, Dataset<Row> dataset) {
        this.decisionTreeParams = decisionTreeParams;
        this.dataset = dataset;
    }

    public IDecisionTreeGenerator getDefaultGenerator() {
        return new DecisionTreeGenerator(decisionTreeParams, dataset);
    }
}
