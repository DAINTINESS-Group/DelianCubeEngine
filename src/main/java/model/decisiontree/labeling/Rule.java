package model.decisiontree.labeling;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Rule implements Serializable {
	
	private String targetColumnName;
	private String sparkOperator;
	private Number limit;
	private String label;

	public Rule(String targetColumnName, String sparkOperator, Number limit, String label) {
		this.targetColumnName = targetColumnName;
		this.sparkOperator = sparkOperator;
		this.limit = limit;
		this.label = label;
	}

	public String getTargetColumnName() { return targetColumnName; }

	@Override
	public String toString() {
		return String.format(
				"WHEN %s %s '%s' THEN '%s' ", targetColumnName, sparkOperator, limit, label);
	}
}
