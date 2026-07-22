package highlights.metamodel;

/**
 * Built-in arithmetic significance facets, named after the interestingness taxonomy.
 * The enum's own {@code name()} (e.g. "PECULIARITY") satisfies {@link ScoreType#name()}.
 */
public enum InterestingnessFacet implements ScoreType {
    PECULIARITY, NOVELTY, RELEVANCE, SURPRISE;

    @Override
    public int compare(double a, double b) { return Double.compare(a, b); }
}
