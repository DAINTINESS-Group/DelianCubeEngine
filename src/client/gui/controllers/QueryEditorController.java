/*
 *    DelianCubeEngine. A simple cube query engine.
 *    Copyright (C) 2018  Panos Vassiliadis
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as published
 *    by the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package client.gui.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import client.ClientRMITransferer;
import client.gui.application.AbstractApplication;
import client.gui.utils.CustomAlertDialog;
import client.gui.utils.ExitController;
import client.gui.utils.FileInfoProvider;
import client.gui.utils.LauncherForViewControllerPairs;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainengine.IMainEngine;
import mainengine.ResultFileMetadata;

/**
 * @author pvassil
 *
 */
public class QueryEditorController extends AbstractController {

	@FXML
	private TextArea textArea;

	//TODO: the entire GUI package requires refactoring. For example here, there is an injection of the class
	//       AbstractApplication from a higher level package, creating cyclic dependencies between the packages. 
	//       This is mainly due to the fact that the Launcher requires to know the Application => maybe relocate at application?
	public QueryEditorController(AbstractApplication anApp, AbstractController aCallerController, Scene aScene, Stage aStage) {
		super(anApp, aCallerController, aScene, aStage);
	}

	public QueryEditorController() {
		super();
	}

	@FXML
	private void handleClose() {
		ExitController x = new client.gui.utils.ExitController(this.stage);
		x.exit();
	}//end handleClose

	@FXML
	public void handleOpenFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.setTitle("File Open");
		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			textArea.clear();
			String nextLine;
			try (BufferedReader buffReader = new BufferedReader(new FileReader(file))) {
				while ((nextLine  = buffReader.readLine()) != null) {
					textArea.appendText(nextLine  + "\n");
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}	
		}
	}//end method 

	@FXML
	public void handleNewFile() {
		textArea.clear();
		textArea.appendText("CubeName: <cubename>\n");
		textArea.appendText("Name: <queryName>\n");
		textArea.appendText("AggrFunc: <aggr.function>\n");
		textArea.appendText("Measure: <the measure aggregated>\n");
		textArea.appendText("Gamma: <dim1.aggrLevel>,<dim2.aggrLevel>\n");
		textArea.appendText("Sigma: comma separared list of atoms: dim.level='value'\n");

	}//end method

	@FXML
	public void handleSaveFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.setTitle("File Save As");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Text Files", "*.txt"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
		File file = fileChooser.showSaveDialog(stage);

		if(saveToFile(file) < 0) {
			CustomAlertDialog a = new CustomAlertDialog("Failed Saving", null, "Saving the text failed. Plz., try again!", this.stage); 
			a.show();
		}
	}//end method

	public int saveToFile(File file) {
		if(file==null)
			return -1;

		String currentText;
		currentText = textArea.getText();

		BufferedWriter outputFile = null;
		try {
			outputFile = new BufferedWriter(new FileWriter(file));
			outputFile.write(currentText);
			outputFile.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return 0;
	}//end method

	@FXML
	public int handleRunQuery() {
		String queryString = null;
		int result = -100;
		
		queryString = textArea.getText();
		
		if(queryString.length() == 0) {
			CustomAlertDialog a = new CustomAlertDialog("Empty query", null, "Your query string is empty", this.stage); 
			a.show();
			return -100;
		}
		result = executeAndDisplayQuery(queryString);
		return result;
	}

	public int executeAndDisplayQuery(String queryString) {
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
	
	
	public String downloadResult(String resultFileLocation,  IMainEngine serverEngine) {	
		File remote = new File(resultFileLocation);
		String[] array = resultFileLocation.split("/");
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

	
	/**
	 * Given a downloaded file via the RMI Client, it displays it via a DataWindow
	 * <p>
	 * @param resultFileLocation The name of the file containing the results of the query
	 * @return 0 if all OK, negative otherwise
	 */
	private int displayResultInDataWindow(String resultFileLocation) {
		DataWindowController controller = new DataWindowController(resultFileLocation);
		VBox dwLayout = null;
		int launchResult = -100;
		
		LauncherForViewControllerPairs launcher = new LauncherForViewControllerPairs();
		launchResult = launcher.launchViewControllerPairNoFXController(this.getApplication(), this, this.getStage(), true, 
				"DataWindow.fxml", controller, dwLayout);
		controller.autoloadFile();
		
		return launchResult;
	}//end method

	
	
	
	
	@FXML
	public int handleHelp() {
		RunQueryHelpController controller = new RunQueryHelpController();
		VBox dwLayout = null;
		int launchResult = -100;
		
		LauncherForViewControllerPairs launcher = new LauncherForViewControllerPairs();
		launchResult = launcher.launchViewControllerPairNoFXController(this.getApplication(), this, this.getStage(), true, 
				"HelpWindow.fxml", controller, dwLayout);
		controller.setup();
		
		return launchResult;

	}//end handle help method
	
	
	
}//end class
