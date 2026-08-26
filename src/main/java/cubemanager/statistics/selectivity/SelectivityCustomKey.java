package cubemanager.statistics.selectivity;

import java.util.Objects;

/**
 * A custom key to be used for the selectivity hashmap.
 * @author mariosjkb
 *
 */
public class SelectivityCustomKey {

	private final String columnName;
	private final String value;
		
	public SelectivityCustomKey(String columnName, String value) {
		this.columnName = columnName;
		this.value = value;
	}
		
  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectivityCustomKey)) return false;
        SelectivityCustomKey that = (SelectivityCustomKey) o;
        return Objects.equals(columnName, that.columnName)
            && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, value);
    }

    @Override
    public String toString() {
        return columnName + "." + value;
    }
    
    public String getColumnName() {
    	return columnName;
    }
    
    public String getValue() {
    	return value;
    }
}

