package interestingnessengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import result.Cell;

public class BeliefBasedDetailedNovelty implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> knownCells;
	private ArrayList<Cell> allCellsVisited;
	private HashMap<String, Float> beliefs;
	private HashMap<String, Integer> timesVisitedCell;
	private float threshold;
	private float pastQuerySize;
	
	/*
	public BeliefBasedDetailedNovelty(IHistoryInput inputManager) {
		
		// Assigning beliefs based on % of cell's appearance in previous searches
		// E.G. the more times the user encountered a cell, the more likely they have a belief close to their values => Not novel
		// Could also assign with using a simple method instead of constructor
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		timesVisitedCell = new HashMap<String, Integer>();
		
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			String c = detailedQueryCube.get(i).getDimensionMembers().toString();
				if(timesVisitedCell.containsKey(c)) {
					timesVisitedCell.replace(c, timesVisitedCell.get(c)+1);
				}
				else {
					timesVisitedCell.put(c, 1);
				}					
		}
		
		beliefs = new HashMap<String, Float>();
		for(Entry<String, Integer> entry : timesVisitedCell.entrySet()) {
			beliefs.put(entry.getKey(), entry.getValue()/pastQuerySize);
		}
		
		
		//run through map for check
		for(Entry<String, Integer> entry : beliefs.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			System.out.println(key + "    " + value);
		}
		
	}
	*/
	
	public double computeMeasure(IHistoryInput inputManager) {
		
		allCellsVisited = inputManager.getAllCellsVisited();
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		knownCells = new ArrayList<Cell>();
		
		knownCells.addAll(detailedQueryCube);

		assignBeliefs(allCellsVisited);
        

		// set threshold (0,05 means that if the user has seen the cell in more than 5% of his queries, 
		// then it's value is probably not novel to him because his beliefs are close to the actual value)
		threshold = (float)0.05;
		
		ArrayList<Cell> unknownCells = new ArrayList<Cell>();

	
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			String signature = c.getDimensionMembers().toString();
			if(beliefs.containsKey(signature)) {
				if(beliefs.get(signature)>=threshold) {
					unknownCells.add(c);
					removeSpecificCellFromArrayList(c);
				}
			}
		}

		return(1 - (double) unknownCells.size() / ( (double) unknownCells.size() + (double) knownCells.size() ));
	}

	
	// Method for assigning beliefs. Just in case we wanna use this instead of a constructor
	
	private void assignBeliefs(ArrayList<Cell> allCellsVisited) {
		// Assigning beliefs based on % of cell's appearance in previous searches
		// E.G. the more times the user encountered a cell, the more likely they have a belief close to their values => Not novel
		
		timesVisitedCell = new HashMap<String, Integer>();
		pastQuerySize = allCellsVisited.size();
		
		for(Cell c: detailedQueryCube){
			String key = c.getDimensionMembers().toString();
			timesVisitedCell.put(key, 0);
		}
		
		for(Cell c: allCellsVisited) {
			String key = c.getDimensionMembers().toString();
			if(timesVisitedCell.containsKey(key)) {
				timesVisitedCell.replace(key, timesVisitedCell.get(key)+1);
			}	
		}
		
		beliefs = new HashMap<String, Float>();
		for(Entry<String, Integer> entry : timesVisitedCell.entrySet()) {
			beliefs.put(entry.getKey(), entry.getValue()/pastQuerySize);
		}
	}
		
	private void removeSpecificCellFromArrayList(Cell cell) {
		for (Cell c: knownCells) {
			if(testifCellsHaveEqualSignatures(c.getDimensionMembers(), cell.getDimensionMembers())) {
				knownCells.remove(c);
				return;
			}
		}
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