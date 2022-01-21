package interestingnessengine;

import result.Cell;

public class BasicValueSurprise implements IInterestingnessMeasureWithExpectedValues{

	private double cellSurprise = 0;
	private double cubeSurprise = 0;
	private double expectedValue = 0;
	private double surpriseSum = 0;
	private double counter = 0;
	
	private double min = Integer.MAX_VALUE;
	private double max = Integer.MIN_VALUE;
	
	public double computeMeasure(IExpectedValuesInput inputManager) {
		for(Cell c: inputManager.getCurrentQueryResult().getCells()) {
			for (Cell expectedCell: inputManager.getExpectedValues()) {
					if(c.getDimensionMembers().toString().equals(expectedCell.getDimensionMembers().toString())) {
					expectedValue = Double.valueOf(expectedCell.getMeasure());
					//δΜ -> absolute distance
					cellSurprise = Math.abs(Double.valueOf(c.getMeasure()) - expectedValue);
					if(cellSurprise<min) {
						min = cellSurprise;
					}
					if(cellSurprise>max) {
						max = cellSurprise;
					}
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
			//normalize result between 0.0 and 1.0
			cubeSurprise = (cubeSurprise-min)/(max-min);
		}else {
			System.out.println("No expected values found for requested cube.");
			return -1; 
		}
		return cubeSurprise;
	}
}
