package chartManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import analyze.AnalyzeQuery;
import chartManagement.utils.DataPoint;
import result.Result;


public class VisualizationManagerTest {
	
	@Test
    public void testReportSiblingQuery() throws IOException {
 
		
		//Arrange
        AnalyzeQuery query = Mockito.mock(AnalyzeQuery.class);
        
       
        when(query.getOriginalSigmaValue()).thenReturn("Original Sigma Value");
        when(query.getModifiedSigmaValue()).thenReturn("Modified Sigma Value");
        when(query.getOriginalGammaValue()).thenReturn("Original Gamma Value");
        when(query.getModifiedGammaValue()).thenReturn("Modified Gamma Value");

        //Act
        Result result = new Result(3,3);
        when(query.getAnalyzeQueryResult()).thenReturn(result);
        VisualizationManager manager = new VisualizationManager();
        String report = manager.reportSiblingQuery(query);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("Type: Sibling"));
        assertTrue(report.contains("Original Sigma Value"));
        assertTrue(report.contains("Modified Sigma Value"));
        assertTrue(report.contains("Original Gamma Value"));
        assertTrue(report.contains("Modified Gamma Value"));
    }
	
	@Test
	public void testReportBaseQuery()
	{
		//Arrange
		AnalyzeQuery query = Mockito.mock(AnalyzeQuery.class);
		
		
		
		
		//Act
		Result result = new Result(3,3);
		 
        when(query.getAnalyzeQueryResult()).thenReturn(result);
        
        VisualizationManager manager = new VisualizationManager();
        String report = manager.reportBaseQuery(query);
		
		
		//Assert
        assertNotNull(report);
        assertTrue(report.contains("Type: Base"));
        assertTrue(report.contains("|Grouper 1|Grouper 2|Measure|"));
        
	}
	
    @Test
    public void testDateFormatYYYY() throws Exception {
        String dateString = "2024";
        VisualizationManager manager = new VisualizationManager();
        assertEquals("yyyy", manager.checkDateFormat(dateString));
    }

    @Test
    public void testDateFormatYYYY_MM() throws Exception {
        String dateString = "2024-03";
        VisualizationManager manager = new VisualizationManager();
        assertEquals("yyyy-MM", manager.checkDateFormat(dateString));
    }
    
    @Test
    public void testReturnSiblingHeader() {
    	//Arrange
        AnalyzeQuery query = Mockito.mock(AnalyzeQuery.class);
        VisualizationManager manager = new VisualizationManager();
        
        when(query.getOriginalSigmaValue()).thenReturn("Original Sigma Value");
        when(query.getModifiedSigmaValue()).thenReturn("Modified Sigma Value");
        when(query.getOriginalGammaValue()).thenReturn("Original Gamma Value");
        when(query.getModifiedGammaValue()).thenReturn("Modified Gamma Value");

        String expectedDetails = "Filter value that is modified compared to the Base Query: **Original Sigma Value**\n" +
                                 "Filter value after modification: **Modified Sigma Value**\n" +
                                 "Grouper value that is modified compared to the Base Query: **Original Gamma Value**\n" +
                                 "Grouper value after modification: **Modified Gamma Value**\n";

        String expectedProducedString = "Type: Sibling\n" +
                                        "Details: " + expectedDetails +
                                        "|Grouper 1|Grouper 2|Measure|\n";

        assertEquals(expectedProducedString, manager.returnSiblingHeader(query));
    }

    @Test
    public void testReadDataFromStringForBaseQuery() throws ParseException
    {
    	//Arrange
    	VisualizationManager visualizationManager = new VisualizationManager();
    	String dataString = "firstLineToSkip\n" +
    			"secondLineToSkip\n" +
    			"thirdLineToSkip\n" +
    			"1998-01\tHl.m. Praha\t37944\n" +
                "1998-02\tHl.m. Praha\t428784\n" +
                "1998-03\tHl.m. Praha\t157068\n";
                
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	
    	//Act
    	List<DataPoint> actualData = visualizationManager.readDataFromStringForBaseQuery(dataString);
    	List<DataPoint> expectedData = new ArrayList<>();
    	DataPoint firstDataPoint = new DataPoint(dateFormat.parse("1998-01"), "1998-01", "Hl.m. Praha", 37944); 
    	DataPoint secondDataPoint = new DataPoint(dateFormat.parse("1998-02"), "1998-02", "Hl.m. Praha", 428784); 
    	DataPoint thirdDataPoint = new DataPoint(dateFormat.parse("1998-03"), "1998-03", "Hl.m. Praha", 157068); 
    	expectedData.add(firstDataPoint);
    	expectedData.add(secondDataPoint);
    	expectedData.add(thirdDataPoint);
    	
    	//Assert
    	assertEquals(expectedData.get(0).getGrouper1(),actualData.get(0).getGrouper1());
    	assertEquals(expectedData.get(1).getMeasure(), actualData.get(1).getMeasure(),0.001); //delta value for comparing float numbers
    	assertEquals(expectedData.get(2).getGrouper2(), actualData.get(2).getGrouper2());
    }
    
    @Test
    public void testReadDataFromStringForSiblingQuery() throws Exception
    {
    	//Arrange
    	VisualizationManager visualizationManager = new VisualizationManager();
    	String dataString = "firstLineToSkip\n" +
    			"secondLineToSkip\n" +
    			"thirdLineToSkip\n" +
    			"forthLineToSkip\n" +
    			"fifthLineToSkip\n" +
    			"sixthLineToSkip\n" +
    			"1998-01\tHl.m. Praha\t37944\n" +
                "1998-02\tHl.m. Praha\t428784\n" +
                "1998-03\tHl.m. Praha\t157068\n";
                
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	
    	//Act
    	List<DataPoint> actualData = visualizationManager.readDataFromStringForSiblingQuery(dataString);
    	List<DataPoint> expectedData = new ArrayList<>();
    	DataPoint firstDataPoint = new DataPoint(dateFormat.parse("1998-01"), "1998-01", "Hl.m. Praha", 37944); 
    	DataPoint secondDataPoint = new DataPoint(dateFormat.parse("1998-02"), "1998-02", "Hl.m. Praha", 428784); 
    	DataPoint thirdDataPoint = new DataPoint(dateFormat.parse("1998-03"), "1998-03", "Hl.m. Praha", 157068); 
    	expectedData.add(firstDataPoint);
    	expectedData.add(secondDataPoint);
    	expectedData.add(thirdDataPoint);
    	
    	//Assert
    	assertEquals(expectedData.get(0).getGrouper1(),actualData.get(0).getGrouper1());
    	assertEquals(expectedData.get(1).getMeasure(), actualData.get(1).getMeasure(),0.001); //delta value for comparing float numbers
    	assertEquals(expectedData.get(2).getGrouper2(), actualData.get(2).getGrouper2());
    }
    
    @Test
    public void testSortResults() throws ParseException
    {
    	//Arrange
    	VisualizationManager visualizationManager = new VisualizationManager();
    	List<DataPoint> actualData = new ArrayList<>();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	
    	//Act
    	DataPoint firstDataPoint = new DataPoint(dateFormat.parse("1998-01"), "1998-01", "Hl.m. Praha", 37944); 
    	DataPoint secondDataPoint = new DataPoint(dateFormat.parse("1998-02"), "1998-02", "Hl.m. Praha", 428784); 
    	DataPoint thirdDataPoint = new DataPoint(dateFormat.parse("1998-03"), "1998-03", "Hl.m. Praha", 157068); 
    	actualData.add(thirdDataPoint);
    	actualData.add(secondDataPoint);
    	actualData.add(firstDataPoint);
    	String [] actualSortedResults = visualizationManager.sortResults(actualData);
    	String [] expectedSortedResults = new String[2];
    	expectedSortedResults[0] = "" +
    							   firstDataPoint.getGrouper1() + "\t" + firstDataPoint.getGrouper2() + "\t" + firstDataPoint.getMeasure() + "\n" +
    							   secondDataPoint.getGrouper1() + "\t" + secondDataPoint.getGrouper2()+ "\t" + secondDataPoint.getMeasure() + "\n" +
    							   thirdDataPoint.getGrouper1() + "\t" + thirdDataPoint.getGrouper2()+ "\t" + thirdDataPoint.getMeasure() + "\n\n" +
    							   "X_axis values : " + firstDataPoint.getGrouper1() + "\t" + secondDataPoint.getGrouper1() + "\t" + thirdDataPoint.getGrouper1() + "\t" +
    							   "\nSeries :" + firstDataPoint.getGrouper2() + "\t" +
    							   "\n\n";
    	expectedSortedResults[1] = "default";
    							   
    	//Assert
    	assertEquals(expectedSortedResults[0], actualSortedResults[0]);
    	assertEquals(expectedSortedResults[1], actualSortedResults[1]);
    	
    	
    }
    
    @Test
    public void testDecideType()
    {
    	//Arrange
    	VisualizationManager visualizationManager = new VisualizationManager();
    	int numOfGroupers2SmallerThan5 = 3;
    	int numOfGroupers2Equals5 = 5;
    	int numOfGroupers2GreaterThan5 = 7;
    	
    	//Act
    	String resNumOfGroupers2SmallerThan5 = visualizationManager.decideType(numOfGroupers2SmallerThan5);
    	String resNumOfGroupers2Equals5 = visualizationManager.decideType(numOfGroupers2Equals5);
    	String resNumOfGroupers2GreaterThan5 =  visualizationManager.decideType(numOfGroupers2GreaterThan5);
    	
    	//Assert
    	assertEquals("default", resNumOfGroupers2SmallerThan5);
    	assertEquals("default", resNumOfGroupers2Equals5);
    	assertEquals("small multiplies", resNumOfGroupers2GreaterThan5);
    }
    
    @Test
    public void testProcessResultsForVisualization()
    {
    	//Arrange
    	VisualizationManager visualizationManager = new VisualizationManager();
    	String [] actualSortedResultsWithTypeOfVisualization = new String[2];
    	actualSortedResultsWithTypeOfVisualization[0] = "Some description Of Query result";
    	actualSortedResultsWithTypeOfVisualization[1] = "small multiplies";
    	IChartQueryNModelGenerator chartQueryNModelGenerator = mock(IChartQueryNModelGenerator.class);
    	when(chartQueryNModelGenerator.generateQueries(actualSortedResultsWithTypeOfVisualization)).thenReturn("small multiplies");
    	
    	//Act      
        visualizationManager.setQueryNModelGenerator(chartQueryNModelGenerator);
        String result = visualizationManager.processResultsForVisualization(actualSortedResultsWithTypeOfVisualization);

        //Assert 
        assertEquals("small multiplies", result);
    	
    }

}
