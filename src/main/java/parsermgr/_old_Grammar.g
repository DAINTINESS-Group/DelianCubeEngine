grammar CubeSql;

options {
  output = Java;
}

tokens{
  DATASOURCE;TYPE;COLON;WITH;INI;FILE;PATH; SLASH; BACKSLASH;
  CREATE;CUBE;REFERENCES;DIMENSION;LEVEL;ATTRIBUTES;ID;DESCRIPTION;
  DIMENSION_TYPE;LIST;OF;LEVELS;AS;HIERARCHY;NAME;
  LBRACE;RBRACE;DOT;COMMA;CHILDOF;QUESTMARK;UNDERSCORE;WS;AT;
  SELECT;OR;AND;WHERE;AS;GROUP;BY;MIN;MAX;COUNT;SUM;AVG;
}

@header{
  package parsermgr;
  
  import java.util.ArrayList;
  import java.util.HashMap;
}

@parser::members{
  String sourceType;
  String iniFilePath;
  String mode;
  String name_creation;
  String datasource_table;
  String levelName;
  HashMap<String, String> levelID;
  HashMap<String, String> levelDescription;
  ArrayList<String> dimensionlst;
  ArrayList<String> hierarchylst;
  ArrayList<String> originallvllst;
  ArrayList<String> customlvllst;
  ArrayList<String> dimensionsAtCubeDataSource;
  ArrayList<String> attributes;
  HashMap<String, ArrayList<String>> levelAttributes;
  HashMap<String, String> attributeTypes;
  HashMap<String, String> attributeDatasource;
  ArrayList<String> conditionlst;
  ArrayList<String> tablelst;
  ArrayList<String> groupperlst;
  ArrayList<String> measurelst;
  ArrayList<String> measurefields;
  String aggregatefunc;
  String tmp_con;
  boolean group;
  String dimensionType;
  
}

@lexer::header{
  package parsermgr;
}

start :{
    mode="";
    tmp_con="";
    group=false;
    dimensionlst=new ArrayList<String>();
    hierarchylst=new ArrayList<String>();
    originallvllst=new ArrayList<String>();
    customlvllst=new ArrayList<String>();
    levelID = new HashMap<String, String>();
    levelDescription = new HashMap<String, String>();
    attributes = new ArrayList<String>();
    levelAttributes = new HashMap<String, ArrayList<String>>();
    attributeTypes = new HashMap<String, String>();
    attributeDatasource = new HashMap<String, String>();
    conditionlst=new ArrayList<String>();
    groupperlst=new ArrayList<String>();
    tablelst=new ArrayList<String>();
    measurelst=new ArrayList<String>();
    measurefields=new ArrayList<String>();
    dimensionsAtCubeDataSource=new ArrayList<String>();
  } parse; 

parse :  	{sourceType=""; iniFilePath="";} source_type_statement 
	|		({  mode="";
			levelName="";
            name_creation="";
            datasource_table="";
            dimensionType = "other";
            if(hierarchylst.size()>0)  hierarchylst.clear();
            if(originallvllst.size()>0)  originallvllst.clear();
            if(customlvllst.size()>0)  customlvllst.clear();
            if(attributes.size()>0) attributes.clear();
            if(levelAttributes.size()>0) levelAttributes.clear();
            if(attributeTypes.size()>0) attributeTypes.clear();
            if(attributeDatasource.size()>0) attributeDatasource.clear();
            if(measurelst.size()>0) measurelst.clear();
            if(measurefields.size()>0) measurefields.clear();
            if(dimensionsAtCubeDataSource.size()>0) dimensionsAtCubeDataSource.clear();
           }
            creation_statement )* 
   |      sql_query
   ;

source_type_statement: DATASOURCE TYPE COLON NAME {sourceType=$NAME.text;} WITH INI FILE COLON PATH {iniFilePath=$PATH.text;};

creation_statement : creation QUESTMARK;

creation: creation_cube | creation_dimension;

creation_cube : create_statement datasource_statement measure_statement referdimension_statement;

creation_dimension : create_statement levels_statement hierarchy_statement datasource_statement |
					 create_statement levels_statement hierarchy_statement dimension_type_statement datasource_statement;

