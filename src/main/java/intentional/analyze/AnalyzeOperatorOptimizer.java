package intentional.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.statistics.selectivity.FilterEstimator;
import cubemanager.statistics.selectivity.SelectivityResult;
import intentional.analyze.AnalyzeQuery.TypeOfAnalyzeQuery;
import intentional.operator.IntentionalStrategy;

/** 
 * Estimates the cost metrics using Selectivity Estimation with Attribute Value Independence.
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
        
    private double allEncompassingNormalizedSelectivity;
    
    private double originalFacilitatorQueryNormalizedSelectivity;
    
    private HashMap<String,Double> siblingFacilitatorQueriesNormalizedSelectivities = new HashMap<String,Double>();

    private double normalizedQOrgSelectivity;
    
	private double normalizedQSib1Selectivity;
	
	private double normalizedQSib2Selectivity;
	
	private double normalizedQAllSelectivity;
	
	private double normalizedSiblingSum;
	
	private double normalizedSiblingImbalance;
	
	private double normalizedMaxSib;
	
	private double normalizedMinSib;
	
	private double normalizedUselessSpace;
	
	private double normalizedUselessSpaceIncludingQOrg;
	
	private double siblingMegaRatio;
	
	private double siblingImbalanceCoefficient;
	
	private boolean encounteredSibling = false;
	
	private StringBuilder diagnosticString = new StringBuilder();;

	private double uselessSpaceWithOrg;
	

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
    
    
	public HashMap<String, Double> getSiblingFacilitatorQueriesNormalizedSelectivities() {
		return siblingFacilitatorQueriesNormalizedSelectivities;
	}

	public double getNormalizedQOrgSelectivity() {
		return normalizedQOrgSelectivity;
	}

	public double getNormalizedQSib1Selectivity() {
		return normalizedQSib1Selectivity;
	}

	public double getNormalizedQSib2Selectivity() {
		return normalizedQSib2Selectivity;
	}

	public double getNormalizedQAllSelectivity() {
		return normalizedQAllSelectivity;
	}

	public double getNormalizedSiblingSum() {
		return normalizedSiblingSum;
	}

	public double getNormalizedSiblingImbalance() {
		return normalizedSiblingImbalance;
	}

	public double getNormalizedMaxSib() {
		return normalizedMaxSib;
	}

	public double getNormalizedMinSib() {
		return normalizedMinSib;
	}

	public double getNormalizedUselessSpace() {
		return normalizedUselessSpace;
	}

	public double getNormalizedUselessSpaceIncludingQOrg() {
		return normalizedUselessSpaceIncludingQOrg;
	}

	public double getSiblingMegaRatio() {
		return siblingMegaRatio;
	}

	public double getSiblingImbalanceCoefficient() {
		return siblingImbalanceCoefficient;
	}

	public double getUselessSpaceWithOrg() {
		return uselessSpaceWithOrg;
	}

	/**
	 * Apply Attribute Value Independence
	 * @param query
	 * @return Estimated Cube Query Selectivity using AVI
	 */
	private double estimateCubeQueryNormalizedSelectivityWithIndependenceAssumption(CubeQuery  query) {		
		List<SelectivityResult> selectivityEstimations = filterEstimator.estimate(query);
		double cubeQuerySelectivity = 1.0;
		for(SelectivityResult sr: selectivityEstimations) {
			cubeQuerySelectivity = cubeQuerySelectivity*sr.getSelectivity();
		}
        return cubeQuerySelectivity;
    }
	
	/**
	 * Estimate the selectivity of each facilitator query using Attribute Value Independence
	 */
	private void estimateFacilitatorQueriesNormalizedSelectivity() {
		for(AnalyzeQuery aq: analyzeQueries) {
    		CubeQuery cq = aq.getAnalyzeCubeQuery();
    		System.out.println(cq);
			double analyzeFacilitatorQueryNormalizedSelectivity = estimateCubeQueryNormalizedSelectivityWithIndependenceAssumption(cq);
			switch(aq.getType()) {
			case UPDATED_SIBLINGS:
				if(!encounteredSibling) {
					siblingFacilitatorQueriesNormalizedSelectivities.put("Qsib1", analyzeFacilitatorQueryNormalizedSelectivity);
					encounteredSibling = true;
				}else {
					siblingFacilitatorQueriesNormalizedSelectivities.put("Qsib2", analyzeFacilitatorQueryNormalizedSelectivity);
					encounteredSibling = false;
				}
				break;
				
			case SINGLEQUERYOPTIMIZER:
				this.allEncompassingNormalizedSelectivity = analyzeFacilitatorQueryNormalizedSelectivity;
				break;
					
			case Base:
				this.originalFacilitatorQueryNormalizedSelectivity = analyzeFacilitatorQueryNormalizedSelectivity;
				break;
			default:
				break;		
			}
    	}
	}
    
	/**
	 * Estimate cost metrics
	 */
    private void estimateCostMetricsWithIndependenceAssumption() {
    	
    	
    	// CALCULATE FACILITATOR SELECTIVITIES
    	estimateFacilitatorQueriesNormalizedSelectivity();
    	normalizedQOrgSelectivity = originalFacilitatorQueryNormalizedSelectivity;
    	normalizedQSib1Selectivity = siblingFacilitatorQueriesNormalizedSelectivities.get("Qsib1");
    	normalizedQSib2Selectivity = siblingFacilitatorQueriesNormalizedSelectivities.get("Qsib2");
    	normalizedQAllSelectivity = allEncompassingNormalizedSelectivity;
    	
    	// CALCULATE COST METRICS
    	normalizedSiblingSum = normalizedQSib1Selectivity + normalizedQSib2Selectivity;
    	normalizedSiblingImbalance = Math.abs(normalizedQSib1Selectivity - normalizedQSib2Selectivity);
    	normalizedMaxSib = Math.max(normalizedQSib1Selectivity, normalizedQSib2Selectivity);
    	normalizedMinSib = Math.min(normalizedQSib1Selectivity, normalizedQSib2Selectivity);
    	normalizedUselessSpace = normalizedQAllSelectivity - normalizedSiblingSum;
    	normalizedUselessSpaceIncludingQOrg = normalizedQAllSelectivity - normalizedSiblingSum + normalizedQOrgSelectivity; 
    	siblingMegaRatio = normalizedSiblingSum/normalizedQAllSelectivity;
    	siblingImbalanceCoefficient = normalizedSiblingImbalance/normalizedSiblingSum;
    	
    	// FILL UP DIAGNOSTIC STRING
    	diagnosticString.append("$$Normalized |Qorg| with IA: " + normalizedQOrgSelectivity + "\n" +
    	"@@Normalized |Qsib1| with IA: " + normalizedQSib1Selectivity + "\n" +
    	"##Normalized |Qsib2| with IA: " + normalizedQSib2Selectivity + "\n" +
    	"%%Normalized |Qall| with IA: " + normalizedQAllSelectivity + "\n" +
    	"^^Normalized SiblingSum with IA: " + normalizedSiblingSum + "\n" +
    	"&&Normalized Sibling Imbalance with IA: " + normalizedSiblingImbalance + "\n" +
    	"**Normalized |maxSib| with IA: " + normalizedMaxSib + "\n" +
    	"\\Normalized |minSib| with IA: " + normalizedMinSib + "\n" +
    	"<<Useless space with IA: " + normalizedUselessSpace + "\n" +
    	"~~Useless space including Qorg with IA: " + normalizedUselessSpaceIncludingQOrg + "\n" +
    	"//Sibling Mega Ratio with IA: " + siblingMegaRatio + "\n" +
    	"++Sibling Imbalance Coefficient with IA: " + siblingImbalanceCoefficient);
    }  
    
    /** 
     * Apply estimated decision rule
     * SILENCED (The detailed printout of all the estimated metrics)
     * @return MQO Intentional Strategy
     */
    public IntentionalStrategy decideMQOAlgorithmWithIndependenceAssumption() {
    	//System.out.println(diagnosticString);
		if(normalizedSiblingSum > 0.35) {
			return IntentionalStrategy.MAX_MQO;
		}else {
			if(siblingMegaRatio <= 0.57) {
				return IntentionalStrategy.MID_MQO;
			}else {
				return IntentionalStrategy.MAX_MQO;
			}
		}
	}
}
