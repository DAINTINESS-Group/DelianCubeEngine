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
public class LabelSurpriseStrict implements IInterestingnessMeasureWithExpectedValues{

	private String expectedLabel;
	/**
	 * Checks whether any cells from the current query have an unexpected label 
	 * @param inputManager The current {@link InputManager} object
	 * @return 0 if all the cells have the expected label and 1 if at least one has a different label
	 */
	public double computeMeasure(IExpectedValuesInput inputManager) {
		Instant start = Instant.now();
		
		for(Cell c: inputManager.getCurrentQueryResult().getCells()) {
			for(Cell expectedC: inputManager.getExpectedLabels()) {
				if(c.getDimensionMembers().equals(expectedC.getDimensionMembers())) {
					expectedLabel = expectedC.getMeasure();
					break;
				}
			}
			if(expectedLabel != null) {
				if(expectedLabel.equals(c.getMeasure()) == false) {
					Instant end = Instant.now();
					long durationComparison = Duration.between(start, end).toMillis();
					
					try {
						String outputTxt = "\n\nLabel Surprise Strict\n"+
				    			"\tQuery Comparison:\t" + durationComparison+ " ms\n";
					    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
					    		outputTxt.getBytes(), StandardOpenOption.APPEND);
					}catch (IOException e) {}
					return 1;
				}
			}
		}
		
		Instant end = Instant.now();
		
		long durationComparison = Duration.between(start, end).toMillis();
		
		try {
			String outputTxt = "\n\nLabel Surprise Strict\n"+
	    			"\tQuery Comparison:\t" + durationComparison+ " ms\n";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		
		return 0;
	}
}
