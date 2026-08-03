package intentional.assess.deltas;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.utils.ComparedCell;
import result.Cell;

import java.util.*;

public class DeltaScheme {
    interface ComparisonFunction {
        double compare(double actual, double benchmark);

        ComparisonFunction absoluteDifference = (actual, benchmark) -> Math.abs(actual - benchmark);
        ComparisonFunction difference = (actual, benchmark) -> actual - benchmark;
        ComparisonFunction invDifference = (actual, benchmark) -> benchmark - actual;
        ComparisonFunction ratio = (actual, benchmark) -> actual / benchmark;
    }

    /** An operand of the comparison: the target cell's value, its benchmark value, or a constant. */
    public static final class Operand {
        public static final Operand TARGET = new Operand(Kind.TARGET, 0);
        public static final Operand BENCHMARK = new Operand(Kind.BENCHMARK, 0);

        enum Kind { TARGET, BENCHMARK, CONSTANT }

        private final Kind kind;
        private final double constant;

        private Operand(Kind kind, double constant) {
            this.kind = kind;
            this.constant = constant;
        }

        public static Operand constant(double value) {
            return new Operand(Kind.CONSTANT, value);
        }

        public boolean needsBenchmark() {
            return kind == Kind.BENCHMARK;
        }

        double valueOf(double target, double benchmark) {
            switch (kind) {
                case TARGET: return target;
                case BENCHMARK: return benchmark;
                default: return constant;
            }
        }
    }

    private static final HashMap<String, ComparisonFunction> functionsMap = createComparisonMap();

    private static HashMap<String, ComparisonFunction> createComparisonMap() {
        HashMap<String, ComparisonFunction> functionsMap = new HashMap<>();
        functionsMap.put("absolute", ComparisonFunction.absoluteDifference);
        functionsMap.put("difference", ComparisonFunction.difference);
        functionsMap.put("invDifference", ComparisonFunction.invDifference);
        functionsMap.put("ratio", ComparisonFunction.ratio);
        return functionsMap;
    }

    private final List<ComparisonFunction> appliedMethods = new ArrayList<>();
    private final Operand left;
    private final Operand right;

    /** With no methods, comparison values stay the target values themselves. */
    public DeltaScheme(List<String> methods) {
        this(methods, Operand.TARGET, Operand.BENCHMARK);
    }

    /** The innermost call's operands feed the first function; outer functions rechain the right operand. */
    public DeltaScheme(List<String> methods, Operand left, Operand right) {
        this.left = left;
        this.right = right;
        if (methods == null) {
            return;
        }
        Collections.reverse(methods);
        for (String method : methods) {
            appliedMethods.add(functionsMap.get(method));
        }
    }

    public HashMap<Cell, Double> compareTargetToBenchmark(List<Cell> targetCubeCells, AssessBenchmark benchmark, List<ComparedCell> comparedCells) {
        HashMap<Cell, Double> comparisonMap = new HashMap<>();
        if (benchmark == null) {
            boolean computable = !appliedMethods.isEmpty()
                    && !left.needsBenchmark() && !right.needsBenchmark();
            for (Cell cell : targetCubeCells) {
                comparisonMap.put(cell, computable
                        ? applyChain(cell.toDouble(), Double.NaN)
                        : cell.toDouble());
            }
            return comparisonMap;
        }

        for (Cell targetCell : targetCubeCells) {
            Optional<Cell> matchedCell = benchmark.matchCell(targetCell);
            if (!matchedCell.isPresent()) {
                comparedCells.add(new ComparedCell(targetCell, null));
                continue;
            }
            comparedCells.add(new ComparedCell(targetCell, matchedCell.get()));
            comparisonMap.put(targetCell,
                    applyChain(targetCell.toDouble(), matchedCell.get().toDouble()));
        }
        return comparisonMap;
    }

    private double applyChain(double targetValue, double benchmarkValue) {
        double leftValue = left.valueOf(targetValue, benchmarkValue);
        double rightValue = right.valueOf(targetValue, benchmarkValue);
        double value = leftValue;
        for (ComparisonFunction function : appliedMethods) {
            value = function.compare(value, rightValue);
        }
        return value;
    }
}
