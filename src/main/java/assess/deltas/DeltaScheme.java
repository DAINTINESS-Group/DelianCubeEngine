package assess.deltas;

import assess.benchmarks.AssessBenchmark;
import assess.utils.ComparedCell;
import result.Cell;

import java.util.*;

public class DeltaScheme {
    interface ComparisonFunction {
        double compare(double actual, double benchmark);

        ComparisonFunction absoluteDifference = (actual, benchmark) -> Math.abs(actual - benchmark);
        ComparisonFunction difference = (actual, benchmark) -> actual - benchmark;
        ComparisonFunction ratio = (actual, benchmark) -> actual / benchmark;
    }

    private static final HashMap<String, ComparisonFunction> functionsMap = createComparisonMap();

    private static HashMap<String, ComparisonFunction> createComparisonMap() {
        HashMap<String, ComparisonFunction> functionsMap = new HashMap<>();
        functionsMap.put("absolute", ComparisonFunction.absoluteDifference);
        functionsMap.put("difference", ComparisonFunction.difference);
        functionsMap.put("ratio", ComparisonFunction.ratio);
        return functionsMap;
    }

    private final List<ComparisonFunction> appliedMethods = new ArrayList<>();

    public DeltaScheme(List<String> methods) {
        Collections.reverse(methods);
        for (String method : methods) {
            appliedMethods.add(functionsMap.get(method));
        }
    }

    public HashMap<Cell, Double> compareTargetToBenchmark(List<Cell> targetCubeCells, AssessBenchmark benchmark, List<ComparedCell> comparedCells) {
        HashMap<Cell, Double> comparisonMap = new HashMap<>();
        for (Cell cell : targetCubeCells) { comparisonMap.put(cell, cell.toDouble()); }
        if (benchmark == null) { return comparisonMap; } // Just label the cell values

        for (Cell targetCell : targetCubeCells) {
            Optional<Cell> matchedCell = benchmark.matchCell(targetCell);
            if(!matchedCell.isPresent()) {
                ComparedCell comparedCell = new ComparedCell(targetCell, null);
                comparedCells.add(comparedCell);
                comparisonMap.remove(targetCell);
                continue;
            }

            double comparisonValue = targetCell.toDouble();
            for (ComparisonFunction function : appliedMethods) {
                comparisonValue = function.compare(comparisonValue, matchedCell.get().toDouble());
            }
            ComparedCell comparedCell = new ComparedCell(targetCell, matchedCell.get());
            comparedCells.add(comparedCell);
            comparisonMap.put(targetCell, comparisonValue);
        }
        return comparisonMap;
    }
}
