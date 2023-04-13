package assess.benchmarks;

import result.Cell;
import result.Result;

import java.util.List;

public class SiblingBenchmark implements AssessBenchmark {
	private final List<Cell> cells;
	private int currentIndex = 0;

	public SiblingBenchmark(Result cubeResult) {cells = cubeResult.getCells();}

	@Override
	public double getCellValue() {
		double cellValue = cells.get(currentIndex).toDouble();
		currentIndex++;
		return cellValue;
	}
}
