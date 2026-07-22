package highlights.metamodel;

/**
 * Constraint a {@link MainMeasureRole} places on the Measure Type it may bind to. A pure marker: whether a
 * concrete measure satisfies it is decided in the cube-aware bridge (the extractor), which reads the typed
 * aggregation function — the metamodel stays decoupled from cube semantics.
 */
public enum MeasureConstraint {
    ANY,
    ADDITIVE
}
