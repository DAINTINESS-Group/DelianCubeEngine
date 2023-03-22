package result;

import java.util.Arrays;

public class CellBelief {
	
	//private ArrayList<String> dimensionMembers;
	//private String measure;
	//private Integer countOfDetailedCells;
	
	private Cell cell;
	private Double belief;

	public CellBelief(String[] values) {

		int numFields = values.length;
		String[] aux;
		aux = Arrays.copyOfRange(values, 0, numFields-1);
		
		cell = new Cell(aux);
		//System.out.println(cell.getMeasure());
		//System.out.println(cell.getDimensionMembers());
		//System.out.println(cell.getCountOfDetailedCells());
		
		belief = Double.parseDouble(values[numFields-1]);
		//System.out.println(belief);
	}
	
	public Double getBelief() {
		return this.belief;
	}
	
	public Cell getCell() {
		return cell;
	}

}
