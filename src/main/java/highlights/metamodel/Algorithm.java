package highlights.metamodel;

import java.util.List;

/**
 * The testing logic an {@link ArchetypeProperty} utilizes to evaluate its hypothesis: its identity and the
 * {@link ParameterRole}s it declares. Checking applicability against a dataset and executing it to produce a
 * result are model-level concerns, declared by {@link highlights.instance.ExecutableAlgorithm}.
 */
public interface Algorithm {

    String name();

    List<ParameterRole> parameterRoles();
}
