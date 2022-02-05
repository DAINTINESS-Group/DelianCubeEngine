package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class IndirectNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> detailedAreaOfInterest;
	
	public IndirectNovelty() {}
	/**
	 * Computes the indirect novelty of the current query as the pct of the cells in the current query 
	 * that the user has not seen yet.
	 * @param inputManager The current {@link InputManager} object
	 * @return the indirect novelty value
	 */
	public double computeMeasure(IHistoryInput inputManager) {
		long startDetailedQuery = System.nanoTime();
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		
		long endDetailedQuery = System.nanoTime();
		
		long durationDetailedQuery = endDetailedQuery - startDetailedQuery;

		long startDetailedArea = System.nanoTime();
		
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterest();
		
		long endDetailedArea = System.nanoTime();
		
		long durationDetailedArea = endDetailedArea - startDetailedArea;
		
		ArrayList<Cell> intersection = new ArrayList<Cell>();
		
		long startIntersection = System.nanoTime();
		
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(c.getDimensionMembers().toString().equals(detailedAreaOfInterest.get(j).getDimensionMembers().toString())) {
					intersection.add(detailedAreaOfInterest.get(j));
					break;
				}
			}
		}
		
		long endIntersection = System.nanoTime();
		
		long durationIntersection = endIntersection - startIntersection;
		
		try {
			String outputTxt = "\n\nIndirect Novelty \n"+
	    			"\tDetailed Query:\t" + durationDetailedQuery+ " ns\n"+
	    			 "\tDetailed Area:\t" + durationDetailedArea + " ns \n"+
	    			 "\tIntersection:\t" +durationIntersection + " ns\n"+
	    			 "\tTotal Time:\t" + (durationDetailedQuery+durationDetailedArea+durationIntersection) + " ns";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		return( 1- ((double)intersection
				.size() / (double)detailedQueryCube.size()));
	}
}
