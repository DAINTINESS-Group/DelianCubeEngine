package assess.deltas;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeltaSchemeTest {
	@Test
	public void testBuildingTheDeltaScheme() {
		List<String> methods = new ArrayList<>(Arrays.asList("ratio", "absolute", "difference"));
		DeltaScheme scheme = new DeltaScheme(methods);
	}
}
