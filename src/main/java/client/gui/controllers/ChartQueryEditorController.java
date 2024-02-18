package client.gui.controllers;


import java.awt.Checkbox;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
	
    @FXML
    private void addTextField() {
        // Create a new TextField
    	textFieldContainer.setVisible(true);
    	
        HBox newHBox = new HBox(5);
        
        Label additionalLabel = new Label("Additional Filter Column Name:");
        
        TextField additionalName = new TextField();
        
        Label additionalLabelValue = new Label("Additional Filter Column Value:");
        
        TextField additionalValue = new TextField();
        
        newHBox.getChildren().addAll(additionalLabel,additionalName,  additionalLabelValue, additionalValue);

        // Add the new HBox to the VBox
        textFieldContainer.getChildren().add(newHBox);
    }


	
	@FXML
	private void handleClose() {
		ExitController x = new client.gui.utils.ExitController(this.stage);
		x.exit();
	}//end handleClose
	
	@FXML
	private void runQuery() throws IOException
	{
		String queryString = constructQuery();
		System.out.println(queryString);
		//queryString = "ANALYZE MIN(amount) FROM loan FOR region='Prague' AND year='1998' GROUP BY district_name,month AS first_query";
		String chartSpecification = plotSelected();
		//System.out.println(queryString);
		//System.out.println(plotSelected());
		//construct a builder that will build the chart request
		IChartRequestBuilder chartRequestBuilder = new ChartRequestBuilderImpl();
		ChartRequest chartQueryObject = chartRequestBuilder.build(chartSpecification, queryString);
		
		executeAndDisplayChartQuery(chartQueryObject);
		
//		
	}//end handleClose
	

	

	public int executeAndDisplayChartQuery(ChartRequest chartQueryObject) throws IOException

	{
		String remoteResultFileLocation = null;
		String remoteInfoFileLocation = null;
		String remoteFolderName = null;
		
		String localFileLocation;
		String localInfoFileLocation;
		
		int result;
		ResultFileMetadata resMetadata = null;
		
		IMainEngine serverEngine = this.getApplication().getServer();

		resMetadata = serverEngine.answerCubeQueryFromChartRequest(chartQueryObject);
		displayChart(resMetadata);
//		displayExampleChart();
		return 0;
		
	}
	
	public void displayExampleChart() {
		// TODO Auto-generated method stub
		String[] cities = {"London", "Athens", "Frankfurt"};
        int[] values = {1, 2, 3, 4, 5};

        // Create X and Y axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        for (int i = 0; i < cities.length; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(cities[i]);

            // Add data to the series
            for (int j = 0; j < values.length; j++) {
                series.getData().add(new XYChart.Data<>(String.valueOf(values[j]), getDataForCity(i, j)));
            }

            // Add the series to the chart
            barChart.getData().add(series);
        }
        
        Stage chartStage = new Stage();
        chartStage.setTitle("test");
        chartStage.setScene(new Scene(barChart, 800, 600));
        chartStage.show();
		
	}
	
    // Replace this method with your actual data retrieval logic for each city
    private double getDataForCity(int cityIndex, int valueIndex) {
        // Replace this with your actual data retrieval logic
        return Math.random() * 10;
    }


	public void displayChart(ResultFileMetadata resMetadata) throws IOException
	{
		Toggle selectedToggle = group.getSelectedToggle();
		String answer = "";
		
		if (selectedToggle != null) {
		    // Cast the selected Toggle to RadioButton
		    RadioButton selectedRadioButton = (RadioButton) selectedToggle;

		    // Now you can check which RadioButton is selected
		    if (selectedRadioButton == barchart) {
		        // User selected barchart
		        createAndDisplayBarChart(resMetadata);
		    } else if (selectedRadioButton == scatterplot) {
		        // User selected scatterplot
		        createAndDisplayScatterPlot(resMetadata);
		    } else if (selectedRadioButton == linechart) {
		        // User selected linechart
		        createAndDisplayLineChart(resMetadata);
		    }
		} 
		
	}

	@FXML
    private void listColumns() {
        String cubeName = cubeNameTextField.getText();
        
        // Call a service method to get available columns based on cubeName
        
        IMainEngine serverEngine = this.getApplication().getServer();		
        List<String> availableColumns = null;//serverEngine.getAvailableColumnNamesFromCubeName(cubeName);
        		
        // Display the available columns in a new window or dialog
        showAvailableColumnsDialog(availableColumns);
    }
	
	



	private void showAvailableColumnsDialog(List<String> availableColumns) {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Available Columns");
        alert.setHeaderText(null);
        
        // Build the content of the dialog based on availableColumns
        StringBuilder contentText = new StringBuilder();
        for (String column : availableColumns) {
            contentText.append(column).append("\n");
        }

        alert.setContentText(contentText.toString());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        alert.showAndWait();
    	
	}





	
	private int displayResultInDataWindow(String resultFileLocation) {
		DataWindowController controller = new DataWindowController(resultFileLocation);
		VBox dwLayout = null;
		int launchResult = -100;
		
		LauncherForViewControllerPairs launcher = new LauncherForViewControllerPairs();
		launchResult = launcher.launchViewControllerPairNoFXController(this.getApplication(), this, this.getStage(), true, 
				"DataWindow.fxml", controller, dwLayout);
		controller.autoloadFile();
		
		return launchResult;
	}//end method displayResultInDataWindow
	
	

	
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
	}//end method
	
	
	public String constructQuery()
	{
		String producedString = "ANALYZE ";
	
		producedString += getMeasureAccordingToAggrFunction(aggregationChooser.getValue(), y_axis.getText())
					      + " FROM " + cubeNameTextField.getText()
					      + " FOR " + whereColumn.getText() + "=" + "'" + whereColumnValue.getText() + "'" + forStatement()
					      + " GROUP BY " + dataSeries1.getText() + "," + x_axis.getText() + " AS first_query";
		return producedString;
		
		
	}

	
	private String getMeasureAccordingToAggrFunction(String aggrFunc, String value) {
		// TODO Auto-generated method stub
		
		return aggrFunc + "(" + value + ")";
	}
	
	public String forStatement()
	{
		String producedForCondition = "";
		
		for(int i=0; i< textFieldContainer.getChildren().size(); i++)
		{
			HBox additionalField = (HBox) textFieldContainer.getChildren().get(i);
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
		    if (selectedRadioButton == barchart) {
		        // User selected barchart
		        answer = "Barchart";
		    } else if (selectedRadioButton == scatterplot) {
		        // User selected scatterplot
		        answer = "Scatterplot";
		    } else if (selectedRadioButton == linechart) {
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
                    LineChart<String, Number> scatterChart = (LineChart<String, Number>) createChartAccordingToTypeAndTitle("Line","Line Chart\n Type: Base");
                    scatterChart.getData().add(readDataFromFile(br));
                    displayChart(scatterChart, "Line Chart\n Type: Base");
                    
                } else if (str.equals("Type: Sibling")) {
                    skipLines(br, 6);
                    counterSiblings++;
                    LineChart<String, Number> scatterChart = (LineChart<String, Number>) createChartAccordingToTypeAndTitle("Line","Line Chart " + counterSiblings + "\n Type: Sibling");
                    scatterChart.getData().add(readDataFromFile(br));
                    displayChart(scatterChart, "Line Chart \n Type: Sibling");
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
                if (str.equals("Type: Base")) {
                	
                	skipLines(br, 3);
                    BarChart<String, Number> barChart = (BarChart<String, Number>) createChartAccordingToTypeAndTitle("Bar","Bar Chart\n Type: Base");
                    barChart.getData().add(readDataFromFile(br));
                    displayChart(barChart, "Bar Chart\n Type: Base");
                    
                } else if (str.equals("Type: Sibling")) {
                    skipLines(br, 6);
                    counterSiblings++;
                    BarChart<String, Number> barChart = (BarChart<String, Number>) createChartAccordingToTypeAndTitle("Bar","Bar Chart " + counterSiblings + "\n Type: Sibling");
                    barChart.getData().add(readDataFromFile(br));
                    displayChart(barChart, "Bar Chart \n Type: Sibling");
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
                    ScatterChart<String, Number> scatterChart = (ScatterChart<String, Number>) createChartAccordingToTypeAndTitle("Scatter","Scatter Chart\n Type: Base");
                    scatterChart.getData().add(readDataFromFile(br));
                    displayChart(scatterChart, "Scatter Chart\n Type: Base");
                    
                } else if (str.equals("Type: Sibling")) {
                    skipLines(br, 6);
                    counterSiblings++;
                    ScatterChart<String, Number> scatterChart = (ScatterChart<String, Number>) createChartAccordingToTypeAndTitle("Scatter","Scatter Chart " + counterSiblings + "\n Type: Sibling");
                    scatterChart.getData().add(readDataFromFile(br));
                    displayChart(scatterChart, "Scatter Chart \n Type: Sibling");
                    
                }
            }
        }
    }
    
    private XYChart<String, Number> createChartAccordingToTypeAndTitle(String type, String title) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
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
    
    private XYChart.Series<String, Number> readDataFromFile(BufferedReader br) throws IOException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String str;

        while (!((str = br.readLine()).equals(""))) {
            String[] line = str.split("\t");
            System.out.println(str);
            int yValue = Integer.parseInt(line[2]);
            String xValue = line[0] + " \t" + line[1];

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
        for (int i = 0; i < linesToSkip; i++) {
            br.readLine(); // Skip the specified number of lines
        }
    }
   

}
