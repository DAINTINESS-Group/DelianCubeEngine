package intentional.assess.fetch;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cubemanager.cubebase.CubeQuery;
import intentional.assess.CubeManagerAdapter;
import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.benchmarks.BenchmarkKind;
import intentional.assess.benchmarks.ConstantBenchmark;
import intentional.assess.benchmarks.PastBenchmark;
import intentional.assess.benchmarks.SiblingBenchmark;
import intentional.assess.utils.DatesHandler;
import result.Result;

/**
 * The one-scan-per-slice fetch: the target, each sibling, and each past period run as their own query.
 * Every slice translates under its own copy of the predicates, so the template is never mutated.
 */
public class SerialFetcher implements AssessFetcher {

    @Override
    public String name() {
        return FetchStrategy.SCAN_PER_SLICE.name();
    }

    @Override
    public FetchedCubes fetch(CubeManagerAdapter adapter, List<List<String>> benchmarkDetails) {
        FetchStats stats = new FetchStats(name());
        CubeQuery targetQuery = adapter.translateToCubeQuery();
        Result targetCube = stats.record(() -> adapter.executeCubeQuery(targetQuery));
        List<AssessBenchmark> benchmarks = new ArrayList<>();
        for (List<String> details : benchmarkDetails) {
            benchmarks.add(createBenchmark(adapter, details, stats));
        }
        return new FetchedCubes(targetQuery, targetCube, benchmarks, stats);
    }

    private AssessBenchmark createBenchmark(CubeManagerAdapter adapter, List<String> details, FetchStats stats) {
        switch (BenchmarkKind.of(details)) {
            case CONSTANT:
                return new ConstantBenchmark(Double.parseDouble(details.get(1)));
            case SIBLING:
                return siblingBenchmark(adapter, details.get(1), details.get(2), stats);
            case PAST:
                return pastBenchmark(adapter, Integer.parseInt(details.get(1)), stats);
            default:
                throw new RuntimeException("External Benchmark not implemented yet!");
        }
    }

    /**
     * Precondition: The selection predicates contain the level we want to slice <br>
     * For example: region should be in the group-by to say "against region = 'South Carolina'"
     */
    private AssessBenchmark siblingBenchmark(CubeManagerAdapter adapter, String siblingKey,
                                             String siblingValue, FetchStats stats) {
        Map<String, String> predicates = adapter.getSelectionPredicates();
        if (predicates == null || predicates.get(siblingKey) == null) {
            throw new RuntimeException(siblingKey + " was not defined in original predicates");
        }
        Map<String, String> slice = new LinkedHashMap<>(predicates);
        slice.put(siblingKey, siblingValue);
        return new SiblingBenchmark(executeSlice(adapter, slice, stats), siblingKey);
    }

    protected AssessBenchmark pastBenchmark(CubeManagerAdapter adapter, int periods, FetchStats stats) {
        List<String> pastDates = pastDates(adapter, periods);
        String dateLevel = adapter.getDateSelectionPredicate();
        List<Result> pastRecords = new ArrayList<>();
        for (String date : pastDates) {
            Map<String, String> slice = new LinkedHashMap<>(adapter.getSelectionPredicates());
            slice.put(dateLevel, date);
            Result period = executeSlice(adapter, slice, stats);
            if (!period.getCells().isEmpty()) {
                pastRecords.add(period);
            }
        }
        if (pastRecords.isEmpty()) {
            throw new RuntimeException("No past records for this query");
        }
        return new PastBenchmark(pastRecords, dateLevel);
    }

    /** The past periods to compare against, most recent first. */
    protected static List<String> pastDates(CubeManagerAdapter adapter, int periods) {
        if (periods <= 0) {
            throw new RuntimeException("Did not provide a valid number of past Records");
        }
        String dateLevel = adapter.getDateSelectionPredicate();
        String currentDate = adapter.getSelectionPredicates().get(dateLevel);
        return DatesHandler.decrementDate(currentDate, dateLevel, periods);
    }

    /** Runs one slice of the family as its own scan. */
    protected Result executeSlice(CubeManagerAdapter adapter, Map<String, String> slice, FetchStats stats) {
        return stats.record(() -> adapter.executeCubeQuery(adapter.translateFor(slice)));
    }
}
