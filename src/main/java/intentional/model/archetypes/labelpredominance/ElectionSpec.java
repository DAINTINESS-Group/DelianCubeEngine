package intentional.model.archetypes.labelpredominance;

/**
 * How the label-predominance election runs: the {@link VotingRule} that composes the vote and the
 * {@link Weighting} the ballots are cast under. The rule may be left unset, in which case each labeling takes
 * {@link VotingRule#defaultFor} its orderedness.
 */
public final class ElectionSpec {

    /** The election every labeling gets when none is imposed: the labeling's default rule, count ballots. */
    public static final ElectionSpec DEFAULT = new ElectionSpec(null, Weighting.CELL_COUNT);

    private final VotingRule rule;
    private final Weighting weighting;

    public ElectionSpec(VotingRule rule, Weighting weighting) {
        if (weighting == null) {
            throw new IllegalArgumentException("An election needs a weighting");
        }
        this.rule = rule;
        this.weighting = weighting;
    }

    /** The rule to run over a labeling of the given orderedness: the imposed one, or its default. */
    public VotingRule ruleFor(boolean ordered) {
        return rule != null ? rule : VotingRule.defaultFor(ordered);
    }

    public Weighting weighting() {
        return weighting;
    }
}
