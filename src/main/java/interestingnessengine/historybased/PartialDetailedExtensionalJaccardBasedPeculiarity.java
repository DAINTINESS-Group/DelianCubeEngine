package interestingnessengine.historybased;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import cubemanager.cubebase.CubeQuery;
import result.Cell;
import result.CellComparator;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class PartialDetailedExtensionalJaccardBasedPeculiarity implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedPreviousQueryCube;
	private ArrayList<Cell> detailedCurrentQueryCube;
	private ArrayList<Double> jaccardDistances = new ArrayList<Double>();
	private HashMap<String, Cell> detailedAreaOfInterestHashMap;
	private double temp;
	
	public PartialDetailedExtensionalJaccardBasedPeculiarity(){}
	
	/**
	 * Computes the peculiarity of the current query as the smallest Jaccard distance of its cells compared to 
	 * the cells of every past query
	 * @param inputManager The current {@link InputManager} object
	 * @return the peculiarity value
	 */
	public double computeMeasure(IHistoryInput inputManager) {
		TreeSet<Cell> union = new TreeSet<Cell>(new CellComparator());
		HashMap<String, Cell> intersection = new HashMap<String, Cell>();
		long startIntersection, endIntersection, startDetailedQuery, endDetailedQuery;
		long durationIntersection, totalIntersection = 0, durationDetailedQuery, totalDetailedQuery = 0;
		
		startDetailedQuery = System.nanoTime();
		
		detailedCurrentQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		
		endDetailedQuery = System.nanoTime();
		
		durationDetailedQuery = endDetailedQuery - startDetailedQuery;
		totalDetailedQuery += durationDetailedQuery;
		
		for(CubeQuery query: inputManager.getQueryHistory()) {
			union = new TreeSet<Cell>(new CellComparator());
			for(int j = 0; j < detailedCurrentQueryCube.size(); j++) {
				union.add(detailedCurrentQueryCube.get(j));
			}
			startDetailedQuery = System.nanoTime();
			
			detailedPreviousQueryCube = inputManager.computeDetailedQueryCube(query);
			detailedAreaOfInterestHashMap = new HashMap<String, Cell>();
			for (Cell c: detailedPreviousQueryCube) {
				String key = c.getDimensionMembers().toString();
				detailedAreaOfInterestHashMap.put(key, c);
			}
			
			endDetailedQuery = System.nanoTime();
			
			durationDetailedQuery = endDetailedQuery - startDetailedQuery;
			totalDetailedQuery += durationDetailedQuery;
			
			intersection = new HashMap<String, Cell>();
			
			startIntersection = System.nanoTime();	
			for(Cell c: detailedCurrentQueryCube) {
				
				ArrayList<String> cellDimensionMembers = c.getDimensionMembers();
				if(detailedAreaOfInterestHashMap.containsKey(cellDimensionMembers.toString())) {
					String key = c.getDimensionMembers().toString();
					intersection.put(key, c);
				}
				
				/*
				for(int j = 0; j < detailedPreviousQueryCube.size(); j++) {
					if(c.getDimensionMembers().toString().equals(detailedPreviousQueryCube.get(j).getDimensionMembers().toString())) {
						intersection.add(detailedPreviousQueryCube.get(j));
						break;
					}
				}*/
			}
	
			endIntersection = System.nanoTime();
			
			durationIntersection = endIntersection - startIntersection;
			totalIntersection += durationIntersection;
			
			for(int j = 0; j < detailedPreviousQueryCube.size(); j++) {
				union.add(detailedPreviousQueryCube.get(j));
			}
			
			if(union.size() > 0) {
				double t = (double)intersection.size() /(double) union.size();
				//System.out.println("Intersection of cells: "+(double)intersection.size());
				//System.out.println("Union of cells: " +(double)union.size());
				//System.out.println("T: "+t);
				temp = 1.0 - t;
				//System.out.println(temp);
				jaccardDistances.add(temp);
			}
			
		}
		
		try {
			String outputTxt = "\n\nPartial Detailed Extensional Jaccard Based Peculiarity \n"+
	    			"\tDetailed Query:\t" + totalDetailedQuery+ " ns\n"+
	    			 "\tIntersection:\t" +totalIntersection + " ns\n"+
	    			 "\tTotal Time:\t" + (totalDetailedQuery+totalIntersection) + " ns";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		jaccardDistances.sort(Comparator.naturalOrder());
		
		if(inputManager.getKthNeighbor() - 1 < 0 || inputManager.getKthNeighbor() - 1 >= jaccardDistances.size()) {
			System.out.println("K is out of bounds.");
			return -1; 
		}
		//for(int i=0; i<jaccardDistances.size();i++) {
		//	System.out.println(jaccardDistances.get(i));
		//}
		return jaccardDistances.get(inputManager.getKthNeighbor() - 1);
	}

}
