package cubemanager.queryoptimizer.statistics;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeBase;
import mainengine.Session;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static org.junit.Assert.*;

/**
 * A test for the offline statistics builder
 */
public class StatisticsBuilderTest {

	private static CubeBase testCubeBase;
	private static File histogramFile;
	private static File sampleFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star");

		CubeManager testCubeManager = new CubeManager(typeOfConnection, userInputList);
		Session testSession = new Session(testCubeManager);
		testSession.initialize(typeOfConnection, userInputList);

		testCubeBase = testCubeManager.getCubeBase();

		new StatisticsBuilder().buildHistograms(testCubeBase, "pkdd99_star", "loan", true);
		new StatisticsBuilder().buildSamples(testCubeBase, "pkdd99_star", "loan", 0.01, true);

		histogramFile = new File("InputFiles/" + "pkdd99_star" + "/" + "loan" + "_histograms.csv");
		sampleFile = new File("InputFiles/" + "pkdd99_star" + "/" + "loan" + "_samples.csv");
	}

	@Test
	public void shouldSkipBuildIfFileAlreadyExists() throws Exception {
		assertFalse(new StatisticsBuilder().buildHistograms(testCubeBase, "pkdd99_star", "loan", false));
		assertFalse(new StatisticsBuilder().buildSamples(testCubeBase, "pkdd99_star", "loan", 0.01, false));
	}

	@Test
	public void shouldBuildIfAsked() throws Exception {
		assertTrue(new StatisticsBuilder().buildHistograms(testCubeBase, "pkdd99_star", "loan", true));
		assertTrue(new StatisticsBuilder().buildSamples(testCubeBase, "pkdd99_star", "loan", 0.01, true));
	}

	@Test
	public void shouldConfirmThatSampleHasTheCorrectSize() throws Exception {
		int factTableSize = 682;
		int expected = (int) (0.01 * factTableSize);
		assertEquals(expected, readSampleRows().size());
	}

	private static List<String[]> readSampleRows() throws Exception {
		List<String[]> rows = new ArrayList<>();
		boolean headerSeen = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(sampleFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("factTableSize = ")) continue;
				if (!headerSeen) {
					headerSeen = true;
					continue;
				}
				rows.add(line.split("\\|", -1));
			}
		}
		return rows;
	}
}
