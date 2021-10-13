package interestingnessengine;

import java.util.ArrayList;
import result.Result;
import result.Cell;
import cubemanager.cubebase.CubeQuery;
/**
 * 
 * @author eiriniMouselli
 *
 */
public interface IHistoryInput {
	/**
	 * Adds the given {@link Result} to the results history list.
	 * @param latestResult The {@link Result} to be added
	 */
	public int updateQueryHistoryResults(Result latestResult);
	/**
	 * @return the results history list.
	 */
	public ArrayList<Result> getQueryHistoryResults();
	/**
	 * Adds the given {@link CubeQuery} to the queries history list.
	 * @param latestQuery The {@link CubeQuery} to be added
	 */
	public int updateQueryHistory(CubeQuery latestQuery);
	/**
	 * @return the queries history list.
	 */
	public ArrayList<CubeQuery> getQueryHistory();
	/**	
	 * @return the kth neighbor
	 */
	public int getKthNeighbor();
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
	
	// --------------------- MADE PRIVATE IN INPUT MANAGER CLASS --------------------
	/** 
	 * Parses the user's query history results
	 * <p>
	 * Goes through every file in given folder and for each file creates a {@link Result} object 
	 * with the {@link Cell}s contained in the file and populates the appropriate list.
	 * @param filePath The path to the file containing previous queries' results
	 */
	//public int parseQueryHistoryResults(String filePath);
	/**
	 * Parses the user's query history 
	 * <p>
	 * Goes through every file in given folder and for each file creates a {@link CubeQuery} object from 
	 * the raw query string contained in the file.
	 * @param folderPath The path to the folder containing previous queries
	 * @throws RemoteException
	 */
	//public int parseQueryHistory(String folderPath) throws RemoteException;
	/**
	 * Sets the value of the kth neighbor to be k.
	 * @param k The desired value of the kth neighbor. >= 1
	 */
	//public void setKthNeighbor(int k);
}
