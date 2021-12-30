package interestingnessengine;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class InterestingnessMeasureHFactory {

	private IInterestingnessMeasureWithHistory measure = null;
	public IInterestingnessMeasureWithHistory createInterestingnessMeasureWithHistory(String measureName) {
		switch(measureName) {
			case "Direct Novelty" : measure = new DirectNovelty();
									break;
			case "Indirect Novelty" : measure = new IndirectNovelty();
									break;
			case "Relevance with DAI" : measure = new RelevanceWithDAI();
									break;
			case "Value Peculiarity" : measure = new ValuePeculiarity();
									break;
			case "Partial Detailed Novelty" : measure = new PartialDetailedNovelty();
									break;
			case "Belief Based Novelty" : measure = new BeliefBasedDetailedNovelty();
									break;		
			case "Goal Based Relevance" : measure = new GoalBasedIntentionalRelevance();
									break;		
		}
		if(measure == null) {
			System.err.println("Did not manage to create the desired measure. Exiting");
			System.exit(-1);
		}
		return measure;
		
	}
}

