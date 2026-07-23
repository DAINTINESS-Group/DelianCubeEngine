package highlights.instance;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import highlights.metamodel.ResultType;

/**
 * The Result of an {@link AlgorithmExecution}: the verdict on whether the tested archetype property holds,
 * together with the auxiliary metrics behind it (e.g. a p-value or a member count).
 */
public final class AlgorithmResult implements ResultType {

    private final boolean verdict;
    private final Map<String, Double> auxiliaryMetrics = new LinkedHashMap<>();

    public AlgorithmResult(boolean verdict) {
        this.verdict = verdict;
    }

    /** Records a technical metric behind the verdict; returns this for chaining. */
    public AlgorithmResult metric(String key, double value) {
        auxiliaryMetrics.put(key, value);
        return this;
    }

    @Override
    public boolean verdict() { return verdict; }

    @Override
    public Map<String, Double> auxiliaryMetrics() { return Collections.unmodifiableMap(auxiliaryMetrics); }
}
