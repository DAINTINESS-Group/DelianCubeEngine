package highlights.metamodel;

/**
 * A named score type ordered by its numeric value — the algorithm-computed metric a highlight
 * carries (e.g. a contribution share, a z-score, a concentration, or an enumerated label's rank).
 */
public final class NamedScoreType implements ScoreType {
    private final String name;

    public NamedScoreType(String name) { this.name = name; }

    @Override
    public String name() { return name; }

    @Override
    public int compare(double a, double b) { return Double.compare(a, b); }
}
