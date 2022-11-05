package mainengine;

import cubemanager.CubeManager;

public class AssessOperator {
	/**
	 * 
	 * A class for the intentional operator Assess
	 *
	 */
	
	private String incomingExpression;
	private CubeManager cubeManager;
	
	public AssessOperator(String incomingExpression, CubeManager cubeManager) {
		this.incomingExpression = incomingExpression;
		this.cubeManager = cubeManager;
		
	}

	public int execute() {
		// TODO Auto-generated method stub
		//this must return a Intentional Result object, not null, not void, not int
		translateInputAssessExpressionToAnalyzeAttributes();
		//.....
		int status = -1;
		return status;
	}
	
	private int translateInputAssessExpressionToAnalyzeAttributes() {
		//do what you gonna do
		int status = -1;
		return status;
	}
}
