package assess.utils;

import result.Cell;

/**
 * Class used for printing information related to the comparison phase for
 * each cell.
 */
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
        return printedString + "was compared against\n\n" +
                "Benchmark Cell info\n" + benchmark.toString(", ");

    }
}
