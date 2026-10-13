package decisiontree.services;

public class IDatasetManagerFactory {

  public IDatasetManager createDatasetProfiler() {
    return new DatasetManager();
  }
}
