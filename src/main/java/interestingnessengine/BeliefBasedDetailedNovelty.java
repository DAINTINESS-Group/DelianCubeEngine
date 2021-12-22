package interestingnessengine;

import java.util.ArrayList;
import java.util.Random;
import result.Cell;

public class BeliefBasedDetailedNovelty implements IInterestingnessMeasureWithExpectedValues{

	private ArrayList<Cell> detailedCurrentQueryCube;
	private ArrayList<Cell> knownCells;
	private ArrayList<Float> probabilities;
	private float threshold;

	
	/**
	 * PV Comment: ti 'n' tot' wre?
	 * Giati to len' "rd" kai giati pairnei mesa o,ti na'nai? oeo?
	 */
	private Random rd = new Random();

	@Override
	public double computeMeasure(IExpectedValuesInput inputManager) {
		
		detailedCurrentQueryCube = inputManager.computeDetailedQueryCube(inputManager.getCurrentQuery());
		
		/**
		 * PV Comm: similarly with PartialDet.., you have removals afterwards, but maybe here you don't care?
		 * Maybe better to do the explicit copy to knownCells anyway?
		 */
		knownCells = detailedCurrentQueryCube;
		
		//assign probabilities (aka beliefs) to cells. This will probably be done by a file through inputManager later
		for(int i = 0; i < detailedCurrentQueryCube.size(); i++) {
			probabilities.add(rd.nextFloat());
		}
        
		//set threshold
		threshold = (float)0.5;
		
		/**
		 * PV Comment: again, unknown cells is probably useless, as only it's size is needed,
		 * which is size()-known.size(), but anyway, we can keep it
		 */
		ArrayList<Cell> unknownCells = new ArrayList<Cell>();
		for(int i = 0; i < detailedCurrentQueryCube.size(); i++) {
			if(probabilities.get(i)<threshold) {
				unknownCells.add(detailedCurrentQueryCube.get(i));
				knownCells.remove(i);
			}
		}

		return( unknownCells.size() / ( unknownCells.size() + knownCells.size() ));

	}

}
