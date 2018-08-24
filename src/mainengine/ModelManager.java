package mainengine;

import result.*;
import java.util.ArrayList;

public class ModelManager {

	public ModelManager(Result res) {
		result = res;
		modelsToLaunch = new ArrayList<AbstractModel>();
	}//end constructor
	
	public ArrayList<AbstractModel> selectModelsToLaunch(String [] modelNames) {
		if (modelNames.length == 0)
			return null;
		
		ModelFactory modelFactory = new ModelFactory();
		for(int i = 0; i < modelNames.length; i++) {
			switch(modelNames[i]) {
				case "rank":	modelsToLaunch.add(modelFactory.generateModel("RankModel", result)); break;
				case "outlier":	modelsToLaunch.add(modelFactory.generateModel("OutlierModel", result)); break;
				default: return null;
			}//end switch
		}
		return modelsToLaunch;
	}//end method
	
	public int executeModelConstruction() {
		for(AbstractModel model: modelsToLaunch) {
			//TODO must assign this to a collection of 2D String arrays ... or, see next...
			model.compute();
			//TODO: you need a compositeThing that has (a) a 2D String array + (b) a model + (c) a Result 
			//Then, you need an ArrayList of compositeThings.
			//Then, for each compositeThing, write a file with the correctName = namePrefix + sthElseDependingOnTheModel.
			//OOOOOOOOPS: namePrefix = queryName!!
			//   a problem is that you CANNOT know the query of a Result via the result 
			//   => you need to pass it as an argument ffrom the SimpleQueryProcessorEngine 

		}
		return 0;
	}
	private ArrayList<AbstractModel> modelsToLaunch;
	private Result result;
	//private String namePrefix; 
}
