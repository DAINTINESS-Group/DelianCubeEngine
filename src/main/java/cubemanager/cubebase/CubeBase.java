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


package cubemanager.cubebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cubemanager.logicaltophysicalmapping.DataSourceFactory;
import cubemanager.physicalschema.DataSourceDescription;
import cubemanager.physicalschema.DimensionTable;
import cubemanager.physicalschema.FactTable;
import cubemanager.physicalschema.Table;
import result.Result;

public class CubeBase {

	private DataSourceDescription dataSourceDescription;
	public List<Dimension> dimensionsList;
	public List<BasicStoredCube> basicStoredCubesList;
 
	public DataSourceDescription getDataSourceDescription(){
		return dataSourceDescription;
	}
	/**
	 * The method looks for the file dbc.ini inside the folder prescribed as the method's parameter.
	 * Then it processed the file and relates to the database prescribed in the dbc.ini file
	 *  
	 * @param lookupFolder	The FOLDER where the input info for the project lies. Must be in the InputFiles folder.
	 */
	public CubeBase(String typeOfConnection, HashMap<String, String> userInputList) {
		DataSourceFactory dataSourceFactory = new DataSourceFactory();
		dataSourceDescription = dataSourceFactory.createConnection(typeOfConnection, userInputList);
		dimensionsList = new ArrayList<Dimension>();
		basicStoredCubesList = new ArrayList<BasicStoredCube>();
	}

	public void registerCubeBase(HashMap<String, String> userInputList) {
		dataSourceDescription.registerCubeBase(userInputList);
	}

	public void addDimension(String dimensionName) {
		dimensionsList.add(new Dimension(dimensionName));
	}

	public void addDimensionTable(String dimensionTable) {
		Table tempTable = dataSourceDescription.getConnectionTableInstance(dimensionTable);
		DimensionTable dm = new DimensionTable(tempTable.getTableName());
		dm.addAllAttributes(tempTable);
		this.getLastInsertedDimension().setDimensionTable(dm);
	}

	public void setDimensionType(String dimensionType) {
		Dimension tmp;
		tmp = this.getLastInsertedDimension();
		tmp.setDimensionType(dimensionType);
	}
	
	public void setDimensionLevelsAndLinearHierachy(ArrayList<String> hierachyList,
			List<String> fieldTables, List<String> customFieldNames, 
			HashMap<String, String> levelID, HashMap<String, String> levelDescription,
			HashMap<String, ArrayList<String>> levelAttributesMap, HashMap<String, String> attributesTypesMap, HashMap<String, String> attributeDatasourceMap) {
		Dimension tmp;
		tmp = this.getLastInsertedDimension();
		LinearHierarchy linearHierarchy = new LinearHierarchy();
		linearHierarchy.setDimension(tmp);
		for (int i = 0; i < hierachyList.size(); i++) {
			if (customFieldNames.get(i).equals(hierachyList.get(i))) {
				Level lvl = new Level(i, hierachyList.get(i));
				
				lvl.setLevelIDAttribute(levelID.get(customFieldNames.get(i)));
				lvl.setLevelDescriptionAttribute(levelDescription.get(customFieldNames.get(i)));
				
				
				String[] tmp_str = fieldTables.get(i).split("\\.");
				ArrayList<String> levelAttributesList = levelAttributesMap.get(customFieldNames.get(i));
				for(int j=0; j <levelAttributesList.size(); j++) {
					
					LevelAttribute lvlattr = new LevelAttribute(attributeDatasourceMap.get(levelAttributesList.get(j)),
							tmp_str[0]);
					
					lvlattr.setLevel(lvl);
					String type = attributesTypesMap.get(levelAttributesList.get(j));
					lvlattr.setAttributeType(type);
					
					lvlattr.setAttributeTable(dataSourceDescription.getFieldOfSqlTable(tmp_str[0],
							attributeDatasourceMap.get(levelAttributesList.get(j))));
					
					lvl.addLevelAttribute(lvlattr);
				}
				
				linearHierarchy.levelsList.add(lvl);
			}
		}
		tmp.getHierarchy().add(linearHierarchy);
	}

