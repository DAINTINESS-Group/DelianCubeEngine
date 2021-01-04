package interestingnessengine;

import java.util.ArrayList;
import cubemanager.cubebase.CubeQuery;
import result.Cell;
import result.Result;
/**
 * 
 * @author eiriniMouselli
 *
 */
public interface IInput {
	
	/**
	 * Updates the value of the current {@link CubeQuery}.
	 * @param current The latest query
	 */
	public void setCurrentQuery(CubeQuery current);
	/**
	 * @return the current {@link CubeQuery}.
	 */
	public CubeQuery getCurrentQuery();
	/**
	 * Updates the value of the current {@link Result}.
	 * @param result The latest query's result.
	 */
	public void setCurrentQueryResult(Result result);
	/**
	 * @return the current {@link CubeQuery}'s {@link Result}.
	 */
	public Result getCurrentQueryResult();
	/**
	 * For every old {@link CubeQuery} in the history list, computes its detailed area and adds all the cells, that
	 * are not already in it, in the detailed area of interest.
	 * @return the union of all the old queries' detailed areas
	 */
	public ArrayList<Cell> computeDetailedAreaOfInterest();
	/**
	 * Creates a copy of the given {@link CubeQuery}, sets all the Gamma Expressions to "lvl.0" and 
	 * executes the new {@link CubeQuery}.
	 * @param query The query for which to compute the detailed area
	 * @return the detailed area of the given query
	 */
	public ArrayList<Cell> computeDetailedQueryCube(CubeQuery query);
}
