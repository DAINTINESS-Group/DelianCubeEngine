package assess.labelers;

public class InvalidLabelingRuleException extends RuntimeException {

	/**
	 * This constructor is used in two occasions:
	 * <ol>
	 *     <li>Low limit is higher than the high limit,</li>
	 *     <li>The rule does not follow ascending order</li>
	 * </ol>
	 * @param rule The invalid rule.
	 * @param reason The reason why the rule is invalid.
	 */
	public InvalidLabelingRuleException(String rule, String reason) {
		super("Rule " + rule + " " + reason);
	}

	/**
	 * This constructor is used when there is edge overlapping.
	 * @param oldRule the previous rule
	 * @param newRule the current rule
	 * @param value the value over which they collide
	 */
	public InvalidLabelingRuleException(String oldRule, String newRule, String value) {
		super("Rules " + oldRule + " and " + newRule + " overlap on " + value);
	}

	/**
	 * This constructor is used when there is range overlapping.
	 * @param oldRule the previous rule.
	 * @param newRule the current rule.
	 * @param lowValue the starting point of the overlapping range.
	 * @param highValue the ending point of the overlapping range.
	 */
	public InvalidLabelingRuleException(
			String oldRule, String newRule,
			String lowValue, String highValue) {

		super("Rules " + oldRule + " and " + newRule + " overlap over range " +
				lowValue + " to " + highValue);
	}
}
