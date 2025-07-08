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


package cubemanager;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Measure;
import extractionmethod.ExtractionMethod;
import extractionmethod.ExtractionMethodFactory;
import result.Result;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CubeManager {

	private CubeBase cubeBase;
	private ICubeQueryTranslator cubeQueryTranslator;
	private CubeQueryTranslatorFactory cubeQueryTranslatorFactory;
	private List<BasicStoredCube> registeredCubesList;
	private List<Dimension> registeredDimensionsList;
	//private HashMap<String, Dimension>


	public CubeManager(String typeOfConnection, HashMap<String, String> userInputList) {
		cubeBase = new CubeBase(typeOfConnection, userInputList);
		cubeQueryTranslatorFactory = new CubeQueryTranslatorFactory();
		registeredCubesList = new ArrayList<BasicStoredCube>();
		registeredDimensionsList = new ArrayList<Dimension>();
	}

	public ICubeQueryTranslator setCubeQueryTranslator() {
		ICubeQueryTranslator result = cubeQueryTranslatorFactory.createCubeQueryTranslator("SQL"); 
		if (result == null) {
			System.err.println("CubeQueryTranslator generation failed. Exiting.");
			System.exit(-1);
		}
		cubeQueryTranslator = result;
		return result;
	}

	/**
	 * Search for a Cube based on its name. If not found throws exception
	 * @param name the name of the Cube we want
	 * @return the requested cube
	 */
	public BasicStoredCube getCubeByName(String name) {
		return registeredCubesList
				.stream()
				.filter(cube -> cube.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Cube not found: " + name));
	}

	public void createCubeBase(HashMap<String, String> userInputList) {
		cubeBase.registerCubeBase(userInputList);
	}
	
	public CubeBase getCubeBase(){
		return this.cubeBase;
	}
	
	public List<Dimension> getDimensions(){
		return cubeBase.getDimensions();
	}
	
	public List<BasicStoredCube> getCubes(){
		return this.registeredCubesList;
	}
	
	public List<Dimension> insertDimension(String dimensionName,
			String dimensionTable, ArrayList<String> originalFieldName,ArrayList<String> customFieldName, 
			String dimensionType, ArrayList<String> hierachyList, HashMap<String, String> levelID, HashMap<String, String> levelDescription,
			HashMap<String, ArrayList<String>> levelAttributes, HashMap<String, String> attributeTypes, HashMap<String, String> attributeDatasource) {
		cubeBase.addDimension(dimensionName);
		cubeBase.addDimensionTable(dimensionTable);
		cubeBase.setDimensionType(dimensionType);
		cubeBase.setDimensionLevelsAndLinearHierachy(hierachyList, originalFieldName, customFieldName, 
													 levelID, levelDescription, levelAttributes, attributeTypes, attributeDatasource);
		registeredDimensionsList = cubeBase.getDimensions();
				
		return registeredDimensionsList;
	}
	
	public List<BasicStoredCube> insertCube(String creationName, String datasourceTable,
			ArrayList<String> dimensionList,
			ArrayList<String> dimemsionRefField, ArrayList<String> measureList,
			ArrayList<String> measureRefField) {

		cubeBase.addCube(creationName);
		cubeBase.addSqlRelatedTable(datasourceTable);
		cubeBase.setCubeDimension(dimensionList, dimemsionRefField);
		cubeBase.setCubeMeasure(measureList, measureRefField);
		registeredCubesList = cubeBase.getRegisteredCubeList();
		
		return registeredCubesList;
	}


	//REMOVED: a parameter   String msrname   from the method's signature 
	//why was the measureName in the signature of this method at the Cinecubes' code?
	//Probably Dimitris was struggling with sth at the parser...
	//
	public CubeQuery createCubeQueryFromString(String cubeQstring, HashMap<String, String> queryParams)
			throws RemoteException {
		String[] rows = cubeQstring.trim().split("\n");
		String Cbname = null, nameCQ = null, aggregateFunction = null, measureName = null;
		String[][] sigma = null;
		String[][] gamma = null;
		for (int i = 0; i < rows.length; i++) {
			String[] temp = rows[i].split(":");
			if (temp[0].equals("CubeName")) {
				Cbname = temp[1].trim();
			}
			if (temp[0].equals("Name")) {
				nameCQ = temp[1].trim();
			} else if (temp[0].equals("AggrFunc")) {
				aggregateFunction = temp[1].trim();
			} else if (temp[0].equals("Measure")) {
				measureName = temp[1].trim();
			} else if (temp[0].equals("Gamma")) {
				String[] tmp_gamma = temp[1].split(",");
				gamma = new String[tmp_gamma.length][2];
				for (int j = 0; j < tmp_gamma.length; j++) {
					String[] split_gamma = tmp_gamma[j].trim().split("\\.");
					gamma[j][0] = split_gamma[0];
					gamma[j][1] = split_gamma[1];
				}
			} else if (temp[0].equals("Sigma")) {
				
				if(temp.length==1) {
					sigma = new String[1][3];
					sigma[0][0] = "1";
					sigma[0][1] = ">";
					sigma[0][2] = "0";
				} else {
					String[] tmp_sigma = temp[1].split(",");
					sigma = new String[tmp_sigma.length][3];
					for (int j = 0; j < tmp_sigma.length; j++) {
						String[] split_sigma_equal = tmp_sigma[j].trim().split("=");
						String[] split_sigma_less = tmp_sigma[j].trim().split("<");
						String[] split_sigma_great = tmp_sigma[j].trim().split(">");
						String[] split_sigma_in = tmp_sigma[j].trim().split("IN");
						String[] split_sigma_not_in = tmp_sigma[j].trim().split("NOT IN");
						String[] split_sigma_between = tmp_sigma[j].trim().split("BETWEEN");
						if(split_sigma_equal.length > 1) {
							sigma[j][0] = split_sigma_equal[0];
							sigma[j][1] = "=";
							sigma[j][2] = split_sigma_equal[1];
						}else if(split_sigma_less.length > 1) {
							sigma[j][0] = split_sigma_less[0];
							sigma[j][1] = "<";
							sigma[j][2] = split_sigma_less[1];
						}else if(split_sigma_great.length > 1) {
							sigma[j][0] = split_sigma_great[0];
							sigma[j][1] = ">";
							sigma[j][2] = split_sigma_great[1];
						}else if(split_sigma_in.length > 1) {
							sigma[j][0] = split_sigma_in[0];
							sigma[j][1] = "IN";
							sigma[j][2] = split_sigma_in[1];
						}else if(split_sigma_not_in.length > 1) {
							sigma[j][0] = split_sigma_not_in[0];
							sigma[j][1] = "NOT IN";
							sigma[j][2] = split_sigma_not_in[1];
						}else if(split_sigma_between.length > 1) {
							sigma[j][0] = split_sigma_between[0];
							sigma[j][1] = "BETWEEN";
							sigma[j][2] = split_sigma_between[1];
						}
					}
				}
			}
		}
		queryParams.put("QueryName", nameCQ);


		//TODO: opportunity to wrap all this ping-pong with CubeQuery into a method/constructor
		CubeQuery cubequery = new CubeQuery(nameCQ);
		cubequery.setAggregateFunction(aggregateFunction);
		/* Must Create Measure In Cube Parser->> I Have Done this */
		/* Search for Measure */
		Measure msrToAdd = new Measure(1,measureName,cubeBase.getDataSourceDescription().getFieldOfSqlTable(Cbname, measureName));
		cubequery.getMeasuresList().add(msrToAdd);
		//msrname = measureName;
		/* Need work to done up here */

		for (int i = 0; i < gamma.length; i++) {
			cubequery.addGammaExpression(gamma[i][0], gamma[i][1]);
		}

		for (int i = 0; i < sigma.length; i++) {
			cubequery.addSigmaExpression(sigma[i][0], sigma[i][1], sigma[i][2]);
		}
		for (BasicStoredCube bsc : cubeBase.basicStoredCubesList) {
			if (bsc.getName().equals(Cbname + "_cube")) {
				cubequery.setBasicStoredCube(bsc);
			}
		}
		return cubequery;
	}

	/**
	 * Returns the {@link Result} of a {@link CubeQuery}.
	 * <p>
	 * The <code>CubeQuery</code> object must have been prepared before via invoking <code>createQueryFromString</code> of this object. 
	 * Then, the execution is passed to <code>CubeBase</code> that actually orchestrates the execution of this query 
	 * 
	 * The method takes a CubeQuery as input and 
	 * (a) generates an extraction method (currently SQL query only) 
	 * (b) executes the extraction method
	 * (c) by simply using the Result object of the extraction method, it also populates a Result object
	 * (d) converts the resultSet of the query execution to a 2d String array
	 * 
	 * @param currentCubeQuery the {@link CubeQuery} that it to be executed (must have been prepared before)
	 * @return the {@link Result} of the execution
	 * 
	 * 
	 * @see CubeQuery
	 * @see Result
	 * @see mainengine.SimpleQueryProcessingEngine#answerCubeQueryFromString(String)
	 */
	public Result executeQuery(CubeQuery currentCubeQuery) {
		//return this.CBase.executeQuery(currentCubeQuery);

		//1. Create an ExtractionMethod (for the moment: SQLQuery)
		ExtractionMethod extractionMethod = ExtractionMethodFactory.createMethod();  
		currentCubeQuery.setExtractionMethod(extractionMethod);   

		
		//OLD setup, where you had the execution at CubeQuery!
		//Result res = extractionMethod.getResult();
		//currentCubeQuery.produceExtractionMethod();
		//String queryString = extractionMethod.toString();	
		
		//2. Convert the cubequery to sth that the DBMS can execute, i.e., a query string
		String queryString = produceExtractionMethod(currentCubeQuery);
		//	if you DEBUG
		System.out.println("\n"+queryString);		

		Result finalResult = cubeBase.executeQueryToProduceResult(queryString, extractionMethod.getResult()) ;
		if (finalResult == null) {
			System.err.println("Exiting due to failure to populate the result");
			System.exit(-100);
			
		}

// OLD code		
//		//2. Fire the query and get a result set
//		ResultSet resultSet=CBase.getDatabase().executeSql(queryString);
//		
//		//3. Populate Result with the ResultSet's data via ExtractionMethod method calls
//		if (res.createResultArray(resultSet) == false) {
//			System.err.println("Exiting due to failure to populate the result");
//			System.exit(-100);
//		}
//		else
//			System.out.println("\n\n"+ "Result produced"+"\n");
		
		
		
		return finalResult;
	}
	
	/**
	 * Returns the {@link Result} of a {@link CubeQuery}.
	 * <p>
	 * The <code>CubeQuery</code> object must have been prepared before via invoking <code>createQueryFromString</code> of this object. 
	 * Then, the execution is passed to <code>CubeBase</code> that actually orchestrates the execution of this query 
	 * 
	 * The method takes a CubeQuery as input and 
	 * (a) generates an extraction method (currently SQL query only) 
	 * (b) executes the extraction method
	 * (c) by simply using the Result object of the extraction method, it also populates a Result object
	 * (d) converts the resultSet of the query execution to a 2d String array
	 * 
	 * @param currentCubeQuery the {@link CubeQuery} that it to be executed (must have been prepared before)
	 * @return the {@link Result} of the execution
	 * 
	 * 
	 * @see CubeQuery
	 * @see Result
	 * @see mainengine.SimpleQueryProcessingEngine#answerCubeQueryFromString(String)
	 */
	public Result executeSimpleSqlQuery(CubeQuery currentCubeQuery) {
		//return this.CBase.executeQuery(currentCubeQuery);

		//1. Create an ExtractionMethod (for this method: SimpleSqlQuery)
		ExtractionMethod extractionMethod = ExtractionMethodFactory.createSimpleExtractionMethod();  
		currentCubeQuery.setExtractionMethod(extractionMethod);   	
		
		//2. Convert the cubequery to sth that the DBMS can execute, i.e., a query string
		String queryString = produceExtractionMethod(currentCubeQuery);
		//	if you DEBUG
		System.out.println("\n"+queryString);		

		Result finalResult = cubeBase.executeQueryToProduceResult(queryString, extractionMethod.getResult()) ;
		if (finalResult == null) {
			System.err.println("Exiting due to failure to populate the result");
			System.exit(-100);
			
		}
	
		return finalResult;
	}

	/**
	 * Returns the string of the extraction Method of a CubeQuery
	 * <p>
	 * See the simple abstract coupling to translators via the interface {@link ICubeQueryTranslator} and its implementations.
	 * <p>
	 * @param cubeQuery  A {@link CubeQuery} that needs to be converted to SQL
	 * @return the string of the (currently) {@link SQLQuery} of a CubeQuery
	 */
	public String produceExtractionMethod(CubeQuery cubeQuery) {
		if (this.cubeQueryTranslator == null) {
			System.err.println("Unable to locate a logical to physical translator. Exiting.");
			System.exit(-1);
		}		
		return cubeQueryTranslator.produceExtractionMethod(cubeQuery);
	}//end method produceExtractionMethod(CubeQuery)

}//end class CubeManager
