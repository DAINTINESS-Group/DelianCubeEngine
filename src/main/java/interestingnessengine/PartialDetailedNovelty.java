package interestingnessengine;

import java.util.ArrayList;
import result.Cell;

public class PartialDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedCurrentQueryCube;
	private ArrayList<Cell> detailedAreaOfInterest;
	private ArrayList<Cell> novelAreaOfInterest;

	@Override
	public double computeMeasure(IHistoryInput inputManager) {
		
		detailedCurrentQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());

		/**
		 * PV Comment: does detailedAreaOfInterest include the detailed area of the current query??
		 * If it does, then this always results in 0% novelty, right?
		 */
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterest();
		
		/**
		 * PV Comment: later, you gonna remove stuff from novelAreaOfInterest.
		 * ATTN: they are also removed from detailedAreaOfInterest, right?
		 * Woudln't you wanna explicitly add the contents of the latter to the former?
		 * I did the following correction, plz., chk if correct: 
		 */
		//novelAreaOfInterest = detailedAreaOfInterest;
		novelAreaOfInterest = new ArrayList<Cell>();
		novelAreaOfInterest.addAll(detailedAreaOfInterest);
		
		
		/* Update: NOT needed afterall, computeDetailedAreaOfInterest() doesn't include duplicates
		 * 
		//delete duplicate cells
        for (int i = 0; i < detailedAreaOfInterest.size(); i++ ) {
            if (!cellsVisited.contains(detailedAreaOfInterest.get(i))) {
            	cellsVisited.add(detailedAreaOfInterest.get(i));
            }
        }
        
        */
		
		/**
		 * PV Comment:
		 * (a) why do you need the union? unclear both wrt name and usage. Possibly useful for relevance but not novelty? Not sure...
		 * (b) probably refactor the test inside the if into a method sth like testifCellsHaveEqualSignatures(c1, c2) or sth like this?
		 */
		ArrayList<Cell> union = new ArrayList<Cell>();
        
		for(int i = 0; i < detailedCurrentQueryCube.size(); i++) {
			Cell c = detailedCurrentQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(c.getDimensionMembers().toString().equals(detailedAreaOfInterest.get(j).getDimensionMembers().toString())) {
					union.add(detailedAreaOfInterest.get(j));
					novelAreaOfInterest.remove(detailedAreaOfInterest.get(j));
					break;
				}
			}

		}

		return(novelAreaOfInterest.size()/detailedCurrentQueryCube.size());

		
	}

}
