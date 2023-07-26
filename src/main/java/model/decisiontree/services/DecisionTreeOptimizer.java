package model.decisiontree.services;

import model.decisiontree.util.DatatypeFilterer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;

import java.util.ArrayList;
import java.util.List;

public class DecisionTreeOptimizer {

    private final int MAX_DISTINCT_VALUES = 32;
    private final Dataset<Row> dataset;

    public DecisionTreeOptimizer(Dataset<Row> dataset) {
        this.dataset = dataset;
    }

    public Dataset<Row> getOptimizedDataset() {
        return dataset.drop(getColumnsToDrop());
    }

    private String[] getColumnsToDrop() {
        List<String> columnsToDrop = new ArrayList<>();
        List<String> categoricalColumns = getCategoricalColumns();
        for (String column : categoricalColumns) {
            if (hasTooManyDistinctValues(column)) {
                columnsToDrop.add(column);
            }
        }
        return columnsToDrop.toArray(new String[0]);
    }

    private List<String> getCategoricalColumns() {
        List<String> categoricalColumns = new ArrayList<>();
        StructField[] fields = dataset.schema().fields();
        for (StructField field : fields) {
            if (DatatypeFilterer.isStringType(field.dataType())) {
                categoricalColumns.add(field.name());
            }
        }
        return categoricalColumns;
    }

    private boolean hasTooManyDistinctValues(String column) {
        long distinctValuesCount = dataset.select(column).distinct().count();
        return distinctValuesCount > MAX_DISTINCT_VALUES;
    }
}
