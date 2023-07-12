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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
	public void collectSelectionPredicatesTest() throws IOException, RecognitionException {
		String predicates = "date = '20/05/2019', type = 'Fresh Fruit', country = 'Italy'";
		AssessQueryParser parser = createParser(predicates);

		HashMap<String, String> expected = new HashMap<>();
		expected.put("date", "20/05/2019");
		expected.put("type", "Fresh Fruit");
		expected.put("country", "Italy");

		HashMap<String, String> actual = parser.selection_predicates();

		assertEquals(expected, actual);
	}
	@Test
	public void collectDates() throws IOException, RecognitionException {
		HashMap<String, String> actual;

		HashMap<String, String> expectedFirstDate = new HashMap<>();
		expectedFirstDate.put("date", "20/05/2019");
		actual = createParser("date = '20/05/2019'").selection_predicates();
		assertEquals(expectedFirstDate.get("date"), actual.get("date"));

		HashMap<String, String> expectedSecondDate = new HashMap<>();
		expectedSecondDate.put("date", "05/2019");
		actual = createParser("date = '05/2019'").selection_predicates();
		assertEquals(expectedSecondDate.get("date"), actual.get("date"));

		HashMap<String, String> expectedThirdDate = new HashMap<>();
		expectedThirdDate.put("date", "2019");
		actual = createParser("date = '2019'").selection_predicates();
		assertEquals(expectedThirdDate.get("date"), actual.get("date"));
	}

	@Test
	public void createGroupBySetTest()
			throws IOException, RecognitionException {
		String predicates = "country, product, age, date";
		AssessQueryParser parser = createParser(predicates);

		HashSet<String> expected = new HashSet<>();
		expected.add("country");
		expected.add("product");
		expected.add("age");
		expected.add("date");

		HashSet<String> actual = parser.group_by_set();

		assertEquals(expected, actual);
	}

	@Test
	public void identifyConstantBenchmarkTest()
			throws IOException, RecognitionException {
		String benchmark = "-1000";
		List<String> expected = new ArrayList<>(Arrays.asList("Constant", "-1000"));

		AssessQueryParser parser = createParser(benchmark);
		List<String> actual = parser.benchmark();

		assertEquals(expected, actual);
	}

	@Test
	public void identifySiblingBenchmarkTest() throws IOException, RecognitionException {
		String benchmark = "country = 'France'";
		List<String> expected = new ArrayList<>(Arrays.asList("Sibling", "country", "France"));

		AssessQueryParser parser = createParser(benchmark);
		List<String> actual = parser.benchmark();

		assertEquals(expected, actual);
	}

	@Test
	public void identifyExternalBenchmarkTest() throws IOException, RecognitionException {
		String benchmark = "SALES.quantity";
		List<String> expected = new ArrayList<>(Arrays.asList("External", "SALES", "quantity"));

		AssessQueryParser parser = createParser(benchmark);
		List<String> actual = parser.benchmark();

		assertEquals(expected, actual);
	}

	@Test
	public void identifyPastBenchmarkTest() throws IOException, RecognitionException {
		String benchmark = "past 4";
		List<String> expected = new ArrayList<>(Arrays.asList("Past", "4"));

		AssessQueryParser parser = createParser(benchmark);
		List<String> actual = parser.benchmark();

		assertEquals(expected, actual);
	}

	@Test
	public void givenUsingStatement_whenParsingInput_thenCollectComparisonMethods()
			throws IOException, RecognitionException {
		String usingStatement = "ratio(absolute(storeSales,benchmark.storeSales))";
		AssessQueryParser parser = createParser(usingStatement);

		List<String> expected = new ArrayList<>(Arrays.asList("ratio", "absolute"));
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