package interestingnessengine;

import java.util.ArrayList;
import result.Cell;
/**
 * 
 * @author eiriniMouselli
 *
 */
public interface IExpectedValuesInput extends IInput{
	/**
	 * Goes through the given file, creates the {@link Cell}s with the expected values it contains 
	 * and populates the appropriate list.
	 * @param filePath The path to the file containing the user's value predictions
	 */
	public void parseExpectedValues(String filePath);
	/**
	 * @return a list containing the user's value predictions.
	 */
	public ArrayList<Cell> getExpectedValues();
	/**
	 * Goes through the given file, creates the {@link Cell}s with the expected labels it contains 
	 * and populates the appropriate list.
	 * @param filePath The path to the file containing the user's label predictions
	 */
	public void parseExpectedLabels(String folderPath);
	/**
	 * @return a list containing the user's label predictions.
	 */
	public ArrayList<Cell> getExpectedLabels(); 
	
}
