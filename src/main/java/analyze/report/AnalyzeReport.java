package analyze.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import analyze.AnalyzeQuery;
import result.Result;

/**
 * Class that sets-up the report file with the Analyze Query results
 * @author mariosjkb
 *
 */
public class AnalyzeReport {
	
	// Analyze input query
	private String incomingExpression;
	
	// Spark or RDBMS dataset connection
	private String connectionType;
	
	// Produced queries for Analyze execution
	private ArrayList<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
	
	// Folder where the report file is stored
	private String localFolder;
	
	// Name of the report file
	private String reportFile;
	
	// Boolean variable that is true when no errors occured during operator execution
	private boolean errorStatus;
	
	// Message with the error occured
	private String errorMessage;
	
	public AnalyzeReport(String incomingExpression,String connectionType) {
		this.incomingExpression = incomingExpression;
		this.connectionType = connectionType;
	}
	
	public boolean getErrorStatus() {
		return errorStatus;
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
	
	public void setAnalyzeQueries(ArrayList<AnalyzeQuery> analyzeQueries) {
		this.analyzeQueries = analyzeQueries;
	}
	
	public void setErrorStatus(boolean errorStatus) {
		this.errorStatus = errorStatus;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	// Method that creates the report file based on the info gained from the Analyze Query objects
	public void createTextReportFile() {
		System.out.println("AGGELIKI 'S TEST : " + analyzeQueries.get(0)
		.getAnalyzeCubeQuery()
		.getName());
		String fileName = analyzeQueries.get(0)
										.getAnalyzeCubeQuery()
										.getName()
										.split("-")[0];
	
		try {
			this.reportFile = fileName + "-Analyze_Operator_Report.md";
			this.localFolder = "OutputFiles" + File.separator;
			FileWriter reportFile = new FileWriter(this.localFolder + this.reportFile);
			
			reportFile.write("## ------------------------------------ANALYZE OPERATOR REPORT-------------------------\n\n");		
			
			reportFile.write("ANALYZE OPERATOR INTENTIONAL QUERY: \n\n**" + incomingExpression + "**\n\n");

			if(errorStatus == true) {
				reportFile.write("**ERROR WAS ENCOUNTERED DURING THE OPERATOR'S EXECUTION**\n\n");
				reportFile.write("AnalyzeExecutionError: " + errorMessage + "\n\n");
				reportFile.close();
			}else {
			
				reportFile.write("### -----------------------------------------PRODUCED CUBE QUERIES--------------------------------------\n\n");
				
				for(AnalyzeQuery aq: analyzeQueries) {
					Result result = aq.getAnalyzeQueryResult();
					if(result == null) {
						continue;
					}
					String[][] resultArray = result.getResultArray();
					String resultString = "";
					if(resultArray == null) {
						resultString = "The result of the Cube Query is empty!";
					}else {
						if(connectionType.equals("RDBMS")) {
							// set up markdown table
							for(int i = 2;i<resultArray.length;i++) {
								if(i==2) {
									resultString += "|";
									if(i == 2) {
										for(int k=0;k<resultArray[i].length-1;k++) {
											if(k == resultArray[i].length-2) {
												resultString += "Metric|";
											}else {
												resultString += "Grouper " + Integer.toString(k+1) + "|";
											}
										}
										resultString += "\n|";
										for(int k=0;k<resultArray[i].length-1;k++) {
											resultString += "---|";
										}
										resultString += "\n|";
									}
								}
								// fill table with data
								for(int j = 0;j<resultArray[i].length-1;j++) {
									resultString += resultArray[i][j] + "|";
								}
								resultString += "\n";
								if(i < resultArray.length - 1) {
									resultString += "|";
								}
							}
						}else if(connectionType.equals("Spark")) {
							// set-up markdown table
							for(int i = 0;i<resultArray.length-2;i++) {
								if(i==0) {
									resultString += "|";
									if(i == 0) {
										for(int k=0;k<resultArray[i].length-1;k++) {
											if(k == resultArray[i].length-2) {
												resultString += "Metric|";
											}else {
												resultString += "Grouper " + Integer.toString(k+1) + "|";
											}
										}
										resultString += "\n|";
										for(int k=0;k<resultArray[i].length-1;k++) {
											resultString += "---|";
										}
										resultString += "\n|";
									}
								}
								//fill table with data
								for(int j = 0;j<resultArray[i].length-1;j++) {
									
									resultString += resultArray[i][j] + "|";
								}
								resultString += "\n";
								if(i < resultArray.length - 3) {
									resultString += "|";
								}
							}
						}
					}
					
					reportFile.write("#### ANALYZE CUBE QUERY\n\n"
									+ aq.getAnalyzeCubeQuery().toString() + "\n\n"
									+ "##### ANALYZE CUBE QUERY DETAILS\n\n" 
									+ "Cube Query Type: **" + aq.getType() + "**\n"
									+ "Filter value that is modified compared to the Base Query: **" + aq.getOriginalSigmaValue() + "**\n"
									+ "Filter value after modification: **" + aq.getModifiedSigmaValue() + "**\n"
									+ "Grouper value that is modified compared to the Base Query: **" + aq.getOriginalGammaValue() + "**\n"
									+ "Grouper value after modification: **" + aq.getModifiedGammaValue() + "**\n"
									+ "Result of the Cube Query in ascending order:\n" + resultString + "\n"
									+ "-----------------------------------------------------------------\n");
				}
				reportFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
