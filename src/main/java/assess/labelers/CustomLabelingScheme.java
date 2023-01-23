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

		public LabelingRule(List<String> dataString) {
			Map<String, EdgeCondition> stringToConditionMap = createMap();

			lowCondition = stringToConditionMap.get(dataString.get(0));

			if (dataString.get(1).equals("-inf")) {
				lowLimit = -Double.MAX_VALUE;
			} else {
				lowLimit = Double.parseDouble(dataString.get(1));
			}

			if (dataString.get(2).equals("inf")) {
				highLimit = Double.MAX_VALUE;
			} else {
				highLimit = Double.parseDouble(dataString.get(2));
			}
			highCondition = stringToConditionMap.get(dataString.get(3));
			label = dataString.get(4);
		}

		private static Map<String, EdgeCondition> createMap() {
			Map<String, EdgeCondition> map = new HashMap<>();
			map.put("(", EdgeCondition.GREATER);
			map.put("[", EdgeCondition.GREATER_EQUAL);
			map.put(")", EdgeCondition.LESS);
			map.put("]", EdgeCondition.LESS_EQUAL);
			return map;
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
	}

	List<LabelingRule> rules = new ArrayList<>();
	
	public CustomLabelingScheme(List<List<String>> rulesList) {
		/*
			We need two checks here
			1. There is no rule overlapping.
			2. All possible values are contained.
		 */
		for (List<String> rule : rulesList)
			rules.add(new LabelingRule(rule));
	}

	@Override
	public String applyLabels(double value) {
		for (LabelingRule rule : rules)
			if (rule.containsValue(value))
				return rule.label;
		return null; // TODO: Throw not found exception?
	}
}
