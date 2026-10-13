package intentional.describe.syntax;
import org.antlr.runtime.*;

import intentional.describe.DescribeParams;

/**
 * A class that encapsulates the ANTLR parsing
 * Instead of having fields for the measures, expressions etc.. it uses a single object DescribeParams
 * @author Nik-Pt
 *
 */
public class DescribeParserManager {
	private DescribeParams params;
	
	public DescribeParserManager() {
		this.params = new DescribeParams();
	}
	
	/**
	 * ANTLR parsing set up and execution
	 * @param incomingQuery (SQL-like query string).
	 * @return Integer with the current number of syntax errors found in the query
	 * @throws RecognitionException 
	 */
	public int parse(String incomingQuery) throws RecognitionException{
		ANTLRStringStream stream = new ANTLRStringStream(incomingQuery);
		DescribeOperatorLexer lexer = new DescribeOperatorLexer(stream);
		
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DescribeOperatorParser parser = new DescribeOperatorParser(tokens);
		
		parser.start();
		this.params = parser.getParams();
		
		return parser.getNumberOfSyntaxErrors();
	}

	public DescribeParams getParams() {
		return this.params;
	}
}
