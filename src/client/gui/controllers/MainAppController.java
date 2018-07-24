package client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import mainengine.IMainEngine;
import client.gui.utils.ExitController;
import client.gui.utils.LauncherForViewControllerPairs;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client.ClientRMITransferer;
import client.gui.utils.CustomAlertDialog;

public class MainAppController extends AbstractController {
	@FXML
	private TextArea textLogger;
	
	public MainAppController() {
		super();
	}


	
	@FXML
	private void handleClose() {
		ExitController x = new client.gui.utils.ExitController(this.stage);
    	x.exit();
	}

	@FXML
	private void handleAbout() {
		String contentText = "Delian Cube Engine implements a simple query engine that receives cube queries and returns the results in tsv files.";
		CustomAlertDialog a = new CustomAlertDialog("about", null, contentText, this.stage); 
		a.show();
		
		String prevText = textLogger.getText();
		textLogger.setText(prevText + "\n" + "About clicked." + "\n");
		
	}

	@FXML
	/*
	 * This is a typical handling of a new window at a menu button. Just call the Launcher :)
	 * All you need is
	 * (i) new an Appropriate controller
	 * (ii) a null layout object, which is the root of the fxml view you have
	 *      (must be a subclass of Parent)
	 * (iii) launch the launching launcher
	 * Hopefully, if sth goes wrong, it's gonna return a negative launchResult value (zero for OK).    
	 */
	private void handlewNewDW() {
		DataWindowController controller = new DataWindowController();
		VBox dwLayout = null;
		int launchResult = -100;
		
		LauncherForViewControllerPairs launcher = new LauncherForViewControllerPairs();
		launchResult = launcher.launchViewControllerPairNoFXController(this.getApplication(), this, this.getStage(), true, 
				"DataWindow.fxml", controller, dwLayout);

		//Just logging what happened.
		String prevText = textLogger.getText();
		if(launchResult <0 ) {
			textLogger.setText(prevText + "\n" + "MISERABLE FAILURE for a new DW!" + "\nResult was: " + launchResult + "\n");
		}
		else {
			textLogger.setText(prevText + "\n" + "New DW!" + "\n");			
		}
	}//end method

	

	@FXML
	private void runStoredQueries(){
		FileChooser fileChooser = new FileChooser();
		ArrayList<String> resultFileLocations = new ArrayList<String>();
		String prevText = textLogger.getText();
		
		File fileName = fileChooser.showOpenDialog(this.stage);
		if(fileName == null)
			return;
		
		IMainEngine serverEngine = this.getApplication().getServer();
		try {
			resultFileLocations = serverEngine.answerCubeQueriesFromFile(fileName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		if(resultFileLocations.size() == 0) {	
			prevText = textLogger.getText();
			textLogger.setText(prevText + "\n" + "Queries File Chosen does not contain valide queries" + "\n");	
		}
		else {
			for(String nextFile: resultFileLocations) {
				
				File remote = new File(nextFile);
				String[] array = nextFile.split("/");
				String localName = "NoName";
				if (array.length > 0)
					localName = array[array.length-1].trim();

				localName = "ClientCache/" + localName;
				File localFile = new File(localName);

				try {
					ClientRMITransferer.download(serverEngine, remote, localFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				prevText = textLogger.getText();
				textLogger.setText(prevText + "\n" + "Next result: " + nextFile + "\n");
				
				
				DataWindowController controller = new DataWindowController(nextFile);
				VBox dwLayout = null;
				int launchResult = -100;
				
				LauncherForViewControllerPairs launcher = new LauncherForViewControllerPairs();
				launchResult = launcher.launchViewControllerPairNoFXController(this.getApplication(), this, this.getStage(), true, 
						"DataWindow.fxml", controller, dwLayout);
				controller.autoloadFile();
				
				//Just logging what happened.
				prevText = textLogger.getText();
				if(launchResult <0 ) {
					textLogger.setText(prevText + "\n" + "MISERABLE FAILURE for a new DW!" + "\nResult was: " + launchResult + "\n");
				}
				else {
					textLogger.setText(prevText + "\n" + "New DW!" + "\n");			

				}
			}//end for
		}//end else
		
		prevText = textLogger.getText();
		textLogger.setText(prevText + "\n" + "Answering Queries From File completed" + "\n");	
	}
	
}//end class MAinAppController







//FXMLLoader loader = new FXMLLoader();
//
//loader.setController(controller);
//loader.setLocation(MainApp.class.getResource("../views/DataWindow.fxml"));
//try {
//	//rootLayout = (VBox) FXMLLoader.load(MainApp.class.getResource("views/DataWindow.fxml"));
//	dwLayout = (VBox) loader.load();
//} catch (IOException e) {
//	e.printStackTrace();
//}
//
//// Handle scene and stage.
//Scene scene = new Scene(dwLayout);
//Stage dwStage = new Stage();
//dwStage.setScene(scene);
//
//controller.setScene(scene);
//controller.setStage(dwStage);
//controller.setApplication(this.mainApp);
//controller.setCallerController(this);
//dwStage.show();
//