package highlights.metamodel;

/**
 * Whether a {@link ScoreType}'s values are arithmetic or enumerated. Numeric values can be aggregated,
 * normalized and compared across highlights; categorical values are the ranks of enumerated labels —
 * comparable within their own domain but meaningless to average or normalize.
 */
public enum ScoreKind {
    NUMERIC,
    CATEGORICAL
}
