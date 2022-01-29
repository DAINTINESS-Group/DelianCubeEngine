package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import result.Cell;
import result.Result;

public class GoalBasedIntentionalRelevance implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> novelAreaOfInterest;
	private ArrayList<Cell> coveredAreaOfInterest;
	private ArrayList<Cell> userGoals;
	
	/** Computes the goal based relevance of the current query as the percentage of
	 * the cells in the current query match the user's goal.
	 * @param inputManager The current {@link InputManager} object.
	 * @return the goal based relevance value in the range of [0.0 , 1.0].
	 * @author SpyridonKaloudis
	 */

	public double computeMeasure(IHistoryInput inputManager) {
		
		Instant startDetailedQuery = Instant.now();
		coveredAreaOfInterest = new ArrayList<Cell>();
		Result res = inputManager.getCurrentQueryResult();
		detailedQueryCube = new ArrayList<Cell>();
		
		for(Cell c: res.getCells()) {
			detailedQueryCube.add(c);
		}

		novelAreaOfInterest = new ArrayList<Cell>();
		//Add all the current query's cells to noverAreaOfInterest ArrayList
		novelAreaOfInterest.addAll(detailedQueryCube);

		//get the user's goals by reading the necessary file
		userGoals = inputManager.getQueryGoals();
		Instant endDetailedQuery = Instant.now();
		long durationDetailedQuery = Duration.between(startDetailedQuery, endDetailedQuery).toMillis();

		Instant startAlgorithm = Instant.now();

		for (int i=0; i<detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for (int j=0; j<userGoals.size(); j++) {
				if(testifCellsHaveEqualSignatures(c.getDimensionMembers(),userGoals.get(j).getDimensionMembers())){
					//if c was part of the usre's goals, add it to the covered list and remove it from the novel list
					coveredAreaOfInterest.add(detailedQueryCube.get(j));
					novelAreaOfInterest.remove(detailedQueryCube.get(j));
					break;
				}
			}
		}
		Instant endAlgorithm = Instant.now();
		long durationAlgorithm = Duration.between(startAlgorithm, endAlgorithm).toMillis();

		try {
			String outputTxt = "\n\nGoal Based Relevance \n"+
	    			"\tDetailed Query:\t" + durationDetailedQuery+ " ms\n"+
	    			 "\tCompute Algorithm:\t" +durationAlgorithm + " ms\n"+
	    			 "\tTotal Time:\t" + (durationDetailedQuery+durationAlgorithm) + " ms";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}

		return (double) coveredAreaOfInterest.size() / (double) (coveredAreaOfInterest.size() + (double) novelAreaOfInterest.size());

	}
	
	/** Removes a specific cell from the novelAreaOfInterest ArrayList
	 * @param cell The cell we want to remove
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
