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
   * Declares parameters for dominance highlight pattern identification. This method is required
   * for overall highlight pattern identification to proceed.
   *
   * @param dominanceColumnSelectionMode - Enum which corresponds to the mode
   *                                     based on which columns involved in dominance pattern identification will be selected
   *                                     (e.g. smart mode, exhaustive mode). This parameter is optional. If null is given, smart mode
   *                                     will be picked by default.
   * @param measurementColumns           - Names of specific measurement columns that should be
   *                                     involved in dominance identification.
   * @param coordinateColumns            - Names of specific coordinate columns that should be involved
   *                                     in dominance identification.
   */
 
  /**
   * This is the main method that automatically computes the profile of a registered dataset.
   *
   * @param parameters - Parameters that specify which dataset analysis parts should be executed,
   *                   as well as the directory path where the auxiliary data (e.g. images of the decision trees) will be generated,
   *                   such that they can later be used in the report.
   * @return A DatasetProfile object that contains all the analysis results.
   * @throws IOException
   */
  DatasetProfile computeProfileOfDataset()
          throws IOException;

}