create_statement: CREATE CUBE {mode="Cube";} NAME {name_creation=$NAME.text;}
                  | 
                  CREATE DIMENSION {mode="Dimension";} NAME {name_creation=$NAME.text.replace("~"," "); dimensionlst.add($NAME.text);};
                  
datasource_statement : DATASOURCE NAME {datasource_table=$NAME.text;} ;

referdimension_statement : REFERENCES DIMENSION dimensions;

measure_statement : MEASURES measures;

levels_statement: LIST OF LEVELS LBRACE levels RBRACE ;

hierarchy_statement : HIERARCHY hierarchy;

dimension_type_statement: DIMENSION_TYPE NAME {dimensionType = $NAME.text;};

dimensions : dimension  (comma_statement dimension)* ;

dimension : NAME AT sqlfield;

measures : measure (comma_statement measure)*;

measure : NAME {measurelst.add($NAME.text);} AT measureName=PATH {measurefields.add($measureName.text);};

levels: level (comma_statement level)*;

level : CREATE LEVEL sqlfield attributes_statement WITH ID COLON idName=NAME {levelID.put(levelName, $idName.text);} AND DESCRIPTION COLON descrName=NAME {levelDescription.put(levelName, $descrName.text);};

attributes_statement: WITH ATTRIBUTES LBRACE attributes RBRACE;

attributes: {attributes.clear();} attribute (comma_statement attribute)* {levelAttributes.put(levelName, (ArrayList)attributes.clone()); originallvllst.add(attributeDatasource.get(attributes.get(0)));};

attribute: attrName=NAME {attributes.add($attrName.text);} 
		   COLON typeName=NAME {attributeTypes.put(($attrName.text), ($typeName.text));}
		   datasource_statement {attributeDatasource.put(($attrName.text), datasource_table);}
		 | attrName1=NAME DOT attrName2=NAME {attributes.add($attrName1.text+"."+$attrName2.text);} 
		   COLON typeName=NAME {attributeTypes.put(($attrName1.text+"."+$attrName2.text), ($typeName.text));}
		   datasource_statement {attributeDatasource.put(($attrName.text), datasource_table);};

comma_statement: WS* COMMA WS*;

hierarchy: name1=NAME {hierarchylst.add($name1.text);} (CHILDOF name2=NAME {hierarchylst.add($name2.text);} )+;

sqlfield : name1=NAME DOT name2=NAME { tmp_con+=$name1.text+"."+$name2.text;
									   if(mode.equals("Dimension")){ customlvllst.add($name1.text+"."+$name2.text);
                                       levelName = $name1.text+"."+$name2.text;}
                                       if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add($name1.text+"."+$name2.text);}}
        | NAME {tmp_con+= $NAME.text;
        		if(mode.equals("Dimension")){ customlvllst.add($NAME.text); levelName=$NAME.text;}
        		if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add($NAME.text);}}
        
        | name=PATH {tmp_con+= $name.text;
        			 if(mode.equals("Dimension")){ customlvllst.add($name.text); levelName=$name.text;}
        			 if(mode.equals("Cube")){ dimensionsAtCubeDataSource.add($name.text);}};



        
sql_query: select_statement 
           from_statement
           where_statement
           group_statement;
           
select_statement: SELECT grouppers COMMA aggregate_statement;

aggregate_statement : {tmp_con="";} aggregate_func '(' sqlfield ')' {aggregatefunc=tmp_con;tmp_con="";};

aggregate_func : SUM {tmp_con=$SUM.text+"~";}
                | AVG {tmp_con=$AVG.text+"~";} 
                | COUNT {tmp_con=$COUNT.text+"~";}
                | MAX {tmp_con=$MAX.text+"~";}
                | MIN {tmp_con=$MIN.text+"~";};

from_statement : FROM tables;

tables : table (COMMA table)*;

table : name1=NAME AS name2=NAME  {tablelst.add($name1.text+"~"+$name2.text);}
      | name1=NAME name2=NAME {tablelst.add($name1.text+"~"+$name2.text);}
      | NAME {tablelst.add($NAME.text);};

