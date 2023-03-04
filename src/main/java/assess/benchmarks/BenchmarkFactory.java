package assess.benchmarks;

import java.util.List;

public class BenchmarkFactory {
	List<String> benchmarkDetails;

	public BenchmarkFactory(List<String> benchmarkDetails) {
		this.benchmarkDetails = benchmarkDetails;
	}

	public AssessBenchmark createBenchmarkQuery() {
		switch (benchmarkDetails.get(0)) {
			case "Constant": return createConstantBenchmarkQuery();
			case "Sibling": return createSiblingBenchmarkQuery();
			case "Past": return createPastBenchmarkQuery();
			case "External": return createExternalBenchmarkQuery();
			default: throw new RuntimeException("Unrecognized Benchmark Type");
		}

	}

	private AssessBenchmark createConstantBenchmarkQuery() {
		double benchmarkValue = Double.parseDouble(benchmarkDetails.get(1));
		return new ConstantBenchmark(benchmarkValue);
	}

	private AssessBenchmark createSiblingBenchmarkQuery() {
		return null;
	}

	private AssessBenchmark createPastBenchmarkQuery() {
		return null;
	}

	// This can not be currently implemented as a CubeManager only handles 1 cube
	private AssessBenchmark createExternalBenchmarkQuery() {
		return null;
	}
}

