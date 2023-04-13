package assess;

import assess.benchmarks.AssessBenchmark;
import assess.deltas.DeltaScheme;
import assess.labelers.LabelingScheme;
import cubemanager.cubebase.CubeQuery;

public class AssessQuery {
	public final CubeQuery targetCubeQuery;
	public final AssessBenchmark benchmark;
	public final DeltaScheme deltaFunction;
	public final LabelingScheme labelingScheme;

	public AssessQuery(CubeQuery targetCubeQuery,
                       AssessBenchmark benchmark,
                       DeltaScheme deltaFunction,
                       LabelingScheme labelingScheme) {
		this.targetCubeQuery = targetCubeQuery;
		this.benchmark = benchmark;
		this.deltaFunction = deltaFunction;
		this.labelingScheme = labelingScheme;
	}
}
