package mainengine;

import cubemanager.CubeManager;

/**
 * 
 * A class for the intentional operator Analyze
 *
 */

public class AnalyzeOperator {
	private String incomingExpression;
	private CubeManager cubeManager;
	
	public AnalyzeOperator(String incomingExpression, CubeManager cubeManager) {
		this.incomingExpression = incomingExpression;
		this.cubeManager = cubeManager;
		
	}

	public int execute() {
		// TODO Auto-generated method stub
		//this must return a Intentional Result object, not null, not void, not int
		translateInputAnalyzeExpressionToAnalyzeAttributes();
		//.....
		int status = -1;
		return status;
	}
	
	private int translateInputAnalyzeExpressionToAnalyzeAttributes() {
		//do what you gonna do
		int status = -1;
		return status;
	}

}
