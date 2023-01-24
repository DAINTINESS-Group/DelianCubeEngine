package assess.labelers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This implementation of a labeling scheme is used when the user provides
 * a non-defined scheme. <br> E.g. [-2, 5): Good, [5, 10]: Excellent
 */
public class CustomLabelingScheme implements LabelingScheme{

	private static class LabelingRule {

		private enum EdgeCondition {
			LESS, LESS_EQUAL,
			GREATER, GREATER_EQUAL
		}

		private final double lowLimit;
		private final double highLimit;
		private final EdgeCondition lowCondition;
		private final EdgeCondition highCondition;
		private final String label;

		private final String originalRule;

		private static Map<String, EdgeCondition> createMap() {
			Map<String, EdgeCondition> map = new HashMap<>();
			map.put("(", EdgeCondition.GREATER);
			map.put("[", EdgeCondition.GREATER_EQUAL);
			map.put(")", EdgeCondition.LESS);
			map.put("]", EdgeCondition.LESS_EQUAL);
			return map;
		}

		public LabelingRule(List<String> parsedRule) {
			Map<String, EdgeCondition> stringToConditionMap = createMap();

			lowCondition = stringToConditionMap.get(parsedRule.get(0));

			if (parsedRule.get(1).equals("-inf")) {
				lowLimit = -Double.MAX_VALUE;
			} else {
				lowLimit = Double.parseDouble(parsedRule.get(1));
			}

			if (parsedRule.get(2).equals("inf")) {
				highLimit = Double.MAX_VALUE;
			} else {
				highLimit = Double.parseDouble(parsedRule.get(2));
			}
			highCondition = stringToConditionMap.get(parsedRule.get(3));
			label = parsedRule.get(4);

			originalRule = parsedRule.get(0) + parsedRule.get(1) + ", "
					+ parsedRule.get(2) + parsedRule.get(3);
		}


		private boolean isGreaterThanLowLimit(double testedValue) {
			if (lowCondition == EdgeCondition.GREATER) {
				return testedValue > lowLimit;
			}
			return testedValue >= lowLimit;
		}

		private boolean isLessThanHighLimit(double testedValue) {
			if (highCondition == EdgeCondition.LESS) {
				return testedValue < highLimit;
			}
			return testedValue <= highLimit;
		}

		public boolean containsValue(double value) {
			return isGreaterThanLowLimit(value) && isLessThanHighLimit(value);
		}
		public String getOriginalRule() {
			return originalRule;
		}
	}

	List<LabelingRule> rules = new ArrayList<>();
	
	public CustomLabelingScheme(List<List<String>> rulesList)
			throws OverlappingLabelingRulesException {
		LabelingRule oldRule = null;
		// TODO: Force ascending order of rules.

		for (List<String> rule : rulesList) {
			LabelingRule newRule = new LabelingRule(rule);
			if (oldRule != null && areRulesOverlapping(oldRule, newRule)) {
				throw new OverlappingLabelingRulesException(
						oldRule.getOriginalRule(),
						newRule.getOriginalRule(),
						String.valueOf(newRule.lowLimit));
			}

			rules.add(newRule);
			oldRule = newRule;
		}
	}

	private boolean areRulesOverlapping(LabelingRule oldRule, LabelingRule newRule) {
		return oldRule.highCondition == LabelingRule.EdgeCondition.LESS_EQUAL &&
				newRule.lowCondition == LabelingRule.EdgeCondition.GREATER_EQUAL &&
				oldRule.highLimit == newRule.lowLimit;
	}

	@Override
	public String applyLabels(double value) {
		for (LabelingRule rule : rules)
			if (rule.containsValue(value))
				return rule.label;
		return null; // TODO: Throw not found exception?
	}
}
