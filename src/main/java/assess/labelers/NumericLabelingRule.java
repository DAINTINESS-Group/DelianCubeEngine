package assess.labelers;


public class NumericLabelingRule {
	
	public enum EdgeCondition{LESS, LESSEQUAL, GREATER, GREATEREQUAL};

	private double lowLimit;
	private double highLimit;
	private EdgeCondition lowCondition;
	private EdgeCondition highCondition;
	private String label;
	
	public NumericLabelingRule(double lowLimit, double highLimit, EdgeCondition lowCondition,  EdgeCondition highCondition,  String label) {
		super();
		this.lowLimit = lowLimit;
		this.highLimit = highLimit;
		this.lowCondition = lowCondition;
		this.highCondition = highCondition;
		this.label = label;
	}

	public NumericLabelingRule(String[] limits) {
		;
		//TODO to make it from strings rather than double and enum
	}
	
	
	private Boolean compareLow(double testedValue) { 
		if (lowCondition == EdgeCondition.GREATER) {
			if (testedValue > lowLimit)
				return true;
		}
		if (lowCondition == EdgeCondition.GREATEREQUAL) {
			if (testedValue >= lowLimit)
				return true;
		}	
		return false;
	}
	
	private Boolean compareHigh(double testedValue) { 
		if (highCondition == EdgeCondition.LESS) {
			if (testedValue < highLimit)
				return true;
		}
		if (highCondition == EdgeCondition.LESSEQUAL) {
			if (testedValue <= highLimit)
				return true;
		}
		
		return false;
	}
	
	
	public Boolean testValue(double testedValue) {
		if(compareLow(testedValue) && compareHigh(testedValue))
			return true;
		return false;
	}
	
	public String getLabel() { return label;}
	
}
