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

public class AssessOperator {
	private final CubeManager cubeManager;
	private final AssessQueryParser parser;
	
	public AssessOperator(String incomingExpression, CubeManager cubeManager) {
		parser = createParser(incomingExpression);
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

	public int execute() throws RecognitionException {
		//1. ask parser(lexer.tokenStream) to produce a AssessQuery
		AssessQuery currentAssessQuery = parser.parse(new AssessQueryBuilder(cubeManager));

		//2. execute queries, match results, compare and label
		// ???
		return 1;
	}
}
