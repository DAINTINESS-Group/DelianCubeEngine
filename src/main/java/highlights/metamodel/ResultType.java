package highlights.metamodel;

import java.util.Map;

/**
 * The result an {@link Algorithm} produces: the verdict and the auxiliary metrics behind it.
 * ArchetypeResult materializes it.
 */
public interface ResultType {

    boolean verdict();

    Map<String, Double> auxiliaryMetrics();
}
