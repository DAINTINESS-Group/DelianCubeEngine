package chartManagement;

import java.util.List;

import chartManagement.models.ChartModel;


public interface IChartQueryNModelGenerator {
	
	
	String generateQueries(String [] sortedResultsWithTypeOfVisualization);
	
	
	List<ChartModel> generateModels();
	
	float assessModelSignificance();

	String getType();

}
