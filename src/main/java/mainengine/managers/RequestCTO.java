package mainengine.managers;

import cubemanager.CubeManager;

/**
 * A CTO representing a parameterized command request
 * This class serves as a Command Object in the Command/Dispatcher design  
 * It encapsulates all the necessary information required to rout an execute a specific operation within the system
 * 
 * @author Nik-Pt
 *
 */
public class RequestCTO {
	
	//The type of manager responsible for handling this request
	private ManagerType managerType;
	
	//The specific command alias to be executed (e.g. "descibe" or "answer_from_string")
	private String commandAlias;
	
	//An Object type variable that contains the input parameters required for the command execution
	private Object input;
	
	//A reference to the cube manager in order to have access to all the underlying data
	private CubeManager cubeManager;

	public RequestCTO(ManagerType managerType, String commandAlias, Object input, CubeManager cubeManager) {
		super();
		this.managerType = managerType;
		this.commandAlias = commandAlias;
		this.input = input;
		this.cubeManager = cubeManager;
	}

	public ManagerType getManagerType() {
		return managerType;
	}

	public String getCommandAlias() {
		return commandAlias;
	}

	public Object getInput() {
		return input;
	}

	public CubeManager getCubeManager() {
		return cubeManager;
	}
}
