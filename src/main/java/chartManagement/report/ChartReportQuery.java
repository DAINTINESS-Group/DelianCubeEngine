package chartManagement.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import analyze.AnalyzeQuery;

public class ChartReportQuery {

	private String filename;
	private FileWriter chartReporter;
	private String localFolder;
	private String localFilename;
	
	public ChartReportQuery(String filename)
	{
		this.filename = filename;
		this.localFolder = "OutputFiles";
	}
	
	
	public void reportChartQueryDetails(ArrayList<AnalyzeQuery> chartQueries)
	{
		this.localFilename = this.filename + "-ChartQueries_Report.md";
		
		String resultQuery = "";
		try {
			
		
			chartReporter = new FileWriter(localFolder +  "/" + localFilename);
			chartReporter.write("Details For Designing:\n");
			chartReporter.write("Chart queries produced: " + chartQueries.size() + "\n\n");
			
			for(AnalyzeQuery query: chartQueries)
			{
				if(query.getType().toString().equals("Base")) 
				{
					resultQuery += reportBaseQuery(query);
					
				}else if(query.getType().toString().equals("Sibling")) {
					resultQuery += reportSiblingQuery(query);
				}
				
			}
			chartReporter.write(resultQuery);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        try {
	            if (chartReporter != null) {
	                chartReporter.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
		

		
	}


	public String reportSiblingQuery(AnalyzeQuery query) throws IOException {
		
		String details = "Filter value that is modified compared to the Base Query: **" + query.getOriginalSigmaValue() + "**\n"
				+ "Filter value after modification: **" + query.getModifiedSigmaValue() + "**\n"
				+ "Grouper value that is modified compared to the Base Query: **" + query.getOriginalGammaValue() + "**\n"
				+ "Grouper value after modification: **" + query.getModifiedGammaValue() + "**\n";
		String resultInFile = "";	
		
		String [][] result =  query.getAnalyzeQueryResult().getResultArray();
		resultInFile += "Type: Sibling\n";
		resultInFile += "Details: " + details;
		resultInFile += "|Grouper 1|Grouper 2|Measure|\n";
		for(int i=2; i<result.length; i++)
		{
			for(int j=0; j<result[i].length-1; j++)
			{
				resultInFile += result[i][j] + "\t";
			}
			resultInFile += "\n";
		}
		resultInFile += "\n";
		return resultInFile;
	}


	public String reportBaseQuery(AnalyzeQuery query){
		String details = "\n";
		String resultInFile = "";	
		
		String [][] result =  query.getAnalyzeQueryResult().getResultArray();
	
		resultInFile += "Type: Base\n";
		resultInFile += "Details: " + details;
		resultInFile += "|Grouper 1|Grouper 2|Measure|\n";
		
		for(int i=2; i<result.length; i++)
		{
			for(int j=0; j<result[i].length-1; j++)
			{
				resultInFile += result[i][j] + "\t";
			}
			resultInFile += "\n";
		}
		resultInFile += "\n";
		return resultInFile;
	}
	
	
	public String getLocalFilename()
	{
		return localFilename;
	}
	
	public String getLocalFolder()
	{
		return localFolder;
	}
	
	
}
