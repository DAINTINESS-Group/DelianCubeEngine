package intentional.assess;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.deltas.DeltaScheme;

/**
 * One comparison an assess query runs: the benchmark compared against, the label identifying it in results
 * and reports — both null when the query has no AGAINST clause — and the delta chain deriving the compared
 * quantity.
 */
public final class AssessComparison {
    public final AssessBenchmark benchmark;
    public final String benchmarkLabel;
    public final DeltaScheme delta;

    public AssessComparison(AssessBenchmark benchmark, String benchmarkLabel, DeltaScheme delta) {
        this.benchmark = benchmark;
        this.benchmarkLabel = benchmarkLabel;
        this.delta = delta;
    }
}
