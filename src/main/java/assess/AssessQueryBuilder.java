package assess;

import assess.benchmarks.AssessBenchmark;
import assess.benchmarks.BenchmarkFactory;
import assess.deltas.DeltaScheme;
import assess.labelers.CustomLabelingScheme;
import cubemanager.CubeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used after we have parsed a query with the AssessQueryParser to
 * build the AssessQuery object. <br>
 */
public class AssessQueryBuilder {

    //TODO
    //Clearly shown original cube query, or cubequery parts
    //Clearly shown benchmark cube query, or cubequery parts
    //Clearly show delta
    //Clearly show labeling scheme

    private final CubeManagerAdapter queryGenerator;
    private List<String> benchmarkDetails = new ArrayList<>(); // Default, as it can be empty
    private List<String> deltaFunctions;
    private List<List<String>> labelingRules;

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

    /* FUTURE: Create a factory method that contains the overriding methods
     * buildLabelingScheme.
     */
    public void setLabelingRules(List<List<String>> rulesList) {
        labelingRules = rulesList;
	}

    /**
     * FUTURE: Builds a labeling scheme based on a predefined method
     * @param method the predefined labeling method to be applied
     */
    public void buildLabelingScheme(String method) {
        System.out.println("I am not currently implemented!");
        System.out.println("Labeling Method: " + method);
    }

    public AssessQuery build() {
        return new AssessQuery(
                queryGenerator.translateToCubeQuery(),
                buildBenchmark(),
                new DeltaScheme(deltaFunctions),
                new CustomLabelingScheme(labelingRules));
    }
}
