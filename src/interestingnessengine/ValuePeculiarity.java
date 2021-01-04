package interestingnessengine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;
import cubemanager.cubebase.CubeQuery;
import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class ValuePeculiarity implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedPreviousQueryCube;
	private ArrayList<Cell> detailedCurrentQueryCube;
	private ArrayList<Double> jaccardDistances = new ArrayList<Double>();
	private double temp;
	
	public ValuePeculiarity(){}
	
	/**
	 * Computes the peculiarity of the current query as the smallest Jaccard distance of its cells compared to 
	 * the cells of every past query
	 * @param inputManager The current {@link InputManager} object
	 * @return the peculiarity value
	 */
	public double compute(IHistoryInput inputManager) {
		TreeSet<Cell> union = new TreeSet<Cell>(new CellComp());
		ArrayList<Cell> intersection = new ArrayList<Cell>();
		
		detailedCurrentQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
				
		for(CubeQuery query: inputManager.getQueryHistory()) {
			union = new TreeSet<Cell>(new CellComp());
			for(int j = 0; j < detailedCurrentQueryCube.size(); j++) {
				union.add(detailedCurrentQueryCube.get(j));
			}
			
			detailedPreviousQueryCube = inputManager.computeDetailedQueryCube(query);
			intersection = new ArrayList<Cell>();
						
			for(int i = 0; i < detailedCurrentQueryCube.size(); i++) {
				Cell c = detailedCurrentQueryCube.get(i);
				for(int j = 0; j < detailedPreviousQueryCube.size(); j++) {
					if(c.getDimensionMembers().toString().equals(detailedPreviousQueryCube.get(j).getDimensionMembers().toString())) {
						intersection.add(detailedPreviousQueryCube.get(j));
						break;
					}
				}
			}
						
			for(int j = 0; j < detailedPreviousQueryCube.size(); j++) {
				union.add(detailedPreviousQueryCube.get(j));
			}
			
			if(union.size() > 0) {
				double t = (double)intersection.size() /(double) union.size();
				
				temp = 1.0 - t;
				jaccardDistances.add(temp);
			}
			
		}
		
		jaccardDistances.sort(Comparator.naturalOrder());
		
		if(inputManager.getKthNeighbor() - 1 < 0 || inputManager.getKthNeighbor() - 1 >= jaccardDistances.size()) {
			System.out.println("K is out of bounds.");
			return -1; 
		}
		return jaccardDistances.get(inputManager.getKthNeighbor() - 1);
	}

}
