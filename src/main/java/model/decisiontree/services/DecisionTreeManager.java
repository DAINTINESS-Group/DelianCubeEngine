package model.decisiontree.services;

import model.decisiontree.labeling.Rule;
import model.decisiontree.labeling.RuleSet;
import model.decisiontree.Column;
import model.decisiontree.DatasetProfile;
import model.decisiontree.DecisionTree;
import model.decisiontree.LabeledColumn;
import model.decisiontree.generator.DecisionTreeGeneratorFactory;
import model.decisiontree.input.DecisionTreeParams;
import model.decisiontree.visualization.DecisionTreeVisualizerFactory;
import model.decisiontree.visualization.DecisionTreeVisualizerType;
import model.decisiontree.visualization.IDecisionTreeVisualizer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionTreeManager {

    private final Dataset<Row> dataset;
    private final DatasetProfile datasetProfile;
    private final IDecisionTreeVisualizer decisionTreeVisualizer;
    private final Path outputDirectory;

    public DecisionTreeManager(Dataset<Row> dataset, DatasetProfile datasetProfile) {
        this.dataset = new DecisionTreeOptimizer(dataset).getOptimizedDataset();
        this.datasetProfile = datasetProfile;
        this.decisionTreeVisualizer = new DecisionTreeVisualizerFactory()
                .getVisualizer(DecisionTreeVisualizerType.GRAPH_VIZ);
        this.outputDirectory = Paths.get(datasetProfile.getAuxiliaryDataOutputDirectory(), "decision_trees");
    }

    public List<String> extractAllDecisionTrees() throws IOException {
        List<LabeledColumn> labeledColumns = getLabeledColumns();
        if (labeledColumns.isEmpty()) {
            return new ArrayList<>();
        }
        createDirectory(outputDirectory);
        for (LabeledColumn column : labeledColumns) {
            extractAllDecisionTreesForColumn(column, new ArrayList<>());
        }
        return labeledColumns.stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }

    /**
     * @param allDecisionTreeParams All the user given DecisionTreeParams
     *                              (even multiple for each column).
     */
    public List<String> extractAllDecisionTrees(List<DecisionTreeParams> allDecisionTreeParams) throws IOException {
        List<LabeledColumn> labeledColumns = getLabeledColumns();
        if (labeledColumns.isEmpty()) {
            return new ArrayList<>();
        }
        createDirectory(outputDirectory);
        for (LabeledColumn column : labeledColumns) {
            List<DecisionTreeParams> columnParams = getColumnSpecificParams(column, allDecisionTreeParams);
            extractAllDecisionTreesForColumn(column, columnParams);
        }
        return labeledColumns.stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }

    private List<LabeledColumn> getLabeledColumns() {
        List<LabeledColumn> labeledColumns = new ArrayList<>();
        for (Column column : datasetProfile.getColumns()) {
            if (column instanceof LabeledColumn) {
                labeledColumns.add((LabeledColumn) column);
            }
        }
        return labeledColumns;
    }

    private List<DecisionTreeParams> getColumnSpecificParams(Column column,
                                                             List<DecisionTreeParams> allDecisionTreeParams) {
        List<DecisionTreeParams> labeledColumnDecisionTreeParams = new ArrayList<>();
        for (DecisionTreeParams params : allDecisionTreeParams) {
            if (params.getLabeledColumnName().equals(column.getName())) {
                labeledColumnDecisionTreeParams.add(params);
            }
        }
        return labeledColumnDecisionTreeParams;
    }

    private void extractAllDecisionTreesForColumn(LabeledColumn column,
                                                  List<DecisionTreeParams> allColumnParams) throws IOException {
        if (allColumnParams.isEmpty()) {
            allColumnParams.add(getDefaultDtParams(column.getRuleSet()));
        }
        for (DecisionTreeParams decisionTreeParams : allColumnParams) {
            DecisionTree dt = extractDecisionTree(decisionTreeParams);
            column.addDecisionTree(dt);
        }
        List<DecisionTree> decisionTrees = column.getDecisionTrees();
        for (int i=0; i < decisionTrees.size(); i++) {
            String outputPath = decisionTreeVisualizer.exportDecisionTreeToPNG(decisionTrees.get(i),
                                    outputDirectory.toString(),
                                    String.format("%s_%d", column.getName(), i+1)
            );
            decisionTrees.get(i).setOutputPath(outputPath);
        }
    }

    private DecisionTreeParams getDefaultDtParams(RuleSet ruleSet) {
        List<String> targetColumns = new ArrayList<>();
        for (Rule rule : ruleSet.getRules()) {
            targetColumns.add(rule.getTargetColumnName());
        }
        return new DecisionTreeParams
                .Builder(ruleSet.getNewColumnName(), targetColumns)
                .build();
    }

    private DecisionTree extractDecisionTree(DecisionTreeParams decisionTreeParams) {
        return new DecisionTreeGeneratorFactory(decisionTreeParams, dataset)
                .getDefaultGenerator()
                .computeDecisionTree();
    }

    private void createDirectory(Path path) throws IOException {
        Files.createDirectories(path);
    }
}