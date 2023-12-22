package chartManagement;

import analyze.AnalyzeOperator;
import cubemanager.CubeManager;
import mainengine.ResultFileMetadata;

public class ChartManager {
	
	private CubeManager cubeManager;
	private ChartQueryGeneratorFacade chartQueryGeneratorFacade;
	private AnalyzeOperator operator;
	private ResultFileMetadata resultQueries;
	
	
	public ChartManager(CubeManager cubeManager)
	{
		this.cubeManager = cubeManager;
		this.chartQueryGeneratorFacade = new ChartQueryGeneratorFacade();
		
	}
	
	public IChartQueryGenerator createChartQuery(String type)
	{
		return chartQueryGeneratorFacade.createChartQueryGenerator(type);
	}
	
	
	public void createConnectionWithAnalyzeOperator(String incomingQuery,String schemaName, String connectionType)
	{
		
		operator = new AnalyzeOperator(incomingQuery, cubeManager, schemaName, connectionType);
		
	}
	
	public AnalyzeOperator getOperator()
	{
		return operator;
	}
	
	public void generateQueries()
	{
		resultQueries = operator.execute();
	}
	
	public ResultFileMetadata executeQueries()
	{
		return resultQueries;
	}

}
