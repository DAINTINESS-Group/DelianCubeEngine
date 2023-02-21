package assess;

import assess.deltas.DeltaScheme;
import assess.labelers.CustomLabelingScheme;
import assess.labelers.LabelingScheme;
import cubemanager.CubeManager;
import cubemanager.cubebase.Cube;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * This class is used after we have parsed a query with the AssessQueryParser to
 * build the AssessQuery object. <br>
 * TODO: Must follow pattern more closely (?)
 */
public class AssessQueryBuilder {

    //TODO
    //Clearly shown original cube query, or cubequery parts
    //Clearly shown benchmark cube query, or cubequery parts
    //Clearly show delta
    //Clearly show labeling scheme
    private final CubeManager cubeManager;
    private String targetCubeName;
    private Map<String, String> selectionPredicates;
    private String aggregationFunction;
    private String measurement;
    private Set<String> groupBySet;
    private List<String> benchmarkDetails;
    private List<String> deltaFunctions;
    private List<List<String>> labelingRules;


    public AssessQueryBuilder(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    public void setTargetCubeName(String targetCubeName) {
        this.targetCubeName = targetCubeName.toLowerCase(); // Assuming cubes are stored in lowercase
    }

    public void setAggregationFunction(String aggregationFunction) {
        this.aggregationFunction = aggregationFunction.toLowerCase();
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public void setSelectionPredicates(Map<String, String> selectionPredicates) {
        this.selectionPredicates = selectionPredicates;
    }

    public void setGroupBySet(Set<String> groupBySet) {
        this.groupBySet = groupBySet;
    }

    public void setBenchmarkDetails(List<String> benchmarkDetails) {
        this.benchmarkDetails = benchmarkDetails;
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
        templateQuery += "Gamma:" + collectGammaLevels() + "\n";
        templateQuery += "Sigma:" + collectSigmaLevels();
        return templateQuery;
    }

    private String collectSigmaLevels() {
        Cube targetCube = cubeManager.getCubeByName(targetCubeName);
        StringJoiner stringJoiner = new StringJoiner(",");
        selectionPredicates.forEach((key, value) -> {
            String result = targetCube.findLevelByName(key);
            result += "='" + value + "'";
            stringJoiner.add(result);
        });
        return stringJoiner.toString();
    }

    private String collectGammaLevels() {
        Cube targetCube = cubeManager.getCubeByName(targetCubeName);
        StringJoiner stringJoiner = new StringJoiner(",");
        groupBySet.forEach(attribute ->
                stringJoiner.add(targetCube.findLevelByName(attribute)));
        return stringJoiner.toString();
    }

    private String buildBenchmarkCubeQuery() {
        // This will be a factory method
        switch (benchmarkDetails.get(0)) {
            case "Sibling": return "Sibling Benchmark";
            case "Past": return "Past Benchmark";
            case "External": return "External Benchmark";
            case "Constant": return "Constant Benchmark";
            default: throw new RuntimeException("Unrecognized Benchmark Type");
        }
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
        String targetCubeQuery = buildStringQueryTemplate();
        String benchmarkQuery = buildBenchmarkCubeQuery();
        DeltaScheme deltaScheme = new DeltaScheme(deltaFunctions);
        LabelingScheme labelingScheme = new CustomLabelingScheme(labelingRules);
        return new AssessQuery(targetCubeQuery, benchmarkQuery, deltaScheme, labelingScheme);
    }
}
