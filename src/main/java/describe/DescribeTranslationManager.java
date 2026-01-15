package describe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Hierarchy;
import cubemanager.cubebase.Level;

/**
 * A class that manages the translation process of a Describe query to its respective CubeQuery
 * @author Nik-Pt
 */
public class DescribeTranslationManager {

    private CubeManager cubeManager;
    private DescribeParams params; 
    private String cubeName;
    private HashMap<String, String> dimensions = new HashMap<>();
    private List<String> measureList = new ArrayList<>();
    private List<String> gammaExpressions = new ArrayList<>();
    private List<String> sigmaExpressions = new ArrayList<>();

    public DescribeTranslationManager(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    /**
     * Main entry point for the translation. Method that creates a cubeQueryGenerator object
     * @param The parsed parameters from the query
     * @return a fully constructed CubeQuery ready for execution
     * @throws Exception
     */
    public CubeQuery translateDescribeToCubeQuery(DescribeParams params) throws Exception {
        this.params = params; 
       
        setUpTranslation();
        
        DescribeQueryGenerator generator = new DescribeQueryGenerator(cubeManager);
        
        CubeQuery query = generator.generateCubeQuery(
            this.cubeName,
            this.measureList,
            this.gammaExpressions,
            this.sigmaExpressions,
            this.dimensions
        );

        return query;
    }

    /**
     * Orchestrates the translation setup.
     */
    public void setUpTranslation() {
        dimensions.clear();
        getDescribeQueryInfo(); 
        getExpressionInfoFromCube();
    }

    private void getDescribeQueryInfo() {
        if (this.params == null) return;

        String rawName = params.getCubeName().replace("\"", "").replace(".dcu", "");
        if (rawName.contains(" JOIN ")) {
            this.cubeName = rawName.split(" JOIN ")[0].trim();
        } else {
            this.cubeName = rawName;
        }

        if (params.getMeasureList() != null) {
            this.measureList.clear();
            this.measureList.addAll(params.getMeasureList());
        }
        if (params.getGammaExpressions() != null) {
            this.gammaExpressions.clear();
            this.gammaExpressions.addAll(params.getGammaExpressions());
        }
        if (params.getSigmaExpressions() != null) {
            this.sigmaExpressions.clear();
            this.sigmaExpressions.addAll(params.getSigmaExpressions());
        }
    }

    /**
     * Iterates over ALL dimensions in CubeManager to populate metadata maps
     * For every level mentioned in the Gamma (Group By) or Sigma (Where) clauses it searches for the Dimension that level belongs to
     */
    private void getExpressionInfoFromCube() {
        ArrayList<String> expressions = new ArrayList<>();
        if (gammaExpressions != null) expressions.addAll(gammaExpressions);
        if (sigmaExpressions != null) {
            for (String s : sigmaExpressions) expressions.add(extractLevelFromSigma(s));
        }
    
        for(int i = 0; i < expressions.size(); i++) {
            String currentExpr = expressions.get(i).trim();
            
            for(int j = 0; j < cubeManager.getDimensions().size(); j++) {
                Dimension dim = cubeManager.getDimensions().get(j);
                
                if(dim.getHierarchy() != null && !dim.getHierarchy().isEmpty()) {
                    Hierarchy hier = dim.getHierarchy().get(0); 
                    
                    for(int k = 0; k < hier.getLevels().size(); k++) {
                        Level lvl = hier.getLevels().get(k);
                        String hierarchy_level = lvl.getName();
                        
                        if(hierarchy_level.equalsIgnoreCase(currentExpr)) {
                            dimensions.put(currentExpr, dim.getName());
                        }
                    }
                }
            }
        }
    }

    private String extractLevelFromSigma(String rawSigma) {
        if (rawSigma.contains(" WITH ")) { 
        	return rawSigma.split(" WITH ")[0].trim();
        }
        
        String[] parts = rawSigma.split("([<>=]|\\s+(?i)(IN|NOT|LIKE)\\s+)");
        
        return parts.length > 0 ? parts[0].trim() : rawSigma;
    }
}