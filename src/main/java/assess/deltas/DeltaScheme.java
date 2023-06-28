package assess.deltas;

import assess.benchmarks.AssessBenchmark;
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

    public HashMap<Cell, Double> compareTargetToBenchmark(List<Cell> targetCubeCells, AssessBenchmark benchmark) {
        HashMap<Cell, Double> comparisonMap = new HashMap<>();
        for (Cell cell : targetCubeCells) { comparisonMap.put(cell, cell.toDouble()); }
        if (benchmark == null) { return comparisonMap; } // Just label the cell values

        for (Cell targetCell : targetCubeCells) {
            Optional<Cell> matchedCell = benchmark.matchCell(targetCell);
            System.out.println("\nTarget Cell: " + targetCell.toString(", "));
            if(!matchedCell.isPresent()) {
                System.out.println("Was not matched");
                comparisonMap.remove(targetCell);
                continue;
            }
            System.out.println("Benchmark Cell: " + matchedCell.get().toString(", "));

            double comparisonValue = targetCell.toDouble();
            for (ComparisonFunction function : appliedMethods) {
                comparisonValue = function.compare(comparisonValue, matchedCell.get().toDouble());
            }
            comparisonMap.put(targetCell, comparisonValue);
        }
        return comparisonMap;
    }
}
