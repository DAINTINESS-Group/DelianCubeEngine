package intentional.assess.fetch;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

import cubemanager.cubebase.CubeQuery;
import intentional.assess.CubeManagerAdapter;
import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.benchmarks.PastBenchmark;
import intentional.assess.utils.DatesHandler;
import result.Cell;
import result.Result;

/**
 * The batched fetch: a past window runs as ONE scan — the date predicate rewritten to the window's periods
 * and the rows split back per period — and slices with identical predicates run once.
 */
public final class BatchedFetcher extends SerialFetcher {

    private final Map<String, Result> fetchedSlices = new LinkedHashMap<>();

    @Override
    public String name() {
        return FetchStrategy.SCAN_PER_BENCHMARK.name();
    }

    @Override
    protected Result executeSlice(CubeManagerAdapter adapter, Map<String, String> slice, FetchStats stats) {
        String key = new TreeMap<>(slice).toString();
        Result fetched = fetchedSlices.get(key);
        if (fetched == null) {
            fetched = super.executeSlice(adapter, slice, stats);
            fetchedSlices.put(key, fetched);
        }
        return fetched;
    }

    @Override
    protected AssessBenchmark pastBenchmark(CubeManagerAdapter adapter, int periods, FetchStats stats) {
        List<String> pastDates = pastDates(adapter, periods);
        String dateLevel = adapter.getDateSelectionPredicate();

        CubeQuery window = adapter.translateFor(adapter.getSelectionPredicates());
        rewriteDateSigma(window, dateLevel, pastDates);
        Result fetched = stats.record(() -> adapter.executeCubeQuery(window));

        List<Result> pastRecords = splitByPeriod(fetched, dateLevel, pastDates);
        if (pastRecords.isEmpty()) {
            throw new RuntimeException("No past records for this query");
        }
        return new PastBenchmark(pastRecords, dateLevel);
    }

    /** Widens the date predicate from the query's own period to the past window. */
    private static void rewriteDateSigma(CubeQuery window, String dateLevel, List<String> pastDates) {
        StringJoiner values = new StringJoiner(",", "(", ")");
        for (String date : pastDates) {
            values.add("'" + DatesHandler.formatDates(date) + "'");
        }
        String suffix = "." + dateLevel.toLowerCase();
        for (String[] sigma : window.getSigmaExpressions()) {
            if (sigma[0].trim().toLowerCase().endsWith(suffix)) {
                sigma[1] = " IN ";
                sigma[2] = values.toString();
                return;
            }
        }
        throw new RuntimeException("A date was not defined in the selection predicates");
    }

    /** Partitions the window's rows back into one result per period, in window order. */
    private static List<Result> splitByPeriod(Result window, String dateLevel, List<String> pastDates) {
        int dateIndex = window.getColumnLabels().stream()
                .map(String::toLowerCase).collect(java.util.stream.Collectors.toList())
                .indexOf(dateLevel);
        if (dateIndex < 0) {
            throw new RuntimeException("Make sure that the comparison field is in the groupers");
        }
        Map<String, ArrayList<Cell>> byPeriod = new LinkedHashMap<>();
        for (String date : pastDates) {
            byPeriod.put(DatesHandler.formatDates(date), new ArrayList<Cell>());
        }
        for (Cell cell : window.getCells()) {
            ArrayList<Cell> period = byPeriod.get(cell.getDimensionMembers().get(dateIndex));
            if (period != null) {
                period.add(cell);
            }
        }
        List<Result> periods = new ArrayList<>();
        for (ArrayList<Cell> cells : byPeriod.values()) {
            if (!cells.isEmpty()) {
                periods.add(new Result(window.getResultArray(), cells,
                        window.getColumnNames(), window.getColumnLabels()));
            }
        }
        return periods;
    }
}
