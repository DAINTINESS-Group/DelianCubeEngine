package highlights.metamodel;

/**
 * A significance facet with an ordinal domain, so that highlights can be compared.
 * Backed by the interestingness taxonomy (arithmetic facets) and by labeling schemes
 * (enumerated, ordinal labels).
 */
public interface ScoreType {

    /** The facet name, e.g. "PECULIARITY" or a label-domain name. */
    String name();

    /** Ordinal comparison of two score values of this type. */
    int compare(double a, double b);
}
