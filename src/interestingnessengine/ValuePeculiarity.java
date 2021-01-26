package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
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
	public double computeMeasure(IHistoryInput inputManager) {
		TreeSet<Cell> union = new TreeSet<Cell>(new CellComp());
		ArrayList<Cell> intersection = new ArrayList<Cell>();
		Instant startIntersection, endIntersection, startDetailedQuery, endDetailedQuery;
		long durationIntersection, totalIntersection = 0, durationDetailedQuery, totalDetailedQuery = 0;
		
		startDetailedQuery = Instant.now();
		
		detailedCurrentQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		
		endDetailedQuery = Instant.now();
		
		durationDetailedQuery = Duration.between(startDetailedQuery, endDetailedQuery).toMillis();
		totalDetailedQuery += durationDetailedQuery;
		
		for(CubeQuery query: inputManager.getQueryHistory()) {
			union = new TreeSet<Cell>(new CellComp());
			for(int j = 0; j < detailedCurrentQueryCube.size(); j++) {
				union.add(detailedCurrentQueryCube.get(j));
			}
			startDetailedQuery = Instant.now();
			
			detailedPreviousQueryCube = inputManager.computeDetailedQueryCube(query);
			
			endDetailedQuery = Instant.now();
			
			durationDetailedQuery = Duration.between(startDetailedQuery, endDetailedQuery).toMillis();
			totalDetailedQuery += durationDetailedQuery;
			
			intersection = new ArrayList<Cell>();
			
			startIntersection = Instant.now();	
			
			for(int i = 0; i < detailedCurrentQueryCube.size(); i++) {
				Cell c = detailedCurrentQueryCube.get(i);
				for(int j = 0; j < detailedPreviousQueryCube.size(); j++) {
					if(c.getDimensionMembers().toString().equals(detailedPreviousQueryCube.get(j).getDimensionMembers().toString())) {
						intersection.add(detailedPreviousQueryCube.get(j));
						break;
					}
				}
			}
			
			endIntersection = Instant.now();
			
			durationIntersection = Duration.between(startIntersection, endIntersection).toMillis();
			totalIntersection += durationIntersection;
			
			for(int j = 0; j < detailedPreviousQueryCube.size(); j++) {
				union.add(detailedPreviousQueryCube.get(j));
			}
			
			if(union.size() > 0) {
				double t = (double)intersection.size() /(double) union.size();
				
				temp = 1.0 - t;
				jaccardDistances.add(temp);
			}
			
		}
		
		try {
			String outputTxt = "\n\nValue Peculiarity \n"+
	    			"\tDetailed Query:\t" + totalDetailedQuery+ " ms\n"+
	    			 "\tIntersection:\t" +totalIntersection + " ms\n"+
	    			 "\tTotal Time:\t" + (totalDetailedQuery+totalIntersection) + " ms";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		jaccardDistances.sort(Comparator.naturalOrder());
		
		if(inputManager.getKthNeighbor() - 1 < 0 || inputManager.getKthNeighbor() - 1 >= jaccardDistances.size()) {
			System.out.println("K is out of bounds.");
			return -1; 
		}
		return jaccardDistances.get(inputManager.getKthNeighbor() - 1);
	}

}
