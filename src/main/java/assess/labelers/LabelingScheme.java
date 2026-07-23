package assess.labelers;

import intentional.result.LabelDomain;

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

	/**
	 * The domain of labels this scheme can assign. Defines an ordinal domain so
	 * labeled results can be compared (e.g. by an enumerated score).
	 */
	LabelDomain domain();
}
