package assess;

import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import assess.labelers.*;

public class AssessOperator {
	private CubeManager cubeManager;
	
	public AssessOperator(String incomingExpression, CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}

	public int execute() {
		
		//1. ask parser(lexer.tokenStream) to produce a AssessQuery
		AssessQuery currentAssessQuery = null;
		//currentAssessQyery.validate();
		
		//2. given the assessQuery, produce the different cube queries to be executed
		CubeQuery cubeQuery = null; //original query
		CubeQuery benchmarkQuery = null; //benchmarkquery
		List<NumericLabelingRule> rules = null;
		//Delta
		//...
		
		//3. execute queries, match results, compare and label
		// ???
		return 1;
	}
}
