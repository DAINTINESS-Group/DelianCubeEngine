package assess;

import assess.labelers.LabelingScheme;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AssessQueryBuilderTest {

    @Test public void
    givenSetOfLabelingRules_whenAskedToBuildLabelingScheme_thenReturnACustomScheme() {
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
