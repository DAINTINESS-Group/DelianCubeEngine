package intentional.assess.benchmarks;

/** An {@link AssessBenchmark} together with the label identifying it in results and reports. */
public final class NamedBenchmark {
    public final String label;
    public final AssessBenchmark matcher;

    public NamedBenchmark(String label, AssessBenchmark matcher) {
        this.label = label;
        this.matcher = matcher;
    }

    @Override
    public String toString() {
        return label;
    }
}
