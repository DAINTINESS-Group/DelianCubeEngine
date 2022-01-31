package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

import result.Cell;

public class BasicValueSurprise implements IInterestingnessMeasureWithExpectedValues{

	private double cellSurprise = 0;
	private double cubeSurprise = 0;
	private double expectedValue = 0;
	private double surpriseSum = 0;
	private double counter = 0;
	private double min = Integer.MAX_VALUE;
	private double max = Integer.MIN_VALUE;
	
	/** Computes the basic surprise of the current query as the average of
	 * the total surprise of all cells of the query.
	 * @param inputManager The current {@link InputManager} object.
	 * @return the goal basic surprise value, normalized in the range of [0.0 , 1.0].
	 * @author SpyridonKaloudis
	 */
	
	public double computeMeasure(IExpectedValuesInput inputManager) {
		Instant start = Instant.now();
		
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
		
		Instant end = Instant.now();
		long durationAlgorithm = Duration.between(start, end).toMillis();
		
		try {
			String outputTxt = "\n\nBasic Value Surprise \n"+
	    			"\tCompute Algorithm:\t" + durationAlgorithm+ " ms\n";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}

		
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
