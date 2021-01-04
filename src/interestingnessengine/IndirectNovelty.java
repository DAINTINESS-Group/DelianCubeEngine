package interestingnessengine;

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
	public double compute(IHistoryInput inputManager) {
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterest();
		
		ArrayList<Cell> intersection = new ArrayList<Cell>();
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(c.getDimensionMembers().toString().equals(detailedAreaOfInterest.get(j).getDimensionMembers().toString())) {
					intersection.add(detailedAreaOfInterest.get(j));
					break;
				}
			}
		}
				
		return( 1- ((double)intersection
				.size() / (double)detailedQueryCube.size()));
	}
}
