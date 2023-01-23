package assess;

import assess.labelers.LabelingScheme;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void buildCustomLabelingScheme() {
        AssessQueryBuilder builder = new AssessQueryBuilder();
        List<List<String>> rules = new ArrayList<>();
        rules.add(Arrays.asList("(", "-inf", "-10.0", ")", "bad"));
        rules.add(Arrays.asList("[", "-10.0", "10.0", "]", "average"));
        rules.add(Arrays.asList("(", "10.0", "inf", "]", "good"));

        LabelingScheme labelingScheme = builder.buildLabelingScheme(rules);

        assertEquals("bad", labelingScheme.applyLabels(-12));
        assertEquals("average", labelingScheme.applyLabels(10.0));
        assertEquals("good", labelingScheme.applyLabels(100));
    }
}
