package intentional.labeling;

import java.util.Collection;

/**
 * A labeling policy: values in, labels out, over a fixed {@link #domain()}. Operator-neutral — a model
 * feeds it whichever per-cell quantity it labels (a raw measure, a delta against a benchmark).
 */
public interface LabelingScheme {
	/**
	 * Identifies the scheme: the name a query's USING clause selects it by, and the name a labeling
	 * it produced is reported under.
	 */
	String name();

	/**
	 * Sees the full set of values before labeling begins. Data-driven schemes derive their
	 * boundaries from the distribution here; rule-based schemes need nothing and ignore it.
	 */
	default void fit(Collection<Double> values) {}

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
