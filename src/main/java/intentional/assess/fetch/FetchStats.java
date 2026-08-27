package intentional.assess.fetch;

import java.util.function.Supplier;

import result.Result;

/** The scans a fetch spent and the time they took, under the strategy that ran them. */
public final class FetchStats {

    private final String strategy;
    private int scans;
    private long millis;

    FetchStats(String strategy) {
        this.strategy = strategy;
    }

    /** Runs one scan, counting it and its duration. */
    Result record(Supplier<Result> scan) {
        long start = System.nanoTime();
        Result result = scan.get();
        millis += (System.nanoTime() - start) / 1_000_000;
        scans++;
        return result;
    }

    public String strategy() { return strategy; }

    public int scans() { return scans; }

    public long millis() { return millis; }

    @Override
    public String toString() {
        return strategy + ": " + scans + " scans, " + millis + " ms";
    }
}
