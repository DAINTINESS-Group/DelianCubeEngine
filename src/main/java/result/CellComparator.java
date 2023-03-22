package result;

import java.util.Comparator;

/**
 * 
 * A Comparator implementation for the {@code TreeSet<Cell>} set
 *
 */
public class CellComparator implements Comparator<Cell>{

	/**
	 * Compares the dimension members of two cells.
	 * @param o1 First cell
	 * @param 02 Second cell
	 */
	public int compare(Cell o1, Cell o2) {
		return o1.getDimensionMembers().toString().compareTo(o2.getDimensionMembers().toString());	
	}	
	
}