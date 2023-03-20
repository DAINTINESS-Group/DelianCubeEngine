package assess.benchmarks;

import assess.CubeManagerAdapter;

import java.util.List;
import java.util.Map;

public class BenchmarkFactory {
	private final CubeManagerAdapter queryStringGenerator;

	public BenchmarkFactory(CubeManagerAdapter queryStringGenerator) {
		this.queryStringGenerator = queryStringGenerator;
	}

	public AssessBenchmark createBenchmark(List<String> details) {
		String benchmarkType = details.get(0);
		switch (benchmarkType) {
			case "Constant":
				return createConstantBenchmark(details.get(1));
			case "Sibling":
				return createSiblingBenchmark(details.get(1), details.get(2));
			case "Past":
				return createPastBenchmark();
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
	 * @return
	 */
	private AssessBenchmark createSiblingBenchmark(String siblingKey, String siblingValue) {
		Map<String, String> predicates = queryStringGenerator.getSelectionPredicates();
		if (predicates.get(siblingKey) == null) {
			throw new RuntimeException(siblingKey + " was not defined in original predicates");
		}
		queryStringGenerator.updateSelectionPredicate(siblingKey, siblingValue);
		return new CubeBenchmark(queryStringGenerator.executeCubeQuery(queryStringGenerator.translateToCubeQuery()));
	}

	private AssessBenchmark createPastBenchmark() {
		return null;
	}

	// This can not be currently implemented as a CubeManager only handles 1 cube
	private AssessBenchmark createExternalBenchmark() {
		return null;
	}
}