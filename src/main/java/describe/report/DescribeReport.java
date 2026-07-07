package describe.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import cubemanager.cubebase.CubeQuery;
import describe.DescribeQuery; // Using the wrapper class
import cubemanager.cubebase.QueryMeasure;
import result.Cell;
import result.Result;

/**
 * Class that sets-up the report file with the Describe Query results
 * @author Nik-Pt
 *
 */
public class DescribeReport {
    
	//Describe input query
    private String incomingExpression;
    //Wrapper holding both query definition and result
    private DescribeQuery describeQuery; 
    
    private String localFolder;
    private String reportFile;
    private boolean errorStatus;
    private String errorMessage;
    
    public DescribeReport(String incomingExpression, String connectionType) {
        this.incomingExpression = incomingExpression;
        this.localFolder = "OutputFiles" + File.separator;
    }
    
    public void setDescribeQuery(DescribeQuery describeQuery) {
        this.describeQuery = describeQuery;
    }
    
    public void setErrorStatus(boolean errorStatus) {
    	this.errorStatus = errorStatus; 
    }
    
    public boolean getErrorStatus() { 
    	return errorStatus; 
    }
    
    public void setErrorMessage(String errorMessage) {
    	this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() { 
    	return errorMessage; 
    }
    
    public String getLocalFolder() { 
    	return localFolder; 
    }
    
    public String getReportFile() { 
    	return reportFile; 
    }
    
    /*
     * Generates the Markdown file
     * The file name is based on the query name, creates the directory if needed and writes the report sections (Header, errors, SQL, Data)
     */
    public void createTextReportFile() {
        String queryName = "DescQuery_Unknown";
        if (describeQuery != null && describeQuery.getCubeQuery() != null) {
            queryName = describeQuery.getCubeQuery().getName();
        }
        
        if (queryName.contains("-")) queryName = queryName.split("-")[0];
        
        this.reportFile = queryName + "-Describe_Operator_Report.md";
        File outputDir = new File(this.localFolder);
        if (!outputDir.exists()) outputDir.mkdirs();
    
        try (FileWriter writer = new FileWriter(this.localFolder + this.reportFile)) {
            
            writer.write("## DESCRIBE OPERATOR REPORT\n\n");       
            writer.write("**Query:** " + incomingExpression + "\n\n");

            if (errorStatus) {
                writer.write("**ERROR:** " + errorMessage + "\n\n");
                return;
            }
            
            if (describeQuery != null) {
                CubeQuery cq = describeQuery.getCubeQuery();
                Result res = describeQuery.getDescribeQueryResult();
                
                writer.write("### CUBE QUERY\n" + (cq != null ? cq.toString() : "null") + "\n\n");
                
                if (res != null) {
                    writer.write("### RESULTS\n");
                    String[][] resultArray = res.getResultArray();
                    if (resultArray != null && resultArray.length > 0) {
                        writer.write(buildMarkdownTable(resultArray, res) + "\n\n");
                    } else {
                        writer.write("No results found.\n\n");
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Converts the 2D String Array into a Markdown table format
     */
    private String buildMarkdownTable(String[][] resultArr, Result result) { 
        StringBuilder sb = new StringBuilder();
        ArrayList<Cell> cells = result.getCells();

        if (cells == null || cells.isEmpty()) return "No Results Found.";
        
        ArrayList<String[][]> modelOutputs = describeQuery.getModelOutputs();

        sb.append("|");
        
        if (describeQuery.getCubeQuery().getGammaExpressions() != null) {
            for (String[] dim : describeQuery.getCubeQuery().getGammaExpressions()) {
                sb.append(String.join(".", dim)).append("|");
            }
        } else {
            sb.append("Dimension|");
        }

        int numSQLMeasures = cells.get(0).getMeasures().size();
        ArrayList<QueryMeasure> definedMeasures = describeQuery.getCubeQuery().getQueryMeasures();
        
        for (int i = 0; i < numSQLMeasures; i++) {
            String header = "Measure_" + (i + 1); 
            
            if (definedMeasures != null && i < definedMeasures.size()) {
                QueryMeasure qm = definedMeasures.get(i);

                if (qm.getAlias() != null && !qm.getAlias().isEmpty()) {
                    header = qm.getAlias();
                } else {
                    header = qm.getName();
                }
            }
            sb.append(header).append("|");
        }
        
        if (modelOutputs != null) {
            for (String[][] modelData : modelOutputs) {
                if (modelData.length > 0) {
                    for (String colHeader : modelData[0]) {
                        sb.append(colHeader).append("|");
                    }
                }
            }
        }
        sb.append("\n|");

        int totalCols;

        if (describeQuery.getCubeQuery().getGammaExpressions() != null) {
            totalCols = describeQuery.getCubeQuery().getGammaExpressions().size() + numSQLMeasures;
        } else {
            totalCols = 1 + numSQLMeasures;
        }
        
        if (modelOutputs != null) {
            for (String[][] modelData : modelOutputs) {
                 if (modelData.length > 0) totalCols += modelData[0].length;
            }
        }
        for (int i = 0; i < totalCols; i++) sb.append("---|");
        sb.append("\n");

        int rowIndex = 0;
        for (Cell c : cells) {
            sb.append("|");
            
            //Dimensions
            for (String dim : c.getDimensionMembers()) sb.append(dim).append("|");
            
            //SQL Measures
            for (String val : c.getMeasures()) sb.append(val).append("|");
            
            if (modelOutputs != null) {
                for (String[][] modelData : modelOutputs) {
                    if (rowIndex + 1 < modelData.length) {
                        for (String val : modelData[rowIndex + 1]) {
                            sb.append(val).append("|");
                        }
                    } else {
                        for (int k=0; k<modelData[0].length; k++) sb.append("N/A|");
                    }
                }
            }
            
            sb.append("\n");
            rowIndex++;
        }

        return sb.toString();
    }
    
//    private String buildMarkdownTable(String[][] resultArr, Result result) {
//        StringBuilder sb = new StringBuilder();
//
//        ArrayList<Cell> cells = result.getCells();
//
//        if (cells == null || cells.isEmpty()) {
//            return "No Results Found.";
//        }
//
//        sb.append("|");
//        if (describeQuery.getCubeQuery().getGammaExpressions() != null) {
//            for (String[] dim : describeQuery.getCubeQuery().getGammaExpressions()) {
//                sb.append(String.join(".", dim)).append("|");
//            }
//        } else {
//            sb.append("Dimension|");
//        }
//
//        result.Cell firstCell = cells.get(0);
//        int numTotalMeasures = firstCell.getMeasures().size();
//        int numOriginalMeasures = describeQuery.getCubeQuery().getMeasuresList().size();
//        
//        for (int i = 0; i < numTotalMeasures; i++) {
//            if (i < numOriginalMeasures) {
//                sb.append(describeQuery.getCubeQuery().getMeasuresList().get(i)).append("|"); 
//            } else {
//                sb.append("Model_Result_").append(i - numOriginalMeasures + 1).append("|");
//            }
//        }
//        sb.append("\n|");
//
//        int totalCols = (describeQuery.getCubeQuery().getGammaExpressions() != null ? 
//                         describeQuery.getCubeQuery().getGammaExpressions().size() : 1) + numTotalMeasures;
//        for (int i = 0; i < totalCols; i++) sb.append("---|");
//        sb.append("\n");
//
//        for (result.Cell c : cells) {
//            sb.append("|");
//            for (String dim : c.getDimensionMembers()) sb.append(dim).append("|");
//            for (String val : c.getMeasures())   sb.append(val).append("|");
//            sb.append("\n");
//        }
//
//        return sb.toString();
//    }
}