package chartManagement;

import java.util.List;

import mainengine.ResultFileMetadata;
import model.abstracts.AbstractModel;

public class BarchartQueryGenerator implements IChartQueryNModelGenerator{

	
	
	
	@Override
	public ResultFileMetadata executeQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AbstractModel> generateModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float assessModelSignificance() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String generateQueries(String[] sortedResultsWithTypeOfVisualization) {
		// TODO Auto-generated method stub
		String typeOfVisualization = sortedResultsWithTypeOfVisualization[1];
		String producedLineOfVisualization = "Visualization : " + typeOfVisualization + "\n";
		return producedLineOfVisualization;
	}
	


}
