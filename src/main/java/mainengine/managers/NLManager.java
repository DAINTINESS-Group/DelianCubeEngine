package mainengine.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.QueryHistoryManager;
import mainengine.nlq.ITranslator;
import mainengine.nlq.ITranslatorFactory;
import mainengine.nlq.NLQValidationResults;
import result.ResultFileMetadata;

public class NLManager implements IBuilder {

    private ArrayList<String> cubeNames;
    private ArrayList<String> aggrFunctions;
    private ArrayList<String> measuresFields;
    private ArrayList<String> dimensions;
    private HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap;
    private HashMap<String, ArrayList<String>> levelsToDimensionsHashmap;
    
    private ITranslatorFactory translatorFactory;

    @Override
    public ResponseDTO execute(RequestCTO cto) throws Exception {
        Map<String, Object> inputParams = (Map<String, Object>) cto.getInput();
        String nlQuery = (String) inputParams.get("query");
        QueryHistoryManager historyManager = (QueryHistoryManager) inputParams.get("historyManager");

        Object resultPayload = null;

        extractCubeMetadata(cto.getCubeManager());

        switch (cto.getCommandAlias()) {
            case "answer_nl_string":
                resultPayload = answerCubeQueryFromNLString(nlQuery, historyManager, cto.getCubeManager());
                break;

            case "answer_nl_metadata":
                resultPayload = answerCubeQueryFromNLStringWithMetadata(nlQuery, historyManager, cto.getCubeManager());
                break;

            case "prepare_nl_query":
                resultPayload = prepareCubeQuery(nlQuery);
                break;

            default:
                throw new IllegalArgumentException("Unknown NL Command: " + cto.getCommandAlias());
        }

        ResponseDTO response = new ResponseDTO(true);
        response.setPayload(resultPayload);
        return response;
    }


    private String answerCubeQueryFromNLString(String nlQuery, QueryHistoryManager historyManager, CubeManager cubeManager) throws Exception {
        String cubeQueryString = translateNLQuery(nlQuery);
        System.out.println("Translated Query: " + cubeQueryString);
        
        return (String) executeGeneratedQuery("answer_from_string", cubeQueryString, historyManager, cubeManager);
    }

    private ResultFileMetadata answerCubeQueryFromNLStringWithMetadata(String nlQuery, QueryHistoryManager historyManager, CubeManager cubeManager) throws Exception {
        String cubeQueryString = translateNLQuery(nlQuery);
        return (ResultFileMetadata) executeGeneratedQuery("answer_with_metadata", cubeQueryString, historyManager, cubeManager);
    }

    private ResultFileMetadata prepareCubeQuery(String nlQuery) {
        ITranslator translator = getNLTranslator();
        
        long t0 = System.nanoTime();
        NLQValidationResults results = translator.prepareCubeQuery(cubeNames, aggrFunctions, measuresFields, nlQuery);
        long tF = System.nanoTime();
        System.out.println("NLQuery Preprocessing Time: " + (tF - t0) + " nanoseconds");

        String errorCheckingInfoOutput = printErrorCheckingResultsToFile(results, "OutputFiles" + File.separator + "ErrorChecking.txt");

        ResultFileMetadata resMetadata = new ResultFileMetadata();
        resMetadata.setErrorCheckingStatus(errorCheckingInfoOutput);
        return resMetadata;
    }


    //Helper methods
    private String translateNLQuery(String nlQuery) {
        ITranslator translator = getNLTranslator();
        
        long t0 = System.nanoTime();
        String analysedString = translator.produceCubeQueryString(nlQuery);
        long tF = System.nanoTime();
        System.out.println("NLQuery Transformation Time: " + (tF - t0) + " nanoseconds");
        
        return analysedString;
    }

    private Object executeGeneratedQuery(String command, String queryString, QueryHistoryManager historyManager, CubeManager cubeMgr) throws Exception {
    	long t0 = System.nanoTime();
        Map<String, Object> execParams = new HashMap<>();
        execParams.put("query", queryString);
        execParams.put("historyManager", historyManager);
        QueryExecutionManager execManager = new QueryExecutionManager();
        
        RequestCTO execRequest = new RequestCTO(ManagerType.EXECUTION, command, execParams, cubeMgr);
        ResponseDTO response = execManager.execute(execRequest);
        
        long tF = System.nanoTime();
		System.out.println("Query Execution Time " + (tF - t0) + " nanoseconds");

        if (response.isSuccess()) {
            Map<String, Object> payload = (Map<String, Object>) response.getPayload();
            return payload.get("returnValue");
        } else {
            throw new Exception("Execution of NL translated query failed: " + response.getError());
        }
    }

    private void extractCubeMetadata(CubeManager cubeManager) {
        cubeNames = new ArrayList<>();
        measuresFields = new ArrayList<>();
        dimensions = new ArrayList<>();
        dimensionsToLevelsHashmap = new HashMap<>();
        levelsToDimensionsHashmap = new HashMap<>();

        List<BasicStoredCube> cubes = cubeManager.getCubes();
        for (BasicStoredCube cube : cubes) {
        	//1.Bring data for CubeName
            cubeNames.add(cube.getName());
            //2.Bring data for Measure Fields
            for (int j = 0; j < cube.getMeasuresList().size(); j++) {
                measuresFields.add(cube.getFactTable().getTableName() + "." + cube.getMeasuresList().get(j).getAttribute().getName());
            }
        }

        //3.Bring data for levels
        List<Dimension> dims = cubeManager.getDimensions();
        for (Dimension dim : dims) {
            dimensions.add(dim.getName());
            for (int j = 0; j < dim.getHierarchy().size(); j++) {
                List<Level> levels = dim.getHierarchy().get(j).getLevels();
                ArrayList<String> tmpLvls = new ArrayList<>();
                for (Level lvl : levels) {
                    tmpLvls.add(lvl.getName());
                    String tmpLevelKey = lvl.getName();
                    
                    levelsToDimensionsHashmap.computeIfAbsent(tmpLevelKey, k -> new ArrayList<>()).add(dim.getName());
                }
                dimensionsToLevelsHashmap.put(dim.getName(), tmpLvls);
            }
        }

        //4.Create data for AggrFunc
        aggrFunctions = new ArrayList<>();
        aggrFunctions.add("max"); aggrFunctions.add("maximum");
        aggrFunctions.add("min"); aggrFunctions.add("minimum");
        aggrFunctions.add("count");
        aggrFunctions.add("avg"); aggrFunctions.add("average");
        aggrFunctions.add("sum");
    }

    private ITranslator getNLTranslator() {
        translatorFactory = new ITranslatorFactory();
        return translatorFactory.getITranslator("NLTranslator", dimensionsToLevelsHashmap, levelsToDimensionsHashmap);
    }

    private String printErrorCheckingResultsToFile(NLQValidationResults results, String filePath) {
        File file = new File(filePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             PrintStream printStream = new PrintStream(fileOutputStream)) {
             
            printStream.print(results.getHashKey() + ";\n");
            printStream.print(results.getFoundError() + ";\n");
            printStream.print(results.getErrorCode() + ";\n");
            printStream.print(results.getDetails());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}