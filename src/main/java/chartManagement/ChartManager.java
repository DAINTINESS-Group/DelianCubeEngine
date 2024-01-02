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
	
	public ChartManager(CubeManager cubeManager)
	{
		this.cubeManager = cubeManager;
		this.chartQueryGeneratorFacade = new ChartQueryGeneratorFacade();
		
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
		System.out.println("size here!!!!! ->   " + producedQueries.size());
		
	}
	
	public  String [][] executeQueries() throws IOException
	{
		String [][] result;
		String [][] produced;
//		System.out.println("ChartManager test: ");
//		System.out.println("size here passed!!!!! ->   " + producedQueries.size());
		for(AnalyzeQuery query: producedQueries)
		{
			if((query.getType().toString()).equals("Base"))
			{
				System.out.print("i get here!"); 
				result = query.getAnalyzeQueryResult().getResultArray();
				produced = new String[result.length][result[1].length];
				
				for(int i=1; i<result.length; i++)
				{
					for(int j=0; j<result[i].length; j++)
					{
						System.out.print(result[i][j] + " ");
						produced[i-1][j] = result[i][j];
					}
					System.out.println();
				}
				System.out.println();
				return produced;
				
			}

		}
		
		return null;
	}

}
