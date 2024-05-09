package chartManagement;

import java.util.List;

import mainengine.ResultFileMetadata;
import model.abstracts.AbstractModel;

public class ScatterplotQueryGenerator implements IChartQueryNModelGenerator{

	
	private String type = "Scatterplot";

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
	
	public AbstractModel generateCorrelationModel()
	{
		return null;
	}
	
	public AbstractModel generateLinearRegressionModel()
	{
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
