package interestingnessengine;

import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class LabelSurprise implements IInterestingnessMeasureWithExpectedValues{

	private String expectedLabel;
	private double sum, counter, cubeSurprise;
	/**
	 * Computes the label surprise for the current query as the pct of the cube's cells that had different 
	 * label than the expected one.
	 * @param inputManager The current {@link InputManager} object
	 * @return the label surprise value or -1 if the expected labels were not found
	 */
	public double compute(IExpectedValuesInput inputManager) {
		for(Cell c: inputManager.getCurrentQueryResult().getCells()) {
			for(Cell expectedC: inputManager.getExpectedLabels()) {
				if(c.getDimensionMembers().equals(expectedC.getDimensionMembers())) {
					expectedLabel = expectedC.getMeasure();
					break;
				}
			}
			if(expectedLabel != null) {
				
				if(expectedLabel.equals(c.getMeasure()) == false) {
					sum += 1;
				}
				counter += 1;
				expectedLabel = null;
			}
		}
		if(counter != 0) {
			cubeSurprise = sum / counter;
		}else {
			System.out.println("Expected labels not found.");
			return -1; 
		}
		return cubeSurprise;
	}

}
