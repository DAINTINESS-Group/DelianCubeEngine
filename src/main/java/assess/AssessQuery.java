package assess;

import java.util.List;

import assess.deltas.AbstractDeltaClass;

public class AssessQuery {
	//TODO
	//Clearly shown original cube query, or cubequery parts
	//Clearly shown benchmark cube query, or cubequery parts
	//Clearly show delta
	//Clearly show labeling scheme
	
	private String targetCube;
	private List<String> parsedGammas;
	private String measurement;
	private String labelingMethod;
	private List<List<String>> labelingSystem; //FIX
	private AbstractDeltaClass deltaFunction;
	
	public AssessQuery(
			String targetCube,
			List<String> parsedGammas,
			String measurement,
			String labelingMethod,
			List<List<String>> labelingSystem) {
		
		//CHECK CORRECTNESS OF PASSED PARAMETERS
		this.targetCube = targetCube;
		this.parsedGammas  = parsedGammas;
		this.measurement = measurement; 
		this.labelingMethod = labelingMethod;
		this.labelingSystem = labelingSystem;
		
		//validate();
		
		System.out.println("Target Cube: " + targetCube);
		System.out.println("Group By: " + parsedGammas);
		System.out.println("Measurement: " + measurement);

		// TODO: Create a labeling System based on the data below
		System.out.println(java.util.Optional.ofNullable(labelingMethod).orElse("Provided Custom System"));
		if (labelingSystem != null) { System.out.println(labelingSystem); }
	}
	
	//private ResultFileMetadata validate();
}
