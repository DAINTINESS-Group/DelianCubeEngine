package intentional.labeling.consensus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import intentional.labeling.LabelDomain;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import result.Cell;

/**
 * A rule producing the consensus of several labelings over one shared ordered domain. Each labeling is a
 * bucket order of the cells — the cells of a label form a bucket, buckets ordered by label rank — and the
 * consensus is the bucket order minimizing the summed generalized Kendall-tau distance to all of them (the
 * generalized Kemeny score). A pair of cells counts one disagreement between two orders when they rank it
 * in opposite strict orders, or when one ties it and the other does not. The consensus of a single labeling
 * is the labeling itself.
 */
public enum ConsensusRule {

    /**
     * Minimizes the generalized Kemeny score by local search: starting from each labeling and from a
     * mean-rank seed, cells move between buckets while the score improves, and the best end state over all
     * starts wins. The bucket count is capped at the domain size, so the consensus stays on the shared
     * domain.
     */
    KEMENY {
        @Override
        public Labeling consense(List<Labeling> labelings) {
            if (labelings.isEmpty()) {
                throw new IllegalArgumentException("A consensus needs at least one labeling");
            }
            if (labelings.size() == 1) {
                return labelings.get(0);
            }
            List<String> domain = sharedOrderedDomain(labelings);
            List<Cell> cells = cellsCoveredByAll(labelings);
            if (cells.isEmpty()) {
                throw new IllegalArgumentException("No cell is labeled by every labeling of the group");
            }

            int[][] votes = new int[labelings.size()][cells.size()];
            for (int v = 0; v < labelings.size(); v++) {
                for (int c = 0; c < cells.size(); c++) {
                    votes[v][c] = labelings.get(v).rankOf(labelings.get(v).of(cells.get(c)));
                }
            }

            int[] best = null;
            long bestScore = Long.MAX_VALUE;
            for (int[] start : startingStates(votes)) {
                int[] state = climb(start, votes, domain.size());
                long score = kemenyScore(state, votes);
                if (score < bestScore) {
                    bestScore = score;
                    best = state;
                }
            }

            Map<Cell, Double> rankByCell = new LinkedHashMap<>();
            for (int c = 0; c < cells.size(); c++) {
                rankByCell.put(cells.get(c), (double) best[c]);
            }
            return Labeling.withInheritedMagnitudes(consensusScheme(labelings, domain), rankByCell,
                    labelings.get(0).measureIndex(), meanMagnitudes(labelings, cells),
                    meanReferences(labelings, cells));
        }
    };

    /** The consensus labeling of the group; the group must share one ordered domain. */
    public abstract Labeling consense(List<Labeling> labelings);

    /** The domain all labelings of the group share; ordered, or the group cannot form bucket orders. */
    private static List<String> sharedOrderedDomain(List<Labeling> labelings) {
        List<String> domain = labelings.get(0).domain();
        for (Labeling labeling : labelings) {
            if (!labeling.ordered() || !labeling.domain().equals(domain)) {
                throw new IllegalArgumentException(
                        "A consensus needs labelings over one shared ordered domain");
            }
        }
        return domain;
    }

    /** The cells every labeling of the group covers, in the first labeling's order. */
    private static List<Cell> cellsCoveredByAll(List<Labeling> labelings) {
        List<Cell> cells = new ArrayList<>();
        for (Cell cell : labelings.get(0).assignment().keySet()) {
            boolean everywhere = true;
            for (Labeling labeling : labelings) {
                if (!labeling.covers(cell)) {
                    everywhere = false;
                    break;
                }
            }
            if (everywhere) {
                cells.add(cell);
            }
        }
        return cells;
    }

    /** Each cell's magnitude in the consensus: the mean of the group's magnitudes for it (all cover it). */
    private static Map<Cell, Double> meanMagnitudes(List<Labeling> labelings, List<Cell> cells) {
        Map<Cell, Double> means = new LinkedHashMap<>();
        for (Cell cell : cells) {
            double sum = 0;
            for (Labeling labeling : labelings) {
                sum += labeling.magnitudeOf(cell);
            }
            means.put(cell, sum / labelings.size());
        }
        return means;
    }

