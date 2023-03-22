package interestingnessengine.historybased;


/**
 * @author eiriniMouselli
 */
public interface IInterestingnessMeasureWithHistory {
	/**
	 * Computes the measure
	 * @param inputManager The current {@link InputManager} object
	 * @return the double value of the measurement
	 */
	public double computeMeasure(IHistoryInput inputManager);
}
