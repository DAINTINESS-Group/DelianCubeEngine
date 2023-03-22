package interestingnessengine.expectedvaluesbased;

import java.util.ArrayList;

import cubemanager.cubebase.CubeQuery;
import result.Cell;
import result.Result;
/**
 * 
 * @author eiriniMouselli
 *
 */
public interface IExpectedValuesInput {
	/**
	 * @return a list containing the user's value predictions.
	 */
	public ArrayList<Cell> getExpectedValues();
	/**
	 * @return a list containing the user's label predictions.
	 */
	public ArrayList<Cell> getExpectedLabels(); 
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
	 * Computes the user's Detailed Area of Interest 
	 * <p>
	 * For every old {@link CubeQuery} in the history list, computes its detailed area and adds all the cells, that
	 * are not already in it, in the detailed area of interest.
	 * @return the union of all the old queries' detailed areas
	 */
	public ArrayList<Cell> computeDetailedAreaOfInterest();
	/**
	 * Computes the detailed cube for the query passed
	 * <p>
	 * Creates a copy of the given {@link CubeQuery}, sets all the Gamma Expressions to "lvl.0" and 
	 * executes the new {@link CubeQuery}.
	 * @param query The query for which to compute the detailed area
	 * @return the detailed area of the given query
	 */
	public ArrayList<Cell> computeDetailedQueryCube(CubeQuery query);
	
	/**
	 * @return the user's goals for a specific query
	 */
	public ArrayList<Cell> getQueryGoals();
	
	// --------------------- MADE PRIVATE IN INPUT MANAGER CLASS --------------------
	/**
	 * Parses the user's expected values. 
	 * <p>
	 * Goes through the given file, creates the {@link Cell}s with the expected values it contains 
	 * and populates the appropriate list.
	 * @param filePath The path to the file containing the user's value predictions
	 */
	//public int parseExpectedValues(String filePath);
	/**
	 * Parses the user's expected labels. 
	 * <p>
	 * Goes through the given file, creates the {@link Cell}s with the expected labels it contains 
	 * and populates the appropriate list.
	 * @param filePath The path to the file containing the user's label predictions
	 */
	//public int parseExpectedLabels(String folderPath);
}
