package intentional.model.archetypes.labelpredominance;

/**
 * What a cell's ballot weighs in the election: one vote each, the cell's magnitude, or the cell's reference
 * value. Under {@link VotingRule#MEDIAN_VOTER} a weighted vote elects the label holding the barycenter of the
 * total weight.
 */
public enum Weighting { CELL_COUNT, MAGNITUDE, REFERENCE }
