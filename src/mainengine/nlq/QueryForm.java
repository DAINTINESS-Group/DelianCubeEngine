package mainengine.nlq;

public class QueryForm {
	
	protected String cubeName;
	protected String queryName;
	protected String aggregateFunction;
	protected String measure;
	protected String gamma;
	protected String sigma;
	
	public QueryForm() {
		cubeName = "";
		queryName = "";
		aggregateFunction = "";
		measure = "";
		gamma = "";
		sigma = "";
	}
	
	public QueryForm(String cubeName, String queryName, String aggregateFunction, String measure, String gamma, String sigma) {
		this.cubeName = cubeName;
		this.queryName = queryName;
		this.aggregateFunction = aggregateFunction;
		this.measure = measure;
		this.gamma = gamma;
		this.sigma = sigma;
	}
	
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
	
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	
	public void setAggregateFunction(String aggregateFunction) {
		this.aggregateFunction = aggregateFunction;
	}
	
	public void setMeasure (String measure) {
		this.measure = measure;
	}
	
	public void setGamma(String gamma) {
		this.gamma = gamma;
	}
	
	public void setSigma(String sigma) {
		this.sigma = sigma;
	}
	
	public String getCubeName() {
		return cubeName;
	}
	
	public String getQueryName() {
		return queryName;
	}
	
	public String getAggregateFunction() {
		return aggregateFunction;
	}

	public String getMeasure() {
		return measure;
	}
	
	public String getGamma() {
		return gamma;
	}
	
	public String getSigma() {
		return sigma;
	}
	
	public String toString() {
		String analysedString = this.cubeName + " " +
				this.queryName +  " " +
				this.aggregateFunction +  " " +
				this.measure + " " +
				this.gamma + " " +
				this.sigma;
		return analysedString;
	}//end toString()
	
}//end class
