package chartManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import analyze.AnalyzeOperator;
import analyze.AnalyzeQuery;
import cubemanager.CubeManager;
import mainengine.ResultFileMetadata;

public class ChartManager {
	
	private CubeManager cubeManager;
	private ChartQueryGeneratorFacade chartQueryGeneratorFacade;
	private AnalyzeOperator operator;
	private ResultFileMetadata resultQueries;
	ArrayList<AnalyzeQuery> producedQueries;
	ArrayList<AnalyzeQuery> BaseAndSiblingQueries;
	private VisualizationManager visualizationManager;
	private String chartType;
	
	public ChartManager(CubeManager cubeManager)
	{
		this.cubeManager = cubeManager;
		this.chartQueryGeneratorFacade = new ChartQueryGeneratorFacade();
		this.BaseAndSiblingQueries = new ArrayList<AnalyzeQuery>();
		this.visualizationManager = new VisualizationManager();
	}
	
	public IChartQueryNModelGenerator createChartQuery(String type)
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
		producedQueries = operator.getAnalyzeQueries();
		
		
	}
	
	private void removeDrillDownQueries() {
		for(AnalyzeQuery query: producedQueries)
		{
			if(!(query.getType().toString()).equals("Drill_Down"))
			{
				BaseAndSiblingQueries.add(query);
			}
			
		}
		
	}
	
	public void setChartType(String type)
	{
		this.chartType = type;
		IChartQueryNModelGenerator chartQueryNModelGenerator = createChartQuery(type);
		visualizationManager.setQueryNModelGenerator(chartQueryNModelGenerator);
	}

	public  ResultFileMetadata executeQueries() throws IOException
	{
		removeDrillDownQueries();
		

		
		visualizationManager.reportChartQueryDetails(BaseAndSiblingQueries, this.chartType);
		
		ResultFileMetadata resultFile = new ResultFileMetadata();
		resultFile.setLocalFolder(visualizationManager.getLocalFolder());
		resultFile.setResultFile(visualizationManager.getLocalFilename());
		
		return resultFile;
	}

}
