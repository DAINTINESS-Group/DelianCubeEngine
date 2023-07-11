package interestingnessengine.expectedvaluesbased;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class LabelSurpriseStrict implements IInterestingnessMeasureWithExpectedValues{

	private String expectedLabel;
	private boolean expectedLabelFound;
	/**
	 * Checks whether any cells from the current query have an unexpected label 
	 * @param inputManager The current {@link InputManager} object
	 * @return 0 if all the cells have the expected label and 1 if at least one has a different label
	 */
	public double computeMeasure(IExpectedValuesInput inputManager) {
		long start = System.nanoTime();
		expectedLabelFound = false;
		
		for(Cell c: inputManager.getCurrentQueryResult().getCells()) {
			for(Cell expectedC: inputManager.getExpectedLabels()) {
				if(c.getDimensionMembers().equals(expectedC.getDimensionMembers())) {
					expectedLabel = expectedC.getMeasure();
					break;
				}
			}
			if(expectedLabel != null) {
				expectedLabelFound = true;
				if(expectedLabel.equals(c.getMeasure()) == false) {
					long end = System.nanoTime();
					long durationComparison = end - start;
					
					try {
						String outputTxt = "\n\nLabel Surprise Strict\n"+
				    			"\tQuery Comparison:\t" + durationComparison+ " ns\n";
					    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
					    		outputTxt.getBytes(), StandardOpenOption.APPEND);
					}catch (IOException e) {}
					return 1;
				}
			}
		}
		
		long end = System.nanoTime();
		
		long durationComparison = end - start;
		
		try {
			String outputTxt = "\n\nLabel Surprise Strict\n"+
	    			"\tQuery Comparison:\t" + durationComparison+ " ns\n";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		if(expectedLabelFound == true) {
			return 0;
		} else {
			return -1;
		}
		
	}
}
