package intentional.assess.fetch;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import cubemanager.cubebase.CubeQuery;
import intentional.assess.CubeManagerAdapter;
import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.benchmarks.BenchmarkKind;
import intentional.assess.benchmarks.ConstantBenchmark;
import intentional.assess.benchmarks.PastBenchmark;
import intentional.assess.benchmarks.SiblingBenchmark;
import intentional.assess.utils.DatesHandler;
import result.Cell;
import result.Result;

/**
 * The one-scan fetch: every slice the query needs — the target, each sibling, each past period — comes out
 * of a single query whose varied predicates are widened to all their values, partitioned back per slice by
 * the levels that vary. Levels that vary must be in the group-by, the same condition benchmark matching
 * already requires.
 */
public final class SingleScanFetcher implements AssessFetcher {

    @Override
    public String name() {
        return FetchStrategy.SCAN_PER_QUERY.name();
    }

    @Override
    public FetchedCubes fetch(CubeManagerAdapter adapter, List<List<String>> benchmarkDetails) {
        FetchStats stats = new FetchStats(name());
        CubeQuery targetQuery = adapter.translateToCubeQuery();

        Map<String, String> targetSlice = sqlFormatted(adapter, adapter.getSelectionPredicates());
        List<List<Map<String, String>>> slicesPerBenchmark = planSlices(adapter, benchmarkDetails);

        Map<String, Set<String>> variedValues = variedValues(targetSlice, slicesPerBenchmark);
        Result window = stats.record(() -> adapter.executeCubeQuery(
                variedValues.isEmpty() ? targetQuery : widen(adapter, variedValues)));
        Map<String, Integer> variedColumns = variedColumns(window, variedValues.keySet());

        Result targetCube = slice(window, variedColumns, targetSlice);
        List<AssessBenchmark> benchmarks = new ArrayList<>();
        for (int i = 0; i < benchmarkDetails.size(); i++) {
            benchmarks.add(createBenchmark(
                    adapter, benchmarkDetails.get(i), slicesPerBenchmark.get(i), window, variedColumns));
        }
        return new FetchedCubes(targetQuery, targetCube, benchmarks, stats);
    }

    /** The predicate values identifying each benchmark's slices; constants and externals need none. */
    private static List<List<Map<String, String>>> planSlices(CubeManagerAdapter adapter,
                                                              List<List<String>> benchmarkDetails) {
        List<List<Map<String, String>>> slicesPerBenchmark = new ArrayList<>();
        for (List<String> details : benchmarkDetails) {
            List<Map<String, String>> slices = new ArrayList<>();
            switch (BenchmarkKind.of(details)) {
                case SIBLING:
                    Map<String, String> predicates = adapter.getSelectionPredicates();
                    if (predicates == null || predicates.get(details.get(1)) == null) {
                        throw new RuntimeException(details.get(1) + " was not defined in original predicates");
                    }
                    Map<String, String> sibling = new LinkedHashMap<>(predicates);
                    sibling.put(details.get(1), details.get(2));
                    slices.add(sqlFormatted(adapter, sibling));
                    break;
                case PAST:
                    String dateLevel = adapter.getDateSelectionPredicate();
                    for (String date : SerialFetcher.pastDates(adapter, Integer.parseInt(details.get(1)))) {
                        Map<String, String> period = new LinkedHashMap<>(adapter.getSelectionPredicates());
                        period.put(dateLevel, date);
                        slices.add(sqlFormatted(adapter, period));
                    }
                    break;
                case CONSTANT:
                    break;
                default:
                    throw new RuntimeException("External Benchmark not implemented yet!");
            }
            slicesPerBenchmark.add(slices);
        }
        return slicesPerBenchmark;
    }

    /** The values every slice pins per level, in the form the scan's rows carry them. */
    private static Map<String, String> sqlFormatted(CubeManagerAdapter adapter, Map<String, String> predicates) {
        Map<String, String> formatted = new LinkedHashMap<>();
        if (predicates == null) {
            return formatted;
        }
        predicates.forEach((key, value) ->
                formatted.put(key, adapter.isDateLevel(key) ? DatesHandler.formatDates(value) : value));
        return formatted;
    }

