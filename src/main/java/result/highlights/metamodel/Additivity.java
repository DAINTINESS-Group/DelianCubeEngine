package result.highlights.metamodel;

/**
 * Classifies aggregation functions by additivity — whether the aggregated values can be meaningfully
 * summed across a dimension. {@code sum} and {@code count} are additive; {@code max}, {@code min} and
 * {@code avg} are not. The function is matched on its leading token, so {@code "sum"} and
 * {@code "sum(amount)"} are treated the same.
 */
public final class Additivity {

    private Additivity() {}

    public static boolean isAdditive(String aggregation) {
        if (aggregation == null) return false;
        String fn = aggregation;
        int paren = fn.indexOf('(');
        if (paren >= 0) fn = fn.substring(0, paren);
        fn = fn.trim().toLowerCase();
        return fn.equals("sum") || fn.equals("count");
    }
}
