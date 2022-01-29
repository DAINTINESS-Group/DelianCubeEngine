package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import result.Cell;

public class BeliefBasedDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> knownCells;
	private ArrayList<Cell> allCellsVisited;
	private HashMap<String, Float> beliefs;
	private HashMap<String, Integer> timesVisitedCell;
	private float threshold;
	private float pastQuerySize;
	
	/*	Keep until the final push, may need parts of it
	public BeliefBasedDetailedNovelty(IHistoryInput inputManager) {
		
		// Assigning beliefs based on % of cell's appearance in previous searches
		// E.G. the more times the user encountered a cell, the more likely they have a belief close to their values => Not novel
		// Could also assign with using a simple method instead of constructor
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		timesVisitedCell = new HashMap<String, Integer>();
		
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			String c = detailedQueryCube.get(i).getDimensionMembers().toString();
				if(timesVisitedCell.containsKey(c)) {
					timesVisitedCell.replace(c, timesVisitedCell.get(c)+1);
				}
				else {
					timesVisitedCell.put(c, 1);
				}					
		}
		
		beliefs = new HashMap<String, Float>();
		for(Entry<String, Integer> entry : timesVisitedCell.entrySet()) {
			beliefs.put(entry.getKey(), entry.getValue()/pastQuerySize);
		}
		
		
		//run through map for check
		for(Entry<String, Integer> entry : beliefs.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			System.out.println(key + "    " + value);
		}
		
	}
	*/
	
	/** Computes the belief based novelty of the current query according to the user's beliefs,
	 * where beliefs are extracted by analyzing the user's query history.
	 * @param inputManager The current {@link InputManager} object.
	 * @return the belief based novelty value in the range of [0.0 , 1.0].
	 * @author SpyridonKaloudis
	 */

	public double computeMeasure(IHistoryInput inputManager) {
		Instant startDetailedArea = Instant.now();
		allCellsVisited = inputManager.getAllCellsVisited();
		Instant endDetailedArea = Instant.now();
		long durationDetailedArea = Duration.between(startDetailedArea, endDetailedArea).toMillis();

		Instant startDetailedQuery = Instant.now();
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		Instant endDetailedQuery = Instant.now();
		long durationDetailedQuery = Duration.between(startDetailedQuery, endDetailedQuery).toMillis();

		Instant startAlgorithm = Instant.now();
		knownCells = new ArrayList<Cell>();
		knownCells.addAll(detailedQueryCube);
		assignBeliefs(allCellsVisited);

		// set threshold (0,05 means that if the user has seen the cell in more than 5% of his queries, 
		// then it's value is probably not novel to him because his beliefs are close to the actual value)
		threshold = (float)0.05;
		
		ArrayList<Cell> unknownCells = new ArrayList<Cell>();
	
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			String signature = c.getDimensionMembers().toString();
			if(beliefs.containsKey(signature)) {
				if(beliefs.get(signature)>=threshold) {
					unknownCells.add(c);
					removeSpecificCellFromArrayList(c);
				}
			}
		}
		Instant endAlgorithm = Instant.now();
		long durationAlgorithm = Duration.between(startAlgorithm, endAlgorithm).toMillis();

		try {
			String outputTxt = "\n\nBelief Based Detailed Novelty \n"+
	    			"\tDetailed Query:\t" + durationDetailedQuery+ " ms\n"+
	    			 "\tDetailed Area:\t" + durationDetailedArea + " ms \n"+
	    			 "\tCompute Algorithm:\t" +durationAlgorithm + " ms\n"+
	    			 "\tTotal Time:\t" + (durationDetailedQuery+durationDetailedArea+durationAlgorithm) + " ms";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}

		return(1 - (double) unknownCells.size() / ( (double) unknownCells.size() + (double) knownCells.size() ));
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
		
		timesVisitedCell = new HashMap<String, Integer>();
		pastQuerySize = allCellsVisited.size();
		
		for(Cell c: detailedQueryCube){
			String key = c.getDimensionMembers().toString();
			timesVisitedCell.put(key, 0);
		}
		
		for(Cell c: allCellsVisited) {
			String key = c.getDimensionMembers().toString();
			if(timesVisitedCell.containsKey(key)) {
				timesVisitedCell.replace(key, timesVisitedCell.get(key)+1);
			}	
		}
		
		beliefs = new HashMap<String, Float>();
		for(Entry<String, Integer> entry : timesVisitedCell.entrySet()) {
			beliefs.put(entry.getKey(), entry.getValue()/pastQuerySize);
		}
	}
	
	/** Removes a specific cell from the novelAreaOfInterest ArrayList
	 * @param cell The cell we want to remove
	 * @author SpyridonKaloudis
	 */
	private void removeSpecificCellFromArrayList(Cell cell) {
		for (Cell c: knownCells) {
			if(testifCellsHaveEqualSignatures(c.getDimensionMembers(), cell.getDimensionMembers())) {
				knownCells.remove(c);
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
	public boolean testifCellsHaveEqualSignatures(ArrayList<String> c1, ArrayList<String> c2) {
		if(c1.toString().equals(c2.toString())) {
			return true;
		}
		else{
			return false;
		}
	}

}