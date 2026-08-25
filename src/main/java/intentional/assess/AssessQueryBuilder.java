package intentional.assess;

import intentional.assess.benchmarks.BenchmarkFactory;
import intentional.assess.deltas.DeltaScheme;
import intentional.labeling.LabelingScheme;
import intentional.labeling.schemes.CustomLabelingScheme;
import intentional.labeling.schemes.SchemeCatalog;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;

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
    private final List<BenchmarkSpec> benchmarkSpecs = new ArrayList<>(); // Empty when there is no AGAINST clause
    private List<String> deltaFunctions; // The USING chain of a benchmark-less query
    private final List<LabelingScheme> labelers = new ArrayList<>();
    private String outputName = null;
    private String[] deltaOperandRefs;

    public AssessQueryBuilder(CubeManager cubeManager) {
        queryGenerator = new CubeManagerAdapter(cubeManager);
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

    private List<AssessComparison> buildComparisons() {
        if (benchmarkSpecs.isEmpty()) {
            return Collections.singletonList(new AssessComparison(null,
                    buildDeltaScheme(deltaFunctions, deltaOperandRefs, false)));
        }
        BenchmarkFactory factory = new BenchmarkFactory(queryGenerator);
        List<AssessComparison> comparisons = new ArrayList<>();
        for (BenchmarkSpec spec : benchmarkSpecs) {
            comparisons.add(new AssessComparison(factory.createNamed(spec.details),
                    buildDeltaScheme(spec.deltaFunctions, spec.operandRefs, true)));
        }
        return comparisons;
    }

    /** Appends a custom rule scheme from the LABELS clause, under the analyst's name when given. */
    public void addCustomLabeler(List<List<String>> rulesList, String name) {
        labelers.add(name == null
                ? new CustomLabelingScheme(rulesList)
                : new CustomLabelingScheme(rulesList, name));
    }

    /** Appends a ready-made scheme the LABELS clause names, configured by its arguments. */
    public void addNamedLabeler(String schemeName, List<String> args) {
        LabelingScheme scheme = SchemeCatalog.byName(schemeName, args);
        if (scheme == null) {
            throw new IllegalArgumentException("Unknown labeling scheme: " + schemeName);
        }
        labelers.add(scheme);
    }

    public void setLabelingRules(List<List<String>> rulesList) {
        addCustomLabeler(rulesList, null);
    }

    public void buildLabelingScheme(String method) {
        addNamedLabeler(method, null);
    }

    public AssessQuery build() {
        if (benchmarkSpecs.size() > 1 && labelers.size() > 1) {
            throw new IllegalArgumentException(
                    "Multiple benchmarks assess under a single labeling scheme; got "
                            + benchmarkSpecs.size() + " benchmarks and " + labelers.size() + " schemes");
        }
        CubeQuery targetCubeQuery = queryGenerator.translateToCubeQuery();
        return new AssessQuery(
                targetCubeQuery,
                queryGenerator.executeCubeQuery(targetCubeQuery),
                buildComparisons(),
                labelers,
                outputName);
    }
}
