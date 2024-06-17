package chartManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import analyze.AnalyzeOperator;
import analyze.AnalyzeQuery;
import chartManagement.models.ModelManager;
import chartManagement.utils.ChartResponse;
import chartManagement.utils.ChartScoreModel;
import chartManagement.utils.ChartVisModel;
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
	private String aggrFunc;
	
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
		extractAggregationFunctionFromAnalyzeQuery(incomingQuery);
		
	}
	
	private void extractAggregationFunctionFromAnalyzeQuery(String incomingQuery) {
		int analyzeIndex = incomingQuery.indexOf("ANALYZE");
        String aggrFunc = "";
        if (analyzeIndex != -1) {
            // Find the position of the first '(' after "ANALYZE"
            int openParenIndex = incomingQuery.indexOf('(', analyzeIndex);
            
            if (openParenIndex != -1) {
            	aggrFunc = incomingQuery.substring(analyzeIndex + 7, openParenIndex).trim();
            }
        }
        this.aggrFunc = aggrFunc;
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
		modelManager.setAggrFunc(aggrFunc);
		
		ResultFileMetadata resultFile = new ResultFileMetadata();
		resultFile.setLocalFolder(visualizationManager.getLocalFolder());
		resultFile.setResultFile(visualizationManager.getLocalFilename());
		
		
		
		return resultFile;
	}
	
	public ChartResponse executeQueriesAndReturnToChartResponse() throws Exception {
		
		removeDrillDownQueries();
		List<ChartVisModel> chartVisModels = visualizationManager.reportChartQueryDetailsForChartResponse(BaseAndSiblingQueries, this.chartType);
		List<ChartScoreModel> chartScoreModels = modelManager.getScoreModelsForChartVisModels(chartVisModels);
		ChartResponse response = new ChartResponse();
		response.setChartScoreModels(chartScoreModels);
		response.setChartVisModels(chartVisModels);
		return response;
		
	}

}
