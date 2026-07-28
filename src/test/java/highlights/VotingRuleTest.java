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
 * The voting rules over the wine example of the DOLAP 2024 paper (Figures 1, 2 and 4): the five cells of
 * one labeling scheme vote with their labels, tallied per label over the ordered domain BAD &lt; OK &lt; GOOD.
 */
public class VotingRuleTest {

    private static final List<String> DOMAIN = Arrays.asList("BAD", "OK", "GOOD");

    private static Map<String, Integer> tally(int bad, int ok, int good) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("BAD", bad);
        counts.put("OK", ok);
        counts.put("GOOD", good);
        return counts;
    }

    /** Scheme L1 labels the cells BAD,OK,OK,OK,GOOD — every rule elects OK (Figures 2, 3, 4). */
    @Test
    public void schemeL1ElectsOkUnderEveryRule() {
        Map<String, Integer> l1 = tally(1, 3, 1);
        assertEquals("OK", VotingRule.MAJORITY.elect(DOMAIN, true, l1, 5));
        assertEquals("OK", VotingRule.MEDIAN_VOTER.elect(DOMAIN, true, l1, 5));
        assertEquals("OK", VotingRule.VALENCE_MEDIAN.elect(DOMAIN, true, l1, 5));
        assertEquals("OK", VotingRule.BORDA.elect(DOMAIN, true, l1, 5));
    }

    /** Scheme L2 labels the cells BAD,BAD,OK,GOOD,GOOD — no majority, but the median voter is OK (Figure 2). */
    @Test
    public void schemeL2HasNoMajorityButAMedian() {
        Map<String, Integer> l2 = tally(2, 1, 2);
        assertNull(VotingRule.MAJORITY.elect(DOMAIN, true, l2, 5));
        assertEquals("OK", VotingRule.MEDIAN_VOTER.elect(DOMAIN, true, l2, 5));
    }

    /** Scheme L3 tallies 2,2,1 — Borda ties BAD and OK at 1.5 and elects no one (Figure 4). */
    @Test
    public void bordaCannotUntieSchemeL3() {
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
        Map<String, Integer> l1 = tally(1, 3, 1);
        assertNull(VotingRule.MEDIAN_VOTER.elect(DOMAIN, false, l1, 5));
        assertNull(VotingRule.VALENCE_MEDIAN.elect(DOMAIN, false, l1, 5));
        assertEquals("OK", VotingRule.MAJORITY.elect(DOMAIN, false, l1, 5));
    }
}
