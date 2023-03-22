package interestingnessengine.expectedvaluesbased;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

import result.Cell;
import result.CellBelief;

public class PartialDetailedExtensionalBeliefBasedDetailedNovelty implements IInterestingnessMeasureWithExpectedValues{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> unknownCells;
	private HashMap<String, Double> beliefs;
	private Double threshold;
	
	
	/** Computes the belief based novelty of the current query according to the user's beliefs,
	 * where beliefs are extracted by analyzing the user's query history.
	 * @param inputManager The current {@link InputManager} object.
	 * @return the belief based novelty value in the range of [0.0 - 1.0].
	 * @author SpyridonKaloudis
	 */

	public double computeMeasure(IExpectedValuesInput inputManager) {
		ArrayList<CellBelief> aux = inputManager.getCellBeliefs();
		beliefs = new HashMap<String, Double>();
		
		
		for(CellBelief belief: aux) {
			Cell c = belief.getCell();
			String signature = c.getDimensionMembers().toString();
			beliefs.put(signature, belief.getBelief());
		}
		
		
		long startDetailedQuery = System.nanoTime();
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		

		long endDetailedQuery = System.nanoTime();
		long durationDetailedQuery = endDetailedQuery - startDetailedQuery;
		
		//assignBeliefs(allCellsVisited);
		
		long startAlgorithm = System.nanoTime();
		unknownCells = new ArrayList<Cell>();
		unknownCells.addAll(detailedQueryCube);

		// set threshold, which means that cells with belief probability above or equal to 0.5, are considered known) 
		threshold = 0.5;
		
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

	
	
	// Method for assigning beliefs.
	/*
	 /** Assigns the user's beliefs to each cell of the current query,
	 * based on the percentage of each cell's appearance in past queries.
	 * @param allCellsVisited an ArrayList of all cells visited by the user in past queries.
	 * @author SpyridonKaloudis
	 */
	/*
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
	}*/
	
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