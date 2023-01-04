package assess;

import java.util.HashMap;
import java.util.HashSet;

/**
 * The AssessQueryBuilder class is called by the AssessParser to build the complex
 * parts of the AssessQuery object.
 */
public class AssessQueryBuilder {
    public void buildTargetCube(
            String targetName, String measurement,
            HashMap<String, String> selectionPredicates,
            HashSet<String> groupBySet) {
        System.out.println(
                "Target: " + targetName +
                "\nMeasurement: " + measurement +
                "\nSelectionPredicates: " + selectionPredicates +
                "\nGroupBySet: " + groupBySet);
    }
}
