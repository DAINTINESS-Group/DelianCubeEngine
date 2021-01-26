package interestingnessengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

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
		Instant start = Instant.now();
		
		for(CubeQuery q: inputManager.getQueryHistory()) {
			if(q.toString().equals(inputManager.getCurrentQuery().toString())) {
				return 0;
			}
		}
		
		Instant end = Instant.now();
		
		long durationComparison = Duration.between(start, end).toMillis();
		
		try {
			String outputTxt = "Direct Novelty \n"+
	    			"\tQuery Comparison:\t" + durationComparison+ " ms";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		return 1;
	}
}
