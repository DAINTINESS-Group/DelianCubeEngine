package intentional.analyze;

import java.util.ArrayList;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.statistics.selectivity.FilterEstimator;
import cubemanager.statistics.selectivity.SelectivityResult;
import intentional.analyze.AnalyzeQuery.TypeOfAnalyzeQuery;
import intentional.operator.IntentionalStrategy;

/** 
 * Estimates the cost metrics using Selectivity Estimation with Independence Assumption (for now).
 * @author mariosjkb
 *
 */
public class AnalyzeOperatorOptimizer {
	public enum AnalyzeStrategy{
		MIN_MQO,
		MID_MQO,
		MAX_MQO;
	}
	
    private FilterEstimator filterEstimator;
    
    private List<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
    
    private int sampleSize;
        
    private double allEncompassingSelectivity;

	private double sumOfSiblings;

	private double diffOfSiblings;
	

    public AnalyzeOperatorOptimizer(CubeManager cubeManager, List<AnalyzeQuery> analyzeQueries) {
        this.analyzeQueries = analyzeQueries;
        FilterEstimator filterEstimator = new FilterEstimator(cubeManager);
        this.filterEstimator = filterEstimator;
        this.sampleSize = cubeManager.getSampleSize();
        estimateCostMetricsWithIndependenceAssumption();
    }
    
    
    
    public List<AnalyzeQuery> getAnalyzeQueries() {
		return analyzeQueries;
	}



	private double estimateCubeQuerySelectivityWithIndependenceAssumption(CubeQuery  query) {		
		List<SelectivityResult> selectivityEstimations = filterEstimator.estimate(query);
		double cubeQuerySelectivity = 1.0;
		for(SelectivityResult sr: selectivityEstimations) {
			cubeQuerySelectivity = cubeQuerySelectivity*sr.getSelectivity();
		}
		
        return cubeQuerySelectivity*sampleSize;
    }
    
    private void estimateCostMetricsWithIndependenceAssumption() {
    	double sumOfSiblings = 0;
    	double diffOfSiblings = 0;
    	for(AnalyzeQuery aq: analyzeQueries) {
    		if(aq.getType() == TypeOfAnalyzeQuery.UPDATED_SIBLINGS){
    			CubeQuery cq = aq.getAnalyzeCubeQuery();
    			double analyzeQuerySelectivity = estimateCubeQuerySelectivityWithIndependenceAssumption(cq);
    			System.out.println(analyzeQuerySelectivity/sampleSize);
    			sumOfSiblings += analyzeQuerySelectivity;
    			if(diffOfSiblings == 0) {
    				diffOfSiblings = analyzeQuerySelectivity;
    			}else {
    				diffOfSiblings -= analyzeQuerySelectivity;
    			}
    		}else if (aq.getType() == TypeOfAnalyzeQuery.SINGLEQUERYOPTIMIZER) {
    			CubeQuery cq = aq.getAnalyzeCubeQuery();
    			double analyzeQuerySelectivity = estimateCubeQuerySelectivityWithIndependenceAssumption(cq);
    			System.out.println("All-encompasing " + analyzeQuerySelectivity/sampleSize);
    			this.allEncompassingSelectivity = analyzeQuerySelectivity;
    		}
    	}
    	this.sumOfSiblings = sumOfSiblings;
    	this.diffOfSiblings = Math.abs(diffOfSiblings);

    }
    
    public double estimateSiblingMegaRatio() {
    	double siblingMegaRatio = (double) this.sumOfSiblings/this.allEncompassingSelectivity;
    	System.out.println("Sibling Mega Ratio " + siblingMegaRatio);
    	return siblingMegaRatio;
    }
    
    public double estimateImbalanceCoefficient() {
    	double imbalanceCoefficient = (double) this.diffOfSiblings/this.sumOfSiblings;
    	System.out.println("Imbalance Coefficient " + imbalanceCoefficient);
    	return imbalanceCoefficient;
    }
    
    
    
    public IntentionalStrategy decideMQOAlgorithmWithIndependenceAssumption() {
		double siblingMegaRatioEstimation = estimateSiblingMegaRatio();
		if(siblingMegaRatioEstimation <= 0.4) {
			return IntentionalStrategy.MID_MQO;
		}else{
			double imbalanceCoefficient = estimateImbalanceCoefficient();
			if(imbalanceCoefficient <= 0.454545) {
				return IntentionalStrategy.MAX_MQO;
			}
		}
		return IntentionalStrategy.MID_MQO;
	}
}
