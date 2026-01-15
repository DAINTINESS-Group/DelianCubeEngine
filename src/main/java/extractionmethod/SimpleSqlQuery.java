package extractionmethod;

import java.sql.ResultSet;
import java.util.ArrayList;

import result.Result;

public class SimpleSqlQuery extends ExtractionMethod {
	//private String[] SelectClauseMeasure;  	/* 0-->AggregateFuncName, 1--> field */  
	private ArrayList<String[]> selectMeasures;
	
	private ArrayList<String[]> Selection;
	private ArrayList<String[]> FromClause; 	/* 0-->TABLE, 1-->customName */
	private ArrayList<String[]> WhereClause;	/* 0-->sqlfld1,1-->op,2-->sqlfld2 */
	private ArrayList<String[]> GroupByClause;
	//TODO: explicitly add OrderByClause
	private ResultSet resultSet;
	
	private void init(){
		Selection = new ArrayList<String[]>();
		FromClause=new ArrayList<String[]>();
		WhereClause=new ArrayList<String[]>();
		GroupByClause=new ArrayList<String[]>();
		
		//SelectClauseMeasure=new String[2];	
		selectMeasures = new ArrayList<String[]>();
	}

	/**
	 * ATTN: includes a <code>super()</code> call, that creates also the {@link Result} of the query!
	 * Also calls <code>init()</code> that performs the <codenew</code> construction calls for all the members,
	 * 
	 * @see ExtractionMethod#ExtractionMethod()
	 */
	public SimpleSqlQuery(){
		super();
		init();
	}
	
	public ArrayList<String[]> getFromClauseArrayList(){
		return FromClause;
	}

	/*
	 * public String[] getSelectClauseMeasure(){ return SelectClauseMeasure; }
	 */ 
	public ArrayList<String[]> getSelectMeasures(){
		return selectMeasures;
	}

	public ArrayList<String[]> getGroupByClause(){
		return GroupByClause;
	}

	public ArrayList<String[]> getWhereClauseArrayList(){
		return WhereClause;
	}

//ATTN: the array SelectClauseMeasure is NOT the full SELECT clause, but just the AggF + Measure to produce AggF(Measure) as measure
	/*
	 * public String getSelectClause(){ String ret_value="";
	 * ret_value+=getGroupClause()+","; ret_value+=
	 * SelectClauseMeasure[0]+"("+SelectClauseMeasure[1]
	 * +") as measure,COUNT(*) as countOfDetailedCells"; return ret_value; }
	 */
	public String getSelectClause(){
		String ret_value = "";
		ret_value += getGroupClause() + ",";
		
		for(int i = 0; i < selectMeasures.size(); i++) {
			String[] measure = selectMeasures.get(i);
			String func = measure[0];
			String attr = measure[1];
			String alias = (measure.length > 2) ? measure[2] : null;
			
			//Reconstruct Func(Attr)
			String expr;
			if(func == null || func.isEmpty()) expr = attr;
			else expr = func + "(" + attr + ")";
			
			ret_value += " " + expr;
			
			if (alias != null && !alias.isEmpty()) {
				ret_value += " AS " + alias;
			} else if (!expr.toUpperCase().contains(" AS ")) {
				if(i == 0) ret_value += " as measure";
				else ret_value += " as measure" + (i+1);
			}
			ret_value += ",";
		}
		
		ret_value += "COUNT(*) as countOfDetailedCells";
		return ret_value;
	}
	
	
	/*
	 * public String getSelectClauseWithNoGroupers(){ String ret_value=""; int i=0;
	 * 
	 * for(String[] x : Selection ){ if(i>0) ret_value+=",";
	 * ret_value+=mergeStringTable(x); i++; }
	 * 
	 * ret_value+= ", " + "("+SelectClauseMeasure[1]+") as measure"; return
	 * ret_value; }
	 */
	public String getSelectClauseWithNoGroupers(){
		String ret_value = "";
		int i = 0;

		for(String[] x : Selection ){
			if(i > 0) ret_value += ",";
			ret_value += mergeStringTable(x);
			i++;
		}
		
		for(int k = 0; k < selectMeasures.size(); k++) {
			String[] measure = selectMeasures.get(k);
			
			String attr = measure[1]; 
			String alias = (measure.length > 2) ? measure[2] : null;
			
			ret_value += ", " + "(" + attr + ")";
			
			if (alias != null && !alias.isEmpty()) {
				ret_value += " AS " + alias;
			} else {
				if(k == 0) ret_value += " as measure";
				else ret_value += " as measure" + (k+1);
			}
		}
		
		return ret_value;
	}

	public String getFromClause(){
		int i=0;
		String ret_value="";

		for(String[] x : FromClause ) {
			if(i>0) ret_value+=",";
			ret_value+=mergeStringTable(x);
			i++;
		}
		return ret_value;
	}

	public String getWhereClause(){
		int i=0;
		String ret_value="";

		for(String[] x : WhereClause ) {   
			if(x[2].equals("'ALL'")) {
				continue;
			}
			if(i>0) ret_value+=" AND ";
			ret_value+=mergeStringTable(x);
			i++;
		}
		return ret_value;
	}

