package assess;

import assess.deltas.DeltaScheme;
import assess.labelers.LabelingScheme;

public class AssessQuery {
	public final String targetCubeQuery;
	public final String benchmarkCubeQuery;
	public final DeltaScheme deltaFunction;
	public final LabelingScheme labelingScheme;

	public AssessQuery(String targetCubeQuery,
					   String benchmarkCubeQuery,
					   DeltaScheme deltaFunction,
					   LabelingScheme labelingScheme) {
		this.targetCubeQuery = targetCubeQuery;
		this.benchmarkCubeQuery = benchmarkCubeQuery;
		this.deltaFunction = deltaFunction;
		this.labelingScheme = labelingScheme;
	}
}
