package describe.syntax;
import java.util.ArrayList;
import java.util.HashMap;
import org.antlr.runtime.*;

import describe.DescribeQuery;

public class DescribeParserManager {
	private DescribeQuery query;
	
	public DescribeParserManager() {
		this.query = new DescribeQuery();
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
		this.query = parser.getQuery();
		
		return parser.getNumberOfSyntaxErrors();
	}

	public DescribeQuery getQuery() {
		return this.query;
	}
}
