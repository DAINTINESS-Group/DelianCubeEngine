package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class RelevanceWithDAI implements IInterestingnessMeasureWithHistory {
	
	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> detailedAreaOfInterest;
	
	public RelevanceWithDAI(){}
	/**
	 * Computes the relevance of the current query as the pct of the cells in the current query 
	 * that the user has seen already.
	 * @param inputManager The current {@link InputManager} object
	 * @return the relevance value
	 */
	public double computeMeasure(IHistoryInput inputManager) {
		Instant startDetailedQuery = Instant.now();
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		Instant endDetailedQuery = Instant.now();
		
		long durationDetailedQuery = Duration.between(startDetailedQuery, endDetailedQuery).toMillis();

		Instant startDetailedArea = Instant.now();
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterest();
		Instant endDetailedArea = Instant.now();
		
		long durationDetailedArea = Duration.between(startDetailedArea, endDetailedArea).toMillis();
		
		ArrayList<Cell> intersection = new ArrayList<Cell>();
		
		Instant startIntersection = Instant.now();
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(c.getDimensionMembers().toString().equals(detailedAreaOfInterest.get(j).getDimensionMembers().toString())) {
					intersection.add(detailedAreaOfInterest.get(j));
					break;
				}
			}
		}
		Instant endIntersection = Instant.now();
		
		long durationIntersection = Duration.between(startIntersection, endIntersection).toMillis();
		
		try {
			String outputTxt = "\n\nRelevanceWithDAI \n"+
	    			"\tDetailed Query:\t" + durationDetailedQuery+ " ms\n"+
	    			 "\tDetailed Area:\t" + durationDetailedArea + " ms \n"+
	    			 "\tIntersection:\t" +durationIntersection + " ms\n"+
	    			 "\tTotal Time:\t" + (durationDetailedQuery+durationDetailedArea+durationIntersection) + " ms";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		return( (double)intersection
				.size() / (double)detailedQueryCube.size());
	}
}
