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


package parsermgr;


import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

/**
 * The main parsing class that returns the parts of the parsed string.
 * 
 * @author D. Gkesoulis
 * @version 0.0.1
 * @since v0.0.0 (the Cincecubes system) 
 *
 */
public class ParserManager {
	
	public String sourceType;
	public String iniFilePath;
	public String mode;
	public String creationName;
	public String datasourceTable;
	public HashMap<String, String> levelID;
	public HashMap<String, String> levelDescription;
	public ArrayList<String> dimensionList;
	public ArrayList<String> hierarchyList;
	public ArrayList<String> originalLevelList;
	public ArrayList<String> customLevelList;
	public String dimensionType;
	public HashMap<String, ArrayList<String>> levelAttributes;
	public HashMap<String, String> attributeTypes;
	public ArrayList<String> conditionList;
	public ArrayList<String> tableList;
	public ArrayList<String> groupperList;
	public ArrayList<String> measureList;
	public ArrayList<String> measureFields;
	public ArrayList<String> dimensionsAtCubeDataSource;
	public String aggregationFunction;	
	

	/**
	 * Simply initializes the ArrayLists holding information for dimensions and queries.
	 * 
	 *  @author D. Gkesoulis
	 *  @since v0.0.0 (the Cincecubes system) 
	 */
	public ParserManager() {
		
		sourceType = null;
		iniFilePath = null;
		mode = null;
		creationName = null;
		datasourceTable = null;
		levelID = new HashMap<String, String>();
		levelDescription = new HashMap<String, String>();
		dimensionList=new ArrayList<String>();
		hierarchyList=new ArrayList<String>();
		originalLevelList=new ArrayList<String>();
		customLevelList=new ArrayList<String>();
		dimensionType = null;
		levelAttributes = new HashMap<String, ArrayList<String>>();
	    attributeTypes = new HashMap<String, String>();
	    conditionList=new ArrayList<String>();
	    tableList=new ArrayList<String>();
	    groupperList=new ArrayList<String>();
	    measureList=new ArrayList<String>();
	    measureFields=new ArrayList<String>();
	    dimensionsAtCubeDataSource=new ArrayList<String>();
	    aggregationFunction = null;
		
		
	}
	
	/**
	 * Returns void, yet it initializes the appropriate fields of this class.
	 * 
	 *  @author D. Gkesoulis
	 *  @see CubeSqlLexer
	 *  @see CubeSqlParser
	 *  @since v0.0.0 (the Cincecubes system) 
	 */

	public void parse(String toParse){
		CharStream stream =	new ANTLRStringStream(toParse);
		CubeSqlLexer lexer = new CubeSqlLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		CubeSqlParser parser = new CubeSqlParser(tokenStream);
		levelID.clear();
		levelDescription.clear();
		hierarchyList.clear();
		originalLevelList.clear();
		customLevelList.clear();
		levelAttributes.clear();
		attributeTypes.clear();
		try {
			parser.start();	
			
			sourceType = parser.sourceType;
			iniFilePath = parser.iniFilePath;
			/*For DIMENSION only*/
			hierarchyList.addAll(parser.hierarchylst);
			dimensionType = parser.dimensionType;
			
			/*For CUBE only*/
			measureFields.addAll(parser.measurefields);
			measureList.addAll(parser.measurelst);
			dimensionsAtCubeDataSource.addAll(parser.dimensionsAtCubeDataSource);
			
			/*SHARED CUBE,DIMENSION */
			mode=parser.mode;
			creationName=parser.name_creation;
			datasourceTable=parser.datasource_table;
			dimensionList.addAll(parser.dimensionlst);
			for(int i=0; i<parser.originallvllst.size(); i++) {
				originalLevelList.add(datasourceTable+"."+parser.originallvllst.get(i));
			}
			customLevelList.addAll(parser.customlvllst);
			
			
			/*For LEVEL ATTRIBUTES only*/
			levelAttributes.putAll(parser.levelAttributes);
			levelID.putAll(parser.levelID);
			levelDescription.putAll(parser.levelDescription);
			//levelAttributes = parser.levelAttributes;
			attributeTypes.putAll(parser.attributeTypes);
			//attributeTypes = parser.attributeTypes;
			
			/* SQL QUERY STAFF */ 
			aggregationFunction=parser.aggregatefunc;
			conditionList.addAll(parser.conditionlst);
			tableList.addAll(parser.tablelst);
			groupperList.addAll(parser.groupperlst);
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

	}

}
