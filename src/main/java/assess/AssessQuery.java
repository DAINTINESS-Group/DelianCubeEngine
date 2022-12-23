package assess;

import java.util.List;

public class AssessQuery {
	public AssessQuery(String targetCube, List<String> parsedGammas, String measurement) {
		System.out.println("Target Cube: " + targetCube);
		System.out.println("Group By: " + parsedGammas);
		System.out.println("Measurement: " + measurement);
	}
}
