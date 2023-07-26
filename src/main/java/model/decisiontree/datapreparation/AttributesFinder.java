package model.decisiontree.datapreparation;

import model.decisiontree.util.DatatypeFilterer;
import model.decisiontree.input.DecisionTreeParams;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;


import java.util.ArrayList;
import java.util.List;


public class AttributesFinder {

    private final Dataset<Row> dataset;
    private final DecisionTreeParams decisionTreeParams;
    private final List<String> datasetColumns = new ArrayList<>();
    private final List<String> numericalFeatures = new ArrayList<>();
    private final List<String> categoricalFeatures = new ArrayList<>();
    private final List<String> categoricalFeaturesIndexed = new ArrayList<>();
    private final List<String> nonGeneratingAttributes = new ArrayList<>();

    public AttributesFinder(DecisionTreeParams decisionTreeParams, Dataset<Row> dataset) {
        this.decisionTreeParams = decisionTreeParams;
        this.dataset = dataset;
        for (StructField field : dataset.schema().fields()) {
            this.datasetColumns.add(field.name());
        }
        discernFeatures();
    }

    public List<String> getNonGeneratingAttributes() {
        return nonGeneratingAttributes;
    }

    public List<String> getCategoricalFeatures() {
        return categoricalFeatures;
    }

    public List<String> getAllFeatures() {
        List<String> allFeatures = new ArrayList<>();
        allFeatures.addAll(numericalFeatures);
        allFeatures.addAll(categoricalFeatures);
        return allFeatures;
    }

    public List<String> getAllFeaturesIndexed() {
        List<String> allFeatures = new ArrayList<>();
        allFeatures.addAll(numericalFeatures);
        allFeatures.addAll(categoricalFeaturesIndexed);
        return allFeatures;
    }

    private void discernFeatures() {
        findNumericalFeatures();
        findCategoricalFeatures();
        findNonGeneratingAttributes();
    }

    private void findNumericalFeatures() {
        StructField[] fields = dataset.schema().fields();
        for (StructField field : fields) {
            if (DatatypeFilterer.isNumerical(field.dataType())
                && shouldBeIncluded(field.name())) {
                numericalFeatures.add(field.name());
            }
        }
    }

    private void findCategoricalFeatures() {
        StructField[] fields = dataset.schema().fields();
        for (StructField field : fields) {
            if (DatatypeFilterer.isStringType(field.dataType())
                && shouldBeIncluded(field.name())) {
                categoricalFeatures.add(field.name());
            }
        }

        for (String categoricalFeature : categoricalFeatures) {
            categoricalFeaturesIndexed.add(categoricalFeature + "_indexed");
        }
    }

    private boolean shouldBeIncluded(String feature) {
        return featureIsValid(feature) && featureIsSelected(feature);
    }

    private boolean featureIsValid(String feature) {
        return !decisionTreeParams.getNonGeneratorAttributes().contains(feature) &&
                !feature.equals(decisionTreeParams.getLabeledColumnName()) &&
                datasetColumns.contains(feature);
    }

    private boolean featureIsSelected(String feature) {
        return  decisionTreeParams.getSelectedFeatures().isEmpty() ||
                decisionTreeParams.getSelectedFeatures().contains(feature);
    }

    private void findNonGeneratingAttributes() {
        StructField[] fields = dataset.schema().fields();
        for (StructField field : fields) {
            if (!numericalFeatures.contains(field.name()) &&
                !categoricalFeatures.contains(field.name())
//                // uncomment if we want the labeled column to not be included
//                && !field.name().equals(decisionTreeParams.getLabeledColumnName())
//                // uncomment if we want the target column names to not be included
//                && !decisionTreeParams.getNonGeneratorAttributes().contains(field.name())
            ) {
                nonGeneratingAttributes.add(field.name());
            }
        }
    }
}