    /** Each cell's consensus reference: the mean over the group members that attached one, absent if none. */
    private static Map<Cell, Double> meanReferences(List<Labeling> labelings, List<Cell> cells) {
        Map<Cell, Double> means = new LinkedHashMap<>();
        for (Cell cell : cells) {
            double sum = 0;
            int count = 0;
            for (Labeling labeling : labelings) {
                double reference = labeling.referenceOf(cell);
                if (!Double.isNaN(reference)) {
                    sum += reference;
                    count++;
                }
            }
            if (count > 0) {
                means.put(cell, sum / count);
            }
        }
        return means;
    }

    /** The climb starts: every vote itself, plus the cells' mean ranks rounded to buckets. */
    private static List<int[]> startingStates(int[][] votes) {
        List<int[]> starts = new ArrayList<>();
        for (int[] vote : votes) {
            starts.add(vote.clone());
        }
        int[] meanSeed = new int[votes[0].length];
        for (int c = 0; c < meanSeed.length; c++) {
            double sum = 0;
            for (int[] vote : votes) sum += vote[c];
            meanSeed[c] = (int) Math.round(sum / votes.length);
        }
        starts.add(meanSeed);
        return starts;
    }

    /** Moves single cells between buckets while the Kemeny score improves; steepest move per cell. */
    private static int[] climb(int[] state, int[][] votes, int buckets) {
        long current = kemenyScore(state, votes);
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int c = 0; c < state.length; c++) {
                int bestBucket = state[c];
                long bestScore = current;
                for (int bucket = 0; bucket < buckets; bucket++) {
                    if (bucket == state[c]) continue;
                    int original = state[c];
                    state[c] = bucket;
                    long score = kemenyScore(state, votes);
                    state[c] = original;
                    if (score < bestScore) {
                        bestScore = score;
                        bestBucket = bucket;
                    }
                }
                if (bestScore < current) {
                    state[c] = bestBucket;
                    current = bestScore;
                    improved = true;
                }
            }
        }
        return state;
    }

    /** The summed generalized Kendall-tau distance from the candidate to every vote. */
    private static long kemenyScore(int[] candidate, int[][] votes) {
        long total = 0;
        for (int[] vote : votes) {
            total += generalizedKendallTau(candidate, vote);
        }
        return total;
    }

    /**
     * The generalized Kendall-tau distance between two bucket orders: one disagreement per pair ranked in
     * opposite strict orders, or tied in one order but not the other.
     */
    private static long generalizedKendallTau(int[] r, int[] s) {
        long disagreements = 0;
        for (int i = 0; i < r.length; i++) {
            for (int j = i + 1; j < r.length; j++) {
                int inR = Integer.compare(r[i], r[j]);
                int inS = Integer.compare(s[i], s[j]);
                boolean oppositeStrict = inR != 0 && inS != 0 && inR != inS;
                boolean tieMismatch = (inR == 0) != (inS == 0);
                if (oppositeStrict || tieMismatch) {
                    disagreements++;
                }
            }
        }
        return disagreements;
    }

    /** Turns consensus bucket ranks back into labels of the shared domain, named after the group. */
    private static LabelingScheme consensusScheme(List<Labeling> labelings, List<String> domainLabels) {
        String name = "Consensus(" + labelings.stream()
                .map(Labeling::schemeName).distinct().collect(Collectors.joining(",")) + ")";
        LabelDomain domain = new LabelDomain(domainLabels, true);
        return new LabelingScheme() {
            @Override public String name() { return name; }
            @Override public String applyLabels(double rank) { return domainLabels.get((int) Math.round(rank)); }
            @Override public LabelDomain domain() { return domain; }
        };
    }
}
