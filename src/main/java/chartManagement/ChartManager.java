package chartManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import analyze.AnalyzeOperator;
import analyze.AnalyzeQuery;
import chartManagement.report.ChartReportQuery;
import cubemanager.CubeManager;
import mainengine.ResultFileMetadata;

public class ChartManager {
	
	private CubeManager cubeManager;
	private ChartQueryGeneratorFacade chartQueryGeneratorFacade;
	private AnalyzeOperator operator;
	private ResultFileMetadata resultQueries;
	ArrayList<AnalyzeQuery> producedQueries;
	ArrayList<AnalyzeQuery> BaseAndSiblingQueries;
	private ChartReportQuery chartReportQuery;
	
	public ChartManager(CubeManager cubeManager)
	{
		this.cubeManager = cubeManager;
		this.chartQueryGeneratorFacade = new ChartQueryGeneratorFacade();
		this.BaseAndSiblingQueries = new ArrayList<AnalyzeQuery>();
		this.chartReportQuery = new ChartReportQuery("File");
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

	public  ResultFileMetadata executeQueries() throws IOException
	{
		removeDrillDownQueries();
		
//		System.out.println("ChartManager test: ");
//		System.out.println("size here passed!!!!! ->   " + producedQueries.size());
		
		chartReportQuery.reportChartQueryDetails(BaseAndSiblingQueries);
		
		ResultFileMetadata resultFile = new ResultFileMetadata();
		resultFile.setLocalFolder(chartReportQuery.getLocalFolder());
		resultFile.setResultFile(chartReportQuery.getLocalFilename());
		
		return resultFile;
	}

}
