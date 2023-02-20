package assess;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.LinearHierarchy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * In this test we are using a mocked CubeManager that only has a single
 * cube (created in createSalesCube method).
 */
public class AssessQueryBuilderTest {
	private static AssessQueryBuilder builder;

	@BeforeClass
	public static void initializeBuilder() {
		CubeManager mockedCubeManager = createMockedCubeManager();
		builder = new AssessQueryBuilder(mockedCubeManager);
	}

	private static CubeManager createMockedCubeManager() {
		BasicStoredCube salesCube = createSalesCube();
		CubeManager mockedCubeManager = mock(CubeManager.class);
		when(mockedCubeManager.getCubeByName("sales")).thenReturn(salesCube);
		when(mockedCubeManager.getCubeByName("none")).thenReturn(null);
		return mockedCubeManager;
	}

	/* We will mock the following cube from the paper:
		SALES = (H, M) :
			H = {date, customer, product, store}, (The hierarchies)
			M = (quantity, storeSales, storeCost), (The measurements)
			date > month > year
			customer > gender
			product > type > category
			store > city > country
	 */

	private static BasicStoredCube createSalesCube() {
		BasicStoredCube salesCube = new BasicStoredCube("sales");
		Dimension date = createDimension("date",
				new ArrayList<>(Arrays.asList("date", "month", "year")));
		salesCube.addDimension(date);

		Dimension customer = createDimension("customer",
				new ArrayList<>(Arrays.asList("customer", "gender")));
		salesCube.addDimension(customer);

		Dimension product = createDimension("product",
				new ArrayList<>(Arrays.asList("product", "type", "category")));
		salesCube.addDimension(product);

		Dimension store = createDimension("store",
				new ArrayList<>(Arrays.asList("store", "city", "country")));
		salesCube.addDimension(store);
		return salesCube;
	}

	private static Dimension createDimension(String dimensionName, List<String> levelNames) {
		LinearHierarchy hierarchy = new LinearHierarchy();
		for (String levelName : levelNames) {
			Level level = new Level(levelNames.indexOf(levelName), levelName);
			hierarchy.getLevels().add(level);
		}
		Dimension result = new Dimension(dimensionName);
		result.setHierarchy(new ArrayList<>(Collections.singletonList(hierarchy)));
		return result;
	}


	/* A possible query
	with SALES
	for month = ’7/2019’, store = ’SmartMart’ (Selection Predicates)
	by month, store
	assess storeSales against past 4
	using ratio(storeSales, benchmark.storeSales)
	labels {[0, 0.9): worse, [0.9, 1.1]: fine, (1.1,inf): better}
	 */
	@Test
	public void testBuildingTargetCubeQuery() {
		builder.setTargetCubeName("SaLEs");
		builder.setMeasurement("quantity");
		Map<String, String> selectionPredicates = Stream.of(new String[][]{
				{"month", "07/2019"},
				{"store", "SmartMart"}
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
		builder.setSelectionPredicates(selectionPredicates);
		Set<String> groupBySet = Stream.of("month", "store")
				.collect(Collectors.toCollection(HashSet::new));
		builder.setGroupBySet(groupBySet);

		String actual = builder.buildStringQueryTemplate();

		String expected = "CubeName:sales\n" +
				"Name:sales_quantity\n" +
				"AggrFunc:??\n" +
				"Measure:quantity\n" +
				"Gamma:date.lvl1,store.lvl0\n" +
				"Sigma:date.lvl1='07/2019',store.lvl0='SmartMart'";
		assertEquals(expected, actual);
	}
	// Create a test for each benchmark type
	// Sibling benchmark should identify what are the hierarchies that can define siblings
	// For the past benchmark, if there is no group by date throw exception
}
