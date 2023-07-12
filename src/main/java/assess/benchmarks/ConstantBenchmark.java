package assess.benchmarks;

import result.Cell;

import java.util.List;
import java.util.Optional;

public class ConstantBenchmark implements AssessBenchmark{
	private final double value;

	ConstantBenchmark(double value) {
		this.value = value;
	}

	@Override
	public Optional<Cell> matchCell(Cell cell) {
		Cell matchedCell = new Cell(new String[] {"Constant", Double.toString(value), "1"});
		return Optional.of(matchedCell);
	}
}
