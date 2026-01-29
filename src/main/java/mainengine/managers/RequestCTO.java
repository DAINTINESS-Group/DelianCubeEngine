package mainengine.managers;

import cubemanager.CubeManager;

public class RequestCTO {
	private ManagerType managerType;
	private String commandAlias;
	private Object input;
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
