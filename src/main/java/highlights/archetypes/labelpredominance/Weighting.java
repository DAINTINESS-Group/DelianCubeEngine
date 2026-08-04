package highlights.archetypes.labelpredominance;

/**
 * What a cell's ballot weighs in the election: one vote each; the cell's magnitude, so the vote follows the
 * volume the labels stand on; or the cell's reference value, so the vote follows the volume that was expected
 * of them. Under {@link VotingRule#MEDIAN_VOTER} a weighted vote elects the label holding the barycenter of
 * the total weight.
 */
public enum Weighting { CELL_COUNT, MAGNITUDE, REFERENCE }
