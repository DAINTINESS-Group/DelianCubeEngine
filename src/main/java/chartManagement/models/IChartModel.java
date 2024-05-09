package chartManagement.models;

public interface IChartModel {
	
	String printAs2DStringArrayForChartReportModel();
	
	 int compute();

	void setFolderAndFilename(String localFilename, String localFolder);
	
	String getModelName();

}
