package interestingnessengine.historybased;

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
			case "Partial Detailed Extensional Relevance" : measure = new PartialDetailedExtensionalRelevance();
									break;
			case "Partial Detailed Extensional Jaccard Based Peculiarity" : measure = new PartialDetailedExtensionalJaccardBasedPeculiarity();
									break;
			case "Partial Detailed Extensional Novelty" : measure = new PartialDetailedExtensionalNovelty();
									break;
			
			//case "Goal Based Relevance" : measure = new GoalBasedIntentionalRelevance();
				//					break;
			case "Partial Syntactic Average Peculiarity" : measure = new PartialSyntacticAveragePeculiarity();
									break;
		}
		if(measure == null) {
			System.err.println("Did not manage to create the desired measure. Exiting");
			System.exit(-1);
		}
		return measure;
		
	}
}

