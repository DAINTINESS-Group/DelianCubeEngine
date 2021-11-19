package interestingnessengine;

import java.util.ArrayList;
import java.util.Random;
import result.Cell;

public class BeliefBasedDetailedNovelty implements IInterestingnessMeasureWithExpectedValues{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> knownCells;
	private ArrayList<Float> probabilities;
	private float threshold;
	private Random rd = new Random();
	
	public double computeMeasure(IExpectedValuesInput inputManager) {
		
		detailedQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		knownCells = detailedQueryCube;
		
		//assign probabilities (aka beliefs) to cells. This will probably be done by a file through inputManager later
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			probabilities.add(rd.nextFloat());
		}
        
		//set threshold
		threshold = (float)0.5;
		
		ArrayList<Cell> unknownCells = new ArrayList<Cell>();

		
		for(int i = 0; i < detailedQueryCube.size(); i++) {
			if(probabilities.get(i)<threshold) {
				unknownCells.add(detailedQueryCube.get(i));
				knownCells.remove(i);
			}
		}

		return( unknownCells.size() / ( unknownCells.size() + knownCells.size() ));

	}

}
