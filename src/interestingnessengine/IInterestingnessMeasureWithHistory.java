package interestingnessengine;
/**
 * @author eiriniMouselli
 */
public interface IInterestingnessMeasureWithHistory {
	/**
	 * Computes the measure
	 * @param inputManager The current {@link InputManager} object
	 * @return the double value of the measurement
	 */
	public double compute(IHistoryInput inputManager);
}
