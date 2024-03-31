package chartManagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import analyze.AnalyzeQuery;
import chartManagement.utils.DataPoint;

public class VisualizationManager {
	
	private String filename;
	private FileWriter chartReporter;
	private String localFolder;
	private String localFilename;
	private IChartQueryNModelGenerator chartQueryNModelGenerator;
	
	public VisualizationManager()
	{
		this.filename = "File";
		this.localFolder = "OutputFiles";
	}
	
	public void reportChartQueryDetails(ArrayList<AnalyzeQuery> chartQueries, String chartType)
	{
		this.localFilename = this.filename + "-ChartQueries_Report.md";
		
		String resultQuery = "";
		try {
			
		
			chartReporter = new FileWriter(localFolder +  "/" + localFilename);
			chartReporter.write("Details For Designing:\n");
			chartReporter.write("Chart queries produced: " + chartQueries.size() + "\n\n");
			
			for(AnalyzeQuery query: chartQueries)
			{
				List<DataPoint> records = null;
				String producedQueryVisualization;
				String typeHeader = null;
				if(query.getType().toString().equals("Base")) 
				{
					records = readDataFromStringForBaseQuery(reportBaseQuery(query));
					typeHeader= "Type: Base\n" + "Details:\n" + "|Grouper 1|Grouper 2|Measure|\n";
					
					
					
				}else if(query.getType().toString().equals("Sibling")) {
					records = readDataFromStringForSiblingQuery(reportSiblingQuery(query));
					typeHeader= returnSiblingHeader(query);
					
				}
				String [] result = sortResults(records);
				
				resultQuery += processResultsForVisualization(result);
				resultQuery += typeHeader;
				resultQuery +=  result[0];
				
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
	
	public void setQueryNModelGenerator(IChartQueryNModelGenerator chartQueryNModelGenerator)
	{
		this.chartQueryNModelGenerator = chartQueryNModelGenerator;
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
	
	public String returnSiblingHeader(AnalyzeQuery query)
	{
		String details = "Filter value that is modified compared to the Base Query: **" + query.getOriginalSigmaValue() + "**\n"
				+ "Filter value after modification: **" + query.getModifiedSigmaValue() + "**\n"
				+ "Grouper value that is modified compared to the Base Query: **" + query.getOriginalGammaValue() + "**\n"
				+ "Grouper value after modification: **" + query.getModifiedGammaValue() + "**\n";
		
		String producedString = "Type: Sibling\n";
		producedString += "Details: " + details;
		producedString += "|Grouper 1|Grouper 2|Measure|\n";
		return producedString;
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
	
    public static String checkDateFormat(String dateString) {
        String[] formatsToCheck = {"yyyy-MM","yyyy"};

        for (String format : formatsToCheck) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Date date = sdf.parse(dateString);
                return format; // If parsing succeeds, return the format
            } catch (ParseException e) {
                // Parsing failed, try the next format
            }
        }

        return null; // If none of the formats match
    }
    
    public static List<DataPoint> readDataFromStringForBaseQuery(String dataString) {
        List<DataPoint> data = new ArrayList<>();
        
        String[] lines = dataString.split("\n");
        
        // Skip the header
        

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        for (int dataIndex = 3; dataIndex< lines.length; dataIndex++) {
            String[] parts = lines[dataIndex].split("\t");
            Date date = null;

            try {
                date = dateFormat.parse(parts[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String grouper1 = parts[0];
            String grouper2 = parts[1];
            int measure = Integer.parseInt(parts[2]);
          
          
            data.add(new DataPoint(date, grouper1, grouper2, measure));
        }
        
        return data;
    }
    
    public String returnSortedRecords(List<DataPoint> points)
    {
    	String produced ="";
    	for(DataPoint dp: points)
    	{
    		produced += dp.getGrouper1() + "\t" + dp.getGrouper2() + "\t" + dp.getMeasure();
    		produced += '\n';
    	}
    	produced+="\n";
    	return produced;
    	
    }
    
    public static List<DataPoint> readDataFromStringForSiblingQuery(String dataString) {
        List<DataPoint> data = new ArrayList<>();

        String[] lines = dataString.split("\n");

        // Skip the header
        

        SimpleDateFormat dateFormat = null;

        for (int dataIndex = 6; dataIndex< lines.length; dataIndex++) {
            String[] parts = lines[dataIndex].split("\t");
            Date date = null;
            if(dataIndex==6) {
            	String dateFormatOfSibling = checkDateFormat(parts[0]);
            	if(dateFormatOfSibling.equals("yyyy"))
            	{
            		dateFormat = new SimpleDateFormat("yyyy");
            	}else {
            		dateFormat = new SimpleDateFormat("yyyy-MM");
            	}
            }

            try {
                date = dateFormat.parse(parts[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String grouper1 = parts[0];
            String grouper2 = parts[1];
            int measure = Integer.parseInt(parts[2]);

            data.add(new DataPoint(date, grouper1, grouper2, measure));
        }

        return data;
    }
    
    public String [] sortResults(List<DataPoint> points)
    {
    	String produced = "";
    	ArrayList<String> groupers1List =  new ArrayList<String>();
    	ArrayList<String> groupers2List = new ArrayList<String>();
    	
    	for(DataPoint point: points)
    	{
    		if(!groupers1List.contains(point.getGrouper1()))
    		{
    			groupers1List.add(point.getGrouper1());
    		}
    		
    		if(!groupers2List.contains(point.getGrouper2()))
    		{
    			groupers2List.add(point.getGrouper2());
    		}
    		
    	}
    	String[] groupers1Array = groupers1List.toArray(new String[0]);
    	String[] groupers2Array = groupers2List.toArray(new String[0]);
    	Arrays.sort(groupers1Array);
    	Arrays.sort(groupers2Array);

    	for(String first_group: groupers1Array)
    	{
    		for(String second_group: groupers2Array)
    		{
    			produced += first_group + "\t" + second_group + "\t" + returnMeasureForRecord(points, first_group, second_group);
    			produced += '\n';
    		}
    		
    	}
    	
    	String typeOfVisualization = decideType(groupers2Array.length);
    	
    	produced += '\n';
    	produced += "X_axis values : ";
    	for(int i=0; i< groupers1Array.length; i++)
    	{
    		produced+= groupers1Array[i] + "\t";
    	}
    	produced += "\nSeries :";
    	for(int i=0; i<  groupers2Array.length; i++)
    	{
    		produced+= groupers2Array[i] + "\t";
    	}
    	produced += "\n\n";
    	String [] resultSet = new String[2]; 
    	resultSet[0] = produced;
    	resultSet[1] = typeOfVisualization;
    	return resultSet;
    	
    }

	private int returnMeasureForRecord(List<DataPoint> points, String grouper1, String grouper2)
	{
		for(DataPoint point: points)
		{
			
			if(point.getGrouper1().equals(grouper1) && point.getGrouper2().equals(grouper2)) 
			{
				return point.getMeasure();
			}
			
			
		}
		
		return 0;
		
	}
	
	public String decideType(int groupers2)
	{
		if(groupers2 >5)
		{
			return "small multiplies";
		}
		
		return "default";
	}
	
	public String processResultsForVisualization(String [] sortedResultsWithTypeOfVisualization) //TODO change type to string
	{
		return chartQueryNModelGenerator.generateQueries(sortedResultsWithTypeOfVisualization); //return queries with what visualization(small multiplies)
	}

}
