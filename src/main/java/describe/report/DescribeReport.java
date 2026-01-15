package describe.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import cubemanager.cubebase.CubeQuery;
import describe.DescribeQuery; // Using the wrapper class
import result.Result;

/**
 * Class that sets-up the report file with the Describe Query results
 * @author Nik-Pt
 *
 */
public class DescribeReport {
    
	//Describe input query
    private String incomingExpression;
    //Spark or RDBMS dataset connection
    private String connectionType;
    //Wrapper holding both query definition and result
    private DescribeQuery describeQuery; 
    
    private String localFolder;
    private String reportFile;
    private boolean errorStatus;
    private String errorMessage;
    
    public DescribeReport(String incomingExpression, String connectionType) {
        this.incomingExpression = incomingExpression;
        this.connectionType = connectionType;
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
                        writer.write(buildMarkdownTable(resultArray) + "\n\n");
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
    private String buildMarkdownTable(String[][] resultArray) {
        StringBuilder sb = new StringBuilder();
        int startRow = connectionType.equals("RDBMS") ? 2 : 0;
        int loopLimit = connectionType.equals("Spark") ? resultArray.length - 2 : resultArray.length;

        if (resultArray.length > startRow) {
            sb.append("|");
            int cols = resultArray[startRow].length;
            for (int k = 0; k < cols - 1; k++) {
                sb.append((k == cols - 2 ? "Metric" : "Dim " + (k + 1)) + "|");
            }
            sb.append("\n|");
            for (int k = 0; k < cols - 1; k++) sb.append("---|");
            sb.append("\n");
            
            for (int i = startRow; i < loopLimit; i++) {
                sb.append("|");
                for (int j = 0; j < resultArray[i].length - 1; j++) {
                    sb.append(resultArray[i][j]).append("|");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}