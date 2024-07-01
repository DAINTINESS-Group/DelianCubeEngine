package chartManagement.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chartManagement.utils.ChartVisModel;

public abstract class ChartModel {
	
	protected String fileLocation;
	protected String  fileName;
	protected String reportedResult = "";
	protected String aggrFunc;
	protected String scoreResult;
	
	
	public abstract String[][] printAs2DStringArray();
	
	public abstract int compute(); //Todo return double in future!
	
	public abstract double computeScore(ChartVisModel model);
	


	
	public abstract String getModelName();
	
	public abstract String getInfoContent();
	
	public abstract double getScoreOfModel();
	
	public abstract String reportScore();
	
	
	public String [][] readResultsFromFileAndSaveTo2DMatrix(){
		FileReader fileReader = null;
		String [][] resultArray = null;
		File report = new File(fileLocation +  "/" + fileName);
	    try {
	        fileReader = new FileReader(report);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        
	        String line;
	        while ((line = bufferedReader.readLine()) != null) {
	            
	        	String[][] matrix = null;
	            if(line.contains("Type: ")) {
	            	matrix = new String[0][3];
	            	Boolean startFillingOfTheArray = false;
	            	
	            	String [] header = new String[3];
	            	header[0] = "Type";
	            	header[1] = "Query";
	            	header[2] = line.split(":")[1];
	            	matrix = addRow(matrix, header);
		            while(!line.isEmpty()) {
		            	
			            line = bufferedReader.readLine();
			            if(startFillingOfTheArray==true) {
			            	String[] newRow = line.split("\t"); 
			            	if(newRow.length==3) {
			            		matrix = addRow(matrix, newRow);	
			            	}
			            	
			            	
			            }
			            if(line.equals("|Grouper 1|Grouper 2|Measure|")) {
			            	String[] newRow = {"Grouper 1", "Grouper 2", "Measure"};
			            	matrix = addRow(matrix, newRow);
			            	startFillingOfTheArray = true;
			            	
			            }
			            
		            }
		            
		            
		            if(resultArray!=null) {
		            	resultArray = addMatrix(resultArray, matrix);
		            }else {
		            	resultArray = matrix;
		            }
		            
	            }
	            
	            
	        }
	        
	        bufferedReader.close();
	        
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return resultArray;
	}
	
    // Method to add a row to the 2D array
    public String[][] addRow(String[][] matrix, String[] newRow) {
    	
    	int numRows = matrix.length;
        int numCols = newRow.length;
    	
        String[][] newMatrix = new String[numRows + 1][numCols];
        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i];
        }
        newMatrix[matrix.length] = newRow;
        
        return newMatrix;
    }

    public String[][] addMatrix(String[][] destination, String[][] source) {
        // Create a new matrix with dimensions to accommodate both matrices
        String[][] newMatrix = new String[destination.length + source.length][destination[0].length];
        
        // Copy the elements from the destination matrix to the new matrix
        for (int i = 0; i < destination.length; i++) {
            for (int j = 0; j < destination[i].length; j++) {
                newMatrix[i][j] = destination[i][j];
            }
        }
        
        // Copy the elements from the source matrix to the new matrix
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                newMatrix[destination.length + i][j] = source[i][j];
            }
        }
        
        return newMatrix;
    }
    
	public List<String[][]> extractArrayListWithSmallerArrays(String [][] bigArray){
		List<String[][]> smallerArrays = new ArrayList<>();
		int startIndex = -1;
        for (int i = 0; i < bigArray.length; i++) {
            if (bigArray[i][0].equals("Type") && bigArray[i][1].equals("Query")) {
                if (startIndex != -1) {
                    // If startIndex is not -1, add the current slice to smallerArrays
                    String[][] slice = new String[i - startIndex][];
                    System.arraycopy(bigArray, startIndex, slice, 0, slice.length);
                    smallerArrays.add(slice);
                }
                // Update startIndex for the next slice
                startIndex = i;
            }
        }

        // Add the last slice
        if (startIndex != -1) {
            String[][] slice = new String[bigArray.length - startIndex][];
            System.arraycopy(bigArray, startIndex, slice, 0, slice.length);
            smallerArrays.add(slice);
        }
        return smallerArrays;

	}
	
	public void setFolderAndFilename(String fileLocation, String filename) {
		this.fileLocation = fileLocation;
		this.fileName = filename;
		
	}
	
	public String printAs2DStringArrayForChartReportModel() {
		return this.reportedResult;
	}
	
	public void setAggrFunc (String func) {
		this.aggrFunc = func;
	}
	
	public String getAggrFunc () {
		return this.aggrFunc;
	}

	public void setScoreResult(String result) {
		scoreResult = result;
	}
	
	public String getScoreResult() {
		return scoreResult;
	}
}
