package assess;

import assess.deltas.AbstractDeltaClass;
import assess.labelers.LabelingScheme;
import cubemanager.cubebase.CubeQuery;

public class AssessQuery {
	public final CubeQuery targetCube;
	public final CubeQuery benchmarkCube;
	public final AbstractDeltaClass deltaFunction;
	public final LabelingScheme labelingScheme;

	public AssessQuery(CubeQuery targetCube,
					   CubeQuery benchmarkCube,
					   AbstractDeltaClass deltaFunction,
					   LabelingScheme labelingScheme) {
		this.targetCube = targetCube;
		this.benchmarkCube = benchmarkCube;
		this.deltaFunction = deltaFunction;
		this.labelingScheme = labelingScheme;
	}
}
