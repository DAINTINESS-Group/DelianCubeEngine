package chartManagement;

import java.util.List;

import chartManagement.models.ChartModel;
import chartManagement.models.ModelListFactory;


public class BarchartQueryGenerator implements IChartQueryNModelGenerator{

	
	private String type = "Barchart";
	


	@Override
	public List<ChartModel> generateModels() {
		return new ModelListFactory().createModelsForChartType(this);
	}

	@Override
	public float assessModelSignificance() {
		return 0;
	}


	@Override
	public String generateQueries(String[] sortedResultsWithTypeOfVisualization) {
		String typeOfVisualization = sortedResultsWithTypeOfVisualization[1];
		String producedLineOfVisualization = "Visualization : " + typeOfVisualization + "\n";
		return producedLineOfVisualization;
	}

	@Override
	public String getType() {
		return type;
	}
	


}