where_statement : WHERE {tmp_con="";} cond_statement {conditionlst.add(tmp_con);} (boolean_expr {tmp_con="";} cond_statement {conditionlst.add(tmp_con);})*
                  | ;
cond_statement : sqlfield operator {tmp_con+="~"+$operator.text+"~";} sqlfield 
                | sqlfield operator {tmp_con+="~"+$operator.text+"~";}  quote_statement NAME quote_statement {tmp_con+="'"+$NAME.text+"'";}
                | sqlfield operator {tmp_con+="~"+$operator.text+"~";}  quote_statement (Digit)+ NAME  quote_statement {tmp_con+="'"+$Digit.text+$NAME.text+"'";}
                | sqlfield operator {tmp_con+="~"+$operator.text+"~";} (Digit)+ {tmp_con+=$Digit.text;}
                | sqlfield operator {tmp_con+="~"+$operator.text+"~";}  quote_statement  quote_statement {tmp_con+="'"+"'";};
                
                
boolean_expr : OR | AND;

quote_statement: '"' | '\'' ;

operator : '=' 
          | '>=' 
          | '<=' 
          | '>'
          | '<'
          | 'LIKE';

group_statement : GROUP BY {group=true;} grouppers {group=false;};

grouppers : {tmp_con="";} sqlfield {if(group){ groupperlst.add(tmp_con);}} (comma_statement {tmp_con="";} sqlfield {if(group)groupperlst.add(tmp_con);} );

OR : O R;

AND : A N D;

DATASOURCE: D A T A S O U R C E;

TYPE: T Y P E;

COLON: ':';

INI: I N I;

FILE: F I L E;

PATH: NAME (NAME | DOT | SLASH | BACKSLASH)* DOT NAME;
SLASH: '/';
BACKSLASH: '\'';


CREATE: C R E A T E;

CUBE :  C U B E ;

REFERENCES: R E F E R E N C E S;

DIMENSION : D I M E N S I O N ;

DIMENSION_TYPE: D I M E N S I O N UNDERSCORE T Y P E;

LIST: L I S T;

OF: O F;

LEVELS:L E V E L S;

LEVEL: L E V E L;

WITH: W I T H;

ATTRIBUTES: A T T R I B U T E S;

ID: I D;

DESCRIPTION: D E S C R I P T I O N;

AS: A S;

AT :A T;

HIERARCHY: H I E R A R C H Y;

SELECT : S E L E C T;

AVG : A V G;

SUM : S U M;

MAX : M A X;

MIN : M I N;

COUNT : C O U N T;

WHERE : W H E R E;

FROM : F R O M;

GROUP : G R O U P;

BY: B Y;

MEASURES : M E A S U R E S;

NAME: Letter (Letter | Digit | '_'  | '-' | '~' )*;

LBRACE:'{';
RBRACE:'}';
DOT:'.';
COMMA:',';
CHILDOF:'>';
QUESTMARK:';';
UNDERSCORE:'_';

fragment Digit :  '0'..'9';
fragment Letter :  'a'..'z' |  'A'..'Z';
fragment A:'A'|'a';
fragment B:'B'|'b';
fragment C:'C'|'c';
fragment D:'D'|'d';
fragment E:'E'|'e';
fragment F:'F'|'f';
fragment G:'G'|'g';
fragment H:'H'|'h';
fragment I:'I'|'i';
fragment J:'J'|'j';
fragment K:'K'|'k';
fragment L:'L'|'l';
fragment M:'M'|'m';
fragment N:'N'|'n';
fragment O:'O'|'o';
fragment P:'P'|'p';
fragment Q:'Q'|'q';
fragment R:'R'|'r';
fragment S:'S'|'s';
fragment T:'T'|'t';
fragment U:'U'|'u';
fragment V:'V'|'v';
fragment W:'W'|'w';
fragment X:'X'|'x';
fragment Y:'Y'|'y';
fragment Z:'Z'|'z';

WS : ( 
    (' ' | '\t' | '\f')+
  |
    // handle newlines
    ( '\r\n'  // DOS/Windows
      | '\r'    // Macintosh
      | '\n'    // Unix
    )
    )
 { $channel = HIDDEN; };