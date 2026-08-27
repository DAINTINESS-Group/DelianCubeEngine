package intentional.assess.fetch;

/** The batched fetch must return the same benchmarks the serial one does, scan for scan cheaper. */
public class BatchedFetcherTest extends SerialFetcherTest {

    @Override
    protected AssessFetcher fetcher() {
        return new BatchedFetcher();
    }
}
