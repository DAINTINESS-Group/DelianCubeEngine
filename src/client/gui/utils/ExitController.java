package client.gui.utils;

import javafx.stage.Stage;
/**
 * For the moment, simply closes the caller stage
 * <p>
 * Must check to see whether there are more stuff to add
 * 
 * @author pvassil
 *
 */
public class ExitController {
	private Stage stage;
	
	public ExitController(Stage callerStage) {
		stage = callerStage;
	}
	
	public void exit() {
		stage.close();
	}
}
