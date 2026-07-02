package result.highlights.metamodel;

/** Constraint a {@link MainMeasureRole} places on the Measure Type it may bind to. */
public enum MeasureConstraint {
    ANY,
    ADDITIVE;

    /** Whether a measure produced by the given aggregation function satisfies this constraint. */
    public boolean accepts(String aggregation) {
        return this == ANY || Additivity.isAdditive(aggregation);
    }
}
