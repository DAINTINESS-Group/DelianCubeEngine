package assess.benchmarks;

public class ConstantBenchmark implements AssessBenchmark{
	private final double value;

	ConstantBenchmark(double value) {
		this.value = value;
	}

	@Override
	public double getCellValue() {
		return value;
	}
}
