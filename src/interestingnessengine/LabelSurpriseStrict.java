package interestingnessengine;

import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class LabelSurpriseStrict implements IInterestingnessMeasureWithExpectedValues{

	private String expectedLabel;
	/**
	 * Checks whether any cells from the current query have an unexpected label 
	 * @param inputManager The current {@link InputManager} object
	 * @return 0 if all the cells have the expected label and 1 if at least one has a different label
	 */
	public double computeMeasure(IExpectedValuesInput inputManager) {
		for(Cell c: inputManager.getCurrentQueryResult().getCells()) {
			for(Cell expectedC: inputManager.getExpectedLabels()) {
				if(c.getDimensionMembers().equals(expectedC.getDimensionMembers())) {
					expectedLabel = expectedC.getMeasure();
					break;
				}
			}
			if(expectedLabel != null) {
				if(expectedLabel.equals(c.getMeasure()) == false) {
					return 1;
				}
			}
		}
		return 0;
	}
}
