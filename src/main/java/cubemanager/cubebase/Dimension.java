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

import cubemanager.relationalstarschema.DimensionTable;

public class Dimension{
    /**
	 * @uml.property  name="hierachy"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="CubeMgr.CubeBase.Level"
	 */
	private String name;
	private DimensionTable dimensionDataSourceTable;
    private ArrayList<Hierarchy> hierachyList;
    private String dimensionType;
	
    
    public Dimension(String nameDim){
    	name=nameDim;
    	hierachyList=new ArrayList<Hierarchy>();
    }
    
    public boolean hasSameName(String dimensionName){
    	return name.equals(dimensionName);
    }
    
    /**
	 * @return
	 * @uml.property  name="dimTbl"
	 */
    public String getTableName() {
            return dimensionDataSourceTable.getTableName();
    }

    /**
	 * @param dimTbl
	 * @uml.property  name="dimTbl"
	 */
    public void setDimensionTable(DimensionTable dimTbl) {
            dimensionDataSourceTable = dimTbl;
    }
    
    public void setDimensionType(String dimensionType) {
    	this.dimensionType = dimensionType;
    }
    
    public String getDimensionType() {
    	return this.dimensionType;
    }

    public ArrayList<Hierarchy> getHierarchy() {
            return hierachyList;
    }
    
    public String getName() {
    	return name;
    }

    public void setHierarchy(ArrayList<Hierarchy> hier) {
    	hierachyList = hier;
    }    
}
