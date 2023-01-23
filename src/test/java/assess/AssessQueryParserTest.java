package assess;

import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Collections of tests for checking validity of parser's rules.
 * Their main purpose is to collect the needed string information for later
 * usage by the builder.
 *
 * NOTE: When giving a full query, make sure you separate the keywords with
 * whitespaces
 */
public class AssessQueryParserTest {
	private AssessQueryParser createParser(String query) throws IOException {
		InputStream stream = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
		ANTLRInputStream input = new ANTLRInputStream(stream);
		AssessQueryLexer lexer = new AssessQueryLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new AssessQueryParser(tokens);
	}

	@Test
	public void givenFullQuery_whenParsing_thenCollectEverythingNeeded()
		throws IOException, RecognitionException {
		String query = "WITH loan " +
				"BY region " +
				"Assess product " +
				"using minMaxNorm(difference(product, 1000) "+
				"labels {[-inf, -0.2): bad, [-0.2, 0.2]: ok, (0.2, inf]: awesome}";
		AssessQueryParser parser = createParser(query);

		parser.parse();

	}

	@Test
	public void givenSelectionFiltersInQuery_whenParsing_thenCollectPredicates()
			throws IOException, RecognitionException {
		String predicates = "date = '20/5/2019', type = 'Fresh Fruit', country = 'Italy'";
		AssessQueryParser parser = createParser(predicates);

		HashMap<String, String> expected = new HashMap<>();
		expected.put("date", "20/5/2019");
		expected.put("type", "Fresh Fruit");
		expected.put("country", "Italy");

		HashMap<String, String> actual = parser.selection_predicates();

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroupByPredicatesInQuery_whenParsing_thenCollectGroupByPredicates()
			throws IOException, RecognitionException {
		String predicates = "country, product";
		AssessQueryParser parser = createParser(predicates);

		HashSet<String> expected = new HashSet<>();
		expected.add("product");
		expected.add("country");

		HashSet<String> actual = parser.group_by_set();

		assertEquals(expected, actual);
	}

	@Test
	public void givenConstantBenchmark_whenParsingQuery_thenInitializeConstantBenchmark()
			throws IOException, RecognitionException {
		String benchmark = "-1000";
		AssessQueryParser parser = createParser(benchmark);

		String expected = "Constant -1000";
		String actual = parser.benchmark();
		assertEquals(expected, actual);
	}

	@Test
	public void givenSiblingBenchmark_whenParsingInput_thenIdentifyPastBenchmark() throws IOException, RecognitionException {
		String benchmark = "country = 'France'";
		AssessQueryParser parser = createParser(benchmark);
		parser.benchmark(); // What should we check this for?
	}

	@Test
	public void givenExternalBenchmark_whenParsingInput_thenIdentifyPastBenchmark() throws IOException, RecognitionException {
		String benchmark = "SALES.stores";
		AssessQueryParser parser = createParser(benchmark);
		parser.benchmark(); // What should we check this for?
	}

	@Test
	public void givenPastBenchmark_whenParsingInput_thenIdentifyPastBenchmark() throws IOException, RecognitionException {
		String benchmark = "past 4";
		AssessQueryParser parser = createParser(benchmark);

		String expected = "Past 4";
		String actual = parser.benchmark();

		assertEquals(expected, actual);
	}

	@Test
	public void givenUsingStatement_whenParsingInput_thenIdentifyComparisonMethod()
			throws IOException, RecognitionException {
		String usingStatement = "ratio(storeSales,benchmark.storeSales)";
		AssessQueryParser parser = createParser(usingStatement);

		List<String> expected = new ArrayList<>();
		expected.add("ratio");

		List<String> actual = parser.comparison_scheme(new ArrayList<>());

		assertEquals(expected, actual);
	}

	@Test
	public void givenUsingStatement_whenParsingInput_thenCollectComparisonMethods()
			throws IOException, RecognitionException {
		String usingStatement = "ratio(deference(storeSales,benchmark.storeSales))";
		AssessQueryParser parser = createParser(usingStatement);

		List<String> expected = new ArrayList<>(Arrays.asList("ratio", "deference"));
		List<String> actual = parser.comparison_scheme(new ArrayList<>());

		assertEquals(expected, actual);
	}

	@Test
	public void givenLabelsStatement_whenParsingInput_buildCustomLabelingScheme() throws IOException, RecognitionException {
		String labelsStatement = "{[-inf, -0.2): bad, [-0.2, 0.2]: ok, (0.2, inf]: awesome}";
		List<List<String>> expected = new ArrayList<>();

		List<String> firstTerm = new ArrayList<>(Arrays.asList("[", "-inf", "-0.2", ")", "bad"));
		expected.add(firstTerm);

		List<String> secondTerm = new ArrayList<>(Arrays.asList("[", "-0.2", "0.2", "]", "ok"));
		expected.add(secondTerm);

		List<String> thirdTerm = new ArrayList<>(Arrays.asList("(", "0.2", "inf", "]", "awesome"));
		expected.add(thirdTerm);

		AssessQueryParser parser = createParser(labelsStatement);

		List<List<String>> actual = parser.custom_labeling();
		assertEquals(expected, actual);
	}
}