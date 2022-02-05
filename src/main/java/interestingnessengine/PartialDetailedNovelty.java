package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import result.Cell;

public class PartialDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> detailedAreaOfInterest;
	private ArrayList<Cell> novelAreaOfInterest;

	
	/** Computes the partial detailed novelty of the current query as the percentage of
	 * the cells in the current query that the user has not seen yet.
	 * @param inputManager The current {@link InputManager} object.
	 * @return the partial detailed novelty value in the range of [0.0 , 1.0].
	 * @author SpyridonKaloudis
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

		long startAlgorithm = System.nanoTime();
		novelAreaOfInterest = new ArrayList<Cell>();
		novelAreaOfInterest.addAll(detailedQueryCube);
		
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for(int j = 0; j < detailedAreaOfInterest.size(); j++) {
				if(testIfCellsHaveEqualSignatures(c.getDimensionMembers(),detailedAreaOfInterest.get(j).getDimensionMembers())) {
					removeSpecificCellFromArrayList(detailedAreaOfInterest.get(j));
					break;
				}
			}

		}
		
		long endAlgorithm = System.nanoTime();
		long durationAlgorithm = endAlgorithm - startAlgorithm;

		try {
			String outputTxt = "\n\nPartial Detailed Novelty \n"+
	    			"\tDetailed Query:\t" + durationDetailedQuery+ " ns\n"+
	    			 "\tDetailed Area:\t" + durationDetailedArea + " ns \n"+
	    			 "\tCompute Algorithm:\t" +durationAlgorithm + " ns\n"+
	    			 "\tTotal Time:\t" + (durationDetailedQuery+durationDetailedArea+durationAlgorithm) + " ns";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}

		return(((double) novelAreaOfInterest.size()/ (double) detailedQueryCube.size()));
		
	}
	
	/** Removes a specific cell from the novelAreaOfInterest ArrayList
	 * @param cell The cell we want to remove
	 * @author SpyridonKaloudis
	 */
	
	private void removeSpecificCellFromArrayList(Cell cell) {
		for (Cell c: novelAreaOfInterest) {
			if(testIfCellsHaveEqualSignatures(c.getDimensionMembers(), cell.getDimensionMembers())) {
				novelAreaOfInterest.remove(c);
				return;
			}
		}
	}
	
	/** Determines if the signatures of two cells are the same.
	 * @param c1 a cell of a detailed cube query answer.
	 * @param c2 a cell of a detailed cube query answer.
	 * @return true if the cells have equal signatures, false otherwise.
	 * @author SpyridonKaloudis
	 */
	
	private boolean testIfCellsHaveEqualSignatures(ArrayList<String> c1, ArrayList<String> c2) {
		if(c1.toString().equals(c2.toString())) {
			return true;
		}
		else{
			return false;
		}
	}

}
