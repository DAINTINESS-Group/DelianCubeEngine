package interestingnessengine;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import interestingnessengine.expectedvaluesbased.IInterestingnessMeasureWithExpectedValues;
import interestingnessengine.expectedvaluesbased.InterestingnessMeasureEVFactory;
import interestingnessengine.historybased.IInterestingnessMeasureWithHistory;
import interestingnessengine.historybased.InterestingnessMeasureHFactory;
import result.Result;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class InterestingnessManager {

	private InterestingnessMeasureEVFactory evMeasureFactory;
	private InterestingnessMeasureHFactory hMeauseFactory;
	private IInterestingnessMeasureWithExpectedValues evMeasure;
	private IInterestingnessMeasureWithHistory hMeasure;
	private InputManager inputManager;
	private ArrayList<String> historyMeasures = new ArrayList<String>(Arrays.asList("Direct Novelty", 
			"Indirect Novelty", "Partial Detailed Extensional Relevance", "Partial Detailed Extensional Jaccard Based Peculiarity", "Partial Detailed Extensional Novelty", "Partial Syntactic Average Peculiarity"));
	
	/**
	 * Constructor to be used when there is no history stored and no expected values/labels.
	 * @param cubeMng The current {@link CubeManager} object, to be passed to {@link InputManager} constructor
	 * @param k       The kth neighbor, to be passed to {@link InputManager} constructor
	 */
	public InterestingnessManager(CubeManager cubeMng, int k) {
		this.inputManager = new InputManager(cubeMng, k);
		this.evMeasureFactory = new InterestingnessMeasureEVFactory();
		this.hMeauseFactory = new InterestingnessMeasureHFactory();
	}
	/**
	 * Constructor to be used when there is history stored and/or expected values/labels.
	 * @param historyFolder   Path to the History folder, to be passed to {@link InputManager} constructor
	 * @param expValuesFolder Path to the Expected Values folder, to be passed to {@link InputManager} constructor
	 * @param expLabelsFolder Path to the Expected Labels folder,to be passed to {@link InputManager} constructor
	 * @param cubeMng         The current {@link CubeManager} object, to be passed to {@link InputManager} constructor
	 * @param k               The kth neighbor, to be passed to {@link InputManager} constructor
	 * @throws RemoteException
	 */
	public InterestingnessManager(String historyFolder, String expValuesFolder, String expLabelsFolder, String beliefFolder, CubeManager cubeMng, int k) throws RemoteException{
		
		this.inputManager = new InputManager(historyFolder, expValuesFolder, expLabelsFolder, beliefFolder, cubeMng, k);
		this.evMeasureFactory = new InterestingnessMeasureEVFactory();
		this.hMeauseFactory = new InterestingnessMeasureHFactory();
	}
	/**
	 * Updates {@link InterestingnessManager} state
	 * <p>
	 * Saves previous {@link CubeQuery} string and {@link Result} in history and updates current {@link CubeQuery} 
	 * and current {@link Result}
	 * @param currentQuery  The current {@link CubeQuery} 
	 * @param currentResult The current {@link Result}
	 */
	public void updateState(CubeQuery currentQuery, Result currentResult) {
		
		if(inputManager.getCurrentQuery() != null) {
			inputManager.updateQueryHistory(inputManager.getCurrentQuery());
			inputManager.updateQueryHistoryResults(inputManager.getCurrentQueryResult());
		}
		inputManager.setCurrentQuery(currentQuery);
		inputManager.setCurrentQueryResult(currentResult);
	}
	/**
	 * Creates the chosen interestingness measure object and computes it.
	 * @param chosenMeasure The name of the chosen measure
	 * @param currentQuery  The current {@link CubeQuery} 
	 * @param currentResult The current {@link Result}
	 * @return the result of the measurement. 
	 */
	public double computeMeasure(String chosenMeasure, CubeQuery currentQuery, Result currentResult) {
		double result;
		
		if(this.historyMeasures.contains(chosenMeasure)) {
			this.hMeasure = this.hMeauseFactory.createInterestingnessMeasureWithHistory(chosenMeasure);
			result = this.hMeasure.computeMeasure(inputManager);
		}
		else {
			this.evMeasure = this.evMeasureFactory.createInterestingnessMeasureWithExpectedValues(chosenMeasure);
			result = this.evMeasure.computeMeasure(inputManager);
		}
		return result;
	}
	
	public String getHelpingQuery(String queryString, String queryString1) {
		FamilyBasedRelevance family = new FamilyBasedRelevance();
		String helpingQuery = family.getHelpingQuery(queryString, queryString1, inputManager);
		return helpingQuery;
	}
	
	public double computeMeasure(String chosenMeasure, CubeQuery currentQuery, Result currentResult, String helpingQueryResult) {
		double result = -10.0;
		//FamilyBasedRelevance doesn't require History and doesn't depend on Expected Values. So I check for it individually
		if(chosenMeasure.equals("FamilyBasedRelevance")){
			FamilyBasedRelevance family = new FamilyBasedRelevance();
			result = family.computeMeasure(chosenMeasure, chosenMeasure, inputManager, helpingQueryResult);
			return result;
		}
		
		return result;
	}

}
