package result.highlights.metamodel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The parameters of an {@link Algorithm} run. At the metamodel level these stand for the algorithm's
 * <em>Parameter Roles</em> (the tunables it declares, e.g. a trend threshold); on an
 * {@link result.highlights.instance.AlgorithmExecution} the same object holds the concrete
 * <em>Parameter Instantiation</em> (the values a specific run was executed with). Externalizes what used
 * to be hardcoded constants inside the chart models. Insertion order is preserved so the instantiation
 * renders deterministically in a highlight's textual report.
 */
public final class AlgorithmParams {
    private final Map<String, Double> p = new LinkedHashMap<>();

    public AlgorithmParams set(String key, double value) { p.put(key, value); return this; }

    public double get(String key, double dflt) { return p.getOrDefault(key, dflt); }

    /** The instantiated parameters, in insertion order, read-only. */
    public Map<String, Double> values() { return Collections.unmodifiableMap(p); }

    /** Renders the instantiation as {@code key=value, ...}, for the "tested via" clause of a report. */
    @Override
    public String toString() {
        return p.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
    }
}
