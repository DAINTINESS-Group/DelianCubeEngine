package interestingnessengine;

import java.util.ArrayList;
import java.util.HashMap;
import result.Cell;

public class BeliefBasedDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> knownCells;
	private ArrayList<Cell> detailedAreaOfInterest;
	private HashMap<Cell,Integer> beliefs;
	private float threshold;
	
	public double computeMeasure(IHistoryInput inputManager) {
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		knownCells = new ArrayList<Cell>();
		knownCells.addAll(detailedQueryCube);
		
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterestOfPreviousQueries();
		
		assignBeliefs();
        
		// set threshold (0,1 means that if the user has seen the cell in more than 10% of his queries, 
		// then it's value is probably not novel to him because of his beliefes)
		threshold = (float)0.1;
		
		ArrayList<Cell> unknownCells = new ArrayList<Cell>();

		
		int pastQuerySize = detailedAreaOfInterest.size();

		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			if(beliefs.containsKey(c)) {
				if((beliefs.get(c)/pastQuerySize)>threshold) {
					unknownCells.add(c);
					knownCells.remove(i);
				}
			}
		}
		return( unknownCells.size() / ( unknownCells.size() + knownCells.size() ));

		
	}

	private void assignBeliefs() {
		
		// Assigning beliefs based on % of cell's appearance in previous searches
		// E.G. the more times the user encountered a cell, the more likely they have a belief close to their values => Not novel
		
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
				if(beliefs.containsKey(c)) {
					beliefs.replace(c, beliefs.get(c)+1);
				}
				else {
					beliefs.put(c, 0);
				}					
		}
		
	}
		
}