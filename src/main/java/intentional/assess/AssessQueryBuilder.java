package intentional.assess;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.benchmarks.BenchmarkFactory;
import intentional.assess.deltas.DeltaScheme;
import intentional.labeling.LabelingScheme;
import intentional.labeling.schemes.CustomLabelingScheme;
import intentional.labeling.schemes.SchemeCatalog;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;

import java.util.ArrayList;
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

    private final CubeManagerAdapter queryGenerator;
    private List<String> benchmarkDetails = new ArrayList<>(); // Default, as it can be empty
    private List<String> deltaFunctions;
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
        this.benchmarkDetails = benchmarkDetails;
        return this;
    }

    private AssessBenchmark buildBenchmark() {
        return new BenchmarkFactory(queryGenerator)
                .createBenchmark(benchmarkDetails);
    }

    public void setDeltaFunctions(List<String> methods) {
        deltaFunctions = methods;
    }

    /** The operand references of the USING clause's innermost call, as written. */
    public void setDeltaOperands(String first, String second) {
        this.deltaOperandRefs = new String[]{first, second};
    }

    /**
     * Binds an operand reference: a number is a constant, {@code benchmark.X} the benchmark's measure,
     * a bare name the target's measure — where X must be the measure the result actually carries.
     */
    private DeltaScheme.Operand resolveOperand(String ref) {
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
        if (onBenchmark && benchmarkDetails.isEmpty()) {
            throw new IllegalArgumentException(
                    "The delta operand '" + ref + "' references the benchmark, but the query has no AGAINST clause");
        }
        return onBenchmark ? DeltaScheme.Operand.BENCHMARK : DeltaScheme.Operand.TARGET;
    }

    private DeltaScheme buildDeltaScheme() {
        if (deltaOperandRefs == null) {
            return new DeltaScheme(deltaFunctions);
        }
        return new DeltaScheme(deltaFunctions,
                resolveOperand(deltaOperandRefs[0]), resolveOperand(deltaOperandRefs[1]));
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
        CubeQuery targetCubeQuery = queryGenerator.translateToCubeQuery();
        return new AssessQuery(
                targetCubeQuery,
                queryGenerator.executeCubeQuery(targetCubeQuery),
                buildBenchmark(),
                buildDeltaScheme(),
                labelers,
                outputName);
    }
}
