package model.decisiontree.input;

public class DecisionTreeDefaultParams {
    // See https://spark.apache.org/docs/latest/api/java/org/apache/spark/ml/classification/DecisionTreeClassifier.html
    public static final int DEFAULT_MAX_DEPTH = 5;
    public static final double DEFAULT_MIN_INFO_GAIN = 0.0;
    public static final double[] DEFAULT_TRAINING_TO_TEST_SPLIT_RATIO = new double[]{0.3, 0.7};
    public static final DecisionTreeParams.Impurity DEFAULT_IMPURITY = DecisionTreeParams.Impurity.GINI;
    public static final int MINIMUM_MAX_DEPTH = 0;
    public static final double MINIMUM_MIN_INFO_GAIN = 0.0;
}
