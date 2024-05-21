package chartManagement;

import java.io.IOException;
import java.util.ArrayList;


import analyze.AnalyzeOperator;
import analyze.AnalyzeQuery;
import chartManagement.models.ModelManager;
import cubemanager.CubeManager;
import mainengine.ResultFileMetadata;

public class ChartManager {
	
	private CubeManager cubeManager;
	private ChartQueryGeneratorFacade chartQueryGeneratorFacade;
	private AnalyzeOperator operator;
	ArrayList<AnalyzeQuery> producedQueries;
	ArrayList<AnalyzeQuery> BaseAndSiblingQueries;
	private VisualizationManager visualizationManager;
	private ModelManager modelManager;
	private String chartType;
	
	public ChartManager(CubeManager cubeManager)
	{
		this.cubeManager = cubeManager;
		this.chartQueryGeneratorFacade = new ChartQueryGeneratorFacade();
		this.BaseAndSiblingQueries = new ArrayList<AnalyzeQuery>();
		this.visualizationManager = new VisualizationManager();
		this.modelManager = new ModelManager();
	}
	
	public IChartQueryNModelGenerator createChartQuery(String type)
	{
		return chartQueryGeneratorFacade.createChartQueryGenerator(type);
	}
	
	
	public void createConnectionWithAnalyzeOperator(String incomingQuery,String schemaName, String connectionType){
		
		operator = new AnalyzeOperator(incomingQuery, cubeManager, schemaName, connectionType);
		
	}
	
	public AnalyzeOperator getOperator(){
		return operator;
	}
	
	public void generateQueries()
	{
		operator.execute();
		producedQueries = operator.getAnalyzeQueries();
		
		
	}
	
	private void removeDrillDownQueries() {
		
		for(AnalyzeQuery query: producedQueries){
			if(!(query.getType().toString()).equals("Drill_Down")){
				BaseAndSiblingQueries.add(query);
			}
			
		}
		
		
	}
	
	public void setChartType(String type){
		this.chartType = type;
		IChartQueryNModelGenerator chartQueryNModelGenerator = createChartQuery(type);
		visualizationManager.setQueryNModelGenerator(chartQueryNModelGenerator);
		modelManager.setQueryNModelGenerator(chartQueryNModelGenerator);
	}

	public  ResultFileMetadata executeQueries() throws Exception{
		removeDrillDownQueries();
		

		
		visualizationManager.reportChartQueryDetails(BaseAndSiblingQueries, this.chartType);
		
		modelManager.setLocalFolderAndLocalFilename(visualizationManager.getLocalFolder() ,visualizationManager.getLocalFilename());
		modelManager.reportModelsForChartType();
		
		ResultFileMetadata resultFile = new ResultFileMetadata();
		resultFile.setLocalFolder(visualizationManager.getLocalFolder());
		resultFile.setResultFile(visualizationManager.getLocalFilename());
		
		
		
		return resultFile;
	}

}
