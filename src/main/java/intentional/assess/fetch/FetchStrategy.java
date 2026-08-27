package intentional.assess.fetch;

/** The fetch plans an assess query can run under; each name states what one scan pays for. */
public enum FetchStrategy {

    /** One scan per slice: the target, each sibling, and each past period separately — the naive baseline. */
    SCAN_PER_SLICE {
        @Override public AssessFetcher fetcher() { return new SerialFetcher(); }
    },

    /** One scan per AGAINST entry: a past benchmark fetches all its periods at once. */
    SCAN_PER_BENCHMARK {
        @Override public AssessFetcher fetcher() { return new BatchedFetcher(); }
    },

    /** One scan for the whole query: every slice carved out of it. */
    SCAN_PER_QUERY {
        @Override public AssessFetcher fetcher() { return new SingleScanFetcher(); }
    };

    public abstract AssessFetcher fetcher();
}
