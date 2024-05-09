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


	public void reportModelsForChartType() throws IOException {
		// TODO Auto-generated method stub
		List<ChartModel> models = returnModelList();
		FileWriter fileWriter = null;
		File report = new File(localFolder +  "/" + localFilename);
		try {
			fileWriter = new FileWriter(report, true);
			System.out.println("i will write to: " + localFolder + "/" + localFilename);
			fileWriter.write("Models produced for the charts:\n");
			fileWriter.write("Model" + "\t" + "Type\t" + "Details\t" + "Result\t" +"\n");
			for(ChartModel model : models)
			{
//				System.out.println("i will write to: " + localFolder + "/" + localFilename + " for model : " + model.getModelName() );
				model.setFolderAndFilename(localFolder, localFilename);
				model.compute();
				fileWriter.write(model.printAs2DStringArrayForChartReportModel());
				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally {
			fileWriter.close();
		}
		
	}


	public void setQueryNModelGenerator(IChartQueryNModelGenerator chartQueryNModelGenerator) {
		// TODO Auto-generated method stub
		this.chartGenerator = chartQueryNModelGenerator;
		
	}


}
