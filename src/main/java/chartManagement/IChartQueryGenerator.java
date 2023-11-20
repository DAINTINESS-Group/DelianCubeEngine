package chartManagement;

import java.util.List;

import model.abstracts.AbstractModel;

public interface IChartQueryGenerator {
	
	
	String generateQueries();
	
	void executeQueries();
	
	List<AbstractModel> generateModels();
	
	float assessModelSignificance();

}
