package assess.benchmarks;

import assess.CubeManagerAdapter;
import result.Result;

import java.util.List;
import java.util.Map;

public class BenchmarkFactory {
    private final CubeManagerAdapter cubeManagerAdapter;

    public BenchmarkFactory(CubeManagerAdapter cubeManageradapter) {
        this.cubeManagerAdapter = cubeManageradapter;
    }

    public AssessBenchmark createBenchmark(List<String> details) {
        if (details.isEmpty()) {
            return null;
        }
        String benchmarkType = details.get(0);
        switch (benchmarkType) {
            case "Constant":
                return createConstantBenchmark(details.get(1));
            case "Sibling":
                return createSiblingBenchmark(details.get(1), details.get(2));
            case "Past":
                return createPastBenchmark(details.get(1));
            case "External":
                return createExternalBenchmark();
            default:
                throw new RuntimeException("Unrecognized Benchmark Type");
        }

    }

    private AssessBenchmark createConstantBenchmark(String benchmarkValue) {
        return new ConstantBenchmark(Double.parseDouble(benchmarkValue));
    }

    /**
     * Precondition: The selection predicates contain the level we want to slice <br>
     * For example: region should be in the group-by to say "against region = 'South Carolina'"
     *
     * @return a sibling benchmark
     */
    private AssessBenchmark createSiblingBenchmark(String siblingKey, String siblingValue) {
        Map<String, String> predicates = cubeManagerAdapter.getSelectionPredicates();
        if (predicates == null || predicates.get(siblingKey) == null) {
            throw new RuntimeException(siblingKey + " was not defined in original predicates");
        }
        cubeManagerAdapter.updateSelectionPredicate(siblingKey, siblingValue);
        return new SiblingBenchmark(
                cubeManagerAdapter.executeCubeQuery(
                        cubeManagerAdapter.translateToCubeQuery()
                ), siblingKey
        );
    }

    private AssessBenchmark createPastBenchmark(String pastRecordsNumber) {
        int recordsNumber = Integer.parseInt(pastRecordsNumber);
        List<Result> pastRecords = cubeManagerAdapter.collectPastRecords(recordsNumber);
        if (pastRecords.isEmpty()) throw new RuntimeException("No past records for this query");
        return new PastBenchmark(pastRecords,
                cubeManagerAdapter.getDateSelectionPredicate());
    }

    // This can not be currently implemented as a CubeManager instance only handles 1 cube
    private AssessBenchmark createExternalBenchmark() {
        throw new RuntimeException("External Benchmark not implemented yet!");
    }
}