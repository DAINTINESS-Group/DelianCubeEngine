package cubemanager.queryoptimizer;

import cubemanager.cubebase.CubeQuery;

/**
 * Interface for query optimization operations.
 * Each implementation represents a specific optimization strategy (e.g. selectivity estimation)
 */
public interface IQueryOptimization {

	/**
	 * Performs the optimization operation
	 * @param query, the cube query to optimize
	 * @return the result, specific to each implementation
	 */
    Object optimize(CubeQuery query);
}
