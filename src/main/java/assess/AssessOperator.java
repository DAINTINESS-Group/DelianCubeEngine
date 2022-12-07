package assess;

import cubemanager.CubeManager;

public class AssessOperator {
	private CubeManager cubeManager;
	
	public AssessOperator(String incomingExpression, CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}

	public int execute() {
		return 1;
	}
}
