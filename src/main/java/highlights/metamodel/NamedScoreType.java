package highlights.metamodel;

/**
 * A named score type ordered by its numeric value — the algorithm-computed metric a highlight
 * carries (e.g. a contribution share, a z-score, a concentration, or an enumerated label's rank).
 * {@link ScoreKind#NUMERIC} unless declared otherwise.
 */
public final class NamedScoreType implements ScoreType {
    private final String name;
    private final ScoreKind kind;

    public NamedScoreType(String name) { this(name, ScoreKind.NUMERIC); }

    public NamedScoreType(String name, ScoreKind kind) {
        this.name = name;
        this.kind = kind;
    }

    @Override
    public String name() { return name; }

    @Override
    public ScoreKind kind() { return kind; }

    @Override
    public int compare(double a, double b) { return Double.compare(a, b); }
}
