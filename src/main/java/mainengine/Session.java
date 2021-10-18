package mainengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Scanner;

import cubemanager.CubeManager;
import parsermgr.ParserManager;

public class Session {

	private String id;
	private CubeManager cubeManager;
	private ParserManager prsMng;
	
	public Session(CubeManager cubeManager, ParserManager prsMng) {
		id = new Timestamp(System.currentTimeMillis()).toInstant().toString();
		this.cubeManager = cubeManager;
		this.prsMng = prsMng;
	}
	
	public String initialize(HashMap<String, String> userInputList) throws RemoteException {		
		initializeCubeMgr(userInputList);
		cubeManager.createCubeBase(userInputList);
		constructDimension(userInputList.get("inputFolder"), userInputList.get("cubeName"));
		cubeManager.setCubeQueryTranslator();
		
		return this.id;
	}
	
	private void initializeCubeMgr(HashMap<String, String> userInputList) throws RemoteException {
		//TODO: na douleuei me typeofconnection kai oxi hard coded rdbms
		cubeManager = new CubeManager("RDBMS", userInputList);
	}
	
	private void constructDimension(String inputlookup, String cubeName)
			throws RemoteException {
		try {
			this.parseFile(new File("InputFiles/" + inputlookup + "/"
					+ cubeName + ".ini"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void parseFile(File file) throws FileNotFoundException {
		if (file != null) {
			prsMng = new ParserManager();
			@SuppressWarnings("resource")
			Scanner sc = (new Scanner(file)).useDelimiter(";");
			while (sc.hasNext()) {
				prsMng.parse(sc.next() + ";");
				if (prsMng.mode == 2) {
					this.cubeManager.InsertionDimensionLvl(
							prsMng.name_creation, prsMng.sqltable,
							prsMng.originallvllst, prsMng.customlvllst,
							prsMng.dimensionlst);
				} else if (prsMng.mode == 1) {
					this.cubeManager.InsertionCube(prsMng.name_creation,
							prsMng.sqltable, prsMng.dimensionlst,
							prsMng.originallvllst, prsMng.measurelst,
							prsMng.measurefields);
				}
			}
		}
	}

	public CubeManager getCubeManager() {
		return cubeManager;
	}

	public void setCubeManager(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}

	public ParserManager getPrsMng() {
		return prsMng;
	}

	public void setPrsMng(ParserManager prsMng) {
		this.prsMng = prsMng;
	}

	public String getId() {
		return id;
	}
	
}
