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
	
	public Integer mode;
	public String name_creation;
	public String sqlTable;
	public ArrayList<String> dimensionList;
	public ArrayList<String> hierachyList;
	public ArrayList<String> originalLevelList;
	public ArrayList<String> customLevelList;
	public ArrayList<String> conditionList;
	public ArrayList<String> tableList;
	public ArrayList<String> groupperList;
	public ArrayList<String> measureList;
	public ArrayList<String> measureFields;
	public String aggregationFunction;
	
	/**
	 * Simply initializes the ArrayLists holding information for dimensions and queries.
	 * 
	 *  @author D. Gkesoulis
	 *  @since v0.0.0 (the Cincecubes system) 
	 */
	public ParserManager() {
		dimensionList=new ArrayList<String>();
		hierachyList=new ArrayList<String>();
		originalLevelList=new ArrayList<String>();
		customLevelList=new ArrayList<String>();
		conditionList=new ArrayList<String>();
		tableList=new ArrayList<String>();
		groupperList=new ArrayList<String>();
		measureList=new ArrayList<String>();
		measureFields=new ArrayList<String>();
		
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
		dimensionList.clear();
		hierachyList.clear();
		originalLevelList.clear();
		customLevelList.clear();
		try {
			parser.start();	
			
			/*For DIMENSION only*/
			hierachyList.addAll(parser.hierachylst);
			
			/*For CUBE only*/
			measureFields.addAll(parser.measurefields);
			measureList.addAll(parser.measurelst);
			
			/*SHARED CUBE,DIMENSION */
			dimensionList.addAll(parser.dimensionlst);
			originalLevelList.addAll(parser.originallvllst);
			customLevelList.addAll(parser.customlvllst);
			mode=parser.mode;
			name_creation=parser.name_creation;
			sqlTable=parser.sql_table;
			
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
