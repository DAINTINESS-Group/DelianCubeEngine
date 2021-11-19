package interestingnessengine;

import java.util.ArrayList;
import result.Cell;

public class PartialDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> detailedAreaOfInterest;
	private ArrayList<Cell> novelAreaOfInterest;

	
	public double computeMeasure(IHistoryInput inputManager) {
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterest();
		novelAreaOfInterest = detailedAreaOfInterest;
		
		/* Update: NOT needed afterall, computeDetailedAreaOfInterest() doesn't include duplicates
		 * 
		//delete duplicate cells
        for (int i = 0; i < detailedAreaOfInterest.size(); i++ ) {
            if (!cellsVisited.contains(detailedAreaOfInterest.get(i))) {
            	cellsVisited.add(detailedAreaOfInterest.get(i));
            }
        }
        
        */
		
		ArrayList<Cell> union = new ArrayList<Cell>();
        
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(c.getDimensionMembers().toString().equals(detailedAreaOfInterest.get(j).getDimensionMembers().toString())) {
					union.add(detailedAreaOfInterest.get(j));
					novelAreaOfInterest.remove(detailedAreaOfInterest.get(j));
					break;
				}
			}

		}

		return(novelAreaOfInterest.size()/detailedQueryCube.size());

		
	}

}
