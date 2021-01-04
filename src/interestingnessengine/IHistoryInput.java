package interestingnessengine;

import java.rmi.RemoteException;
import java.util.ArrayList;
import result.Result;
import result.Cell;
import cubemanager.cubebase.CubeQuery;
/**
 * 
 * @author eiriniMouselli
 *
 */
public interface IHistoryInput extends IInput {

	/**
	 * Goes through every file in given folder and for each file creates a {@link Result} object 
	 * with the {@link Cell}s contained in the file and populates the appropriate list.
	 * @param filePath The path to the file containing previous queries' results
	 */
	public void parseQueryHistoryResults(String filePath);
	/**
	 * Adds the given {@link Result} to the results history list.
	 * @param latestResult The {@link Result} to be added
	 */
	public void updateQueryHistoryResults(Result latestResult);
	/**
	 * @return the results history list.
	 */
	public ArrayList<Result> getQueryHistoryResults();
	/**
	 * Goes through every file in given folder and for each file creates a {@link CubeQuery} object from 
	 * the raw query string contained in the file.
	 * @param folderPath The path to the folder containing previous queries
	 * @throws RemoteException
	 */
	public void parseQueryHistory(String folderPath) throws RemoteException;
	/**
	 * Adds the given {@link CubeQuery} to the queries history list.
	 * @param latestQuery The {@link CubeQuery} to be added
	 */
	public void updateQueryHistory(CubeQuery latestQuery);
	/**
	 * @return the queries history list.
	 */
	public ArrayList<CubeQuery> getQueryHistory();
	/**	
	 * @return the kth neighbor
	 */
	public int getKthNeighbor();
	/**
	 * Sets the value of the kth neighbor to be k.
	 * @param k The desired value of the kth neighbor. >= 1
	 */
	public void setKthNeighbor(int k);
}
