package chartManagement.utils;

import java.io.Serializable;
import java.util.List;

public class ChartResponse implements Serializable{
	
	
	private List<ChartVisModel> chartVisModels;
	
	private List<ChartScoreModel> chartScoreModels;
	
	
	
	public List<ChartVisModel> getChartVisModels() {
		
		
		return chartVisModels;
	}
	
	public void setChartVisModels(List<ChartVisModel> visModels) {
		
		
		chartVisModels = visModels;
	}
	
	public List<ChartScoreModel> getChartScoreModels() {
		
		
		return chartScoreModels;
	}
	
	public void setChartScoreModels(List<ChartScoreModel> scoreModels) {
		
		
		chartScoreModels = scoreModels;
	}

}
