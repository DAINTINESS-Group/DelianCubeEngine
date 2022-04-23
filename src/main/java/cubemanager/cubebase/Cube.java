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
import java.util.List;

public class Cube {

	protected String name;
	protected List<Level> cubeLevelsList;
	protected List<Measure> cubeMeasuresList;
	protected List<Dimension> cubeDimensionsList;
	protected ArrayList<String> dimensionRefFieldList; 
	
	public Cube(String cubeName) {
		this.name = cubeName;
		cubeLevelsList = new ArrayList<Level>();
		cubeMeasuresList = new ArrayList<Measure>();
		cubeDimensionsList = new ArrayList<Dimension>();
		dimensionRefFieldList = new ArrayList<String>();
	}

	public String getName(){
		return name;
	}
	
	public List<Level> getLevelsList(){
		return cubeLevelsList;
	}
	
	public List<Measure> getMeasuresList(){
		return cubeMeasuresList;
	}
	
	public List<Dimension> getDimensionsList(){
		return cubeDimensionsList;
	} 
	
	public ArrayList<String> getDimensionRefFieldList() {
		return dimensionRefFieldList;
	}

	public void setMeasuresList(List<Measure> measuresList) {
		this.cubeMeasuresList = measuresList;
	}
	
	public void addMeasure(Measure measure) {
		cubeMeasuresList.add(measure);
	}

	public void addDimension(Dimension dimension) {
		this.cubeDimensionsList.add(dimension);
	}

	public void addDimensionRefField(String namefield) {
		this.dimensionRefFieldList.add(namefield);
	}

	/*
	 * 
	 * This function return Parent Level Parameters dimension dimension name
	 * level name of level witch need to find Parent
	 */
	
	public Level getParentLevel(String dimension, String level) {
		List<Dimension> dimensions = this.cubeDimensionsList;
		for (int j = 0; j < dimensions.size(); j++) { // for each dimension
			if (dimensions.get(j).hasSameName(dimension)) {
				ArrayList<Hierarchy> current_hierachy = dimensions.get(j)
						.getHierarchy();
				for (int k = 0; k < current_hierachy.size(); k++) {// for each
																	// hierarchy
																	// of
																	// dimension
					List<Level> current_lvls = current_hierachy.get(k).levelsList;
					for (int l = 0; l < current_lvls.size(); l++) {
						if (current_lvls.get(l).getName().equals(level)) {
							if (l < current_lvls.size() - 1)
								return current_lvls.get(l + 1);
							else if (l + 1 < current_lvls.size())
								return current_lvls.get(l + 1);
						}
					}
				}
			}
		}
		return null;
	}

	/*
	 * 
	 * This function return Dimension Table Parameters dimension dimension name
	 * we need the table name
	 */
	public String getSqlTableByDimensionName(String dimension) {
		List<Dimension> dimensions = this.cubeDimensionsList;
		for (int j = 0; j < dimensions.size(); j++) { // for each dimension
			if (dimensions.get(j).hasSameName(dimension)) {
				return dimensions.get(j).getTableName();
			}
		}
		return "";
	}

	/*
	 * 
	 * This function return Attribute Name of Level Parameters dimension
	 * dimension name we need the table name
	 */
	public String getSqlFieldByDimensionLevelName(String dimension, String level) {
		List<Dimension> dimensions = this.cubeDimensionsList;
		for (int j = 0; j < dimensions.size(); j++) { // for each dimension
			if (dimensions.get(j).hasSameName(dimension)) {
				ArrayList<Hierarchy> current_hierachy = dimensions.get(j).getHierarchy();
				for (int k = 0; k < current_hierachy.size(); k++) {// for each
																	// hierarchy
																	// of
																	// dimension
					List<Level> current_lvls = current_hierachy.get(k).levelsList;
					for (int l = 0; l < current_lvls.size(); l++) {
						if (current_lvls.get(l).getName().equals(level))
							return current_lvls.get(l).getAttributeName(0);
					}
				}
			}
		}
		return "";
	}
}
