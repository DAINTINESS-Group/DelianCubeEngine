package result.highlights.metamodel;

/** The studied measure of an archetype property: a placeholder for a Measure Type and its constraint. */
public final class MainMeasureRole {
    public final String name;
    public final MeasureConstraint constraint;

    public MainMeasureRole(String name, MeasureConstraint constraint) {
        this.name = name;
        this.constraint = constraint;
    }
}