	public String getGroupClause(){/*add this to play groupper=1 to select clause*/
		String ret_value="";
		int i=0;

		for(String[] x : GroupByClause ){
			if(i>0) ret_value+=",";
			ret_value+=mergeStringTable(x);
			i++;
		}

		return ret_value;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	
	public void printQuery(){
		System.out.println(toString());
	}
	
	/**
	 * Returns the SQL expression of the Cube query.
	 * <p>
	 * Used for the execution of the query, as well as the debugging of what is actually executed towards the underlying database.
	 * 
	 * 
	 * @since 0.0.0 (extracted from the Cinecubes system
	 */
	public String toString(){
		//Waiting for refactoring, to fix groupers 
		//@version 0.0.1 (ORDER BY groupers rather than by measure)
		String ret_value;
		if(getGroupClause().equals("")) {
			ret_value="SELECT "+getSelectClauseWithNoGroupers();
			ret_value+="\nFROM "+ getFromClause();
			ret_value+="\nWHERE "+getWhereClause(); 
			//ret_value+=getOrderClauseByMeasure(0);
			//ret_value+=getOrderClauseByGroupers(0);
			//if(getGroupClause().length()>0)	return ret_value ;
			//else return "SELECT '"+this.SelectClauseMeasure[0]+"'";
		}
		else {
			ret_value="SELECT "+getSelectClause();
			ret_value+="\nFROM "+ getFromClause();
			ret_value+="\nWHERE "+getWhereClause();
			ret_value+="\nGROUP BY "+getGroupClause(); 
			//ret_value+=getOrderClauseByMeasure(0);
			//ret_value+=getOrderClauseByGroupers(0);
			//if(getGroupClause().length()>0)	return ret_value ;
			//else return "SELECT '"+this.SelectClauseMeasure[0]+"'";
		}
		
		return ret_value ;
	}
	
	/*  order_type=0 -> ASCENING
	 *  order_type=1 -> DESCENDING
	 */
	public String getOrderClauseByMeasure(int order_type){
		String sortColumn = "measure";
		if(selectMeasures != null && !selectMeasures.isEmpty()) {
			String[] m = selectMeasures.get(0);
			if(m.length > 2 && m[2] != null && !m[2].isEmpty()) {
				sortColumn = m[2];
			}
		}
		
		if(order_type==0) return "\nORDER BY " + sortColumn + " ASC";
		return "\nORDER BY " + sortColumn + " DESC";
	}
	
	/**
	 * Waiting to be used.
	 * 
	 * 2018-08-21: Have fixed dates in the pkdd99 database (months are listed as YYY-MM now)
	 * Can now: sort by groupers in toString()
	 * @param order_type
	 * @return
	 */
	
	public String getOrderClauseByGroupers(int order_type){
		String orderClauseString = "\nORDER BY "+ getGroupClause(); 
		if(order_type==0) return orderClauseString + " ASC";
		return orderClauseString + " DESC";
	}

	private String mergeStringTable(String[] tomerge){
		String ret_value="";
		for(int i=0;i<tomerge.length;i++){
			ret_value+=" "+tomerge[i];
		}
		return ret_value;
	}

	/*
	 * @Override public void addReturnedFields(String aggregationFuction, String
	 * attribute){ SelectClauseMeasure[0] =aggregationFuction;
	 * SelectClauseMeasure[1]= attribute; }
	 */
	@Override
	public void addReturnedFields(String aggregationFuction, String attribute, String alias){
		String safeFunc = (aggregationFuction == null) ? "" : aggregationFuction;
		String safeAttr = (attribute == null) ? "" : attribute;
		String safeAlias = (alias == null) ? "" : alias;
	    
	    this.selectMeasures.add(new String[] {safeFunc, safeAttr, safeAlias});
		
		if(this.getResult() != null) {
			this.getResult().setNumMeasures(this.selectMeasures.size());
		}
	}


	@Override
	public boolean compareExtractionMethod(ExtractionMethod toCompare) {
		int count=0;
		SimpleSqlQuery tocomp=(SimpleSqlQuery)toCompare;
		for(int i=0;i<this.WhereClause.size();i++){
			if(tocomp.WhereClause.get(i)[0]==this.WhereClause.get(i)[0]){
				if(tocomp.WhereClause.get(i)[1]==this.WhereClause.get(i)[1]){
					if(tocomp.WhereClause.get(i)[2]==this.WhereClause.get(i)[2]){
						count++;
					}
				}
			}
		}
		return (count == this.WhereClause.size() ? true : false);
	}

	@Override
	public void addFilter(String[] index) {
		this.WhereClause.add(index);
	}

	@Override
	public void addGroupers(String[] index) {
		this.GroupByClause.add(index);
	}

	@Override
	public void addSourceCube(String[] index) {
		this.FromClause.add(index);
	}

	@Override
	public void addSelection(String[] index) {
		this.Selection.add(index);
		
	}

}
