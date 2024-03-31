package client.gui.controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import analyze.AnalyzeQuery;
import chartRequestManagement.ChartRequestBuilderImpl;
import chartRequestManagement.ChartRequest;
import chartRequestManagement.IChartRequestBuilder;
import client.ClientRMITransferer;

import client.ClientRMITransferer;

import client.gui.application.AbstractApplication;
import client.gui.utils.CustomAlertDialog;
import client.gui.utils.ExitController;
import client.gui.utils.LauncherForViewControllerPairs;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainengine.IMainEngine;
import mainengine.ResultFileMetadata;

public class ChartQueryEditorController extends AbstractController 
{
	
    @FXML
    private TextField cubeNameTextField;

    @FXML
    private TextField dataSeries1;

    @FXML
    private TextField x_axis;

    @FXML
    private TextField y_axis;

    @FXML
    private ComboBox<String> aggregationChooser;

    @FXML
    private TextField whereColumn;

    @FXML
    private TextField whereColumnValue;

    @FXML
    private ToggleGroup group;

    @FXML
    private RadioButton barchart;

    @FXML
    private RadioButton scatterplot;

    @FXML
    private RadioButton linechart;

    @FXML
    private Button runQuery;
    
    @FXML
    private VBox textFieldContainer;

    


    

	public ChartQueryEditorController(AbstractApplication anApp, AbstractController aCallerController, Scene aScene, Stage aStage) {
		super(anApp, aCallerController, aScene, aStage);
		

	}
	
	public ChartQueryEditorController()
	{
		
	}
	
    @FXML
	public void addTextField() {
        // Create a new TextField
    	getTextFieldContainer().setVisible(true);
    	
        HBox newHBox = new HBox(5);
        
        Label additionalLabel = new Label("Additional Filter Column Name:");
        
        TextField additionalName = new TextField();
        
        Label additionalLabelValue = new Label("Additional Filter Column Value:");
        
        TextField additionalValue = new TextField();
        
        newHBox.getChildren().addAll(additionalLabel,additionalName,  additionalLabelValue, additionalValue);

        // Add the new HBox to the VBox
        getTextFieldContainer().getChildren().add(newHBox);
    }


	
	@FXML
	private void handleClose() {
		ExitController x = new client.gui.utils.ExitController(this.stage);
		x.exit();
	}//end handleClose
	
	@FXML
	public void runQuery() throws IOException
	{
		String queryString = constructQuery();
		System.out.println(queryString);
		String chartSpecification = plotSelected();
		
		IChartRequestBuilder chartRequestBuilder = new ChartRequestBuilderImpl();
		ChartRequest chartQueryObject = chartRequestBuilder.build(chartSpecification, queryString);
		
		executeAndDisplayChartQuery(chartQueryObject);
		
//		
	}//end handleClose
	

	

	public int executeAndDisplayChartQuery(ChartRequest chartQueryObject) throws IOException

	{

		ResultFileMetadata resMetadata = null;
		
		IMainEngine serverEngine = this.getApplication().getServer();

		resMetadata = serverEngine.answerCubeQueryFromChartRequest(chartQueryObject);
		displayChart(resMetadata);
//		displayExampleChart();
		return 0;
		
	}
	



