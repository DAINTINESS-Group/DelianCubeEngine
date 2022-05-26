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

import cubemanager.relationalstarschema.Attribute;

public class LevelAttribute{
    
	private String levelAttributeName;
	private String levelAttributeDatasource;
    private Level levelToPoint;
    private Attribute attributeTable;
    private String attributeType;
  
    public LevelAttribute(String name, String datasource){
    	setLevelAttributeName(name);
    	setLevelAttributeDatasource(datasource);
    }
    
	public String getLevelAttributeName() {
		return levelAttributeName;
	}

	public void setLevelAttributeName(String lvlAttrName) {
		this.levelAttributeName = lvlAttrName;
	}

	public String getLevelAttributeDatasource() {
		return levelAttributeDatasource;
	}

	public void setLevelAttributeDatasource(String datasource) {
		levelAttributeDatasource = datasource;
	}

	public Level getLevel() {
		return levelToPoint;
	}

	public void setLevel(Level level) {
		this.levelToPoint = level;
	}

	public void setAttributeTable(Attribute attribute) {
		this.attributeTable = attribute;
	}

	public Attribute getAttributeTable() {
		return attributeTable;
	}
	
	public void setAttributeType(String type) {
		this.attributeType = type;
	}
	
	public String getAttributeType() {
		return this.attributeType;
	}
    
}
