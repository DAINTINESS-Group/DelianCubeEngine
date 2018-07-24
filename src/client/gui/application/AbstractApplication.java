package client.gui.application;

import javafx.application.Application;
//import javafx.scene.Scene;
import javafx.stage.Stage;
import mainengine.IMainEngine;
import client.gui.controllers.AbstractController;
public abstract class AbstractApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        this.currentStage = primaryStage;
        this.originalStage = primaryStage;
    }

    public abstract IMainEngine getServer();
    
    public Stage getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(Stage currentStage) {
		this.currentStage = currentStage;
	}
	public Stage getOriginalStage() {
		return originalStage;
	}
	public void setOriginalStage(Stage originalStage) {
		this.originalStage = originalStage;
	}
//	public Scene getCurrentScene() {
//		return currentScene;
//	}
//	public void setCurrentScene(Scene currentScene) {
//		this.currentScene = currentScene;
//	}
//	public Scene getOriginalScene() {
//		return originalScene;
//	}
//	public void setOriginalScene(Scene originalScene) {
//		this.originalScene = originalScene;
//	}
	public AbstractController getFirstCalledController() {
		return firstCalledController;
	}
	public void setFirstCalledController(AbstractController firstCalledController) {
		this.firstCalledController = firstCalledController;
	}
	public AbstractController getLastCalledController() {
		return lastCalledController;
	}
	public void setLastCalledController(AbstractController lastCalledController) {
		this.lastCalledController = lastCalledController;
	}



	private Stage currentStage = null;
    private Stage originalStage = null;
//    private Scene currentScene = null;
//    private Scene originalScene = null;
    private AbstractController firstCalledController = null;
    private AbstractController lastCalledController = null;
}
