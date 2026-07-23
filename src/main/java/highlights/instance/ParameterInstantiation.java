package highlights.instance;

import highlights.metamodel.ParameterRole;

/**
 * The value an {@link AlgorithmExecution} bound to one of its algorithm's {@link ParameterRole}s.
 */
public final class ParameterInstantiation {
    public final ParameterRole role;
    public final double value;

    public ParameterInstantiation(ParameterRole role, double value) {
        this.role = role;
        this.value = value;
    }

    /** Instantiates the role to its declared default. */
    public static ParameterInstantiation ofDefault(ParameterRole role) {
        return new ParameterInstantiation(role, role.defaultValue);
    }

    @Override
    public String toString() { return role.name + "=" + value; }
}
