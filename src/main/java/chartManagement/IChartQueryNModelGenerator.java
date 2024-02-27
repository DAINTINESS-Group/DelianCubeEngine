package chartManagement;

import java.util.List;

import mainengine.ResultFileMetadata;
import model.abstracts.AbstractModel;

public interface IChartQueryNModelGenerator {
	
	
	String generateQueries(String [] sortedResultsWithTypeOfVisualization);
	
	ResultFileMetadata executeQueries();
	
	List<AbstractModel> generateModels();
	
	float assessModelSignificance();

	

}
