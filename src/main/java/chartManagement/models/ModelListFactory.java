package chartManagement.models;

import java.util.ArrayList;
import java.util.List;

import chartManagement.IChartQueryNModelGenerator;
import model.abstracts.AbstractModel;

public class ModelListFactory {
	
	
	
	public List<ChartModel> createModelsForChartType(IChartQueryNModelGenerator chartQueryNModelGenerator) {
		List<ChartModel> models = new ArrayList<>();
		
		switch(chartQueryNModelGenerator.getType()) {
		
			case("Barchart"):
			
				//models.add(new DominanceModel());
				//models.add(new ContributorModel());
				//break;
				
			case("LineChart"): 
			case("Scatterplot"):
				models.add(new KendallBasedTrendModel());
				models.add(new AbsoluteTrendModel());
				models.add(new ModalityModel());
				models.add(new RegressionModel());
				models.add(new ContributorModel());
				break;
				
			default:
				return null;
			
		}
		
		return models;
		
	}

}
