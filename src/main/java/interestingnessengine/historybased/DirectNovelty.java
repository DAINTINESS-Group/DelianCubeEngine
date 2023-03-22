package interestingnessengine.historybased;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
		long start = System.nanoTime();
		
		for(CubeQuery q: inputManager.getQueryHistory()) {
			if(q.toString().equals(inputManager.getCurrentQuery().toString())) {
				return 0;
			}
		}
		
		long end = System.nanoTime();
		
		long durationComparison = end - start;
		
		try {
			String outputTxt = "Direct Novelty \n"+
	    			"\tQuery Comparison:\t" + durationComparison+ " ns \n";
		    Files.write(Paths.get("OutputFiles/Interestingness/Experiments/experiments200T.txt"), 
		    		outputTxt.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {}
		
		return 1;
	}
}
