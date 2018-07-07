/**
 * 
 */
package result;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class storing the cells of a cube
 * @author pvassil
 *
 */
public class Cell {

	private ArrayList<String> dimensionMembers;
	private String measure;
	private Integer countOfDetailedCells;

	public Cell() {
		dimensionMembers = new ArrayList<String>();
	}

	public Cell(String[] values) {
		int numFields = values.length;
		String[] aux;
		
		dimensionMembers = new ArrayList<String>();
		aux = Arrays.copyOfRange(values, 0, numFields-2);
		dimensionMembers.addAll(Arrays.asList(aux));
		
		measure = values[numFields-2];
		countOfDetailedCells = Integer.parseInt(values[numFields-1]);
		
	/*  Testing @ development	
		System.out.println("---------------");
		System.out.println("Size of dim's: " + dimensionMembers.size());
		for(String s : dimensionMembers)
			 System.out.print(s + "\t");
		System.out.print("| " +measure + "\t");
		System.out.println(countOfDetailedCells);
    */		
	}

	public ArrayList<String> getDimensionMembers() {
		return dimensionMembers;
	}

	public String getMeasure() {
		return measure;
	}

	public Integer getCountOfDetailedCells() {
		return countOfDetailedCells;
	}

	public String toString(String delimiter) {
		String result = new String();
		for(int i=0; i<dimensionMembers.size();i++)
			result = result + dimensionMembers.get(i) + delimiter;
		result = result + measure + delimiter;
		result = result+ countOfDetailedCells + delimiter;
		
		return result;
	}
}