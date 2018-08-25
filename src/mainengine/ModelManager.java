package mainengine;

import result.*;

import java.io.File;
import java.util.ArrayList;

public class ModelManager {

	public ModelManager(Result res) {
		result = res;
		modelsToLaunch = new ArrayList<AbstractModel>();
		modelComponentFileNames = new ArrayList<String>();
		modelInfoFileNames = new ArrayList<String>();
	}//end constructor
	
	/**
	 * Allows the developer to setup which models to be processed by the ModelManager in the sequel
	 * 
	 * @param modelNames an array of Strings with the names of the models to be executed
	 * @return an ArrayList of the models selected 
	 */
	public ArrayList<AbstractModel> selectModelsToLaunch(String [] modelNames) {
		if (modelNames.length == 0)
			return null;
		
		ModelFactory modelFactory = new ModelFactory();
		for(int i = 0; i < modelNames.length; i++) {
			switch(modelNames[i]) {
				case "Rank":	modelsToLaunch.add(modelFactory.generateModel("Rank", result)); break;
				case "Outlier":	modelsToLaunch.add(modelFactory.generateModel("Outlier", result)); break;
				default: break;
			}//end switch
		}
		
		return modelsToLaunch;
	}//end method
	
	/**
	 * Executes the previously selected models
	 * 
	 * @param queryName a String with the name of the Query over the result of which AbstractModels will be generated
	 * @return 0 if all OK, negative otherwise
	 */
	public int executeModelConstruction(String queryName) {
		namePrefix = queryName;
		if(modelsToLaunch.size() == 0) {
			System.out.println("No models selected for generation for the query " + queryName);
			return -1;
		}
	
		for(AbstractModel model: modelsToLaunch) {
			if (model == null) {
				System.out.println("NULL?");
				return -1;
			}
			System.out.println(model.getModelName());

			//1.execute the model
			int modelExecution =  model.compute();
			
			if(modelExecution==0) {
				//2a. output results to a file 
				String outputFolder = "OutputFiles"+ File.separator;
				String resultFileName = outputFolder + namePrefix + "_" + model.getModelName() + ".tab";
				model.printComponentsToFile(resultFileName);
				//2b. add the file with the results to the list of file results
				modelComponentFileNames.add(resultFileName);
				
				//3a. construct the info string for the entire model
				//TODO: make each model produce an info string once (i.e., within) compute() was launched
				//3b. add info files to the list
				//TODO: add info files to the list
				
			}//end if
		}// end for
		return 0;
	}// end executeModelConstruction
	
	/**
	 * Returns an updated ResultFileMetadata object that contains the names of the files of the execution of the selected models
	 * 
	 * @param resMetadata a ResultFileMetadata object to be populated with the names of the files of the execution of the selected models
	 * @return the size of the arraylist that was added to the object
	 */
	public int addComponentsToResultMetadata(ResultFileMetadata resMetadata) {
		resMetadata.setComponentResultFiles(modelComponentFileNames);
		return modelComponentFileNames.size();
	}//end addComponentsToResultMetadata
	
	private ArrayList<AbstractModel> modelsToLaunch;
	private Result result;
	private String namePrefix; 
	private ArrayList<String> modelComponentFileNames;
	private ArrayList<String> modelInfoFileNames;
}
