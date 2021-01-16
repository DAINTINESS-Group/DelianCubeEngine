package interestingnessengine;

import cubemanager.cubebase.CubeQuery;

public class DirectNovelty implements IInterestingnessMeasureWithHistory {

	public DirectNovelty(){}
	
	
	/**
	 * Computes the direct novelty measure for the current {@link CubeQuery}.
	 * @param The only {@link InputManager} object, from the {@link InterestingnessManager} class.
	 * @return 0 if the same {@link CubeQuery} has been asked before, 1 otherwise.
	 * @author eiriniMouselli
	 */
	public double computeMeasure(IHistoryInput inputManager) {
		
		for(CubeQuery q: inputManager.getQueryHistory()) {
			if(q.toString().equals(inputManager.getCurrentQuery().toString())) {
				return 0;
			}
		}
		return 1;
	}
}
