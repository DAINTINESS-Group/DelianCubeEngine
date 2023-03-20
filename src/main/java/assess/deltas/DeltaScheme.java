package assess.deltas;

import assess.benchmarks.AssessBenchmark;
import cubemanager.cubebase.Cube;
import result.Cell;
import result.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    public List<Double> compareTargetToBenchmark(Result targetCube, AssessBenchmark benchmark) {
        ArrayList<Double> comparisonResults = new ArrayList<>();
        for (Cell cell : targetCube.getCells()) {
            double expectedValue = benchmark.getCellValue();
            double actualValue = cell.toDouble();
            for (ComparisonFunction function : appliedMethods) {
                actualValue = function.compare(actualValue, expectedValue);
            }
            comparisonResults.add(actualValue);
        }
        return comparisonResults;
    }
}
