package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class PartialDetailedExtensionalRelevance implements IInterestingnessMeasureWithHistory {
	
	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> detailedAreaOfInterest;
	private HashMap<String, Cell> detailedAreaOfInterestHashMap;
	
	public PartialDetailedExtensionalRelevance(){}
	/**
	 * Computes the relevance of the current query as the pct of the cells in the current query 
	 * that the user has seen already.
	 * @param inputManager The current {@link InputManager} object
	 * @return the relevance value
	 */
	public double computeMeasure(IHistoryInput inputManager) {
		long startDetailedQuery = System.nanoTime();
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		long endDetailedQuery = System.nanoTime();
		
		long durationDetailedQuery = endDetailedQuery - startDetailedQuery;

		long startDetailedArea = System.nanoTime();
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterest();
		detailedAreaOfInterestHashMap = new HashMap<String, Cell>();
		for (Cell c: detailedAreaOfInterest) {
			String key = c.getDimensionMembers().toString();
			detailedAreaOfInterestHashMap.put(key, c);
		}
		long endDetailedArea = System.nanoTime();
		
		long durationDetailedArea =  endDetailedArea - startDetailedArea;
		
		ArrayList<Cell> intersection = new ArrayList<Cell>();
		
		long startIntersection = System.nanoTime();
		for(Cell c: detailedQueryCube) {
			String cellDimensionMembers = c.getDimensionMembers().toString();		
			if(detailedAreaOfInterestHashMap.containsKey(cellDimensionMembers)) {
				intersection.add(detailedAreaOfInterestHashMap.get(cellDimensionMembers));
			}
		}
		long endIntersection = System.nanoTime();
		
		long durationIntersection = endIntersection - startIntersection;
		
		try {
			String outputTxt = "\n\nPartial Detailed Extensional Relevance \n"+
	    			"\tDetailed Query:\t" + durationDetailedQuery+ " ns\n"+
	    			 "\tDetailed Area:\t" + durationDetailedArea + " ns \n"+
	    			 "\tIntersection:\t" +durationIntersection + " ns\n"+
	    			 "\tTotal Time:\t" + (durationDetailedQuery+durationDetailedArea+durationIntersection) + " ns";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		return( (double)intersection
				.size() / (double)detailedQueryCube.size());
	}
}
