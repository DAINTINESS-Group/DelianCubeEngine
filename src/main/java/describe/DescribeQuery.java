package describe;

import java.util.ArrayList;
import java.util.List;

import cubemanager.cubebase.CubeQuery;
import result.Result;

/**
 * A wrapper class acting as a container for the entire context of a Describe operation.
 * Intermediate Representation: It holds the parsed components of of the query before they are translated into a CubeQuery
 * Execution State: It stores the resulting CubeQuery object and the final Result (data cells) returned from the DB
 * @author Nik-Pt
 */
public class DescribeQuery {
    private String cubeName;
    private ArrayList<QueryMeasure> measures;
    private ArrayList<String> groupByExpressions; //"region", "year"
    private ArrayList<FilterExpression> filterExpressions; //Holds complex filters
	private Result describeQueryResult;
	private CubeQuery cubeQuery;
	
	//This is the most simple way to get the results from the models in order to add them to the report
	private ArrayList<String[][]> modelOutputs = new ArrayList<>();

    public DescribeQuery() {
        this.measures = new ArrayList<>();
        this.groupByExpressions = new ArrayList<>();
        this.filterExpressions = new ArrayList<>();
    }

    /**
     * A helper DTO (Data Transfer Object) representing a single filter condition.
     */
    public static class FilterExpression {
        private String dimension; 
        private String type;      
        private String operator;  
        private String value;     

        public FilterExpression(String dimension, String type, String operator, String value) {
            this.dimension = dimension;
            this.type = type;
            this.operator = operator;
            this.value = value;
        }
        
        public String getDimension() {
        	return dimension;
        }
        
        public String getType() {
        	return type; 
        }
        
        public String getOperator() { 
        	return operator; 
        }
        
        public String getValue() { 
        	return value;
        }
    }


    public String getCubeName() {
    	return cubeName;
    }
    
    public void setCubeName(String cubeName) {
    	this.cubeName = cubeName; 
    }

    public ArrayList<QueryMeasure> getMeasures() {
    	return measures;
    }
    
    public void addMeasure(QueryMeasure qm) {
    	this.measures.add(qm);
    }

    public ArrayList<String> getGroupByExpressions() {
    	return groupByExpressions;
    }

    public void addGroupBy(String gb) {
    	this.groupByExpressions.add(gb);
    }

    public ArrayList<FilterExpression> getFilterExpressions() {
    	return filterExpressions;
    }
    
    public void addFilter(FilterExpression filter) {
    	this.filterExpressions.add(filter);
    }
    
    public void setDescribeQueryResult(Result result) {
		this.describeQueryResult = result;
	}
    
    public Result getDescribeQueryResult() {
        return describeQueryResult;
    }
    
    public CubeQuery getCubeQuery() {
        return cubeQuery;
    }

    public void setCubeQuery(CubeQuery cubeQuery) {
        this.cubeQuery = cubeQuery;
    }
    
    public void addModelOutput(String[][] output) {
        this.modelOutputs.add(output);
    }
    
    public ArrayList<String[][]> getModelOutputs() {
        return this.modelOutputs;
    }
	
}