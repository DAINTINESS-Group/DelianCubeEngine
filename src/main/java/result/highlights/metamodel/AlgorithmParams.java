package result.highlights.metamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete parameter instantiations for an {@link Algorithm} run (e.g. a trend threshold).
 * Externalizes what used to be hardcoded constants inside the chart models.
 */
public final class AlgorithmParams {
    private final Map<String, Double> p = new HashMap<>();

    public AlgorithmParams set(String key, double value) { p.put(key, value); return this; }

    public double get(String key, double dflt) { return p.getOrDefault(key, dflt); }
}
