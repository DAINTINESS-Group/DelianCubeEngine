package highlights.metamodel;

import java.util.Map;

/**
 * The type of result an {@link Algorithm} produces: a verdict and the auxiliary metrics behind it.
 * Materialized at the model level by {@link highlights.instance.AlgorithmResult}.
 */
public interface ResultType {

    boolean verdict();

    Map<String, Double> auxiliaryMetrics();
}
