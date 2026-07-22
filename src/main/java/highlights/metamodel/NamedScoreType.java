package highlights.metamodel;

/**
 * An arithmetic significance facet identified by a name, ordered by its numeric value. Used for
 * algorithm-computed metrics (e.g. a contribution share) that are not part of the interestingness taxonomy.
 */
public final class NamedScoreType implements ScoreType {
    private final String name;

    public NamedScoreType(String name) { this.name = name; }

    @Override
    public String name() { return name; }

    @Override
    public int compare(double a, double b) { return Double.compare(a, b); }
}
