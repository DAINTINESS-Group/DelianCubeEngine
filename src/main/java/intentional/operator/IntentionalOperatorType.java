package intentional.operator;

/**
 * The family of intentional operator to run. Describe and assess own their command alias directly; the
 * analyze family's alias is owned by the selected {@link IntentionalStrategy}.
 */
public enum IntentionalOperatorType {
    DESCRIBE("describe"),
    ASSESS("assess"),
    ANALYZE("analyze");

    public final String alias;

    IntentionalOperatorType(String alias) { this.alias = alias; }

    /**
     * Resolves the operator type for a command alias: an analyze variant maps to {@link #ANALYZE},
     * otherwise the alias must match a type directly.
     */
    public static IntentionalOperatorType fromAlias(String alias) {
        if (IntentionalStrategy.fromAlias(alias) != null) return ANALYZE;
        for (IntentionalOperatorType type : values()) {
            if (type.alias.equalsIgnoreCase(alias)) return type;
        }
        throw new IllegalArgumentException("Unknown Intentional Command: " + alias);
    }
}
