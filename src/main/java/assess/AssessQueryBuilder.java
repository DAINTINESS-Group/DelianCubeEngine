package assess;

import assess.benchmarks.AssessBenchmark;
import assess.benchmarks.BenchmarkFactory;
import assess.deltas.DeltaScheme;
import assess.labelers.CustomLabelingScheme;
import assess.labelers.LabelingScheme;
import cubemanager.CubeManager;
import cubemanager.cubebase.Cube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

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
    private final CubeManager cubeManager;
    private String targetCubeName;
    private Map<String, String> selectionPredicates = new HashMap<>(); // Empty value, as selection predicates are optional
    private String aggregationFunction;
    private String measurement;
    private Set<String> groupBySet;
    private List<String> benchmarkDetails = new ArrayList<>(); // Default, as it can be empty
    private List<String> deltaFunctions;
    private List<List<String>> labelingRules;


    public AssessQueryBuilder(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    public AssessQueryBuilder setTargetCubeName(String targetCubeName) {
        this.targetCubeName = targetCubeName.toLowerCase(); // Assuming cubes are stored in lowercase
        return this;
    }

    public AssessQueryBuilder setAggregationFunction(String aggregationFunction) {
        this.aggregationFunction = aggregationFunction.toLowerCase();
        return this;
    }

    public AssessQueryBuilder setMeasurement(String measurement) {
        this.measurement = measurement;
        return this;
    }

    public AssessQueryBuilder setSelectionPredicates(Map<String, String> selectionPredicates) {
        this.selectionPredicates = selectionPredicates;
        return this;
    }

    public AssessQueryBuilder setGroupBySet(Set<String> groupBySet) {
        this.groupBySet = groupBySet;
        return this;
    }

    public AssessQueryBuilder setBenchmarkDetails(List<String> benchmarkDetails) {
        this.benchmarkDetails = benchmarkDetails;
        return this;
    }

    /**
     * Call on the {@link cubemanager.CubeManager#createCubeQueryFromString(String, HashMap)}
     * method to create the CubeQuery.
     */
    private String buildStringQueryTemplate() {
        String templateQuery = "CubeName:" + targetCubeName + "\n";
        templateQuery += "Name:" + targetCubeName + "_" + measurement + "\n";
        templateQuery += "AggrFunc:" + aggregationFunction + "\n";
        templateQuery += "Measure:" + measurement + "\n";
        templateQuery += "Gamma:" + collectGammaLevels(targetCubeName + "_cube") + "\n";
        templateQuery += "Sigma:" + collectSigmaLevels(targetCubeName + "_cube");
        return templateQuery;
    }

    private String collectSigmaLevels(String targetCubeName) {
        // For some reason cubes are stored with the _cube suffix in memory
        Cube targetCube = cubeManager.getCubeByName(targetCubeName);
        StringJoiner stringJoiner = new StringJoiner(",");
        selectionPredicates.forEach((key, value) -> {
            String result = targetCube.findLevelByName(key);
            result += "='" + value + "'";
            stringJoiner.add(result);
        });
        return stringJoiner.toString();
    }

    private String collectGammaLevels(String targetCubeName) {
        // For some reason cubes are stored with the _cube suffix in memory
        Cube targetCube = cubeManager.getCubeByName(targetCubeName);
        StringJoiner stringJoiner = new StringJoiner(",");
        groupBySet.forEach(attribute ->
                stringJoiner.add(targetCube.findLevelByName(attribute)));
        return stringJoiner.toString();
    }

    private AssessBenchmark buildBenchmark() {
        return new BenchmarkFactory(benchmarkDetails)
                .createBenchmarkQuery();
    }

    public AssessQueryBuilder setDeltaFunctions(List<String> methods) {
        deltaFunctions = methods;
        return this;
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
        String targetCubeQuery = buildStringQueryTemplate();
        AssessBenchmark benchmark = buildBenchmark();
        DeltaScheme deltaScheme = new DeltaScheme(deltaFunctions);
        LabelingScheme labelingScheme = new CustomLabelingScheme(labelingRules);
        return new AssessQuery(targetCubeQuery, benchmark, deltaScheme, labelingScheme);
    }
}
