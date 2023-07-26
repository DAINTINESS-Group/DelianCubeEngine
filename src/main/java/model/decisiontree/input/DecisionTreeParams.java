package model.decisiontree.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DecisionTreeParams {
    public enum Impurity {ENTROPY, GINI}
    private final String impurity;
    private final String labeledColumnName;
    private final List<String> nonGeneratorAttributes;
    private final List<String> selectedFeatures;
    private final double[] trainingToTestDataSplitRatio;
    private final int maxDepth;
    private final double minInfoGain;

    private DecisionTreeParams(Builder builder) {
        this.impurity = builder.impurity;
        this.labeledColumnName = builder.labeledColumnName;
        this.nonGeneratorAttributes = builder.nonGeneratorAttributes;
        this.selectedFeatures = builder.selectedFeatures;
        this.trainingToTestDataSplitRatio = builder.trainingToTestDataSplitRatio;
        this.maxDepth = builder.maxDepth;
        this.minInfoGain = builder.minInfoGain;
    }

    public String getImpurity() {
        return impurity;
    }

    public String getLabeledColumnName() {
        return labeledColumnName;
    }

    public List<String> getNonGeneratorAttributes() {
        return nonGeneratorAttributes;
    }

    public List<String> getSelectedFeatures() {
        return selectedFeatures;
    }

    public double[] getTrainingToTestDataSplitRatio() {
        return trainingToTestDataSplitRatio;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public double getMinInfoGain() {
        return minInfoGain;
    }

    public static class Builder {
        private final String labeledColumnName;
        private final List<String> nonGeneratorAttributes;
        private String impurity;
        private List<String> selectedFeatures = new ArrayList<>();
        private double[] trainingToTestDataSplitRatio;
        private int maxDepth;
        private double minInfoGain;

        public Builder(String labeledColumnName, List<String> nonGeneratorAttributes) {
            this.labeledColumnName = labeledColumnName;
            this.nonGeneratorAttributes = nonGeneratorAttributes;
            this.impurity = DecisionTreeDefaultParams.DEFAULT_IMPURITY.toString().toLowerCase();
            this.trainingToTestDataSplitRatio = DecisionTreeDefaultParams.DEFAULT_TRAINING_TO_TEST_SPLIT_RATIO;
            this.maxDepth = DecisionTreeDefaultParams.DEFAULT_MAX_DEPTH;
            this.minInfoGain = DecisionTreeDefaultParams.DEFAULT_MIN_INFO_GAIN;
        }

        public Builder impurity(Impurity impurity) {
            if (impurity != null) {
                this.impurity = impurity.toString().toLowerCase();
            }
            return this;
        }

        public Builder selectedFeatures(List<String> selectedFeatures) {
            this.selectedFeatures = validateSelectedFeatures(selectedFeatures);
            return this;
        }

        public Builder trainingToTestDataSplitRatio(double[] ratio) {
            if (ratio != null && ratio.length == 2)
                this.trainingToTestDataSplitRatio = ratio;
            return this;
        }

        public Builder maxDepth(int value) {
            if (value >= DecisionTreeDefaultParams.MINIMUM_MAX_DEPTH)
                this.maxDepth = value;
            return this;
        }

        public Builder minInfoGain(double value) {
            if (value >= DecisionTreeDefaultParams.MINIMUM_MIN_INFO_GAIN)
                this.minInfoGain = value;
            return this;
        }

        public DecisionTreeParams build() {
            return new DecisionTreeParams(this);
        }

        // TODO: Removes only not null elements. Maybe throw an error instead?
        private List<String> validateSelectedFeatures(List<String> selectedFeatures) {
            if (selectedFeatures == null)
                return new ArrayList<>();
            if (selectedFeatures.contains(null))
                return selectedFeatures.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            return selectedFeatures;
        }
    }
}
