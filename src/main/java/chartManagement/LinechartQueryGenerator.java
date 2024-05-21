package chartManagement;

import java.util.List;

import chartManagement.models.ChartModel;
import chartManagement.models.ModelListFactory;

public class LinechartQueryGenerator implements IChartQueryNModelGenerator{

	private String type = "LineChart";



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
		
		String typeOfVisualization = "default";
		String producedLineOfVisualization = "Visualization : " + typeOfVisualization + "\n";
		return producedLineOfVisualization;
	}

	@Override
	public String getType() {
		
		return type;
	}

}
