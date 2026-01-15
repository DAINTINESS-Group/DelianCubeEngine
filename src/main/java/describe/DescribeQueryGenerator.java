package describe;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeQuery;

/**
 * A utility class responsible for constructing the CubeQuery object
 * It takes the semantic information from the DescribeTranslationManager (cube name, list of measure, etc) 
 * and converts them to the required components used by CubeQuery
 * @author Nik-Pt
 *
 */
public class DescribeQueryGenerator {

    private CubeManager cubeManager;

    public DescribeQueryGenerator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    /**
     * Generates the CubeQuery using the information prepared by the TranslationManager.
     * @return constructed CubeQuery 
     */
    public CubeQuery generateCubeQuery(String cubeName,
                                       List<String> measureList,
                                       List<String> gammaExpressions,
                                       List<String> sigmaExpressions,
                                       HashMap<String, String> dimensionsMap) throws Exception {
        
        //Attempt to find the cube using various naming conventions and load said cube
        BasicStoredCube workingCube = null;
        String[] variants = { cubeName, cubeName + "_cube", cubeName.toLowerCase(), cubeName.toLowerCase() + "_cube" };
        
        for (String name : variants) {
            try {
                workingCube = cubeManager.getCubeByName(name);
                if (workingCube != null) break;
            } catch (Exception e) { /* Ignore mismatches*/ }
        }

        if (workingCube == null) {
            throw new Exception("Cube not found: " + cubeName);
        }

        //nitialize Query
        CubeQuery query = new CubeQuery("DescQuery_" + System.currentTimeMillis());
        query.setBasicStoredCube(workingCube);

        //Add Measures
        if (measureList != null) {
            for (String raw : measureList) {
                QueryMeasure qm = new QueryMeasure(raw); 
                query.addQueryMeasure(qm.getFunction(), qm.getAttribute(), qm.getAlias());
            }
        }

        //Add Groupers (Gamma)
        if (gammaExpressions != null) {
            for (String raw : gammaExpressions) {
                String clean = raw.trim();
                String dim = dimensionsMap.getOrDefault(clean, "");
                
                String lvlName = clean;
                if (dim.isEmpty() && clean.contains(".")) {
                    String suffix = clean.substring(clean.lastIndexOf('.') + 1);
                    dim = dimensionsMap.getOrDefault(suffix, "");
                    lvlName = suffix;
                }
                
                query.addGammaExpression(dim, lvlName);
            }
        }

        //Add Filters (Sigma)
        if (sigmaExpressions != null) {
            for (String raw : sigmaExpressions) {
                translateComplexFilter(query, raw.trim(), workingCube, dimensionsMap);
            }
        }

        return query;
    }


    /**
     * Translates advanced filter syntax into SQL compatible sigma expressions
     * Cases: Subqueries ('WITH'): region WITH amount > 10000 -> IN (SELECT region FROM... WHERE amount > 10000)
     * 		  Set membership ('IN'): region IN {A,B} -> IN ('A', 'B')
     * 		  Standard operators: year > 1998
     * @param query
     * @param rawSigma
     * @param cube
     * @param dimensionsMap
     */
    private void translateComplexFilter(CubeQuery query, String rawSigma, BasicStoredCube cube, HashMap<String, String> dimensionsMap) {
        String trimmed = rawSigma.trim();

        //Subquery synatx (WITH)
        if (trimmed.contains(" WITH ")) {
            String[] parts = trimmed.split(" WITH ");
            String lvlRaw = parts[0].trim();
            String condition = parts[1].trim();
            
            String dim = dimensionsMap.getOrDefault(lvlRaw, "");
            String fullCol = dim.isEmpty() ? lvlRaw : dim + "." + lvlRaw;
            
            String table = cube.getFactTable().getTableName(); 
            String subQuery = "(SELECT DISTINCT " + lvlRaw + " FROM " + table + " WHERE " + condition + ")";
            
            query.addSigmaExpression(fullCol, "IN", subQuery);
            return;
        }

        //Set syntax (IN)
        if (trimmed.toUpperCase().contains(" IN ")) {
            String[] parts = trimmed.split("(?i) IN ");
            String lvlRaw = parts[0].trim();
            String values = parts[1].trim();
            
            values = values.replace("{", "(").replace("}", ")");
            
            /*If values are strings but missing quotes adds them
             * Assume if no quotes and no numbers that it is a list of strings
             */
            if (!values.contains("'") && !values.matches(".*\\d.*")) {
                String inner = values.substring(1, values.length()-1);
                String[] items = inner.split(",");
                StringBuilder sqlValues = new StringBuilder("(");
                
                for(int i=0; i<items.length; i++) {
                    if(i>0) sqlValues.append(",");
                    sqlValues.append("'").append(items[i].trim()).append("'");
                }
                
                sqlValues.append(")");
                values = sqlValues.toString();
            }

            String dim = dimensionsMap.getOrDefault(lvlRaw, "");
            String fullCol = dim.isEmpty() ? lvlRaw : dim + "." + lvlRaw;
            
            query.addSigmaExpression(fullCol, "IN", values);
            return;
        }

        //Standard Inequality operators
        Pattern pattern = Pattern.compile("(.*?)\\s*(>=|<=|!=|=|>|<)\\s*(.*)");
        Matcher matcher = pattern.matcher(trimmed);
        
        if (matcher.matches()) {
            String colRaw = matcher.group(1).trim();
            String op = matcher.group(2).trim();
            String val = matcher.group(3).trim();
            
            val = val.replaceAll("^'|'$", "");
            
            String dim = dimensionsMap.getOrDefault(colRaw, "");
            String fullCol = dim.isEmpty() ? colRaw : dim + "." + colRaw;
            
            query.addSigmaExpression(fullCol, op, "'" + val + "'");
        }
    }
}