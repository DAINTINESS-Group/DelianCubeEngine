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
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.javafx.charts.Legend;

import analyze.AnalyzeQuery;
import chartManagement.utils.ChartResponse;
import chartManagement.utils.ChartScoreModel;
import chartManagement.utils.ChartVisModel;
import chartManagement.utils.DataPoint;
import chartRequestManagement.ChartRequestBuilderImpl;
import chartRequestManagement.ChartRequestFactory;
import chartRequestManagement.ChartRequest;
import chartRequestManagement.IChartRequestBuilder;
import client.ClientRMITransferer;

import client.ClientRMITransferer;

import client.gui.application.AbstractApplication;
import client.gui.utils.CustomAlertDialog;
import client.gui.utils.DatastoryReporter;
import client.gui.utils.ExitController;
import client.gui.utils.LauncherForViewControllerPairs;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mainengine.IMainEngine;
import result.ResultFileMetadata;

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

    
    private Stage stage;
    
    private AbstractApplication app;
    
    private Scene scene;

    

	public ChartQueryEditorController(AbstractApplication anApp, AbstractController aCallerController, Scene aScene, Stage aStage) {
		super(anApp, aCallerController, aScene, aStage);
		this.stage = aStage;
		this.app = anApp;
		this.scene = aScene;

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
	public void runQuery() throws Exception
	{
		long startTime = System.nanoTime();
		
		
		ChartRequest chartQueryObject = convertUserInput2ChartRequest();
		long endTime = System.nanoTime();
		double time = endTime - startTime;
		System.out.println("Conversion of User Input to ChartRequest Object time: " + Double.toString(time/1000000) + " ms");
		executeAndDisplayChartQuery(chartQueryObject);
		//long finalTime = System.nanoTime();
		//System.out.println("Conversion of User Input to ChartRequest Object time: " + Double.toString((finalTime-startTime)/1000000) + " ms");
	}//end handleClose
	
	
	public void runExperimentQuery(String region) throws Exception
	{
		long startTime = System.nanoTime();
		String query = "ANALYZE min(amount) FROM loan FOR region ='" + region +  "' GROUP BY district_name,month,status AS first_query";
		String chart = "Barchart";
		ChartRequest chartQueryObject = convertUserInput2ChartRequest(query, chart);
		long endTime = System.nanoTime();
		double time = endTime - startTime;
		System.out.println("Conversion of User Input to ChartRequest Object time: " + Double.toString(time/1000000) + " ms");
		executeAndDisplayChartQuery(chartQueryObject);
		//long finalTime = System.nanoTime();
		//System.out.println("Conversion of User Input to ChartRequest Object time: " + Double.toString((finalTime-startTime)/1000000) + " ms");
	}//end handleClose

	public ChartRequest convertUserInput2ChartRequest() {
		String queryString = constructQuery();
		String chartSpecification = plotSelected();
		ChartRequestFactory chartRequestFactory = new ChartRequestFactory();
		IChartRequestBuilder chartRequestBuilder = chartRequestFactory.createRequest();
		ChartRequest chartQueryObject = chartRequestBuilder.build(chartSpecification, queryString);
		return chartQueryObject;
	}
	
	public ChartRequest convertUserInput2ChartRequest(String query, String chart) {
		String queryString = query;
		String chartSpecification = chart;
		ChartRequestFactory chartRequestFactory = new ChartRequestFactory();
		IChartRequestBuilder chartRequestBuilder = chartRequestFactory.createRequest();
		ChartRequest chartQueryObject = chartRequestBuilder.build(chartSpecification, queryString);
		return chartQueryObject;
	}

	public int executeAndDisplayChartQuery(ChartRequest chartQueryObject) throws Exception

	{

		ResultFileMetadata resMetadata = null;
		
		IMainEngine serverEngine = this.getApplication().getServer();

		//
		ChartResponse chartResponse = serverEngine.answerCubeQueryFromChartRequestAndReturnAsChartResponse(chartQueryObject);
		Boolean readFromFile = false;
		for(ChartVisModel model : chartResponse.getChartVisModels()) {
			if(model.getSeries().size()>1) {
				readFromFile=true;
				break;
			}
		}
		if(readFromFile) {
			
			resMetadata = serverEngine.answerCubeQueryFromChartRequest(chartQueryObject);
			displayChart(resMetadata);
		} else {
			displayChartFromChartResponse(chartResponse);
		}
//		System.out.println("chart response: " + chartResponse.getChartScoreModels().get(9).getScore());
		
		//displayChart(resMetadata);
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
	
	public void displayChartFromChartResponse(ChartResponse chartResponse) {
		Toggle selectedToggle = group.getSelectedToggle();
		String answer = "";
		
		if (selectedToggle != null) {
		    // Cast the selected Toggle to RadioButton
		    RadioButton selectedRadioButton = (RadioButton) selectedToggle;

		    
		    if (selectedRadioButton == getLinechart()) {
		    
		    	createAndDisplayLineChartForChartResponse(chartResponse);
		    	
		    } else if(selectedRadioButton == getScatterplot()) {
		    	
		    	createAndDisplayScatterChartForChartResponse(chartResponse);
		    } else {
		    	
		    	createAndDisplayBarChartForChartResponse(chartResponse);
		    }
		} 
	}


	private void createAndDisplayLineChartForChartResponse(ChartResponse chartResponse) {
    	List<ChartVisModel> visModels = chartResponse.getChartVisModels();
		 for(ChartVisModel visModel : visModels) {
			 String type = visModel.getQueryType();
			 LineChart<String, Number> lineChart = (LineChart<String, Number>) createChartAccordingToTypeAndTitle("Line","Line Chart\n Type: " + type);
			 XYChart.Series<String, Number> series = new XYChart.Series<>();
			 List<DataPoint> points = visModel.getDataPoints();
			 
			 for(DataPoint point: points) {
				 series.getData().add(new XYChart.Data<>(point.getGrouper1(), point.getMeasure()));
			 }
			 
			 lineChart.getData().add(series);
			 
	        // Change the line color to blue
	        series.getNode().setStyle("-fx-stroke: blue;");

	        // Change the point color to blue
	        for (XYChart.Data<String, Number> data : series.getData()) {
	            data.getNode().setStyle("-fx-background-color: blue, white; -fx-background-insets: 0, 2;");
	        }
	        // Customize the legend items
	        Legend legend = (Legend) lineChart.lookup(".chart-legend");
	        if (legend != null) {
	            for (Legend.LegendItem legendItem : legend.getItems()) {
	                Node symbol = legendItem.getSymbol();
	                symbol.setStyle("-fx-background-color: blue, white;");
	            }
	        }
			 
			 displayChart(lineChart, "Line Chart\n Type: " + type, "");
			
		 }
		 List<Data> comparisons = displayComparisonBetweenTheModelsFromChartRequest("Comparison between queries", chartResponse);
		 List<QueryInfoData> queries = displayQueriesInfo("Chart type Info", chartResponse);
		 createDatastory( chartResponse, comparisons, queries);
    }
	
	private void createAndDisplayScatterChartForChartResponse(ChartResponse chartResponse) {
    	List<ChartVisModel> visModels = chartResponse.getChartVisModels();
		 for(ChartVisModel visModel : visModels) {
			 String type = visModel.getQueryType();
			 ScatterChart<String, Number> scatterChart = (ScatterChart<String, Number>) createChartAccordingToTypeAndTitle("Scatter","Scatter Chart\n Type: " + type);
			 XYChart.Series<String, Number> series = new XYChart.Series<>();
			 List<DataPoint> points = visModel.getDataPoints();
			 
			 for(DataPoint point: points) {
				 series.getData().add(new XYChart.Data<>(point.getGrouper1(), point.getMeasure()));
			 }
			 
			 scatterChart.getData().add(series);
		        for (XYChart.Data<String, Number> data : series.getData()) {
		            data.getNode().setStyle("-fx-background-color: blue, white; -fx-background-insets: 0, 2;");
		        }
	        Legend legend = (Legend) scatterChart.lookup(".chart-legend");
	        if (legend != null) {
	            for (Legend.LegendItem legendItem : legend.getItems()) {
	                Node symbol = legendItem.getSymbol();
	                symbol.setStyle("-fx-background-color: blue;");
	            }
	        }
			 displayChart(scatterChart, "Scatter Chart\n Type: " + type,"");
			 
		 }
		 
		 List<Data> comparisons = displayComparisonBetweenTheModelsFromChartRequest("Comparison between queries", chartResponse);
		 List<QueryInfoData> queries = displayQueriesInfo("Chart type Info", chartResponse);
		 createDatastory( chartResponse, comparisons, queries);
		 
		
    }
	
	private void createAndDisplayBarChartForChartResponse(ChartResponse chartResponse) {
		
    	List<ChartVisModel> visModels = chartResponse.getChartVisModels();
		 for(ChartVisModel visModel : visModels) {
			 String type = visModel.getQueryType();
			 BarChart<String, Number> barChart = (BarChart<String, Number>) createChartAccordingToTypeAndTitle("Bar","Bar Chart\n Type: " + type);
			 XYChart.Series<String, Number> series = new XYChart.Series<>();
			 List<DataPoint> points = visModel.getDataPoints();
			 System.out.println("Points size = " + points.size());
			 for(DataPoint point: points) {
				 series.getData().add(new XYChart.Data<>(point.getGrouper1(), point.getMeasure()));
			 }
			 
			 barChart.getData().add(series);
			 for(XYChart.Data<String, Number> data : series.getData()) {
				 data.getNode().setStyle("-fx-bar-fill: blue;");
			 }
	        Legend legend = (Legend) barChart.lookup(".chart-legend");
	        if (legend != null) {
	            for (Legend.LegendItem legendItem : legend.getItems()) {
	                Node symbol = legendItem.getSymbol();
	                symbol.setStyle("-fx-background-color: blue;");
	            }
	        }
			displayChart(barChart, "Bar Chart\n Type: " + type,"");
			
			
		 }
		 List<Data> comparisons = displayComparisonBetweenTheModelsFromChartRequest("Comparison between queries", chartResponse);
		 List<QueryInfoData> queries = displayQueriesInfo("Chart type Info", chartResponse);
		 long startTime = System.nanoTime();
		 createDatastory( chartResponse, comparisons, queries);
         long endTime = System.nanoTime();
 		double time = endTime - startTime;
 		System.out.println("Creation of Datastory time: " + Double.toString(time/1000000) + " ms");
	}


    private List<Data> displayComparisonBetweenTheModelsFromChartRequest(String title, ChartResponse chartResponse) {
    	
    	Stage modelsStage = new Stage();
        modelsStage.setTitle(title);
        TableView<Data> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Data, String> characteristicCol = new TableColumn<>("Model");
        characteristicCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCharacteristic()));
        
        TableColumn<Data, Double> originalCol = new TableColumn<>("Original");
        originalCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOriginal()));
        
        TableColumn<Data, String> commonCol = new TableColumn<>("Common");
        commonCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommon()));

        TableColumn<Data, String> uniqueCol = new TableColumn<>("Unique");
        uniqueCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUnique()));

        table.getColumns().add(characteristicCol);
        table.getColumns().add(originalCol);
        List<ChartVisModel> visModels = chartResponse.getChartVisModels();
        List<String> distinctScoreModels = chartResponse.getChartScoreModels().stream().map(m -> m.getName()).distinct().collect(Collectors.toList());
        List<String> siblings = visModels.stream().filter(m -> !m.getQueryType().equals("Base")).map(m-> m.getQueryType()).collect(Collectors.toList());
      
        for (int i = 0; i < siblings.size(); i++) {
            final int index = i;
            TableColumn<Data, Double> siblingCol = new TableColumn<>(siblings.get(i));
            siblingCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSibling(index)));
            table.getColumns().add(siblingCol);
        }
        
       
        List<Data> results = new ArrayList<>();
        for(String modelName: distinctScoreModels) {
        	
        	
        	List<Double> scoresForSiblings = new ArrayList<>();
        	double original = findScoreForAnalyzeTypeQueryAndScoreModel("Base",modelName,chartResponse.getChartScoreModels());
        	
        	for(String siblingName: siblings) {
        		scoresForSiblings.add(findScoreForAnalyzeTypeQueryAndScoreModel(siblingName,modelName,chartResponse.getChartScoreModels()));
        	}
        	
        	
        	if(isModelUniqueForQueries(original,scoresForSiblings)) {
        		results.add(new Data(modelName, original, scoresForSiblings, "","✓"));
        	} else if(isModelCommonForAllQueries(original,scoresForSiblings)) {
        		results.add(new Data(modelName, original, scoresForSiblings, "✓",""));
        	} else {
        		results.add(new Data(modelName, original, scoresForSiblings, "",""));
        	}
        }
   
        
        table.getColumns().add(commonCol);
        table.getColumns().add(uniqueCol);
        table.getItems().addAll(results);
        
        // Calculate the height for the table
        double rowHeight = 25; // Approximate row height
        double headerHeight = 28; // Approximate header height
        int rowCount = results.size();
        double tableHeight = headerHeight + rowHeight * rowCount;
        
        modelsStage.setScene(new Scene(table, 300, tableHeight));
        modelsStage.show();
        
        return results;
    	
    }
    
    private boolean isModelUniqueForQueries(double original, List<Double> scoresForSiblings) {
		

		List<Double> positiveScoresAndAboveThreshold = scoresForSiblings.stream().filter(sc -> Math.abs(sc) > 0.5).collect(Collectors.toList()); //th=0.5				
		if(scoresForSiblings.size()>1 && Math.abs(original) > 0.5 && positiveScoresAndAboveThreshold.isEmpty()) {
			return true;
		}
		return false;
	}

	private boolean isModelCommonForAllQueries(double original, List<Double> scoresForSiblings) {
		
    	List<Double> allScores = scoresForSiblings;
    	allScores.add(original);
		List<Double> positiveScoresAndAboveThreshold = allScores.stream().filter(sc -> Math.abs(sc) > 0.5).collect(Collectors.toList()); //th=0.5
		if(allScores.size()>1 && positiveScoresAndAboveThreshold.size()==allScores.size()) {
			return true;
		}
		return false;
	}

	private List<QueryInfoData> displayQueriesInfo(String title, ChartResponse chartResponse) {
    	
    	Stage infoStage = new Stage();
    	infoStage.setTitle(title);
    	List<ChartVisModel> visModels = chartResponse.getChartVisModels();
    	TableView<QueryInfoData> table = new TableView<>();
    	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    	TableColumn<QueryInfoData, String> nameCol = new TableColumn<>("Name of Query");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().name);

        TableColumn<QueryInfoData, String> typeCol = new TableColumn<>("Type of Query");
        typeCol.setCellValueFactory(cellData -> cellData.getValue().type);

        TableColumn<QueryInfoData, String> sqlExpressionCol = new TableColumn<>("SQL Expression");
        sqlExpressionCol.setCellValueFactory(cellData -> cellData.getValue().sqlExpression);

        table.getColumns().add(nameCol);
        table.getColumns().add(typeCol);
        table.getColumns().add(sqlExpressionCol);
        
        List<QueryInfoData> queries = visModels.stream().map(m -> new QueryInfoData(m.getQueryType(),extractGeneralType(m.getQueryType()), m.getSqlExpression())).collect(Collectors.toList());
        table.getItems().addAll(queries);
        
        
        infoStage.setScene(new Scene(table, 300, 400));
        infoStage.show();
    	return queries;
    }
	
	private String extractGeneralType(String typeWithNo) {
		if(typeWithNo.contains("Base")) {
			return "Base";
			
		} else if(typeWithNo.contains("Sibling")) {
			return "Sibling";
		}
		
		return typeWithNo;
	}
	
	private double findScoreForAnalyzeTypeQueryAndScoreModel(String analyzeQueryType,String scoreModelName, List<ChartScoreModel> scoreModelsList) {
		
		List<ChartScoreModel> modelsWithSpecifiedModelName = scoreModelsList.stream().filter(m -> m.getName().equals(scoreModelName)).collect(Collectors.toList());
		List<ChartScoreModel> modelsWithSpecifiedAnalyzeType = modelsWithSpecifiedModelName.stream().
																filter(m -> m.getChartVisModel().getQueryType().equals(analyzeQueryType))
																.collect(Collectors.toList());
		
		if(modelsWithSpecifiedAnalyzeType.size()==1) {
			return modelsWithSpecifiedAnalyzeType.get(0).getScore();
		} 
		
		return 0.0;
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
		System.out.println("aggregation function: " + aggrFunc);
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
            if(checkIfEmptyFilterValue(additionalName) && checkIfEmptyFilterValue(additionalValue)) {
            	String statement= " AND " + additionalName + "=" + "'" + additionalValue + "'"; 
                producedForCondition +=statement;
            }
            
		}
		
		return producedForCondition;
	}
	
	private Boolean checkIfEmptyFilterValue(String statement) {
		return statement!=null && statement!="";
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
            int counterSiblings = 1;
            List<ChartModel> chartModels = null;
            while ((str = br.readLine()) != null) {
            	
                if (str.equals("Type: Base")) {
                	
                	skipLines(br, 3);
                    LineChart<String, Number> lineChart = (LineChart<String, Number>) createChartAccordingToTypeAndTitle("Line","Line Chart\n Type: Base");
                    lineChart.getData().add(readDataFromFile(br));
                    String sqlExpression = constructSQLExpressionForBasic(constructQuery());
                    displayChart(lineChart, "Line Chart\n Type: Base\n","Base");
                    
                    
    				
                    
                } else if (str.equals("Type: Sibling")) {
                    skipLines(br, 6);
                    //TODO Na diavazei apo to arxeio ti allaxthke + na pairnei to sql expression tou client kai na ftiaxnei to sql expression
                    Map<String, ArrayList<String []>> multiplies;
                    
                    multiplies = returnSeriesDatasets(br);
                    if(multiplies.keySet().size()==1)
                    {
                    	LineChart<String, Number> producedChart = (LineChart<String, Number>) createChartAccordingToTypeAndTitle("Line","Line Chart\n Type: Sibling");
                    	producedChart.getData().add(convertMapToSeries(multiplies));
                    	displayChart(producedChart, "Line Chart\n Type: Sibling" + counterSiblings,"Sibling" + counterSiblings);
                    	
                    			
                    }else {
	                	FlowPane pane = new FlowPane();
	                	for(String key: multiplies.keySet())
	                	 {
	                		 LineChart<String, Number> producedChart = (LineChart<String, Number>) createMultipleChartsForGivenTypeAndData(multiplies.get(key), key,"Line");
	                		 pane.getChildren().add(producedChart);
	                	 }
	                	 displayChartWithSmallMultiplies(pane,"Line Chart\n Type: Sibling" + counterSiblings,"Sibling" + counterSiblings);
	                	 
//	                	 Scene scene = new Scene(pane, 595, 350);
//	                     stage.setTitle("Line Chart");
//	                     stage.setScene(scene);
//	                     stage.show();
//                    
                    }
                    counterSiblings+=1;
                }
                if(str.equals("Models produced for the charts:")) {
                	chartModels = new ArrayList<>();
                	skipLines(br,2);
                	while((str = br.readLine()) != null) {
                		String [] modelDescriptionMatrix = str.split("\t");
                		if(modelDescriptionMatrix.length==3) {
                			ChartModel chartModel = new ChartModel(modelDescriptionMatrix[0],modelDescriptionMatrix[1],modelDescriptionMatrix[2],  "");
                			chartModels.add(chartModel);
                		}else if(modelDescriptionMatrix.length>3) {
                			StringBuilder sb = new StringBuilder();
                		    for (int i = 2; i < modelDescriptionMatrix.length; i++) {
                		        sb.append(modelDescriptionMatrix[i]);
                		        if (i < modelDescriptionMatrix.length - 1) {
                		            sb.append("\n");
                		        }
                		    }
                		    String score = "";//modelDescriptionMatrix[modelDescriptionMatrix.length - 1];
                		    String parameters = sb.toString();
                		    ChartModel chartModel = new ChartModel(modelDescriptionMatrix[0], modelDescriptionMatrix[1], parameters, score);
                		    
                		    chartModels.add(chartModel);
                		}
                	}
                	displayModels("Models For Chart And Auxiliary Queries",chartModels);
                	createDatastoryForMultipleSeries(chartModels);
                }
            }
            
            
        }

    }
    
    private String constructSQLExpressionForBasic(String constructQuery) {
	// TODO Auto-generated method stub
    String producedString = constructQuery;
	if(producedString.contains("ANALYZE")) {
		producedString.replace("ANALYZE", "SELECT");
	} 
	
	if(producedString.contains(" AS first_query")) {
		producedString.replace(" AS first_query", "");
	}
	
	return producedString;
}

	private void createAndDisplayBarChart(ResultFileMetadata resMetadata) throws NumberFormatException, IOException {
    	File reportFile = new File(resMetadata.getLocalFolder() + "/" + resMetadata.getResultFile());
        try (BufferedReader br = new BufferedReader(new FileReader(reportFile))) {
            String str;
            int counterSiblings = 0;
            List<ChartModel> chartModels = null;
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
                    Map<String, Map<String, Double>> data = parseFile(br);
                    
                	for (String seriesName : data.keySet()) {
                        addSeries(barChart, seriesName, data.get(seriesName));
                    }
                    String filename = (type.equals("Base")) ? "Base" : type+counterSiblings;   
                    displayChart(barChart, "Bar Chart \n Type: " + type,filename);
                }
                
                if(str.equals("Models produced for the charts:")) {
                	chartModels = new ArrayList<>();
                	skipLines(br,2);
                	while((str = br.readLine()) != null) {
                		String [] modelDescriptionMatrix = str.split("\t");
                		if(modelDescriptionMatrix.length==3) {
                			ChartModel chartModel = new ChartModel(modelDescriptionMatrix[0],modelDescriptionMatrix[1],modelDescriptionMatrix[2], "");
                			chartModels.add(chartModel);
                		}else if(modelDescriptionMatrix.length>3) {
                			StringBuilder sb = new StringBuilder();
                		    for (int i = 2; i < modelDescriptionMatrix.length-1; i++) {
                		        sb.append(modelDescriptionMatrix[i]);
                		        if (i < modelDescriptionMatrix.length - 1) {
                		            sb.append("\n");
                		        }
                		    }
                		    String score = "";//modelDescriptionMatrix[modelDescriptionMatrix.length - 1];
                		    String parameters = sb.toString();
                		    ChartModel chartModel = new ChartModel(modelDescriptionMatrix[0], modelDescriptionMatrix[1], parameters, score);
                		    chartModels.add(chartModel);
                		}
                	}
                }
                
                
            }
            
            displayModels("Models For Chart And Auxiliary Queries",chartModels);
            long startTime = System.nanoTime();
            createDatastoryForMultipleSeries(chartModels);
            long endTime = System.nanoTime();
    		double time = endTime - startTime;
    		System.out.println("Creation of Datastory time: " + Double.toString(time/1000000) + " ms");
            
            
        }
    }
    
    private void createAndDisplayScatterPlot(ResultFileMetadata resMetadata) throws NumberFormatException, IOException {
    	File reportFile = new File(resMetadata.getLocalFolder() + "/" + resMetadata.getResultFile());
    	
        try (BufferedReader br = new BufferedReader(new FileReader(reportFile))) {
            String str;
            int counterSiblings = 1;
            List<ChartModel> chartModels = null;
            while ((str = br.readLine()) != null) {
            	
                if (str.equals("Type: Base")) {
                	
                	skipLines(br, 3);
                    ScatterChart<String, Number> lineChart = (ScatterChart<String, Number>) createChartAccordingToTypeAndTitle("Scatter","Line Chart\n Type: Base");
                    lineChart.getData().add(readDataFromFile(br));
                    displayChart(lineChart, "Line Chart\n Type: Base","Base");
                    
                } else if (str.equals("Type: Sibling")) {
                    skipLines(br, 6);
                    Map<String, ArrayList<String []>> multiplies;
                    
                    multiplies = returnSeriesDatasets(br);
                    if(multiplies.keySet().size()==1)
                    {
                    	ScatterChart<String, Number> producedChart = (ScatterChart<String, Number>) createChartAccordingToTypeAndTitle("Scatter","Line Chart\n Type: Sibling");
                    	producedChart.getData().add(convertMapToSeries(multiplies));
                    	displayChart(producedChart, "Scatterplot \n Type: Sibling", "Sibling" + counterSiblings);
                    	
                    			
                    }else {
	                	FlowPane pane = new FlowPane();
	                	for(String key: multiplies.keySet())
	                	 {
	                		ScatterChart<String, Number> producedChart = (ScatterChart<String, Number>) createMultipleChartsForGivenTypeAndData(multiplies.get(key), key,"Scatter");
	                		 pane.getChildren().add(producedChart);
	                	 }
	                	 displayChartWithSmallMultiplies(pane, "Scatterplot \n Type: Sibling", "Sibling" + counterSiblings);
	                	 
//	                	 Scene scene = new Scene(pane, 595, 350);
//	                     stage.setTitle("Line Chart");
//	                     stage.setScene(scene);
//	                     stage.show();
//                    
                    }
                    counterSiblings+=1;
                }
                if(str.equals("Models produced for the charts:")) {
                	chartModels = new ArrayList<>();
                	skipLines(br,2);
//                	int counterOfSiblings = 1;
                	while((str = br.readLine()) != null) {
                		String [] modelDescriptionMatrix = str.split("\t");
                		if(modelDescriptionMatrix.length==3) {
                			ChartModel chartModel = new ChartModel(modelDescriptionMatrix[0],modelDescriptionMatrix[1],modelDescriptionMatrix[2], "");
//                			chartModel.setNo(String.valueOf(counterOfSiblings));
                			chartModels.add(chartModel);
                		}else if(modelDescriptionMatrix.length>3) {
                			StringBuilder sb = new StringBuilder();
                		    for (int i = 2; i < modelDescriptionMatrix.length-1; i++) {
                		    	
                		        sb.append(modelDescriptionMatrix[i]);
                		        if (i < modelDescriptionMatrix.length - 1) {
                		            sb.append("\n");
                		        }
                		    } 
                		    String score = "";//modelDescriptionMatrix[modelDescriptionMatrix.length - 1];
                		    String parameters = sb.toString();
                		    ChartModel chartModel = new ChartModel(modelDescriptionMatrix[0], modelDescriptionMatrix[1], parameters, score);
//                		    chartModel.setNo(String.valueOf(counterOfSiblings));
                		    chartModels.add(chartModel);
                		    
                		}
//                		counterOfSiblings +=1;
                	}
                	displayModels("Models For Chart And Auxiliary Queries",chartModels);
                	createDatastoryForMultipleSeries(chartModels);
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
          
            double yValue = Double.parseDouble(line[2]);
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
    		double yValue = Double.parseDouble(record[1]);
    		String xValue = record[0];
    		
    		series.getData().add(new XYChart.Data<>(xValue, yValue));
    	}
    	return series;
    	
    }
    
    private void displayModels(String title, List<ChartModel> lista) {
//    	Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle("Information");
//        alert.setHeaderText(null);
//        alert.setContentText("Model execution is not currently supported");
//
//        
//        alert.showAndWait();
    	Stage modelsStage = new Stage();
        modelsStage.setTitle(title);

        // Create table view
        TableView<ChartModel> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Define columns
        TableColumn<ChartModel, String> column1 = new TableColumn<>("Model");
        column1.setCellValueFactory(new PropertyValueFactory<>("model"));
        column1.setPrefWidth(12);

        TableColumn<ChartModel, String> column2 = new TableColumn<>("Type");
        column2.setCellValueFactory(new PropertyValueFactory<>("typeQuery"));
        column2.setPrefWidth(10);

        TableColumn<ChartModel, String> column3 = new TableColumn<>("Result");
        column3.setCellValueFactory(new PropertyValueFactory<>("result"));
        
//        TableColumn<ChartModel, String> column4 = new TableColumn<>("Score");
//        column4.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        tableView.getColumns().addAll(column1, column2,column3);
        
        List<ChartModel> chartModelsProduced = lista;
        
        for (int i = 0; i < lista.size(); i++) {
            tableView.getItems().add(lista.get(i));
        }

        // Set scene
        modelsStage.setScene(new Scene(tableView, 300, 600));
        modelsStage.show();
        //displayComparisonBetweenTheModels("Test", lista);
    }
    
    private void displayComparisonBetweenTheModels(String title, List<ChartModel> lista) {
    	
    	Stage modelsStage = new Stage();
        modelsStage.setTitle(title);
        TableView<ChartModel> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<ChartModel, String> column1 = new TableColumn<>("Model");
        column1.setCellValueFactory(new PropertyValueFactory<>("model"));
        column1.setPrefWidth(12);
        tableView.getColumns().add(column1);
        List<String> labels = lista.stream().map(model -> model.getTypeQuery()).distinct().collect(Collectors.toList());
        for(String label : labels) {
        	
        	TableColumn<ChartModel, String> column = new TableColumn<>(label);
            column.setCellValueFactory(new PropertyValueFactory<>("typeQuery"));
            column.setPrefWidth(12);
            tableView.getColumns().add(column);
        }
        TableColumn<ChartModel, String> column = new TableColumn<>("Uniqueness");
        column.setCellValueFactory(new PropertyValueFactory<>(""));
        column.setPrefWidth(12);
        tableView.getColumns().add(column);
        TableColumn<ChartModel, String> extraColumn = new TableColumn<>("Commoness");
        extraColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        extraColumn.setPrefWidth(12);
        tableView.getColumns().add(extraColumn);
        
        modelsStage.setScene(new Scene(tableView, 300, 600));
        modelsStage.show();
    	
    }
    
    private void displayChart(XYChart<String, Number> typeChart, String title,String filename) {
  
        Stage chartStage = new Stage();
        chartStage.setTitle(title);
        chartStage.setScene(new Scene(typeChart, 800, 600));
        chartStage.show();
        
        if(filename.equals("")) {
        	String name = title.split(":")[1].trim();
        	saveChartAsPNG(chartStage,name);
        } else {
        	saveChartAsPNG(chartStage,filename);
        }

        
    }
    
    private void displayChartWithSmallMultiplies(FlowPane pane,String title, String filename) {
    	
	 Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
     Scene scene = new Scene(pane, screenBounds.getWidth(), screenBounds.getHeight());
     stage.setTitle(title);
     stage.setScene(scene);
     stage.show();
     saveChartAsPNG(stage,filename);
     
    }
    
    private void createDatastory(ChartResponse chartResponse, List<Data> comparisons, List<QueryInfoData> queries) {
    	
        DatastoryReporter reporter = new DatastoryReporter();
        String imagesPath = "ChartImages/";
        String outputFolderPath = "OutputFiles/";
        
        reporter.addTitle("Chart Queries Report: ");
        reporter.addH2("The results for the original query and auxiliary queries are summarized below.");
        for(ChartVisModel model: chartResponse.getChartVisModels()) {
        	
        	
        	List<ChartScoreModel> mathModels = chartResponse.getChartScoreModels().stream()
        										.filter($ -> $.getChartVisModel().equals(model)).collect(Collectors.toList());
        	List<String> modelResults = mathModels.stream().map($-> $.getResult()).collect(Collectors.toList());
            String sumResult = "<ul>";
            for(String res: modelResults ) {
                sumResult += "<li>" + res + "</li>";
            }
            sumResult += "</ul>";
            reporter.addImageWithText(imagesPath  + model.getQueryType() + ".png",model.getQueryType()+ "Image", 
                    "<div class='container'><div><strong>Query with name: </strong>" + model.getQueryType() + "<br>" 
                    + "<strong>SQL expression: </strong><br>" + model.getSqlExpression() + "<br>"
                    + "<strong>Extracted phenomena: </strong>" + sumResult + "</div></div>");
        }
        reporter.addH2("Comparison between original query and sibling queries:");
        reporter.addText(summarizePhenomenaForAllQueries(chartResponse,comparisons));
        reporter.finalizeReport();
        reporter.saveReport(outputFolderPath + "reporter.html");
    }
    
    private void createDatastoryForMultipleSeries(List<ChartModel> chartModels) {
    	 DatastoryReporter reporter = new DatastoryReporter();
         String imagesPath = "ChartImages/";
         String outputFolderPath = "OutputFiles/";
         List<String> queries = chartModels.stream().map(query -> query.getTypeQuery()).distinct().collect(Collectors.toList());
         
         reporter.addTitle("Chart Queries Report: ");
         reporter.addH2("The results for the original query and auxiliary queries are summarized below.");
         for(String query : queries) {
        	 Map<String, String> mathModels = chartModels.stream().filter(q -> q.getTypeQuery().equals(query)).collect(Collectors.toMap(ChartModel::getModel, ChartModel::getResult));
        	 String sumResults = "<ul>";
        	 for (Map.Entry<String, String> entry : mathModels.entrySet()) {
        		    sumResults += "<li>" + entry.getValue() + "</li>";
        	 }
             sumResults += "</ul>";
             reporter.addImageWithText(imagesPath  + query + ".png",query+ "Image", 
                     "<div class='container'><div><strong>Query with name: </strong>" + query + "<br>" 
                     + "<strong>Extracted phenomena: </strong>" + sumResults + "</div></div>");
             reporter.finalizeReport();
             reporter.saveReport(outputFolderPath + "reporter.html");
         }
         
    }
    
    private String summarizePhenomenaForAllQueries(ChartResponse chartResponse, List<Data> comparisons) {
    	String producedSummary = "";
    	String moreSpecifically = " More specifically, ";
    	String also = " Also, ";
    	String finallyStatement = " Finally, ";
    	String both = "Both";
    	String furthermore = " Furthermore ,";
    	List<String> uniqueModels = comparisons.stream().filter(data -> data.getUnique().equals("✓")).map(data -> data.getCharacteristic()).collect(Collectors.toList());
    	List<String> commonModels = comparisons.stream().filter(data -> data.getCommon().equals("✓")).map(data -> data.getCharacteristic()).collect(Collectors.toList());
    	if (uniqueModels.isEmpty()) {
    		producedSummary +="Original query has not a unique Model in comparison with it's siblings. <br>";
    	} else {
    		switch(uniqueModels.size()) {
    			case 1:
    				producedSummary += "Original query has a " + uniqueModels.get(0) + " in comparison with it's siblings.";
    				producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",uniqueModels.get(0));
    				producedSummary+= "<br>";
    				break;
    			case 2:	
    				producedSummary += "Original query has a " + uniqueModels.get(0) + " in comparison with it's siblings.";
    				producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",uniqueModels.get(0));
    				producedSummary += also + "Original query has a " + uniqueModels.get(1) + " in comparison with it's siblings.";
    				producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",uniqueModels.get(1));
    				producedSummary+= "<br>";
    				break;
    				
    			default:
    				break;
    		}
    		if(uniqueModels.size()>=3) {
    			producedSummary += "Original query has a " + uniqueModels.get(0) + " in comparison with it's siblings.";
				producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",uniqueModels.get(0));
				for (int i=1; i<uniqueModels.size()-1; i++) {
					producedSummary += "Original query has a " + uniqueModels.get(i) + " in comparison with it's siblings.";
					producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",uniqueModels.get(i));
				}
				producedSummary += "Original query has a " + uniqueModels.get(uniqueModels.size()-1) + " in comparison with it's siblings.";
				producedSummary += finallyStatement + takeStringResultForScoreModel(chartResponse,"Base",uniqueModels.get(uniqueModels.size()-1));
				producedSummary+= "<br>";
    		}
    			
    	}
    	if(commonModels.isEmpty()) {
    		producedSummary +="Original query has not any common model phenomena with it's siblings. <br>";
    	} else {
    		switch(commonModels.size()) {
			case 1:
				producedSummary += both + "Original query and it's siblings contain " + commonModels.get(0);
				//producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",commonModels.get(0));
				producedSummary+= "<br>";
				break;
			case 2:	
				producedSummary += both +"Original query and it's siblings contain " + commonModels.get(0);
				//producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",commonModels.get(0));
				producedSummary += furthermore + "all queries contain " + commonModels.get(1);
				//producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",commonModels.get(1));
				producedSummary+= "<br>";
				break;
				
			default:
				break;
		}
		if(commonModels.size()>=3) {
			producedSummary += both + "Original query and it's siblings contain " + commonModels.get(0);
			//producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",commonModels.get(0));
			for (int i=1; i<commonModels.size()-1; i++) {
				producedSummary += furthermore + "all queries contain " + commonModels.get(i);
				//producedSummary += "Original query has a " + commonModels.get(i) + " in comparison with it's siblings.";
				//producedSummary += moreSpecifically + takeStringResultForScoreModel(chartResponse,"Base",commonModels.get(i));
			}
			producedSummary += finallyStatement + "all queries contain have " + commonModels.get(commonModels.size()-1);
			//producedSummary += finallyStatement + takeStringResultForScoreModel(chartResponse,"Base",commonModels.get(commonModels.size()-1));
			producedSummary+= "<br>";
		}
    	}
    	
    	return producedSummary;
    	
    }
    
    private String takeStringResultForScoreModel(ChartResponse chartResponse, String nameQuery, String modelName) {
    	String result = "";
    	List<ChartScoreModel> scoreModelsWithRequestedModelName = chartResponse.getChartScoreModels().stream().filter(m -> m.getName().equals(modelName)).collect(Collectors.toList()); 
    	List<ChartScoreModel> scoreModelsWithRequestedModelNameAndNameQuery = scoreModelsWithRequestedModelName.stream().filter(m -> m.getChartVisModel().getQueryType().equals(nameQuery)).collect(Collectors.toList());
    	
    	if(scoreModelsWithRequestedModelNameAndNameQuery.size()==1) {
    		result = scoreModelsWithRequestedModelNameAndNameQuery.get(0).getResult();
    	}
    	return result;
    }
    
    private void saveChartAsPNG(Stage stage,String filename) {
    	
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Scene scene = stage.getScene();
                WritableImage image = scene.snapshot(null);

                // Save the snapshot to a file
                File file = new File("OutputFiles/ChartImages/" + filename + ".png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stop();
            }
        };

        timer.start();
    }
    
    private void saveTableAsPNG(TableView<?> tableView, String filename) {
    	WritableImage snapshot = tableView.snapshot(new SnapshotParameters(), null);
        File file = new File("OutputFiles/ChartImages/" + filename + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            System.out.println("Snapshot saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void skipLines(BufferedReader br, int linesToSkip) throws IOException {
        for (int i = 0; i < linesToSkip-1; i++) {
            br.readLine(); // Skip the specified number of lines
        }
    }
   
    private Map<String, Map<String, Double>> parseFile(BufferedReader br) throws IOException {
        Map<String, Map<String, Double>> data = new HashMap<>();

        
            String line;
            try {
            	while ((line = br.readLine()) != null && !line.equals("")) {
				    String[] parts = line.split("\t");
				    if (parts.length == 3) {
				        String month = parts[0];
				        String seriesName = parts[1];
				        double measure = Double.parseDouble(parts[2]);

				        data.computeIfAbsent(seriesName, k -> new HashMap<>()).put(month, measure);
				    }
				}
			} catch (NumberFormatException e) {
			
				e.printStackTrace();
			}
        return data;
    }
    
    private void addSeries(BarChart<String, Number> barChart, String seriesName, Map<String, Double> map) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        List<String> sortedMonths = new ArrayList<>(map.keySet());
        Collections.sort(sortedMonths);
        // Add data to the series based on parsed data
        for (String month : sortedMonths) {
            series.getData().add(new XYChart.Data<>(month, map.get(month)));
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
        		record = new LineChart.Data<>(data.get(i)[0], Double.parseDouble(data.get(i)[1]));
        	}else {
        		record = new ScatterChart.Data<>(data.get(i)[0], Double.parseDouble(data.get(i)[1]));
        	}
        	
           
           series.getData().add(record);
        }
        
        chart.getData().add(series);
        chart.setPrefSize(350, 350);
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
    
	/*--------------------------------*/
	public static class ChartModel {
		
		
		private String model;
		
		private String typeQuery;
		
		private String result;
		
		private String score;
		
		private String No;
		
		private String expression;
		
		
		public ChartModel(String aModel, String aTypeQuery,String  aResult, String ascore) {
			this.model = aModel;
			this.typeQuery = aTypeQuery;
			this.result = aResult;
			this.score = ascore;
		}
		
		
		public String getModel() {
	        return model;
	    }

	    public void setModel(String model) {
	        this.model = model;
	    }

	    public String getTypeQuery() {
	        return typeQuery;
	    }

	    public void setTypeQuery(String typeQuery) {
	        this.typeQuery = typeQuery;
	    }

	    public String getResult() {
	        return result;
	    }

	    public void setResult(String result) {
	        this.result = result;
	    }

	    public String getScore() {
	    	return score;
	    }
	    
	    public void setScore(String score) {
	    	this.score = score;
	    }
	    
	    public String getNo() {
	    	return No;
	    }
	    
	    public void setNo(String no) {
	    	this.No = no;
	    }
		
	    public String getExpression() {
	    	return expression;
	    }
	    
	    public void setExpression(String exp) {
	    	this.expression = exp;
	    }

	}
	

	public static class Data {
        private final String characteristic;
        private final Double original;
        private final List<Double> siblings;
        private final String common;
        private final String unique;

        public Data(String characteristic, Double original, List<Double> siblings, String common, String unique) {
            this.characteristic = characteristic;
            this.original = original;
            this.siblings = siblings;
            this.common = common;
            this.unique = unique;
        }

        public String getCharacteristic() {
            return characteristic;
        }

        public Double getOriginal() {
            return original;
        }

        public List<Double> getSiblings() {
            return siblings;
        }

        public String getCommon() {
            return common;
        }

        public String getUnique() {
            return unique;
        }

        public Double getSibling(int index) {
            if (index < siblings.size()) {
                return siblings.get(index);
            }
            return null;
        }
    }
	
    public static class QueryInfoData {
        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty sqlExpression;

        public QueryInfoData(String name, String type, String sqlExpression) {
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.sqlExpression = new SimpleStringProperty(sqlExpression);
        }

        public String getName() {
            return name.get();
        }

        public String getType() {
            return type.get();
        }

        public String getSqlExpression() {
            return sqlExpression.get();
        }
    }



}
