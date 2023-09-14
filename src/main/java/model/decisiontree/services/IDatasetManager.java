package model.decisiontree.services;

import java.io.IOException;

import org.apache.spark.sql.AnalysisException;

import model.decisiontree.labeling.RuleSet;
import model.decisiontree.DatasetProfile;

public interface IDatasetManager {

  /**
   * Registers a dataset into the system such that profiling can be performed.
   *
   * @param datasetName  - Name of the dataset.
   * @param path   - Path of the dataset file.
   * @throws AnalysisException
   */
  void registerDataset(String datasetName, String path) throws AnalysisException;

  /**
   * Computes a new labeled column for the registered dataset.
   *
   * @param ruleSet - A rule set based on which the labeled column is computed.
   */
  void computeLabeledColumn(RuleSet ruleSet);
 
  /**
   * This is the main method that automatically computes the profile of a registered dataset.
   *
   * @param path - The path to which the profile of the dataset will be stored
   * @return A DatasetProfile object that contains all the analysis results.
   * @throws IOException
   */
  DatasetProfile computeProfileOfDataset()
          throws IOException;

}
