package chartManagement.utils;

import java.io.Serializable;
import java.util.List;

import analyze.AnalyzeQuery;

public class ChartVisModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<DataPoint> dataPoints;
	
	private String chartVisType;
	
	private String queryType;
	
	private List<String> x_axisValues;
	
	private String sqlExpression;
	
	private List<String> series;
	
	
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}
	
	public String getChartVisType() {
		return chartVisType;
	}
	
	public String getQueryType() {
		return queryType;
	}
	
	public List<String> getX_axisValues() {
		return x_axisValues;
	}
	
	public String getSqlExpression() {
		return sqlExpression;
	}
	
	public List<String> getSeries() {
		return series;
	}
	
	public void setDataPoints(List<DataPoint> points) {
		dataPoints = points;
	}
	
	public void setChartVisType(String achartVisType) {
		chartVisType = achartVisType;
	}
	
	public  void setQueryType(String type) {
		queryType = type;
	}
	
	public void setX_axisValues(List<String> values) {
		x_axisValues = values;
	}
	
	public void setSqlExpression(String expression) {
		sqlExpression = expression;;
	}
	
	public void setSeries(List<String> names) {
		series = names;
	}


	

}
