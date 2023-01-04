package assess;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

public class AssessQueryBuilderTest {
    @Test
    public void givenNecessaryInput_whenAskedToBuildTarget_thenReturnTargetCube() {
        String targetCubeName = "SALES";
        String measurement = "storeSales";

        HashMap<String, String> selectionPredicates = new HashMap<>();
        selectionPredicates.put("type", "Fresh Fruit");
        selectionPredicates.put("country", "Italy");

        HashSet<String> groupBySet = new HashSet<>();
        groupBySet.add("product");
        groupBySet.add("country");

        // Note: We have to decide whether the query or the object should be returned
    }
}
