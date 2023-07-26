package model.decisiontree;



public class Column {

  private final int position;
  private final String name;
  private final String datatype;


  public Column(int position, String name, String datatype) {
    this.position = position;
    this.name = name;
    this.datatype = datatype;
  }

  public String getName() {
    return name;
  }

  public String getDatatype() {
    return datatype;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("=============================================================================\n");
    stringBuilder.append("Column\n");
    stringBuilder.append(String.format("position: %d\n", position));
    stringBuilder.append(String.format("name: %s\n", name));
    stringBuilder.append(String.format("datatype: %s\n", datatype));
    stringBuilder.append("\n");

    return stringBuilder.toString();
  }
}
