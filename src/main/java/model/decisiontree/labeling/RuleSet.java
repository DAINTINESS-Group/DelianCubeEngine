package model.decisiontree.labeling;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RuleSet implements Serializable{

	/**
	 * 
	 */
	private final String newColumnName;
	private final List<Rule> rules;

	public RuleSet(String newColumnName, List<Rule> rules) {
		this.newColumnName = newColumnName;
		this.rules = rules;
	}

	public String getNewColumnName() {
		return newColumnName;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public String generateSparkSqlExpression() {
		StringBuilder stringBuilder = new StringBuilder("CASE ");
		for (Rule rule : rules) {
			stringBuilder.append(rule.toString());
		}
		stringBuilder.append("END");

		return stringBuilder.toString();
	}
}
