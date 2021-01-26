package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

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
		Instant start = Instant.now();
		
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

		Instant end = Instant.now();
		
		long durationComparison = Duration.between(start, end).toMillis();
		
		try {
			String outputTxt = "\n\nValue Surprise \n"+
	    			"\tQuery Comparison:\t" + durationComparison+ " ms\n";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
				
		if(counter != 0) {
			cubeSurprise = sum / counter;
		}else {
			System.out.println("Expected values not found.");
			return -1; 
		}
		return cubeSurprise;
	}

}
