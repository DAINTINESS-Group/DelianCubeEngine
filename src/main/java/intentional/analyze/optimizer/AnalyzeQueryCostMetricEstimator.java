package intentional.analyze.optimizer;

import java.util.ArrayList;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.analyze.AnalyzeQuery;
import intentional.analyze.AnalyzeQuery.TypeOfAnalyzeQuery;
import intentional.analyze.optimizer.selectivityestimation.ReservoirSamplingEstimator;
import intentional.analyze.optimizer.selectivityestimation.SelectivityResult;

/** 
 * Estimates the cost metrics using Selectivity Estimation with Independence Assumption (for now).
 * @author mariosjkb
 *
 */
public class AnalyzeQueryCostMetricEstimator {
	
    private ReservoirSamplingEstimator reservoirSamplingEstimator;
    
    private List<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
    
    private int sampleSize;
        
    private int allEncompassingSelectivity;

	private int sumOfSiblings;

	private int diffOfSiblings;
	

    public AnalyzeQueryCostMetricEstimator(CubeManager cubeManager, List<AnalyzeQuery> analyzeQueries) {
        this.analyzeQueries = analyzeQueries;
        ReservoirSamplingEstimator reservoirSamplingEstimator = new ReservoirSamplingEstimator(cubeManager);
        this.reservoirSamplingEstimator = reservoirSamplingEstimator;
        this.sampleSize = cubeManager.getSampleSize();
        estimateSelectivitiesWithIndependenceAssumption();
    }
    
    
    
    public List<AnalyzeQuery> getAnalyzeQueries() {
		return analyzeQueries;
	}



	private int estimateCubeQuerySelectivityWithIndependenceAssumption(CubeQuery  query) {		
		List<SelectivityResult> selectivityEstimations = reservoirSamplingEstimator.estimate(query);
		double cubeQuerySelectivity = 1.0;
		for(SelectivityResult sr: selectivityEstimations) {
			cubeQuerySelectivity = cubeQuerySelectivity*sr.getSelectivity();
		}
		
        return (int) (cubeQuerySelectivity*sampleSize);
    }
    
    private void estimateSelectivitiesWithIndependenceAssumption() {
    	int sumOfSiblings = 0;
    	int diffOfSiblings = 0;
    	for(AnalyzeQuery aq: analyzeQueries) {
    		if(aq.getType() == TypeOfAnalyzeQuery.UPDATED_SIBLINGS){
    			CubeQuery cq = aq.getAnalyzeCubeQuery();
    			int analyzeQuerySelectivity = estimateCubeQuerySelectivityWithIndependenceAssumption(cq);
    			sumOfSiblings += analyzeQuerySelectivity;
    			if(diffOfSiblings == 0) {
    				diffOfSiblings = analyzeQuerySelectivity;
    			}else {
    				diffOfSiblings -= analyzeQuerySelectivity;
    			}
    		}else if (aq.getType() == TypeOfAnalyzeQuery.SINGLEQUERYOPTIMIZER) {
    			CubeQuery cq = aq.getAnalyzeCubeQuery();
    			int analyzeQuerySelectivity = estimateCubeQuerySelectivityWithIndependenceAssumption(cq);
    			this.allEncompassingSelectivity = analyzeQuerySelectivity;
    		}
    	}
    	this.sumOfSiblings = sumOfSiblings;
    	this.diffOfSiblings = diffOfSiblings;

    }
    
    public double estimateSiblingMegaRatioWithIndependenceAssumption() {
    	double siblingMegaRatio = (double) this.sumOfSiblings/this.allEncompassingSelectivity;
    	System.out.println("Sibling Mega Ratio " + siblingMegaRatio);
    	return siblingMegaRatio;
    }
    
    public double estimateImbalanceCoefficientWithIndependenceAssumption() {
    	double imbalanceCoefficient = (double) this.diffOfSiblings/this.sumOfSiblings;
    	System.out.println("Imbalance Coefficient " + imbalanceCoefficient);
    	return imbalanceCoefficient;
    }
}
