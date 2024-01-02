package chartManagement;

import java.util.List;

import mainengine.ResultFileMetadata;
import model.abstracts.AbstractModel;

public interface IChartQueryNModelGenerator {
	
	
	void generateQueries();
	
	ResultFileMetadata executeQueries();
	
	List<AbstractModel> generateModels();
	
	float assessModelSignificance();

}
