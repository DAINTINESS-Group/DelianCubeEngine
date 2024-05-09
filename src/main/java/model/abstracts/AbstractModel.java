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
package model.abstracts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import result.Result;

/**
 * The abstract class representing <i>any</i> AbstractModel.
 * 
 * All concrete instantiations of model generation algorithms must implement the "contract" offered the abstract methods of AbstractModel.
 * THe class needs to have:
 * (1) a Result to which it refers to. Without a query result, creating a model is meaningless. This is also shown at the constructor.
 *     Remember that the Result has a <i>list</i> of cells. The model components also include a list of labels, in one-to-one correspondence with cells.
 * (2) a set (here: hashmap) of components. Each component aims to annotate each cell with a label / value
 * (3) an abstract method compute() which populates the components and their lists of labels.
 * 
 * @author pvassil
 *
 */
public abstract class AbstractModel {
	
	/**
	 * This is an abstract method to be overridden by concrete extensions of the class.
	 * Each model type (cluster, top-k, decision-tree, KPI, prediction, ...) must implement the compute() method.
	 * The method assumes that the hashmap has been created by the constructor.
	 * Then the method must:
	 * (1) create the appropriate components (this is dependent upon the type of the highlight)
	 * (2) compute the respective value of each component per cell of the result.
	 * 
	 * @return 0 if all is OK, a negative value if there are problems
	 */
	public abstract int compute();
	public abstract String getModelName();
	public abstract String[][] printAs2DStringArray();
	public abstract String getInfoContent();
	public abstract void setFileName(String filename, String fileLocation);
	
	protected Result result;
	protected HashMap<String, AbstractModelComponent> components;
	
	public AbstractModel(Result aResult) {
		result = aResult;
		components = new HashMap<String, AbstractModelComponent>();
	}

	public AbstractModel() {
		// TODO Auto-generated constructor stub
	}
	public HashMap<String, AbstractModelComponent> getComponents() {
		return components;
	}

	public void setComponents(HashMap<String, AbstractModelComponent> components) {
		this.components = components;
	}

	public AbstractModelComponent getComponentByName(String componentName) {
		return components.get(componentName);
	}
	
	public int getNumComponents() {
		return components.size();		
//		int total = components.values().stream().mapToInt(List::size).sum(); 	//get it by the VALUES part of the hashmap
	}
	
	public int printComponentsToFile(String fileName) {
		String delim = "\t";

		String[][] array = this.printAs2DStringArray();
		int numRows = array.length;
		int numCols = array[0].length;

		if(numCols != this.getNumComponents()) {
			return -1;
		}

		File file=new File(fileName);
		PrintStream printStream=null;
		try {
			printStream=new PrintStream(new FileOutputStream(file));
			for (int i =0; i<numRows;i++) {
				String line = array[i][0];
				for (int j = 1; j < numCols; j++) {
					line = line + delim + array[i][j];
				}
				printStream.println(line);
			}//end for i

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
			try {
				if(printStream!=null){
					printStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}//end finally try
		}//end finally

		return 0;
	}//end method printContentsToFile
	
	public String printInfoToInfoFile(String fileName) {
		File file=new File(fileName);
		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null;
		try {
			printStream=new PrintStream(new FileOutputStream(file));
			printStream.print(getInfoContent()+"\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
				if(printStream!=null){
					printStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}//end finally try
		}//end finally
		
		return fileName;
	}//end method

	/****************************************** Chart models helper methods ***********************************************************/
	
	public String [][] readResultsFromFileAndSaveTo2DMatrix(String fileLocation, String fileName){
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
    
   
	
}//end class
