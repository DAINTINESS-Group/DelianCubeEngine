package assess.deltas;

import assess.benchmarks.AssessBenchmark;
import result.Cell;
import result.Result;

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

    public HashMap<Cell, Double> compareTargetToBenchmark(Result targetCube, AssessBenchmark benchmark) {
        HashMap<Cell, Double> comparisonMap = new HashMap<>();
        for (Cell cell : targetCube.getCells()) {
            try { // When the benchmark cells are not enough, the remaining target cells will not be added to the set of cells
                double expectedValue = benchmark.getCellValue();
                double comparisonValue = cell.toDouble();
                for (ComparisonFunction function : appliedMethods) {
                    comparisonValue = function.compare(comparisonValue, expectedValue);
                }
                comparisonMap.put(cell, comparisonValue);
            } catch (NoSuchElementException ie) {
                int ignoredCellsNumber = targetCube.getCells().size() - targetCube.getCells().indexOf(cell);
                System.out.println("Benchmark cells were not enough, " + ignoredCellsNumber +" cell(s) will be ignored");
                break;
            }
        }

        return comparisonMap;
    }
}
