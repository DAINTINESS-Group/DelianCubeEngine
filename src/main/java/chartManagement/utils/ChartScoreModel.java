package chartManagement.utils;

import java.io.Serializable;

public class ChartScoreModel implements Serializable{


	private static final long serialVersionUID = 6316580195681444713L;

	private Double score;
	
	private String name;
	
	private ChartVisModel chartVisModel;
	
	private String result;
	
	public void setScore(Double ascore) {
		
		this.score = ascore;
		
	}
	
	public Double getScore() {
		
		return this.score;
		
	}
	
	public void setName(String aname) {
		
		this.name = aname;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public void setChartVisModel(ChartVisModel model) {
		this.chartVisModel = model;
	}
	
	public ChartVisModel getChartVisModel() {
		return this.chartVisModel;
	}
	
	public void setResult(String result) {
		
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}
	
	
	
}
