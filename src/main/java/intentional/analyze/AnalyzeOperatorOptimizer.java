package intentional.analyze;

import java.util.List;
import cubemanager.CubeManager;
import intentional.analyze.optimizer.AnalyzeOperatorOptimizerQueryGenerator;
import intentional.analyze.optimizer.AnalyzeQueryCostMetricEstimator;

/**
 * A class that uses Sibling Mega Ratio and Imbalance Coefficient estimations to decide which
 * ANALYZE MQO Strategy to apply in order to achieve optimal performance.
 * @author mariosjkb
 *
 */
public class AnalyzeOperatorOptimizer{
	public enum AnalyzeStrategy{
		MIN_MQO,
		MID_MQO,
		MAX_MQO;
	}
	
	private CubeManager cubeManager;
	
	private AnalyzeQueryCostMetricEstimator analyzeQueryCostMetricEstimator;
	
	private List<AnalyzeQuery> analyzeQueriesForEstimation;
			
	private AnalyzeOperatorOptimizerQueryGenerator analyzeOperatorOptimizerQueryGenerator;
	
	public AnalyzeOperatorOptimizer(CubeManager cubeManager, AnalyzeOperatorOptimizerQueryGenerator analyzeOperatorOptimizerQueryGenerator) {
		this.cubeManager = cubeManager;
		this.analyzeOperatorOptimizerQueryGenerator = analyzeOperatorOptimizerQueryGenerator;
		constructAnalyzeQueries();
		setSelectivityEstimatorOptimizer();
	}
	
	private boolean constructAnalyzeQueries() {
		boolean incomingExpressionIsValid;

		incomingExpressionIsValid = this.analyzeOperatorOptimizerQueryGenerator.validateIncomingExpression();
		if(incomingExpressionIsValid) {
			analyzeQueriesForEstimation = analyzeOperatorOptimizerQueryGenerator.translateToAnalyzeQueries();
			return true;
		}else {
			System.err.println("ANALYZE incoming expression contains syntax errors!Please check.");
			return false;
		}
	}
	
	private void setSelectivityEstimatorOptimizer() {
		this.analyzeQueryCostMetricEstimator = new AnalyzeQueryCostMetricEstimator(cubeManager, analyzeQueriesForEstimation);
	}
	
	public AnalyzeQueryCostMetricEstimator getSelectivityEstimationOptimizer() {
		return analyzeQueryCostMetricEstimator;
	}
	
	public List<AnalyzeQuery> getAnalyzeQueriesForEstimation(){
		return analyzeQueriesForEstimation;
	}

	public double estimateSiblingMegaRatio() {
		double siblingMegaRatio = analyzeQueryCostMetricEstimator.estimateSiblingMegaRatioWithIndependenceAssumption();
		return siblingMegaRatio;
	}
	
	public double estimateImbalanceCoefficient() {
		double imbalanceCoefficient = analyzeQueryCostMetricEstimator.estimateImbalanceCoefficientWithIndependenceAssumption();
		return imbalanceCoefficient;
	}
	
	public AnalyzeStrategy decideMQOAlgorithmWithIndependenceAssumption() {
		double siblingMegaRatioEstimation = estimateSiblingMegaRatio();
		if(siblingMegaRatioEstimation <= 0.4) {
			return AnalyzeStrategy.MID_MQO;
		}else{
			double imbalanceCoefficient = estimateImbalanceCoefficient();
			if(imbalanceCoefficient <= 0.454545) {
				return AnalyzeStrategy.MAX_MQO;
			}
		}
		return AnalyzeStrategy.MID_MQO;
	}

}
