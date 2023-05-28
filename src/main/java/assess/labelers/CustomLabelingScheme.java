package assess.labelers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This implementation of a labeling scheme is used when the user provides
 * a non-defined scheme. <br>
 * E.g. [-2, 5): Good, [5, 10]: Excellent
 */
public class CustomLabelingScheme implements LabelingScheme {

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

        private static final Map<String, EdgeCondition> stringToConditionMap = createMap();

        /**
         * Creates a map that translates edge condition characters to
         * EdgeCondition values
         *
         * @return stringToEdgeCondition map
         */
        private static Map<String, EdgeCondition> createMap() {
            Map<String, EdgeCondition> map = new HashMap<>();
            map.put("(", EdgeCondition.GREATER);
            map.put("[", EdgeCondition.GREATER_EQUAL);
            map.put(")", EdgeCondition.LESS);
            map.put("]", EdgeCondition.LESS_EQUAL);
            return map;
        }

        public LabelingRule(List<String> parsedRule) {
            lowCondition = stringToConditionMap.get(parsedRule.get(0));
            lowLimit = parseLimitValue(parsedRule.get(1));
            highLimit = parseLimitValue(parsedRule.get(2));
            highCondition = stringToConditionMap.get(parsedRule.get(3));
            label = parsedRule.get(4);

            originalRule = parsedRule.get(0) + parsedRule.get(1) + ", "
                    + parsedRule.get(2) + parsedRule.get(3);
        }

        private double parseLimitValue(String value) {
            if (value.equals("-inf")) {
                return -Double.MAX_VALUE;
            } else if (value.equals("inf") || value.equals("+inf")) {
                return Double.MAX_VALUE;
            }
            return Double.parseDouble(value);
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

        private String getOriginalRule() { return originalRule; }
    }

    List<LabelingRule> rules = new ArrayList<>();
    // Should we be keeping a set of labels so that results are unique as well?

    public CustomLabelingScheme(List<List<String>> rulesList) {
        for (List<String> parsedRule : rulesList) {
            LabelingRule newRule = new LabelingRule(parsedRule);
            validateRule(newRule);
            rules.add(newRule);
        }
    }

    private void validateRule(LabelingRule rule) {
        if (rule.lowLimit > rule.highLimit) {
            throw new InvalidLabelingRuleException(
                    rule.getOriginalRule(), "has low limit higher than high limit"
            );
        }
        if (!rules.isEmpty()) {
            compareToPreviousRule(rule);
        }
    }

    /**
     * After the first rule has been added, any new rule must abide to the
     * following rules:
     * <ol>
     * <li>We are following an ascending order (mostly for easier checking)</li>
     * <li>There are no overlapping on the edges (Limits must be different values
     * when both have equal conditions)</li>
     * <li>There is no range overlapping (Each rule should contain a unique range)</li>
     * </ol>
     */
    private void compareToPreviousRule(LabelingRule rule) {
        LabelingRule oldRule = rules.get(rules.size() - 1);

        if (!areRulesInAscendingOrder(oldRule, rule)) {
            throw new InvalidLabelingRuleException(
                    rule.getOriginalRule(), "does not follow ascending order"
            );
        }
        if (areRulesOverlappingOnEdges(oldRule, rule)) {
            throw new InvalidLabelingRuleException(
                    oldRule.getOriginalRule(),
                    rule.getOriginalRule(),
                    String.valueOf(rule.lowLimit)
            );
        }
        if (areRulesOverlappingOverRanges(oldRule, rule)) {
            throw new InvalidLabelingRuleException(
                    oldRule.getOriginalRule(),
                    rule.getOriginalRule(),
                    String.valueOf(rule.lowLimit),
                    String.valueOf(oldRule.highLimit)
            );
        }
    }

    private boolean areRulesInAscendingOrder(LabelingRule oldRule, LabelingRule newRule) {
        return oldRule.lowLimit < newRule.lowLimit;
    }

    /**
     * Check if the two rules contain an equal condition
     * (LESS_EQUAL & GREATER_EQUAL) and the high limit of the previous rule
     * equals the low limit of the new one.
     *
     * @param oldRule The previous rule that was created.
     * @param newRule The current rule that is created.
     * @return Edge overlapping check result.
     */
    private boolean areRulesOverlappingOnEdges(LabelingRule oldRule, LabelingRule newRule) {
        return oldRule.highCondition == LabelingRule.EdgeCondition.LESS_EQUAL &&
                newRule.lowCondition == LabelingRule.EdgeCondition.GREATER_EQUAL &&
                oldRule.highLimit == newRule.lowLimit;
    }


    /**
     * Check if the two rules contain overlapping ranges. <br>
     * Note: The second condition is used to avoid issues where the limits are
     * the same value. This check has already been done as part of the edge
     * overlapping assertion.
     *
     * @param oldRule The previous rule that was created
     * @param newRule the current rule that is created
     * @return Range overlapping check result
     */
    private boolean areRulesOverlappingOverRanges(LabelingRule oldRule, LabelingRule newRule) {
        return oldRule.containsValue(newRule.lowLimit) &&
                oldRule.highLimit != newRule.lowLimit;
    }

    @Override
    public String applyLabels(double value) {
        return rules.stream()
                .filter(rule -> rule.containsValue(value))
                .findFirst() // Find rule containing value
                .map(rule -> rule.label)
                .orElse("no match"); // If no rule contains value, return "no match"
    }
}
