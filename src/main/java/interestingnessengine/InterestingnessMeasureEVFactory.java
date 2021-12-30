package interestingnessengine;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class InterestingnessMeasureEVFactory {

	private IInterestingnessMeasureWithExpectedValues measure = null;
	
	public IInterestingnessMeasureWithExpectedValues createInterestingnessMeasureWithExpectedValues(String measureName) {
		switch(measureName) {
		case "Label Surprise" : measure = new LabelSurprise();
								break;
		case "Label Surprise Strict" : measure = new LabelSurpriseStrict();
								break;
		case "Value Surprise" : measure = new ValueSurprise();
								break;
		case "Basic Value Surprise" : measure = new BasicValueSurprise();
								break;
		}
		if(measure == null) {
			System.err.println("Did not manage to create the desired measure. Exiting");
			System.exit(-1);
		}
		return measure;
	}
}
