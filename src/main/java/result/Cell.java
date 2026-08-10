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
package result;

import java.util.ArrayList;

/**
 * The class storing the cells of a cube
 * 
 * @author pvassil
 *
 */
public class Cell {

	/** The OLAP "All" value: a dimension aggregated over rather than bound to a concrete member. */
	public static final String ALL = "All";

	private ArrayList<String> dimensionMembers = new ArrayList<>();
	private ArrayList<String> measures = new ArrayList<>();
	private Integer countOfDetailedCells;

	/*
	 * public Cell(String[] values) { int numFields = values.length; String[] aux;
	 * 
	 * dimensionMembers = new ArrayList<String>(); aux = Arrays.copyOfRange(values,
	 * 0, numFields-2); dimensionMembers.addAll(Arrays.asList(aux));
	 * 
	 * measure = values[numFields-2]; countOfDetailedCells =
	 * Integer.parseInt(values[numFields-1]);
	 * 
	 * Testing @ development System.out.println("---------------");
	 * System.out.println("Size of dim's: " + dimensionMembers.size()); for(String s
	 * : dimensionMembers) System.out.print(s + "\t"); System.out.print("| "
	 * +measure + "\t"); System.out.println(countOfDetailedCells);
	 * 
	 * }
	 * 
	 * Replaced the original constructor to have the ability to have multiple measures instead of just one
	 */

    public Cell(String[] values, int numMeasures) {
        int totalFields = values.length;
        
        int countIndex = totalFields - 1;
        this.countOfDetailedCells = Integer.parseInt(values[countIndex]);

        int measuresStartIndex = countIndex - numMeasures;
        
        for(int i = 0; i < measuresStartIndex; i++) {
            dimensionMembers.add(values[i]);
        }

        for(int i = measuresStartIndex; i < countIndex; i++) {
            measures.add(values[i]);
        }
    }

    //Legacy Constructor that defaults to 1 measure
    public Cell(String[] values) {
        this(values, 1);
    }

    /**
     * An aggregate cell: the given member bound at one dimension and {@link #ALL} at the others, carrying one
     * measure (the value marginalized over the unbound dimensions).
     */
    public static Cell aggregate(int dimensionCount, int boundDimension, String member, double measure) {
        String[] values = new String[dimensionCount + 2]; // dimensions + one measure + the count
        for (int i = 0; i < dimensionCount; i++) {
            values[i] = i == boundDimension ? member : ALL;
        }
        values[dimensionCount] = Double.toString(measure);
        values[dimensionCount + 1] = "1";
        return new Cell(values, 1);
    }

	public ArrayList<String> getDimensionMembers() {
		return dimensionMembers;
	}

	public String getMeasure() {
        return measures.isEmpty() ? "" : measures.get(0);
    }
	
	public ArrayList<String> getMeasures() {
        return measures;
    }

	public Integer getCountOfDetailedCells() {
		return countOfDetailedCells;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (String d : dimensionMembers) { sb.append(d).append(",");
        
        }
        for (String m : measures) {
        	sb.append(m).append(",");
        }
        
        sb.append(countOfDetailedCells);
        
        return sb.toString();
    }
    
    //Legacy toString with delimiter
    public String toString(String delimiter) {
        StringBuilder sb = new StringBuilder();
        
        for (String d : dimensionMembers) {
        	sb.append(d).append(delimiter);
        }
        for (String m : measures) {
        	sb.append(m).append(delimiter);
        }
        
        sb.append(countOfDetailedCells).append(delimiter);
        
        return sb.toString();
    }

	public Double toDouble() {
		return Double.valueOf(measures.get(0));}

	/** The i-th measure of this cell as a double. */
	public Double toDouble(int index) {
		return Double.valueOf(measures.get(index));
	}
}