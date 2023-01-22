package assess.labelers;

import java.util.List;

public class NumericLabelingScheme extends AbstractLabelingScheme {
	List<NumericLabelingRule> rules;
	
	public NumericLabelingScheme(List<NumericLabelingRule> rules) {
		super(rules);
		this.rules = rules;
	}

	@Override
	public String labelDeltaValue(double deltaValue) {
		// TODO Auto-generated method stub
		return null;
	}

}
