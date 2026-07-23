package highlights.metamodel;

/**
 * A tunable an {@link Algorithm} declares: a named placeholder (e.g. a dominance threshold or a top-k
 * cutoff) with a description and the default value a run uses when not given an explicit one. Materialized
 * into a {@link highlights.instance.ParameterInstantiation} by a run.
 */
public final class ParameterRole {
    public final String name;
    public final String description;
    public final double defaultValue;

    public ParameterRole(String name, String description, double defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() { return name; }
}
