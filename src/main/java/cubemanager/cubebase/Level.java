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

public class Level {

    private ArrayList<LevelAttribute> levelAttributes;
    private Integer positionInHierarchy;
    private String name;
    private String levelIDAttribute;
    private String levelDescriptionAttribute;
    
    public Level(Integer position,String nm){
    	positionInHierarchy=position;
    	name=nm;
    	levelAttributes=new ArrayList<LevelAttribute>();
    }

    public String getAttributeName(int i){
    	return levelAttributes.get(i).getAttributeTable().getName();
    }
    
    public String getName() {
    	return name;
    }
    
    public void setLevelIDAttribute(String id) {
    	this.levelIDAttribute = id;
    }
    
    public String getLevelIDAttribute() {
    	return this.levelIDAttribute;
    }
    
    public void setLevelDescriptionAttribute(String descriptionAttribute) {
    	this.levelDescriptionAttribute = descriptionAttribute;
    }
    
    public String getLevelDescriptionAttribute() {
    	return this.levelDescriptionAttribute;
    }
        
    public void setLevelAttributesList(ArrayList<LevelAttribute> levelAttributes){
    	this.levelAttributes=levelAttributes;
    }

    public ArrayList<LevelAttribute> getLevelAttributesList() {
    	return this.levelAttributes;
    }

    public Integer getPositionInHierarchy() {
    	return this.positionInHierarchy;
    }
    
	public void addLevelAttribute(LevelAttribute lvlattribute) {
		levelAttributes.add(lvlattribute);
	}
}
