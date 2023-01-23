package assess;

import assess.labelers.CustomLabelingScheme;
import assess.labelers.LabelingScheme;

import java.util.*;

/**
 * The AssessQueryBuilder class is called by the AssessParser to build the complex
 * parts of the AssessQuery object.
 */
public class AssessQueryBuilder {
    public void buildTargetCubeQuery(
            String targetName, String measurement,
            Map<String, String> selectionPredicates,
            Set<String> groupBySet) {
        System.out.println(
                "Target: " + targetName +
                "\nMeasurement: " + measurement +
                "\nSelectionPredicates: " + selectionPredicates +
                "\nGroupBySet: " + groupBySet);
    }

    public void buildBenchmarkCubeQuery() {
        // TODO: Build the benchmark query
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

    public LabelingScheme buildLabelingScheme(List<List<String>> rulesList) {
        System.out.println("Building Custom Labeling Scheme...");
		return new CustomLabelingScheme(rulesList);
	}

    public void buildLabelingScheme(String method) {
        System.out.println("Labeling Method: " + method);
    }
}
