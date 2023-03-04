package assess.benchmarks;

import cubemanager.cubebase.BasicStoredCube;

public class CubeBenchmark implements AssessBenchmark {
	private final BasicStoredCube cube;

	public CubeBenchmark(BasicStoredCube cube) { this.cube = cube; }

	@Override
	public double getCellValue() {
		// Must create an iterator over the cube's measurements
		System.out.println(cube.getMeasuresList());
		return 0;
	}
}
