package analyze;

import cubemanager.CubeManager;

/**
 * 
 * A class for the intentional operator Analyze
 *
 */

public class AnalyzeOperator {
	
	// CubeManager object to manage the cube
			private CubeManager cubeManager;
			
			// A manager object that manages the whole translation process
			private AnalyzeTranslationManager analyzeTranslationManager;
			
			// The name of the dataset loaded in Delian
			private String schemaName;
			
			// Spark or RDMS connection
			private String connectionType;
			
		public AnalyzeOperator(String incomingExpression, CubeManager cubeManager, String schemaName, String connectionType) {
			this.cubeManager = cubeManager;
			this.analyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,cubeManager,schemaName,connectionType);
			this.schemaName = schemaName;
			this.connectionType = connectionType;
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