	public Dimension getLastInsertedDimension() {
		return dimensionsList.get(dimensionsList.size() - 1);
	}
	
	public List<Dimension> getDimensions() {
		return dimensionsList;
	}
	
	public void addCube(String name_creation) {
		basicStoredCubesList.add(new BasicStoredCube(name_creation));

	}

	public void addSqlRelatedTable(String sqltable) {
		Table tmp_tbl = dataSourceDescription.getConnectionTableInstance(sqltable);
		BasicStoredCube tmp = basicStoredCubesList.get(basicStoredCubesList.size() - 1);
		FactTable fctbl = new FactTable(tmp_tbl. getTableName());
		fctbl.addAllAttributes(tmp_tbl);
		tmp.setFactTable(fctbl);
	}

	public void setCubeDimension(ArrayList<String> dimensionlst,
			ArrayList<String> DimemsionRefField) {
		BasicStoredCube last_cube = basicStoredCubesList.get(basicStoredCubesList.size() - 1);
		int i = 0;
		for (String item : dimensionlst) {
			int tmp = findDimensionIdByName(item);
			if (tmp == -1) {
				System.err.println("Error with Dimension At Cube construction!");
				System.exit(1);
			}
			last_cube.addDimension(this.dimensionsList.get(tmp));
			last_cube.addDimensionRefField(DimemsionRefField.get(i));
			i++;
		}
	}

	public void setCubeMeasure(ArrayList<String> measurelst,
			ArrayList<String> measureRefField) {
		BasicStoredCube last_cube = basicStoredCubesList.get(basicStoredCubesList.size() - 1);
		int i = 0;
		for (String item : measurelst) {
			String[] tmp = measureRefField.get(i).split("\\.");
			Measure msrToAdd = new Measure(i +1,item, this.dataSourceDescription.getFieldOfSqlTable(tmp[0], tmp[1]));
			last_cube.cubeMeasuresList.add(msrToAdd);
		}
	}


	private Integer findDimensionIdByName(String nameDimension) {
		int ret_val = -1;
		int i = 0;
		while (ret_val == -1 && i < this.dimensionsList.size()) {
			if (this.dimensionsList.get(i).hasSameName(nameDimension))
				ret_val = i;
			i++;
		}
		return ret_val;
	}

	public boolean returnIfTableIsDimensionTbl(String table) {
		boolean ret_value = true;
		for (BasicStoredCube basiccube : this.basicStoredCubesList) {
			if (basiccube.getFactTable().getTableName().equals(table))
				ret_value = false;
		}
		return ret_value;
	}

	public Result executeQueryToProduceResult(String queryString, Result result) {
		return dataSourceDescription.executeQueryToProduceResult(queryString, result);
	}
	
	public List<BasicStoredCube> getRegisteredCubeList(){
		return basicStoredCubesList;
	}
	
//	/*
//	 * OPEN SINCE CINECUBES:
//	 * Needs a code or parameter to finding proper cube. Now we check only the last cube in the cubebase 
//	 */
//	public Measure getMeasureInstanceByName(String name) {
//		BasicStoredCube last_cube = BasicCubes.get(BasicCubes.size() - 1);
//		for (int i = 0; i < last_cube.Msr.size(); i++) {
//			Measure msr = last_cube.Msr.get(i);
//			if (msr.getName().equals(name))
//				return msr;
//		}
//		return null;
//	}
	
	public String getChildOfGamma(String[] gamma_tmp) {
		String ret_value = null;
		for (int i = 0; i < dimensionsList.size(); i++) {
			Dimension tmp = dimensionsList.get(i);
			if (tmp.hasSameName(gamma_tmp[0])) {
				for (Hierarchy hier : tmp.getHierarchy()) {
					for (int j = 0; j < hier.levelsList.size(); j++) {
						if (hier.levelsList.get(j).getName().equals(gamma_tmp[1])) {
							if (j > 0)
								ret_value = hier.levelsList.get(j - 1).getName();
						}
					}
				}
			}
		}
		return ret_value;
	}


}
