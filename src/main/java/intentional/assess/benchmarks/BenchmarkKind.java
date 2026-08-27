package intentional.assess.benchmarks;

import java.util.List;

/** The kinds of benchmark an AGAINST entry can name, tagged by the first token of its descriptor. */
public enum BenchmarkKind {
    CONSTANT, SIBLING, PAST, EXTERNAL;

    /** The kind a descriptor names; the only place the tag tokens are read. */
    public static BenchmarkKind of(List<String> details) {
        switch (details.get(0)) {
            case "Constant": return CONSTANT;
            case "Sibling": return SIBLING;
            case "Past": return PAST;
            case "External": return EXTERNAL;
            default: throw new IllegalArgumentException("Unrecognized Benchmark Type: " + details.get(0));
        }
    }
}
