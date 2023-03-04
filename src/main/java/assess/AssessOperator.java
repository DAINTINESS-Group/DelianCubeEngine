package assess;

import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import cubemanager.CubeManager;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The top layer class for any assessments done in the intentional model.
 * Given that the CubeManager handles only one cube at a time, instances
 * of this class are created everytime we wish to change cubes.
 */
public class AssessOperator {
	private final CubeManager cubeManager;

	public AssessOperator(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}

	private AssessQueryParser createParser(String incomingExpression) {
		try {
			InputStream stream = new ByteArrayInputStream(incomingExpression.getBytes(StandardCharsets.UTF_8));
			ANTLRInputStream input = new ANTLRInputStream(stream);
			AssessQueryLexer lexer = new AssessQueryLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			return new AssessQueryParser(tokens);
		} catch (IOException e) {
			throw new RuntimeException("There was an error while creating the parser");
		}
	}

	/**
	 * Executes the provided query. Returns ?
	 * @param assessQuery The user-provided query for assessment reasons
	 * @return
	 * @throws RecognitionException If the query does not follow the defined syntax
	 */
	public int execute(String assessQuery) throws RecognitionException {
		AssessQuery currentAssessQuery = createParser(assessQuery)
				.parse(new AssessQueryBuilder(cubeManager));
		System.out.println(currentAssessQuery.targetCubeQuery);

		// 2. Compare the targetCube to the Benchmark
		// 3. Label the results
		// 4. Figure out what this method should return
//		CubeQuery targetCube = cubeManager.createCubeQueryFromString(currentAssessQuery.targetCubeQuery, new HashMap<>());
//		CubeQuery benchmarkCube = cubeManager.createCubeQueryFromString(currentAssessQuery.benchmarkCubeQuery, new HashMap<>());
//
//		Cube comparisonCube = currentAssessQuery.deltaFunction.compare(targetCube, benchmarkCube);
//		currentAssessQuery.labelingScheme.applyLabels(comparisonCube);
		// ???
		return 1;
	}
}
