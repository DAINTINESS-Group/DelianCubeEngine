package interestingnessengine.historybased;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import result.Cell;
import result.CellBelief;

public class PartialDetailedExtensionalBeliefBasedDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> unknownCells;
	private ArrayList<Cell> allCellsVisited;
	private ArrayList<CellBelief> beliefss;
	private HashMap<String, Float> beliefs;
	private HashMap<String, Integer> timesVisitedCell;
	private float threshold;
	private int historySize;
	
	
	/** Computes the belief based novelty of the current query according to the user's beliefs,
	 * where beliefs are extracted by analyzing the user's query history.
	 * @param inputManager The current {@link InputManager} object.
	 * @return the belief based novelty value in the range of [0.0 , 1.0].
	 * @author SpyridonKaloudis
	 */

	public double computeMeasure(IHistoryInput inputManager) {
		beliefss=inputManager.getCellBeliefs();
		
		
		allCellsVisited = inputManager.getAllCellsVisited();
		historySize = inputManager.getQueryHistory().size();
		timesVisitedCell = new HashMap<String, Integer>();
		
		
		long startDetailedQuery = System.nanoTime();
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		for(Cell c: detailedQueryCube){
			String key = c.getDimensionMembers().toString();
			timesVisitedCell.put(key, 0);
		}
		long endDetailedQuery = System.nanoTime();
		long durationDetailedQuery = endDetailedQuery - startDetailedQuery;
		
		assignBeliefs(allCellsVisited);
		
		long startAlgorithm = System.nanoTime();
		unknownCells = new ArrayList<Cell>();
		unknownCells.addAll(detailedQueryCube);

		// set threshold (0,05 means that if the user has seen the cell in more than 5% of his queries, 
		// then it's value is probably not novel to him because his beliefs are close to the actual value)
		threshold = (float)0.05*historySize;
		//System.out.println("TO THRESHOLD EINAI:" +threshold );
		
		ArrayList<Cell> knownCells = new ArrayList<Cell>();
	
		for(Cell c: detailedQueryCube) {
			String signature = c.getDimensionMembers().toString();
			if(beliefs.containsKey(signature)) {
				if(beliefs.get(signature)>=threshold) {
					knownCells.add(c);
					removeSpecificCellFromArrayList(c);
					
				}
			}
		}
		long endAlgorithm = System.nanoTime();
		long durationAlgorithm = endAlgorithm - startAlgorithm;

		try {
			String outputTxt = "\n\nBelief Based Detailed Novelty \n"+
	    			"\tDetailed Query:\t" + durationDetailedQuery+ " ns\n"+
	    			 "\tCompute Algorithm:\t" +durationAlgorithm + " ns\n"+
	    			 "\tTotal Time:\t" + (durationDetailedQuery+durationAlgorithm) + " ns";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		return (double) unknownCells.size() / ( (double) knownCells.size() + (double) unknownCells.size() );
	}

	/** Assigns the user's beliefs to each cell of the current query,
	 * based on the percentage of each cell's appearance in past queries.
	 * @param allCellsVisited an ArrayList of all cells visited by the user in past queries.
	 * @author SpyridonKaloudis
	 */
	
	// Method for assigning beliefs.
	private void assignBeliefs(ArrayList<Cell> allCellsVisited) {
		// Assigning beliefs based on % of cell's appearance in previous searches
		// E.G. the more times the user encountered a cell, the more likely they have a belief close to their values => Not novel

			
		for(Cell c: allCellsVisited) {
			String key = c.getDimensionMembers().toString();
			if(timesVisitedCell.containsKey(key)) {
				timesVisitedCell.replace(key, timesVisitedCell.get(key)+1);
			}	
		}
		
		beliefs = new HashMap<String, Float>();
		
		for(Entry<String, Integer> entry : timesVisitedCell.entrySet()) {
			beliefs.put(entry.getKey(), (float)entry.getValue()/historySize);
			
		}
	}
	
	/** Removes a specific cell from the novelAreaOfInterest ArrayList
	 * @param cell The cell we want to remove
	 * @author SpyridonKaloudis
	 */
	private void removeSpecificCellFromArrayList(Cell cell) {
		for (Cell c: unknownCells) {
			if(testIfCellsHaveEqualSignatures(c.getDimensionMembers(), cell.getDimensionMembers())) {
				unknownCells.remove(c);
				return;
			}
		}
	}

	/** Determines if the signatures of two cells are the same.
	 * @param c1 a cell of a detailed cube query answer.
	 * @param c2 a cell of a detailed cube query answer.
	 * @return true if the cells have equal signatures, false otherwise.
	 * @author SpyridonKaloudis
	 */
	private boolean testIfCellsHaveEqualSignatures(ArrayList<String> c1, ArrayList<String> c2) {
		if(c1.toString().equals(c2.toString())) {
			return true;
		}
		else{
			return false;
		}
	}

}