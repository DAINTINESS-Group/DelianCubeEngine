package chartManagement;

import java.util.List;

import mainengine.ResultFileMetadata;
import model.abstracts.AbstractModel;

public class LinechartQueryGenerator implements IChartQueryNModelGenerator{

	private String type = "LineChart";

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
		String typeOfVisualization = "default";
		String producedLineOfVisualization = "Visualization : " + typeOfVisualization + "\n";
		return producedLineOfVisualization;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
