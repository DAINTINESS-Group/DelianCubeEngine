package assess;

import assess.labelers.CustomLabelingScheme;
import assess.labelers.LabelingScheme;
import cubemanager.CubeManager;
import cubemanager.cubebase.Cube;

import java.util.*;

/**
 * This class is used after we have parsed a query with the AssessQueryParser to
 * build the AssessQuery object.
 */
public class AssessQueryBuilder {

    //TODO
    //Clearly shown original cube query, or cubequery parts
    //Clearly shown benchmark cube query, or cubequery parts
    //Clearly show delta
    //Clearly show labeling scheme
    private CubeManager cubeManager;

    private String targetCubeName;
    private String measurement;
    private Map<String, String> selectionPredicates;
    private Set<String> groupBySet;
    private String benchmark;

    // TODO: Need a way to represent all of the comparison methods to be applied.
    private LabelingScheme labelingScheme;

    public AssessQueryBuilder(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    public void setTargetCubeName(String targetCubeName) {
        this.targetCubeName = targetCubeName.toLowerCase(); // Assuming cubes are stored in lowercase
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

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    /**
     * Call on the {@link cubemanager.CubeManager#createCubeQueryFromString(String, HashMap)}
     * method to create the CubeQuery.
     */
    public String buildTargetCubeQueryTemplate() {
        String templateQuery = "CubeName:" + targetCubeName + "\n";
        templateQuery += "Name:" + targetCubeName + "_" + measurement + "\n";
        templateQuery += "AggrFunc:" + "??" + "\n"; // Replace with what it should actually be
        templateQuery += "Measure:" + measurement + "\n";
        templateQuery += "Gamma:" + collectGammaLevels() + "\n"; // Ask the CubeManager to get the level(s)
        templateQuery += "Sigma:" + collectSigmaLevels(); // Ask the CubeManager to get the level(s)
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
        selectionPredicates.forEach((key, value) -> {
            String result = targetCube.findLevelByName(key);
            stringJoiner.add(result);
        });
        return stringJoiner.toString();
    }

    public void buildBenchmarkCubeQuery() {
        System.out.println(benchmark);
        // TODO: Build the benchmark query
        // What if a benchmark is not defined in the Query?
    }

    /**
     * This method accepts a list of comparison method names and
     * initializes the delta scheme.
     * @param methods The Comparison methods to be used
     */
    public void buildDeltaScheme(List<String> methods) {
        System.out.println("Building the Comparison Scheme");
        for (String method : methods)
            System.out.println(method);
    }

    public void buildLabelingScheme(List<List<String>> rulesList) {
        System.out.println("Building Custom Labeling Scheme...");
        labelingScheme = new CustomLabelingScheme(rulesList);
	}

    /**
     * Builds a labeling scheme based on a predefined method
     * @param method the predefined labeling method to be applied
     */
    public void buildLabelingScheme(String method) {
        System.out.println("Labeling Method: " + method);
    }

    public AssessQuery build() {
        return new AssessQuery(null, null, null, labelingScheme);
    }
}
