package chartManagement;

import cubemanager.CubeManager;

public class ChartManager {
	
	private CubeManager cubeManager;
	private ChartQueryGeneratorFacade chartQueryGeneratorFacade;
	
	public ChartManager(CubeManager cubeManager)
	{
		this.cubeManager = cubeManager;
		this.chartQueryGeneratorFacade = new ChartQueryGeneratorFacade();
	}
	
	public IChartQueryGenerator createChartQuery(String type)
	{
		return chartQueryGeneratorFacade.createChartQueryGenerator(type);
	}

}
