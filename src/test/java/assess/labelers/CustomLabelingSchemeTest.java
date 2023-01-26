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
		List<List<String>> rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "-inf", "-10.0", ")", "bad"));
		rules.add(Arrays.asList("[", "-10.0", "10.0", "]", "average"));
		rules.add(Arrays.asList("(", "10.0", "inf", "]", "good"));

		LabelingScheme labelingScheme = new CustomLabelingScheme(rules);

		assertEquals("bad", labelingScheme.applyLabels(-12));
		assertEquals("average", labelingScheme.applyLabels(10.0));
		assertEquals("good", labelingScheme.applyLabels(100));
	}
	@Test
	public void givenRuleWithInvalidLimits_whenAskedToCreateScheme_thenThrowInvalidLabelingRuleException() {
		List<List<String>> rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "inf", "-inf", ")", "everything"));
		String expectedExceptionMessage =
				"Rule (inf, -inf) has low limit higher than high limit";

		InvalidLabelingRuleException actualException =
				assertThrows(InvalidLabelingRuleException.class, () ->
					new CustomLabelingScheme(rules)
				);

		assertEquals(expectedExceptionMessage, actualException.getMessage());

	}

	/**
	 * Check 1: Every new rule should have a low limit higher than the low limit
	 * of the previous rule, thus ensuring ascending ordering of rules.
	 */
	@Test public void
	givenSetOfRules_whenAskedToCreateScheme_thenAssertAscendingOrderOfRules() {
		List<List<String>> rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "-10.0", "-10.0", ")", "bad"));
		rules.add(Arrays.asList("[", "-20.0", "10.0", "]", "average"));
		String expectedExceptionMessage =
				"Rule [-20.0, 10.0] does not follow ascending order";

		InvalidLabelingRuleException actualException =
			assertThrows(InvalidLabelingRuleException.class, () ->
				new CustomLabelingScheme(rules)
			);

		assertEquals(expectedExceptionMessage, actualException.getMessage());
	}

	 /** Check 2: Given three rules, e.g. (-20, -10), [-10, 10], [10, 20],
	throw an InvalidLabelingRuleException between rules [-10, 10] and [10, 20]
	on value 10 as both are closed limits. <br> Note: rules (-12, -10), [-10, 10]
	do not clash, as the first rule does not contain an equal condition on -10.
	 */
	@Test public void
	givenSetOfRules_whenRulesAreOverlappingOnEdges_thenThrowOverlappingException() {
		List<List<String>> rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "-20.0", "-10.0", ")", "bad"));
		rules.add(Arrays.asList("[", "-10.0", "10", "]", "average"));
		rules.add(Arrays.asList("[", "10.0", "20.0", "]", "good"));
		String expectedExceptionMessage =
				"Rules [-10.0, 10] and [10.0, 20.0] overlap on 10.0";

		InvalidLabelingRuleException actualException =
				assertThrows(InvalidLabelingRuleException.class, () ->
					new CustomLabelingScheme(rules)
				);

		assertEquals(expectedExceptionMessage, actualException.getMessage());
	}

	/** Check 3: Given two rules with overlapping ranges, e.g., (-20, 10], [0, 20]
	throw an InvalidLabelingRuleException for the range 0 to 10.
	 */
	@Test
	public void givenSetOfRules_whenRulesAreOverlappingOverRanges_thenThrowOverlappingException() {
		List<List<String>> rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "-20", "10", "]", "bad"));
		rules.add(Arrays.asList("[", "0", "20", "]", "bad"));

		String expectedExceptionMessage =
				"Rules (-20, 10] and [0, 20] overlap over range 0.0 to 10.0";

		InvalidLabelingRuleException actualException =
				assertThrows(InvalidLabelingRuleException.class, () ->
					new CustomLabelingScheme(rules)
				);

		assertEquals(expectedExceptionMessage, actualException.getMessage());
	}

	/* If a value cannot be labeled, it's not an issue of the labeling system.
	 * The user must be notified that of the rules he/she provided are
	 * not sufficient.
	 */
}
