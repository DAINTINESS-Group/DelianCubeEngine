package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import highlights.archetypes.labelpredominance.ElectionSpec;
import highlights.archetypes.labelpredominance.VotingRule;
import highlights.archetypes.labelpredominance.Weighting;

/**
 * The election spec's rule resolution: an unset rule defers to the labeling's default, an imposed one holds
 * regardless of orderedness.
 */
public class ElectionSpecTest {

    @Test
    public void defaultDefersToTheLabelingsDefaultRule() {
        assertSame(VotingRule.MEDIAN_VOTER, ElectionSpec.DEFAULT.ruleFor(true));
        assertSame(VotingRule.MAJORITY, ElectionSpec.DEFAULT.ruleFor(false));
        assertEquals(Weighting.CELL_COUNT, ElectionSpec.DEFAULT.weighting());
    }

    @Test
    public void animposedRuleHoldsRegardlessOfOrderedness() {
        ElectionSpec spec = new ElectionSpec(VotingRule.BORDA, Weighting.MAGNITUDE);
        assertSame(VotingRule.BORDA, spec.ruleFor(true));
        assertSame(VotingRule.BORDA, spec.ruleFor(false));
        assertEquals(Weighting.MAGNITUDE, spec.weighting());
    }

    @Test
    public void aWeightingIsRequired() {
        assertThrows(IllegalArgumentException.class, () -> new ElectionSpec(VotingRule.MAJORITY, null));
    }
}
