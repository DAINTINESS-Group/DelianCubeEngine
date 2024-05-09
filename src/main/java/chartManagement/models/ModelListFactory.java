package chartManagement.models;

import java.util.ArrayList;
import java.util.List;

import chartManagement.IChartQueryNModelGenerator;
import model.abstracts.AbstractModel;

public class ModelListFactory {
	
	public List<IChartModel> createModelsForChartType(IChartQueryNModelGenerator chartModel) {
		List<IChartModel> models = new ArrayList<>();
		
		if(chartModel.getType().equals("Barchart")) {
			IChartModel dominanceFinder =  new DominanceModel();
			
			IChartModel contributor = new ContributorModel();
			
			
			models.add(dominanceFinder);

			models.add(contributor);

		}else if(chartModel.getType().equals("LineChart") || chartModel.getType().equals("Scatterplot")){
			IChartModel TrendFinder =  new TrendModel();
			IChartModel regressionModel = new RegressionModel();
			IChartModel modalityFinder = new ModalityModel();
			
			models.add(TrendFinder);
			models.add(modalityFinder);
			models.add(regressionModel);
		}else {
			return null;
		}
		return models;
		
	}

}
