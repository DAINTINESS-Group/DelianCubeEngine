package assess.utils;

import result.Cell;

public class ComparedCell {
    public final Cell target;
    public final Cell benchmark;

    public ComparedCell(Cell targetCell, Cell benchmarkCell) {
        this.target = targetCell;
        this.benchmark = benchmarkCell;
    }

    @Override
    public String toString() {
        String printedString = "Target Cell: " + target.toString(", ") + "\n";
        if (benchmark == null) {
            return printedString + "had no match, thus will not be labeled";
        }
        return printedString + "was compared against\n" +
                "Benchmark Cell: " + benchmark.toString(", ");

    }
}
