package model.decisiontree.datapreparation;

import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import model.decisiontree.input.DecisionTreeParams;

import java.util.*;


public class DecisionTreeDataProcessor {

    private final AttributesFinder attributesFinder;
    private final String labeledColumnName;
    private Dataset<Row> dataset;
    private final Dataset<Row> trainingData;
    private final Dataset<Row> testData;

    public DecisionTreeDataProcessor(DecisionTreeParams decisionTreeParams, AttributesFinder attributesFinder,
                                     Dataset<Row> dataset) {
        this.attributesFinder = attributesFinder;
        this.labeledColumnName = decisionTreeParams.getLabeledColumnName();
        this.dataset = dataset;
        // compute data
        Dataset<Row>[] splits = getInputData()
                .randomSplit(decisionTreeParams.getTrainingToTestDataSplitRatio());
        this.trainingData = splits[0];
        this.testData = splits[1];
    }

    public Dataset<Row> getTrainingData() {
        return trainingData;
    }

    public Dataset<Row> getTestData() {
        return testData;
    }

    private Dataset<Row> getInputData() {
        indexCategoricalFeaturesInTheDataset();

        return new VectorAssembler()
                .setHandleInvalid("skip")
                .setInputCols(attributesFinder.getAllFeaturesIndexed()
                        .toArray(new String[0]))
                .setOutputCol("features")
                .transform(dataset)
                .select("features", labeledColumnName + "_indexed");
    }

    private void indexCategoricalFeaturesInTheDataset() {
        // index all categorical features
        for (String categoricalColumn : attributesFinder.getCategoricalFeatures()) {
            dataset = indexColumn(categoricalColumn);
        }
        // index labeled field
        dataset = indexColumn(labeledColumnName);
    }

    private Dataset<Row> indexColumn(String categoricalColumn) {
        return new StringIndexer().setHandleInvalid("skip")
                .setInputCol(categoricalColumn)
                .setOutputCol(categoricalColumn + "_indexed")
                .fit(dataset)
                .transform(dataset);
    }

    public Map<String, Map<Double, String>> getIndexedToActualValuesForEachIndexedColumn() {
        Map<String, Map<Double, String>> indexedColumnsToIndexedAndActualValues = new HashMap<>();

        List<String> indexedColumns = new ArrayList<>();
        indexedColumns.add(labeledColumnName);
        indexedColumns.addAll(attributesFinder.getCategoricalFeatures());
        for (String indexedColumn : indexedColumns) {
            Map<Double, String> indexedToActualValues = new HashMap<>();
            Iterator<Row> rows = dataset
                    .select(indexedColumn, indexedColumn + "_indexed")
                    .distinct()
                    .toLocalIterator();
            while (rows.hasNext()) {
                Row row = rows.next();
                indexedToActualValues.put(row.getDouble(1), row.getString(0));
            }
            indexedColumnsToIndexedAndActualValues.put(indexedColumn, indexedToActualValues);
        }
        return indexedColumnsToIndexedAndActualValues;
    }
}