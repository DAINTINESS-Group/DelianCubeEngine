package analyze.syntax;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;


/**
 * A class that manages the ANTLR parser
 * @author mariosjkb
 *
 */
public class AnalyzeParserManager {
	// key info to be obtained by the incoming expression
	private String aggrFunc;
	private String measure;
	private String cubeName;
	private ArrayList<String> sigmaExpressions;
	private HashMap<String,String> sigmaExpressionsValues;
	private ArrayList<String> gammaExpressions;
	private String queryAlias;
	
	public AnalyzeParserManager() {
		aggrFunc = null;
		measure = null;
		cubeName = null;
		sigmaExpressions = new ArrayList<String>();
		sigmaExpressionsValues = new HashMap<String,String>();
		gammaExpressions = new ArrayList<String>();
		queryAlias = null;
	}
	
	/**
	 * Method that set ups and executes the ANTLR parsing process.
	 * @param incomingExpression
	 * @return An integer that states how many syntax errors found in the incoming expression
	 * @throws RecognitionException
	 */
	public int parse(String incomingExpression) throws RecognitionException {
		CharStream stream =	new ANTLRStringStream(incomingExpression);
		AnalyzeOperatorLexer lexer = new AnalyzeOperatorLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		AnalyzeOperatorParser parser = new AnalyzeOperatorParser(tokenStream);
		
		try {
			parser.start();
			
			aggrFunc = parser.aggrFunc;
			measure = parser.measure;
			cubeName = parser.cubeName;
			sigmaExpressions = parser.sigmaExpressions;
			sigmaExpressionsValues = parser.sigmaExpressionsValues;
			gammaExpressions= parser.gammaExpressions;
			queryAlias = parser.queryAlias;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return parser.getNumberOfSyntaxErrors();
	}
	
	// Getter method to access the fields of the class
	public String getAggrFunc() {
		return this.aggrFunc;
	}
	
	public String getMeasure() {
		return this.measure;
	}
	
	public String getCubeName() {
		return this.cubeName;
	}
	
	public ArrayList<String> getSigmaExpressions(){
		return this.sigmaExpressions;
	}
	
	public HashMap<String,String> getSigmaExpressionsValues(){
		return this.sigmaExpressionsValues;
	}
	
	public ArrayList<String> getGammaExpressions(){
		return this.gammaExpressions;
	}

	public String getQueryAlias() {
		return this.queryAlias;
	}
}
