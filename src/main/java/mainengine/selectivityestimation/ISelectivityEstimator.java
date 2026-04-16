package mainengine.selectivityestimation;

import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;

/**
 * Interface for selectivity estimation methods
 */
public interface ISelectivityEstimator {

	/**
	 * Takes a cube query and returns the selectivity for each of its sigma
	 * predicates.
	 *
	 * @param query       the cube query we want to estimate
	 * @param cubeManager needed to access metadata and run queries against the db
	 * @return a list of SelectivityResult, one per sigma predicate
	 */
	List<SelectivityResult> estimate(CubeQuery query, CubeManager cubeManager);
}
