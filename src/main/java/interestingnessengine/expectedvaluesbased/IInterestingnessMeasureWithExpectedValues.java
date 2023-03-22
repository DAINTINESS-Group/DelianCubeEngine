package interestingnessengine.expectedvaluesbased;


/**
 * 
 * @author eiriniMouselli
 *
 */
public interface IInterestingnessMeasureWithExpectedValues {
	/**
	 * Computes the measure
	 * @param inputManager The current {@link InputManager} object
	 * @return the double value of the measurement
	 */
	public double computeMeasure(IExpectedValuesInput inputManager);
}
