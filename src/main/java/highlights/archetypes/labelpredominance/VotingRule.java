package highlights.archetypes.labelpredominance;

import java.util.List;
import java.util.Map;

/**
 * A vote-composition method electing one label to characterize a whole labeled result (DOLAP 2024, §3.3):
 * the cells are the voters, the domain's labels the candidates, and each cell casts the single vote of its
 * own label. The ordinal rules derive a voter's preference between two candidates from the distance of its
 * vote along the ordered domain; they elect nothing over an unordered one. Every rule may fail to produce
 * a single winner, in which case it elects {@code null}.
 */
public enum VotingRule {

    /** The winner must gather an absolute majority of the votes. */
    MAJORITY {
        @Override
        public String elect(List<String> domain, boolean ordered, Map<String, Integer> counts, int total) {
            for (Map.Entry<String, Integer> e : counts.entrySet()) {
                if (2 * e.getValue() > total) return e.getKey();
            }
            return null;
        }
    },

    /**
     * The median voter's label wins: sort the votes along the ordered domain and take the middle one — the
     * Condorcet winner when one exists. Fails when an even electorate is split across two labels.
     */
    MEDIAN_VOTER {
        @Override
        public String elect(List<String> domain, boolean ordered, Map<String, Integer> counts, int total) {
            if (!ordered || total == 0) return null;
            String lower = null;
            int cumulative = 0;
            for (String label : domain) {
                cumulative += counts.getOrDefault(label, 0);
                if (lower == null && 2 * cumulative >= total) lower = label;
                if (2 * cumulative > total) return label.equals(lower) ? label : null;
            }
            return null;
        }
    },

    /**
     * Median voter with valence: every pair of candidates duels, and each vote counts for the duellist
     * closer to it on the ordered domain (equidistant votes abstain). The label winning every duel wins.
     */
    VALENCE_MEDIAN {
        @Override
        public String elect(List<String> domain, boolean ordered, Map<String, Integer> counts, int total) {
            if (!ordered) return null;
            for (int i = 0; i < domain.size(); i++) {
                boolean winsAll = true;
                for (int j = 0; j < domain.size() && winsAll; j++) {
                    if (i == j) continue;
                    int forI = 0, forJ = 0;
                    for (int k = 0; k < domain.size(); k++) {
                        int votes = counts.getOrDefault(domain.get(k), 0);
                        int toI = Math.abs(k - i), toJ = Math.abs(k - j);
                        if (toI < toJ) forI += votes;
                        else if (toJ < toI) forJ += votes;
                    }
                    winsAll = forI > forJ;
                }
                if (winsAll) return domain.get(i);
            }
            return null;
        }
    },

    /**
     * Borda-style scoring over the tallies: a label scores 1 for every label it out-polls and 1/2 per tie;
     * the unique top scorer wins.
     */
    BORDA {
        @Override
        public String elect(List<String> domain, boolean ordered, Map<String, Integer> counts, int total) {
            if (total == 0) return null;
            String winner = null;
            double best = -1.0;
            boolean unique = false;
            for (String label : domain) {
                double score = 0.0;
                for (String other : domain) {
                    if (other.equals(label)) continue;
                    int mine = counts.getOrDefault(label, 0);
                    int theirs = counts.getOrDefault(other, 0);
                    if (mine > theirs) score += 1.0;
                    else if (mine == theirs) score += 0.5;
                }
                if (score > best) { best = score; winner = label; unique = true; }
                else if (score == best) unique = false;
            }
            return unique ? winner : null;
        }
    };

    /** The elected label, or {@code null} when the rule fails to produce a single winner. */
    public abstract String elect(List<String> domain, boolean ordered, Map<String, Integer> counts, int total);

    /** The rule a labeling gets when none is imposed: the median voter when ordered, majority otherwise. */
    public static VotingRule defaultFor(boolean ordered) {
        return ordered ? MEDIAN_VOTER : MAJORITY;
    }
}
