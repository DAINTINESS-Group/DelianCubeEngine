package interestingnessengine;
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
	public double compute(IExpectedValuesInput inputManager);
}
