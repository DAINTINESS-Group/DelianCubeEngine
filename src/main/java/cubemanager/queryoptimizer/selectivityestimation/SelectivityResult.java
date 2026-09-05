package cubemanager.queryoptimizer.selectivityestimation;

import java.io.Serializable;
import java.util.List;

/**
 * Holds the result of a selectivity estimation for one sigma predicate.
 * Selectivity = matchingRows / totalRows
 * Needs Serializable because of the RMI
 */
public class SelectivityResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String[] sigmaExpression;

	private final String tableName;

	private final String columnName;

	private final int totalRows;

	private final int matchingRows;

	private final double selectivity;

	public SelectivityResult(String[] sigmaExpression, String tableName, String columnName, int totalRows, int matchingRows) {
		this.sigmaExpression = sigmaExpression;
		this.tableName = tableName;
		this.columnName = columnName;
		this.totalRows = totalRows;
		this.matchingRows = matchingRows;
		if (totalRows <= 0) {
			this.selectivity = 0.0;
		} else {
			this.selectivity = (double) matchingRows / totalRows;
		}
	}

	public String[] getSigmaExpression() {
		return sigmaExpression;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getMatchingRows() {
		return matchingRows;
	}

	public double getSelectivity() {
		return selectivity;
	}

	@Override
	public String toString() {
		return String.format(
				"Sigma Predicate -> [%s %s %s] | Table: %s | Column: %s | Rows: %d / %d | Selectivity: %.4f",
				sigmaExpression[0], sigmaExpression[1], sigmaExpression[2], tableName, columnName,
				matchingRows, totalRows, selectivity);
	}

	/**
	 * Estimates the selectivity of a conjunction of predicates as the product of the individual selectivities,
	 * under the attribute value independence assumption.
	 * @param results the per-predicate results of a single query as returned by an estimator
	 * @return the estimated selectivity of the conjunction, or 0.0 if no results available
	 */
	public double conjunctiveCubeQuerySelectivity(List<SelectivityResult> results) {
		if (results == null || results.isEmpty()) {
			return 0.0;
		}

		double product = 1.0;
		for (SelectivityResult result : results) {
			product *= result.getSelectivity();
		}

		return product;
	}
}
