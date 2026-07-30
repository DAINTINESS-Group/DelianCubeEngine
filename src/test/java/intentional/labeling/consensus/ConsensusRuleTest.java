package intentional.labeling.consensus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import intentional.labeling.LabelDomain;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import result.Cell;

/**
 * The Kemeny consensus over labelings-as-bucket-orders: five cells labeled by three schemes over the
 * ordered domain BAD &lt; OK &lt; GOOD, with the known optimal consensus as the golden fixture.
 */
public class ConsensusRuleTest {

    private static final List<String> DOMAIN = Arrays.asList("BAD", "OK", "GOOD");

    private static LabelingScheme rankScheme(String name, List<String> domainLabels, boolean ordered) {
        return new LabelingScheme() {
            @Override public String name() { return name; }
            @Override public String applyLabels(double value) { return domainLabels.get((int) value); }
            @Override public LabelDomain domain() { return new LabelDomain(domainLabels, ordered); }
        };
    }

    private static Labeling labeling(String schemeName, Cell[] cells, double... ranks) {
        Map<Cell, Double> rankByCell = new LinkedHashMap<>();
        for (int i = 0; i < cells.length; i++) {
            rankByCell.put(cells[i], ranks[i]);
        }
        return new Labeling(rankScheme(schemeName, DOMAIN, true), rankByCell);
    }

    private static Cell[] fiveCells() {
        String[] names = {"Red", "Rose", "Copper", "Yellow", "White"};
        Cell[] cells = new Cell[names.length];
        for (int i = 0; i < names.length; i++) {
            cells[i] = new Cell(new String[]{names[i], "0", "1"}, 1);
        }
        return cells;
    }

    /**
     * The three votes: [{Red},{Rose,Copper,Yellow},{White}], [{Red,Rose},{Copper},{Yellow,White}] and
     * [{Red,Rose},{Copper,Yellow},{White}] — the third is the optimal consensus of the group.
     */
    @Test
    public void consensusIsTheKemenyOptimalBucketOrder() {
        Cell[] cells = fiveCells();
        List<Labeling> group = Arrays.asList(
                labeling("L1", cells, 0, 1, 1, 1, 2),
                labeling("L2", cells, 0, 0, 1, 2, 2),
                labeling("L3", cells, 0, 0, 1, 1, 2));

        Labeling consensus = ConsensusRule.KEMENY.consense(group);

        assertEquals("BAD", consensus.of(cells[0]));
        assertEquals("BAD", consensus.of(cells[1]));
        assertEquals("OK", consensus.of(cells[2]));
        assertEquals("OK", consensus.of(cells[3]));
        assertEquals("GOOD", consensus.of(cells[4]));
        assertEquals("Consensus(L1,L2,L3)", consensus.schemeName());
        assertEquals(DOMAIN, consensus.domain());
    }

    @Test
    public void consensusOfOneLabelingIsTheLabelingItself() {
        Cell[] cells = fiveCells();
        Labeling single = labeling("L1", cells, 0, 1, 1, 1, 2);
        assertSame(single, ConsensusRule.KEMENY.consense(Arrays.asList(single)));
    }

    @Test
    public void unanimousVotesAreTheirOwnConsensus() {
        Cell[] cells = fiveCells();
        List<Labeling> group = Arrays.asList(
                labeling("A", cells, 0, 0, 1, 2, 2),
                labeling("B", cells, 0, 0, 1, 2, 2));

        Labeling consensus = ConsensusRule.KEMENY.consense(group);
        assertEquals("BAD", consensus.of(cells[0]));
        assertEquals("BAD", consensus.of(cells[1]));
        assertEquals("OK", consensus.of(cells[2]));
        assertEquals("GOOD", consensus.of(cells[3]));
        assertEquals("GOOD", consensus.of(cells[4]));
    }

    @Test
    public void labelingsOverDifferentDomainsCannotConsense() {
        Cell[] cells = fiveCells();
        Map<Cell, Double> ranks = new LinkedHashMap<>();
        for (Cell cell : cells) ranks.put(cell, 0.0);
        Labeling other = new Labeling(
                rankScheme("other", Arrays.asList("low", "high"), true), ranks);

        assertThrows(IllegalArgumentException.class, () -> ConsensusRule.KEMENY.consense(
                Arrays.asList(labeling("L1", cells, 0, 1, 1, 1, 2), other)));
    }

    @Test
    public void unorderedDomainsCannotConsense() {
        Cell[] cells = fiveCells();
        Map<Cell, Double> ranks = new LinkedHashMap<>();
        for (Cell cell : cells) ranks.put(cell, 0.0);
        Labeling unordered = new Labeling(rankScheme("clusters", DOMAIN, false), ranks);

        assertThrows(IllegalArgumentException.class, () -> ConsensusRule.KEMENY.consense(
                Arrays.asList(unordered, unordered)));
    }
}
