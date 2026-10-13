package intentional.operator;

/**
 * The intentional variant to run. Currently only for analyze,
 * the Iakovidis operator or one of the multi-query-optimization strategies.
 * Each owns the command alias that selects it.
 */
public enum IntentionalStrategy {
    IAKOVIDIS("analyze_iakovidis"),
    MIN_MQO("analyze_min_mqo"),
    MAX_MQO("analyze_max_mqo"),
    MID_MQO("analyze_mid_mqo");

    public final String alias;

    IntentionalStrategy(String alias) { this.alias = alias; }

    /** The strategy for a command alias, or null if the alias is not an analyze variant. */
    public static IntentionalStrategy fromAlias(String alias) {
        for (IntentionalStrategy strategy : values()) {
            if (strategy.alias.equalsIgnoreCase(alias)) return strategy;
        }
        return null;
    }
}
