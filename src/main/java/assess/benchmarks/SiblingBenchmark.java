package assess.benchmarks;

import result.Cell;
import result.Result;

import java.util.Iterator;
import java.util.List;

public class SiblingBenchmark implements AssessBenchmark {
	private final Iterator<Cell> benchmarkCells;

	public SiblingBenchmark(Result cubeResult) {
		benchmarkCells = cubeResult.getCells().iterator();
	}

	@Override
	public double getCellValue() {
		return benchmarkCells.next().toDouble();
	}
}
