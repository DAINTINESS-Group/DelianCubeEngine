package interestingnessengine;

import result.Cell;

public class BasicValueSurprise implements IInterestingnessMeasureWithExpectedValues{

	private double cellSurprise = 0;
	private double cubeSurprise = 0;
	private double expectedValue = 0;
	private double surpriseSum = 0;
	private double counter = 0;
	
	public double computeMeasure(IExpectedValuesInput inputManager) {
		for (int i=0; i<inputManager.getCurrentQueryResult().getCells().size();) {
			for (int j=0; j<inputManager.getExpectedValues().size();) {
				Cell c = inputManager.getCurrentQueryResult().getCells().get(i);
				Cell expectedCell = inputManager.getExpectedValues().get(j);
				//If a cell's expected value exists
				if(c.getDimensionMembers().equals(expectedCell.getDimensionMembers())) {
					expectedValue = Double.valueOf(expectedCell.getMeasure());
					//δΜ -> absolute distance
					cellSurprise = Math.abs(Double.valueOf(c.getMeasure()) - expectedValue);
					//fagg/cell -> sum
					surpriseSum += cellSurprise;
					counter++;
					break;
				}
			}
		}
						
		if(counter != 0) {
			//fagg/cube -> avg
			cubeSurprise = surpriseSum / counter;
		}else {
			System.out.println("No expected values found for requested cube.");
			return -1; 
		}
		return cubeSurprise;
	}

}
