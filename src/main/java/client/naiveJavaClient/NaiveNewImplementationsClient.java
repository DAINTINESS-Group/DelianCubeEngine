package client.naiveJavaClient;

/*
*    DelianCubeEngine. A simple cube query engine.
*    Copyright (C) 2018  Panos Vassiliadis
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License as published
*    by the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

import client.ClientRMITransferer;
//import mainengine.Foo;
import mainengine.IMainEngine;
import result.ResultFileMetadata;
/**
 * A simple client that 
 * (a) locates an RMI server in the HOST at PORT
 * (b) implements the registration of the database
 * 
 * @author DimosGkitsakis
 *
 */
public class NaiveNewImplementationsClient {

	// Host or IP of Server
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static Registry registry;

	public static void main(String[] args) throws Exception {
		// Search the registry in the specific Host, Port.
		registry = LocateRegistry.getRegistry(HOST, PORT);
		// LookUp for MainEngine on the registry
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class
				.getSimpleName());
		if(service == null) {
			System.out.println("Unable to commence server, exiting");
			System.exit(-100);
		}
		
				
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99");
		service.initializeConnection(typeOfConnection, userInputList);
		System.out.println("Completed connection initialization");
		
		String testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name:CubeQueryLoan2_Copy" + " \n" +
				"AggrFunc:Avg" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl2,date_dim.lvl2" + " \n" +
				"Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'";

		service.answerCubeQueryFromString(testQueryString2);   /**/
		
		ResultFileMetadata resMetadata = service.rollUp("CubeQueryLoan2_Copy", "CubeQueryLoan2_RollUp", "account_dim", "lvl3");
		System.out.println("Errors: "+resMetadata.getErrorCheckingStatus());
		
		ResultFileMetadata resMetadata2 = service.drillDown("CubeQueryLoan2_Copy", "CubeQueryLoan2_DrillDown", "account_dim", "lvl0");
		System.out.println("Errors: "+resMetadata2.getErrorCheckingStatus());
		
		ResultFileMetadata resMetadata3 = service.drillDown("CubeQueryLoan2_Copy", "CubeQueryLoan2_DrillDown2", "account_dim", "lvl1");
		System.out.println("Errors: "+resMetadata3.getErrorCheckingStatus());
	}
	
}//end class
