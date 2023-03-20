package assess.benchmarks;

import result.Cell;
import result.Result;

import java.util.List;

public class CubeBenchmark implements AssessBenchmark {
	private final Result cubeResult;
	private int currentIndex = 0;

	public CubeBenchmark(Result cubeResult) { this.cubeResult = cubeResult; }

	@Override
	public double getCellValue() {
		List<Cell> cells = cubeResult.getCells();
		double result = cells.get(currentIndex).toDouble();
		currentIndex++;
		return result;
	}
}
