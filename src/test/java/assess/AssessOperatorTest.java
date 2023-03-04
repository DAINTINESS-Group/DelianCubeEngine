package assess;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

// Integration Test class that will be expanded every time a part of the system is finished
public class AssessOperatorTest {

	private final String simpleQuery =
			"with SALES by month assess sum(storeSales)" +
			"labels {[0,0.5):bad, [0.5,1]: good}";
	private final String complexQuery =
			"WiTh SALES\n" +
			"FoR month = '07/1997', store = 'SmartMart'\n" +
			"by month, store\n" +
			"AssesS cOunT(storeSales)\n" +
			"agAiNst past 4\n" +
			"using ratio(storeSales, benchmark.storeSales)\n" +
			"labels {[0,0.9): worse, [0.9,1.1]: fine, (1.1, inf):better}";

	@Test
	public void runSimpleQuery() {
		AssessOperator testedOperator = new AssessOperator(null);
		try {
			testedOperator.execute(simpleQuery);
		} catch (RecognitionException re) {
			System.out.println("Invalid query");
			re.printStackTrace();
		}
	}

	@Test
	public void runComplexQuery() {
		AssessOperator testedOperator = new AssessOperator(null);
		try {
			testedOperator.execute(complexQuery);
		} catch (RecognitionException re) {
			System.out.println("Invalid query");
			re.printStackTrace();
		}
	}

}
