package intentional.assess;

import intentional.assess.benchmarks.NamedBenchmark;
import intentional.assess.deltas.DeltaScheme;

/**
 * One comparison an assess query runs: the benchmark compared against — null when the query has no AGAINST
 * clause — and the delta chain deriving the compared quantity.
 */
public final class AssessComparison {
    public final NamedBenchmark benchmark;
    public final DeltaScheme delta;

    public AssessComparison(NamedBenchmark benchmark, DeltaScheme delta) {
        this.benchmark = benchmark;
        this.delta = delta;
    }
}
