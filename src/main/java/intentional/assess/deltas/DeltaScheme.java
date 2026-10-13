package intentional.assess.deltas;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.utils.ComparedCell;
import result.Cell;

import java.util.*;
import java.util.function.Function;

/**
 * The USING expression of an assess comparison, evaluated over the compared cells. Cells whose value is not
 * finite are left out.
 */
public class DeltaScheme {
    interface ComparisonFunction {
        double compare(double actual, double benchmark);

        ComparisonFunction absoluteDifference = (actual, benchmark) -> Math.abs(actual - benchmark);
        ComparisonFunction difference = (actual, benchmark) -> actual - benchmark;
        ComparisonFunction invDifference = (actual, benchmark) -> benchmark - actual;
        ComparisonFunction ratio = (actual, benchmark) -> actual / benchmark;
    }

    /** Rewrites a whole column; the total column is the target column unless given. */
    interface HolisticTransform {
        double[] apply(double[] column, double[] totalColumn);
    }

    /** A USING expression as parsed: a function call or an operand reference. */
    public static final class Term {
        final String function;
        final List<Term> arguments;
        final String ref;

        private Term(String function, List<Term> arguments, String ref) {
            this.function = function;
            this.arguments = arguments;
            this.ref = ref;
        }

        public static Term call(String function, List<Term> arguments) {
            return new Term(function, new ArrayList<>(arguments), null);
        }

        public static Term ref(String ref) {
            return new Term(null, Collections.<Term>emptyList(), ref);
        }

        @Override
        public String toString() {
            if (ref != null) {
                return ref;
            }
            StringJoiner written = new StringJoiner(", ", function + "(", ")");
            for (Term argument : arguments) {
                written.add(argument.toString());
            }
            return written.toString();
        }
    }

    /** A leaf of the comparison: the target cell's value, its benchmark value, or a constant. */
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

