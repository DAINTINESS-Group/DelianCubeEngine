package interestingnessengine;

import java.util.ArrayList;
import result.Cell;

public class PartialDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> detailedAreaOfInterest;
	private ArrayList<Cell> novelAreaOfInterest;

	
	public double computeMeasure(IHistoryInput inputManager) {
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		detailedAreaOfInterest = inputManager.computeDetailedAreaOfInterestOfPreviousQueries();
		
		novelAreaOfInterest = new ArrayList<Cell>();
		novelAreaOfInterest.addAll(detailedAreaOfInterest);
		        
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(testifCellsHaveEqualSignatures(c.getDimensionMembers(),detailedAreaOfInterest.get(j).getDimensionMembers())) {
					novelAreaOfInterest.remove(detailedAreaOfInterest.get(j));
					break;
				}
			}

		}

		return(novelAreaOfInterest.size()/detailedQueryCube.size());

		
	}
	
	public boolean testifCellsHaveEqualSignatures(ArrayList<String> c1, ArrayList<String> c2) {
		if(c1.toString().equals(c2.toString())) {
			return true;
		}
		else{
			return false;
		}
	}

}
