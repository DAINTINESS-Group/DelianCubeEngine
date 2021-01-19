package interestingnessengine;

import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class ValueSurprise implements IInterestingnessMeasureWithExpectedValues{

	private double cellSurprise = 0, cubeSurprise = 0,expectedValue = 0;
	private boolean expectedValueChanged = false;
	private double sum, counter;
	
	public ValueSurprise() {}
	/**
	 * Computes the value surprise of the current query as the avg of the cells' value surprises
	 * @param inputManager The current {@link InputManager} object
	 * @return the value surprise value
	 */
	public double computeMeasure(IExpectedValuesInput inputManager) {
		for(Cell c: inputManager.getCurrentQueryResult().getCells()) {
			for(Cell expectedC: inputManager.getExpectedValues()) {
				if(c.getDimensionMembers().equals(expectedC.getDimensionMembers())) {
					expectedValue = Double.valueOf(expectedC.getMeasure());
					expectedValueChanged = true;
					break;
				}
			}
			if(expectedValueChanged) {
				cellSurprise = Math.abs(Double.valueOf(c.getMeasure()) - expectedValue);
				sum += cellSurprise;
				counter += 1;
				expectedValue = 0;
				expectedValueChanged = false;
				cellSurprise = 0;
			}else {
				System.out.println("Expected value not found for " + c.toString(",") + " cell.");
				
			}
			
		}
		if(counter != 0) {
			cubeSurprise = sum / counter;
		}else {
			System.out.println("Expected values not found.");
			return -1; 
		}
		return cubeSurprise;
	}

}
