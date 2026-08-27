package intentional.assess.fetch;

/** The single-scan fetch must return the same benchmarks the serial one does, out of one query. */
public class SingleScanFetcherTest extends SerialFetcherTest {

    @Override
    protected AssessFetcher fetcher() {
        return new SingleScanFetcher();
    }
}
