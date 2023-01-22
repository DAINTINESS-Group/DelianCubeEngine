package assess.labelers;
import java.util.List;

public abstract class AbstractLabelingScheme {

	public AbstractLabelingScheme(List<NumericLabelingRule> rules) {
		; //add as parameter the list of labeling rules
	}
	
	public abstract String labelDeltaValue(double deltaValue);
	
}//end class
