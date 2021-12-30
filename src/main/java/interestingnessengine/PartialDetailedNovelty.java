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

		novelAreaOfInterest = new ArrayList<Cell>();
		novelAreaOfInterest.addAll(detailedQueryCube);
		
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(testifCellsHaveEqualSignatures(c.getDimensionMembers(),detailedAreaOfInterest.get(j).getDimensionMembers())) {
					removeSpecificCellFromArrayList(detailedAreaOfInterest.get(j));
					break;
				}
			}

		}
		
		return(((double) novelAreaOfInterest.size()/ (double) detailedQueryCube.size()));
		
	}
	
	private void removeSpecificCellFromArrayList(Cell cell) {
		for (Cell c: novelAreaOfInterest) {
			if(testifCellsHaveEqualSignatures(c.getDimensionMembers(), cell.getDimensionMembers())) {
				novelAreaOfInterest.remove(c);
				return;
			}
		}
	}

	/*
	 * Symfwna me tis dokimes pou exw kanei, ta signatures einai panta me thn idia seira opote den xreiazetai e3tra check (!)
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
