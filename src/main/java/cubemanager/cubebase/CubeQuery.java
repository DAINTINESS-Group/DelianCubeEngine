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
import java.util.HashSet;
//import java.util.List;

import extractionmethod.ExtractionMethod;
import result.Result;

public class CubeQuery extends Cube {

	private ArrayList<String[]> GammaExpressions; // 0->dimension_name, 1->level
	// of dimension
	private ArrayList<String[]> SigmaExpressions; // 0->dimension_name.level, 1->
	// operator , 2->VALUE
	private String AggregateFunction;
	private ExtractionMethod extractionMethod;
	private BasicStoredCube referCube;



	public CubeQuery(String Name) {
		super(Name);
		GammaExpressions = new ArrayList<String[]>();
		SigmaExpressions = new ArrayList<String[]>();
	}

	public CubeQuery(CubeQuery oldQuery){
		super("");
		GammaExpressions = new ArrayList<String[]>();
		SigmaExpressions = new ArrayList<String[]>();
		copyGammaExpressions(oldQuery);
		copySigmaExpressions(oldQuery);
		this.AggregateFunction = (oldQuery.AggregateFunction);
		this.referCube = oldQuery.referCube;
		this.cubeMeasuresList = oldQuery.cubeMeasuresList;
	}

	public ExtractionMethod getExtractionMethod() {
		return extractionMethod;
	}

