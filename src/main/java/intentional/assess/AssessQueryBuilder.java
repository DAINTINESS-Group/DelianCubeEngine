package intentional.assess;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.benchmarks.BenchmarkKind;
import intentional.assess.deltas.DeltaScheme;
import intentional.assess.fetch.AssessFetcher;
import intentional.assess.fetch.FetchStrategy;
import intentional.assess.fetch.FetchedCubes;
import intentional.labeling.LabelingScheme;
import intentional.labeling.schemes.CustomLabelingScheme;
import intentional.labeling.schemes.SchemeCatalog;
import cubemanager.CubeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

/**
 * This class is used after we have parsed a query with the AssessQueryParser to
 * build the AssessQuery object. <br>
 */
public class AssessQueryBuilder {

    /** One AGAINST entry: its benchmark descriptor and the USING chain bound to it. */
    private static final class BenchmarkSpec {
        final List<String> details;
        List<String> deltaFunctions;
        String[] operandRefs;

        BenchmarkSpec(List<String> details) {
            this.details = details;
        }
    }

    private final CubeManagerAdapter queryGenerator;
    private final AssessFetcher fetcher;
    private final List<BenchmarkSpec> benchmarkSpecs = new ArrayList<>(); // Empty when there is no AGAINST clause
    private List<String> deltaFunctions; // The USING chain of a benchmark-less query
    private LabelingScheme labeler;
    private String outputName = null;
    private String[] deltaOperandRefs;

    public AssessQueryBuilder(CubeManager cubeManager) {
        this(cubeManager, FetchStrategy.SCAN_PER_SLICE);
    }

    public AssessQueryBuilder(CubeManager cubeManager, FetchStrategy strategy) {
        queryGenerator = new CubeManagerAdapter(cubeManager);
        fetcher = strategy.fetcher();
    }

    public void setTargetCubeName(String targetCubeName) {
        queryGenerator.setTargetCubeName(targetCubeName.toLowerCase());
    }

    public AssessQueryBuilder setAggregationFunction(String aggregationFunction) {
        queryGenerator.setAggregationFunction(aggregationFunction.toLowerCase());
        return this;
    }

    public AssessQueryBuilder setMeasurement(String measurement) {
        queryGenerator.setMeasurement(measurement);
        return this;
    }

    private static final Pattern SIMPLE_TARGET =
            Pattern.compile("(?i)^([a-z]+)\\s*\\(\\s*([a-z0-9_]+)\\s*\\)$");
    private static final Pattern FIRST_AGGREGATE =
            Pattern.compile("(?i)([a-z]+)\\s*\\(\\s*([a-z0-9_]+)");

    /**
     * The query's target measure: a single aggregate keeps the plain translation; an expression or an
     * aliased measure goes through the derived-measure path, anchored on its first aggregate and column.
     */
    public void setTargetMeasure(String expression, String alias) {
        String trimmed = expression.trim();
        Matcher simple = SIMPLE_TARGET.matcher(trimmed);
        if (alias == null && simple.matches()) {
            setAggregationFunction(simple.group(1));
            setMeasurement(simple.group(2));
            return;
        }
        Matcher base = FIRST_AGGREGATE.matcher(trimmed);
        if (!base.find()) {
            throw new IllegalArgumentException(
                    "The target measure needs at least one aggregate: " + expression);
        }
        queryGenerator.setMeasureExpression(trimmed, alias, base.group(1), base.group(2));
    }

    public void setOutputName(String name) {
        this.outputName = name;
    }

    public void setSelectionPredicates(Map<String, String> selectionPredicates) {
        queryGenerator.setSelectionPredicates(selectionPredicates);
    }

    public void setGroupBySet(Set<String> groupBySet) {
        queryGenerator.setGroupBySet(groupBySet);
    }

    public AssessQueryBuilder setBenchmarkDetails(List<String> benchmarkDetails) {
        this.benchmarkSpecs.clear();
        addBenchmarkDetails(benchmarkDetails);
        return this;
    }

    /** Opens one AGAINST clause entry; the USING clause that follows binds to it. */
    public void addBenchmarkDetails(List<String> details) {
        if (details != null && !details.isEmpty()) {
            benchmarkSpecs.add(new BenchmarkSpec(details));
        }
    }

    /** Binds the parsed USING chain to the entry being parsed, or to the query when it has no AGAINST. */
    public void setDeltaFunctions(List<String> methods) {
        if (benchmarkSpecs.isEmpty()) {
            deltaFunctions = methods;
            return;
        }
        BenchmarkSpec current = benchmarkSpecs.get(benchmarkSpecs.size() - 1);
        if (current.deltaFunctions != null) {
            throw new IllegalArgumentException("A benchmark takes a single USING clause");
        }
        current.deltaFunctions = methods;
    }

