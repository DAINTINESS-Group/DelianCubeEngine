package assess.labelers;

/**
 * We are using a strategy pattern so that the AssessQueryBuilder can dynamically
 * set the proper labeling scheme.
 */
public interface LabelingScheme {
	/**
	 * This is the main method that labels a whole cube
	 * @param value Currently a single double, in the future it will be a whole
	 *              cube
	 */
	String applyLabels(double value);
}