        double valueOf(double target, double benchmark) {
            double value = kind == Kind.TARGET ? target : kind == Kind.BENCHMARK ? benchmark : constant;
            return Double.isFinite(value) ? value : Double.NaN;
        }
    }

    private static final class Node {
        final Operand operand;
        final ComparisonFunction cellWise;
        final HolisticTransform holistic;
        final List<Node> arguments;

        Node(Operand operand, ComparisonFunction cellWise, HolisticTransform holistic, List<Node> arguments) {
            this.operand = operand;
            this.cellWise = cellWise;
            this.holistic = holistic;
            this.arguments = arguments;
        }
    }

    private static final HashMap<String, ComparisonFunction> functionsMap = createComparisonMap();
    private static final HashMap<String, HolisticTransform> transformsMap = createTransformMap();
    /** Holistic functions that take the column to total as an optional second argument. */
    private static final Set<String> totalingTransforms = Collections.singleton("percOfTotal");

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

    private static final Node TARGET_LEAF = new Node(Operand.TARGET, null, null, Collections.<Node>emptyList());

    private final Node root;

    private DeltaScheme(Node root) {
        this.root = root;
    }

    public DeltaScheme(Term expression, Function<String, Operand> resolver) {
        this(resolve(expression, resolver));
    }

    /** Each cell's own value. */
    public static DeltaScheme identity() {
        return new DeltaScheme(TARGET_LEAF);
    }

    /** The target value minus the benchmark value. */
    public static DeltaScheme difference() {
        Node benchmark = new Node(Operand.BENCHMARK, null, null, Collections.<Node>emptyList());
        return new DeltaScheme(new Node(null, ComparisonFunction.difference, null,
                Arrays.asList(TARGET_LEAF, benchmark)));
    }

    private static Node resolve(Term term, Function<String, Operand> resolver) {
        if (term.ref != null) {
            return new Node(resolver.apply(term.ref), null, null, Collections.<Node>emptyList());
        }
        List<Node> arguments = new ArrayList<>();
        for (Term argument : term.arguments) {
            arguments.add(resolve(argument, resolver));
        }
        ComparisonFunction cellWise = functionsMap.get(term.function);
        if (cellWise != null) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException(String.format(
                        "%s compares two values, e.g. %s(total, benchmark.total); got %d argument(s)",
                        term.function, term.function, arguments.size()));
            }
            return new Node(null, cellWise, null, arguments);
        }
        HolisticTransform holistic = transformsMap.get(term.function);
        if (holistic == null) {
            throw new IllegalArgumentException("Unknown comparison function: " + term.function);
        }
        int maxArguments = totalingTransforms.contains(term.function) ? 2 : 1;
        if (arguments.isEmpty() || arguments.size() > maxArguments) {
            throw new IllegalArgumentException(String.format(
                    "%s takes %s; got %d argument(s)", term.function,
                    maxArguments == 1 ? "one argument" : "one or two arguments", arguments.size()));
        }
        if (arguments.get(0).operand != null && arguments.get(0).operand.kind == Operand.Kind.CONSTANT) {
            throw new IllegalArgumentException(
                    "A constant has no value distribution for " + term.function + " to transform");
        }
        return new Node(null, null, holistic, arguments);
    }

    /** The comparison value of every matched cell whose value is defined. */
    public HashMap<Cell, Double> compareTargetToBenchmark(List<Cell> targetCubeCells, AssessBenchmark benchmark, List<ComparedCell> comparedCells) {
        List<Cell> matched = new ArrayList<>();
        List<Double> benchmarkValues = new ArrayList<>();
        for (Cell targetCell : targetCubeCells) {
            if (benchmark == null) {
                matched.add(targetCell);
                benchmarkValues.add(Double.NaN);
                continue;
            }
            Optional<Cell> matchedCell = benchmark.matchCell(targetCell);
            comparedCells.add(new ComparedCell(targetCell, matchedCell.orElse(null)));
            if (matchedCell.isPresent()) {
                matched.add(targetCell);
                benchmarkValues.add(matchedCell.get().toDouble());
            }
        }

        double[] targets = new double[matched.size()];
        double[] benchmarks = new double[matched.size()];
        for (int i = 0; i < targets.length; i++) {
            targets[i] = matched.get(i).toDouble();
            benchmarks[i] = benchmarkValues.get(i);
        }
        double[] column = evaluate(root, targets, benchmarks);

        HashMap<Cell, Double> comparisonMap = new HashMap<>();
        for (int i = 0; i < column.length; i++) {
            if (!Double.isNaN(column[i])) {
                comparisonMap.put(matched.get(i), column[i]);
            }
        }
        return comparisonMap;
    }

    /** An undefined cell is NaN, and stays NaN upward. */
    private static double[] evaluate(Node node, double[] targets, double[] benchmarks) {
        int size = targets.length;
        double[] out = new double[size];
        if (node.operand != null) {
            for (int i = 0; i < size; i++) {
                out[i] = node.operand.valueOf(targets[i], benchmarks[i]);
            }
            return out;
        }
        double[] first = evaluate(node.arguments.get(0), targets, benchmarks);
        double[] second = node.arguments.size() > 1
                ? evaluate(node.arguments.get(1), targets, benchmarks)
                : (node.holistic != null ? evaluate(TARGET_LEAF, targets, benchmarks) : null);
        if (node.cellWise != null) {
            for (int i = 0; i < size; i++) {
                out[i] = finiteOrUndefined(node.cellWise.compare(first[i], second[i]));
            }
            return out;
        }
        return applyHolistic(node.holistic, first, second);
    }

    /** Runs the transform over the defined cells only. */
    private static double[] applyHolistic(HolisticTransform transform, double[] column, double[] totalColumn) {
        List<Integer> defined = new ArrayList<>();
        for (int i = 0; i < column.length; i++) {
            if (!Double.isNaN(column[i]) && !Double.isNaN(totalColumn[i])) {
                defined.add(i);
            }
        }
        double[] out = new double[column.length];
        Arrays.fill(out, Double.NaN);
        if (defined.isEmpty()) {
            return out;
        }
        double[] compact = new double[defined.size()];
        double[] compactTotals = new double[defined.size()];
        for (int k = 0; k < compact.length; k++) {
            compact[k] = column[defined.get(k)];
            compactTotals[k] = totalColumn[defined.get(k)];
        }
        double[] transformed = transform.apply(compact, compactTotals);
        for (int k = 0; k < transformed.length; k++) {
            out[defined.get(k)] = finiteOrUndefined(transformed[k]);
        }
        return out;
    }

    private static double finiteOrUndefined(double value) {
        return Double.isFinite(value) ? value : Double.NaN;
    }

    /** Rescales the column to [0,1] between its minimum and maximum; a flat column maps to zero. */
    private static double[] minMaxNorm(double[] column, double[] totalColumn) {
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

    /** Divides each value by the total of the total column; a zero total fails. */
    private static double[] percOfTotal(double[] column, double[] totalColumn) {
        double total = 0.0;
        for (double value : totalColumn) {
            total += value;
        }
        if (total == 0.0) {
            throw new IllegalStateException(
                    "percOfTotal divides by a total that is zero over the compared cells");
        }
        double[] out = new double[column.length];
        for (int i = 0; i < column.length; i++) {
            out[i] = column[i] / total;
        }
        return out;
    }

    /** Centers each value on the column mean, in standard deviations; a spreadless column maps to zero. */
    private static double[] zscore(double[] column, double[] totalColumn) {
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
    private static double[] rank(double[] column, double[] totalColumn) {
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