	public void setExtractionMethod(ExtractionMethod extractionMethod) {
		this.extractionMethod = extractionMethod;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBasicStoredCube(BasicStoredCube referCube){
		this.referCube = referCube;
	}

	public void setAggregateFunction(String AggregateFunction){
		this.AggregateFunction = AggregateFunction;
	}

	public String getAggregateFunction() {
		return AggregateFunction;
	}

	public ArrayList<String[]> getGammaExpressions() {
		return GammaExpressions;
	}
	/**
	 * Used to calculate detailed area of query
	 * @param gammaExpressions
	 * @see InputManager
	 */
	public void setGammaExpressions(ArrayList<String[]> gammaExpressions) {
		this.GammaExpressions = gammaExpressions;
	}

	public ArrayList<String[]> getSigmaExpressions() {
		return SigmaExpressions;
	}

	public BasicStoredCube getReferCube() {
		return referCube;
	}
	
	public Level getParentLevel(String dimension, String lvlname) {
		return referCube.getParentLevel(dimension,lvlname);
	}

	public String getDimensionRefField(int index){
		return referCube.getDimensionRefFieldList().get(index);
	}

	public Result getResult() {
		return extractionMethod.getResult();
	}

	public void addGammaExpression(String table, String field) {
		String toadd[] = new String[2];
		toadd[0] = table;
		toadd[1] = field;
		GammaExpressions.add(toadd);
	}

	public void addSigmaExpression(String tablefield, String operator,
			String value) {
		String toadd[] = new String[3];
		toadd[0] = tablefield;
		toadd[1] = operator;
		toadd[2] = value;
		SigmaExpressions.add(toadd);
	}

	/**
	 * Reporting method that tells us what the cube query entails.
	 * Comes handy in many debugging circumstances
	 * 
	 * @author dgkesouli
	 * @since v.0.0 Cinecubes
	 */
	public String toString() {
		String ret_value = 
				"CubeName:" + this.referCube.getName().substring(0, this.referCube.getName().length()-5) +
				"\nName: " + this.name + 
				"\nAggrFunc:" + AggregateFunction + "\n";
		if (this.cubeMeasuresList.size() > 0 && this.cubeMeasuresList.get(0) != null
				&& this.cubeMeasuresList.get(0).getAttribute() != null)
			ret_value += "Measure:" + this.cubeMeasuresList.get(0).getAttribute().getName() + "\n";
		ret_value += "Gamma:";
		for (int i = 0; i < GammaExpressions.size(); i++) {
			if (i > 0)
				ret_value += ", ";
			if (GammaExpressions.get(i)[0].length() > 0)
				ret_value += GammaExpressions.get(i)[0] + ".";
			ret_value += GammaExpressions.get(i)[1];
		}

		ret_value += "\nSigma:";
		for (int i = 0; i < SigmaExpressions.size(); i++) {
			if (i > 0)
				ret_value += ", ";
			ret_value += SigmaExpressions.get(i)[0]
					+ SigmaExpressions.get(i)[1] + SigmaExpressions.get(i)[2];
		}
		return ret_value;
	}


	
	
	
	/* ******************** NOT USEFUL FOR THE MOMENT, KEEP THEM AS THEY CAN HELP WITH R-UP, D-D, ...  ************************* */
	public String createQuery(int i, Level parentLvl) {
		String dimension = SigmaExpressions.get(i)[0].split("\\.")[0];
		String lvlname = SigmaExpressions.get(i)[0].split("\\.")[1];
		String table = referCube.getSqlTableByDimensionName(dimension);
		String field = referCube.getSqlFieldByDimensionLevelName(dimension, lvlname);
		String field2 = parentLvl.getAttributeName(0);
		String tmp_query="SELECT DISTINCT "+field2+ " FROM "+table+" WHERE "+field+"="+ SigmaExpressions.get(i)[2];
		return tmp_query;
	}

	
	public Level getNameParentLevel(int i){
		String dimension = SigmaExpressions.get(i)[0].split("\\.")[0];
		String level = SigmaExpressions.get(i)[0].split("\\.")[1];
		return referCube.getParentLevel(dimension, level);
	}

	public int getIndexOfSigma( String gamma_dim) {
		int ret_value=-1;
		int i=0;
		for(String[] sigma : SigmaExpressions ){
			if(sigma[0].split("\\.")[0].equals(gamma_dim)) ret_value=i;
			i++;
		}
		return ret_value;
	}

	public void addMeasure(int id, String name){
		Measure msrToAdd = new Measure(id, name, null);
		cubeMeasuresList.add( msrToAdd);
	}

	public boolean checkIfSigmaExprIsInGamma(int toChange) {
		boolean ret_value=false;
		String [] tmp = SigmaExpressions.get(toChange)[0].split("\\.");
		for(String [] gammaExpr : GammaExpressions){
			if(gammaExpr[0].equals(tmp[0]))
				ret_value=true; 
		}
		return ret_value;
	}

	public int getGammaPositionOfSigma(int toChange) {
		int ret_value=0;
		String [] tmp = SigmaExpressions.get(toChange)[0].split("\\.");
		for(int i=  0; i < GammaExpressions.size(); i++) {
			String[] gammaExpr= GammaExpressions.get(i);
			if(gammaExpr[0].equals(tmp[0])) {
				ret_value=i;
				break;
			}
		}
		return ret_value;
	}

	private void copyGammaExpressions(CubeQuery oldQuery){
		for(int i = 0; i < oldQuery.GammaExpressions.size(); i++){
			String[] old = oldQuery.GammaExpressions.get(i);
			String[] toadd = new String[old.length];
			for(int j = 0;j < old.length; j++){
				toadd[j] = old[j];
			}
			GammaExpressions.add(toadd);
		}
	}


	private void copySigmaExpressions(CubeQuery oldQuery){
		for(int i = 0; i < oldQuery.SigmaExpressions.size(); i++){
			String[] old = oldQuery.SigmaExpressions.get(i);
			String[] toadd = new String[old.length];
			for(int j = 0;j < old.length; j++){
				toadd[j] = old[j];
			}
			SigmaExpressions.add(toadd);
		}
	}



	public boolean doDrillInColVersion(CubeBase cubeBase, String[] valuesToChange,
			HashSet<String> row_per_col, CubeQuery newQuery) {/*  valuesToChange[0]->Row
			 * Value,valuesToChange[1]->Column Value */

		String[] gamma_tmp = GammaExpressions.get(0); // Row Dimension

		int index_sigma_change_bygamma = getIndexOfSigmaToDelete(gamma_tmp[0]);// getRow Sigma							
		if (index_sigma_change_bygamma > -1) {
			newQuery.SigmaExpressions.get(index_sigma_change_bygamma)[0] = newQuery.GammaExpressions
					.get(0)[0] + "." + newQuery.GammaExpressions.get(0)[1];
			newQuery.SigmaExpressions.get(index_sigma_change_bygamma)[2] = "'"
					+ valuesToChange[1] + "'";
		}
		String child_level_of_gamma = cubeBase.getChildOfGamma(gamma_tmp);
		if (child_level_of_gamma != null) {
			newQuery.GammaExpressions.get(0)[1] = child_level_of_gamma;
			newQuery.GammaExpressions.set(0, newQuery.GammaExpressions
					.set(1,	newQuery.GammaExpressions.get(0)));
		} else 
			return false;
		return true;
	}

	public boolean doDrillInRowVersion(CubeBase cubeBase, String[] valuesToChange,
			HashSet<String> cols, CubeQuery newQuery) { /* valuesToChange[0]->Row
			 * Value,valuesToChange[1]->Column Value */

		String[] gamma_tmp = GammaExpressions.get(1); //column dimension
		int index_sigma_change_bygamma = getIndexOfSigmaToDelete(gamma_tmp[0]);
		String child_level_of_gamma = cubeBase.getChildOfGamma( gamma_tmp);

		if (index_sigma_change_bygamma > -1) {
			newQuery.SigmaExpressions.get(index_sigma_change_bygamma)[0] = newQuery.GammaExpressions
					.get(1)[0] + "." + newQuery.GammaExpressions.get(1)[1];
			newQuery.SigmaExpressions.get(index_sigma_change_bygamma)[2] = "'"
					+ valuesToChange[1] + "'";
		}

		if (child_level_of_gamma != null) {
			newQuery.GammaExpressions.get(1)[1] = child_level_of_gamma;

		} else
			return false;
		return true;
	}

	public int getIndexOfSigmaToDelete(String gamma_dim) {
		int ret_value = -1;
		int i = 0;
		for (String[] sigma : SigmaExpressions) {
			if (sigma[0].split("\\.")[0].equals(gamma_dim))
				ret_value = i;
			i++;
		}
		return ret_value;
	}

	public String getSigmaTextForIntro(){
		String retTxt= "";
		int i=0;
		for(String [] sigma: SigmaExpressions) {
			if(i == SigmaExpressions.size() - 1)
				retTxt += " and ";
			else if(i>0)
				retTxt+=", ";
			retTxt += sigma[0].split("\\.")[0].replace("_dim","").replace("lvl", "level ")+" is fixed to "+sigma[2].replace("*", "ALL");
			i++;
		}
		return retTxt;
	}

	public String getSigmaTextForOriginalAct1(){
		String dimensionText= "";
		int j = 0;
		for(String[] sigma: SigmaExpressions){
			if(j > 0)
				dimensionText += ", ";
			if(j == SigmaExpressions.size() - 1) 
				dimensionText+=" and ";
			dimensionText += sigma[0].split("\\.")[0].replace("_dim", "")+" to be equal to "+sigma[2].replace("*", "ALL")+"";
			j++;
		}
		return dimensionText;
	}

	public String getGammaTextForOriginalAct1(){
		String dimensionText= "";
		int j = 0;
		for(String[] gamma: GammaExpressions){
			if(j > 0)
				dimensionText += ", ";
			if(j==GammaExpressions.size()-1)
				dimensionText += " and ";
			dimensionText+=gamma[0].replace("_dim", "")+" at "+gamma[1].replace("lvl", "level ");
			j++;
		}
		return dimensionText;
	}



	
	 
} //end of class CubeQuery




