package cubemanager.queryoptimizer.selectivityestimation;

import java.util.List;

import cubemanager.cubebase.CubeQuery;

public interface ISelectivityEstimator {

	/**
	 * Estimates the selectivity of every sigma predicate in the query
	 * @param query
	 * @param factTableSize the number of rows in the fact table, used as the denominator
	 * @return one result per sigma that could be resolved (the list could be shorter than the query's sigma list,
	 * since unresolved predicates are skipped
	 */
	List<SelectivityResult> estimate(CubeQuery query, int factTableSize);

	/**
	 * Returns the fact table size loaded from the pre-built statistics file, or -1
	 * if the estimator has no pre-built file (e.g. FullTableScan)
	 */
	default int getFactTableSize() {return -1 ;}
}
