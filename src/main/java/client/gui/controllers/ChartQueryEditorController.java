package client.gui.controllers;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client.ChartQueryBuilderImpl;
import client.ChartQueryObject;
import client.ClientRMITransferer;
import client.IChartQueryBuilder;
import client.gui.application.AbstractApplication;
import client.gui.utils.CustomAlertDialog;
import client.gui.utils.ExitController;
import client.gui.utils.LauncherForViewControllerPairs;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainengine.IMainEngine;
import mainengine.ResultFileMetadata;

public class ChartQueryEditorController extends AbstractController 
{
	
	@FXML
	private TextArea textArea;
	
    @FXML
    private CheckBox barchart;

    @FXML
    private CheckBox scatterplot;

    @FXML
    private CheckBox linechart;

   
	
	
	public ChartQueryEditorController(AbstractApplication anApp, AbstractController aCallerController, Scene aScene, Stage aStage) {
		super(anApp, aCallerController, aScene, aStage);
	}

	
	
	@FXML
	private void handleClose() {
		ExitController x = new client.gui.utils.ExitController(this.stage);
		x.exit();
	}//end handleClose
	
	@FXML
	private int runQuery()
	{
		String queryString = null;
		int result = -100;
		
		queryString = textArea.getText();
		
		if(queryString.length() == 0) {
			CustomAlertDialog a = new CustomAlertDialog("Empty query", null, "Your query string is empty", this.stage); 
			a.show();
			return -100;
		}
		result = executeAndDisplaySimpleQuery(queryString); //temporary!
		String specifiedChart = getSpecifiedChart();
		IChartQueryBuilder chartQueryBuilder = new ChartQueryBuilderImpl();
		ChartQueryObject chartQueryObject = chartQueryBuilder.build(specifiedChart, queryString);
		//executeAndDisplayChartQuery(chartQueryObject);
		
		return result;
	}//end handleClose
	
	public int executeAndDisplayChartQuery(ChartQueryObject chartQueryObject) throws RemoteException
	{
		String remoreResultFileLocation = null;
		String remoteInfoFileLocation = null;
		String remoteFolderName = null;
		
		String localFileLocation;
		String localInfoFileLocation;
		
		int result;
		ResultFileMetadata resMetadata = null;
		
		IMainEngine serverEngine = this.getApplication().getServer();
		resMetadata = serverEngine.answerCubeQueryFromChartQueryObjectWithMetadata(chartQueryObject);
		if(resMetadata != null) {
			remoreResultFileLocation = resMetadata.getResultFile();
			remoteInfoFileLocation = resMetadata.getResultInfoFile();
			remoteFolderName = resMetadata.getLocalFolder();
		
			System.out.println("Remote _info_ file FOLDER: " + remoteFolderName);
			System.out.println("Remote result file METADATA: " + remoreResultFileLocation);
			System.out.println("Remote _info_ file METADATA: " + remoteInfoFileLocation);
		}
		else {
			System.out.println("Remote METADATA: NULL" );
			return -1;
		}

		
		if(remoreResultFileLocation.length() == 0) {	
			CustomAlertDialog a = new CustomAlertDialog("Invalid query, no result", null, "The query did not return any result", this.stage); 
			a.show();
			return -1;
		}
		else {
			localFileLocation = downloadResult(remoreResultFileLocation, serverEngine);
			result = displayResultInDataWindow(localFileLocation);
//			OLD WAY OF GETTING INFO FILE
//			FileInfoProvider infoProvider = new FileInfoProvider(remoreResultFileLocation);
//			infoFileLocation = infoProvider.getNameForInfoFile(remoreResultFileLocation);    //getInfoFileAbsoluteLocation();
//			System.out.println("Remote result file: " + remoreResultFileLocation);
//			System.out.println("Remote _info_ file: " + remoteInfoFileLocation);

			localInfoFileLocation = downloadResult(remoteInfoFileLocation, serverEngine);
	
		}
		return result;
	}
	
	public int executeAndDisplaySimpleQuery(String queryString) {
		String remoreResultFileLocation = null;
		String remoteInfoFileLocation = null;
		String remoteFolderName = null;
		
		String localFileLocation;
		String localInfoFileLocation;
		
		int result;
		ResultFileMetadata resMetadata = null;
		
		IMainEngine serverEngine = this.getApplication().getServer();
		try {
			//USED TO GET DATA BY THE answerCubeQueryFromString
			//remoreResultFileLocation = serverEngine.answerCubeQueryFromStringExtraInfo(queryString);
			
			resMetadata = serverEngine.answerCubeQueryFromStringWithMetadata(queryString);
			if(resMetadata != null) {
				remoreResultFileLocation = resMetadata.getResultFile();
				remoteInfoFileLocation = resMetadata.getResultInfoFile();
				remoteFolderName = resMetadata.getLocalFolder();
			
				System.out.println("Remote _info_ file FOLDER: " + remoteFolderName);
				System.out.println("Remote result file METADATA: " + remoreResultFileLocation);
				System.out.println("Remote _info_ file METADATA: " + remoteInfoFileLocation);
			}
			else {
				System.out.println("Remote METADATA: NULL" );
				return -1;
			}
		} catch (RemoteException e) {
			System.out.println("Cannot execute query answering!");
			e.printStackTrace();
		}

		
		if(remoreResultFileLocation.length() == 0) {	
			CustomAlertDialog a = new CustomAlertDialog("Invalid query, no result", null, "The query did not return any result", this.stage); 
			a.show();
			return -1;
		}
		else {
			localFileLocation = downloadResult(remoreResultFileLocation, serverEngine);
			result = displayResultInDataWindow(localFileLocation);
//			OLD WAY OF GETTING INFO FILE
//			FileInfoProvider infoProvider = new FileInfoProvider(remoreResultFileLocation);
//			infoFileLocation = infoProvider.getNameForInfoFile(remoreResultFileLocation);    //getInfoFileAbsoluteLocation();
//			System.out.println("Remote result file: " + remoreResultFileLocation);
//			System.out.println("Remote _info_ file: " + remoteInfoFileLocation);

			localInfoFileLocation = downloadResult(remoteInfoFileLocation, serverEngine);
	
		}
		return result;
	}//end method
	
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
	

	public String getSpecifiedChart()
	{
		String chart = "";
		if(barchart.isSelected())
		{
			chart = "barchart";
		}else if(scatterplot.isSelected())
		{
			chart = "scatterplot";
		}else if(linechart.isSelected()) {
			chart = "linechart";
		}
		return chart;
	}
	
}
