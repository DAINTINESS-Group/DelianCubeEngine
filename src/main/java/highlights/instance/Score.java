package highlights.instance;

import highlights.metamodel.ScoreType;

/**
 * A significance value: a {@link ScoreType} together with an arithmetic value, or, for an
 * enumerated facet, the ordinal rank of a label plus the label itself.
 */
public final class Score {
    public final ScoreType type;
    public final double value;   // arithmetic value, or the ordinal rank of a label
    public final String label;   // enumerated label, or null when the score is arithmetic

    public Score(ScoreType type, double value) { this(type, value, null); }

    public Score(ScoreType type, double value, String label) {
        this.type = type;
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return type.name() + "=" + (label != null ? label : Double.toString(value));
    }
}
