package highlights.instance;

import highlights.metamodel.ParameterRole;

/**
 * The value an {@link AlgorithmExecution} bound to one of its algorithm's {@link ParameterRole}s: an
 * arithmetic value, or, for an enumerated parameter, the ordinal of the chosen constant plus its name.
 */
public final class ParameterInstantiation {
    public final ParameterRole role;
    public final double value;   // arithmetic value, or the ordinal of an enumerated constant
    public final String label;   // the enumerated constant's name, or null when the value is arithmetic

    public ParameterInstantiation(ParameterRole role, double value) { this(role, value, null); }

    public ParameterInstantiation(ParameterRole role, double value, String label) {
        this.role = role;
        this.value = value;
        this.label = label;
    }

    /** Instantiates the role to its declared default. */
    public static ParameterInstantiation ofDefault(ParameterRole role) {
        return new ParameterInstantiation(role, role.defaultValue);
    }

    @Override
    public String toString() { return role.name + "=" + (label != null ? label : Double.toString(value)); }
}
