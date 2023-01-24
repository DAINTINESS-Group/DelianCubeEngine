package assess.labelers;

public class OverlappingLabelingRulesException extends RuntimeException {
	public OverlappingLabelingRulesException(String oldRule, String newRule, String value) {
		super(oldRule + " and " + newRule + " overlap on " + value);
	}
}
