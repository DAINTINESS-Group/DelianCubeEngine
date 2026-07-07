package cubemanager.cubebase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link Measure} as used within a {@link CubeQuery}: the aggregated measure, plus the aggregation
 * function applied to it and an optional alias. It decomposes a raw measure string from a query and handles:
 * <ul>
 *   <li>Standard aggregation: {@code SUM(amount)} -> function="SUM", name="amount"</li>
 *   <li>Derived measures: {@code amount - payments} -> function="", name="amount - payments"</li>
 *   <li>Aliased measures: {@code SUM(amount) AS Total} -> function="SUM", name="amount", alias="Total"</li>
 * </ul>
 * When built from a raw string it carries only its name; it is fully resolved (with its cube
 * {@link cubemanager.physicalschema.Attribute}) when constructed from a resolved measure.
 */
public class QueryMeasure extends Measure {
    private String function;
    private String alias;

    /**
     * Converts "SUM(amount) AS Total" -> function="SUM", name="amount", alias="Total".
     */
    public QueryMeasure(String rawString) {
        this(parse(rawString));
    }

    private QueryMeasure(String[] parts) {
        this(parts[0], parts[1], parts[2]);
    }

    public QueryMeasure(String function, String attribute, String alias) {
        super(null, attribute, null);
        this.function = function;
        this.alias = alias;
    }

    public QueryMeasure(String function, Measure measure, String alias) {
        super(null, measure.getName(), measure.getAttribute());
        this.function = function;
        this.alias = alias;
    }

    public String getFunction() {
        return function;
    }

    /** The typed aggregation function, classified from the raw {@link #getFunction()} token. */
    public AggregationFunction getAggregationFunction() {
        return AggregationFunction.parse(function);
    }

    public String getAlias() {
        return alias;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    /** Splits a raw measure string into {function, attribute, alias}. */
    private static String[] parse(String rawString) {
        String workStr = rawString.trim();
        String alias = null;
        if (workStr.toUpperCase().contains(" AS ")) {
            String[] parts = workStr.split(" (?i)AS ");
            workStr = parts[0].trim();
            alias = parts[1].trim().replaceAll("['\"]", "");
        }
        String function;
        String attribute;
        Matcher matcher = Pattern.compile("^([a-zA-Z]+)\\((.*)\\)$").matcher(workStr);
        if (matcher.find()) {
            function = matcher.group(1);
            attribute = matcher.group(2);
        } else {
            function = "";
            attribute = workStr;
        }
        return new String[]{ function, attribute, alias };
    }
}
