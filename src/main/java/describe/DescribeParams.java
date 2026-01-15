package describe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Data Transfer Object (DTO) that holds the raw parameters extracted from a Describe query
 * These parameters are:
 * List of raw filtering expressions, raw grouping expressions, list of measures, models requested in the query, joins (should be removed probably in the future)
 * @author Nik-Pt
 *
 */
public class DescribeParams {
	private String aggrFunc = "";
	private String cubeName = "";
	private String queryAlias = "";
	
	private ArrayList<String> sigmaExpressions = new ArrayList<>();
	private HashMap<String , String> sigmaExpressionsValues = new HashMap<>();
	private ArrayList<String> gammaExpressions = new ArrayList<>();

	private ArrayList<String> measureList = new ArrayList<>();
	private String joinCondition = "";
	private String joinType = "";
	private boolean isJoined = false;
	
	private ArrayList<String> orderExpressions = new ArrayList<>();
	private ArrayList<String> modelList = new ArrayList<>();
	
	public String getAggrFunc() {
		return aggrFunc;
	}
	public void setAggrFunc(String aggrFunc) {
		this.aggrFunc = aggrFunc;
	}
	
	public String getCubeName() {
		return cubeName;
	}
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
	public void appendToCubeName(String suffix) {
		this.cubeName += suffix; 
	}
	
	public String getQueryAlias() {
		return queryAlias;
	}
	public void setQueryAlias(String queryAlias) {
		this.queryAlias = queryAlias;
	}
	
	public ArrayList<String> getSigmaExpressions() {
		return sigmaExpressions;
	}
	public void addSigmaExpression(String expr) {
		this.sigmaExpressions.add(expr); 
	}
	
	public HashMap<String, String> getSigmaExpressionsValues() {
		return sigmaExpressionsValues;
	}
	public void addSigmaValue(String key, String value) { 
		this.sigmaExpressionsValues.put(key, value); 
	}
	
	public ArrayList<String> getGammaExpressions() {
		return gammaExpressions;
	}
	public void addGammaExpression(String gamma) { 
		this.gammaExpressions.add(gamma);
	}
	
	public ArrayList<String> getMeasureList() {
		return measureList;
	}
	public void addMeasure(String measure) { 
		this.measureList.add(measure);
	}
	
	public String getJoinCondition() {
		return joinCondition;
	}
	public void setJoinCondition(String joinCondition) {
		this.joinCondition = joinCondition;
	}
	
	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	
	public boolean isJoined() {
		return isJoined;
	}
	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}
	
	public ArrayList<String> getOrderExpressions() {
		return orderExpressions;
	}
	public void addOrderExpression(String order) {
		this.orderExpressions.add(order); 
	}
	
	public ArrayList<String> getModelList() {
		return modelList;
	}
	public void addModel(String model) { 
		this.modelList.add(model);
	}
}
