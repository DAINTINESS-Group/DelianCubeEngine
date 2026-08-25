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

    /**
     * Rewrites the whole comparison column at once, against column-level statistics no single cell can
     * provide; the target measure column rides along for transforms that scale against it.
     */
    interface HolisticTransform {
        double[] apply(double[] column, double[] targetColumn);
    }

    /** One link of the comparison chain: either a cell-wise function or a holistic transform. */
    private static final class Step {
        final ComparisonFunction cellWise;
        final HolisticTransform holistic;

        Step(ComparisonFunction cellWise, HolisticTransform holistic) {
            this.cellWise = cellWise;
            this.holistic = holistic;
        }
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
    private static final HashMap<String, HolisticTransform> transformsMap = createTransformMap();

    private static HashMap<String, ComparisonFunction> createComparisonMap() {
        HashMap<String, ComparisonFunction> functionsMap = new HashMap<>();
        functionsMap.put("absolute", ComparisonFunction.absoluteDifference);
        functionsMap.put("difference", ComparisonFunction.difference);
        functionsMap.put("invDifference", ComparisonFunction.invDifference);
        functionsMap.put("ratio", ComparisonFunction.ratio);
        return functionsMap;
    }

    private static HashMap<String, HolisticTransform> createTransformMap() {
        HashMap<String, HolisticTransform> transformsMap = new HashMap<>();
        transformsMap.put("minMaxNorm", DeltaScheme::minMaxNorm);
        transformsMap.put("percOfTotal", DeltaScheme::percOfTotal);
        transformsMap.put("zscore", DeltaScheme::zscore);
        transformsMap.put("rank", DeltaScheme::rank);
        return transformsMap;
    }

    private final List<Step> steps = new ArrayList<>();
    private final Operand left;
    private final Operand right;

    /** With no methods, comparison values stay the target values themselves. */
    public DeltaScheme(List<String> methods) {
        this(methods, Operand.TARGET, Operand.BENCHMARK);
    }

    /**
     * The innermost call's operands feed the first function; each outer cell-wise function rechains the
     * right operand, while a holistic transform rewrites the running column.
     */
    public DeltaScheme(List<String> methods, Operand left, Operand right) {
        this.left = left;
        this.right = right;
        if (methods == null) {
            return;
        }
        Collections.reverse(methods);
        for (String method : methods) {
            ComparisonFunction cellWise = functionsMap.get(method);
            HolisticTransform holistic = cellWise == null ? transformsMap.get(method) : null;
            if (cellWise == null && holistic == null) {
                throw new IllegalArgumentException("Unknown comparison function: " + method);
            }
            steps.add(new Step(cellWise, holistic));
        }
        if (!steps.isEmpty() && steps.get(0).holistic != null) {
            throw new IllegalArgumentException(
                    "The innermost comparison function takes the operand pair, so it must be cell-wise");
        }
    }

    public HashMap<Cell, Double> compareTargetToBenchmark(List<Cell> targetCubeCells, AssessBenchmark benchmark, List<ComparedCell> comparedCells) {
        HashMap<Cell, Double> comparisonMap = new HashMap<>();
        List<Cell> matched = new ArrayList<>();
        List<Double> targetValues = new ArrayList<>();
        List<Double> benchmarkValues = new ArrayList<>();

        if (benchmark == null) {
            boolean computable = !steps.isEmpty()
                    && !left.needsBenchmark() && !right.needsBenchmark();
            if (!computable) {
                for (Cell cell : targetCubeCells) {
                    comparisonMap.put(cell, cell.toDouble());
                }
                return comparisonMap;
            }
            for (Cell cell : targetCubeCells) {
                matched.add(cell);
                targetValues.add(cell.toDouble());
                benchmarkValues.add(Double.NaN);
            }
        } else {
            for (Cell targetCell : targetCubeCells) {
                Optional<Cell> matchedCell = benchmark.matchCell(targetCell);
                if (!matchedCell.isPresent()) {
                    comparedCells.add(new ComparedCell(targetCell, null));
                    continue;
                }
                comparedCells.add(new ComparedCell(targetCell, matchedCell.get()));
                matched.add(targetCell);
                targetValues.add(targetCell.toDouble());
                benchmarkValues.add(matchedCell.get().toDouble());
            }
        }

        double[] column = applyChain(targetValues, benchmarkValues);
        for (int i = 0; i < matched.size(); i++) {
            comparisonMap.put(matched.get(i), column[i]);
        }
        return comparisonMap;
    }

    /** Folds the chain over the matched columns: cell-wise steps run per cell, holistic steps per column. */
    private double[] applyChain(List<Double> targetValues, List<Double> benchmarkValues) {
        int size = targetValues.size();
        double[] targetColumn = new double[size];
        double[] column = new double[size];
        for (int i = 0; i < size; i++) {
            targetColumn[i] = targetValues.get(i);
            column[i] = left.valueOf(targetValues.get(i), benchmarkValues.get(i));
        }
        for (Step step : steps) {
            if (step.holistic != null) {
                column = step.holistic.apply(column, targetColumn);
                continue;
            }
            for (int i = 0; i < size; i++) {
                column[i] = step.cellWise.compare(column[i],
                        right.valueOf(targetValues.get(i), benchmarkValues.get(i)));
            }
        }
        return column;
    }

    /** Rescales the column to [0,1] between its minimum and maximum; a flat column maps to zero. */
    private static double[] minMaxNorm(double[] column, double[] targetColumn) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (double value : column) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        double span = max - min;
        double[] out = new double[column.length];
        for (int i = 0; i < column.length; i++) {
            out[i] = span == 0.0 ? 0.0 : (column[i] - min) / span;
        }
        return out;
    }

    /** Scales each value against the total of the target measure; a zero total maps to zero. */
    private static double[] percOfTotal(double[] column, double[] targetColumn) {
        double total = 0.0;
        for (double value : targetColumn) {
            total += value;
        }
        double[] out = new double[column.length];
        for (int i = 0; i < column.length; i++) {
            out[i] = total == 0.0 ? 0.0 : column[i] / total;
        }
        return out;
    }

    /** Centers each value on the column mean, in standard deviations; a spreadless column maps to zero. */
    private static double[] zscore(double[] column, double[] targetColumn) {
        double mean = 0.0;
        for (double value : column) {
            mean += value;
        }
        mean /= column.length;
        double variance = 0.0;
        for (double value : column) {
            variance += (value - mean) * (value - mean);
        }
        double std = column.length < 2 ? 0.0 : Math.sqrt(variance / (column.length - 1));
        double[] out = new double[column.length];
        for (int i = 0; i < column.length; i++) {
            out[i] = std == 0.0 ? 0.0 : (column[i] - mean) / std;
        }
        return out;
    }

    /** Each value's one-based position in the column's ascending order; tied values share their mean position. */
    private static double[] rank(double[] column, double[] targetColumn) {
        Integer[] order = new Integer[column.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        Arrays.sort(order, Comparator.comparingDouble(i -> column[i]));
        double[] out = new double[column.length];
        int start = 0;
        while (start < order.length) {
            int end = start;
            while (end + 1 < order.length && column[order[end + 1]] == column[order[start]]) {
                end++;
            }
            double meanPosition = (start + end) / 2.0 + 1;
            for (int i = start; i <= end; i++) {
                out[order[i]] = meanPosition;
            }
            start = end + 1;
        }
        return out;
    }
}