    /** The operand references of the USING clause's innermost call, as written. */
    public void setDeltaOperands(String first, String second) {
        String[] refs = new String[]{first, second};
        if (benchmarkSpecs.isEmpty()) {
            this.deltaOperandRefs = refs;
            return;
        }
        benchmarkSpecs.get(benchmarkSpecs.size() - 1).operandRefs = refs;
    }

    private static final Pattern TRANSFORMED_OPERAND =
            Pattern.compile("^([A-Za-z_][A-Za-z0-9_]*)\\((.+)\\)$");

    /**
     * Binds an operand reference: a number is a constant, {@code benchmark.X} the benchmark's measure,
     * a bare name the target's measure — where X must be the measure the result actually carries. A
     * {@code transform(X)} wrapper normalizes the bound operand against its own value distribution.
     */
    private DeltaScheme.Operand resolveOperand(String ref, boolean hasBenchmark) {
        Matcher wrapped = TRANSFORMED_OPERAND.matcher(ref);
        if (wrapped.matches()) {
            return DeltaScheme.Operand.transformed(wrapped.group(1), resolveOperand(wrapped.group(2), hasBenchmark));
        }
        if (ref.matches("[0-9.]+")) {
            return DeltaScheme.Operand.constant(Double.parseDouble(ref));
        }
        boolean onBenchmark = ref.startsWith("benchmark.");
        String name = onBenchmark ? ref.substring("benchmark.".length()) : ref;
        String carried = queryGenerator.getTargetMeasureReference();
        if (!name.equals(carried)) {
            throw new IllegalArgumentException(String.format(
                    "The delta operand '%s' does not resolve: the result carries the measure '%s'",
                    ref, carried));
        }
        if (onBenchmark && !hasBenchmark) {
            throw new IllegalArgumentException(
                    "The delta operand '" + ref + "' references the benchmark, but the query has no AGAINST clause");
        }
        return onBenchmark ? DeltaScheme.Operand.BENCHMARK : DeltaScheme.Operand.TARGET;
    }

    private DeltaScheme buildDeltaScheme(List<String> functions, String[] operandRefs, boolean hasBenchmark) {
        if (operandRefs == null) {
            return new DeltaScheme(functions);
        }
        return new DeltaScheme(functions,
                resolveOperand(operandRefs[0], hasBenchmark), resolveOperand(operandRefs[1], hasBenchmark));
    }

    private List<AssessComparison> buildComparisons(List<AssessBenchmark> benchmarks) {
        if (benchmarkSpecs.isEmpty()) {
            return Collections.singletonList(new AssessComparison(null, null,
                    buildDeltaScheme(deltaFunctions, deltaOperandRefs, false)));
        }
        List<AssessComparison> comparisons = new ArrayList<>();
        for (int i = 0; i < benchmarkSpecs.size(); i++) {
            BenchmarkSpec spec = benchmarkSpecs.get(i);
            comparisons.add(new AssessComparison(benchmarks.get(i), labelOf(spec.details),
                    buildDeltaScheme(spec.deltaFunctions, spec.operandRefs, true)));
        }
        return comparisons;
    }

    /** The label identifying a benchmark descriptor in results and reports. */
    private static String labelOf(List<String> details) {
        switch (BenchmarkKind.of(details)) {
            case CONSTANT: return "constant " + details.get(1);
            case SIBLING: return details.get(1) + " = '" + details.get(2) + "'";
            case PAST: return "past " + details.get(1);
            default: return details.get(0);
        }
    }

    /** Sets the LABELS clause's custom rule scheme, under the analyst's name when given. */
    public void addCustomLabeler(List<List<String>> rulesList, String name) {
        setLabeler(name == null
                ? new CustomLabelingScheme(rulesList)
                : new CustomLabelingScheme(rulesList, name));
    }

    /** Sets the ready-made scheme the LABELS clause names, configured by its arguments. */
    public void addNamedLabeler(String schemeName, List<String> args) {
        LabelingScheme scheme = SchemeCatalog.byName(schemeName, args);
        if (scheme == null) {
            throw new IllegalArgumentException("Unknown labeling scheme: " + schemeName);
        }
        setLabeler(scheme);
    }

    private void setLabeler(LabelingScheme scheme) {
        if (labeler != null) {
            throw new IllegalArgumentException("An assess query takes a single labeling scheme");
        }
        labeler = scheme;
    }

    public void setLabelingRules(List<List<String>> rulesList) {
        addCustomLabeler(rulesList, null);
    }

    public void buildLabelingScheme(String method) {
        addNamedLabeler(method, null);
    }

    public AssessQuery build() {
        List<List<String>> benchmarkDetails = new ArrayList<>();
        for (BenchmarkSpec spec : benchmarkSpecs) {
            benchmarkDetails.add(spec.details);
        }
        FetchedCubes cubes = fetcher.fetch(queryGenerator, benchmarkDetails);
        return new AssessQuery(
                cubes.targetQuery,
                cubes.targetCube,
                buildComparisons(cubes.benchmarks),
                labeler,
                outputName,
                cubes.stats);
    }
}