    /** The levels whose value differs across slices, with every value they take. */
    private static Map<String, Set<String>> variedValues(Map<String, String> targetSlice,
                                                         List<List<Map<String, String>>> slicesPerBenchmark) {
        Map<String, Set<String>> valuesByLevel = new LinkedHashMap<>();
        targetSlice.forEach((level, value) ->
                valuesByLevel.computeIfAbsent(level, l -> new LinkedHashSet<>()).add(value));
        for (List<Map<String, String>> slices : slicesPerBenchmark) {
            for (Map<String, String> slice : slices) {
                slice.forEach((level, value) ->
                        valuesByLevel.computeIfAbsent(level, l -> new LinkedHashSet<>()).add(value));
            }
        }
        valuesByLevel.values().removeIf(values -> values.size() < 2);
        return valuesByLevel;
    }

    /** The target's query with each varied predicate widened to all the values its slices pin. */
    private static CubeQuery widen(CubeManagerAdapter adapter, Map<String, Set<String>> variedValues) {
        CubeQuery scan = adapter.translateFor(adapter.getSelectionPredicates());
        variedValues.forEach((level, values) -> {
            StringJoiner joined = new StringJoiner(",", "(", ")");
            values.forEach(value -> joined.add("'" + value + "'"));
            String suffix = "." + level.toLowerCase();
            for (String[] sigma : scan.getSigmaExpressions()) {
                if (sigma[0].trim().toLowerCase().endsWith(suffix)) {
                    sigma[1] = " IN ";
                    sigma[2] = joined.toString();
                    return;
                }
            }
            throw new RuntimeException("A predicate on " + level + " was expected in the scan");
        });
        return scan;
    }

    /** Where each varied level sits in the scan's rows; partitioning needs it among the groupers. */
    private static Map<String, Integer> variedColumns(Result window, Set<String> variedLevels) {
        List<String> labels = new ArrayList<>();
        window.getColumnLabels().forEach(label -> labels.add(label.toLowerCase()));
        Map<String, Integer> columns = new LinkedHashMap<>();
        for (String level : variedLevels) {
            int index = labels.indexOf(level.toLowerCase());
            if (index < 0) {
                throw new RuntimeException("Make sure that the comparison field is in the groupers");
            }
            columns.put(level, index);
        }
        return columns;
    }

    /** The scan's rows whose varied levels carry the slice's values. */
    private static Result slice(Result window, Map<String, Integer> variedColumns, Map<String, String> values) {
        ArrayList<Cell> cells = new ArrayList<>();
        for (Cell cell : window.getCells()) {
            boolean belongs = true;
            for (Map.Entry<String, Integer> varied : variedColumns.entrySet()) {
                if (!cell.getDimensionMembers().get(varied.getValue()).equals(values.get(varied.getKey()))) {
                    belongs = false;
                    break;
                }
            }
            if (belongs) {
                cells.add(cell);
            }
        }
        return new Result(window.getResultArray(), cells, window.getColumnNames(), window.getColumnLabels());
    }

    private static AssessBenchmark createBenchmark(CubeManagerAdapter adapter, List<String> details,
                                                   List<Map<String, String>> slices, Result window,
                                                   Map<String, Integer> variedColumns) {
        switch (BenchmarkKind.of(details)) {
            case CONSTANT:
                return new ConstantBenchmark(Double.parseDouble(details.get(1)));
            case SIBLING:
                return new SiblingBenchmark(slice(window, variedColumns, slices.get(0)), details.get(1));
            case PAST:
                List<Result> pastRecords = new ArrayList<>();
                for (Map<String, String> period : slices) {
                    Result periodResult = slice(window, variedColumns, period);
                    if (!periodResult.getCells().isEmpty()) {
                        pastRecords.add(periodResult);
                    }
                }
                if (pastRecords.isEmpty()) {
                    throw new RuntimeException("No past records for this query");
                }
                return new PastBenchmark(pastRecords, adapter.getDateSelectionPredicate());
            default:
                throw new RuntimeException("External Benchmark not implemented yet!");
        }
    }
}
