package mainengine;

import result.*;

import java.io.File;
import java.util.ArrayList;

public class ModelManager {

	public ModelManager(Result res) {
		result = res;
		modelsToLaunch = new ArrayList<AbstractModel>();
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
			int modelExecution =  model.compute();
			
			if(modelExecution==0) {
				String outputFolder = "OutputFiles";
				String resultFileName = outputFolder + File.separator + namePrefix + "_" + model.getModelName() + ".tab";
				model.printComponentsToFile(resultFileName);
			}//end if
		}// end for
		return 0;
	}// end executeModelConstruction
	
	private ArrayList<AbstractModel> modelsToLaunch;
	private Result result;
	private String namePrefix; 
}
