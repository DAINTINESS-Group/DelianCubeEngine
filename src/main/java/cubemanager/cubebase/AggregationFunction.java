package cubemanager.cubebase;

/**
 * The closed vocabulary of aggregation functions a {@link QueryMeasure} may apply, together with the one
 * semantic property downstream reasoning needs: additivity (whether values may be summed across a
 * dimension). This is the typed source of truth for that reasoning; the raw lexeme is retained separately
 * on the QueryMeasure for faithful SQL/serialization round-trips.
 *
 * <ul>
 *   <li>{@link #DERIVED} — no aggregation function (a derived expression, e.g. {@code amount - payments});</li>
 *   <li>{@link #UNKNOWN} — a token outside this vocabulary.</li>
 * </ul>
 */
public enum AggregationFunction {
    SUM(true),
    COUNT(true),
    AVG(false),
    MIN(false),
    MAX(false),
    DERIVED(false),
    UNKNOWN(false);

    /** Whether values aggregated by this function may be meaningfully summed across a dimension. */
    public final boolean additive;

    AggregationFunction(boolean additive) { this.additive = additive; }

    /**
     * Classifies a raw function token (as carried by {@link QueryMeasure#getFunction()}). Case-insensitive
     * and tolerant of a trailing argument list; an empty/absent token is {@link #DERIVED}, an unrecognized
     * one {@link #UNKNOWN}.
     */
    public static AggregationFunction parse(String raw) {
        if (raw == null) return DERIVED;
        String fn = raw.trim();
        int paren = fn.indexOf('(');
        if (paren >= 0) fn = fn.substring(0, paren).trim();
        if (fn.isEmpty()) return DERIVED;
        switch (fn.toLowerCase()) {
            case "sum":               return SUM;
            case "count":             return COUNT;
            case "avg": case "average": return AVG;
            case "min":               return MIN;
            case "max":               return MAX;
            default:                  return UNKNOWN;
        }
    }
}
