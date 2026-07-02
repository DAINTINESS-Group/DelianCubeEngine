package result.highlights.metamodel;


/** A breakdown/sorter feature an archetype property reserves: a placeholder for a Character Type and its constraint. */
public final class ExplanatorRole {
    public final String name;
    public final ExplanatorConstraint constraint;

    public ExplanatorRole(String name, ExplanatorConstraint constraint) {
        this.name = name;
        this.constraint = constraint;
    }
}
