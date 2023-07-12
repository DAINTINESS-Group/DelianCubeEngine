package assess.utils;

import result.Cell;

public class LabeledCell {
    public final Cell cell;
    public final String label;
    public final Double comparisonResult;

    public LabeledCell(Cell cell, Double comparisonResult, String label) {
        this.cell = cell;
        this.label = label;
        this.comparisonResult = comparisonResult;
    }

    @Override
    public String toString() {
        return "Target Cell: " + cell.toString(", ") +
                "\nwith measurement: " + cell.getMeasure() +
                "\nComparison Result:" + comparisonResult +
                "\nwas labeled: " + label;
    }
}
