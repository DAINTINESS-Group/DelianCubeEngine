package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import highlights.archetypes.labelpredominance.VotingRule;

/**
 * The voting rules over per-label tallies on the ordered domain BAD &lt; OK &lt; GOOD — plain ballot
 * counts, or volume weights for the weighted vote.
 */
public class VotingRuleTest {

    private static final List<String> DOMAIN = Arrays.asList("BAD", "OK", "GOOD");

    private static Map<String, Double> tally(double bad, double ok, double good) {
        Map<String, Double> tallies = new LinkedHashMap<>();
        tallies.put("BAD", bad);
        tallies.put("OK", ok);
        tallies.put("GOOD", good);
        return tallies;
    }

    /** A tally piled on the center label is elected by every rule. */
    @Test
    public void centeredTallyElectsOkUnderEveryRule() {
        Map<String, Double> tallies = tally(1, 3, 1);
        assertEquals("OK", VotingRule.MAJORITY.elect(DOMAIN, true, tallies, 5));
        assertEquals("OK", VotingRule.MEDIAN_VOTER.elect(DOMAIN, true, tallies, 5));
        assertEquals("OK", VotingRule.VALENCE_MEDIAN.elect(DOMAIN, true, tallies, 5));
        assertEquals("OK", VotingRule.BORDA.elect(DOMAIN, true, tallies, 5));
    }

    /** A polarized tally has no absolute majority, yet its median voter sits on the center label. */
    @Test
    public void polarizedTallyHasNoMajorityButAMedian() {
        Map<String, Double> tallies = tally(2, 1, 2);
        assertNull(VotingRule.MAJORITY.elect(DOMAIN, true, tallies, 5));
        assertEquals("OK", VotingRule.MEDIAN_VOTER.elect(DOMAIN, true, tallies, 5));
    }

    /** Two labels with equal tallies tie on points, and Borda elects no one. */
    @Test
    public void bordaCannotUntieEqualTallies() {
        assertNull(VotingRule.BORDA.elect(DOMAIN, true, tally(2, 2, 1), 5));
    }

    /** An even electorate split across two labels has no median voter. */
    @Test
    public void knifeEdgeVoteHasNoMedian() {
        assertNull(VotingRule.MEDIAN_VOTER.elect(DOMAIN, true, tally(2, 0, 2), 4));
    }

    /** The ordinal rules elect nothing over an unordered domain. */
    @Test
    public void ordinalRulesNeedAnOrderedDomain() {
        Map<String, Double> tallies = tally(1, 3, 1);
        assertNull(VotingRule.MEDIAN_VOTER.elect(DOMAIN, false, tallies, 5));
        assertNull(VotingRule.VALENCE_MEDIAN.elect(DOMAIN, false, tallies, 5));
        assertEquals("OK", VotingRule.MAJORITY.elect(DOMAIN, false, tallies, 5));
    }

    /** Volume weights move the barycenter to a label the ballot counts alone would not elect. */
    @Test
    public void weightedTalliesShiftTheMedian() {
        assertEquals("BAD", VotingRule.MEDIAN_VOTER.elect(DOMAIN, true, tally(11, 0, 9), 20));
        assertEquals("OK", VotingRule.MEDIAN_VOTER.elect(DOMAIN, true, tally(28, 34, 11), 73));
    }
}
