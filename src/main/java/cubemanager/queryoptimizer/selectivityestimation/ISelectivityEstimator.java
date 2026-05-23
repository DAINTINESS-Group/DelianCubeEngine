package cubemanager.queryoptimizer.selectivityestimation;

import java.util.List;

import cubemanager.cubebase.CubeQuery;

public interface ISelectivityEstimator {

	List<SelectivityResult> estimate(CubeQuery query, int factTableSize);

	/**
	 * Returns the fact table size loaded from the pre-built statistics file, or -1
	 * if the estimator has no pre-built file (e.g. FullTableScan)
	 */
	default int getFactTableSize() {return -1 ;}
}
