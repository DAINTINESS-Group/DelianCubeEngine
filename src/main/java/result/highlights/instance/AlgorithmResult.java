package result.highlights.instance;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The structured result of an algorithm execution: the verdict (does the archetype hold?) and a set of
 * auxiliary metrics (e.g. {@code {MSE, R2, pValue, tau}}).
 */
public final class AlgorithmResult {
    public final boolean verdict;
    public final Map<String, Double> auxiliaryMetrics = new LinkedHashMap<>();

    public AlgorithmResult(boolean verdict) { this.verdict = verdict; }

    public AlgorithmResult metric(String key, double value) { auxiliaryMetrics.put(key, value); return this; }
}
