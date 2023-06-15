package assess.utils;

import result.Cell;

public class LabeledCell {
    public final Cell cell;
    public final String label;

    public LabeledCell(Cell cell, String label) {
        this.cell = cell;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Target Cell: " + cell.toString(", ") +
                "\nwith measurement: " + cell.getMeasure() +
                "\nwas labeled: " + label;
    }
}
