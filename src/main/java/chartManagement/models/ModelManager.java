package chartManagement.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import chartManagement.IChartQueryNModelGenerator;


public class ModelManager {
	
	private String localFolder;
	
	private String localFilename;
	
	private ModelListFactory modelListFactory;
	
	private IChartQueryNModelGenerator chartGenerator;
	

	public ModelManager()
	{
		modelListFactory = new ModelListFactory();
		
	}
	
	
	public void setLocalFolderAndLocalFilename(String localFolder, String localFilename) {
		
		this.localFolder = localFolder;
		this.localFilename = localFilename;
	}
	
	
	
	public List<ChartModel> returnModelList()
	{
		return modelListFactory.createModelsForChartType(chartGenerator);
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
				if(model.compute()==0) {
					fileWriter.write(model.printAs2DStringArrayForChartReportModel());
				} else {
					
					throw new Exception("Reading of query results for " + model.getModelName() + "has failed.");
				}
				
				
				
			}
			
			
		} catch (IOException e) {
			
			throw new IOException();
			
		}finally {
			fileWriter.close();
		}
		
	}


	public void setQueryNModelGenerator(IChartQueryNModelGenerator chartQueryNModelGenerator) {
		this.chartGenerator = chartQueryNModelGenerator;
		
	}


}
