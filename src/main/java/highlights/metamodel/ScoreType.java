package highlights.metamodel;

/**
 * The type of a {@link highlights.instance.Score} a highlight carries: a name plus an ordinal
 * domain, so highlights can be compared on it. Its values are the algorithm-computed metrics of a
 * highlight — a contribution share, a z-score, a concentration, a dominant share, or an enumerated
 * label with its ordinal rank.
 */
public interface ScoreType {

    /** The score-type name, e.g. "ContributionShare" or "ZScore". */
    String name();

    /** Ordinal comparison of two score values of this type. */
    int compare(double a, double b);
}
