package chartManagement.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chartManagement.IChartQueryNModelGenerator;
import chartManagement.utils.ChartScoreModel;
import chartManagement.utils.ChartVisModel;


public class ModelManager {
	
	private String localFolder;
	
	private String localFilename;
	
	private ModelListFactory modelListFactory;
	
	private IChartQueryNModelGenerator chartGenerator;
	
	private String aggrFunc;
	

	public ModelManager()
	{
		modelListFactory = new ModelListFactory();
		
	}
	
	
	public void setLocalFolderAndLocalFilename(String localFolder, String localFilename) {
		
		this.localFolder = localFolder;
		this.localFilename = localFilename;
	}
	
	
	
	public List<ChartModel> returnModelList() {
		return modelListFactory.createModelsForChartType(chartGenerator);
	}
	
	public Boolean isChartModelTypeOfContributor(ChartModel model) {
		if (model instanceof ContributorModel) {
			return true;
		}
		
		return false;
	}


	public void reportModelsForChartType() throws Exception {
		List<ChartModel> models = returnModelList();
		FileWriter fileWriter = null;
		File report = new File(localFolder +  "/" + localFilename);
		try {
			fileWriter = new FileWriter(report, true);
			//System.out.println("i will write to: " + localFolder + "/" + localFilename);
			fileWriter.write("Models produced for the charts:\n");
			fileWriter.write("Model" + "\t" + "Type\t" + "Details\t" + "Result\t" +"\n");
			for(ChartModel model : models)
			{

				model.setFolderAndFilename(localFolder, localFilename);
				if (isChartModelTypeOfContributor(model)) {
					model.setAggrFunc(getAggrFunc());
				}
				if (model.compute()==0) {
					fileWriter.write(model.printAs2DStringArrayForChartReportModel());
				} else {
					
					throw new Exception("Reading of query results for " + model.getModelName() + " has failed.");
				}
				
				
				
			}
			
			
		} catch (IOException e) {
			
			throw new IOException();
			
		}finally {
			fileWriter.close();
		}
		
	}
	
	public List<ChartScoreModel> getScoreModelsForChartVisModels(List<ChartVisModel> chartVisModels) {
		List<ChartModel> chartModels = returnModelList();
		List<ChartScoreModel> scoreModels = new ArrayList<>();
		
		for(ChartModel chartModel: chartModels) {
			for(ChartVisModel visModel: chartVisModels) {
				
				ChartScoreModel scoreModel = new ChartScoreModel();
				scoreModel.setScore(chartModel.computeScore(visModel));
				scoreModel.setName(chartModel.getModelName());
				scoreModel.setChartVisModel(visModel);
				scoreModel.setResult(chartModel.getScoreResult());
				scoreModels.add(scoreModel);
			}
		}
		
		return scoreModels;
	}


	private String getAggrFunc() {
		// TODO Auto-generated method stub
		return this.aggrFunc;
	}
	
	public void setAggrFunc(String func) {
		this.aggrFunc = func;
	}


	public void setQueryNModelGenerator(IChartQueryNModelGenerator chartQueryNModelGenerator) {
		this.chartGenerator = chartQueryNModelGenerator;
		
	}


}
