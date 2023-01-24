package assess.labelers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CustomLabelingSchemeTest {
	@Test public void
	givenSetOfRules_whenAskedToCreateScheme_thenReturnAFunctioningLabelingScheme() {
		List<List<String>> rules;
		rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "-inf", "-10.0", ")", "bad"));
		rules.add(Arrays.asList("[", "-10.0", "10.0", "]", "average"));
		rules.add(Arrays.asList("(", "10.0", "inf", "]", "good"));

		LabelingScheme labelingScheme = new CustomLabelingScheme(rules);

		assertEquals("bad", labelingScheme.applyLabels(-12));
		assertEquals("average", labelingScheme.applyLabels(10.0));
		assertEquals("good", labelingScheme.applyLabels(100));
	}


	 /* Test 1: Given three rules, e.g. (-20, -10), [-10, 10], [10, 20],
	throw an OverlappingRulesException between rules [-10, 10] and [10, 20]
	on value 10 as both are closed limits. Note: rules (-12, -10), [-10, 10]
	do not clash, as the first rule does not contain an equal condition on -10.
	 */
	@Test
	public void givenSetOfRules_whenRulesAreOverlapping_thenThrowOverlappingException() {
		List<List<String>> rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "-20.0", "-10.0", ")", "bad"));
		rules.add(Arrays.asList("[", "-10.0", "10.0", "]", "average"));
		rules.add(Arrays.asList("[", "10.0", "20.0", "]", "good"));
		String expectedExceptionMessage =
				"[-10.0, 10.0] and [10.0, 20.0] overlap on 10.0";

		OverlappingLabelingRulesException actualException =
				assertThrows(OverlappingLabelingRulesException.class, () -> {
					new CustomLabelingScheme(rules);
				});

		assertEquals(expectedExceptionMessage, actualException.getMessage());
	}

    /* Test 2: Given a value that can not be labeled (no rule contains it),
    throw an ValueOutsideSystemRange Exception.
     */
}