	public void displayChart(ResultFileMetadata resMetadata) throws IOException
	{
		Toggle selectedToggle = group.getSelectedToggle();
		String answer = "";
		
		if (selectedToggle != null) {
		    // Cast the selected Toggle to RadioButton
		    RadioButton selectedRadioButton = (RadioButton) selectedToggle;

		    // Now you can check which RadioButton is selected
		    if (selectedRadioButton == getBarchart()) {
		        // User selected barchart
		        createAndDisplayBarChart(resMetadata);
		    } else if (selectedRadioButton == getScatterplot()) {
		        // User selected scatterplot
		        createAndDisplayScatterPlot(resMetadata);
		    } else if (selectedRadioButton == getLinechart()) {
		        // User selected linechart
		        createAndDisplayLineChart(resMetadata);
		    }
		} 
		
	}






	
	

	
	public String downloadResult(String resultFileLocation,  IMainEngine serverEngine) {	
		File remote = new File(resultFileLocation);
		String sep = "\\" + File.separator;	//Java idioms. You need to add the "\\" before!
		
		String[] array = resultFileLocation.split(sep);
		String localName = "";
		if (array.length > 0)
			localName = array[array.length-1].trim();

		localName = "ClientCache" + File.separator + localName;
		File localFile = new File(localName);

		try {
			ClientRMITransferer.download(serverEngine, remote, localFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return localName;
	}
	
	
	public String constructQuery()
	{
		String producedString = "ANALYZE ";
	
		producedString += getMeasureAccordingToAggrFunction(getAggregationChooser().getValue(), getY_axis().getText())
					      + " FROM " + getCubeNameTextField().getText()
					      + " FOR " + getWhereColumn().getText() + "=" + "'" + getWhereColumnValue().getText() + "'" + forStatement()
					      + " GROUP BY " + getDataSeries1().getText() + "," + getX_axis().getText() + " AS first_query";
		return producedString;
		
		
	}

	
	private String getMeasureAccordingToAggrFunction(String aggrFunc, String value) {
		// TODO Auto-generated method stub
		
		return aggrFunc + "(" + value + ")";
	}
	
	private String forStatement()
	{
		String producedForCondition = "";
		
		for(int i=0; i< getTextFieldContainer().getChildren().size(); i++)
		{
			HBox additionalField = (HBox) getTextFieldContainer().getChildren().get(i);
            TextField additionalNameField = (TextField) additionalField.getChildren().get(1);
            TextField additionalValueField = (TextField) additionalField.getChildren().get(3);
            
            String additionalName = additionalNameField.getText();
            String additionalValue = additionalValueField.getText();
            String statement= " AND " + additionalName + "=" + "'" + additionalValue + "'"; 
            producedForCondition +=statement;
		}
		
		return producedForCondition;
	}



	public String plotSelected()
	{
		Toggle selectedToggle = group.getSelectedToggle();
		String answer = "";
		
		if (selectedToggle != null) {
		    // Cast the selected Toggle to RadioButton
		    RadioButton selectedRadioButton = (RadioButton) selectedToggle;

		    // Now you can check which RadioButton is selected
		    if (selectedRadioButton == getBarchart()) {
		        // User selected barchart
		        answer = "Barchart";
		    } else if (selectedRadioButton == getScatterplot()) {
		        // User selected scatterplot
		        answer = "Scatterplot";
		    } else if (selectedRadioButton == getLinechart()) {
		        // User selected linechart
		        answer = "Linechart";
		    }
		} else {
		    // No RadioButton selected
		    answer = null;
		}
		return answer;
	}
	

/*********************************** Chart Display functions  ***********************************************************/	
	
    private void createAndDisplayLineChart(ResultFileMetadata resMetadata) throws IOException {
    	File reportFile = new File(resMetadata.getLocalFolder() + "/" + resMetadata.getResultFile());
    	
        try (BufferedReader br = new BufferedReader(new FileReader(reportFile))) {
            String str;
            int counterSiblings = 0;
            while ((str = br.readLine()) != null) {
            	
                if (str.equals("Type: Base")) {
                	
                	skipLines(br, 3);
                    LineChart<String, Number> lineChart = (LineChart<String, Number>) createChartAccordingToTypeAndTitle("Line","Line Chart\n Type: Base");
                    lineChart.getData().add(readDataFromFile(br));
                    displayChart(lineChart, "Line Chart\n Type: Base");
                    
                } else if (str.equals("Type: Sibling")) {
                    skipLines(br, 6);
                    Map<String, ArrayList<String []>> multiplies;
                    
                    multiplies = returnSeriesDatasets(br);
                    if(multiplies.keySet().size()==1)
                    {
                    	LineChart<String, Number> producedChart = (LineChart<String, Number>) createChartAccordingToTypeAndTitle("Line","Line Chart\n Type: Sibling");
                    	producedChart.getData().add(convertMapToSeries(multiplies));
                    	displayChart(producedChart, "Line Chart\n Type: Sibling");
                    	
                    			
                    }else {
	                	FlowPane pane = new FlowPane();
	                	for(String key: multiplies.keySet())
	                	 {
	                		 LineChart<String, Number> producedChart = (LineChart<String, Number>) createMultipleChartsForGivenTypeAndData(multiplies.get(key), key,"Line");
	                		 pane.getChildren().add(producedChart);
	                	 }
	                	 Scene scene = new Scene(pane, 595, 350);
	                     stage.setTitle("Line Chart");
	                     stage.setScene(scene);
	                     stage.show();
//                    
                    }
                }
            }
        }
        
		
        

    }
    
    private void createAndDisplayBarChart(ResultFileMetadata resMetadata) throws NumberFormatException, IOException {
    	File reportFile = new File(resMetadata.getLocalFolder() + "/" + resMetadata.getResultFile());
        try (BufferedReader br = new BufferedReader(new FileReader(reportFile))) {
            String str;
            int counterSiblings = 0;
            while ((str = br.readLine()) != null) {
            	
            	BarChart<String, Number> barChart = null;
            	String type = null;
            	
                if (str.equals("Type: Base")) {
                	
                	skipLines(br, 3);
                    barChart = (BarChart<String, Number>) createChartAccordingToTypeAndTitle("Bar","Bar Chart\n Type: Base");
                    type = "Base";
                   
                   
                    
                } 
                if(str.equals("Type: Sibling")){
                	
                    skipLines(br, 6);
                    counterSiblings++;
                    barChart = (BarChart<String, Number>) createChartAccordingToTypeAndTitle("Bar","Bar Chart " + counterSiblings + "\n Type: Sibling");
                    type= "Sibling";
                    

                    
                    
                }
                
                if(barChart!= null)
                {
                    Map<String, Map<String, Integer>> data = parseFile(br);
                    
                	for (String seriesName : data.keySet()) {
                        addSeries(barChart, seriesName, data.get(seriesName));
                    }
                        
                    displayChart(barChart, "Bar Chart \n Type: " + type);
                }

                
                
            }
        }
    }
    
    private void createAndDisplayScatterPlot(ResultFileMetadata resMetadata) throws NumberFormatException, IOException {
    	File reportFile = new File(resMetadata.getLocalFolder() + "/" + resMetadata.getResultFile());
    	
        try (BufferedReader br = new BufferedReader(new FileReader(reportFile))) {
            String str;
            int counterSiblings = 0;
            while ((str = br.readLine()) != null) {
            	
                if (str.equals("Type: Base")) {
                	
                	skipLines(br, 3);
                    ScatterChart<String, Number> lineChart = (ScatterChart<String, Number>) createChartAccordingToTypeAndTitle("Scatter","Line Chart\n Type: Base");
                    lineChart.getData().add(readDataFromFile(br));
                    displayChart(lineChart, "Line Chart\n Type: Base");
                    
                } else if (str.equals("Type: Sibling")) {
                    skipLines(br, 6);
                    Map<String, ArrayList<String []>> multiplies;
                    
                    multiplies = returnSeriesDatasets(br);
                    if(multiplies.keySet().size()==1)
                    {
                    	ScatterChart<String, Number> producedChart = (ScatterChart<String, Number>) createChartAccordingToTypeAndTitle("Scatter","Line Chart\n Type: Sibling");
                    	producedChart.getData().add(convertMapToSeries(multiplies));
                    	displayChart(producedChart, "Line Chart\n Type: Sibling");
                    	
                    			
                    }else {
	                	FlowPane pane = new FlowPane();
	                	for(String key: multiplies.keySet())
	                	 {
	                		ScatterChart<String, Number> producedChart = (ScatterChart<String, Number>) createMultipleChartsForGivenTypeAndData(multiplies.get(key), key,"Scatter");
	                		 pane.getChildren().add(producedChart);
	                	 }
	                	 Scene scene = new Scene(pane, 595, 350);
	                     stage.setTitle("Line Chart");
	                     stage.setScene(scene);
	                     stage.show();
//                    
                    }
                }
            }
        }
    }
    
    private XYChart<String, Number> createChartAccordingToTypeAndTitle(String type, String title) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Measure");
        
        if(type.equals("Scatter"))
        {
        	 ScatterChart<String, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        	 scatterChart.setTitle(title);
             return scatterChart;
             
        }else if(type.equals("Bar")){
        	 BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        	 barChart.setTitle(title);
        	 return barChart;
        }else {
        	LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        	lineChart.setTitle(title);
        	return lineChart;
        }
        
       
        
    }
    
    
    private XYChart.Series<String, Number> readDataFromFile(BufferedReader br) throws IOException { //TODO make it read the xValues and and the series and run for them
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String str;
        
        while (!((str = br.readLine()).equals(""))) {
            String[] line = str.split("\t");
          
            int yValue = Integer.parseInt(line[2]);
            String xValue = line[0] + " \t" + line[1];

            series.getData().add(new XYChart.Data<>(xValue, yValue));
        }

        return series;
    }
    
    private XYChart.Series<String, Number> convertMapToSeries( Map<String, ArrayList<String []>> array)
    {
    	
    	XYChart.Series<String, Number> series = new XYChart.Series<>();
    	
    	String grouper1 = array.keySet().iterator().next();
    	ArrayList<String []> grouper2WithMeasure = array.get(grouper1);
    	
    	for(String [] record : grouper2WithMeasure)
    	{
    		int yValue = Integer.parseInt(record[1]);
    		String xValue = record[0];
    		
    		series.getData().add(new XYChart.Data<>(xValue, yValue));
    	}
    	return series;
    	
    }
    
    private void displayChart(XYChart<String, Number> scatterChart, String title) {
        Stage chartStage = new Stage();
        chartStage.setTitle(title);
        chartStage.setScene(new Scene(scatterChart, 800, 600));
        chartStage.show();
    }
    
    private void skipLines(BufferedReader br, int linesToSkip) throws IOException {
        for (int i = 0; i < linesToSkip-1; i++) {
            br.readLine(); // Skip the specified number of lines
        }
    }
   
    private Map<String, Map<String, Integer>> parseFile(BufferedReader br) throws IOException {
        Map<String, Map<String, Integer>> data = new HashMap<>();

        
            String line;
            try {
            	while ((line = br.readLine()) != null && !line.equals("")) {
				    String[] parts = line.split("\t");
				    if (parts.length == 3) {
				        String month = parts[0];
				        String seriesName = parts[1];
				        int measure = Integer.parseInt(parts[2]);

				        data.computeIfAbsent(seriesName, k -> new HashMap<>()).put(month, measure);
				    }
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return data;
    }
    
    private void addSeries(BarChart<String, Number> barChart, String seriesName, Map<String, Integer> seriesData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        List<String> sortedMonths = new ArrayList<>(seriesData.keySet());
        Collections.sort(sortedMonths);
        // Add data to the series based on parsed data
        for (String month : sortedMonths) {
            series.getData().add(new XYChart.Data<>(month, seriesData.get(month)));
        }
        // Add the series to the chart
        barChart.getData().add(series);
    }
    
    
    private Map<String, ArrayList<String []>> returnSeriesDatasets(BufferedReader br) throws IOException
    {
    	
    	Map<String, ArrayList<String []>> seriesDatasets = new HashMap<>();
    	String line;
    	
    	while(!(line = br.readLine()).equals(""))
    	{
    		String[] parts = line.split("\t");
    		String [] arrayGrouperWithMeasure; 
		    if (parts.length == 3) 
		    {
		    	String grouper1 = parts[0];
		    	String grouper2 = parts[1];
		    	String measure = parts[2];
		    	
		    	arrayGrouperWithMeasure = new String[2];
		    	arrayGrouperWithMeasure[0] = grouper1;
		    	arrayGrouperWithMeasure[1] = measure;
		    	
		    	if(seriesDatasets.containsKey(grouper2)) {
		    		seriesDatasets.get(grouper2).add(arrayGrouperWithMeasure);
		    		
		    	}else {
		    		ArrayList<String []> records = new ArrayList<>();
		    		records.add(arrayGrouperWithMeasure);
		    		seriesDatasets.put(grouper2, records);
		    	}
		    }
    	}
		return seriesDatasets;
    	
    }
    

	public XYChart<String, Number> createMultipleChartsForGivenTypeAndData(ArrayList<String []> data, String grouper2,String chartType) {
        
    	CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Measure");
        XYChart<String, Number> chart;
        XYChart.Series<String, Number> series;
        if(chartType.equals("Line"))
        {
        	chart = new LineChart<>(xAxis, yAxis);
        	series = new LineChart.Series<>();
        }else {
        	chart = new ScatterChart<>(xAxis, yAxis);
        	series = new ScatterChart.Series<>();
        }
         
         
        
      //Setting data
        XYChart.Data<String, Number> record;
        for (int i = 0; i < data.size(); i++) {
        	
        	if(chartType.equals("Line"))
        	{
        		record = new LineChart.Data<>(data.get(i)[0], Integer.parseInt(data.get(i)[1]));
        	}else {
        		record = new ScatterChart.Data<>(data.get(i)[0], Integer.parseInt(data.get(i)[1]));
        	}
        	
           
           series.getData().add(record);
        }
        chart.getData().add(series);
        chart.setPrefSize(300, 300);
        chart.setTitle(grouper2);
        
        return chart;
     }

	public TextField getCubeNameTextField() {
		return cubeNameTextField;
	}

	public void setCubeNameTextField(TextField cubeNameTextField) {
		this.cubeNameTextField = cubeNameTextField;
	}

	public TextField getDataSeries1() {
		return dataSeries1;
	}

	public void setDataSeries1(TextField dataSeries1) {
		this.dataSeries1 = dataSeries1;
	}

	public TextField getX_axis() {
		return x_axis;
	}

	public void setX_axis(TextField x_axis) {
		this.x_axis = x_axis;
	}

	public TextField getY_axis() {
		return y_axis;
	}

	public void setY_axis(TextField y_axis) {
		this.y_axis = y_axis;
	}

	public ComboBox<String> getAggregationChooser() {
		return aggregationChooser;
	}

	public void setAggregationChooser(ComboBox<String> aggregationChooser) {
		this.aggregationChooser = aggregationChooser;
	}

	public TextField getWhereColumn() {
		return whereColumn;
	}

	public void setWhereColumn(TextField whereColumn) {
		this.whereColumn = whereColumn;
	}

	public TextField getWhereColumnValue() {
		return whereColumnValue;
	}

	public void setWhereColumnValue(TextField whereColumnValue) {
		this.whereColumnValue = whereColumnValue;
	}

	public VBox getTextFieldContainer() {
		return textFieldContainer;
	}

	public void setTextFieldContainer(VBox textFieldContainer) {
		this.textFieldContainer = textFieldContainer;
	}

	public RadioButton getBarchart() {
		return barchart;
	}

	public void setBarchart(RadioButton barchart) {
		this.barchart = barchart;
	}

	public RadioButton getScatterplot() {
		return scatterplot;
	}

	public void setScatterplot(RadioButton scatterplot) {
		this.scatterplot = scatterplot;
	}

	public RadioButton getLinechart() {
		return linechart;
	}

	public void setLinechart(RadioButton linechart) {
		this.linechart = linechart;
	}

	public void setGroup(ToggleGroup toggleGroup) {
		this.group = toggleGroup;
		
	}

	public ToggleGroup getGroup() {
		// TODO Auto-generated method stub
		return this.group;
	}
    


}
