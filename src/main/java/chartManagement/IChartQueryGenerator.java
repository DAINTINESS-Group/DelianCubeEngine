package chartManagement;

import java.util.List;

import mainengine.ResultFileMetadata;
import model.abstracts.AbstractModel;

public interface IChartQueryGenerator {
	
	
	void generateQueries();
	
	ResultFileMetadata executeQueries();
	
	List<AbstractModel> generateModels();
	
	float assessModelSignificance();

}
