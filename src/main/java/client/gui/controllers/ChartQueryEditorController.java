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
import javax.swing.filechooser.FileNameExtensionFilter;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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
    
    
	
	public ChartQueryEditorController(AbstractApplication anApp, AbstractController aCallerController, Scene aScene, Stage aStage) {
		super(anApp, aCallerController, aScene, aStage);
		

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
		String chartSpecification = "Barchart";//plotSelected();
		//System.out.println(queryString);
		//System.out.println(plotSelected());
		//construct a builder that will build the chart request
		IChartRequestBuilder chartRequestBuilder = new ChartRequestBuilderImpl();
		ChartRequest chartQueryObject = chartRequestBuilder.build(chartSpecification, queryString);
		
		int result = executeAndDisplayChartQuery(chartQueryObject);
		
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

		//resMetadata = serverEngine.answerCubeQueryFromChartQueryObjectWithMetadata(chartQueryObject);
		try {
			
			resMetadata = serverEngine.answerCubeQueryFromChartRequest(chartQueryObject);
			if(resMetadata != null) {
				remoteResultFileLocation = resMetadata.getResultFile();
				//remoteInfoFileLocation = resMetadata.getResultInfoFile();
				remoteFolderName = resMetadata.getLocalFolder();
			
				System.out.println("Remote _info_ file FOLDER: " + remoteFolderName);
				System.out.println("Remote result file METADATA: " + remoteResultFileLocation);
				//System.out.println("Remote _info_ file METADATA: " + remoteInfoFileLocation);
			}
			else {
				System.out.println("Remote METADATA: NULL" );
				return -1;
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(remoteResultFileLocation.length() == 0) {	
			CustomAlertDialog a = new CustomAlertDialog("Invalid query, no result", null, "The query did not return any result", this.stage); 
			a.show();
			return -1;
		}
		else {
			//localFileLocation = downloadResult(remoteResultFileLocation, serverEngine);
			fetchData(remoteFolderName, remoteResultFileLocation);
			result = 0;//displayResultInDataWindow(localFileLocation);
//			OLD WAY OF GETTING INFO FILE
//			FileInfoProvider infoProvider = new FileInfoProvider(remoreResultFileLocation);
//			infoFileLocation = infoProvider.getNameForInfoFile(remoreResultFileLocation);    //getInfoFileAbsoluteLocation();
//			System.out.println("Remote result file: " + remoreResultFileLocation);
//			System.out.println("Remote _info_ file: " + remoteInfoFileLocation);

			//localInfoFileLocation = downloadResult(remoteInfoFileLocation, serverEngine);
	
		}
		return result;
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
					      + " FOR " + whereColumn.getText() + "=" + "'" + whereColumnValue.getText() + "'"
					      + " GROUP BY " + dataSeries1.getText() + "," + x_axis.getText() + " AS first_query";
		return producedString;
		
		
	}

	
	private String getMeasureAccordingToAggrFunction(String aggrFunc, String value) {
		// TODO Auto-generated method stub
		
		return aggrFunc + "(" + value + ")";
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
	
	public static void fetchData(String localFolder,String resultFile) throws IOException {
		String str;
		File reportFile = new File(localFolder + resultFile);
		BufferedReader br = new BufferedReader(new FileReader(reportFile));
		while((str = br.readLine()) != null) {
			System.out.println(str);
		}
		br.close();
	}

}
