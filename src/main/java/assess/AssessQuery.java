package assess;

import assess.deltas.AbstractDeltaClass;
import assess.labelers.LabelingScheme;

public class AssessQuery {
	public final String targetCubeQuery;
	public final String benchmarkCubeQuery;
	public final AbstractDeltaClass deltaFunction;
	public final LabelingScheme labelingScheme;

	public AssessQuery(String targetCubeQuery,
					   String benchmarkCubeQuery,
					   AbstractDeltaClass deltaFunction,
					   LabelingScheme labelingScheme) {
		this.targetCubeQuery = targetCubeQuery;
		this.benchmarkCubeQuery = benchmarkCubeQuery;
		this.deltaFunction = deltaFunction;
		this.labelingScheme = labelingScheme;
	}
}
